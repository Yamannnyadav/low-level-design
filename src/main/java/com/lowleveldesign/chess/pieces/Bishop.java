package com.lowleveldesign.chess.pieces;

import com.lowleveldesign.chess.entities.Board;
import com.lowleveldesign.chess.entities.Cell;
import com.lowleveldesign.chess.enums.PieceColour;
import com.lowleveldesign.chess.enums.PieceType;

public class Bishop extends Piece {

    public Bishop(PieceColour colour) {
        super(colour, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Board board, Cell start, Cell end) {
        if (start.equals(end)) return false;

        // If the target cell has piece of same colour, then it is invalid move
        if (end.getPiece() != null && end.getPiece().getColour() == start.getPiece().getColour()) return false;

        // If the target cell is not diagonal to the start cell, then it is invalid move
        int rowDiff = Math.abs(end.getPosition().getRow() - start.getPosition().getRow());
        int colDiff = Math.abs(end.getPosition().getCol() - start.getPosition().getCol());

        if (rowDiff == colDiff && isPathClear(board, start, end)) {
            Piece targetPiece = board.getCell(end.getPosition()).getPiece();
            return targetPiece == null || targetPiece.getColour() != start.getPiece().getColour();
        }
        return false;
    }

}
