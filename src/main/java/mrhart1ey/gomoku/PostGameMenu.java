package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;

public interface PostGameMenu {
    public boolean shouldStartANewGame(Gomoku finalBoard, PlayerName myName);
}
