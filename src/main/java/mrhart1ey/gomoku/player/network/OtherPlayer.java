package mrhart1ey.gomoku.player.network;

import java.util.concurrent.BlockingQueue;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;

final class OtherPlayer implements Player {

    private final SingleTimePasser<Gomoku> boardPasser;
    private final BlockingQueue<Position> movePasser;

    OtherPlayer(SingleTimePasser<Gomoku> boardPasser,
            BlockingQueue<Position> movePasser) {
        this.boardPasser = boardPasser;
        this.movePasser = movePasser;
    }

    @Override
    public Position nextMove(Gomoku board) {
        Position move = null;

        try {
            boardPasser.put(board);
            
            move = movePasser.take();
        } catch (InterruptedException ex) {

        }

        return move;
    }

}
