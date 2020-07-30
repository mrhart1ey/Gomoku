package mrhart1ey.gomoku.player.ai;

import java.util.HashSet;
import java.util.Set;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.Player;

/**
 * Does a minimax search with alpha-beta pruning.
 */
final class PlayerMinimax implements Player {

    private GomokuHeuristic heuristic;
    private Gomoku board;
    private final PossibleMoveGenerator moveGenerator;
    private final int searchDepth;

    public PlayerMinimax(GomokuHeuristic heuristic, Gomoku initialBoard, 
            PossibleMoveGenerator moveGenerator, int searchDepth) {
        this.heuristic = heuristic;
        board = initialBoard;
        this.moveGenerator = moveGenerator;
        this.searchDepth = searchDepth;
    }

    @Override
    public Position nextMove(Gomoku boardToEvaluate) {
        if (boardToEvaluate.getGameState() != GameState.ONGOING) {
            throw new IllegalArgumentException("Can not make a move when the "
                    + "game is already over");
        }

        Set<Position> boardDifference = difference(board, boardToEvaluate);
        board = boardToEvaluate;
        heuristic = heuristic.evaluate(boardToEvaluate, boardDifference);

        Tuple<Long, Position> result
                = minimax(boardToEvaluate, heuristic, null, searchDepth, 
                        Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        Position move = result.value2;

        return move;
    }

    private Tuple<Long, Position> minimax(Gomoku node,
            GomokuHeuristic currentHeuristic, Position move, 
            int depth, long alpha, long beta, boolean maximizingPlayer) {

        if (depth == 0 || node.getGameState() != GameState.ONGOING) {
            return new Tuple<>(currentHeuristic.getScore(), move);
        }
        
        Set<Position> possibleMove = moveGenerator.generate(node);

        if (maximizingPlayer) {
            long value = Integer.MIN_VALUE;

            Position bestMove = null;

            for (Position nextMove : possibleMove) {
                Gomoku child = node.move(nextMove);

                GomokuHeuristic newHeuristic
                        = currentHeuristic.evaluate(child, Set.of(nextMove));

                Tuple<Long, Position> result = minimax(child, newHeuristic, 
                        nextMove, depth - 1, alpha, beta, false);

                if (value <= result.value1) {
                    value = result.value1;
                    bestMove = nextMove;
                }
                
                if(value >= beta) {
                    break;
                }
                
                alpha = Math.max(alpha, value);
            }
            
            return new Tuple<>(value, bestMove);
        } else {
            long value = Integer.MAX_VALUE;

            Position bestMove = null;

            for (Position nextMove : possibleMove) {
                Gomoku child = node.move(nextMove);

                GomokuHeuristic newHeuristic
                        = currentHeuristic.evaluate(child, Set.of(nextMove));

                Tuple<Long, Position> result = minimax(child, newHeuristic, 
                        nextMove, depth - 1, alpha, beta, true);

                if (value >= result.value1) {
                    value = result.value1;
                    bestMove = nextMove;
                }
                
                if(value <= alpha) {
                    break;
                }
                
                beta = Math.min(beta, value);
            }
            
            return new Tuple<>(value, bestMove);
        }
    }
    
    private Set<Position> difference(Gomoku board, Gomoku boardToEvaluate) {
        Set<Position> result = new HashSet<>();

        for (int row = 0; row < Gomoku.SIZE; row++) {
            for (int column = 0; column < Gomoku.SIZE; column++) {
                Position pos = new Position(row, column);

                if (board.getPositionContent(pos)
                        != boardToEvaluate.getPositionContent(pos)) {
                    result.add(pos);
                }
            }
        }

        return result;
    }
}
