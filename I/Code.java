import java.util.*;

// The problem is about dividing a map of altitudes into regions called drainage basins, where each basin represents areas that share the same lowest point where water would naturally flow.
// For each cell on the map, water flows to its neighboring cell with the lowest altitude, and if there’s a tie, it follows a priority order.
// If no neighboring cell has a lower altitude, the current cell itself becomes the sink.
// All cells that lead directly or indirectly to the same sink are part of the same basin.
// The goal is to label each basin with a unique lowercase letter starting from 'a', ensuring the labels are assigned in a lexicographically smallest order, with the basin of the top-left corner always labeled as 'a'.
// The solution involves finding the sink for each cell, mapping sinks to unique labels, and assigning these labels to every cell based on their sink.
public class Watershed {

    // Class representing a cell in the map
    static class Cell implements Comparable<Cell> {
        int row, col;

        // Constructor to initialize row and column
        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        // Overriding compareTo for ordering cells, used in maps or sets
        @Override
        public int compareTo(Cell other) {
            if (this.row != other.row) {
                return Integer.compare(this.row, other.row);
            }
            return Integer.compare(this.col, other.col);
        }

        // Overriding equals to check cell equality based on row and column
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Cell cell = (Cell) obj;
            return row == cell.row && col == cell.col;
        }

        // Overriding hashCode to allow this class to be used as a key in HashMaps
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    // Direction vectors for North, West, East, South
    static int[] dR = {-1, 0, 0, 1};
    static int[] dC = {0, -1, 1, 0};

    // Check if a given cell is within map boundaries
    static boolean isValid(int r, int c, int H, int W) {
        return r >= 0 && r < H && c >= 0 && c < W;
    }

    // Find the sink (lowest point) that water flows to for a given cell
    static Cell findSink(int r, int c, int[][] altitudes) {
        int H = altitudes.length;        // Number of rows (height of map)
        int W = altitudes[0].length;     // Number of columns (width of map)

        while (true) {
            int minR = r, minC = c; // Start with current cell as the lowest point
            for (int i = 0; i < 4; i++) { // Check all 4 neighboring directions
                int nr = r + dR[i], nc = c + dC[i];
                // Update the lowest point if a valid neighbor has a lower altitude
                if (isValid(nr, nc, H, W) && altitudes[nr][nc] < altitudes[minR][minC]) {
                    minR = nr;
                    minC = nc;
                }
            }
            // If no lower point is found, this cell is the sink
            if (minR == r && minC == c) break;
            // Move to the new lowest point
            r = minR;
            c = minC;
        }
        return new Cell(r, c); // Return the sink cell
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read map dimensions
        int H = scanner.nextInt();
        int W = scanner.nextInt();

        // Initialize altitude matrix and basin labels
        int[][] altitudes = new int[H][W];
        char[][] labels = new char[H][W];

        // Read the altitudes of the map
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                altitudes[i][j] = scanner.nextInt();
            }
        }

        // Map to store basins and their labels
        Map<Cell, Character> basinMap = new HashMap<>();
        char label = 'a'; // Start with label 'a'

        // Iterate through each cell in the map
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                // Find the sink for the current cell
                Cell sink = findSink(i, j, altitudes);
                // Assign a new label if the sink is not already labeled
                if (!basinMap.containsKey(sink)) {
                    basinMap.put(sink, label++);
                }
                // Assign the label of the sink to the current cell
                labels[i][j] = basinMap.get(sink);
            }
        }

        // Output the basin labels row by row
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                System.out.print(labels[i][j]);
                if (j < W - 1) System.out.print(" "); // Space between labels in the same row
            }
            System.out.println(); // Move to the next row
        }

        scanner.close();
    }
}
