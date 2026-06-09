package game.hub;

import java.util.Random;
import java.util.Scanner;


/**
 * It is a simple Rock Paper Scissor game. Provides user with two modes - (player vs player) and (player vs computer).
 * 
 * @author Mubashir Shahzad
 * @since 05 April, 2026
 */
public class RockPaperScissor {
    private int player1Choice;
    private int player2Choice;

    private String player1Name;
    private String player2Name;

    String[] choices = {"Rock", "Paper", "Scissor"};

    private final Scanner scanner;
    private Random random = new Random();

    public RockPaperScissor(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startGame() {
        System.out.println("<================ ROCK PAPER SCISSOR ================>");
        int mode = chooseMode();

        switch (mode) {
            case 1 -> playerVsPlayer();
            case 2 -> playerVsComputer();
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
     * makes sure that player's name has maximum length of 20
     * @param playerName
     * @result current name if not more than 20 letters, a new name otherwise
     */
    private String validPlayerName(String playerName) {
        while (playerName.length() > 20) {
            System.out.print("Enter name (maximum length = 20): ");
            playerName = scanner.nextLine();
        }

        return playerName;
    }

    /** 
     * Displays output based on the player 1 and player 2 choices
     */
    private void decideWinner() {
        System.out.print("\n");
        LeaderBoardManager lbm = new LeaderBoardManager();

        if (player1Choice == player2Choice) {
            System.out.println("It's a Draw! Both players chose " + choices[player1Choice - 1] + ".");
        } else if ((player1Choice == player2Choice - 1) || (player2Choice == 3 && player1Choice == 1)) {
            System.out.println(player2Name + " Won! " + player1Name + " chose " + choices[player1Choice - 1] + " and " + player2Name + " chose " + choices[player2Choice - 1] + ".");
            lbm.addEntryInLeaderBoard(player2Name, "Rock Paper Scissor");
        } else {
            System.out.println(player1Name + " Won! " + player1Name + " chose " + choices[player1Choice - 1] + " and " + player2Name + " chose " + choices[player2Choice - 1] + ".");
            lbm.addEntryInLeaderBoard(player1Name, "Rock Paper Scissor");
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
        player1Name = validPlayerName(player1Name);
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();
        player2Name = validPlayerName(player2Name);

        System.out.println("\n<------------------ " + player1Name + " ----------------->");
        player1Choice = takePlayerChoice();
        System.out.println("\n<------------------ " + player2Name + " ----------------->");
        player2Choice = takePlayerChoice();
    }

    private void playerVsComputer() {
        System.out.print("Enter name (Player 1): ");
        player1Name = scanner.nextLine();
        player1Name = validPlayerName(player1Name);
        player2Name = "Computer";
        System.out.println("\n<------------------ " + player1Name + " ----------------->");
        player1Choice = takePlayerChoice();

        player2Choice = random.nextInt(1, 4);
    }
}