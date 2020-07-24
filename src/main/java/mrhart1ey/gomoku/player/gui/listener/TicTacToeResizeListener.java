package mrhart1ey.gomoku.player.gui.listener;

import mrhart1ey.gomoku.MultiTimePasser;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TicTacToeResizeListener extends ComponentAdapter {
    private final MultiTimePasser<Dimension> passer;
    
    public TicTacToeResizeListener(MultiTimePasser<Dimension> passer) {
        this.passer = passer;
    }
    
    @Override
    public void componentResized(ComponentEvent event) {
        Dimension componentSize =  event.getComponent().getSize();
        
        passer.set(new Dimension(componentSize));
    }
}
