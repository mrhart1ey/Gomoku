package mrhart1ey.gomoku;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Provides a way to atomically pass an object around, when the object is read 
 * it will be taken and not be able to be read again unless it is put back in. 
 * If no object is in the passer on a request to take an object, it will block
 * 
 * @param <T> The type to pass
 */
public class SingleTimePasser<T> {
    private final BlockingQueue<T> impl;
    
    public SingleTimePasser() {
        impl = new LinkedBlockingQueue<>();
    }
    
    /**
     * @param objectToPass The object to put in. If an object is already in the
     *  passer then it will be removed and the new object will be put in its place
     */
    public synchronized void put(T objectToPass) {
        impl.clear();
        
        try {
            impl.put(objectToPass);
        } catch (InterruptedException ex) {
        }
    }
    
    /**
     * @return True if an object can be taken, false otherwise
     */
    public boolean isObjectWaiting() {
        return !impl.isEmpty();
    }
    
    /**
     * The method will block if no object is in the passer to take
     * 
     * @return The object in the passer
     * @throws InterruptedException 
     */
    public T take() throws InterruptedException {
        return impl.take();
    }
}
