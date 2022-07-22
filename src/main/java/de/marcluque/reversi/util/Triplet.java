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

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
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
}
