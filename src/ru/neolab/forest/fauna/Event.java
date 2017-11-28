package ru.neolab.forest.fauna;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.flora.Beast;

public abstract class Event {
    public static class BeastMove extends Event {
        final Beast beast;
        final Coordinates to;

        public BeastMove(final Beast beast, final Coordinates to) {
            this.beast = beast;
            this.to = to;
        }

        @Override
        void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
            wildlifeSanctuary.moveBeast(beast, to);
        }
    }

    public static class BeastDead extends Event {
        final Beast beast;

        public BeastDead(final Beast beast) {
            this.beast = beast;
        }

        @Override
        void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
            wildlifeSanctuary.beastDead(beast);
        }
    }

    public static class BeastBeBorn extends Event {
        final Beast beast;
        final Coordinates coordinates;

        public BeastBeBorn(final Beast beast, final Coordinates coordinates) {
            this.beast = beast;
            this.coordinates = coordinates;
        }

        @Override
        void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
            wildlifeSanctuary.addBeast(beast, coordinates);
        }
    }

    static class BeastAte extends Event {
        final Beast beast;
        final double kilocalories;

        BeastAte(final Beast beast, final double kilocalories) {
            this.beast = beast;
            this.kilocalories = kilocalories;
        }

        @Override
        void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
            wildlifeSanctuary.beastAte(beast, kilocalories);
        }
    }

    abstract void apply(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
