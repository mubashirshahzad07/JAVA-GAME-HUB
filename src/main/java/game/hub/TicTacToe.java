package game.hub;

import java.util.Scanner;
import java.util.Random;


/**
 * It provides user with two modes - (player vs player) and (player vs computer)
 * 
 * @author Mubashir Shahzad
 * @since 05 April, 2026
 */
public class TicTacToe {
    // 0 for none, -1 for cross and 1 for tick
    private int[] grid = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    private String player1Name;
    private String player2Name;

    private int player1Cell;
    private int player2Cell;

    private final char tick = '✓';
    private final char cross = 'x';

    private final Scanner scanner;
    private final Random random = new Random();

    public TicTacToe(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startGame() {
        System.out.println("<=========================== Tic Tac Toe =====================>");
        int mode = chooseMode();

        switch (mode) {
            case 1 -> playerVsPlayer();
            case 2 -> playerVsComputer();
        }
    }

    private int chooseMode() {
        System.out.println("Available modes are");
        System.out.println("\t1. Player vs Player");
        System.out.println("\t2. Player vs Computer");
        System.out.print("Enter the mode: ");
        int mode = scanner.nextInt();

        while (mode < 1 || mode > 2) {
            System.out.print("Enter the mode: ");
            mode = scanner.nextInt();
        }
        // clean \n from the input buffer
        scanner.nextLine();
        return mode;
    }

    private String validPlayerName(String playerName) {
        while (playerName.length() > 20) {
            System.out.print("Enter name (maximum length = 20): ");
            playerName = scanner.nextLine();
        }

        return playerName;
    }

    private void playerVsPlayer() {
        System.out.println("\033[2J\033[1H");
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        player1Name = validPlayerName(player1Name);
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();
        player2Name = validPlayerName(player2Name);

        while ((!hasWinner()) && (!isDraw())) {
            System.out.println("\033[2J\033[1H");
            printGrid();
            System.out.println("\n<---------------------- " + player1Name + " ---------------------->");
            System.out.print("Enter the cell (" + tick + "): ");
            player1Cell = scanner.nextInt();
            player1Cell = isCorrectCell(player1Cell);
            grid[player1Cell - 1] = 1;
            System.out.println("\033[2J" + "\033[1H");
            printGrid();

            if (hasWinner()) {
                break;
            }

            System.out.println("\n<---------------------- " + player2Name + " ---------------------->");
            System.out.print("Enter the cell (" + cross + "): ");
            player2Cell = scanner.nextInt();
            player2Cell = isCorrectCell(player2Cell);
            grid[player2Cell - 1] = -1;
            System.out.println("\033[2J" + "\033[1H");
            printGrid();
        }

        scanner.nextLine(); // cleans \n from the input buffer
    }

    private void playerVsComputer() {
        System.out.println("\033[2J\033[1H");
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        player1Name = validPlayerName(player1Name);
        player2Name = "Computer";

        while ((!hasWinner()) && (!isDraw())) {
            System.out.println("\033[2J\033[1H");
            printGrid();
            System.out.println("\n<---------------------- " + player1Name + " ---------------------->");
            System.out.print("Enter the cell (" + tick + "): ");
            player1Cell = scanner.nextInt();
            player1Cell = isCorrectCell(player1Cell);
            grid[player1Cell - 1] = 1;
            System.out.println("\033[2J\033[1H");
            printGrid();

            if (hasWinner()) {
                break;
            }

            player2Cell = random.nextInt(1, 9);
            while ((!isEmptyCell(player2Cell)) || (!isValidCell(player2Cell))) {
                player2Cell = random.nextInt(1, 9);
            }
            grid[player2Cell - 1] = -1;

            System.out.println("\033[2J\033[1H");
            printGrid();
        }

        scanner.nextLine(); // cleans \n from the input buffer
    }

    /**
     * Keep on reprompting the player for cell as long as the cell is already occupied or cell is not valid 
     * @param cell
     * @return a valid and empty cell
     */
    private int isCorrectCell(int cell) {
        if (isValidCell(cell) && isEmptyCell(cell)) {
            return cell;
        }

        if (!isValidCell(cell)) {
            System.out.println("Cell is out of range! It must be in the range of 1 - 9.");
            System.out.print("ReEnter the cell (1 - 9): ");
        } else {
            System.out.println("Cell is already occupied!");
            System.out.print("ReEnter the cell (1 - 9): ");
        }
        cell = scanner.nextInt();

        // clears the \n from input buffer
        scanner.nextLine();
        return isCorrectCell(cell);
    }

    /**
     * @param cell
     * @return true if cell is in the range of 1..9, false otherwise
     */
    private boolean isValidCell(int cell) {
        if (cell >= 1 && cell <= 9) {
            return true;
        }
        return false;
    }

    /**
     * @param cell
     * @return true if cell is not occupied, false otherwise
     */
    private boolean isEmptyCell(int cell) {
        if (grid[cell - 1] == 0) {
            return true;
        }
        return false;
    }

    private void printGrid() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[(i * 3) + j] == 0) {
                    System.out.print("        ");
                } else if (grid[(i * 3) + j] == -1) {
                    System.out.print("    " + cross + "   ");
                } else {
                    System.out.print("    " + tick + "   ");
                }

                if (j < 2) {
                    System.out.print("❚");
                }
            }

            if (i < 2) {
                System.out.println("\n   ━━━━━━━━━━━━━━━━━━━━━");
            }
        }
        System.out.print("\n");
    }

    private boolean hasWinner() {
        if (hasWinnerInRows() || hasWinnerInColumns() || hasWinnerInDiagonals()) {
            return true;
        }

        return false;
    }

    private boolean hasWinnerInRows() {
        for (int i = 0; i < 3; i++) {
            if ((grid[(i * 3)] == grid[(i * 3) + 1]) && (grid[(i * 3)] == grid[(i * 3) + 2]) && (grid[i * 3] != 0)) {
                decideWinner(grid[i * 3]);
                return true;
            }
        }

        return false;
    }

    private boolean hasWinnerInColumns() {
        for (int j = 0; j < 3; j++) {
            if (((grid[j]) == grid[j + 3]) && (grid[j] == grid[j + 6]) && (grid[j] != 0)) {
                decideWinner(grid[j]);
                return true;
            }
        }

        return false;
    }

    private boolean hasWinnerInDiagonals() {
        if ((grid[0] == grid[4]) && (grid[0] == grid[8]) && (grid[0] != 0)) {
            decideWinner(grid[0]);
            return true;
        }

        if ((grid[2] == grid[4]) && (grid[2] == grid[6]) && (grid[2] != 0)) {
            decideWinner(grid[2]);
            return true;
        }

        return false;
    }

    private void decideWinner(int winnerNumber) {
        LeaderBoardManager lbm = new LeaderBoardManager();

        if (winnerNumber == 1) {
            System.out.println("\nCongratulations! " + player1Name + " has won.");
            System.out.println("Better Luck Next Time! " + player2Name + ".");
            lbm.addEntryInLeaderBoard(player1Name, "Tic Tac Toe");
        } else {
            System.out.println("\nCongratulations! " + player2Name + " has won.");
            System.out.println("Better Luck Next Time! " + player1Name + ".");
            lbm.addEntryInLeaderBoard(player2Name, "Tic Tac Toe");
        }
    }

    private boolean isDraw() {
        for (int i = 0; i < 9; i++) {
            if (grid[i] == 0) {
                return false;
            }
        }

        return true;
    }
}