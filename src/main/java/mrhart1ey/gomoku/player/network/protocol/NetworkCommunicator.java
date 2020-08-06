package mrhart1ey.gomoku.player.network.protocol;

import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.game.Position;

public interface NetworkCommunicator {
    public void close();
    
    public void sendGameConfiguration(GameConfiguration gameConfiguration);
    
    public GameConfiguration readGameConfiguration() throws InterruptedException;
    
    public void sendMove(Position move);
    
    public Position readMove() throws InterruptedException;
    
    public void indicateIfIWantANewGame(boolean newGame);
    
    public boolean doesOpponentWantANewGame() throws InterruptedException;
    
    public boolean isOpponentActive();
}
