package ru.neolab.forest.fauna;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.WildlifeSanctuaryListener;
import ru.neolab.forest.flora.Beast;

import java.util.*;

public class WildlifeSanctuary {
    private final int ySize;
    private final int xSize;
    private final HashMap<Beast, Coordinates> beasts = new HashMap<>();
    private final ArrayList<WildlifeSanctuaryListener> listeners = new ArrayList<>();

    public WildlifeSanctuary(final int xSize, final int ySize) {
        this.ySize = ySize;
        this.xSize = xSize;
    }

    private boolean possibleMove(final Coordinates from, final Coordinates to) throws SanctuaryException {
        if (!checkIsPossible(from)) {
            throw new SanctuaryException("Impossible " + from);
        }
        return checkIsPossible(to)
                && Math.abs(from.x - to.x) <= 1
                && Math.abs(from.y - to.y) <= 1;
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

    public List<Coordinates> getPossibleMoves(final Coordinates from) throws SanctuaryException {
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

    boolean checkIsPossible(final Coordinates coordinates) {
        return coordinates.x >= 0
                && coordinates.x < xSize
                && coordinates.y >= 0
                && coordinates.y < ySize;
    }

    public void addBeast(final Beast beast, final Coordinates coordinates) {
        beasts.put(beast, coordinates);
    }

    private final ArrayList<Event> events = new ArrayList<>();

    private void iteration() throws SanctuaryException {
        if (!events.isEmpty()) {
            throw new SanctuaryException("Not empty state");
        }
        for (final Beast beast : beasts.keySet()) {
            beast.chooseMove(this);
        }
        // меняем этот мир....
        for (final Event event : events) {
            event.apply(this);
        }
        events.clear();
    }

    public void start(final int iterations, final long delay) throws InterruptedException, SanctuaryException {
        for (int i = 0; i < iterations; i++) {
            Thread.sleep(delay);
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
        for (final Map.Entry<Beast, Coordinates> entry : beasts.entrySet()) {
            if (entry.getValue().equals(coordinate)) {
                out.add(entry.getKey());
            }
        }
        return out;
    }

    public void goingToMove(final Beast beast, final Coordinates to) throws SanctuaryException {
        if (!possibleMove(whereBeast(beast), to)) {
            throw new SanctuaryException("Can't move " + beast + " to " + to);
        }
        events.add(new Event.BeastMove(beast, to));
    }

    public Coordinates whereBeast(final Beast beast) {
        return beasts.get(beast);
    }

    void moveBeast(final Beast beast, final Coordinates to) throws SanctuaryException {
        final Coordinates from = beasts.get(beast);
        if (!possibleMove(from, to)) {
            throw new SanctuaryException("Can't move " + beast + " to " + to);
        }
        beasts.put(beast, to);
    }
}
