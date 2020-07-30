package mrhart1ey.gomoku.player.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mrhart1ey.gomoku.game.Direction;
import mrhart1ey.gomoku.game.Directions;
import mrhart1ey.gomoku.game.GameUtil;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PositionContent;

/**
 * Scores a Gomoku board.
 */
public final class GomokuHeuristicImpl implements GomokuHeuristic {
    private final Gomoku scoredBoard;
    private final PlayerName forPlayer;
    private final long score;

    /**
     * @param initialBoard The initial board
     * @param forPlayer The perspective that the scoring is done from
     */
    public GomokuHeuristicImpl(Gomoku initialBoard, PlayerName forPlayer) {
        this(initialBoard, forPlayer, 0);
    }

    private GomokuHeuristicImpl(Gomoku board, PlayerName forPlayer, 
            long score) {
        this.scoredBoard = board;
        this.forPlayer = forPlayer;
        this.score = score;
    }

    @Override
    public GomokuHeuristic evaluate(Gomoku newBoard, Set<Position> difference) {
        long oldBoardScoreAroundMoves = scoreAroundPositions(scoredBoard,
                difference);
        
        long newBoardScoreAroundMoves = scoreAroundPositions(newBoard, 
                difference);

        long scoreForNewBoard = score + (newBoardScoreAroundMoves
                - oldBoardScoreAroundMoves);

        return new GomokuHeuristicImpl(newBoard, forPlayer, 
                scoreForNewBoard);
    }

    @Override
    public long getScore() {
        return score;
    }

    private long scoreAroundPositions(Gomoku board, Collection<Position> moves) {
        Set<Tuple<Position, Direction>> startOfRegions = new HashSet<>();

        for (Position move : moves) {
            Set<Tuple<Position, Direction>> startsOfRegionsThatMoveIsIn
                    = findTheStartsOfRegionsThatPositionIsIn(move);

            startOfRegions.addAll(startsOfRegionsThatMoveIsIn);
        }

        Collection<Region> regions = new HashSet<>();
        for (Tuple<Position, Direction> startOfRegion : startOfRegions) {
            Region region = newRegion(board, startOfRegion);
            
            regions.add(region);
        }

        long totalScore = 0;
        for (Region region : regions) {
            totalScore += scoreRegion(region, forPlayer);
        }

        return totalScore;
    }

    private Set<Tuple<Position, Direction>>
            findTheStartsOfRegionsThatPositionIsIn(Position move) {

        Set<Tuple<Position, Direction>> result = new HashSet<>();

        Set<Tuple<Position, Direction>> northRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.NORTH);
        result.addAll(northRegions);

        Set<Tuple<Position, Direction>> northEastRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.NORTH_EAST);
        result.addAll(northEastRegions);

        Set<Tuple<Position, Direction>> eastRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.EAST);
        result.addAll(eastRegions);

        Set<Tuple<Position, Direction>> southEastRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.SOUTH_EAST);
        result.addAll(southEastRegions);

        Set<Tuple<Position, Direction>> southRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.SOUTH);
        result.addAll(southRegions);

        Set<Tuple<Position, Direction>> southWestRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.SOUTH_WEST);
        result.addAll(southWestRegions);

        Set<Tuple<Position, Direction>> westRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.WEST);
        result.addAll(westRegions);

        Set<Tuple<Position, Direction>> northWestRegions
                = findTheStartsOfRegionsThatPositionIsInDirectionally(move,
                        Directions.NORTH_WEST);
        result.addAll(northWestRegions);

        return result;
    }

    private Set<Tuple<Position, Direction>>
            findTheStartsOfRegionsThatPositionIsInDirectionally(Position start, 
                    Direction direction) {
                
        Set<Tuple<Position, Direction>> result = new HashSet<>();

        Position currentPosition = start;

        for (int i = 0; i < Region.MAX_REGION_SIZE + 1; i++) {
            if (GameUtil.isPositionOnTheBoard(currentPosition)) {
                currentPosition = direction.apply(currentPosition);
                
                result.add(new Tuple<>(currentPosition, direction.getOpposite()));
            }
        }

        return result;
    }

    private Region newRegion(Gomoku board, 
            Tuple<Position, Direction> startOfRegion) {
        List<PositionContent> contents = new ArrayList<>();

        Position currentPosition = startOfRegion.value1;

        int blackPieceCount = 0;
        int whitePieceCount = 0;

        while (contents.size() != Region.MAX_REGION_SIZE
                && GameUtil.isPositionOnTheBoard(currentPosition)) {

            PositionContent positionContent
                    = board.getPositionContent(currentPosition);

            contents.add(positionContent);

            switch (positionContent) {
                case BLACK:
                    blackPieceCount++;
                    break;
                case WHITE:
                    whitePieceCount++;
                    break;
            }

            currentPosition = startOfRegion.value2.apply(currentPosition);
        }

        if ((blackPieceCount > 0 && whitePieceCount > 0)
                || contents.size() != Region.MAX_REGION_SIZE
                || (blackPieceCount == 0 && whitePieceCount == 0)) {
            return null;
        }

        int openEndCount = 0;

        Position potentialOpenEnd1 = 
                startOfRegion.value2.getOpposite().apply(startOfRegion.value1);
        Position potentialOpenEnd2 = currentPosition;

        if (GameUtil.isPositionOnTheBoard(potentialOpenEnd1)
                && !board.getPositionContent(potentialOpenEnd1).isOccupied()) {
            openEndCount++;
        }

        if (GameUtil.isPositionOnTheBoard(potentialOpenEnd2)
                && !board.getPositionContent(potentialOpenEnd2).isOccupied()) {
            openEndCount++;
        }

        return new Region(contents, openEndCount);
    }

    private static int scoreRegion(Region region, PlayerName forPlayer) {
        int result = scoreRegion(region);

        if (region != null && region.getOwner() != forPlayer) {
            return -result;
        }

        return result;
    }

    private static int scoreRegion(Region region) {
        if (region == null) {
            return 0;
        } else if (region.getPieceCount() != 5 && region.isClosedEnded()) {
            return 0;
        } else if (region.getPieceCount() == 1 && region.isPartiallyOpenEnded()) {
            return 1;
        } else if (region.getPieceCount() == 1 && region.isFullyOpenEnded()) {
            return 4;
        } else if (region.getPieceCount() == 2 && region.isPartiallyOpenEnded()) {
            return 4;
        } else if (region.getPieceCount() == 2 && region.isFullyOpenEnded()) {
            return 27;
        } else if (region.getPieceCount() == 3 && region.isPartiallyOpenEnded()) {
            return 27;
        } else if (region.getPieceCount() == 3 && region.isFullyOpenEnded()) {
            return 256;
        } else if (region.getPieceCount() == 4 && region.isPartiallyOpenEnded()) {
            return 256;
        } else if (region.getPieceCount() == 4 && region.isFullyOpenEnded()) {
            return 3125;
        } else if (region.getPieceCount() == 5 && region.isPartiallyOpenEnded()
                || region.getPieceCount() == 5 && region.isFullyOpenEnded()
                || region.getPieceCount() == 5 && region.isClosedEnded()) {
            return 46656;
        }

        throw new IllegalArgumentException("Unknown region config");
    }
}
