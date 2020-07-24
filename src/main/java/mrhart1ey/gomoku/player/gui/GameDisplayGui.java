package mrhart1ey.gomoku.player.gui;

import mrhart1ey.gomoku.player.gui.GameDisplay;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JFrame;
import mrhart1ey.gomoku.MultiTimePasser;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.player.gui.listener.TicTacToeKeyListener;
import mrhart1ey.gomoku.player.gui.listener.TicTacToeMouseListener;
import mrhart1ey.gomoku.player.gui.listener.TicTacToeResizeListener;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJob;

/**
 * A game display that uses swing to draw the board to the screen
 */
public class GameDisplayGui implements GameDisplay {

    public static final int INITIAL_WIDTH = 640;
    public static final int INITIAL_HEIGHT = 480;

    private final JFrame frame;
    private final TicTacToeComponent game;
    private final SingleTimePasser<Position> movePasser;
    private final MultiTimePasser<Position> positionMouseIsOver;
    private final BlockingQueue<Boolean> newGameIndicator;

    public GameDisplayGui() {
        frame = new JFrame("Gomoku");

        frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);

        MultiTimePasser<Dimension> passer
                = new MultiTimePasser<>(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        positionMouseIsOver
                = new MultiTimePasser<>(null);

        game = new TicTacToeComponent();

        movePasser = new SingleTimePasser<>();
        newGameIndicator = new LinkedBlockingQueue<>();

        MouseAdapter mouseListener = new TicTacToeMouseListener(movePasser,
                passer, positionMouseIsOver);

        game.addMouseListener(mouseListener);
        game.addMouseMotionListener(mouseListener);
        game.addComponentListener(new TicTacToeResizeListener(passer));
        game.addKeyListener(new TicTacToeKeyListener(newGameIndicator));

        frame.add(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        game.setFocusable(true);
        frame.setFocusable(false);
        game.requestFocus();
    }

    @Override
    public void draw(DrawJob board, List<DrawJob> widgets) {
        game.update(board, widgets);
        game.repaint();
    }

    @Override
    public Position getBoardPositionMouseIsCurrentlyOver() {
        return positionMouseIsOver.get();
    }

    public void show() {
        if (!frame.isVisible()) {
            frame.setVisible(true);
        }
    }

    public void stopShowing() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * The method will block while it waits for a response from the user
     *
     * @return True if the player wants a new game, false otherwise
     */
    public boolean doesPlayerWantANewGame() {
        boolean result = false;

        try {
            result = newGameIndicator.take();
        } catch (InterruptedException ex) {

        }

        return result;
    }

    /**
     * @return An instance of a player that takes its moves from the mouse
     * clicking in the game frame
     */
    public Player getMe() {
        return new Me(movePasser);
    }

    @Override
    public boolean hasPlayerAnsweredIfTheyWantANewGame() {
        return !newGameIndicator.isEmpty();
    }

    private static class Me implements Player {

        private final SingleTimePasser<Position> movePasser;

        public Me(SingleTimePasser<Position> movePasser) {
            this.movePasser = movePasser;
        }

        @Override
        public Position nextMove(Gomoku board) {
            Position move = null;

            try {
                if (movePasser.isObjectWaiting()) {
                    movePasser.take();
                }

                move = movePasser.take();
            } catch (InterruptedException ex) {

            }

            return move;
        }

    }

}
