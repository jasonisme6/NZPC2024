import java.util.*;

// The goal is to help a pedestrian find the fastest way to cross a grid-based city from one corner to the opposite.
// Each intersection has traffic lights with alternating cycles for north-south and east-west directions.
// The pedestrian can cross a street in 1 minute if the light is green or walk along the edge of a block in 2 minutes.
// To solve the problem, we calculate the time it takes to move through each point in the grid, considering the traffic light cycles at each intersection.
// Starting from the southwest corner, we explore all possible movements using a queue, always updating the shortest time to reach each point.
// By carefully accounting for light timings and waiting if necessary, we eventually determine the minimum time needed to reach the northeast corner.
public class GridPathSolver {

    // Class representing a point in the grid
    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Constants and movement directions
    static final int MAX_GRID_SIZE = 20; // Maximum grid size
    static final int[] DX = {1, -1, 0, 0}; // Movement directions along x-axis
    static final int[] DY = {0, 0, 1, -1}; // Movement directions along y-axis

    // Traffic signal data
    static int[][] greenTime = new int[MAX_GRID_SIZE][MAX_GRID_SIZE]; // Green light duration for north-south direction
    static int[][] redTime = new int[MAX_GRID_SIZE][MAX_GRID_SIZE];   // Green light duration for east-west direction
    static int[][] cycleStart = new int[MAX_GRID_SIZE][MAX_GRID_SIZE]; // Signal cycle start time

    // Variables for shortest path calculation
    static long[][] minTime = new long[MAX_GRID_SIZE * 2][MAX_GRID_SIZE * 2]; // Minimum time to reach each point
    static boolean[][] inQueue = new boolean[MAX_GRID_SIZE * 2][MAX_GRID_SIZE * 2]; // Tracks if a point is in the queue

    // Calculate the next valid crossing time for the pedestrian
    static long calculateTime(int x, int y, int direction, long currentTime) {
        int cycleStartTime = cycleStart[x / 2][y / 2]; // Cycle start time for the intersection
        int northSouthGreen = greenTime[x / 2][y / 2]; // North-south green light duration
        int eastWestGreen = redTime[x / 2][y / 2];     // East-west green light duration
        int totalCycle = northSouthGreen + eastWestGreen; // Total cycle duration
        int elapsed = (int)((currentTime - cycleStartTime) % totalCycle + totalCycle) % totalCycle; // Elapsed time in the current cycle

        if (direction < 2) { // Moving in north-south direction
            if ((x ^ direction) % 2 == 1) {
                return currentTime + 2; // Moving along the edge takes 2 minutes
            } else if (elapsed < northSouthGreen) {
                return currentTime + 1; // Green light allows crossing
            } else {
                return currentTime + (totalCycle - elapsed) + 1; // Wait for the next green light
            }
        } else { // Moving in east-west direction
            if ((y ^ direction) % 2 == 1) {
                return currentTime + 2; // Moving along the edge takes 2 minutes
            } else if (elapsed >= northSouthGreen) {
                return currentTime + 1; // Green light allows crossing
            } else {
                return currentTime + (northSouthGreen - elapsed) + 1; // Wait for the next green light
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read grid size
        int rows = scanner.nextInt(); // Number of rows
        int cols = scanner.nextInt(); // Number of columns

        // Read traffic signal data for each intersection
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                greenTime[i][j] = scanner.nextInt(); // Green light time for north-south direction
                redTime[i][j] = scanner.nextInt();   // Green light time for east-west direction
                cycleStart[i][j] = scanner.nextInt(); // Start time of the cycle
            }
        }

        // Initialize minimum time array
        for (long[] row : minTime) Arrays.fill(row, -1);

        // Breadth-first search to find the shortest path
        Queue<Point> queue = new LinkedList<>();
        minTime[2 * rows - 1][0] = 0; // Start time at the southwest corner
        queue.add(new Point(2 * rows - 1, 0));

        while (!queue.isEmpty()) {
            Point current = queue.poll(); // Current point
            int x = current.x;
            int y = current.y;
            inQueue[x][y] = false; // Mark as not in queue

            // Explore all four directions
            for (int i = 0; i < 4; i++) {
                int nx = x + DX[i]; // New x-coordinate
                int ny = y + DY[i]; // New y-coordinate

                // Skip invalid positions
                if (nx < 0 || nx >= 2 * rows || ny < 0 || ny >= 2 * cols) {
                    continue;
                }

                long nextTime = calculateTime(x, y, i, minTime[x][y]); // Calculate time to new position
                if (minTime[nx][ny] == -1 || nextTime < minTime[nx][ny]) {
                    minTime[nx][ny] = nextTime; // Update minimum time
                    if (!inQueue[nx][ny]) {
                        inQueue[nx][ny] = true; // Mark as in queue
                        queue.add(new Point(nx, ny)); // Add to queue
                    }
                }
            }
        }

        // Output the shortest time to reach the northeast corner
        System.out.println(minTime[0][2 * cols - 1]);
        scanner.close();
    }
}
