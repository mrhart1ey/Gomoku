package mrhart1ey.gomoku.player.network;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import mrhart1ey.gomoku.game.Position;

/**
 * A collection of methods to help with networking
 */
final class NetworkUtil {

    static final int PORT = 22222;
    static final int ACTIVITY_PORT = 22223;
    static final int NEW_GAME_PORT = 22224;
    static final String COORDINATE_SEPERATOR = ",";
    static final int COMPONENT_ROW_INDEX = 0;
    static final int COMPONENT_COLUMN_INDEX = 1;
    
    static final String REQUEST_POSITION = "POSITION";
    static final String ACK = "ACK";
    static final String ANOTHER_GAME = "TRUE";
    static final String NOT_ANOTHER_GAME = "FALSE";
    
    

    private NetworkUtil() {
    }

    static Position positionFromString(String input) {
        String[] components = input.split(COORDINATE_SEPERATOR);

        int row = Integer.parseInt(components[COMPONENT_ROW_INDEX]);
        int column = Integer.parseInt(components[COMPONENT_COLUMN_INDEX]);

        return new Position(row, column);
    }

    static String stringFromPosition(Position pos) {
        return pos.row + COORDINATE_SEPERATOR + pos.column;
    }

    static void write(AsynchronousByteChannel out, String message) 
            throws InterruptedException, ExecutionException {
        byte[] bytes = message.getBytes();
        
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(intToBytes(bytes.length));
        buffer.flip();
        out.write(buffer);
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        
        byteBuffer.put(bytes);
        
        byteBuffer.flip();
        
        out.write(byteBuffer);
    }
    
    static String read(AsynchronousByteChannel in) 
            throws InterruptedException, ExecutionException {
        ByteBuffer bytes = ByteBuffer.allocate(Integer.BYTES);
        
        Future<Integer> readResult = in.read(bytes);
        
        readResult.get();
        
        int messageSize = bytesToInt(bytes.array());
        
        bytes = ByteBuffer.allocate(messageSize);
        
        readResult = in.read(bytes);
        
        readResult.get();
        
        return new String(bytes.array()).trim();
    }

    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(0, x);
        return buffer.array();
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getInt();
    }
}
