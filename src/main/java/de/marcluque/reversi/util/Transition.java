package de.marcluque.reversi.util;

import java.util.Objects;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class Transition {

    private int x;

    private int y;

    private int direction;

    public Transition(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transition transition = (Transition) o;
        return x == transition.x && y == transition.y && direction == transition.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, direction);
    }

    @Override
    public String toString() {
        return "(" + x +", " + y + ", " + direction + ')';
    }
}