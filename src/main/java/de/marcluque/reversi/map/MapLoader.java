package de.marcluque.reversi.map;

import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Transition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MapLoader {

    private MapLoader() {}

    public static Map generateMapFromString(String mapString) {
        String[] mapLines = mapString.lines().toArray(String[]::new);

        // Players and override stones
        Map.setNumberOfPlayers(Integer.parseInt(mapLines[0]));
        int[] overrideStones = new int[Map.getNumberOfPlayers() + 1];
        overrideStones[1] = Integer.parseInt(mapLines[1]);
        for (int i = 2; i < overrideStones.length; i++) {
            overrideStones[i] = overrideStones[1];
        }

        // Bombs
        String[] s = mapLines[2].split(" ");
        int[] bombs = new int[overrideStones.length];
        bombs[1] = Integer.parseInt(s[0]);
        for (int i = 2; i < bombs.length; i++) {
            bombs[i] = bombs[1];
        }
        Map.setBombRadius(Integer.parseInt(s[1]));

        // Height and width
        s = mapLines[3].split(" ");
        Map.setMapHeight(Integer.parseInt(s[0]));
        Map.setMapWidth(Integer.parseInt(s[1]));

        int[] numberOfStones = new int[bombs.length + 1];

        // Game map
        char[][] map = new char[Map.getMapHeight()][Map.getMapWidth()];
        for (short i = 0; i < Map.getMapHeight(); i++) {
            char[] line = mapLines[4 + i].replace(" ", "").toCharArray();
            for (int j = 0; j < line.length; j++) {
                map[i][j] = line[j];

                /* NUMBER OF STONES PER PLAYER */
                if (MapUtil.isPlayerTile(map[i][j])) {
                    numberOfStones[Character.getNumericValue(map[i][j])]++;
                }
            }
        }

        // Transitions
        Map.getTransitions().clear();
        for (int i = 4 + Map.getMapHeight(); i < mapLines.length; i++) {
            s = mapLines[i].replace(" <-> ", " ").split(" ");

            Transition part1 = new Transition(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
            Transition part2 = new Transition(Integer.parseInt(s[3]), Integer.parseInt(s[4]), Integer.parseInt(s[5]));
            Map.getTransitions().put(part1, part2);
            Map.getTransitions().put(part2, part1);
        }

        return new Map(map, overrideStones, numberOfStones, bombs);
    }

    public static Map generateStringFromMapFile(String absoluteMapPath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(absoluteMapPath))))) {
            return generateMapFromString(br.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            System.err.println("Couldn't read file.");
            e.printStackTrace();
        }

        return null;
    }
}