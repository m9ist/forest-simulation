package ru.neolab.forest.flora;

public class Hare extends Beast {
    public Hare() {
        super();
    }

    @Override
    protected Beast getChildren() {
        return new Hare();
    }
}
