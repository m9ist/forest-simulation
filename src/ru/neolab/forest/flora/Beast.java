package ru.neolab.forest.flora;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.Event;
import ru.neolab.forest.fauna.WildlifeSanctuary;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    /**
     * Нетолерантность в чистом виде, задо оптимально. Мужик - true, деваха - ytn.
     */
    private final boolean male;
    /**
     * Сколько надо ходов перед тем как:
     * 1. Захотеть самку после спаривания для мужика
     * 2. Самке если она беременна, сколько надо на родить
     */
    private int stepsToWantSex = 0;

    Beast() {
        male = Math.random() > 0.45;
        beastId = String.format("%s%d%s", getClass().getSimpleName(), beastCounter.incrementAndGet(), male ? "male" : "female");
    }

    /**
     * @return сгенерировать отпрыска
     */
    protected abstract Beast getChildren();

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

    private static final double HUNGER_STATE_TO_REPRODUCTION = 0.6;
    private static final double HUNGER_LOOSE_PER_STEP = 0.01;

    public void chooseMove(final WildlifeSanctuary wildlifeSanctuary) throws SanctuaryException {
        hunger = Math.max(0, hunger - HUNGER_LOOSE_PER_STEP);
        if (isDead()) {
            wildlifeSanctuary.addEvent(new Event.BeastDead(this));
            return;
        }
        if (stepsToWantSex > 0) {
            stepsToWantSex--;
            if (stepsToWantSex == 0 && !male) {
                // родилось новое животное!
                final Beast children = getChildren();
                if (children.getClass() != getClass()) {
                    throw new SanctuaryException(String.format("%s give birth to %s", this, children));
                }
                wildlifeSanctuary.addEvent(new Event.BeastBeBorn(children, wildlifeSanctuary.whereBeast(this)));
            }
        }
        if (hunger > HUNGER_STATE_TO_REPRODUCTION && stepsToWantSex == 0) {
            final Collection<Beast> beasts = wildlifeSanctuary.getBeasts(wildlifeSanctuary.whereBeast(this));
            final Optional<Beast> first = beasts.stream().filter(beast -> beast.getClass() == getClass() && beast.male != male && beast.stepsToWantSex == 0).findFirst();
            if (first.isPresent()) {
                // СЕКАС!
                final Beast maleBeast = male ? this : first.get();
                final Beast femaleBeast = !male ? this : first.get();
                maleBeast.stepsToWantSex = 1;
                femaleBeast.stepsToWantSex = 5;
            }
        }
        final List<Coordinates> possibleMoves = wildlifeSanctuary.getPossibleMoves(wildlifeSanctuary.whereBeast(this), speed);
        wildlifeSanctuary.addEvent(new Event.BeastMove(this, possibleMoves.get((int) (Math.random() * possibleMoves.size()))));
    }

    /**
     * В теории скорость можно варьировать. Скажем если мы вводим видимость и направленность животного на какую-то цель,
     * скажем движение за самкой ради воспроизводства, то мы можем ради этого чутка увелививать прыть животного... Ну и тд.
     */
    public double getSpeed() {
        return speed;
    }
}
