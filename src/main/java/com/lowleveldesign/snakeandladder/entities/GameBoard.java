package com.lowleveldesign.snakeandladder.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class GameBoard {

    private Cell[][] cells;

    public GameBoard(int size, int numOfSnakes, int numOfLadder){
        initialiseCells(size);
        addSnakesAndLadders(cells, numOfSnakes, numOfLadder);
    }

    public void initialiseCells(int size){

        cells = new Cell[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                Cell cellObj = new Cell();
                cells[i][j] = cellObj;
            }
        }

    }

    public void addSnakesAndLadders(Cell[][] cells, int numOfSnakes, int numOfLadders){

        while(numOfSnakes > 0){

            int snakeHead = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            int snakeTail = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            if(snakeTail >= snakeHead){
                continue;
            }

            Jump snakeJump = new Jump(snakeHead, snakeTail); //creates the jump for snake
            Cell currCell = getCell(snakeHead); //check curr cell
            currCell.setJump(snakeJump); //sets a jump from that cell

            numOfSnakes--;
        }

        while(numOfLadders > 0){

            int ladderStart = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            int ladderEnd = ThreadLocalRandom.current().nextInt(1, cells.length * cells.length - 1);
            if(ladderEnd <= ladderStart){
                continue;
            }

            Jump ladderJump = new Jump(ladderStart, ladderEnd); //creates the jump for ladder
            Cell currCell = getCell(ladderStart); //check curr cell
            currCell.setJump(ladderJump); //sets a jump from that cell

            numOfLadders--;

        }

    }

    public Cell getCell(int playerPos){
        int row = playerPos / cells.length;
        int col = playerPos % cells.length;
        return cells[row][col];
    }
}
