package ru.neolab.forest.fauna;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.WildlifeSanctuaryListener;
import ru.neolab.forest.flora.Beast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class WildlifeSanctuary {
    private final int ySize;
    private final int xSize;
    private final ArrayList<Beast> beasts = new ArrayList<>();
    private final ArrayList<WildlifeSanctuaryListener> listeners = new ArrayList<>();

    public WildlifeSanctuary(final int xSize, final int ySize) {
        this.ySize = ySize;
        this.xSize = xSize;
    }

    public boolean possibleMove(final Coordinates from, final Coordinates to) throws SanctuaryException {
        if (!checkIsPossible(from)) {
            throw new SanctuaryException("Impossible " + from);
        }
        if (!checkIsPossible(to)) return false;
        return Math.abs(from.x - to.x) <= 1 && Math.abs(to.x - to.y) <= 1;
    }

    public Collection<Coordinates> getPossibleCoordinates() {
        final LinkedList<Coordinates> coordinates = new LinkedList<>();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                final Coordinates coordinate = new Coordinates(x, y);
                if (checkIsPossible(coordinate)) {
                    coordinates.add(coordinate);
                }
            }
        }
        return coordinates;
    }

    public Collection<Coordinates> getPossibleMoves(final Coordinates from) throws SanctuaryException {
        final LinkedList<Coordinates> coordinates = new LinkedList<>();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                final Coordinates to = new Coordinates(x, y);
                if (possibleMove(from, to)) {
                    coordinates.add(to);
                }
            }
        }
        return coordinates;
    }

    public boolean checkIsPossible(final Coordinates coordinates) {
        return coordinates.x >= 0
                && coordinates.x < xSize
                && coordinates.y >= 0
                && coordinates.y < ySize;
    }

    public void addBeast(final Beast beast) {
        beasts.add(beast);
    }

    private void iteration() {
    }

    public void start(final int iterations) throws InterruptedException {
        for (int i = 0; i < iterations; i++) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            iteration();
            for (final WildlifeSanctuaryListener listener : listeners) {
                listener.changed();
            }
        }
    }

    public void addListener(final WildlifeSanctuaryListener listener) {
        listeners.add(listener);
    }

    public Collection<Beast> getBeasts(final Coordinates coordinate) {
        final ArrayList<Beast> out = new ArrayList<>();
        for (final Beast beast : beasts) {
            if (beast.getCoordinates().equals(coordinate)) {
                out.add(beast);
            }
        }
        return out;
    }
}
