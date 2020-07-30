package mrhart1ey.gomoku.player.ai;

import java.util.List;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.PositionContent;

/**
 * A region is an area of the board that it is possible for a player to win the game with.
 * 
 * As a region is an area of the board that it is possible for a player to 
 * win the game with, it means that a region can only contain pieces of one colour,
 * but they can also contain free positions. 
 * 
 * A region's size is the number of pieces needed to win the game.
 * 
 * A region has an owner, who is a player of the game.
 * 
 * A regions piece count is the number of the owner's pieces in the region.
 * 
 * The open end count of a region can be 0, 1, or 2. If it is 2 then the region
 * is open ended, so there are free positions at either side. If it is 1 then
 * it is called partially open, so there is one free position at one end. If it
 * is 0 then it is closed, there are no free positions at any end. 
 */
final class Region {
    public static final int MAX_REGION_SIZE = 
            Gomoku.CONSECUTIVE_PIECES_TO_WIN;
    
    private final int openEndCount;

    private final PlayerName owner;
    private final int pieceCount;

    /**
     * @param contents The 
     * @param openEndCount 
     */
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
