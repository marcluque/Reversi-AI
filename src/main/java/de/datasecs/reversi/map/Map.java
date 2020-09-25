package de.datasecs.reversi.map;

import de.datasecs.reversi.util.Transition;

import java.util.Arrays;
import java.util.HashMap;

public class Map {

    private char[][] gameField;

    protected static java.util.Map<Transition, Transition> transitions = new HashMap<>();

    private static int mapHeight;

    private static int mapWidth;

    private static int numberOfHoles;

    private static int numberOfPlayers;

    private static int bombRadius;

    private int[] overrideStones;

    private int[] numberOfStones;

    private int[] bombs;

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

    public void setGameField(char[][] gameField) {
        this.gameField = gameField;
    }
}