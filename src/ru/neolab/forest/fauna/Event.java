package ru.neolab.forest.fauna;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.flora.Beast;

public abstract class Event {
    static class BeastMove extends Event {
        final Beast beast;
        final Coordinates to;

        BeastMove(final Beast beast, final Coordinates to) {
            this.beast = beast;
            this.to = to;
        }

        @Override
        void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
            wildlifeSanctuary.moveBeast(beast, to);
        }
    }

    abstract void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
