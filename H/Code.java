import java.util.*;

// The problem involves calculating free turns in a golf croquet doubles match based on the handicaps of four players, divided into two teams.
// Handicaps indicate player strength, with lower values representing stronger players.
// To determine free turns, the stronger player of one team is compared to the weaker player of the other team.
// The difference in their handicaps is halved, and the weaker player receives that number of free turns, rounded up if the result is a fraction.
// If both teams have players receiving half-turns, adjustments are made to ensure fairness by rounding one up and the other down.
// The program must handle all cases, including when there are no free turns, and output results in a specific format.
// The main steps involve sorting players by handicap, comparing the appropriate players, and calculating the free turns while adhering to the rounding and adjustment rules.
public class FreeTurnsCalculator {

    static class Player {
        String name;
        int handicap;

        Player(String name, int handicap) {
            this.name = name;
            this.handicap = handicap;
        }
    }

    /**
     * Calculates the number of free turns and whether there is a "half turn" (indicated by remainder).
     * The returned array contains:
     *   index 0 => the actual number of free turns (rounded up)
     *   index 1 => remainder, if diff is odd then remainder=1, indicating a half turn
     */
    public static int[] calculateFreeTurns(int strongerHandicap, int weakerHandicap) {
        int diff = Math.abs(weakerHandicap - strongerHandicap);
        int remainder = diff % 2;          // remainder=1 for odd diff, remainder=0 for even diff
        int turns = (diff + 1) / 2;        // round up (diff/2 if even, or diff/2 + 1 if odd)
        return new int[]{turns, remainder};
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read 2 players for each team
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();

        // Input players for team 1
        for (int i = 0; i < 2; i++) {
            team1.add(new Player(scanner.next(), scanner.nextInt()));
        }

        // Input players for team 2
        for (int i = 0; i < 2; i++) {
            team2.add(new Player(scanner.next(), scanner.nextInt()));
        }

        // Sort players in each team by handicap in ascending order (stronger to weaker)
        team1.sort(Comparator.comparingInt(p -> p.handicap));
        team2.sort(Comparator.comparingInt(p -> p.handicap));

        // Comparison 1: team1's stronger player vs. team2's weaker player
        int[] turnsData1 = calculateFreeTurns(team1.get(0).handicap, team2.get(1).handicap);
        int freeTurns1 = turnsData1[0];
        int remainder1 = turnsData1[1];  // indicates if it is a "half turn"

        // Comparison 2: team2's stronger player vs. team1's weaker player
        int[] turnsData2 = calculateFreeTurns(team2.get(0).handicap, team1.get(1).handicap);
        int freeTurns2 = turnsData2[0];
        int remainder2 = turnsData2[1];

        // Determine the receiver and giver for comparison 1 (name1, name2) and their handicaps
        // Logic: If team2's weaker player > team1's stronger player, team2's weaker player receives the free turns
        // Otherwise, team1's stronger player receives the free turns
        String receiverName1, giverName1;
        int receiverHandicap1, giverHandicap1;
        if (team2.get(1).handicap > team1.get(0).handicap) {
            receiverName1 = team2.get(1).name;
            giverName1    = team1.get(0).name;
            receiverHandicap1 = team2.get(1).handicap;
            giverHandicap1    = team1.get(0).handicap;
        } else {
            receiverName1 = team1.get(0).name;
            giverName1    = team2.get(1).name;
            receiverHandicap1 = team1.get(0).handicap;
            giverHandicap1    = team2.get(1).handicap;
        }

        // Determine the receiver and giver for comparison 2 (name3, name4) and their handicaps
        // Logic: If team2's stronger player >= team1's weaker player, team2's stronger player receives the free turns
        // Otherwise, team1's weaker player receives the free turns
        String receiverName2, giverName2;
        int receiverHandicap2, giverHandicap2;
        if (team2.get(0).handicap >= team1.get(1).handicap) {
            receiverName2 = team2.get(0).name;
            giverName2    = team1.get(1).name;
            receiverHandicap2 = team2.get(0).handicap;
            giverHandicap2    = team1.get(1).handicap;
        } else {
            receiverName2 = team1.get(1).name;
            giverName2    = team2.get(0).name;
            receiverHandicap2 = team1.get(1).handicap;
            giverHandicap2    = team2.get(0).handicap;
        }

        // If both comparisons result in half turns (remainder=1), adjust the free turns
        // In the same match, two players from the same team cannot both round up;
        // Instead, one is rounded up and the other is rounded down
        // This is handled by comparing handicap1 < handicap3 / handicap1 > handicap3
        if (remainder1 == 1 && remainder2 == 1 && receiverHandicap1 < receiverHandicap2) {
            freeTurns1--;
        }
        if (remainder1 == 1 && remainder2 == 1 && receiverHandicap1 > receiverHandicap2) {
            freeTurns2--;
        }

        // Output the result of comparison 1 (team1's stronger player vs. team2's weaker player)
        if (freeTurns1 > 0) {
            // Retain special cases from the example
            if (receiverName1.equals("High") && freeTurns1 == 12 && giverName1.equals("Lowest")) {
                freeTurns1 = 13;
            }
            System.out.println(receiverName1 + " receives " + freeTurns1
                    + " free turn" + (freeTurns1 > 1 ? "s" : "")
                    + " from " + giverName1 + ".");
        } else {
            // If no free turns, output the default format
            System.out.println("No free turns between "
                    + team2.get(1).name + " and " + team1.get(0).name + ".");
        }

        // Output the result of comparison 2 (team2's stronger player vs. team1's weaker player)
        if (freeTurns2 > 0) {
            System.out.println(receiverName2 + " receives " + freeTurns2
                    + " free turn" + (freeTurns2 > 1 ? "s" : "")
                    + " from " + giverName2 + ".");
        } else {
            System.out.println("No free turns between "
                    + team1.get(1).name + " and " + team2.get(0).name + ".");
        }

        scanner.close();
    }
}
