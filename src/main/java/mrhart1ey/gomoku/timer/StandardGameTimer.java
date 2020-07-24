package mrhart1ey.gomoku.timer;

public class StandardGameTimer implements GameTimer {

    private final Clock clock;

    private final long creationTimeStamp;

    private final long timeUnitsLeft;

    private final long initialReserveTime;
    private final long timeUnitsAddedPerTurn;

    public StandardGameTimer(Clock clock, long reserveTimeUnits,
            long timeUnitsAddedPerTurn) {

        this(clock, clock.getTime(), reserveTimeUnits,
                reserveTimeUnits, timeUnitsAddedPerTurn);
    }

    private StandardGameTimer(Clock clock, long creationTimeStamp,
            long timeUnitsLeft,
            long initialReserveTime, long timeUnitsAddedPerTurn) {

        this.clock = clock;
        this.creationTimeStamp = creationTimeStamp;

        this.timeUnitsLeft = timeUnitsLeft;

        this.initialReserveTime = initialReserveTime;
        this.timeUnitsAddedPerTurn = timeUnitsAddedPerTurn;
    }

    @Override
    public GameTimer startTimingMyTurn() {
        return new StandardGameTimer(clock, clock.getTime(),
                timeUnitsLeft, initialReserveTime, timeUnitsAddedPerTurn);
    }

    @Override
    public GameTimer endTimingMyTurn() {
        long timeUnitsLeftFoprNewTimer = timeUnitsLeft + timeUnitsAddedPerTurn;

        return new StandardGameTimer(clock, creationTimeStamp,
                timeUnitsLeftFoprNewTimer, initialReserveTime,
                timeUnitsAddedPerTurn);
    }

    @Override
    public GameTimer tick() {
        long timeUnitsLeftFoprNewTimer = timeUnitsLeft - (clock.getTime()
                - creationTimeStamp);

        if(timeUnitsLeftFoprNewTimer < 0) {
            timeUnitsLeftFoprNewTimer = 0;
        }
        
        return new StandardGameTimer(clock, clock.getTime(),
                timeUnitsLeftFoprNewTimer, initialReserveTime,
                timeUnitsAddedPerTurn);
    }

    @Override
    public long getTimeLeft() {
        return timeUnitsLeft;
    }

    @Override
    public boolean didTimeRunOut() {
        return getTimeLeft() == 0;
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    public long getInitialReserveTime() {
        return initialReserveTime;
    }

    public long getTimeAddedPerTurn() {
        return timeUnitsAddedPerTurn;
    }

}
