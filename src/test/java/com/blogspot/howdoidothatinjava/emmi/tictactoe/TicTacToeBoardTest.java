package com.blogspot.howdoidothatinjava.emmi.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blogspot.howdoidothatinjava.emmi.enums.MarkerType;
import com.blogspot.howdoidothatinjava.emmi.enums.RotationDegrees;

public class TicTacToeBoardTest {

    TicTacToeBoard testUnit;

    @Before
    public void setUp() throws Exception {
        testUnit = new TicTacToeBoard();
    }

    @After
    public void tearDown() throws Exception {
        testUnit.reset();
    }

    @Test
    public void printEmptyBoard() {
        testUnit.printBoard();
    }

    @Test
    public void printBoardWithMarks() {
        testUnit.recordMove(5, MarkerType.X);
        testUnit.recordMove(7, MarkerType.O);
        testUnit.printBoard();
    }

    @Test
    public void moves() {
        int actual = testUnit.getMoves();
        int expected = 0;
        assertEquals("moves incorrect ", expected, actual);

        testUnit.recordMove(1, MarkerType.X);
        testUnit.recordMove(5, MarkerType.O);
        testUnit.recordMove(9, MarkerType.X);

        actual = testUnit.getMoves();
        expected = 159000000;
        assertEquals("moves incorrect", expected, actual);
    }

    @Test
    public void openSpaces() {
        String expected = "123456789";
        String actual = testUnit.getOpenSpaces();

        assertEquals("Empty board open spaces reported incorrectly", expected, actual);

        testUnit.recordMove(1, MarkerType.X);
        testUnit.recordMove(3, MarkerType.X);
        testUnit.recordMove(5, MarkerType.X);
        testUnit.recordMove(7, MarkerType.X);
        testUnit.recordMove(9, MarkerType.X);

        expected = "2468";
        actual = testUnit.getOpenSpaces();

        assertEquals("Odds only board reported open spaces incorrectly", expected, actual);

        testUnit.reset();

        expected = "123456789";
        actual = testUnit.getOpenSpaces();

        assertEquals("Reset board open spaces reported incorrectly", expected, actual);

        testUnit.recordMove(2, MarkerType.O);
        testUnit.recordMove(4, MarkerType.O);
        testUnit.recordMove(6, MarkerType.O);
        testUnit.recordMove(8, MarkerType.O);

        expected = "13579";
        actual = testUnit.getOpenSpaces();

        assertEquals("Evens only board reported open spaces incorrectly", expected, actual);
        testUnit.recordMove(1, MarkerType.X);
        testUnit.recordMove(3, MarkerType.X);
        testUnit.recordMove(5, MarkerType.X);
        testUnit.recordMove(7, MarkerType.X);
        testUnit.recordMove(9, MarkerType.X);

        expected = "";
        actual = testUnit.getOpenSpaces();

        assertEquals("Full board reported open spaces incorrectly", expected, actual);

    }

    @Test
    public void rotationFull() {
        String expected = "000000000";
        String actual = testUnit.getMovesWithRotation(RotationDegrees.ZERO);
        assertEquals("No marks rotation failed", expected, actual);

        testUnit.recordMove(1, MarkerType.X);
        testUnit.recordMove(2, MarkerType.O);
        testUnit.recordMove(3, MarkerType.X);
        testUnit.recordMove(4, MarkerType.O);
        testUnit.recordMove(5, MarkerType.X);
        testUnit.recordMove(6, MarkerType.O);
        testUnit.recordMove(7, MarkerType.X);
        testUnit.recordMove(8, MarkerType.O);
        testUnit.recordMove(9, MarkerType.X);

        expected = "123456789";
        actual = testUnit.getMovesWithRotation(RotationDegrees.ZERO);
        assertEquals("ZERO rotation failed", expected, actual);

        expected = "369258147";
        actual = testUnit.getMovesWithRotation(RotationDegrees.NINETY);
        assertEquals("NINETY rotation failed", expected, actual);

        expected = "987654321";
        actual = testUnit.getMovesWithRotation(RotationDegrees.ONE_EIGHTY);
        assertEquals("ONE_EIGHTY rotation failed", expected, actual);

        expected = "741852963";
        actual = testUnit.getMovesWithRotation(RotationDegrees.TWO_SEVENTY);
        assertEquals("TWO_SEVENTY rotation failed", expected, actual);
    }

    @Test
    public void rotationAlternation() {
        testUnit.recordMove(1, MarkerType.X);
        testUnit.recordMove(3, MarkerType.X);
        testUnit.recordMove(5, MarkerType.X);
        testUnit.recordMove(7, MarkerType.X);
        testUnit.recordMove(9, MarkerType.X);

        assertTrue(Integer.valueOf(135790000) == testUnit.getMoves().intValue());

        String expected = "135790000";
        String actual = testUnit.getMovesWithRotation(RotationDegrees.ZERO);
        assertEquals("ZERO rotation failed", expected, actual);

        expected = "395170000";
        actual = testUnit.getMovesWithRotation(RotationDegrees.NINETY);
        assertEquals("NINETY rotation failed", expected, actual);

        expected = "975310000";
        actual = testUnit.getMovesWithRotation(RotationDegrees.ONE_EIGHTY);
        assertEquals("ONE_EIGHTY rotation failed", expected, actual);

        expected = "715930000";
        actual = testUnit.getMovesWithRotation(RotationDegrees.TWO_SEVENTY);
        assertEquals("TWO_SEVENTY rotation failed", expected, actual);
    }

    @Test
    public void boardOrderForRotation() {
        String expected = "123456789";
        String actual = testUnit.getBoardOrderForRotation(RotationDegrees.ZERO);

        assertEquals("ZERO board order is incorrect", expected, actual);

        expected = "369258147";
        actual = testUnit.getBoardOrderForRotation(RotationDegrees.NINETY);

        assertEquals("NINETY board order is incorrect", expected, actual);

        expected = "987654321";
        actual = testUnit.getBoardOrderForRotation(RotationDegrees.ONE_EIGHTY);

        assertEquals("ONE_EIGHTY board order is incorrect", expected, actual);

        expected = "741852963";
        actual = testUnit.getBoardOrderForRotation(RotationDegrees.TWO_SEVENTY);

        assertEquals("TWO_SEVENTY board order is incorrect", expected, actual);
    }

    @Test
    public void rotationMap() {
        Map<String, String> map = testUnit.getRotationMap(RotationDegrees.ZERO, RotationDegrees.ZERO);
        String expected = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";
        String actual = map.values().toString();

        assertEquals("ZERO-ZERO map incorrect", expected, actual);

        map = testUnit.getRotationMap(RotationDegrees.ZERO, RotationDegrees.NINETY);
        expected = "[3, 6, 9, 2, 5, 8, 1, 4, 7]";
        actual = map.values().toString();

        assertEquals("ZERO-NINETY map incorrect", expected, actual);

        map = testUnit.getRotationMap(RotationDegrees.ZERO, RotationDegrees.ONE_EIGHTY);
        expected = "[9, 8, 7, 6, 5, 4, 3, 2, 1]";
        actual = map.values().toString();

        assertEquals("ZERO-ONE_EIGHTY map incorrect", expected, actual);

        map = testUnit.getRotationMap(RotationDegrees.ZERO, RotationDegrees.TWO_SEVENTY);
        expected = "[7, 4, 1, 8, 5, 2, 9, 6, 3]";
        actual = map.values().toString();

        assertEquals("ZERO-TWO_SEVENTY map incorrect", expected, actual);

        map = testUnit.getRotationMap(RotationDegrees.NINETY, RotationDegrees.TWO_SEVENTY);
        expected = "[1, 2, 3, 4, 5, 6, 7, 8, 9]";
        actual = map.values().toString();

        assertEquals("NINETY-TWO_SEVENTY map incorrect", expected, actual);

        map = testUnit.getRotationMap(RotationDegrees.NINETY, RotationDegrees.ZERO);
        expected = "[3, 6, 9, 2, 5, 8, 1, 4, 7]";
        actual = map.values().toString();

        assertEquals("NINETY-TWO_SEVENTY map incorrect", expected, actual);

    }

    @Test
    public void checkWin() {
        testUnit.recordMove(5, MarkerType.X);
        testUnit.recordMove(1, MarkerType.O);
        testUnit.recordMove(2, MarkerType.X);
        testUnit.recordMove(4, MarkerType.O);
        testUnit.recordMove(7, MarkerType.X);
    }
}
