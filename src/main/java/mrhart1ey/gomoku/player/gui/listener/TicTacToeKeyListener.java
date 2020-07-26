package mrhart1ey.gomoku.player.gui.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import mrhart1ey.gomoku.SingleTimePasser;

public class TicTacToeKeyListener extends KeyAdapter {

    private final SingleTimePasser<Boolean> newGameIndicator;

    public TicTacToeKeyListener(SingleTimePasser<Boolean> newGameIndicator) {
        this.newGameIndicator = newGameIndicator;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                newGameIndicator.put(true);
                break;
            case KeyEvent.VK_ESCAPE:
                newGameIndicator.put(false);
                break;
        }
    }

}
