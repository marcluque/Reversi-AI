package de.marcluque.reversi.ai.search;

import java.util.HashSet;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public abstract class AbstractSearch {

    protected AbstractSearch() {}

    private static char max;

    private static int maxId;

    // Only important for minimax algorithms
    private static char min;

    private static final Set<Character> OPPONENTS = new HashSet<>();

    private static final char OPPONENT = '§';

    private static final Set<Character> ACTIVE_PLAYERS = new HashSet<>();

    public static char getMax() {
        return max;
    }

    public static void setMax(char max) {
        AbstractSearch.max = max;
    }

    public static int getMaxId() {
        return maxId;
    }

    public static void setMaxId(int maxId) {
        AbstractSearch.maxId = maxId;
    }

    public static char getMin() {
        return min;
    }

    public static void setMin(char min) {
        AbstractSearch.min = min;
    }

    public static Set<Character> getOPPONENTS() {
        return OPPONENTS;
    }

    public static char getOPPONENT() {
        return OPPONENT;
    }

    public static Set<Character> getActivePlayers() {
        return ACTIVE_PLAYERS;
    }
}