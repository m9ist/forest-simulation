package ru.neolab.forest.flora;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.Event;
import ru.neolab.forest.fauna.WildlifeSanctuary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Beast {
    private final String beastId;
    /**
     * Степень "голодности" индивидума.
     * 0.0 и меньше - смерть
     * 1.0 - сытость
     */
    private double hunger = 1.0;
    double speed = 1.0;
    private final static AtomicInteger beastCounter = new AtomicInteger(0);

    Beast() {
        beastId = String.format("%s%d", getClass().getSimpleName(), beastCounter.incrementAndGet());
    }

    @Override
    public String toString() {
        return isDead()
                ? "X_X " + beastId
                : beastId;
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

    public double getHunger() {
        return hunger;
    }

    public boolean isDead() {
        return hunger < 1e-8;
    }

    public void chooseMove(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
        hunger = Math.max(0, hunger - 0.1);
        if (isDead()) {
            wildlifeSanctuary.addEvent(new Event.BeastDead(this));
            return;
        }
        final List<Coordinates> possibleMoves = wildlifeSanctuary.getPossibleMoves(wildlifeSanctuary.whereBeast(this), speed);
        wildlifeSanctuary.addEvent(new Event.BeastMove(this, possibleMoves.get((int) (Math.random() * possibleMoves.size()))));
    }

    public double getSpeed() {
        return speed;
    }
}
