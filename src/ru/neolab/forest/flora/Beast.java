package ru.neolab.forest.flora;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.WildlifeSanctuary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Beast {
    private final String beastId;
    private final static AtomicInteger beastCounter = new AtomicInteger(0);

    Beast() {
        beastId = String.format("%s%d", getClass().getSimpleName(), beastCounter.incrementAndGet());
    }

    @Override
    public String toString() {
        return beastId;
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

    public void chooseMove(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
        final List<Coordinates> possibleMoves = wildlifeSanctuary.getPossibleMoves(wildlifeSanctuary.whereBeast(this));
        wildlifeSanctuary.goingToMove(this, possibleMoves.get((int) (Math.random() * possibleMoves.size())));
    }
}
