import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// This program calculates the total points for a football pool entry based on the results of 8 selected games and identifies any scoring draws, which are games where both teams score at least one goal but neither team wins.
// The program first takes the names of the games and their corresponding scores as input, storing them in separate lists for easy processing.
// It then evaluates each game's result to calculate points according to the rules: 1 point for a home or away win, 2 points for a 0-0 draw, and 3 points for a scoring draw.
// For scoring draws, the game names are recorded for later output.
// Finally, the program prints the total points and lists all scoring draws in the order they were input; if no scoring draws exist, it outputs "No scoring draws."
// This approach ensures the solution is straightforward and adheres to the problem's requirements.
public class MatchResults {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Lists to store game names, scores, and scoring draws
        List<String> games = new ArrayList<>(); // Store the names of the games
        List<int[]> scores = new ArrayList<>(); // Store the scores for each game
        List<String> scoringDraws = new ArrayList<>(); // Store the games that are scoring draws

        // Input the names of 8 games
        for (int i = 0; i < 8; i++) {
            games.add(scanner.nextLine()); // Add each game name to the list
        }

        // Input the scores for each game
        for (int i = 0; i < 8; i++) {
            int home = scanner.nextInt(); // Home team score
            int away = scanner.nextInt(); // Away team score
            scores.add(new int[] { home, away }); // Add the scores as a pair to the list
        }

        int totalPoints = 0; // Total points scored by the player

        // Calculate the points and identify scoring draws
        for (int i = 0; i < 8; i++) {
            int home = scores.get(i)[0]; // Home team score
            int away = scores.get(i)[1]; // Away team score

            if (home > away) {
                // Home team wins
                totalPoints += 1; // 1 point for a win
            } else if (away > home) {
                // Away team wins
                totalPoints += 1; // 1 point for a win
            } else if (home == 0 && away == 0) {
                // 0-0 draw
                totalPoints += 2; // 2 points for a 0-0 draw
            } else {
                // Scoring draw (both teams score at least one goal)
                totalPoints += 3; // 3 points for a scoring draw
                scoringDraws.add(games.get(i)); // Add the game to the scoring draws list
            }
        }

        // Output the total points
        System.out.println("Points scored: " + totalPoints);

        // Output the scoring draws or a message if none exist
        if (scoringDraws.isEmpty()) {
            System.out.println("No scoring draws"); // No scoring draws found
        } else {
            for (String game : scoringDraws) {
                System.out.println(game); // Print each scoring draw game name
            }
        }

        scanner.close(); // Close the scanner to free resources
    }
}
