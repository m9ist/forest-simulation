package ru.neolab.forest.flora;

import ru.neolab.forest.fauna.Coordinates;

public abstract class Beast {
    private Coordinates coordinates;

    protected Beast(final Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), getCoordinates());
    }
}
