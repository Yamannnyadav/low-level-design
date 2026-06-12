# SNAKE AND LADDER

**Problem Statement**:
Design a detailed low-level-design of Snake and Ladder Game including all object-oriented design patterns
and principles.


**Functional Requirements**:
- Player should be able to roll a dice.
- Player should be able to move based on dice value.
- Player should automatically climb a ladder when landing on its start position.
- Player should automatically slide down a snake when landing on its head.
- Multiple players should be able to play in turns. (currently we have 2 players)
- First player to reach the destination cell wins the game.


**Non-Function Requirements**:
- - System should update the board state after every move with low latency.
- System should maintain consistency of player positions.
- System should be extensible to support multiple dice or custom board sizes.


**Core Entities**:
- Player
- Game Board
- Game
- Dice
- Move
- Jump (Snake or Ladder)


**Entities and their relationship:**

Player
- id
- currPos


GameBoard
- size
- Cell[]


Dice
- diceNum
- max
- min


Jump (Snake and Ladder)
- int start
- int end


Cell
- Jump jump



Game
- GameBoard board
- Dequeue<Player>
- Player Winner
- Dice dice



**Patterns Used**:
- Strategy Pattern for different dice rolling strategies.
- Factory Pattern for creation of board jumps (Snake/Ladder).