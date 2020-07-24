package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DrawJobOpponentLeft implements DrawJob {

    private final String menuKey;

    public DrawJobOpponentLeft(String menuKey) {
        this.menuKey = menuKey;
    }

    @Override
    public void draw(Graphics2D g2) {
        String message = "Your opponent left. Press " + menuKey + 
                " to return to the menu.";

        int componentWidth = g2.getClipBounds().width;
        int componentHeight = g2.getClipBounds().height;

        int textWidth = (int) (componentWidth / 1.1);
        int textHeight = (int) (componentHeight / 8);

        int topLeftX = (componentWidth - textWidth) / 2;
        int topLeftY = ((componentHeight - textHeight) / 2)
                + (componentHeight / 7);

        g2.setColor(Color.GRAY);

        Font font = DrawUtil.findOptimalStandardFont(g2, message,
                textWidth, textHeight);

        g2.setFont(font);

        DrawUtil.drawText(g2, message, topLeftX, topLeftY,
                textWidth, textHeight);
    }

}
