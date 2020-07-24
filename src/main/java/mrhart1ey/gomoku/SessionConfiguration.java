package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.timer.GameTimer;

public class SessionConfiguration {
    public final Gomoku initialboard;
    public final Player me;
    public final Player otherPlayer;
    public final PlayerName myName;
    public final GameTimer timer;
    public final PlayerHandler handler;
    public final GameMonitor gameMonitor;
    public final TurnIndicator turnIndicator;
    
    public SessionConfiguration(Gomoku initialboard, Player me, Player otherPlayer, 
            PlayerName myName, GameTimer timer, PlayerHandler handler, 
            GameMonitor gameMonitor, TurnIndicator turnIndicator) {
        this.initialboard = initialboard;
        this.me = me;
        this.otherPlayer = otherPlayer;
        this.myName = myName;
        this.timer = timer;
        this.handler = handler;
        this.gameMonitor = gameMonitor;
        this.turnIndicator = turnIndicator;
    }
}
