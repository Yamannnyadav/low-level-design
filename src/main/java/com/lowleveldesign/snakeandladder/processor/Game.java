package com.lowleveldesign.snakeandladder.processor;

import com.lowleveldesign.snakeandladder.entities.Cell;
import com.lowleveldesign.snakeandladder.entities.Dice;
import com.lowleveldesign.snakeandladder.entities.GameBoard;
import com.lowleveldesign.snakeandladder.entities.Player;

import java.util.Deque;
import java.util.LinkedList;

public class Game {

    private GameBoard gameBoard;
    private Dice dice;
    private final Deque<Player> players = new LinkedList<>();
    private Player winner;

    public Game(){
        initializeGame();
    }

    private void initializeGame(){
        gameBoard = new GameBoard(10, 5, 4);
        dice = new Dice(1);
        winner = null;
        addPlayers();
    }

    private void addPlayers(){
        players.add(new Player("Player-1", 0));
        players.add(new Player("Player-2", 0));
    }

    public void startGame(){

        while (winner == null) {

            //check whose turn is this
            Player playerTurn = findPlayerTurn();
            System.out.println("Player turn:" + playerTurn.getId() + " current position is: " + playerTurn.getCurrPos());

            //roll the dice
            int diceNumber = dice.rollDice();

            //get the new position and make move
            int playerNewPos = playerTurn.getCurrPos() + diceNumber;
            playerNewPos = jumpCheck(playerNewPos);
            playerTurn.setCurrPos(playerNewPos);

            System.out.println("Player turn:" + playerTurn.getId() + " new Position is: " + playerTurn.getCurrPos());

            //check for winner
            if (playerNewPos >= gameBoard.getCells().length * gameBoard.getCells().length - 1) {
                winner = playerTurn;
            }
        }

        System.out.println("\n===> The Winner is:" + winner.getId());

    }

    private Player findPlayerTurn(){
        Player playerTurn = players.removeFirst();
        players.addLast(playerTurn);
        return playerTurn;
    }

    private int jumpCheck (int playerPos) {

        if(playerPos > gameBoard.getCells().length * gameBoard.getCells().length - 1){
            return playerPos;
        } //no change in pos as invalid playerPos

        Cell cell = gameBoard.getCell(playerPos); //get cell where player is present

        if (cell.getJump() != null && cell.getJump().getStart() == playerPos) {
            String jumpBy = (cell.getJump().getStart() < cell.getJump().getEnd()) ? "Ladder" : "Snake";
            System.out.println("[+] Jump done by: " + jumpBy);
            return cell.getJump().getEnd();
        }

        return playerPos;

    }

}
