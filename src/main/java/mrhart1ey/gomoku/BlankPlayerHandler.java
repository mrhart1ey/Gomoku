package mrhart1ey.gomoku;

public final class BlankPlayerHandler implements PlayerHandler {

    @Override
    public void stop() {
        
    }

    @Override
    public boolean doesOpponentWantAnotherGame() {
        return true;
    }

    @Override
    public void tellTheOpponentIfAnotherGameIsWanted(boolean arg0) {
        
    }

    @Override
    public boolean isOpponentActive() {
        return true;
    }
    
}
