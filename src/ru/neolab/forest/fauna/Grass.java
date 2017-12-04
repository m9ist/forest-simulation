package ru.neolab.forest.fauna;

public class Grass {
    private final double kilocaloriesPerIteration = 10 * (Math.random() * 0.5 + 0.1);
    private static final double MAX_KCAL = 100;
    private final double maxKilocalories = MAX_KCAL * (Math.random() * 0.5 + 0.5);
    private double kilocalories = (Math.random() * 0.1 + 0.5) * maxKilocalories;

    double getKilocalories() {
        return kilocalories;
    }

    void stepDone(final double kilocaloriesAte) {
        if (kilocalories <= kilocaloriesAte + 1e-9) {
            kilocalories = 0;
            return; // трава не растет там где ее сожрали
        }
        kilocalories = Math.max(0, Math.min(kilocalories + kilocaloriesPerIteration - kilocaloriesAte, maxKilocalories));
    }

    void growGrass(final double powerGrow) {
        if (powerGrow > 0) {
            kilocalories = maxKilocalories * (powerGrow / 8);
        }
    }

    @Override
    public String toString() {
        return String.format("%s(%.1f) - %.1f", getClass().getSimpleName(), kilocaloriesPerIteration, kilocalories);
    }

    public double getPercentageOfAbsoluteMax() {
        return kilocalories / MAX_KCAL;
    }

    /**
     * @return true - если трава готова прорасти в соседней пустой клетке
     */
    public boolean isGoodForReproduction() {
        return getKilocalories() >= maxKilocalories / 3;
    }
}
