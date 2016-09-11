package com.blogspot.howdoidothatinjava.emmi.tictactoe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.blogspot.howdoidothatinjava.emmi.enums.CoordinateMap;
import com.blogspot.howdoidothatinjava.emmi.enums.MarkerType;
import com.blogspot.howdoidothatinjava.emmi.enums.RotationDegrees;
import com.blogspot.howdoidothatinjava.emmi.enums.WinCombination;

public class TicTacToeBoard {

    private final static String ZERO = "0";
    private final static String SPACE = " ";

    private int moveNumber = 0;
    private Integer moves = Integer.valueOf(ZERO);
    private final String[][] EMPTY_BOARD = new String[][] { { "1", "2", "3" }, { "4", "5", "6" }, { "7", "8", "9" } };

    private String[][] boardLayout;

    public TicTacToeBoard() {
        boardLayout = initializeBoardLayout();
    }

    private String[][] initializeBoardLayout() {
        String[][] copy = new String[3][3];
        int rowNumber = 0;
        for (String[] row : EMPTY_BOARD) {
            System.arraycopy(row, 0, copy[rowNumber], 0, row.length);
            rowNumber++;
        }
        return copy;
    }

    public void recordMove(Integer move, MarkerType type) {
        moves += move * nextPower();
        CoordinateMap map = CoordinateMap.getMapForId(move);
        boardLayout[map.getX()][map.getY()] = type.toString();
        moveNumber++;
    }

    private Integer nextPower() {
        return (int) Math.pow(10, Double.valueOf(getOpenSpaces().length() - 1));
    }

    public void reset() {
        boardLayout = initializeBoardLayout();
        moves = Integer.valueOf(ZERO);
    }

    public String getOpenSpaces() {
        StringBuffer buffer = new StringBuffer();
        for (String[] row : boardLayout) {
            for (String rcValue : row) {
                if (rcValue.equals("X") || rcValue.equalsIgnoreCase("O")) {
                    continue;
                }
                buffer.append(rcValue);
            }
        }
        return buffer.toString();
    }

    public Integer getMoves() {
        return moves;
    }

    public Integer getMoveNumber() {
        return moveNumber;
    }

    public void printBoard() {
        clearConsole();
        int rowCount = 0;
        StringBuffer buffer = new StringBuffer();
        for (String[] row : boardLayout) {
            if (rowCount++ > 0) {
                System.out.println("-----------");
            }
            int colCount = 0;
            for (String rcValue : row) {
                if (colCount++ > 0) {
                    buffer.append("|");
                }
                buffer.append(SPACE + rcValue + SPACE);
            }
            System.out.println(buffer.toString());
            buffer.setLength(0);
        }
        System.out.println();
    }

    public void clearConsole() {
        IntStream.range(0, 250).forEach(i -> System.out.println("\n\n\n"));
    }

    public String getMovesWithRotation(RotationDegrees rotation) {
        StringBuffer buffer = new StringBuffer();

        Map<String, String> rotationMap = getRotationMap(RotationDegrees.ZERO, rotation);

        if (moves == 0) {
            return "000000000";
        }

        String[] movesArray = getMovesArray();
        for (int i = 0; i < 9; i++) {
            if (ZERO.equals(movesArray[i])) {
                buffer.append(ZERO);
                continue;
            }
            buffer.append(rotationMap.get(String.valueOf(movesArray[i])));
        }

        return buffer.toString();
    }

    public Map<String, String> getRotationMap(RotationDegrees startOrientation, RotationDegrees degreesRotationToApply) {
        Map<String, String> rotationMap = new LinkedHashMap<>();
        RotationDegrees relativeRotation = degreesRotationToApply;

        int fromOrdinal = startOrientation.ordinal();
        if (fromOrdinal > 0) {
            int toOrdinal = degreesRotationToApply.ordinal();
            relativeRotation = RotationDegrees.values()[(fromOrdinal + toOrdinal) % 4];
        }
        String[] from = getBoardOrderForRotation(startOrientation).split("");
        String[] to = getBoardOrderForRotation(relativeRotation).split("");
        for (int i = 0; i < 9; i++) {
            rotationMap.put(from[i], to[i]);
        }
        return rotationMap;
    }

    public String getBoardOrderForRotation(RotationDegrees rotation) {
        String boardOrder = "";
        switch (rotation) {

            case NINETY:
                boardOrder = "369258147";
                break;

            case ONE_EIGHTY:
                boardOrder = "987654321";
                break;

            case TWO_SEVENTY:
                boardOrder = "741852963";
                break;

            case ZERO:
            default:
                boardOrder = "123456789";
                break;
        }

        return boardOrder;
    }

    public Integer getWinSquare(String mark) {
        String[] flatBoard = getFlattenedBoardLayout();
        int markerCount = 0;
        int unmarkedCount = 0;
        int winIndex = -1;

        for (WinCombination combination : WinCombination.values()) {
            if (markerCount == 2 && unmarkedCount == 1) {
                break;
            }
            markerCount = 0;
            unmarkedCount = 0;
            winIndex = -1;
            for (int i : combination.getWinIndexArray()) {
                String observedSquare = flatBoard[i];
                if (observedSquare.contains(mark)) {
                    markerCount++;
                } else if (Pattern.compile("[0-9]").matcher(observedSquare).matches()) {
                    unmarkedCount++;

                    if (unmarkedCount > 1) {
                        winIndex = -1;
                        break;
                    }

                    winIndex = i + 1;
                } else {
                    winIndex = -1;
                }
            }

        }
        return winIndex;
    }

    public boolean checkWin(String mark) {
        String[] flatBoard = getFlattenedBoardLayout();

        boolean winFound = false;

        for (WinCombination combination : WinCombination.values()) {
            int markerCount = 0;
            for (int i : combination.getWinIndexArray()) {
                String observedSquare = flatBoard[i];
                if (observedSquare.equals(mark)) {
                    markerCount++;
                    if (markerCount == 3) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return winFound;
    }

    public String[] getMovesArray() {
        return moves.toString().split(StringUtils.EMPTY);
    }

    public String[] getFlattenedBoardLayout() {
        String[] flattenedArray = new String[9];
        int index = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                flattenedArray[index++] = boardLayout[x][y];
            }
        }
        return flattenedArray;
    }

}
