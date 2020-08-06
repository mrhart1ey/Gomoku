package mrhart1ey.gomoku.player.network.protocol;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class MessageReader implements Runnable {

    private final AsynchronousSocketChannel channel;

    private final BlockingDeque<String> messages;

    public MessageReader(AsynchronousSocketChannel channel,
            BlockingDeque<String> messages) {
        this.channel = channel;

        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                String message = read();

                if (message.isEmpty()) {
                    break;
                }

                messages.put(message);
            }
        } catch (InterruptedException ex) {

        }
    }

    public String read() throws InterruptedException {
        try {
            ByteBuffer bytes = ByteBuffer.allocate(Integer.BYTES);

            Future<Integer> readResult = channel.read(bytes);

            readResult.get();

            int messageSize = bytesToInt(bytes.array());

            bytes = ByteBuffer.allocate(messageSize);

            readResult = channel.read(bytes);

            readResult.get();

            return new String(bytes.array()).trim();
        } catch (ExecutionException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getInt();
    }
}
