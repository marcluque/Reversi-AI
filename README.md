# Reversi-AI
This piece of software implements artificial intelligence that is based on informed search MiniMax algorithms and their extensions.
The game played is Reversi. However, this version of Reversi is tweaked to be harder and not playable for humans anymore.
This poses a greater challenge for AI. The exact rules of the tweaked Reversi game are explained below.
The AI communicates with a game master that is a [server](https://github.com/DataSecs/Reversi-Server).
The communication is done according to the protocol defined below.

## Rules
The tweaked version of Reversi comprises several rules that make the game more difficult.
These rules are defined in the following.
Afterward, the protocol for the communication of the client and server is explained.

### Game


### Protocol

## Algorithms
The following algorithms are implemented in the Reversi-AI with the help of the book [Artificial Intelligence: A Modern Approach](http://aima.cs.berkeley.edu/).

### MiniMax
An algorithm that performs an informed search.
The search tries to find a path or move in a state space that minimizes the losing chances for itself and maximizes them for the opponent.
It is important to note that this algorithm explores the whole state space.

### Alpha-Beta
An extension of the MiniMax algorithm tries to reduce the searched state space by neglecting "uninteresting" paths.
If at some point it is already clear that an explored path is more promising than an unexplored path, it is pruned.

### Iterative Deepening
Iterative Deepening is a technique that may be applied regardless of the type of informed search.
It is best described by thinking of an iterative exploration of the state space.
The depth for the search is increased with each iteration, such that each iteration increases the search depth by 1.
This also means that every iteration the search has to start from the root of the tree.

### Move Ordering
The best case for Alpha-Beta search is the case where the best move is explored first.
All other moves with worse scores may then be pruned.
This can be achieved by sorting the nodes that will be explored.

### Aspiration Windows
When using Iterative Deepening, the search space may be reduced by using so-called aspiration windows for the alpha and beta values.
The values are usually retrieved from the previous iteration of the search.
However, when picking a too narrow window, the best move might be missed.

## Heuristics

## Server
This client is used to play Reversi with the [server](https://github.com/DataSecs/Reversi-Server) as a game master.
The client communicates with the server by using the above-mentioned protocol.
The game is played according to the above-defined rules.

## Running it

## License
Licensed under the BSD 2-Clause License - see the [LICENSE](https://github.com/DataSecs/Reversi-AI/blob/master/LICENSE) file for details.
