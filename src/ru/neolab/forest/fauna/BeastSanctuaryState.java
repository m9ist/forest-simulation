package ru.neolab.forest.fauna;

import ru.neolab.forest.flora.Beast;

class BeastSanctuaryState {
    final Beast beast;
    Coordinates coordinates;
    boolean isDead;

    BeastSanctuaryState(final Beast beast, final Coordinates coordinates) {
        this.beast = beast;
        this.coordinates = coordinates;
        isDead = beast.isDead();
    }
}
