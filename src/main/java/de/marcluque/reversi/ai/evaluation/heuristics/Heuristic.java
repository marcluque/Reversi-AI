package de.datasecs.reversi.ai.evaluation.heuristics;

import de.datasecs.reversi.map.Map;

public interface Heuristic {
    
    void initHeuristic(Map map);

    double executeHeuristic(Map map, char player);
}