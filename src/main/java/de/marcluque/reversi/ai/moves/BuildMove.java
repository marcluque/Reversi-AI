package de.marcluque.reversi.ai.moves;

import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.MoveTriplet;

import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public class BuildMove {

    private BuildMove() {}

    public static MoveTriplet executeBuildMove(Map map, int x, int y, int specialFieldChoice, char player,
                                               Set<Coordinate> capturableStones) {
        int playerId = MapUtil.playerToInt(player);

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
                map.getNumberOfStones()[MapUtil.playerToInt(tile)]--;
            }

            map.getNumberOfStones()[playerId]++;
            map.getGameField()[coordinate.getY()][coordinate.getX()] = player;
        }

        int specialTile = checkForSpecialTile(map, x, y, specialFieldChoice, player);

        return new MoveTriplet(x, y, specialTile);
    }

    private static int checkForSpecialTile(Map map, int x, int y, int specialFieldChoice, char player) {
        // Check whether field is a special field
        int specialTile = 0;
        int playerId = MapUtil.playerToInt(player);
        switch (map.getGameField()[y][x]) {
            case 'c':
                map.getGameField()[y][x] = player;
                specialTile = computeChoiceTile(map, player, specialFieldChoice);
                break;
            case 'i':
                map.getGameField()[y][x] = player;
                computeInversionTile(map);
                break;
            case 'b':
                map.getGameField()[y][x] = player;
                specialTile = computeBonusTile(map, specialFieldChoice, playerId);
                break;
            default:
                // Fall-through since we do not have a special tile
        }

        return specialTile;
    }

    private static int computeChoiceTile(Map map, char player, int specialFieldChoice) {
        int playerId = MapUtil.playerToInt(player);

        // ASSUMPTION: we always pick the player with most stones
        int specialTile = specialFieldChoice == 0 ? MapUtil.playerWithMaxStones(map) : specialFieldChoice;

        // Switch stones of the player in numberOfStones array
        int temp = map.getNumberOfStones()[specialTile];
        map.getNumberOfStones()[specialTile] = map.getNumberOfStones()[playerId];
        map.getNumberOfStones()[playerId] = temp;

        // Switch stones of the player specified in playerWithMostStones
        char playerWithMostStones = MapUtil.intToPlayer(specialTile);
        for (int newY = 0, height = Map.getMapHeight(); newY < height; newY++) {
            for (int newX = 0, width = Map.getMapWidth(); newX < width; newX++) {
                if (map.getGameField()[newY][newX] == playerWithMostStones) {
                    map.getGameField()[newY][newX] = player;
                } else if (map.getGameField()[newY][newX] == player) {
                    map.getGameField()[newY][newX] = playerWithMostStones;
                }
            }
        }

        return specialTile;
    }

    private static void computeInversionTile(Map map) {
        int[] tempStones = new int[map.getNumberOfStones().length];
        // Switches the stones of the players
        for (int switchPlayer = 1, numberOfPlayers = Map.getNumberOfPlayers(); switchPlayer <= numberOfPlayers; switchPlayer++) {
            int nextPlayer = (switchPlayer % Map.getNumberOfPlayers()) + 1;
            tempStones[nextPlayer] = map.getNumberOfStones()[switchPlayer];
        }
        System.arraycopy(tempStones, 0, map.getNumberOfStones(), 0, map.getNumberOfStones().length);

        // Iterates over all players and switches their stones with (i % numberOfPlayers) + 1 where i is the player
        for (int newY = 0, height = Map.getMapHeight(); newY < height; newY++) {
            for (int newX = 0, width = Map.getMapWidth(); newX < width; newX++) {
                if (MapUtil.isPlayerTile(map.getGameField()[newY][newX])) {
                    int playerNumber = Character.getNumericValue(map.getGameField()[newY][newX]);
                    map.getGameField()[newY][newX] = MapUtil.intToPlayer((playerNumber % Map.getNumberOfPlayers()) + 1);
                }
            }
        }
    }

    private static int computeBonusTile(Map map, int specialFieldChoice, int playerId) {
        int specialTile;
        if (specialFieldChoice != 0) {
            specialTile = specialFieldChoice;
        } else if (Rules.isPickOverrideStoneOverBomb()) {
            specialTile = 21;
        } else {
            specialTile = 20;
        }

        if (specialTile == 21) {
            map.getOverrideStones()[playerId]++;
        } else {
            map.getBombs()[playerId]++;
        }

        return specialTile;
    }
}