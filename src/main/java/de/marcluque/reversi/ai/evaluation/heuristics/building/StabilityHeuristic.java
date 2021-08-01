package de.marcluque.reversi.ai.evaluation.heuristics.building;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Transition;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StabilityHeuristic extends AbstractHeuristic implements Heuristic {

    public StabilityHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        final double[] threatCounter = {0};

        MapUtil.iterateMap((x, y) -> {
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
                            : new Coordinate(x + AbstractMove.CORNERS[k][0], y + AbstractMove.CORNERS[k][1]);

                    // Check the potential opposite neighbour in direction k + 4
                    transition = Map.getTransitions().get(new Transition(x, y, k + 4));
                    oppositeNeighbour = (transition != null)
                            ? new Coordinate(transition.getX(), transition.getY())
                            : new Coordinate(x + AbstractMove.CORNERS[k + 4][0], y + AbstractMove.CORNERS[k + 4][1]);

                    if (!MapUtil.isCoordinateInMap(neighbour.getX(), neighbour.getY())
                            || !MapUtil.isCoordinateInMap(oppositeNeighbour.getX(), oppositeNeighbour.getY())) {
                        continue;
                    }

                    boolean neighbourIsThreat = MapUtil.isTileFree(
                            map.getGameField()[neighbour.getY()][neighbour.getX()])
                            && MapUtil.isDifferentPlayerStone(map, oppositeNeighbour.getX(),
                            oppositeNeighbour.getY(), player);

                    boolean oppositeNeighbourIsThreat = MapUtil.isTileFree(
                            map.getGameField()[oppositeNeighbour.getY()][oppositeNeighbour.getX()])
                            && MapUtil.isDifferentPlayerStone(map, neighbour.getX(), neighbour.getY(),
                            player);

                    if (neighbourIsThreat || oppositeNeighbourIsThreat) {
                        threatCounter[0]++;
                    }
                }
            }
        });

        // Divide by number of directions
        return threatCounter[0] / 8;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void updateWeight(double weight) {
        super.weight = weight;
    }
}