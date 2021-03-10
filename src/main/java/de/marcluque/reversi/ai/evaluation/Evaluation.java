package de.datasecs.reversi.ai.evaluation;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;

import java.util.LinkedList;
import java.util.List;

public class Evaluation {

    private static final List<Heuristic> heuristics = new LinkedList<>();

    public void addHeuristic(Heuristic heuristic) {
        heuristics.add(heuristic);
    }

    public void initHeuristics(Map map) {
        heuristics.forEach(heuristic -> heuristic.initHeuristic(map));
    }

    public static double utility(Map map, char player) {
        return heuristics.stream()
                .mapToDouble(heuristic -> heuristic.executeHeuristic(map, player))
                .sum();
    }
}