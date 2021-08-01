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

    public static Move executeBuildMove(Map map, int x, int y, int specialField, char player, List<Coordinate> capturableStones) {
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
        int specialTile = 0;
        switch (map.getGameField()[y][x]) {
            case 'c' -> {
                map.getGameField()[y][x] = player;

                // ASSUMPTION: we always pick the player with most stones
                specialTile = specialField == 0 ? MapUtil.playerWithMaxStones(map) : specialField;

                // Switch stones of the player in numberOfStones array
                int temp = map.getNumberOfStones()[specialTile];
                map.getNumberOfStones()[specialTile] = map.getNumberOfStones()[playerId];
                map.getNumberOfStones()[playerId] = temp;

                // Switch stones of the player specified in playerWithMostStones
                char playerWithMostStones = MapUtil.intToPlayer(specialTile);
                MapUtil.iterateMap((newX, newY) -> {
                    if (map.getGameField()[newY][newX] == playerWithMostStones) {
                        map.getGameField()[newY][newX] = player;
                    } else if (map.getGameField()[newY][newX] == player) {
                        map.getGameField()[newY][newX] = playerWithMostStones;
                    }
                });

            }
            case 'i' -> {
                map.getGameField()[y][x] = player;

                int[] tempStones = new int[map.getNumberOfStones().length];
                // Switches the stones of the players
                for (int switchPlayer = 1, numberOfPlayers = Map.getNumberOfPlayers(); switchPlayer <= numberOfPlayers; switchPlayer++) {
                    int nextPlayer = (switchPlayer % Map.getNumberOfPlayers()) + 1;
                    tempStones[nextPlayer] = map.getNumberOfStones()[switchPlayer];
                }
                System.arraycopy(tempStones, 0, map.getNumberOfStones(), 0, map.getNumberOfStones().length);

                // Iterates over all players and switches their stones with (i % numberOfPlayers) + 1 where i is the player
                MapUtil.iterateMap((newX, newY) -> {
                    if (MapUtil.isPlayerTile(map.getGameField()[newY][newX])) {
                        map.getGameField()[newY][newX] = MapUtil.intToPlayer((Character.getNumericValue(map.getGameField()[newY][newX]) % Map.getNumberOfPlayers()) + 1);
                    }
                });
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