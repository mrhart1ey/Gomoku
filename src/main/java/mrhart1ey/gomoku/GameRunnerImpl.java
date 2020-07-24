package mrhart1ey.gomoku;

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
import mrhart1ey.gomoku.timer.GameTimer;

public class GameRunnerImpl implements GameRunner {

    private final Gomoku initialboard;
    private final Player me;
    private final Player otherPlayer;
    private final GameTimer initialTimer;
    private final GameMonitor gameMonitor;
    private final TurnIndicator turnIndicator;
    private final GameDisplay display;

    public GameRunnerImpl(Gomoku initialboard, Player me, Player otherPlayer,
            GameTimer initialTimer, GameMonitor gameMonitor,
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

        GameTimer myTimer = initialTimer.startTimingMyTurn();

        GameTimer opponentsTimer = initialTimer.startTimingMyTurn();

        MoveRetriever retriever = new MoveRetriever(me, otherPlayer, board, myName);

        retriever.start();

        while (gameMonitor.shouldKeepPlaying(board, myTimer,
                opponentsTimer)) {

            if (retriever.isAMoveAvailable()) {
                Position move = retriever.retrieveMove();

                if (isMoveValid(board, move)) {
                    if(board.getCurrentTurn() == myName) {
                        myTimer = myTimer.endTimingMyTurn();
                        opponentsTimer = opponentsTimer.startTimingMyTurn();
                    }else {
                        myTimer = myTimer.startTimingMyTurn();
                        opponentsTimer = opponentsTimer.endTimingMyTurn();
                    }
                    
                    board = board.move(move);
                    retriever.moveWasAccepted(board);
                } else {
                    retriever.moveWasNotAccepted();
                }

            }
            
            if(board.getCurrentTurn() == myName) {
                myTimer = myTimer.tick();
            }else {
                opponentsTimer = opponentsTimer.tick();
            }
            

            Position positionMouseIsOver
                    = display.getBoardPositionMouseIsCurrentlyOver();

            List<DrawJob> drawJobs = new ArrayList<>();

            if (turnIndicator.isMyTurn(myName, board.getCurrentTurn())) {
                drawJobs.add(new DrawJobMouseHover(board, positionMouseIsOver));
            }

            if (myTimer.isFinite() || opponentsTimer.isFinite()) {
                drawJobs.add(new DrawJobTime(myTimer.getTimeLeft(),
                        opponentsTimer.getTimeLeft()));
            }

            display.draw(new DrawJobBoard(board), drawJobs);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }

        retriever.stop();

        return board;
    }

    private static boolean isMoveValid(Gomoku board, Position move) {
        return GameUtil.isPositionOnTheBoard(move)
                && !board.getPositionContent(move).isOccupied();
    }
}
