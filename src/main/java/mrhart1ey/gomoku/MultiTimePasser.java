package mrhart1ey.gomoku;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides a way to atomically pass an object around without it being taken 
 * on reading
 * 
 * @param <T> The type to pass
 */
public class MultiTimePasser<T> {
    private final AtomicReference<T> impl;
    
    public MultiTimePasser(T initialValue) {
        impl = new AtomicReference<>(initialValue);
    }
    
    public void set(T newValue) {
        impl.set(newValue);
    }
    
    public T get() {
        return impl.get();
    }
}
