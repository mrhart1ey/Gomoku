package mrhart1ey.gomoku.player.network.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.game.Position;

abstract class AbstractNetworkCommunicator implements NetworkCommunicator {

    private static final String MESSAGE_SEPERATOR = ";";
    private static final int MESSAGE_CONTENT = 1;

    private static final String MESSAGE_GAME_CONFIGURATION = "GAME_CONFIGURATION";
    private static final String MESSAGE_POSITION = "POSITION";
    private static final String MESSAGE_NEW_GAME = "NEW_GAME";

    private static final String COORDINATE_SEPERATOR = ",";
    private static final int COMPONENT_ROW_INDEX = 0;
    private static final int COMPONENT_COLUMN_INDEX = 1;

    private final AsynchronousSocketChannel channel;

    private final BlockingDeque<String> messages;

    private final Thread messageReader;
    
    private final GameConfigurationToString gameConfigurationToString;
    
    private final StringToGameConfiguration stringToGameConfiguration;

    protected AbstractNetworkCommunicator(AsynchronousSocketChannel channel) {
        this.channel = channel;

        messages = new LinkedBlockingDeque<>();

        messageReader = new Thread(new MessageReader(channel, messages));
        
        gameConfigurationToString = new GameConfigurationToString();
        
        stringToGameConfiguration = new StringToGameConfiguration();
    }

    protected void start() {
        messageReader.start();
    }

    @Override
    public void close() {
        messageReader.interrupt();
        
        try {
            channel.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void sendGameConfiguration(GameConfiguration gameConfiguration) {
        String message
                = MESSAGE_GAME_CONFIGURATION + MESSAGE_SEPERATOR
                + gameConfigurationToString.convert(gameConfiguration);

        write(message);
    }

    @Override
    public GameConfiguration readGameConfiguration() throws InterruptedException {

        String message = messages.take();

        String[] parts = message.split(MESSAGE_SEPERATOR);

        String gameConfigurationString = parts[MESSAGE_CONTENT];

        return stringToGameConfiguration.convert(gameConfigurationString);
    }

    @Override
    public void sendMove(Position move) {
        String message
                = MESSAGE_POSITION + MESSAGE_SEPERATOR + stringFromPosition(move);

        write(message);
    }

    @Override
    public Position readMove() throws InterruptedException{
        
        String message = messages.take();

        String[] parts = message.split(MESSAGE_SEPERATOR);

        String moveString = parts[MESSAGE_CONTENT];

        return positionFromString(moveString);
    }

    @Override
    public void indicateIfIWantANewGame(boolean newGame) {
        
        String message
                = MESSAGE_NEW_GAME + MESSAGE_SEPERATOR + Boolean.toString(newGame);

        write(message);
    }

    @Override
    public boolean doesOpponentWantANewGame() throws InterruptedException {
        
        String message = messages.take();

        String[] parts = message.split(MESSAGE_SEPERATOR);

        String newGameString = parts[MESSAGE_CONTENT];

        return Boolean.valueOf(newGameString);
    }

    @Override
    public boolean isOpponentActive() {
        return messageReader.getState() != Thread.State.TERMINATED;
    }

    private static Position positionFromString(String input) {
        String[] components = input.split(COORDINATE_SEPERATOR);

        int row = Integer.parseInt(components[COMPONENT_ROW_INDEX]);
        int column = Integer.parseInt(components[COMPONENT_COLUMN_INDEX]);

        return new Position(row, column);
    }

    private static String stringFromPosition(Position pos) {
        return pos.row + COORDINATE_SEPERATOR + pos.column;
    }

    private void write(String message) {
        byte[] bytes = message.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(intToBytes(bytes.length));
        buffer.flip();
        channel.write(buffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);

        byteBuffer.put(bytes);

        byteBuffer.flip();

        channel.write(byteBuffer);
    }

    private static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(0, x);
        return buffer.array();
    }
}
