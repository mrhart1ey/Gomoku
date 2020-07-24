package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.PlayerName;

public interface TurnIndicator {
    public boolean isMyTurn(PlayerName myName, PlayerName playerToMakeTheNextMove);
}
