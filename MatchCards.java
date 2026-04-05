import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Provides user with two modes - (player vs player) and (player vs computer)
 *
 * @author Mubashir Shahzad
 * @since 05 April, 2026
 */
public class MatchCards {
    private String player1Name;
    private String player2Name;

    private int player1Score;
    private int player2Score;

    private int[] cards = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8};
    private int[] showCards = {-1, -1, -1 , -1, -1, -1, -1 , -1, -1, -1, -1 , -1, -1, -1, -1 , -1};
    private int previousCard = -1;
    private HashMap<Integer, Integer> computerMemory = new HashMap<Integer, Integer>(); // HashMap<index of the card, value of the card>

    private final Random random = new Random();
    private final Scanner scanner;

    /**
     * takes the Scanner instance as parameter to prevent System.in dependency issues
     * @param scanner
     */
    public MatchCards(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startGame() {
        System.out.println("<=================== MATCH CARDS ===================>");
        shuffleCards();
        int mode = chooseMode();

        switch (mode) {
            case 1 -> playerVsPlayer();
            case 2 -> playerVsComputer();
        }

        scanner.nextLine();
        decideWinner();
    }

    private void shuffleCards() {
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

    private String validPlayerName(String playerName) {
        while (playerName.length() > 20) {
            System.out.print("Enter name (maximum length = 20): ");
            playerName = scanner.nextLine();
        }

        return playerName;
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
            computerMemory.put(card, cards[card - 1]);
            
            previousCard = card - 1;
            System.out.println("\033[2J\033[1H");
            printCards(-1);
            System.out.println("\n<------------------- " + playerName + " --------------------->");
            System.out.print("Enter the card: ");
            card = scanner.nextInt();
            card = correctCard(card, previousCard + 1); // prevoiusCard = previouslyChosenCard - 1 => previousCard + 1 = previouslyChosenCard
            computerMemory.put(card, cards[card - 1]);

            if (cards[card - 1] == cards[previousCard]) {
                showCards[previousCard] = cards[previousCard];
                showCards[card - 1] = cards[previousCard];
                computerMemory.remove(card);
                computerMemory.remove(previousCard + 1);

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
     * does all the necessary work for computer's turn
     */
    private void computerTurn() {
        String playerName = "Computer";
        boolean cardsMatched = true;

        while ((cardsMatched) && (!allCardsFlipped())) {
            System.out.println("\033[2J\033[1H");
            printCards(-1);

            System.out.println("\n<------------------- " + playerName + " --------------------->");

            int card;
            int cardPair = pairOfCardExists();

            int[] pairofCards = new int[2];
            if (cardPair != -1) {
                pairofCards = pairOfCardsIndices(cardPair);
                card = pairofCards[0];
            } else {
                card = random.nextInt(1, 17);
                card = correctCardComputer(card, previousCard);
                computerMemory.put(card, cards[card - 1]);
            }
            
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

            int previousCardPair = pairOfCard(card, cards[card - 1]);
            
            if (cardPair != -1) {
                card = pairofCards[1];
            } else if (previousCardPair != -1) { // if pair of the first card that is random exists return card, else -1 
                card = previousCardPair;
            } else {
                card = random.nextInt(1, 17);
                card = correctCardComputer(card, previousCard + 1);
                computerMemory.put(card, cards[card - 1]);
            }

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
     * @return card if pair of cards exist in computer memory, -1 otherwise
     */
    private int pairOfCardExists() {
        HashSet<Integer> cardsInMemory = new HashSet<Integer>();

        for (int card : computerMemory.values()) {
            if (cardsInMemory.contains(card)) {
                return card;
            }
            cardsInMemory.add(card);
        }

        return -1;
    }

    /**
     * @param cardPairFound card whose pair is previously found
     * @return an array of size two, containing the indices of pair of cards
     */
    private int[] pairOfCardsIndices(int cardPairFound) {
        int[] indices = new int[2];
        int i = 0;

        for (int index : computerMemory.keySet()) {
            if (computerMemory.get(index) == cardPairFound) {
                indices[i++] = index;
            }
        }

        computerMemory.remove(indices[0]);
        computerMemory.remove(indices[1]);

        return indices;
    }

    /**
     * @return index of the card if it matches the previous card, else -1
     */
    private int pairOfCard(int previousIndex, int prevoiusComputerCard) {
        for (int index : computerMemory.keySet()) {
            if ((computerMemory.get(index) == prevoiusComputerCard) && (index != previousIndex)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * after the games ends, decide the winner based on the score of both players
     */
    private void decideWinner() {
        LeaderBoardManager lbm = new LeaderBoardManager();
        System.out.println("\033[2J\033[1H");

        if (player1Score > player2Score) {
            System.out.println("Congratulations! " + player1Name + ".");
            System.out.println("Better Luck Next Time! " + player2Name + ".");
            lbm.addEntryInLeaderBoard(player1Name, "Match Cards");
        } else if (player2Score > player1Score) {
            System.out.println("Congratulations! " + player2Name + ".");
            System.out.println("Better Luck Next Time! " + player1Name + ".");
            lbm.addEntryInLeaderBoard(player2Name, "Match Cards");
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
        player1Name = validPlayerName(player1Name);
        System.out.print("Enter name (Player 2): ");
        player2Name = scanner.nextLine();
        player2Name = validPlayerName(player2Name);

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
        player1Name = validPlayerName(player1Name);
        player2Name = "Computer";

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