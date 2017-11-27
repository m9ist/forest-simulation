package ru.neolab.forest.flora;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.WildlifeSanctuary;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Beast {
    private Coordinates coordinates;
    private final String beastId;
    private final static AtomicInteger beastCounter = new AtomicInteger(0);

    protected Beast(final Coordinates coordinates) {
        this.coordinates = coordinates;
        beastId = String.format("%s%d", getClass().getSimpleName(), beastCounter.incrementAndGet());
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return String.format("%s %s", beastId, getCoordinates());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Beast beast = (Beast) o;

        return beastId.equals(beast.beastId);
    }

    @Override
    public int hashCode() {
        return beastId.hashCode();
    }

    public Coordinates chooseMove(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
        final List<Coordinates> possibleMoves = wildlifeSanctuary.getPossibleMoves(coordinates);
        return possibleMoves.get((int) (Math.random() * possibleMoves.size()));
    }

    public void move(final Coordinates coordinates, final WildlifeSanctuary wildlifeSanctuary) {
        this.coordinates = coordinates;
    }
}
