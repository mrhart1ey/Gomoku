package mrhart1ey.gomoku.player.network;

import java.util.concurrent.BlockingQueue;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;

final class Me implements Player {

    private final Player input;
    private final SingleTimePasser<Gomoku> boardPasser;
    private final BlockingQueue<Position> movePasser;
    
    Me(Player input, SingleTimePasser<Gomoku> boardPasser,
            BlockingQueue<Position> movePasser) {
        this.input = input;
        this.boardPasser = boardPasser;
        this.movePasser = movePasser;
    }

    @Override
    public Position nextMove(Gomoku board) {
        Position move = input.nextMove(board);

        try {
            boardPasser.put(board);
            
            if(move != null) {
                 movePasser.put(move);
            }
        } catch (InterruptedException ex) {

        }
        return move;
    }

}
