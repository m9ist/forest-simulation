package ru.neolab.forest.swing;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class DrozdovTest {
    @Test
    public void getGrassColor() throws Exception {
        final Color nothing = Drozdov.getGrassColor(0);
        assertEquals(255, nothing.getGreen());
        assertEquals(255, nothing.getRed());
        assertEquals(255, nothing.getBlue());

        final Color onlyGreen = Drozdov.getGrassColor(0.95);
        assertTrue(onlyGreen.getGreen() > 0);
        assertEquals(0, onlyGreen.getRed());
        assertEquals(0, onlyGreen.getBlue());

        final Color full = Drozdov.getGrassColor(1);
        assertEquals(80, full.getGreen());
        assertEquals(0, full.getRed());
        assertEquals(0, full.getBlue());
    }
}