# DESIGN CHESS

## Problem Statement

Design a detailed low-level design of a Chess game where two players can play against each other following standard chess rules.

---

# Functional Requirements

* Two players should be able to start a new chess game.
* The game should initialize the board with all pieces in their standard positions.
* Players should be able to move their pieces alternately.
* The system should validate whether a move is legal based on the movement rules of the selected piece.
* The system should allow capturing an opponent's piece.
* The system should prevent illegal moves.
* The system should detect when a king is in check.
* The system should detect checkmate and end the game.
* The system should detect stalemate and declare the game a draw.
* The system should maintain the current state of the game (In Progress, Check, Checkmate, Stalemate, Draw).

---

# Non-Functional Requirements

* The system should provide low-latency move validation.
* The system should ensure consistency of the game state.
* The system should support only two players per game.
* The design should be extensible to support additional chess variants in the future.
* The system should maintain move history for replay or undo functionality.

---

# Core Entities

* Game
* Board
* Cell
* Position
* Player
* Piece
* Move

---

# Entities and Their Relationships

## Game

```
id
Board board
Player whitePlayer
Player blackPlayer
Player currentPlayer
GameStatus status
List<Move> moveHistory
```

### Responsibilities

* Start game
* Validate moves
* Switch turns
* Detect winner
* End game

---

## Board

```
Cell[][] cells
```

### Responsibilities

* Initialize board
* Move pieces
* Return cell by position

---

## Cell

```
Position position
Piece piece
```

### Responsibilities

* Hold a chess piece
* Determine whether occupied

---

## Position

```
int row
int column
```

Represents a location on the chess board.

---

## Player

```
String id
String name
Color color
```

### Responsibilities

* Make a move
* Own chess pieces

---

## Piece (Abstract)

```
Color color
PieceType pieceType
boolean captured
```

### Common Methods

```
isValidMove(Board board, Position source, Position destination)

getPossibleMoves(Board board)
```

---

## Pawn

Extends Piece

Responsibilities

* Move one step forward
* Move two steps on first move
* Capture diagonally

---

## Rook

Extends Piece

Responsibilities

* Move horizontally
* Move vertically

---

## Bishop

Extends Piece

Responsibilities

* Move diagonally

---

## Knight

Extends Piece

Responsibilities

* Move in L-shape
* Jump over other pieces

---

## Queen

Extends Piece

Responsibilities

* Move horizontally
* Move vertically
* Move diagonally

---

## King

Extends Piece

Responsibilities

* Move one square in any direction
* Cannot move into check

---

## Move

```
Player player
Position source
Position destination
Piece movedPiece
Piece capturedPiece
LocalDateTime timestamp
```

### Responsibilities

* Maintain move history
* Support replay
* Support undo (optional)

---

# Enums

## PieceType

```
KING
QUEEN
ROOK
BISHOP
KNIGHT
PAWN
```

---

## Color

```
WHITE
BLACK
```

---

## GameStatus

```
IN_PROGRESS
CHECK
CHECKMATE
STALEMATE
DRAW
```

---

# Relationships

```
Game
│
├── Board
│      │
│      └── Cell[][] (8 x 8)
│              │
│              └── Piece
│
├── White Player
├── Black Player
│
└── List<Move>
```

---

# Design Patterns Used

### 1. Strategy Pattern

Each chess piece implements its own movement validation logic.

Examples:

* Pawn Movement Strategy
* Rook Movement Strategy
* Bishop Movement Strategy
* Knight Movement Strategy
* Queen Movement Strategy
* King Movement Strategy

---

### 2. Factory Pattern

Used while initializing the chess board and creating chess pieces.

Example:

```
PieceFactory.createPiece(PieceType.ROOK, Color.WHITE)
```

---

### 3. State Pattern

Represents different game states.

Examples

```
IN_PROGRESS

CHECK

CHECKMATE

STALEMATE

DRAW
```

---

### 4. Command Pattern (Optional)

Represents every move as a command.

Useful for

* Undo Move
* Move History
* Replay

---

### 5. Singleton Pattern (Optional)

If a GameManager class is introduced to manage active chess games.

---

# Assumptions

* Standard 8 × 8 chess board.
* Only two players participate.
* One player controls White pieces and the other controls Black pieces.
* White always makes the first move.
* Advanced rules (Castling, En Passant, Pawn Promotion) can be added as future enhancements.
