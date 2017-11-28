package ru.neolab.forest.flora;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.Event;
import ru.neolab.forest.fauna.WildlifeSanctuary;

import java.util.Collection;
import java.util.List;

public class Wolf extends Beast {
    public Wolf() {
        super();
        speed = 2;
    }

    private static final double VISIBLE_DISTANCE = 10;

    @Override
    protected Beast getChildren() {
        return new Wolf();
    }

    @Override
    public double getNeededKilocaloriesAmount() {
        return Math.max(0, (1 - getHunger()) * 500);
    }

    @Override
    protected void chooseMoveBeastAlgorithm(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
        if (getHunger() > 0.8) {
            super.chooseMoveBeastAlgorithm(wildlifeSanctuary);
            return;
        }
        final Coordinates whereBeast = wildlifeSanctuary.whereBeast(this);
        final List<Coordinates> visibleField = wildlifeSanctuary.getPossibleMoves(whereBeast, VISIBLE_DISTANCE);
        Coordinates nearestHare = null;
        for (final Coordinates possibleMove : visibleField) {
            final Collection<Beast> beastsNear = wildlifeSanctuary.getBeasts(possibleMove);
            beastsNear.removeIf(beast -> !(beast instanceof Hare));
            if (beastsNear.isEmpty()) continue;
            if (nearestHare == null || wildlifeSanctuary.getDistance(whereBeast, possibleMove) < wildlifeSanctuary.getDistance(whereBeast, nearestHare)) {
                nearestHare = possibleMove;
            }
        }
        final List<Coordinates> possibleMoves = wildlifeSanctuary.getPossibleMoves(whereBeast, getSpeed());
        final Coordinates to;
        if (nearestHare == null) {
            to = possibleMoves.get((int) (Math.random() * possibleMoves.size()));
        } else {
            Coordinates nearestToHarePoint = null;
            for (final Coordinates possibleMove : possibleMoves) {
                if (nearestToHarePoint == null || wildlifeSanctuary.getDistance(nearestHare, possibleMove) < wildlifeSanctuary.getDistance(nearestHare, nearestToHarePoint)) {
                    nearestToHarePoint = possibleMove;
                }
            }
            if (nearestToHarePoint == null) {
                throw new SanctuaryException("Wolf search path to food is broken");
            }
            to = nearestToHarePoint;
        }
        wildlifeSanctuary.addEvent(new Event.BeastMove(this, to));
    }
}
