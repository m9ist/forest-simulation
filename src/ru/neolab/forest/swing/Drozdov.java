package ru.neolab.forest.swing;

import ru.neolab.forest.WildlifeSanctuaryListener;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.Grass;
import ru.neolab.forest.fauna.WildlifeSanctuary;
import ru.neolab.forest.flora.Beast;
import ru.neolab.forest.flora.Hare;
import ru.neolab.forest.flora.Wolf;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class Drozdov extends JComponent implements WildlifeSanctuaryListener {
    private final WildlifeSanctuary wildlifeSanctuary;

    public Drozdov(final WildlifeSanctuary wildlifeSanctuary) {
        this.wildlifeSanctuary = wildlifeSanctuary;
        final JFrame frame = new JFrame();
        frame.setContentPane(this);
        frame.setSize(800, 400);
        // frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);// максимизировать окно
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        calcPossibleCoordinates();
        repaint();
    }

    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;

    private void calcPossibleCoordinates() {
        final Collection<Coordinates> possibleCoordinates = wildlifeSanctuary.getPossibleCoordinates();
        for (final Coordinates possibleCoordinate : possibleCoordinates) {
            minX = Math.min(possibleCoordinate.x, minX);
            minY = Math.min(possibleCoordinate.y, minY);
            maxX = Math.max(possibleCoordinate.x, maxX);
            maxY = Math.max(possibleCoordinate.y, maxY);
        }
    }

    private static long lastStateUpdate = 0L;

    @Override
    public void changed() throws InterruptedException {
        repaint();
        final long currentTime = System.currentTimeMillis();
        final long l = 200L;
        if (currentTime < lastStateUpdate + l) {
            final long sleep = Math.max(l - Math.min(currentTime - lastStateUpdate, l), 0L);
            Thread.sleep(sleep);
        }
        lastStateUpdate = System.currentTimeMillis();
    }

    private static final int OFFSET_FIELD = 3;
    private static final int OFFSET_CELLS = 1;
    private static final int LIFE_BAR_OFFSET = 1;
    private static final Color GRID_COLOR = Color.LIGHT_GRAY;
    private static final Color COLOR_FEMALE = Color.RED;
    private static final Color COLOR_MALE = Color.BLACK;

    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        final int originWidth = getWidth();
        final int height = getHeight();
        final double stepX = (originWidth - OFFSET_FIELD * 2) * 1.0 / (maxX - minX + 1);
        final double stepY = (height - OFFSET_FIELD * 2) * 1.0 / (maxY - minY + 1);

        final Collection<Coordinates> possibleCoordinates = wildlifeSanctuary.getPossibleCoordinates();
        for (final Coordinates coordinate : possibleCoordinates) {
            drawCell(coordinate,
                    stepX,
                    stepY,
                    g2
            );
        }
    }

    private void drawCell(final Coordinates coordinate, final double stepX, final double stepY, final Graphics2D g2) {
        // сначала рисуем само поле
        g2.setStroke(new BasicStroke(1));
        g2.setColor(GRID_COLOR);
        final int x = (int) (OFFSET_FIELD + stepX * coordinate.x + OFFSET_CELLS);
        final int y = (int) (OFFSET_FIELD + stepY * coordinate.y + OFFSET_CELLS);
        final int width = (int) (stepX - OFFSET_CELLS * 2);
        final int height = (int) (stepY - OFFSET_CELLS * 2);
        g2.drawRect(
                x,
                y,
                width,
                height
        );
        // рисуем травку
        final Grass grass = wildlifeSanctuary.getGrass(coordinate);
        g2.setColor(getGrassColor(grass.getPercentageOfAbsoluteMax()));
        g2.fillRect(x, y, width, height);

        // потом рисуем пацанов, которые там обитают
        final Collection<Beast> beasts = wildlifeSanctuary.getBeasts(coordinate);
        for (final Beast beast : beasts) {
            if (!beast.isDead()) {
                g2.setColor(beast.isMale() ? COLOR_MALE : COLOR_FEMALE);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(x + OFFSET_CELLS, y + OFFSET_CELLS + LIFE_BAR_OFFSET, (int) (x + OFFSET_CELLS + width * beast.getHunger()), y + OFFSET_CELLS + LIFE_BAR_OFFSET);
            }
            final Image image;
            if (beast.getClass() == Wolf.class) {
                image = new ImageIcon(Drozdov.class.getResource("/icons8-Волк-50.png")).getImage();
            } else if (beast.getClass() == Hare.class) {
                image = new ImageIcon(Drozdov.class.getResource("/icons8-Кролик-48.png")).getImage();
            } else {
                throw new RuntimeException("Doesn't support " + beast.getClass().getCanonicalName());
            }
            g2.drawImage(image, x + OFFSET_CELLS, y + OFFSET_CELLS, width, height, null);
        }
    }

    protected static Color getGrassColor(final double percentageFromMax) {
        final int redBlue = Math.max(0, (int) (255 * (0.05 - percentageFromMax) / 0.05));
        final int green = (int) (80 + 175 * (1 - percentageFromMax));
        return new Color(redBlue, green, redBlue);
    }
}
