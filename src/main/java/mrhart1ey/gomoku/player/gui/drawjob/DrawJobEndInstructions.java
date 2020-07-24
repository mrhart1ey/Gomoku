package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DrawJobEndInstructions implements DrawJob {

    private final String newGameKey;
    private final String menuKey;

    public DrawJobEndInstructions(String newGameKey, String menuKey) {
        this.newGameKey = newGameKey;
        this.menuKey = menuKey;
    }

    @Override
    public void draw(Graphics2D g2) {
        String message = "Press " + newGameKey + " to continue or "
                + menuKey + " to go back to the menu";

        int componentWidth = g2.getClipBounds().width;
        int componentHeight = g2.getClipBounds().height;
        
        int textWidth = (int) (componentWidth / 1.1);
        int textHeight = (int) (componentHeight / 8);
        
        int topLeftX = (componentWidth - textWidth) / 2;
        int topLeftY = ((componentHeight - textHeight) / 2) 
                + (componentHeight / 7);
        
        g2.setColor(Color.gray);
        
        Font font = DrawUtil.findOptimalStandardFont(g2, message, 
                textWidth, textHeight);
        
        g2.setFont(font);
        
        DrawUtil.drawText(g2, message, topLeftX, topLeftY, 
                textWidth, textHeight);
    }
}
