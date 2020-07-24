package mrhart1ey.gomoku.game;

/**
 * Represents the two Gomoku players, black and white.
 */
public enum PlayerName {
    BLACK(PositionContent.BLACK), WHITE(PositionContent.WHITE);
    
    private final PositionContent pieceToPlace;
    
    private PlayerName(PositionContent pieceToPlace) {
        this.pieceToPlace = pieceToPlace;
    }
    
    /**
     * @return PlayerName.BLACK if it was called on PlayerName.WHITE, otherwise
     * PlayerName.WHITE
     */
    public PlayerName turnAfter() {
        if(this == WHITE) {
            return BLACK;
        }
        
        return WHITE;
    }
    
    /**
     * @return What the player will place on the board when it places a piece 
     * on the board
     */
    public PositionContent getPieceToPlace() {
        return pieceToPlace;
    }
}
