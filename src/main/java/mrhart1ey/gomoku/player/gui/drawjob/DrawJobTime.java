package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

public class DrawJobTime implements DrawJob {

    private final long myTimeLeft;
    private final long opponentsTimeLeft;

    public DrawJobTime(long myTimeLeft, long opponentsTimeLeft) {
        this.myTimeLeft = myTimeLeft;
        this.opponentsTimeLeft = opponentsTimeLeft;
    }

    @Override
    public void draw(Graphics2D g2) {
        Rectangle bounds = g2.getClipBounds();

        String myTimeLeftText = Long.toString(myTimeLeft / 1000);
        String opponentsTimeLeftText = Long.toString(opponentsTimeLeft / 1000);

        Collection<String> texts
                = List.of(myTimeLeftText, opponentsTimeLeftText);

        int textWidth = (int) (bounds.getWidth() / 19);
        int textHeight = (int) (bounds.getHeight() / 22);

        Font font
                = DrawUtil.largestStandardFontSoAllTextsFitInBound(g2, texts,
                        textWidth, textHeight);

        g2.setFont(font);

        int topLeftY = 4;

        int myTimeLeftTopLeftX = 4;

        int opponentsTimeLeftTopLeftX = bounds.width - textWidth - 4;

        DrawUtil.drawText(g2, myTimeLeftText,
                myTimeLeftTopLeftX, topLeftY,
                textWidth, textHeight);

        DrawUtil.drawText(g2, opponentsTimeLeftText,
                opponentsTimeLeftTopLeftX, topLeftY,
                textWidth, textHeight);
    }
}
