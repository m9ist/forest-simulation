package ru.neolab.forest;

public class Coordinates {
    public final int x;
    public final int y;

    public Coordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
