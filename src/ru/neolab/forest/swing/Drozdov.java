package ru.neolab.forest.swing;

import ru.neolab.forest.WildlifeSanctuaryListener;
import ru.neolab.forest.fauna.Coordinates;
import ru.neolab.forest.fauna.WildlifeSanctuary;
import ru.neolab.forest.flora.Beast;

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
        changed();
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

    @Override
    public void changed() {
        repaint();
    }

    private final int OFFSET_FIELD = 10;
    private final int OFFSET_CELLS = 2;
    private static final Color GRID_COLOR = Color.LIGHT_GRAY;
    private static final Color TEXT_COLOR_DEAD = Color.RED;
    private static final Color TEXT_COLOR_ALIVE = Color.BLACK;

    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        final int originWidth = getWidth();
        final int height = getHeight();
        final int stepX = (originWidth - OFFSET_FIELD) / (maxX - minX + 1) - OFFSET_CELLS / 2;
        final int stepY = (height - OFFSET_FIELD) / (maxY - minY + 1) - OFFSET_CELLS / 2;

        final Collection<Coordinates> possibleCoordinates = wildlifeSanctuary.getPossibleCoordinates();
        for (final Coordinates coordinate : possibleCoordinates) {
            drawCell(coordinate,
                    stepX,
                    stepY,
                    g2
            );
        }
    }

    private void drawCell(final Coordinates coordinate, final int stepX, final int stepY, final Graphics2D g2) {
        // сначала рисуем само поле
        g2.setColor(GRID_COLOR);
        final int x = OFFSET_FIELD / 2 + stepX * coordinate.x + OFFSET_CELLS;
        final int y = OFFSET_FIELD / 2 + stepY * coordinate.y + OFFSET_CELLS;
        g2.drawRect(
                x,
                y,
                stepX - OFFSET_CELLS * 2,
                stepY - OFFSET_CELLS * 2
        );
        // потом рисуем пацанов, которые там обитают
        final Collection<Beast> beasts = wildlifeSanctuary.getBeasts(coordinate);
        for (final Beast beast : beasts) {
            g2.setColor(beast.isDead() ? TEXT_COLOR_DEAD : TEXT_COLOR_ALIVE);
            g2.drawString(beast.toString(), x + OFFSET_CELLS, y + stepY / 2);
        }
    }
}
