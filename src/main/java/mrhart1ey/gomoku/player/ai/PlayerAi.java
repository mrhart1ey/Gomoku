package mrhart1ey.gomoku.player.ai;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import mrhart1ey.gomoku.game.Directions;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.GameUtil;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.player.Player;

/**
 * An AI implementation of a player
 */
public class PlayerAi implements Player {

    private GomokuHeuristic heuristic;
    private final PlayerName forPlayer;

    public PlayerAi(GomokuHeuristic heuristic, PlayerName forPlayer) {
        this.heuristic = heuristic;
        this.forPlayer = forPlayer;
    }

    @Override
    public Position nextMove(Gomoku board) {
        Position move;

        heuristic = heuristic.evaluate(board, forPlayer);

        Set<Position> possibleMoves = generatePossibleMoves(board);

        if (board.getGameState() == GameState.ONGOING) {
            Tuple<Long, Position> result
                    = alphabeta(board, heuristic, possibleMoves, null, 2,
                            Integer.MIN_VALUE, Integer.MAX_VALUE, true);

            move = result.value2;
        } else {
            move = null;
        }

        return move;
    }

    private Tuple<Long, Position> alphabeta(Gomoku node,
            GomokuHeuristic currentHeuristic, Set<Position> possibleMoves,
            Position move, int depth, long alpha,
            long beta, boolean maximizingPlayer) {

        if (depth == 0 || node.getGameState() != GameState.ONGOING) {
            return new Tuple<>(currentHeuristic.evaluate(node, 
                    forPlayer).getScore(), move);
        }

        if (maximizingPlayer) {
            long value = Integer.MIN_VALUE;

            Position bestMove = null;

            Set<Position> possibleMovesToPassOn = new HashSet<>(possibleMoves);

            for (Position nextMove : possibleMoves) {
                Gomoku child = node.move(nextMove);

                Set<Position> freeAdjacentPositionsByMove
                        = getFreeAdjacentPositions(child, nextMove);
                Set<Position> freeAdjacentPositionsByMoveNotAlreadyPossible
                        = getFreeAdjacentPositions(child, nextMove);
                for (Position freeAdjacentPositionByMove : freeAdjacentPositionsByMove) {
                    if (possibleMoves.contains(freeAdjacentPositionByMove)) {
                        freeAdjacentPositionsByMoveNotAlreadyPossible.
                                remove(freeAdjacentPositionByMove);
                    }
                }
                possibleMovesToPassOn.remove(nextMove);
                possibleMovesToPassOn.addAll(freeAdjacentPositionsByMoveNotAlreadyPossible);

                GomokuHeuristic newHeuristic
                        = currentHeuristic.evaluate(child, forPlayer);

                Tuple<Long, Position> result = alphabeta(child, newHeuristic,
                        possibleMovesToPassOn, nextMove, depth - 1, alpha, beta, false);

                possibleMovesToPassOn.add(nextMove);
                possibleMovesToPassOn.removeAll(freeAdjacentPositionsByMoveNotAlreadyPossible);

                if (value <= result.value1) {
                    value = result.value1;
                    bestMove = nextMove;
                }
            }

            return new Tuple<>(value, bestMove);
        } else {
            long value = Integer.MAX_VALUE;

            Position bestMove = null;

            Set<Position> possibleMovesToPassOn = new HashSet<>(possibleMoves);

            for (Position nextMove : possibleMoves) {
                Gomoku child = node.move(nextMove);

                Set<Position> freeAdjacentPositionsByMove
                        = getFreeAdjacentPositions(child, nextMove);
                Set<Position> freeAdjacentPositionsByMoveNotAlreadyPossible
                        = getFreeAdjacentPositions(child, nextMove);
                for (Position freeAdjacentPositionByMove : freeAdjacentPositionsByMove) {
                    if (possibleMoves.contains(freeAdjacentPositionByMove)) {
                        freeAdjacentPositionsByMoveNotAlreadyPossible.
                                remove(freeAdjacentPositionByMove);
                    }
                }
                possibleMovesToPassOn.remove(nextMove);
                possibleMovesToPassOn.addAll(freeAdjacentPositionsByMoveNotAlreadyPossible);

                GomokuHeuristic newHeuristic
                        = currentHeuristic.evaluate(child, forPlayer);

                Tuple<Long, Position> result = alphabeta(child, newHeuristic,
                        possibleMovesToPassOn, nextMove, depth - 1, alpha, beta, true);

                possibleMovesToPassOn.add(nextMove);
                possibleMovesToPassOn.removeAll(freeAdjacentPositionsByMoveNotAlreadyPossible);

                if (value >= result.value1) {
                    value = result.value1;
                    bestMove = nextMove;
                }
            }

            return new Tuple<>(value, bestMove);
        }
    }

    private Set<Position> generatePossibleMoves(Gomoku board) {
        Set<Position> result = new HashSet<>();

        for (int row = 0; row < Gomoku.SIZE; row++) {
            for (int column = 0; column < Gomoku.SIZE; column++) {
                Position position = new Position(row, column);

                if (board.getPositionContent(position).isOccupied()) {
                    result.addAll(getFreeAdjacentPositions(board, position));
                }
            }
        }

        if (result.isEmpty()) {
            Random r = new Random();

            Position move = new Position(r.nextInt(Gomoku.SIZE),
                    r.nextInt(Gomoku.SIZE));

            result.add(move);
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
