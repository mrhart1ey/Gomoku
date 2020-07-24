package mrhart1ey.gomoku.timer;

public class InfiniteGameTimer implements GameTimer {

    @Override
    public GameTimer startTimingMyTurn() {
        return new InfiniteGameTimer();
    }

    @Override
    public GameTimer endTimingMyTurn() {
        return new InfiniteGameTimer();
    }

    @Override
    public GameTimer tick() {
        return new InfiniteGameTimer();
    }

    @Override
    public long getTimeLeft() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean didTimeRunOut() {
        return false;
    }

    @Override
    public boolean isFinite() {
        return false;
    }

}
