import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Random;


public class Games {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int game;
        do {
            System.out.println("\033[2J" + "\033[H");
            System.out.println("Available Games are");
            System.out.println("\t1. Rock Paper Scissor");
            System.out.println("\t2. Snake and Ladder");
            System.out.println("\t3. Tic Tac Toe");
            System.out.println("\t4. Match Cards");
            System.out.println("\t5. Quit");
            System.out.print("Enter the game you want to play: "); 
            game = scanner.nextInt();

            while (game < 1 || game > 5) {
                System.out.print("Enter the game you want to play: ");
                game = scanner.nextInt();
            }

            System.out.println("\033[2J\033[1H");

            switch (game) {
                case 1:
                    RockPaperScissor rps = new RockPaperScissor(scanner);
                    rps.startGame();
                    break;

                case 2:
                    SnakeAndLadder snl = new SnakeAndLadder(scanner);
                    snl.startGame();
                    break;

                case 3:
                    TicTacToe ttt = new TicTacToe(scanner);
                    ttt.startGame();
                    break;
                
                case 4:
                    MatchCards mc = new MatchCards(scanner);
                    mc.startGame();
                    break;

                case 5:
                    scanner.close();
                    System.exit(0);
            }

            System.out.print("\nEnter any character to continue to the main menu: ");
            scanner.nextLine();
        } while (game != 5);

        scanner.close();
    }
}


class RockPaperScissor {
    private int player1Choice;
    private int player2Choice;
    private String player1Name;
    private String player2Name;
    private Random random = new Random();
    String[] choices = {"Rock", "Paper", "Scissor"};
    private final Scanner scanner;

    RockPaperScissor(Scanner scanner) {
        this.scanner = scanner;
    }

    void startGame() {
        System.out.println("<================ ROCK PAPER SCISSOR ================>");
        int mode = chooseMode();

        switch (mode) {
            case 1 -> playerVsPlayer();
            case 2 -> playerVscanneromputer();
        }

        decideWinner();
    }

    // returns user choice
    private int takePlayerChoice() {
        int choice;

        System.out.print("Enter a number [Rock(1), Paper(2), Scissor(3)]: ");
        choice = scanner.nextInt();

        while (choice < 1 || choice > 3) {
            System.out.print("Wrong choice entered! Number must be between 1 and 3 [Rock(1), Paper(2), Scissor(3)]: ");
            choice = scanner.nextInt();
        }
        // clearing the input buffer for player's name
        scanner.nextLine();

        return choice;
    }

    /** 
     * Displays output based on the player 1 and player 2 choices
     */
    private void decideWinner() {
        System.out.print("\n");

        if (player1Choice == player2Choice) {
            System.out.println("It's a Draw! Both players chose " + choices[player1Choice - 1] + ".");
        } else if ((player1Choice == player2Choice - 1) || (player2Choice == 3 && player1Choice == 1)) {
            System.out.println(player2Name + " Won! " + player1Name + " chose " + choices[player1Choice - 1] + " and " + player2Name + " chose " + choices[player2Choice - 1] + ".");
        } else {
            System.out.println(player1Name + " Won! " + player1Name + " chose " + choices[player1Choice - 1] + " and " + player2Name + " chose " + choices[player2Choice - 1] + ".");
        }
    }

    // checks either player vs player mode or player vs computer mode is chosen
    private int chooseMode() {
        System.out.println("Available modes are ");
        System.out.println("\t1. Player vs Player"); 
        System.out.println("\t2. Player vs Computer"); 
        System.out.print("Enter which mode you want to play: ");

        int mode = scanner.nextInt();

        while (mode > 2 || mode < 1) {
            System.out.print("Wrong mode! Enter a number (1-2): ");
            mode = scanner.nextInt();
        }
        // clearing the input buffer for player's name
        scanner.nextLine();
        System.out.println("\033[2J" + "\033[H");

        return mode;
    }

    private void playerVsPlayer() {
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();

        System.out.println("\n<------------------ " + player1Name + " ----------------->");
        player1Choice = takePlayerChoice();
        System.out.println("\n<------------------ " + player2Name + " ----------------->");
        player2Choice = takePlayerChoice();
    }

    private void playerVscanneromputer() {
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        player2Name = "Computer";
        System.out.println("\n<------------------ " + player1Name + " ----------------->");
        player1Choice = takePlayerChoice();

        player2Choice = random.nextInt(1, 4);
    }
}


/**
 * Tic Tac Toe CLI Game
 */
class TicTacToe {
    // 0 for none, -1 for cross and 1 for tick
    private int[] grid = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private String player1Name;
    private String player2Name;
    private int player1Cell;
    private int player2Cell;
    private char tick = '✓';
    private char cross = 'x';
    private final Scanner scanner;
    private final Random random = new Random();

    TicTacToe(Scanner scanner) {
        this.scanner = scanner;
    }

    void startGame() {
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

    private void playerVsPlayer() {
        System.out.println("\033[2J\033[1H");
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();

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
        if (winnerNumber == 1) {
            System.out.println("\nCongratulations! " + player1Name + " has won.");
            System.out.println("Better Luck Next Time! " + player2Name + ".");
        } else {
            System.out.println("\nCongratulations! " + player2Name + " has won.");
            System.out.println("Better Luck Next Time! " + player1Name + ".");
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


/**
 * Snake and Ladder CLI Game
 */
class SnakeAndLadder {
    // ANSI color codes
    // declared final as these are constants 
    // declared static so they are not created with every instance creation, so the memory is saved
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
    
    // players name
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

    SnakeAndLadder(Scanner scanner) {
        this.scanner = scanner;
    }

    void startGame() {
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
        System.out.println("\033[2J" + "\033[1H");
        if (player1Number == 100) {
            System.out.println("\nCongratulations! " + player1Name + " won the game.");
            System.out.println("Better Luck Next Time! " + player2Name + ".");
        } else {
            System.out.println("Congratulations! " + player2Name + " won the game.");
            System.out.println("Better Luck Next Time! " + player1Name + ".");
        }
    }

    private void playerVsPlayer() {
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();

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
                if (sym == (GREEN + BOLD + " 🪜 " + RESET)) {
                    System.out.print(sym + " ║");
                } else {
                    System.out.print(sym + "║");
                }
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


/**
 * Match Card Memory Game
 */
class MatchCards {
    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private int[] cards = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8};
    private int[] showCards = {-1, -1, -1 , -1, -1, -1, -1 , -1, -1, -1, -1 , -1, -1, -1, -1 , -1};
    private int previousCard = -1;
    private final Random random = new Random();
    private final Scanner scanner;

    /**
     * takes the Scanner instance as parameter to prevent System.in dependency issues
     * @param scanner
     */
    MatchCards(Scanner scanner) {
        this.scanner = scanner;
    }

    void startGame() {
        System.out.println("<=================== MATCH CARDS ===================>");
        shuffleCards();
        int mode = chooseMode();

        switch (mode) {
            case 1 -> playerVsPlayer();
            case 2 -> playerVsComputer();
        }

        decideWinner();
    }

    void shuffleCards() {
        for (int i = (cards.length - 1); i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            int temp = cards[randomIndex];
            cards[randomIndex] = cards[i];
            cards[i] = temp;
        }
    }

    private int chooseMode() {
        System.out.println("Available modes are");
        System.out.println("\t1. Player vs Player");
        System.out.println("\t2. Player vs Computer");
        System.out.print("Enter the mode: ");
        int mode = scanner.nextInt();

        while (mode < 1 || mode > 2) {
            System.out.println("Enter the mode: ");
            mode = scanner.nextInt();
        }

        scanner.nextLine(); // cleans \n from the input buffer
        return mode;
    }

    /**
     * does all the necessary work required for each players turn
     * @param playerName
     */
    private void playersTurn(String playerName) {
        boolean cardsMatched = true;
        while ((cardsMatched) && (!allCardsFlipped())) {
            System.out.println("\033[2J\033[1H");
            printCards(-1);
            System.out.println("\n<------------------- " + playerName + " --------------------->");
            System.out.print("Enter the card: ");
            int card = scanner.nextInt();
            card = correctCard(card, previousCard); // as previousCard = -1 (unset), no need to add 1 here

            previousCard = card - 1;
            System.out.println("\033[2J\033[1H");
            printCards(-1);
            System.out.println("\n<------------------- " + playerName + " --------------------->");
            System.out.print("Enter the card: ");
            card = scanner.nextInt();
            card = correctCard(card, previousCard + 1); // prevoiusCard = previouslyChosenCard - 1 => previousCard + 1 = previouslyChosenCard

            if (cards[card - 1] == cards[previousCard]) {
                showCards[previousCard] = cards[previousCard];
                showCards[card - 1] = cards[previousCard];
                if (playerName.equals(player1Name)) {
                    player1Score++;
                } else {
                    player2Score++;
                }
            } else {
                cardsMatched = false;
                System.out.println("\033[2J\033[1H");
                printCards(card - 1);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                previousCard = -1;
            }
        }
    }

    /**
     * does all the necessary work for computers turn
     */
    private void computerTurn() {
        String playerName = "Computer";
        boolean cardsMatched = true;
        while ((cardsMatched) && (!allCardsFlipped())) {
            System.out.println("\033[2J\033[1H");
            printCards(-1);
            System.out.println("\n<------------------- " + playerName + " --------------------->");
            int card = random.nextInt(1, 17);
            card = correctCardComputer(card, previousCard);
            System.out.println("Selected " + card + ".");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            previousCard = card - 1;
            System.out.println("\033[2J\033[1H");
            printCards(-1);
            System.out.println("\n<------------------- " + playerName + " --------------------->");
            card = random.nextInt(1, 16);
            card = correctCardComputer(card, previousCard + 1);
            System.out.println("Selected " + card + ".");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            if (cards[card - 1] == cards[previousCard]) {
                showCards[previousCard] = cards[previousCard];
                showCards[card - 1] = cards[previousCard];
                player2Score++;
            } else {
                cardsMatched = false;
                System.out.println("\033[2J\033[1H");
                printCards(card - 1);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                previousCard = -1;
            }
        }
    }

    /**
     * after the games ends, decide the winner based on the score of both players
     */
    private void decideWinner() {
        System.out.println("\033[2J\033[1H");
        if (player1Score > player2Score) {
            System.out.println("Congratulations! " + player1Name + ".");
            System.out.println("Better Luck Next Time! " + player2Name + ".");
        } else if (player2Score > player1Score) {
            System.out.println("Congratulations! " + player2Name + ".");
            System.out.println("Better Luck Next Time! " + player1Name + ".");
        } else {
            System.out.println("It's a Draw!");
        }
    }

    /**
     * handles the mode of player vs player
     */
    private void playerVsPlayer() {
        System.out.println("\033[2J\033[1H");
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();

        while (!allCardsFlipped()) {
            playersTurn(player1Name);
            
            if (allCardsFlipped()) break;

            playersTurn(player2Name);
        }

        scanner.nextLine();
    }

    /**
     * handles the mode of player vs computer
     */
    private void playerVsComputer() {
        System.out.println("\033[2J\033[1H");
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();

        while (!allCardsFlipped()) {
            playersTurn(player1Name);

            if (allCardsFlipped()) break;

            computerTurn();
        }
    }

    /** 
     * @param currentCard
     * @param previousCard
     * @return current card if it is valid and not flipped, otherwise reprompts the player to enter a new card
     */
    private int correctCard(int currentCard, int previousCard) {
        if (isValid(currentCard) && isEmpty(currentCard, previousCard)) {
                return currentCard;
        }

        if(!isValid(currentCard)) {
            System.out.println("Card out of range! Card should be in the range 1-16.");
        } else {
            System.out.println("Card is already flipped!");
        }
        System.out.print("ReEnter card (1-16): ");
        currentCard = scanner.nextInt();
        scanner.nextLine(); // clean \n from the input buffer
        return correctCard(currentCard, previousCard);
    }

    /**
     * @param currentCard
     * @param previousCard
     * @return current card if it is valid and not flipped, otherwise chooses a new card for computer
     */
    private int correctCardComputer(int currentCard, int previousCard) {
        if (isEmpty(currentCard, previousCard)) {
                return currentCard;
        }

        while (!isEmpty(currentCard, previousCard)) {
            currentCard = random.nextInt(1, 17);
        }
        return currentCard;
    }

    /**
     * @return true if all cards are flipped, false otherwise
     */
    private boolean allCardsFlipped() {
        for (int card : showCards) {
            if (card == -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param card
     * @return true if card is in 1..16, otherwise false
     */
    private boolean isValid(int card) {
        if (card >= 1 && card <= 16) {
            return true;
        }

        return false;
    }

    /**
     * @param currentCard
     * @param previousCard
     * @return true if card is not flipped, false otherwise
     */
    private boolean isEmpty(int currentCard, int previousCard) {
        if ((showCards[currentCard - 1] == -1) && (currentCard != previousCard)) {
            return true;
        }

        return false;
    }

    /**
     * @param currentCard -1 to be passed for the first card guessed, (cardNumber - 1) for the second guess
     */
    private void printCards(int currentCard) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        for (int i = 0; i < 4; i++) {
            System.out.print("❚");
            for (int j = 0; j < 4; j++) {
                if ((showCards[(i * 4) + j] != -1) || (previousCard == (i * 4) + j) || (currentCard == (i * 4) + j)) {
                    System.out.print("    " + cards[(i * 4) + j] + "   ");
                } else {
                    System.out.print("    x   ");
                }
                System.out.print("❚");
            }

            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        }
    }
}