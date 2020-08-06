package mrhart1ey.gomoku;


public interface PlayerHandler {
    /**
     * When called it will cause the player(s) to stopped, 
     * the players can not continue playing the game after this call
     */
    public void stop();
    
    public boolean doesOpponentWantAnotherGame();
    
    public void tellTheOpponentIfIWantAnotherGame(boolean anotherGame);
    
    public boolean isOpponentActive();
}
