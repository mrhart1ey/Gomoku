package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DrawJobGameResult implements DrawJob {

    private final String resultMessage;

    public DrawJobGameResult(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    @Override
    public void draw(Graphics2D g2) {
        int componentWidth = g2.getClipBounds().width;
        int componentHeight = g2.getClipBounds().height;
        
        g2.setColor(Color.GRAY);

        int textWidth = (int) (componentWidth / 1.2);
        int textHeight = (int) (componentHeight / 7);
        
        int topLeftX = (componentWidth - textWidth) / 2;
        int topLeftY = (componentHeight - textHeight) / 2;
        
        Font font = DrawUtil.findOptimalStandardFont(g2, resultMessage, 
                textWidth, textHeight);
        
        g2.setFont(font);
        
        DrawUtil.drawText(g2, resultMessage, topLeftX, topLeftY, textWidth, 
                textHeight);
    }

}
