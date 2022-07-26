package de.marcluque.reversi.util;

import java.util.Objects;

public class Triplet {

    protected final int x;

    protected final int y;

    protected final int z;

    public Triplet(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triplet)) return false;

        Triplet triplet = (Triplet) o;

        if (x != triplet.x) return false;
        if (y != triplet.y) return false;
        return z == triplet.z;
    }
}
