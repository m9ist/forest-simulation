package ru.neolab.forest.flora;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HareTest {
    @Test
    public void getChildren() throws Exception {
        final Hare hare = new Hare();
        assertEquals(hare.getClass(), hare.getChildren().getClass());
    }
}