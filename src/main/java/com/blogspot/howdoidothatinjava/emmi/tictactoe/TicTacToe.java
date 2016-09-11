package com.blogspot.howdoidothatinjava.emmi.tictactoe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.blogspot.howdoidothatinjava.emmi.enums.CoordinateMap;
import com.blogspot.howdoidothatinjava.emmi.enums.MarkerType;
import com.blogspot.howdoidothatinjava.emmi.enums.RotationDegrees;

public class TicTacToe {

    private static final String PLAY_OR_NOT = "[YyNnQq]";
    private static final String YNQ_INSTRUCTION = "(Y to Play / N or Q to Quit)\n";

    private static boolean quit = false;

    private static Random rng = new Random();

    private static TicTacToeBoard board = new TicTacToeBoard();

    private static String gameLog = "./game.log";
    private static Path file = Paths.get(gameLog);
    private static Integer lastRecordedLoss = 0;
    private static TreeSet<Integer> moveHistory = new TreeSet<>();
    private static Pattern pattern = Pattern.compile(PLAY_OR_NOT);

    public static void main(String[] args) {

        setup();
        Scanner scanner = new Scanner(System.in);
        String response = StringUtils.EMPTY;

        do {
            board.printBoard();
            response = scanForInput(scanner, "Shall we play a game? " + YNQ_INSTRUCTION, pattern);
        } while (StringUtils.EMPTY.equals(response));

        if ("Yy".contains(response)) {
            while (!quit) {

                move();
                if (board.checkWin(MarkerType.X.toString())) {
                    pattern = Pattern.compile(PLAY_OR_NOT);
                    do {
                        board.printBoard();
                        response = scanForInput(scanner, "I Won.  Want to play again? " + YNQ_INSTRUCTION, pattern);
                    } while (StringUtils.EMPTY.equals(response));

                    if ("NnQq".contains(response)) {
                        quit = true;
                    } else {
                        board.reset();
                    }

                } else {
                    if (board.getOpenSpaces().length() > 0) {
                        response = StringUtils.EMPTY;
                        do {
                            board.printBoard();
                            pattern = getOpenSpaceChoices();
                            response = scanForInput(scanner, "Your move. Enter a value " + pattern + "\n", pattern);
                        } while (StringUtils.EMPTY.equals(response));

                        board.recordMove(Integer.valueOf(response), MarkerType.O);

                        if (board.checkWin(MarkerType.O.toString())) {

                            recordLoss(board.getMoves());

                            pattern = Pattern.compile(PLAY_OR_NOT);
                            do {
                                board.printBoard();
                                response = scanForInput(scanner,
                                        "You Won. You are lucky. Want to play again? " + YNQ_INSTRUCTION, pattern);
                            } while (StringUtils.EMPTY.equals(response));

                            if ("NnQq".contains(response)) {
                                quit = true;
                            } else {
                                board.reset();
                            }
                        }
                    } else {
                        response = StringUtils.EMPTY;
                        pattern = Pattern.compile(PLAY_OR_NOT);
                        do {
                            board.printBoard();
                            response = scanForInput(scanner,
                                    "It is a tie! Want to play again? " + YNQ_INSTRUCTION, pattern);
                        } while (StringUtils.EMPTY.equals(response));

                        if ("NnQq".contains(response)) {
                            quit = true;
                        } else {
                            board.reset();
                        }
                    }
                }

                if ("NnQq".contains(response)) {
                    quit = true;
                }
            }
        }

        System.out.println("\nThank you. Goodbye.\n");
    }

    private static String scanForInput(final Scanner scanner, final String promptToUser, final Pattern pattern) {
        System.out.println(promptToUser);

        String response = StringUtils.EMPTY;

        try {
            response = scanner.next(pattern);
        } catch (InputMismatchException e) {
            // Unexpected value not in pattern
            System.out.println("You must enter a valid character.");
        } finally {
            scanner.nextLine();
        }

        return response;
    }

    public void setGameLog(String pathToFile) {
        gameLog = pathToFile;
        file = Paths.get(gameLog);
    }

    // This is only used for a computer move!
    private static void move() {

        if (board.getMoves() == 0) {
            Integer start = getRandomStart();
            board.recordMove(start, MarkerType.X);
        } else {
            Integer moveNumber = Integer.valueOf(9 - openSpaces().length());

            if (openSpaces().length() == 1) {
                // last space is used
                board.recordMove(Integer.valueOf(openSpaces()), MarkerType.X);
            } else {

                if (moveNumber >= 3) {
                    Integer winSquare = board.getWinSquare(MarkerType.X.toString());
                    if (winSquare > 0) {
                        // I can Win
                        board.recordMove(winSquare, MarkerType.X);
                        return;
                    }

                    Integer blockSquare = board.getWinSquare(MarkerType.O.toString());
                    if (blockSquare > 0) {
                        // I must block
                        board.recordMove(blockSquare, MarkerType.X);
                        return;
                    }
                }

                moveToOpenSpot();

            }
        }
    }

    private static void moveToOpenSpot() {
        boolean canMove = false;

        StringBuffer buffer = new StringBuffer(); // for gathering attempts info
        // if move sequence repeats
        buffer.append("Move attempt information:\n\n");
        buffer.append("openSpaces: " + openSpaces() + "\n");
        for (String openSpace : openSpaces().split(StringUtils.EMPTY)) {
            buffer.append("Trying: " + openSpace + " canMove: ");
            canMove = squareIsValid(Integer.valueOf(openSpace));
            buffer.append(canMove + "\n");
            if (canMove) {
                board.recordMove(Integer.valueOf(openSpace), MarkerType.X);
                break;
            }
        }
        buffer.append("\n");
        if (!canMove) {
            // We should never hit this if the program is correct
            System.out.println("Internal Move History:\n");
            int i = 0;
            for (Integer move : moveHistory) {
                System.out.println("Move " + i++ + ": " + move.toString());
            }
            System.out.println();
            System.out.println(buffer.toString());
            buffer.setLength(0);
            throw new IllegalStateException("Cannot move without repeating a loss sequence.");
        }
    }

    private static String openSpaces() {
        return board.getOpenSpaces();
    }

    private static Integer getRandomStart() {
        return Integer.valueOf(rng.nextInt(9) + 1);
    }

    private static boolean squareIsValid(Integer prospectMove) {
        String movesWithoutOpens = null;
        boolean isValid = true;
        if (moveHistory.size() == 0 && board.getMoves() == 0) {
            return true;
        }

        for (RotationDegrees rotatedBy : RotationDegrees.values()) {
            movesWithoutOpens = board.getMovesWithRotation(rotatedBy).replaceAll("0", "");
            movesWithoutOpens += prospectMove.toString();

            Iterator<Integer> historyIterator = moveHistory.iterator();
            while (isValid && historyIterator.hasNext()) {
                Integer historicMove = historyIterator.next();
                if (historicMove.toString().startsWith(movesWithoutOpens)) {
                    isValid = false;
                }
            }

            if (isValid) {
                break;
            }

        }
        return isValid;
    }

    private static Pattern getOpenSpaceChoices() {
        String choicesPattern = "[" + openSpaces() + "]";
        return Pattern.compile(choicesPattern);
    }

    private static void setup() {
        if (Files.notExists(file, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loadHistory();
            if (moveHistory.isEmpty()) {
                lastRecordedLoss = Integer.valueOf(0);
            } else {
                lastRecordedLoss = moveHistory.last();
            }
        }
    }

    protected static void loadHistory() {
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String in = null;
            while ((in = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(in)) {
                    try {
                        moveHistory.add(parseLogEntry(in));
                    } catch (NumberFormatException e) {
                        // Do nothing... we can't fix it and won't add it.
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recordLoss(Integer moves) {
        StringBuffer buffer = new StringBuffer();

        for (RotationDegrees rotation : RotationDegrees.values()) {
            String movesForRotation = board.getMovesWithRotation(rotation);

            if (movesForRotation != null) {
                moveHistory.add(Integer.valueOf(movesForRotation));
            }

            String[] movesArray = movesForRotation.split(StringUtils.EMPTY);

            int movesAdded = 0;

            for (String move : movesArray) {
                if (Integer.valueOf(move) != 0) {
                    if (movesAdded++ > 0) {
                        buffer.append("-");
                    }

                    buffer.append(CoordinateMap.getMapForId(Integer.valueOf(move)).getPrettyCoordinates());
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("UTF-8"),
                    StandardOpenOption.WRITE, StandardOpenOption.APPEND);
                    PrintWriter printer = new PrintWriter(writer, true);) {
                printer.println(buffer.toString());
            } catch (IOException e) {
                System.out.println("Unable to record your win " + movesForRotation + " to the game log ("
                        + file.toString() + ").\n");
            }

            buffer.setLength(0);
        }
    }

    protected static Integer parseLogEntry(String entry) {
        int retValue = 0;
        String[] entries = entry.split("-");
        int exponent = 8;
        for (String coords : entries) {
            int id = CoordinateMap.getCoordinateMapForCoords(coords).getId();
            int value = id * (int) Math.pow(10, exponent);
            --exponent;
            retValue += value;
        }
        return Integer.valueOf(retValue);
    }

    protected static Integer getNextMove() {
        return null;
    }


}
