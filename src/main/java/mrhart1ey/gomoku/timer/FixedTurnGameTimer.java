package mrhart1ey.gomoku.timer;

public class FixedTurnGameTimer implements GameTimer {
    
    private final Clock clock;
    private final long creationTimeStamp;
    private final long timeUnitsElapsed;
    private final long timeUnitsPerTurn;

    public FixedTurnGameTimer(Clock clock, long timeUnitsPerTurn) {
        this(clock, clock.getTime(), 0, timeUnitsPerTurn);
    }
    
    private FixedTurnGameTimer(Clock clock, long creationTimeStamp, 
            long timeUnitsElapsed, long timeUnitsPerTurn) {
        this.clock = clock;
        this.creationTimeStamp = creationTimeStamp;
        this.timeUnitsElapsed = timeUnitsElapsed;
        this.timeUnitsPerTurn = timeUnitsPerTurn;
    }
    
    @Override
    public GameTimer startTimingMyTurn() {
        return new FixedTurnGameTimer(clock, timeUnitsPerTurn);
    }

    @Override
    public GameTimer endTimingMyTurn() {
        return new FixedTurnGameTimer(clock, timeUnitsPerTurn);
    }
    
    @Override
    public GameTimer tick() {
        long timeUnitsElapsedForNewTimer = clock.getTime() - creationTimeStamp;
        
        return new FixedTurnGameTimer(clock, creationTimeStamp, 
                timeUnitsElapsedForNewTimer, timeUnitsPerTurn);
    }
    
    @Override
    public long getTimeLeft() {
        long result;
        
        long timeDifference = timeUnitsPerTurn - timeUnitsElapsed;
        
        if(timeDifference <= 0) {
            result = 0;
        }else {
            result = timeDifference;
        }
        
        return result;
    }

    @Override
    public boolean didTimeRunOut() {
        return getTimeLeft() == 0;
    }

    @Override
    public boolean isFinite() {
        return true;
    }
    
    public long getTimePerTurn() {
        return timeUnitsPerTurn;
    }
    
}
