package de.marcluque.reversi.ai.search;

import java.util.List;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public abstract class AbstractSearch {

    public static char MAX;

    public static List<Character> OPPONENTS;
    protected static final char OPPONENT = '§';

    public static List<Character> ACTIVE_PLAYERS;

    // Only important for minimax algorithms
    public static char MIN = '§';
}