package com.blogspot.howdoidothatinjava.emmi.tictactoe;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TicTacToeTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseLogEntry() {
        Integer expected = 123456789;
        Integer actual = TicTacToe.parseLogEntry("(1,1)-(1,2)-(1,3)-(2,1)-(2,2)-(2,3)-(3,1)-(3,2)-(3,3)");
        assertEquals(expected, actual);

        expected = 192837465;
        actual = TicTacToe.parseLogEntry("(1,1)-(3,3)-(1,2)-(3,2)-(1,3)-(3,1)-(2,1)-(2,3)-(2,2)");
        assertEquals(expected, actual);

    }

}
