package mrhart1ey.gomoku.game;

import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the Gomoku board game.
 */
public final class GomokuImpl implements Gomoku {

    private final PositionContent[][] board;

    private final PlayerName currentTurn;

    private final GameState state;

    // The amount of pieces currenly on the board
    private final int pieceCount;

    /**
     * Creates a blank Gomoku board.
     */
    public GomokuImpl() {
        board = new PositionContent[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                board[row][column] = PositionContent.FREE;
            }
        }

        currentTurn = Gomoku.INITIAL_TURN;

        state = GameState.ONGOING;

        pieceCount = 0;
    }

    private GomokuImpl(GomokuImpl boardToCopy, Position move) {
        board = new PositionContent[SIZE][];

        /*
        Does an efficant copy. Only the row which is going to have the new piece
        added needs to be fully copied, for the other rows a reference to their
        array can just be copied. 
         */
        for (int row = 0; row < SIZE; row++) {
            if (move.row != row) {
                board[row] = boardToCopy.board[row];
            } else {
                board[row] = new PositionContent[SIZE];

                System.arraycopy(boardToCopy.board[row], 0,
                        board[row], 0, Gomoku.SIZE);
            }
        }

        PlayerName playersMove = boardToCopy.getCurrentTurn();

        board[move.row][move.column] = playersMove.getPieceToPlace();

        currentTurn = playersMove.turnAfter();

        pieceCount = boardToCopy.pieceCount + 1;

        if (hasGameBeenWon(move)) {
            state = GameState.VICTORY;
        } else if (!canAnymorePiecesBeAdded(pieceCount)) {
            // If the game has not been won and no more piece can be added, it is a draw
            state = GameState.DRAW;
        } else {
            state = GameState.ONGOING;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If the passed in position is not on the
     * board
     *
     * @param pos {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public PositionContent getPositionContent(Position pos) {
        if (!GameUtil.isPositionOnTheBoard(pos)) {
            throw new IllegalArgumentException("Out of bounds position.");
        }

        return board[pos.row][pos.column];
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If the passed in position is not on the
     * board
     * @throws IllegalArgumentException If a piece is already at the passed in
     * location
     * @throws IllegalStateException If the game is not ongoing
     *
     * @param pos {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Gomoku move(Position pos) {
        if (!GameUtil.isPositionOnTheBoard(pos)) {
            throw new IllegalArgumentException("Out of bounds position.");
        } else if (board[pos.row][pos.column].isOccupied()) {
            throw new IllegalArgumentException("Already a piece at given position.");
        } else if (state != GameState.ONGOING) {
            throw new IllegalStateException("Game has already finished.");
        }

        return new GomokuImpl(this, pos);
    }

    @Override
    public GameState getGameState() {
        return state;
    }

    @Override
    public PlayerName getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Position pos = new Position(row, column);

                switch (getPositionContent(pos)) {
                    case FREE:
                        builder.append("*");
                        break;
                    case BLACK:
                        builder.append("B");
                        break;
                    case WHITE:
                        builder.append("W");
                        break;
                }
            }

            builder.append("\n");
        }

        builder.delete(builder.length() - 1, builder.length());

        return builder.toString();
    }

    private boolean hasGameBeenWon(Position movePosition) {
        List<List<Position>> channels
                = GameUtil.extractChannelsPieceIsIn(movePosition);

        PositionContent placedPiece = getPositionContent(movePosition);

        for (List<Position> channel : channels) {
            int maxConsecutivePiecesOfTheSameColour
                    = findMaxConsecutivePiecesOfTheSameColour(channel, placedPiece);

            if (maxConsecutivePiecesOfTheSameColour
                    >= Gomoku.CONSECUTIVE_PIECES_TO_WIN) {
                return true;
            }
        }

        return false;
    }

    private int findMaxConsecutivePiecesOfTheSameColour(List<Position> channel,
            PositionContent placedPiece) {
        int current = 0;
        int max = current;

        for (Position positionContent : channel) {
            if (getPositionContent(positionContent) == placedPiece) {
                current++;

                if (current > max) {
                    max = current;
                }
            } else {
                current = 0;
            }
        }

        return max;
    }

    private static boolean canAnymorePiecesBeAdded(int pieceCount) {
        return pieceCount != Gomoku.SIZE * Gomoku.SIZE;
    }

    /**
     * Compares the board and the passed in object for equality.
     * 
     * Two boards are equal if the content of the boards is the same. 
     * Whose turn it is does not matter as for the same board content it will 
     * always be the same players turn. 
     * 
     * The passed in object must be an instance of GomokuImpl, any other class
     * will make the method return false.
     * 
     * @param obj The object to compare the board to
     * @return True if the board equals obj, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final GomokuImpl objCasted = (GomokuImpl) obj;

        /*
        Only need to check the board's content, as the other fields currentTurn,
        state, and pieceCount can all be derived from the boards content, 
        and the rule that black goes first and then the turn to place a piece
        switchs from player to player after each move. 
         */
        return Arrays.deepEquals(board, objCasted.board);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Arrays.deepHashCode(board);
        return hash;
    }
}
