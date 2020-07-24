package mrhart1ey.gomoku.game;

/**
 * Gomoku is a two player board game, played on a 19 by 19 board,
 * were each player plays as black or white, and places pieces of their 
 * respective colour. Black starts first and after that the players take it in 
 * turns to place their coloured piece on the board.
 * The first player to reach 5 or more consecutive adjacent pieces of the same colour 
 * wins the game, if nobody wins the game is a draw.
 * 
 * For a piece on the board it can have a maximum of 8 adjacent pieces, north,
 * north-east, east, south-east, south, south-west, west, north-west.
 * 
 * All classes that implement this interface should be immutable, so if a change
 * is needed to be made to the board then a new instance of the board should be returned.
 */
public interface Gomoku {
    public static final int SIZE = 19;
    public final static int CONSECUTIVE_PIECES_TO_WIN = 5;
    public static final PlayerName INITIAL_TURN = PlayerName.BLACK;
    
    /**
     * @param position The position to get content from
     * @return The content at the passed in position
     */
    public PositionContent getPositionContent(Position position);
    
    /**
     * Places a piece on the board at the passed in position.
     * No other positions on the board will have their content changed buy the move.
     * 
     * The colour of the placed piece will depend on whose turn it currently is.
     * 
     * @param pos The position to put a piece on
     * @return The board after the move
     */
    public Gomoku move(Position pos);
    
    /**
     * @return The state of the game
     */
    public GameState getGameState();
    
    /**
     * @return The player name of the player who should make the next move
     */
    public PlayerName getCurrentTurn();
}
