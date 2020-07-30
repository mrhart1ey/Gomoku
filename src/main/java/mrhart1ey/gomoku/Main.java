package mrhart1ey.gomoku;

import mrhart1ey.gomoku.player.gui.menu.GameMenu;
import mrhart1ey.gomoku.player.gui.GameDisplayGui;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.timer.DeactivatedGameTimer;

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
        
            Player opponent = sessionConfiguration.otherPlayer;
        
            PlayerName myName = sessionConfiguration.myName;
            
            DeactivatedGameTimer timer = sessionConfiguration.timer;
        
            GameMonitor gameMonitor = sessionConfiguration.gameMonitor;
                
            TurnIndicator turnIndicator = sessionConfiguration.turnIndicator;
            
            MoveRetriever moveRetriever = new MoveRetriever(me,  opponent, myName);
            moveRetriever.start();
            
            GameRunner gameRunner = new GameRunnerImpl(board, timer, gameMonitor, 
                    turnIndicator, display, moveRetriever);
            
            PostGameMenu postGameMenu = new PostGameMenuImpl(display, handler);
            
            display.show();
            
            do {
                Gomoku finalBoard = gameRunner.runGame(myName);
                
                shouldStartANewGame = 
                        postGameMenu.shouldStartANewGame(finalBoard, myName);
            }while(shouldStartANewGame);

            handler.stop();
            
            moveRetriever.stop();
            
            display.stopShowing();
            
            menu.show();
        }    
    }
    

}
