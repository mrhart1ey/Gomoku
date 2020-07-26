package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class DrawJobTime implements DrawJob {

    private final Duration myTimeLeft;
    private final Duration opponentsTimeLeft;

    public DrawJobTime(Duration myTimeLeft, Duration opponentsTimeLeft) {
        this.myTimeLeft = myTimeLeft;
        this.opponentsTimeLeft = opponentsTimeLeft;
    }

    @Override
    public void draw(Graphics2D g2) {
        Rectangle bounds = g2.getClipBounds();

        String myTimeLeftText = Long.toString(myTimeLeft.toSeconds());
        String opponentsTimeLeftText = Long.toString(opponentsTimeLeft.toSeconds());

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
