package com.lowleveldesign.tictactoe.entities;

import com.lowleveldesign.tictactoe.enums.PieceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayingPiece {

    private PieceType pieceType;

    PlayingPiece(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}
