package mrhart1ey.gomoku.player.ai;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import mrhart1ey.gomoku.game.Directions;
import mrhart1ey.gomoku.game.GameUtil;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.Position;


/**
 * Generates moves that are adjacent to occupied positions.
 */
final class PossibleMoveGeneratorAdjacent implements PossibleMoveGenerator {

    private final Random random;

    /**
     * @param random Used to choose a random position if the board has no occupied
     * positions on it
     */
    public PossibleMoveGeneratorAdjacent(Random random) {
        this.random = random;
    }

    /**
     * @param board The board to find possible moves for
     * @return All of the positions that are free and are directly adjacent to an 
     * occupied position. If the passed in board has no occupied positions then
     * a position will be chosen randomly
     */
    @Override
    public Set<Position> generate(Gomoku board) {

        Set<Position> result = new HashSet<>();
        
        boolean areAnyPositionsTaken = false;
        
        for(int row = 0; row < Gomoku.SIZE; row++) {
            for(int column = 0; column < Gomoku.SIZE; column++) {
                Position position = new Position(row, column);
                
                if(board.getPositionContent(position).isOccupied()) {
                    result.addAll(getFreeAdjacentPositions(board, position));
                    areAnyPositionsTaken = true;
                }
            }
        }
        
        if (!areAnyPositionsTaken) {
            Position randomMove = new Position(random.nextInt(Gomoku.SIZE),
                    random.nextInt(Gomoku.SIZE));
            result.add(randomMove);
        }

        return result;
    }

    private Set<Position> getFreeAdjacentPositions(Gomoku board, Position position) {
        Set<Position> result = new HashSet<>();

        Position north = Directions.NORTH.apply(position);
        if (GameUtil.isPositionOnTheBoard(north)
                && !board.getPositionContent(north).isOccupied()) {
            result.add(north);
        }

        Position northEast = Directions.NORTH_EAST.apply(position);
        if (GameUtil.isPositionOnTheBoard(northEast)
                && !board.getPositionContent(northEast).isOccupied()) {
            result.add(northEast);
        }

        Position east = Directions.EAST.apply(position);
        if (GameUtil.isPositionOnTheBoard(east)
                && !board.getPositionContent(east).isOccupied()) {
            result.add(east);
        }

        Position southEast = Directions.SOUTH_EAST.apply(position);
        if (GameUtil.isPositionOnTheBoard(southEast)
                && !board.getPositionContent(southEast).isOccupied()) {
            result.add(southEast);
        }

        Position south = Directions.SOUTH.apply(position);
        if (GameUtil.isPositionOnTheBoard(south)
                && !board.getPositionContent(south).isOccupied()) {
            result.add(south);
        }

        Position southWest = Directions.SOUTH_WEST.apply(position);
        if (GameUtil.isPositionOnTheBoard(southWest)
                && !board.getPositionContent(southWest).isOccupied()) {
            result.add(southWest);
        }

        Position west = Directions.WEST.apply(position);
        if (GameUtil.isPositionOnTheBoard(west)
                && !board.getPositionContent(west).isOccupied()) {
            result.add(west);
        }

        Position northWest = Directions.NORTH_WEST.apply(position);
        if (GameUtil.isPositionOnTheBoard(northWest)
                && !board.getPositionContent(northWest).isOccupied()) {
            result.add(northWest);
        }

        return result;
    }
    
}
