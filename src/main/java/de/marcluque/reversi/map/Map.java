package de.marcluque.reversi.map;

import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.Transition;

import java.util.*;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Map {

    private final char[][] gameField;

    protected static final java.util.Map<Transition, Transition> TRANSITIONS = new HashMap<>();

    private static final List<Coordinate> CORNERS = new ArrayList<>();

    private static final List<Coordinate> BOMB_TILES = new ArrayList<>();

    private static final List<Coordinate> OVERRIDE_TILES = new ArrayList<>();

    private static int mapHeight;

    private static int mapWidth;

    private static int numberOfHoles;

    private static int numberOfPlayers;

    private static int bombRadius;

    private static int phase;

    private final int[] overrideStones;

    private final int[] numberOfStones;

    private final int[] bombs;

    private int numberFreeTiles;

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
        this.numberFreeTiles = map.getNumberFreeTiles();
    }

    public char[][] getGameField() {
        return gameField;
    }

    public static java.util.Map<Transition, Transition> getTransitions() {
        return TRANSITIONS;
    }

    public static List<Coordinate> getCorners() {
        return CORNERS;
    }

    public static List<Coordinate> getBombTiles() {
        return BOMB_TILES;
    }

    public static List<Coordinate> getOverrideTiles() {
        return OVERRIDE_TILES;
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

    public int getNumberFreeTiles() {
        return numberFreeTiles;
    }

    public void setNumberFreeTiles(int numberFreeTiles) {
        this.numberFreeTiles = numberFreeTiles;
    }

    public void decrementNumberFreeTiles() {
        numberFreeTiles--;
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