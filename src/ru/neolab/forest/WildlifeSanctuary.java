package ru.neolab.forest;

import java.util.Collection;
import java.util.LinkedList;

public class WildlifeSanctuary {
    private final int ySize;
    private final int xSize;

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
}
