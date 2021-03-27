package de.marcluque.reversi.ai.moves;

import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;

import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public abstract class BuildMove {

    public static Move executeBuildMove(Map map, int x, int y, char player, List<Coordinate> capturableStones) {
        int playerId = Character.getNumericValue(player);

        if (MapUtil.isOccupied(map.getGameField()[y][x])) {
            map.getOverrideStones()[playerId]--;
        }

        for (Coordinate coordinate : capturableStones) {
            char tile = map.getGameField()[coordinate.getY()][coordinate.getX()];

            // If a special tile is encountered, ignore, as it's dealt with separately
            if (MapUtil.isTileSpecial(tile)) {
                map.getNumberOfStones()[playerId]++;
                continue;
            } else if (MapUtil.isPlayerTile(tile)) {
                map.getNumberOfStones()[Character.getNumericValue(tile)]--;
            }

            map.getNumberOfStones()[playerId]++;
            map.getGameField()[coordinate.getY()][coordinate.getX()] = player;
        }

        // Check whether field is a special field
        int specialTile = -1;
        switch (map.getGameField()[y][x]) {
            case 'c' -> {
                map.getGameField()[y][x] = player;

                // ASSUMPTION: we always pick the player with most stones
                specialTile = MapUtil.playerWithMaxStones(map);

                // Switch stones of the player in numberOfStones array
                int temp = map.getNumberOfStones()[specialTile];
                map.getNumberOfStones()[specialTile] = map.getNumberOfStones()[playerId];
                map.getNumberOfStones()[playerId] = temp;

                // Switches stones of the player specified in choiceStonePlayer
                char playerWithMostStones = MapUtil.intToPlayer(specialTile);
                for (int i = 0; i < map.getGameField().length; i++) {
                    for (int j = 0; j < map.getGameField()[0].length; j++) {
                        if (map.getGameField()[i][j] == playerWithMostStones) {
                            map.getGameField()[i][j] = player;
                        } else if (map.getGameField()[i][j] == player) {
                            map.getGameField()[i][j] = playerWithMostStones;
                        }
                    }
                }

            }
            case 'i' -> {
                map.getGameField()[y][x] = player;

                // Switches the stones of the players
                for (int switchPlayer = 1, numberOfPlayers = Map.getNumberOfPlayers(); switchPlayer <= numberOfPlayers; switchPlayer++) {
                    int nextPlayer = (switchPlayer % Map.getNumberOfPlayers()) + 1;
                    int tempNumber = map.getNumberOfStones()[switchPlayer];
                    map.getNumberOfStones()[switchPlayer] = map.getNumberOfStones()[nextPlayer];
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

                if (Rules.pickOverrideStoneOverBomb) {
                    map.getOverrideStones()[playerId]++;
                    specialTile = 21;
                } else {
                    map.getBombs()[playerId]++;
                    specialTile = 20;
                }
            }
        }

        return new Move(x, y, specialTile);
    }
}