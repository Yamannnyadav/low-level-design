# CAR RENTAL SYSTEM

**Problem Statement**:
Design a comprehensive 2-player tic-tac-toe game of Xs and Os with the executable code
following all design principles, patterns, best practices and guidelines discussed before.


**Functional Requirements**:
- Player should be able to start a game.
- Player should be able to choose a playing piece.
- Player should be able to make a move.
- Player should be able to win a game after completing a row, column or diagonal.
- Player should be able to mark a game as draw once the Player is out of free moves.


**Non-Function Requirements**:
- System shouldn't allow invalid moves.
- System should be able to load the updated board after every move with low latency.
- System should keep the game consistent.
- System should be extendable to N x N board.


**Core Entities**:
- Player
- Game Board
- Game
- Playing Piece
- Piece Type
- Move


**Entities and their relationship:**

Player
- id
- PlayingPiece playingPiece


GameBoard
- size
- PlayingPiece[]


Game
- id
- GameBoard board
- Dequeue<Player>
- Player Winner


PlayingPiece
- pieceType

**Patterns Used**:
- Strategy pattern will be used to charge Player based on hourly reservation.