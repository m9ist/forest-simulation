package ru.neolab.forest.fauna;

import ru.neolab.forest.SanctuaryException;
import ru.neolab.forest.WildlifeSanctuaryListener;
import ru.neolab.forest.flora.Beast;

import java.util.*;

public class WildlifeSanctuary {
    private final int ySize;
    private final int xSize;
    private final HashMap<Beast, BeastSanctuaryState> beasts = new HashMap<>();
    private final ArrayList<WildlifeSanctuaryListener> listeners = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();

    public WildlifeSanctuary(final int xSize, final int ySize) {
        this.ySize = ySize;
        this.xSize = xSize;
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

    //-------------------------------------- работа с движением и возможными координатами

    /**
     * «адаютс€ правила передвижени€ по полю. ћожно здесь накидать преп€тствий, оврагов и пр и др и тд
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
     * «адаетс€ геометри€ пол€, тут можно хоть круг нарисовать
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

    //-------------------------------------- итерации
    public void addBeast(final Beast beast, final Coordinates coordinates) {
        beasts.put(beast, new BeastSanctuaryState(beast, coordinates));
    }

    /**
     * ѕроводим итерацию изменени€ мира. ѕока мир наш прост, без эксцессов, все выбирают что он будут делать, потом
     * это происходит.
     * <p>
     *   примеру за€ц сказал - буду двигатьс€ туда, волк - буду размножатьс€, а травка - что она отрастет, а второй за€ц -
     * что буду жрать как не в себ€ и наслаждатьс€ жизнью.
     */
    private void iteration() throws SanctuaryException {
        if (!events.isEmpty()) {
            throw new SanctuaryException("Not empty state");
        }
        for (final Beast beast : beasts.keySet()) {
            if (beast.isDead()) continue;
            beast.chooseMove(this);
        }
        // мен€ем этот мир....
        for (final Event event : events) {
            event.apply(this);
        }
        events.clear();
    }

    public void addEvent(final Event event) {
        events.add(event);
    }

    public Coordinates whereBeast(final Beast beast) {
        return beasts.get(beast).coordinates;
    }

    //----------------------------------- реализаци€ применени€ накопленных решений изменений мира

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

    //----------------------------------- дл€ прорисовки мира

    /**
     * ѕолучить животных в этой €чейке мира
     */
    public Collection<Beast> getBeasts(final Coordinates coordinate) {
        final ArrayList<Beast> out = new ArrayList<>();
        for (final BeastSanctuaryState beastSanctuaryState : beasts.values()) {
            if (beastSanctuaryState.coordinates.equals(coordinate)) {
                out.add(beastSanctuaryState.beast);
            }
        }
        return out;
    }
}
