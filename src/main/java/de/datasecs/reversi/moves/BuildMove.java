package de.datasecs.reversi.moves;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.Coordinate;
import de.datasecs.reversi.util.MapUtil;
import de.datasecs.reversi.util.Move;

import java.util.List;

public abstract class BuildMove {

    public static Move executeBuildMove(Map map, int x, int y, char player, List<Coordinate> capturableStones) {
        int playerId = Character.getNumericValue(player);

        if (MapUtil.isOccupied(map.getGameField()[y][x])) {
            map.getOverrideStones()[playerId]--;
        }

        capturableStones.forEach(coordinate -> {
            char tile = map.getGameField()[coordinate.getY()][coordinate.getX()];

            // If a special tile is encountered, ignore, as it's dealt with separately
            if (MapUtil.isTileSpecial(tile)) {
                map.getNumberOfStones()[playerId]++;
                return;
            } else if (MapUtil.isPlayerTile(tile)) {
                map.getNumberOfStones()[Character.getNumericValue(tile)]--;
            }

            map.getNumberOfStones()[playerId]++;
            map.getGameField()[coordinate.getY()][coordinate.getX()] = player;
        });

        // Check whether field is a special field
        int specialTile = -1;
        switch (map.getGameField()[y][x]) {
            case 'c' -> {
                map.getGameField()[y][x] = player;

                // HEURISTIC: Determine player with most stones for choice
                int playerWithMostStones = player;
                int max = -1;
                for (int i = 1; i <= 8; i++) {
                    if (map.getNumberOfStones()[i] > max) {
                        max = map.getNumberOfStones()[i];
                        playerWithMostStones = i;
                    }
                }

                specialTile = playerWithMostStones;

                // Switch stones of the player in numberOfStones array
                int temp = map.getNumberOfStones()[playerWithMostStones];
                map.getNumberOfStones()[playerWithMostStones] = map.getNumberOfStones()[playerId];
                map.getNumberOfStones()[playerId] = temp;

                // Switches stones of the player specified in choiceStonePlayer
                char playerWithMostStonesChar = (char) (playerWithMostStones + 96);
                for (int i = 0; i < map.getGameField().length; i++) {
                    for (int j = 0; j < map.getGameField()[0].length; j++) {
                        if (map.getGameField()[i][j] == playerWithMostStonesChar) {
                            map.getGameField()[i][j] = player;
                        } else if (map.getGameField()[i][j] == player) {
                            map.getGameField()[i][j] = playerWithMostStonesChar;
                        }
                    }
                }

            }
            case 'i' -> {
                map.getGameField()[y][x] = player;

                // Switches the stones of the players
                for (int i = 1; i <= 8; i++) {
                    int nextPlayer = (i % Map.getNumberOfPlayers()) + 1;
                    int tempNumber = map.getNumberOfStones()[i];
                    map.getNumberOfStones()[i] = map.getNumberOfStones()[nextPlayer];
                    map.getNumberOfStones()[nextPlayer] = tempNumber;
                }

                // Iterates over all players and switches their stones with (i % numberOfPlayers) + 1 where i is the player
                for (int i = 0; i < map.getGameField().length; i++) {
                    for (int j = 0; j < map.getGameField()[0].length; j++) {
                        if (MapUtil.isPlayerTile(map.getGameField()[i][j])) {
                            map.getGameField()[i][j] = (char) ((Character.getNumericValue(map.getGameField()[i][j]) % Map.getNumberOfPlayers()) + 1);
                        }
                    }
                }
            }
            case 'b' -> {
                map.getGameField()[y][x] = player;

                // HEURISTIC: If 2 players and map smaller than 17x17, player picks bomb, else override
                if (Map.getNumberOfPlayers() == 2 && Map.getMapWidth() * Map.getMapHeight() <= 300) {
                    map.getBombs()[playerId]++;
                    specialTile = 20;
                } else {
                    map.getOverrideStones()[playerId]++;
                    specialTile = 21;
                }
            }
        }

        return new Move(x, y, specialTile);
    }
}