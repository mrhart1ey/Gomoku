package mrhart1ey.gomoku;

import java.util.ArrayList;
import java.util.List;
import mrhart1ey.gomoku.game.GameResult;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.GameUtil;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.gui.GameDisplay;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJob;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobBoard;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobEndInstructions;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobGameResult;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobOpponentLeft;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJobVictoryLine;

public class PostGameMenuImpl implements PostGameMenu {

    private final GameDisplay display;
    private final PlayerHandler playerHandler;

    public PostGameMenuImpl(GameDisplay display, PlayerHandler playerHandler) {
        this.display = display;
        this.playerHandler = playerHandler;
    }

    @Override
    public boolean shouldStartANewGame(Gomoku finalBoard, PlayerName myName) {
        GameResult result = findGameResult(finalBoard,
                myName, !playerHandler.isOpponentActive());

        String resultMessage = findResultMessage(result);
        
        boolean answered = false;
        
        boolean shouldStartANewGame = false;
        
        while(!answered) {
            List<DrawJob> drawJobs = new ArrayList<>();
            
           if(finalBoard.getGameState() == GameState.VICTORY) {
               List<Position> victoryLine = GameUtil.getVictoryLine(finalBoard);
                    
               Position lineStart = victoryLine.get(0);
               Position lineEnd = victoryLine.get(victoryLine.size() - 1);
                    
               drawJobs.add(
                       new DrawJobVictoryLine(lineStart, lineEnd));
           }
            
           drawJobs.add(new DrawJobGameResult(resultMessage));
           
           if(playerHandler.isOpponentActive()) {
               drawJobs.add(new DrawJobEndInstructions("Space", "Escape"));
               if(display.hasEscapeKeyBeenPressed()) {
                   boolean doIWantANewGame = display.hasSpaceBarBeenPressed();
                
                   playerHandler.tellTheOpponentIfIWantAnotherGame(doIWantANewGame);
        
                   boolean doesOpponentWantAnotherGame = true;
        
                   if(doIWantANewGame) {
                       doesOpponentWantAnotherGame = playerHandler.doesOpponentWantAnotherGame();
                   }
                   
                   if(doesOpponentWantAnotherGame) {
                       answered = true;
                   }
                   
                   shouldStartANewGame = doesOpponentWantAnotherGame && doIWantANewGame;
               }
           }else  {
               drawJobs.add(new DrawJobOpponentLeft("Escape"));
               if(display.hasEscapeKeyBeenPressed() && 
                       !display.hasSpaceBarBeenPressed()) {
                   answered = true;
               }
           }
           
           display.draw(new DrawJobBoard(finalBoard), drawJobs);
           
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                
            }
        }
        
        return shouldStartANewGame;
    }
    
    private static GameResult findGameResult(Gomoku finalBoard,
            PlayerName myName, boolean didOpponentDisconnect) {
        GameResult result;

        if (didOpponentDisconnect) {
            if (myName == PlayerName.BLACK) {
                result = GameResult.BLACK_VICTORY;
            } else {
                result = GameResult.WHITE_VICTORY;
            }
        } else if (finalBoard.getGameState() == GameState.VICTORY) {
            result = findGameResultIfTimeDidNotRunOut(finalBoard);
        } else if (finalBoard.getGameState() == GameState.DRAW) {
            result = GameResult.DRAW;
        } else {
            result = findGameResultIfTimeRunOut(finalBoard);
        }

        return result;
    }
    
    private String findResultMessage(GameResult gameResult) {
        switch (gameResult) {
            case BLACK_VICTORY:
            case WHITE_VICTORY:
                if(gameResult == GameResult.BLACK_VICTORY) {
                    return "Black won!";
                }
                
                return "White won!";
            default:
                return "It was a draw!";
        }
    }

    private static GameResult findGameResultIfTimeRunOut(Gomoku finalBoard) {
        if (finalBoard.getCurrentTurn() == PlayerName.BLACK) {
            return GameResult.WHITE_VICTORY;
        } else {
            return GameResult.BLACK_VICTORY;
        }
    }

    private static GameResult findGameResultIfTimeDidNotRunOut(Gomoku finalBoard) {
        if (finalBoard.getCurrentTurn() == PlayerName.WHITE) {
            return GameResult.BLACK_VICTORY;
        } else {
            return GameResult.WHITE_VICTORY;
        }
    }
}
