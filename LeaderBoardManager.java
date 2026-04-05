import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages the LeaderBoard
 */
public class LeaderBoardManager {
    /**
     * creates a leader board file, if it does not exist
     */
    private void creatLeaderBoard() {
        boolean newLeaderBoardCreated = false;
        try {
            File leaderBoardFile = new File("leader_board.txt");
            newLeaderBoardCreated = leaderBoardFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Error occured while creating file!");
            e.printStackTrace();
        }

        if (newLeaderBoardCreated) {
            try (FileWriter leaderBoardWriter = new FileWriter("leader_board.txt")) {
                leaderBoardWriter.write("Name\t\t\t\t\t\tScore\t\tGame\n\n");
            } catch (IOException e) {
                System.out.println("Error while writing into the Leader Board file! from function: {createLeaderBoard}.");
            }
        }
    }

    /**
     * update the score of the player if he already exists, otherwise add the player to leaderboard
     * @param winnerName name of the winner
     * @param gameName name of the game
     */
    public void addEntryInLeaderBoard(String winnerName, String gameName) {
        creatLeaderBoard();

        boolean entryExists = false;
        ArrayList<String> leaderBoardEntries = new ArrayList<String>();
        ArrayList<Integer> leaderBoardEntriesScores = new ArrayList<Integer>();
        File leaderBoard = new File("leader_board.txt");

        try (Scanner leaderBoardReader = new Scanner(leaderBoard)) {

            leaderBoardReader.nextLine(); // skip header

            while (leaderBoardReader.hasNextLine()) {
                String line = leaderBoardReader.nextLine().trim();

                if (line.isEmpty())
                    continue;

                String name = line.substring(0, 48).trim();
                int score = Integer.parseInt(line.substring(48, 63).trim());
                String game = line.substring(63).trim();

                if ((name.equals(winnerName)) && (game.equals(gameName))) {
                    entryExists = true;
                    score += 1;
                }

                leaderBoardEntries.add(String.format("%-47s %-15d %s%n", name, score, game));
                leaderBoardEntriesScores.add(score);
            }

            // make a new entry for player, if player does not exist in leader board
            if (!entryExists) {
                leaderBoardEntries.add(String.format("%-47s %-15d %s%n", winnerName, 1, gameName));
            }

        } catch (IOException e) {

            System.out.println("Error occured while writing to the Leader Board file! from function: { addEntryInLeaderBoard }.");
            e.printStackTrace();

        }

        sortLeaderBoardEntries(leaderBoardEntriesScores, leaderBoardEntries);

        try (FileWriter leaderBoardWriter = new FileWriter("leader_board.txt")) {

            leaderBoardWriter.write(String.format("%-47s %-15s %s%n%n", "Name", "Score", "Game"));
            for (String entry : leaderBoardEntries) {
                leaderBoardWriter.write(entry);
            }

        } catch (IOException e) {
            System.out.println("Error occured while writing to the leader board! from function: { addEntryInLeaderBoard }");
            e.printStackTrace();
        }
    }

    /**
     * sorts the entries in the leader board using the scores
     */
    private void sortLeaderBoardEntries(ArrayList<Integer> scores, ArrayList<String> entries) {
		for (int i = 1; i < scores.size(); i++) {
			for (int j = i; (j > 0) && (scores.get(j - 1) < scores.get(j)); j--) {
                int scoreTemp = scores.get(j - 1);
                String entryTemp = entries.get(j - 1);

				scores.set(j - 1, scores.get(j));
                entries.set(j - 1, entries.get(j));

				scores.set(j, scoreTemp);
                entries.set(j, entryTemp);
			}
		}
    }

    /**
     * prints leader board
     */
    public void printLeaderBoard() {
        creatLeaderBoard();
        File leaderBoard = new File("leader_board.txt");

        try (Scanner leaderBoardReader = new Scanner(leaderBoard)) {
            while (leaderBoardReader.hasNextLine()) {
                System.out.println(leaderBoardReader.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error occured while reading the leader board file! from function: { printLeaderBoard }.");
            e.printStackTrace();
        }
    }

}