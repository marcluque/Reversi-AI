package de.marcluque.reversi.map;

import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.Transition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class Map {

    private final char[][] gameField;

    protected static java.util.Map<Transition, Transition> transitions = new HashMap<>();

    private static final Set<Coordinate> corners = new HashSet<>();

    private static int mapHeight;

    private static int mapWidth;

    private static int numberOfHoles;

    private static int numberOfPlayers;

    private static int bombRadius;

    private static int phase;

    private final int[] overrideStones;

    private final int[] numberOfStones;

    private final int[] bombs;

    // Public constructor
    public Map(char[][] gameField, int[] overrideStones, int[] numberOfStones, int[] bombs) {
        this.gameField = gameField;
        this.overrideStones = overrideStones;
        this.numberOfStones = numberOfStones;
        this.bombs = bombs;
    }

    // Copy Constructor
    public Map(Map map) {
        this.gameField = Arrays.stream(map.getGameField()).map(char[]::clone).toArray(a -> map.getGameField().clone());
        this.overrideStones = Arrays.copyOf(map.getOverrideStones(), map.getOverrideStones().length);
        this.numberOfStones = Arrays.copyOf(map.getNumberOfStones(), map.getNumberOfStones().length);
        this.bombs = Arrays.copyOf(map.getBombs(), map.getBombs().length);
    }

    public char[][] getGameField() {
        return gameField;
    }

    public static java.util.Map<Transition, Transition> getTransitions() {
        return transitions;
    }

    public static Set<Coordinate> getCorners() {
        return corners;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getNumberOfHoles() {
        return numberOfHoles;
    }

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static int getBombRadius() {
        return bombRadius;
    }

    public static int getPhase() {
        return phase;
    }

    public int[] getOverrideStones() {
        return overrideStones;
    }

    public int[] getNumberOfStones() {
        return numberOfStones;
    }

    public int[] getBombs() {
        return bombs;
    }

    public static void setMapHeight(int mapHeight) {
        Map.mapHeight = mapHeight;
    }

    public static void setMapWidth(int mapWidth) {
        Map.mapWidth = mapWidth;
    }

    public static void setNumberOfHoles(int numberOfHoles) {
        Map.numberOfHoles = numberOfHoles;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Map.numberOfPlayers = numberOfPlayers;
    }

    public static void setBombRadius(int bombRadius) {
        Map.bombRadius = bombRadius;
    }

    public static void setPhase(int phase) {
        Map.phase = phase;
    }
}