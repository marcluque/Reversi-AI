package de.datasecs.reversi.ai.evaluation.heuristics.building;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.moves.Move;
import de.datasecs.reversi.util.Coordinate;
import de.datasecs.reversi.util.MapUtil;
import de.datasecs.reversi.util.Transition;

public class StabilityHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {

    }

    @Override
    public double executeHeuristic(Map map, char player, boolean allowOverrideStones) {
        int threatCounter = 0;

        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                if (map.getGameField()[y][x] == player) {
                    Transition transition;
                    Coordinate neighbour;
                    Coordinate oppositeNeighbour;

                    for (int k = 0; k < 4; k++) {
                        // Checks whether a path via a transition can be enclosed
                        // Check the potential neighbour in direction k
                        transition = Map.getTransitions().get(new Transition(x, y, k));
                        neighbour = (transition != null)
                                ? new Coordinate(transition.getX(), transition.getY())
                                : new Coordinate(x + Move.CORNERS[k][0], y + Move.CORNERS[k][1]);

                        // Check the potential opposite neighbour in direction k + 4
                        transition = Map.getTransitions().get(new Transition(x, y, k + 4));
                        oppositeNeighbour = (transition != null)
                                ? new Coordinate(transition.getX(), transition.getY())
                                : new Coordinate(x + Move.CORNERS[k + 4][0], y + Move.CORNERS[k + 4][1]);

                        boolean threatInMap = MapUtil.isCoordinateInMap(neighbour.getX(), neighbour.getY())
                                && MapUtil.isCoordinateInMap(oppositeNeighbour.getX(), oppositeNeighbour.getY());
                        boolean neighbourIsThreat = MapUtil.isTileFree(map.getGameField()[neighbour.getY()][neighbour.getX()])
                                && MapUtil.isDifferentPlayerStone(map, oppositeNeighbour.getX(), oppositeNeighbour.getY(), player);
                        boolean oppositeNeighbourIsThreat = MapUtil.isTileFree(map.getGameField()[oppositeNeighbour.getY()][oppositeNeighbour.getX()])
                                && MapUtil.isDifferentPlayerStone(map, neighbour.getX(), neighbour.getY(), player);

                        // Threat is found
                        if (threatInMap && (neighbourIsThreat || oppositeNeighbourIsThreat)) {
                            threatCounter++;
                        }
                    }
                }
            }
        }

        // Divide by number of directions
        return threatCounter / 8d;
    }
}