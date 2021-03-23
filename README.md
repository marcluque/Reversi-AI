# Reversi-AI
This piece of software implements artificial intelligence that is based on informed search MiniMax algorithms and their extensions.
The game played is Reversi. However, this version of Reversi is tweaked to be harder and not playable for humans.
This poses a greater challenge for AI. The exact rules of the tweaked Reversi game are explained in the [Game](#game) section.
The AI communicates with a game master that is a [server](https://github.com/marcluque/Reversi-Server).
The communication is done according to the protocol defined in the [Protocol](#protocol) section.

## Rules
The tweaked version of Reversi comprises several rules that make the game more difficult.
These rules are defined in the following.
Afterward, the protocol for the communication of the client and server is explained.

### Game

### Protocol

## Algorithms
This version of Reversi allows for two or more players.
A 2-player game allows for the commonly known MiniMax algorithm that assumes two players (Min and Max).
However, an n-player scenario is more difficult than that. More players allow for more intricate situations than just one opponent.
Just consider the idea of a coalition that your opponents could form.  
The [2-Player Algorithms](#2-player-algorithms) section below presents MiniMax and its extensions.  
The [Multi-Player Algorithms](#multi-player-algorithms) section presents various algorithms for multi-player scenarios.

### 2-Player Algorithms
The 2-player algorithms use the classical scenario for the MiniMax algorithm and its extensions.
The following algorithms are implemented in the Reversi-AI with
the help of the book [Artificial Intelligence: A Modern Approach](https://dl.acm.org/doi/10.5555/1671238).

#### MiniMax
An algorithm that performs an informed search.
The search tries to find a path or move in a state space that minimizes the losing chances for itself and maximizes them for the opponent.
It is important to note that this algorithm explores the whole state space.

#### Alpha-Beta
An extension of the MiniMax algorithm tries to reduce the searched state space by neglecting "uninteresting" paths.
If at some point it is already clear that an explored path is more promising than an unexplored path, it is pruned.

#### Iterative Deepening
Iterative Deepening is a technique that may be applied regardless of the type of informed search.
It is best described by thinking of an iterative exploration of the state space.
The depth for the search is increased with each iteration, such that each iteration increases the search depth by 1.
This also means that every iteration the search has to start from the root of the tree.

#### Move Sorting (aka Move Ordering)
The best case for Alpha-Beta search is the case where the best move is explored first.
All other moves with worse scores may then be pruned.
This can be achieved by sorting the nodes that will be explored.

#### Aspiration Windows
When using Iterative Deepening, the search space may be reduced by using so-called aspiration windows for the alpha and beta values.
The values are usually retrieved from the previous iteration of the search.
However, when picking a too narrow window, the best move might be missed.

### Multi-Player Algorithms
The following algorithms try several approaches for multi-player situations.
The implementations do not rely on a book, like the standard MiniMax and its extensions. 
Instead, the algorithms are implemented with the help of their respective papers, all the papers are linked in their respective sections.   

#### Max^N
The implementation is taken from the following paper [An Algorithmic Solution Of N-Person Games](https://www.aaai.org/Papers/AAAI/1986/AAAI86-025.pdf).
Note that the book [Artificial Intelligence: A Modern Approach](https://dl.acm.org/doi/10.5555/1671238) also provides an explanation of the idea for Max^N.

#### Paranoid Search
The implementation is taken from the following paper [On Pruning Techniques for Multi-Player Games](https://www.aaai.org/Papers/AAAI/2000/AAAI00-031.pdf).

#### Best-Reply Search (aka BRS)
The implementation is taken from the following paper [Best-Reply Search for Multi-Player Games](https://dke.maastrichtuniversity.nl/m.winands/documents/BestReplySearch.pdf).

#### Opponent-Pruning Paranoid Search (aka OPPS)
The implementation is taken from the following paper [Opponent-Pruning Paranoid Search](https://dl.acm.org/doi/10.1145/3402942.3402957).

## Heuristics


## Server
This client is used to play Reversi with the [server](https://github.com/marcluque/Reversi-Server) as a game master.
The client communicates with the server by using the above-mentioned protocol.
The game is played according to the above-defined rules.

## Running it

## License
Licensed under the BSD 2-Clause License - see the [LICENSE](https://github.com/marcluque/Reversi-AI/blob/master/LICENSE) file for details.