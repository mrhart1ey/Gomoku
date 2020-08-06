package mrhart1ey.gomoku.player.network.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class ServerNetworkCommunicator extends AbstractNetworkCommunicator {
    
    private final AsynchronousServerSocketChannel server;
    
    protected ServerNetworkCommunicator(AsynchronousSocketChannel channel, 
            AsynchronousServerSocketChannel server) {
        super(channel);
        
        this.server = server;
    }

    public static NetworkCommunicator waitForClient(int port) 
            throws IOException, InterruptedException, ExecutionException {
        
        AsynchronousServerSocketChannel server
                = AsynchronousServerSocketChannel.open();
        
        server.bind(new InetSocketAddress("localhost", port));

        Future<AsynchronousSocketChannel> futureChannel = server.accept();

        AsynchronousSocketChannel channel = futureChannel.get();

        AbstractNetworkCommunicator instance = 
                new ServerNetworkCommunicator(channel, server);

        instance.start();

        return instance;
    }
    
    @Override
    public void close() {
        try {
            super.close();
            
            server.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
