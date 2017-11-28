package ru.neolab.forest;

import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.WildlifeSanctuary;
import ru.neolab.forest.flora.Hare;
import ru.neolab.forest.flora.Wolf;
import ru.neolab.forest.swing.Drozdov;

import java.util.List;

public class ForestSimulationLauncher {
    public static void main(final String[] args) throws InterruptedException, SanctuaryException {
        final WildlifeSanctuary wildlifeSanctuary = new WildlifeSanctuary(50, 20);
        final List<Coordinates> possibleCoordinates = wildlifeSanctuary.getPossibleCoordinates();
        final int numWolfs = 3;
        final int numHares = 5;
        for (int i = 0; i < numWolfs; i++) {
            wildlifeSanctuary.addBeast(new Wolf(), possibleCoordinates.get((int) (Math.random() * possibleCoordinates.size())));
        }
        for (int i = 0; i < numHares; i++) {
            wildlifeSanctuary.addBeast(new Hare(), possibleCoordinates.get((int) (Math.random() * possibleCoordinates.size())));
        }

        wildlifeSanctuary.addListener(new Drozdov(wildlifeSanctuary));
        wildlifeSanctuary.startSimulations(100);
    }
}
