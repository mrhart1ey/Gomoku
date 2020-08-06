package mrhart1ey.gomoku.player.network.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class ClientNetworkCommunicator extends AbstractNetworkCommunicator {

    protected ClientNetworkCommunicator(AsynchronousSocketChannel channel) {
        super(channel);
    }

    public static NetworkCommunicator connectToServer(String address, int port) 
            throws IOException, InterruptedException, ExecutionException {
        
        AsynchronousSocketChannel channel
                = AsynchronousSocketChannel.open();

        Future<Void> connectionWaiter
                = channel.connect(new InetSocketAddress(address,
                        port));

        connectionWaiter.get();

        AbstractNetworkCommunicator instance = new ClientNetworkCommunicator(channel);

        instance.start();

        return instance;
    }
}
