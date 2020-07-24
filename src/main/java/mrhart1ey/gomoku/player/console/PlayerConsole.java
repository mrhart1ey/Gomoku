package mrhart1ey.gomoku.player.console;

import java.util.Scanner;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;

public class PlayerConsole implements Player {

    private final Scanner in;

    public PlayerConsole() {
        in = new Scanner(System.in);
    }

    @Override
    public Position nextMove(Gomoku board) {
        System.out.println("Enter row: ");
        int row = in.nextInt();
        
        System.out.println("Enter column: ");
        int column = in.nextInt();

        return new Position(row, column);
    }

}
