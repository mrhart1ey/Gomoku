package mrhart1ey.gomoku;

import mrhart1ey.gomoku.player.gui.menu.GameMenu;
import mrhart1ey.gomoku.player.gui.GameDisplayGui;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.timer.GameTimer;

public class Main {

    public static void main(String[] args) throws Exception {
        GameDisplayGui display = new GameDisplayGui();

        GameMenu menu = new GameMenu(display);

        menu.show();

        boolean shouldStartANewGame;
        
        while(true) {
            SessionConfiguration sessionConfiguration = 
                menu.waitForUserToChooseTheSessionConfig();
            
            menu.stopShowing();

            PlayerHandler handler = sessionConfiguration.handler;
            
            Gomoku board = sessionConfiguration.initialboard;
        
            Player me = sessionConfiguration.me;
        
            Player otherPlayer = sessionConfiguration.otherPlayer;
        
            PlayerName myName = sessionConfiguration.myName;
            
            GameTimer timer = sessionConfiguration.timer;
        
            GameMonitor gameMonitor = sessionConfiguration.gameMonitor;
                
            TurnIndicator turnIndicator = sessionConfiguration.turnIndicator;
            
            GameRunner gameRunner = new GameRunnerImpl(board, me, otherPlayer, 
                    timer, gameMonitor, turnIndicator, display);
            
            PostGameMenu postGameMenu = new PostGameMenuImpl(display, handler);
            
            display.show();
            
            do {
                Gomoku finalBoard = gameRunner.runGame(myName);
                
                shouldStartANewGame = 
                        postGameMenu.shouldStartANewGame(finalBoard, myName);
            }while(shouldStartANewGame);

            handler.stop();
            
            display.stopShowing();
            
            menu.show();
        }    
    }
    

}
