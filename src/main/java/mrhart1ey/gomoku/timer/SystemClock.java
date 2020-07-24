package mrhart1ey.gomoku.timer;

public final class SystemClock implements Clock {

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }
    
}
