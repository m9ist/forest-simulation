package ru.neolab.forest.flora;

public class Wolf extends Beast {
    public Wolf() {
        super();
        speed = 2;
    }

    @Override
    protected Beast getChildren() {
        return new Wolf();
    }

    @Override
    public double getNeededKilocaloriesAmount() {
        throw new RuntimeException("Not implemented");
    }
}
