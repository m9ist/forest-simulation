package ru.neolab.forest.flora;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WolfTest {
    @Test
    public void getChildren() {
        final Wolf wolf = new Wolf();
        assertEquals(wolf.getClass(), wolf.getChildren().getClass());
    }
}