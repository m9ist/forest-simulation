package ru.neolab.forest;

import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.WildlifeSanctuary;
import ru.neolab.forest.flora.Hare;
import ru.neolab.forest.flora.Wolf;
import ru.neolab.forest.swing.Drozdov;

import java.util.concurrent.TimeUnit;

public class ForestSimulationLauncher {
    public static void main(final String[] args) throws InterruptedException, SanctuaryException {
        System.out.println("Hello world!");

        final WildlifeSanctuary wildlifeSanctuary = new WildlifeSanctuary(20, 10);
        wildlifeSanctuary.addBeast(new Wolf(new Coordinates(0, 0)));
        wildlifeSanctuary.addBeast(new Wolf(new Coordinates(15, 7)));
        wildlifeSanctuary.addBeast(new Hare(new Coordinates(19, 9)));

        wildlifeSanctuary.addListener(new Drozdov(wildlifeSanctuary));
        wildlifeSanctuary.start(100, TimeUnit.MILLISECONDS.toMillis(300));
    }
}
