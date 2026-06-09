package game.hub;

import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * It is CLI based Snake and Ladder Game. Provides user with two modes - (player vs player) and (player vs computer).
 * 
 * @author Mubashir Shahzad
 * @since 05 April, 2026
 */
public class SnakeAndLadder {
    // ANSI color codes
    private final String RESET  = "\u001B[0m";
    private final String RED    = "\u001B[31m";
    private final String GREEN  = "\u001B[32m";
    private final String YELLOW = "\u001B[33m";
    private final String CYAN   = "\u001B[36m";
    private final String WHITE  = "\u001B[37m";
    private final String BOLD   = "\u001B[1m";

    // players symbols
    private static final String player1Symbol = "1️⃣";
    private static final String player2Symbol = "2️⃣";

    // players numbers
    private int player1Number = 0;
    private int player2Number = 0;
    
    // players names
    private String player1Name;
    private String player2Name;

    private final Random random = new Random();
    private final Scanner scanner;

    // Snake positions: head -> tail
    static final int[][] SNAKES = {
        {97, 78}, {95, 56}, {88, 24}, {62, 19}, {48, 26}, {36, 6}, {32, 10}
    };

    // Ladder positions: bottom -> top
    static final int[][] LADDERS = {
        {1, 38}, {4, 14}, {9, 31}, {20, 59}, {28, 84}, {40, 59}, {51, 67}, {63, 81}, {71, 91}
    };

    public SnakeAndLadder(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startGame() {
        printTitle();
        printBoard();

        int mode = takePlayerChoice();
        switch (mode) {
            case 1 -> playerVsPlayer();
            case 2 -> playerVsComputer();
        }

        decideWinner(); 
    }

    private int takePlayerChoice() {
        System.out.println("Available modes are");
        System.out.println("\t1. Player vs Player");
        System.out.println("\t2. Player vs Computer");
        System.out.print("Enter the mode: ");
        int mode = scanner.nextInt();

        while (mode < 1 || mode > 2) {
            System.out.print("Enter the mode: ");
            mode = scanner.nextInt();
        }
        // clearing the input buffer for input of player's name
        scanner.nextLine();
        
        return mode;
    }

    private void decideWinner() {
        LeaderBoardManager lbm = new LeaderBoardManager();

        System.out.println("\033[2J" + "\033[1H");
        if (player1Number == 100) {
            System.out.println("\nCongratulations! " + player1Name + " won the game.");
            System.out.println("Better Luck Next Time! " + player2Name + ".");
            lbm.addEntryInLeaderBoard(player1Name, "Snake and Ladder");
        } else {
            System.out.println("Congratulations! " + player2Name + " won the game.");
            System.out.println("Better Luck Next Time! " + player1Name + ".");
            lbm.addEntryInLeaderBoard(player2Name, "Snake and Ladder");
        }
    }

    private String validPlayerName(String playerName) {
        while (playerName.length() > 20) {
            System.out.print("Enter name (maximum length = 20): ");
            playerName = scanner.nextLine();
        }

        return playerName;
    }

    private void playerVsPlayer() {
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        player1Name = validPlayerName(player1Name);
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();
        player2Name = validPlayerName(player2Name);

        while (player1Number != 100 && player2Number != 100) {
            System.out.println("\n<--------------------- " + player1Name + " ----------------->");
            System.out.print("Enter any letter to throw the dice: ");
            scanner.next();
            player1Number = rollDice(player1Number);
            player1Number = ladderCheck(player1Number);
            player1Number = snakeCheck(player1Number);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted!");
            }

            // so if the player 1 gets to 100, loop is broken and player 2 does not get the turn
            if (player1Number == 100) break;

            // clears the terminal scannerreen and moves the cursor to the top left corner
            System.out.println("\033[2J" + "\033[1H");
            printBoard();

            System.out.println("\n<--------------------- " + player2Name + " ----------------->");
            System.out.print("Enter any letter to throw the dice: ");
            scanner.next();
            player2Number = rollDice(player2Number);
            player2Number = ladderCheck(player2Number);
            player2Number = snakeCheck(player2Number);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted!");
            }

            System.out.println("\033[2J" + "\033[1H");
            printBoard();
        }

        scanner.nextLine(); // to clear the new line character in input buffer because of scanner.next()
    }

    private void playerVsComputer() {
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        player1Name = validPlayerName(player1Name);
        player2Name = "Computer";

        while (player1Number != 100 && player2Number != 100) {
            System.out.println("\n<--------------------- " + player1Name + " ----------------->");
            System.out.print("Enter any letter to throw the dice: ");
            scanner.next();
            player1Number = rollDice(player1Number);
            player1Number = ladderCheck(player1Number);
            player1Number = snakeCheck(player1Number);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted!");
            }

            // so if the player 1 gets to 100, loop is broken and player 2 does not get the turn
            if (player1Number == 100) break;

            // clears the terminal screen and moves the cursor to the top left corner
            System.out.println("\033[2J" + "\033[1H");
            printBoard();

            System.out.println("\n<--------------------- Computer ------------------>");
            player2Number = rollDice(player2Number);
            player2Number = ladderCheck(player2Number);
            player2Number = snakeCheck(player2Number);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted!");
            }

            System.out.println("\033[2J" + "\033[1H");
            printBoard();
        }

        scanner.nextLine(); // to clear the new line character in input buffer because of scanner.next()
    }

    // returns a random number for the player
    private int rollDice(int playerNumber) {
        int number = random.nextInt(1, 7);
        System.out.println("You got " + number);

        if ((playerNumber + number) > 100) {
            return playerNumber;
        } else {
            return playerNumber + number;
        }
    }

    private int ladderCheck(int playerNumber) {
        for (int[] l : LADDERS) {
            if (l[0] == playerNumber) {
                System.out.println("Ladder at " + playerNumber + ", your new position is " + l[1] + ".");
                return l[1];
            }
        }
        return playerNumber;
    }

    private int snakeCheck(int playerNumber) {
        for (int[] s : SNAKES) {
            if (s[0] == playerNumber) {
                System.out.println("Bitten by the Snake at " + playerNumber + ", your new position is " + s[1] + ".");
                return s[1];
            }
        }
        return playerNumber;
    }

    private void printTitle() {
        System.out.println();
        System.out.println(BOLD + YELLOW +
            "  ╔══════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + YELLOW +
            "  ║    🎲  S N A K E S   &   L A D D E R S  🎲                       ║" + RESET);
        System.out.println(BOLD + YELLOW +
            "  ╚══════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void printBoard() {
        // Build grid: row 0 = top (cells 91-100), row 9 = bottom (cells 1-10)
        int[][] grid = new int[10][10];
        for (int row = 0; row < 10; row++) {
            int baseRow = 9 - row; // row 9 = numbers 1-10 at bottom
            for (int col = 0; col < 10; col++) {
                if (baseRow % 2 == 0) {
                    // even base-row: left to right
                    grid[row][col] = baseRow * 10 + col + 1;
                } else {
                    // odd base-row: right to left
                    grid[row][col] = baseRow * 10 + (10 - col);
                }
            }
        }

        // Print top border
        System.out.println("  ╔════╦════╦════╦════╦════╦════╦════╦════╦════╦════╗");

        for (int row = 0; row < 10; row++) {
            // --- Number row ---
            System.out.print("  ║");
            for (int col = 0; col < 10; col++) {
                int cell = grid[row][col];
                String color = getCellNumberColor(cell);
                System.out.printf("%s%3d %s║", color + BOLD, cell, RESET);
            }

            System.out.println();
            // empty line between number and symbol row
            System.out.print("  ║");
            for (int col = 0; col < 10; col++) {
                int player1Row = (player1Number % 10 == 0) ? (10 - (player1Number / 10)) : (9 - (player1Number / 10));
                // subtracting 1 to keep the number in range of 1 to 9
                int player1Col = ((player1Number % 10 == 0) ? (10) : (player1Number % 10)) - 1;
                player1Col = (row % 2 == 0) ? (9 - player1Col) : (player1Col);

                int player2Row = (player2Number % 10 == 0) ? (10 - (player2Number / 10)) : (9 - (player2Number / 10));
                // subtracting 1 to keep the number in range of 1 to 9
                int player2Col = ((player2Number % 10 == 0) ? (10) : (player2Number % 10)) - 1;
                player2Col = (row % 2 == 0) ? (9 - player2Col) : (player2Col);
                
                if (((row == player1Row) && (col == player1Col)) && ((row == player2Row) && (col == player2Col))) {
                    System.out.print(player2Symbol + "  " +  player1Symbol +  "║");
                } else if ((row == player1Row) && (col == player1Col)) {
                    System.out.print(" " + player1Symbol +  "  ║");
                } else if ((row == player2Row) && (col == player2Col)) {
                    System.out.print(" " + player2Symbol +  "  ║");
                } else {
                    System.out.print("    ║");
                }
            }

            System.out.println();

            // --- Symbol row ---
            System.out.print("  ║");
            for (int col = 0; col < 10; col++) {
                int cell = grid[row][col];
                String sym = getCellSymbol(cell);
                System.out.print(sym + "║");
            }
            System.out.println();

            // --- Row separator ---
            if (row < 9) {
                System.out.println("  ╠════╬════╬════╬════╬════╬════╬════╬════╬════╬════╣");
            } else {
                System.out.println("  ╚════╩════╩════╩════╩════╩════╩════╩════╩════╩════╝");
            }
        }
    }

    private String getCellNumberColor(int cell) {
        // Snake head
        for (int[] s : SNAKES) {
            if (s[0] == cell) return RED;
        }
        // Ladder bottom
        for (int[] l : LADDERS) {
            if (l[0] == cell) return GREEN;
        }
        // Special cells
        if (cell == 100) return YELLOW;
        if (cell == 1)   return CYAN;
        return WHITE;
    }

    private String getCellSymbol(int cell) {
        // Snake head
        for (int[] s : SNAKES) {
            if (s[0] == cell) {
                return RED + BOLD + " 🐍 " + RESET;
            }
        }
        // Ladder bottom
        for (int[] l : LADDERS) {
            if (l[0] == cell) {
                return GREEN + BOLD + " 🪜 " + RESET;
            }
        }
        // Cell 100 = WIN
        if (cell == 100) return YELLOW + BOLD + " 🏆 " + RESET;
        // Cell 1 = START
        if (cell == 1)   return CYAN   + BOLD + " 🚀 " + RESET;
        // Normal cell
        return "    ";
    }
}
