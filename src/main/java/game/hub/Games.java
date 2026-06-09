package game.hub;

import java.util.Scanner;

/**
 * Primary class responsible for running Game Hub application.
 *
 * @author Mubashir Shahzad
 * @since 05 April 2026
 */
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
            System.out.println("\t5. View Leader Board");
            System.out.println("\t6. Quit");
            System.out.print("Enter the game you want to play: "); 
            game = scanner.nextInt();

            while (game < 1 || game > 6) {
                System.out.print("Enter the game you want to play: ");
                game = scanner.nextInt();
            }
            scanner.nextLine();

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
                    LeaderBoardManager lbm = new LeaderBoardManager();
                    lbm.printLeaderBoard();
                    break;

                case 6:
                    scanner.close();
                    System.exit(0);
            }

            System.out.print("\nEnter any character to continue to the main menu: ");
            scanner.nextLine();
        } while (game != 6);

        scanner.close();
    }
}