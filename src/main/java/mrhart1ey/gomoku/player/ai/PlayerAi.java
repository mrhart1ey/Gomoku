package mrhart1ey.gomoku.player.ai;

import java.util.Random;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;

/**
 * An AI implementation of a player
 */
public final class PlayerAi implements Player {
    private static final int SEARCH_DEPTH = 2;
    
    private final Player aiImplementation;

    /**
     * @param heuristic How the board is scored
     * @param initialBoard The initial board
     */
    public PlayerAi(GomokuHeuristic heuristic, Gomoku initialBoard) {
        PossibleMoveGenerator moveGenerator = 
                new PossibleMoveGeneratorAdjacent(
                        new Random(System.currentTimeMillis()));
        
        aiImplementation = new PlayerMinimax(heuristic, initialBoard, 
                moveGenerator, SEARCH_DEPTH);
    }

    /**
     * {@inheritDoc}
     * 
     * @param boardToEvaluate {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IllegalArgumentException If the ai is passed a board that has 
     * a game state that is not ongoing
     */
    @Override
    public Position nextMove(Gomoku boardToEvaluate) {
        return aiImplementation.nextMove(boardToEvaluate);
    }
    
}
