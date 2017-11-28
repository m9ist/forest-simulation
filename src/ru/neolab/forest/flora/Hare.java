package ru.neolab.forest.flora;

/**
 * Сжирает порядка 102г комбикорма за сутки, молодняк в районе 50г.
 * В 100г комбикорма содержится порядка 300-320ккал
 */
public class Hare extends Beast {
    public Hare() {
        super();
    }

    @Override
    protected Beast getChildren() {
        return new Hare();
    }

    @Override
    public double getNeededKilocaloriesAmount() {
        return Math.max(0, (1 - getHunger()) * 300);
    }
}
