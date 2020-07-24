package mrhart1ey.gomoku.player.ai;

import java.util.List;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.PositionContent;

final class Region {
    public static final int MAX_REGION_SIZE = 
            Gomoku.CONSECUTIVE_PIECES_TO_WIN;
    
    private final int openEndCount;

    private final PlayerName owner;
    private final int pieceCount;

    Region(List<PositionContent> contents, int openEndCount) {
        if(contents.size() != MAX_REGION_SIZE) {
            throw new IllegalArgumentException("A region should be of size " 
                    + MAX_REGION_SIZE);
        }
        
        if (openEndCount > 2 || openEndCount < 0) {
            throw new IllegalArgumentException("The open end count should be 0, 1, or 2");
        }

        this.openEndCount = openEndCount;

        owner = findOwner(contents);
        
        if(!doAllPiecesHaveTheSameOwner(contents, owner)) {
            throw new IllegalArgumentException("All of the pieces should have the same owner");
        }
        
        pieceCount = countPieces(contents);
    }

    public PlayerName getOwner() {
        return owner;
    }
    
    public int getPieceCount() {
        return pieceCount;
    }

    public boolean isClosedEnded() {
        return openEndCount == 0;
    }

    public boolean isPartiallyOpenEnded() {
        return openEndCount == 1;
    }

    public boolean isFullyOpenEnded() {
        return openEndCount == 2;
    }

    private static PlayerName findOwner(List<PositionContent> contents) {
        for (PositionContent content : contents) {
            if (content == PositionContent.BLACK) {
                return PlayerName.BLACK;
            } else if (content == PositionContent.WHITE) {
                return PlayerName.WHITE;
            }
        }

        throw new IllegalArgumentException("Region should contain a player's piece");
    }

    private static int countPieces(List<PositionContent> contents) {
        int count = 0;
        
        for(PositionContent content: contents) {
            if(content.isOccupied()) {
                count++;
            }
        }
        
        return count;
    }
    
    private static boolean doAllPiecesHaveTheSameOwner(
            List<PositionContent> contents, PlayerName owner) {
        for(PositionContent content: contents) {
            if(content.isOccupied() && 
                    content != owner.getPieceToPlace()) {
                return false;
            }
        }
        
        return true;
    }
}
