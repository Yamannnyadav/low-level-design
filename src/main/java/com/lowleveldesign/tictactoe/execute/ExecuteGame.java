package com.lowleveldesign.tictactoe.execute;

import com.lowleveldesign.tictactoe.enums.GameStatus;
import com.lowleveldesign.tictactoe.processor.TicTacToeGame;

public class ExecuteGame {

    public static void main(String[] args) {
        System.out.println("\n===>>> TicTacToe Game\n");
        TicTacToeGame game = new TicTacToeGame();
        game.initializeGame();
        GameStatus status = game.startGame();
        System.out.print("\n===>>> GAME OVER: ");
        switch (status) {
            case WINNER:
                System.out.print(game.winner.getName() + " won the game");
                break;
            case DRAW:
                System.out.print(" Its a Draw!");
                break;
            default:
                System.out.print(" Game Ends");
                break;
        }

    }

}
