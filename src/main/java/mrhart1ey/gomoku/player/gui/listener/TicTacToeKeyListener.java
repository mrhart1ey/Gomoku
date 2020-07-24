package mrhart1ey.gomoku.player.gui.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;

public class TicTacToeKeyListener extends KeyAdapter {

    private final BlockingQueue<Boolean> newGameIndicator;

    public TicTacToeKeyListener(BlockingQueue<Boolean> newGameIndicator) {
        this.newGameIndicator = newGameIndicator;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        try {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    newGameIndicator.put(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    newGameIndicator.put(false);
                    break;
            }
        } catch (InterruptedException ex) {

        }
    }

}
