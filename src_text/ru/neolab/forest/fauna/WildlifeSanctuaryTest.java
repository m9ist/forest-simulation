package ru.neolab.forest.fauna;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WildlifeSanctuaryTest {
    @Test
    public void testIsPossible() {
        final WildlifeSanctuary wildlifeSanctuary = new WildlifeSanctuary(5, 3);
        assertTrue(wildlifeSanctuary.checkIsPossible(new Coordinates(0, 0)));
        assertTrue(wildlifeSanctuary.checkIsPossible(new Coordinates(0, 1)));
        assertTrue(wildlifeSanctuary.checkIsPossible(new Coordinates(0, 2)));
        assertTrue(wildlifeSanctuary.checkIsPossible(new Coordinates(1, 2)));
        assertTrue(wildlifeSanctuary.checkIsPossible(new Coordinates(4, 2)));
        assertTrue(wildlifeSanctuary.checkIsPossible(new Coordinates(0, 2)));
        assertFalse(wildlifeSanctuary.checkIsPossible(new Coordinates(0, 3)));
        assertFalse(wildlifeSanctuary.checkIsPossible(new Coordinates(5, 0)));
        assertFalse(wildlifeSanctuary.checkIsPossible(new Coordinates(5, 3)));
        assertFalse(wildlifeSanctuary.checkIsPossible(new Coordinates(5, 3)));
        assertFalse(wildlifeSanctuary.checkIsPossible(new Coordinates(-1, 2)));
        assertFalse(wildlifeSanctuary.checkIsPossible(new Coordinates(3, -1)));
    }
}