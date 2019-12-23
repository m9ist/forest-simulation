package ru.neolab.forest.fauna;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.WildlifeSanctuaryListener;
import ru.neolab.forest.flora.Beast;
import ru.neolab.forest.flora.Hare;
import ru.neolab.forest.flora.Wolf;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WildlifeSanctuary {
    private final int ySize;
    private final int xSize;
    private final ConcurrentHashMap<Beast, BeastSanctuaryState> beasts = new ConcurrentHashMap<>();
    private final HashMap<Coordinates, Grass> grasses = new HashMap<>();
    private final ArrayList<WildlifeSanctuaryListener> listeners = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();

    public WildlifeSanctuary(final int xSize, final int ySize) {
        this.ySize = ySize;
        this.xSize = xSize;
        for (final Coordinates coordinates : getPossibleCoordinates()) {
            grasses.put(coordinates, new Grass());
        }
    }

    public void startSimulations(final int iterations) throws InterruptedException, SanctuaryException {
        for (int i = 0; i < iterations; i++) {
            iteration();
            for (final WildlifeSanctuaryListener listener : listeners) {
                listener.changed();
            }
        }
    }

    public void addListener(final WildlifeSanctuaryListener listener) {
        listeners.add(listener);
    }

    //-------------------------------------- ðàáîòà ñ äâèæåíèåì è âîçìîæíûìè êîîðäèíàòàìè

    /**
     * Çàäàþòñÿ ïðàâèëà ïåðåäâèæåíèÿ ïî ïîëþ. Ìîæíî çäåñü íàêèäàòü ïðåïÿòñòâèé, îâðàãîâ è ïð è äð è òä
     */
    private boolean possibleMove(final Coordinates from, final Coordinates to, final double speed) throws SanctuaryException {
        if (!checkIsPossible(from)) {
            throw new SanctuaryException("Impossible " + from);
        }
        return checkIsPossible(to)
                && Math.abs(from.x - to.x) <= speed
                && Math.abs(from.y - to.y) <= speed;
    }

    /**
     * Çàäàåòñÿ ãåîìåòðèÿ ïîëÿ, òóò ìîæíî õîòü êðóã íàðèñîâàòü
     */
    boolean checkIsPossible(final Coordinates coordinates) {
        return coordinates.x >= 0
                && coordinates.x < xSize
                && coordinates.y >= 0
                && coordinates.y < ySize;
    }

    public final List<Coordinates> getPossibleCoordinates() {
        final LinkedList<Coordinates> coordinates = new LinkedList<>();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                final Coordinates coordinate = new Coordinates(x, y);
                if (checkIsPossible(coordinate)) {
                    coordinates.add(coordinate);
                }
            }
        }
        return coordinates;
    }

    public final List<Coordinates> getPossibleMoves(final Coordinates from, final double speed) throws SanctuaryException {
        final LinkedList<Coordinates> coordinates = new LinkedList<>();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                final Coordinates to = new Coordinates(x, y);
                if (possibleMove(from, to, speed)) {
                    coordinates.add(to);
                }
            }
        }
        return coordinates;
    }

    //-------------------------------------- èòåðàöèè
    public void addBeast(final Beast beast, final Coordinates coordinates) {
        beasts.put(beast, new BeastSanctuaryState(beast, coordinates));
    }

    /**
     * Ïðîâîäèì èòåðàöèþ èçìåíåíèÿ ìèðà. Ïîêà ìèð íàø ïðîñò, áåç ýêñöåññîâ, âñå âûáèðàþò ÷òî îí áóäóò äåëàòü, ïîòîì
     * ýòî ïðîèñõîäèò.
     * <p>
     * Ê ïðèìåðó çàÿö ñêàçàë - áóäó äâèãàòüñÿ òóäà, âîëê - áóäó ðàçìíîæàòüñÿ, à òðàâêà - ÷òî îíà îòðàñòåò, à âòîðîé çàÿö -
     * ÷òî áóäó æðàòü êàê íå â ñåáÿ è íàñëàæäàòüñÿ æèçíüþ.
     */
    private void iteration() throws SanctuaryException {
        List<Hare> ateHares = new ArrayList<Hare>();
        if (!events.isEmpty()) {
            throw new SanctuaryException("Not empty state");
        }
        for (final Beast beast : beasts.keySet()) {
            if (beast.isDead()) continue;
            beast.chooseMove(this);
        }
        final ArrayList<Beast> wolfs = new ArrayList<>(beasts.keySet());
        wolfs.removeIf(beast -> beast.isDead() || !(beast instanceof Wolf));
        for (final Beast wolf : wolfs) {
            final List<Hare> beasts = getAliveBeast(whereBeast(wolf), Hare.class);
            if (beasts.size() == 0) continue;
            final Hare ateHare = beasts.get((int) (beasts.size() * Math.random()));
            events.add(new Event.BeastAte(wolf, ateHare.getKilocalories()));
            beasts.remove(ateHare);
        }
        for (final Coordinates coordinate : grasses.keySet()) {
            final Collection<Hare> beasts = getAliveBeast(coordinate, Hare.class);
            final Grass grass = grasses.get(coordinate);
            if (beasts.size() > 0) {
                final double kilocalories = grass.getKilocalories();
                double needKcal = 0;
                for (final Beast beast : beasts) {
                    needKcal += beast.getNeededKilocaloriesAmount();
                }
                final double kilocaloriesAte = Math.min(needKcal, kilocalories);
                grass.stepDone(kilocaloriesAte);
                if (needKcal > 1e-8) {
                    for (final Beast beast : beasts) {
                        addEvent(new Event.BeastAte(beast, kilocaloriesAte * beast.getNeededKilocaloriesAmount() / needKcal));
                        beast.getNeededKilocaloriesAmount();
                    }
                }
            } else {
                grass.stepDone(0);
            }
        }
        // âîñïðîèçâîäñòâî òðàâû ïîñëå òîãî êàê åå âñþ ñúåëè
        for (final Coordinates coordinate : grasses.keySet()) {
            final Grass grass = grasses.get(coordinate);
            if (grass.getKilocalories() <= 0) {
                final List<Coordinates> possibleParentGrass = getPossibleMoves(coordinate, 1);
                int numPossibleParents = 0;
                for (final Coordinates coordWithGrass : possibleParentGrass) {
                    final Grass parentGrass = grasses.get(coordWithGrass);
                    if (parentGrass.isGoodForReproduction()) {
                        numPossibleParents++;
                    }
                }

                grass.growGrass(numPossibleParents);
            }
        }
        // ìåíÿåì ýòîò ìèð....
        for (final Event event : events) {
            if (event.beast.isDead()) continue;
            event.apply(this);
        }
        events.clear();
        
        if (!ateHares.isEmpty()) {
            for (Hare ateHare : ateHares) {
                this.beasts.remove(ateHare);
            }
        }
    }

    public void addEvent(final Event event) {
        events.add(event);
    }

    public Coordinates whereBeast(final Beast beast) {
        return beasts.get(beast).coordinates;
    }

    public double getDistance(final Coordinates c1, final Coordinates c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
    }

    //----------------------------------- ðåàëèçàöèÿ ïðèìåíåíèÿ íàêîïëåííûõ ðåøåíèé èçìåíåíèé ìèðà

    void moveBeast(final Beast beast, final Coordinates to) throws SanctuaryException {
        final BeastSanctuaryState beastSanctuaryState = beasts.get(beast);
        if (!possibleMove(beastSanctuaryState.coordinates, to, beast.getSpeed())) {
            throw new SanctuaryException("Can't move " + beast + " to " + to);
        }
        beastSanctuaryState.coordinates = to;
    }

    void beastDead(final Beast beast) {
        beasts.get(beast).isDead = true;
    }

    void beastAte(final Beast beast, final double kilocalories) {
        beast.kilocaloriesAte(kilocalories);
    }

    //----------------------------------- äëÿ ïðîðèñîâêè ìèðà

    /**
     * Ïîëó÷èòü æèâîòíûõ â ýòîé ÿ÷åéêå ìèðà
     */
    public List<Beast> getBeasts(final Coordinates coordinate) {
        final ArrayList<Beast> out = new ArrayList<>();
        for (final BeastSanctuaryState beastSanctuaryState : beasts.values()) {
            if (beastSanctuaryState.coordinates.equals(coordinate)) {
                out.add(beastSanctuaryState.beast);
            }
        }
        return out;
    }

    public <K extends Beast> List<K> getAliveBeast(final Coordinates coordinate, final Class<K> kClass) {
        final List<Beast> beasts = getBeasts(coordinate);
        final ArrayList<K> hares = new ArrayList<>();
        for (final Beast beast : beasts) {
            if (beast.getClass() == kClass && !beast.isDead()) {
                hares.add((K) beast);
            }
        }
        return hares;
    }

    public Grass getGrass(final Coordinates coordinate) {
        return grasses.get(coordinate);
    }
}
