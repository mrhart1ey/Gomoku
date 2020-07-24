package mrhart1ey.gomoku.game;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of helper functions for dealing with the game board
 */
public final class GameUtil {

    private GameUtil() {
    }

    public static boolean isPositionOnTheBoard(Position position) {
        return position.row >= 0 && position.row < Gomoku.SIZE
                && position.column >= 0 && position.column < Gomoku.SIZE;
    }

    /**
     * For a given piece there will be 4 channels, the horizontal, vertical, 
     * left diagonal, and right diagonal. The channel is a list of position 
     * content that resides in one of the 4 lines mentioned above.
     * 
     * @param piece A piece on the board
     * @return The 4 channels that the piece is in
     */
    public static List<List<Position>> extractChannelsPieceIsIn(Position piece) {
        List<List<Position>> channels = new ArrayList<>();

        channels.add(getRow(piece));

        channels.add(getColumn(piece));

        channels.add(getLeftDiagonal(piece));

        channels.add(getRightDiagonal(piece));

        return channels;
    }
    
    /**
     * @param board The board that has a victory line
     * @throws IllegalArgumentException if there is no victory line on the board
     * @return The consecutive pieces that have won the game
     */
    public static List<Position> getVictoryLine(Gomoku board) {
        for (int row = 0; row < Gomoku.SIZE; row++) {
            for (int column = 0; column < Gomoku.SIZE; column++) {
                Position position = new Position(row, column);

                List<Position> eastLine
                        = findMaxConsecutiveSameColoredPieces(board, position,
                                Directions.EAST);

                if (eastLine.size() == Gomoku.CONSECUTIVE_PIECES_TO_WIN) {
                    return eastLine;
                }

                List<Position> southEastLine
                        = findMaxConsecutiveSameColoredPieces(board, position,
                                Directions.SOUTH_EAST);

                if (southEastLine.size() == Gomoku.CONSECUTIVE_PIECES_TO_WIN) {
                    return southEastLine;
                }

                List<Position> southLine
                        = findMaxConsecutiveSameColoredPieces(board, position,
                                Directions.SOUTH);

                if (southLine.size() == Gomoku.CONSECUTIVE_PIECES_TO_WIN) {
                    return southLine;
                }

                List<Position> southWestLine
                        = findMaxConsecutiveSameColoredPieces(board, position,
                                Directions.SOUTH_WEST);

                if (southWestLine.size() == Gomoku.CONSECUTIVE_PIECES_TO_WIN) {
                    return southWestLine;
                }
            }
        }

        throw new IllegalArgumentException("The game has not beeen won.");
    }

    private static List<Position> findMaxConsecutiveSameColoredPieces(
            Gomoku board, Position start, Direction direction) {
        List<Position> result = new ArrayList<>();

        Position next = start;

        PositionContent startContent = board.getPositionContent(start);

        while (startContent != PositionContent.FREE
                && isPositionOnTheBoard(next)
                && startContent == board.getPositionContent(next)
                && result.size() < Gomoku.CONSECUTIVE_PIECES_TO_WIN) {
            result.add(next);
            next = direction.apply(next);
        }

        return result;
    }

    private static List<Position> getRow(Position piece) {
        return normaliseChannel(new Position(piece.row, 0),
                Directions.EAST);
    }

    private static List<Position> getColumn(Position piece) {
        return normaliseChannel(new Position(0, piece.column),
                Directions.SOUTH);
    }

    private static List<Position> getLeftDiagonal(Position piece) {
        int amountToMove = Math.min(piece.row, piece.column);

        return normaliseChannel(new Position(piece.row - amountToMove,
                piece.column - amountToMove),
                Directions.SOUTH_EAST);
    }

    private static List<Position> getRightDiagonal(Position piece) {
        int amountToMove = Math.min(piece.row, 
                Gomoku.SIZE - 1 - piece.column);
        
        return normaliseChannel(new Position(piece.row - amountToMove,
                piece.column + amountToMove),
                Directions.SOUTH_WEST);
    }
    
    
    private static List<Position> normaliseChannel(Position start, 
            Direction direction) {
        List<Position> result = new ArrayList<>();

        Position current = start;

        while (GameUtil.isPositionOnTheBoard(current)) {

            result.add(current);

            current = direction.apply(current);
        }

        return result;
    }

}
