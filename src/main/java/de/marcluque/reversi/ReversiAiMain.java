package de.marcluque.reversi;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneCountHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneParityHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.CornerHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.MobilityHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.StabilityHeuristic;
import de.marcluque.reversi.network.Client;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class ReversiAiMain {

    public static void main(String[] args) {
        Evaluation.addBuildingHeuristic(new StoneCountHeuristic(0.2));
        Evaluation.addBuildingHeuristic(new StoneParityHeuristic(0.2));
        Evaluation.addBuildingHeuristic(new CornerHeuristic(0.2));
        Evaluation.addBuildingHeuristic(new MobilityHeuristic(0.2));
        Evaluation.addBuildingHeuristic(new StabilityHeuristic(0.2));

        Evaluation.setSortingHeuristic(new StoneParityHeuristic(1));

        new Client("127.0.0.1", 8080).start();
    }
}