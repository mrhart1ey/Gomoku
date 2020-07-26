package mrhart1ey.gomoku;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import mrhart1ey.gomoku.game.GameUtil;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.player.gui.GameDisplay;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJob;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobBoard;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobMouseHover;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobTime;
import mrhart1ey.gomoku.timer.ActivatedGameTimer;
import mrhart1ey.gomoku.timer.DeactivatedGameTimer;
import mrhart1ey.gomoku.timer.GameTimer;

public class GameRunnerImpl implements GameRunner {

    private final Gomoku initialboard;
    private final Player me;
    private final Player otherPlayer;
    private final DeactivatedGameTimer initialTimer;
    private final GameMonitor gameMonitor;
    private final TurnIndicator turnIndicator;
    private final GameDisplay display;

    public GameRunnerImpl(Gomoku initialboard, Player me, Player otherPlayer,
            DeactivatedGameTimer initialTimer, GameMonitor gameMonitor,
            TurnIndicator turnIndicator, GameDisplay display) {

        this.initialboard = initialboard;
        this.me = me;
        this.otherPlayer = otherPlayer;
        this.initialTimer = initialTimer;
        this.gameMonitor = gameMonitor;
        this.turnIndicator = turnIndicator;

        this.display = display;
    }

    @Override
    public Gomoku runGame(PlayerName myName) {
        Gomoku board = initialboard;

        Clock clock = Clock.systemUTC();

        Instant currentInstant = clock.instant();

        // For the player whose turn it currently is
        ActivatedGameTimer activeTimer
                = initialTimer.startTimingTurn(currentInstant);

        // For the player who is not taking their turn
        DeactivatedGameTimer dormantTimer = initialTimer;

        MoveRetriever retriever = new MoveRetriever(me, otherPlayer, board, myName);

        retriever.start();

        while (gameMonitor.shouldKeepPlaying(board, activeTimer.didTimeRunOut(currentInstant),
                dormantTimer.didTimeRunOut(currentInstant))) {

            if (retriever.isAMoveAvailable()) {
                Position move = retriever.retrieveMove();

                if (isMoveValid(board, move)) {
                    DeactivatedGameTimer newDormantTimer
                            = activeTimer.stopTimingTurn(currentInstant);

                    activeTimer = dormantTimer.startTimingTurn(currentInstant);

                    dormantTimer = newDormantTimer;

                    board = board.move(move);
                    retriever.moveWasAccepted(board);
                } else {
                    retriever.moveWasNotAccepted();
                }

            }

            Position positionMouseIsOver
                    = display.getBoardPositionMouseIsCurrentlyOver();

            List<DrawJob> drawJobs = new ArrayList<>();

            if (turnIndicator.isMyTurn(myName, board.getCurrentTurn())) {
                drawJobs.add(new DrawJobMouseHover(board, positionMouseIsOver));
            }

            if (activeTimer.isFinite() || dormantTimer.isFinite()) {
                boolean myTurn = board.getCurrentTurn() == myName;
                
                DrawJob drawJobTimer = 
                        newDrawJobTime(activeTimer.getTimeLeft(currentInstant),
                                dormantTimer.getTimeLeft(currentInstant),
                                myTurn);

                drawJobs.add(drawJobTimer);
            }

            display.draw(new DrawJobBoard(board), drawJobs);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }

            currentInstant = clock.instant();
        }

        retriever.stop();

        return board;
    }

    private static boolean isMoveValid(Gomoku board, Position move) {
        return GameUtil.isPositionOnTheBoard(move)
                && !board.getPositionContent(move).isOccupied();
    }

    private static DrawJob newDrawJobTime(Duration activeTimerTimeLeft,
            Duration dormantTimerTimeLeft, boolean myTurn) {
        Duration myTimeLeft;
        Duration opponentsTimeLeft;

        if (myTurn) {
            myTimeLeft = activeTimerTimeLeft;
            opponentsTimeLeft = dormantTimerTimeLeft;
        } else {
            myTimeLeft = dormantTimerTimeLeft;
            opponentsTimeLeft = activeTimerTimeLeft;
        }

        return new DrawJobTime(myTimeLeft, opponentsTimeLeft);
    }
}
