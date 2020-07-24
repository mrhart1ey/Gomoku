package mrhart1ey.gomoku.timer;

public interface GameTimer {
    public GameTimer startTimingMyTurn();
    
    public GameTimer endTimingMyTurn();
    
    public GameTimer tick();
    
    public long getTimeLeft();
    
    public boolean didTimeRunOut();
    
    public boolean isFinite();
}
