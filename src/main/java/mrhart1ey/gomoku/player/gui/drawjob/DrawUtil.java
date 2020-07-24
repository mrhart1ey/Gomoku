package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.gui.BoardGraphicalState;


/**
 * A collection of drawing utility functions 
 */
final class DrawUtil {

    public static final String FONT_NAME_STANDARD = Font.MONOSPACED;
    public static final int FONT_STYLE_STANDARD = Font.PLAIN;

    private DrawUtil() {
    }

    public static void drawText(Graphics2D g2, String text, int topLeftX,
            int topLeftY, int width, int height) {

        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, text);

        int o = gv.getPixelBounds(frc, 0, 0).x;
        int w = gv.getPixelBounds(frc, 0, 0).width;

        int o2 = gv.getPixelBounds(frc, 0, 0).y;
        int h = gv.getPixelBounds(frc, 0, 0).height;

        int widthErrorToMakeUpFor = (int)Math.round((w - width) / 2.0);
        int heightErrorToMakeUpFor = (int)Math.round((height - h) / 2.0)
                - (h + o2);

        g2.drawString(text,
                topLeftX - o - widthErrorToMakeUpFor,
                topLeftY + h + heightErrorToMakeUpFor);
    }

    public static void fillCircle(Graphics2D g2, int centerX, int centerY, int radius) {
        int width = (int) ((2 * radius) / Math.sqrt(2.0));
        int height = width;

        int upperLeftX = centerX - (int) (0.5 * width);
        int upperLeftY = centerY - (int) (0.5 * height);

        g2.fillOval(upperLeftX, upperLeftY, width, height);
    }

    public static void drawBlackPiece(Graphics2D g2, int centerX, int centerY, int radius) {
        drawPiece(g2, centerX, centerY, radius, new Color(28, 28, 28));
    }

    public static void drawWhitePiece(Graphics2D g2, int centerX, int centerY, int radius) {
        drawPiece(g2, centerX, centerY, radius, new Color(219, 219, 219));
    }

    /**
     * 
     * @param g2 The graphics environment
     * @param fontName The font family of the font to return
     * @param fontStyle The font style of the font to return
     * @param texts The text to fonts for
     * @param boundingWidth The width that each bit of text should fit into
     * @param boundingHeightThe height that each bit of text should fit into
     * @return A font that has fontName and fontStyle, and allows all of the 
     * passed in text to fit within the bounding rectangle at the same font
     */
    public static Font largestFontSoAllTextsFitInBound(Graphics2D g2,
            String fontName, int fontStyle, Collection<String> texts,
            int boundingWidth, int boundingHeight) {

        Font best = null;

        for (String text : texts) {
            Font current = findOptimalFont(g2, fontName, fontStyle, text,
                    boundingWidth, boundingHeight);

            if (best == null || current.getSize() < best.getSize()) {
                best = current;
            }
        }

        return best;
    }

    private static void drawPiece(Graphics2D g2, int centerX, int centerY,
            int radius, Color color) {
        Point radialCenter = new Point((int) (centerX - radius * 0.9),
                (int) (centerY - radius * 0.9));
        float radialRadius = radius * 1.2f + 0.01f;

        float[] dist = {
            0f,
            1f
        };

        Color[] colors = {
            new Color(255, 255, 255),
            color
        };

        RadialGradientPaint rgp = new RadialGradientPaint(radialCenter,
                radialRadius, dist, colors);

        Paint oldPaint = g2.getPaint();

        g2.setPaint(rgp);

        int width = (int) ((2 * radius) / Math.sqrt(2.0));
        int height = width;
        
        int upperLeftX = centerX - (int) (0.5 * width);
        int upperLeftY = centerY - (int) (0.5 * height);

        g2.fill(new Ellipse2D.Double(upperLeftX, upperLeftY, width, height));

        g2.setPaint(oldPaint);
    }

    public static Point findOnScreenPieceLocation(BoardGraphicalState state,
            Position boardPosition) {

        int circleCenterX = state.boardHorizontalOffset
                + (int) (state.verticalBarSeperation * boardPosition.column);

        int circleCenterY = state.boardVerticalOffset
                + (int) (state.horizontalBarSeperation * boardPosition.row);

        return new Point(circleCenterX, circleCenterY);
    }

    public static Font findOptimalStandardFont(Graphics2D g2, String text,
            int boundingWidth, int boundingHeight) {
        return findOptimalFont(g2, FONT_NAME_STANDARD, FONT_STYLE_STANDARD,
                text, boundingWidth, boundingHeight);
    }

    public static Font largestStandardFontSoAllTextsFitInBound(Graphics2D g2, 
            Collection<String> texts, int boundingWidth, int boundingHeight) {
        return largestFontSoAllTextsFitInBound(g2, FONT_NAME_STANDARD, 
                FONT_STYLE_STANDARD, texts, boundingWidth, boundingHeight);
    }

    /**
     * 
     * @param g2 The graphics environment
     * @param fontName The font family of the font to return
     * @param fontStyle The font style of the font to return
     * @param text The text to find the font for
     * @param boundingWidth The width that the text should fit into
     * @param boundingHeightThe height that the text should fit into
     * @return A font that has fontName and fontStyle, and is the largest it 
     * can be while the text fits into the bounding rectanglre
     */
    public static Font findOptimalFont(Graphics2D g2, String fontName,
            int fontStyle, String text, int boundingWidth, int boundingHeight) {
        int currentFontSize = 0;

        int textWidth;
        int textHeight;

        Font oldFont = g2.getFont();

        do {
            currentFontSize++;

            Font f = new Font(fontName, fontStyle, currentFontSize);

            g2.setFont(f);

            FontRenderContext frc = g2.getFontRenderContext();

            GlyphVector gv = f.createGlyphVector(frc, text);

            textWidth = gv.getPixelBounds(null, 0, 0).width;
            textHeight = gv.getPixelBounds(null, 0, 0).height;
        } while (textWidth < boundingWidth && textHeight < boundingHeight);

        g2.setFont(oldFont);

        return new Font(fontName, fontStyle, currentFontSize);
    }
}
