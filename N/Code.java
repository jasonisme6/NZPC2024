import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.text.DecimalFormat;

// The goal of this problem is to determine the possible area where an object could be hidden based on a series of clues provided during the game "Hotter, Colder."
// The room is represented as a 10x10 square, and the player starts at position (0,0).
// For each new position the player visits, the game gives feedback—whether the player is getting "Hotter" (closer), "Colder" (farther), or "Same" (the same distance) relative to the hidden object.
// Based on this feedback, we iteratively narrow down the possible region where the object might be.
// This is done by adding constraints (represented as lines) to define valid regions using geometry.
// At each step, the intersecting region of all constraints is recalculated, representing the remaining valid area.
// If there is no valid region left, the area is zero.
// The solution involves calculating these intersections and finding the area of the remaining region while handling edge cases like invalid or conflicting constraints.
// The output is the area rounded to two decimal places for each step.
public class Main {
    static final double EPS = 1e-9; // Small constant for floating-point comparisons

    // Represents a point in 2D space
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }

    // Helper methods for vector operations
    static Point add(Point A, Point B) { return new Point(A.x + B.x, A.y + B.y); }
    static Point sub(Point A, Point B) { return new Point(A.x - B.x, A.y - B.y); }
    static Point mul(Point A, double p) { return new Point(A.x * p, A.y * p); }
    static Point div(Point A, double p) { return new Point(A.x / p, A.y / p); }
    static double cross(Point A, Point B) { return A.x * B.y - A.y * B.x; } // Cross product
    static Point normal(Point A) { return new Point(-A.y, A.x); } // Perpendicular vector

    // Represents a line in 2D space
    static class Line implements Comparable<Line> {
        Point P, v; // Point on the line and direction vector
        double ang; // Angle of the line (used for sorting)

        Line(Point P, Point v) {
            this.P = P;
            this.v = v;
            this.ang = Math.atan2(v.y, v.x);
        }

        // Compare lines based on their angles
        public int compareTo(Line o) {
            double d = this.ang - o.ang;
            if (Math.abs(d) < EPS) return 0;
            return d < 0 ? -1 : 1;
        }
    }

    // Check if a point is on the left side of a line
    static boolean onLeft(Line L, Point p) {
        return cross(L.v, sub(p, L.P)) >= -EPS;
    }

    // Calculate intersection point of two lines
    static Point getIntersection(Line a, Line b) {
        Point u = sub(a.P, b.P);
        double t = cross(b.v, u) / cross(a.v, b.v);
        return add(a.P, mul(a.v, t));
    }

    // Half-plane intersection algorithm to find the intersection of multiple half-planes
    static int halfPlaneIntersection(Line[] L, int n, Point[] poly) {
        Line[] temp = new Line[n];
        System.arraycopy(L, 0, temp, 0, n);
        Arrays.sort(temp);

        Line[] q = new Line[n];
        Point[] p = new Point[n];
        int first = 0, last = 0;
        q[0] = temp[0];

        for (int i = 1; i < n; i++) {
            while (first < last && !onLeft(temp[i], p[last - 1])) last--;
            while (first < last && !onLeft(temp[i], p[first])) first++;
            q[++last] = temp[i];

            // Check for parallel lines
            if (Math.abs(cross(q[last].v, q[last - 1].v)) < EPS) {
                last--;
                if (onLeft(q[last], temp[i].P)) q[last] = temp[i];
            }

            // Compute intersection points
            if (first < last) p[last - 1] = getIntersection(q[last - 1], q[last]);
        }

        while (first < last && !onLeft(q[first], p[last - 1])) last--;
        if (last - first <= 1) return 0; // No valid region

        p[last] = getIntersection(q[last], q[first]); // Close the polygon

        int m = 0;
        for (int i = first; i <= last; i++) poly[m++] = p[i];
        return m;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); // Number of positions

        Line[] L = new Line[1010]; // Array to store lines
        int lcnt = 0;

        // Define the initial bounding box (the room)
        Point[] corners = {
                new Point(0, 0),
                new Point(10, 0),
                new Point(10, 10),
                new Point(0, 10)
        };

        for (int i = 0; i < 4; i++) {
            Point P = corners[i];
            Point v = sub(corners[(i + 1) % 4], corners[i]);
            L[lcnt++] = new Line(P, v);
        }

        Point[] poly = new Point[1010]; // Array to store the resulting polygon
        for (int i = 0; i < 1010; i++) {
            poly[i] = new Point(0, 0);
        }

        DecimalFormat df = new DecimalFormat("0.00"); // Format for output
        Point pre = new Point(0, 0); // Previous position
        boolean flag = true; // Indicates if the region is valid

        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            if (line == null) break;
            st = new StringTokenizer(line);
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            String status = st.nextToken(); // "Hotter", "Colder", or "Same"

            if (!flag) {
                System.out.println("0.00");
                continue;
            }

            Point cur = new Point(x, y); // Current position
            if (status.charAt(0) == 'S') { // "Same" case
                System.out.println("0.00");
                flag = false;
            } else {
                Point mid = div(add(pre, cur), 2.0); // Midpoint of current and previous positions
                if (status.charAt(0) == 'H') { // "Hotter" case
                    Point dir = normal(sub(pre, cur));
                    L[lcnt++] = new Line(mid, dir);
                } else { // "Colder" case
                    Point dir = normal(sub(cur, pre));
                    L[lcnt++] = new Line(mid, dir);
                }

                int m = halfPlaneIntersection(L, lcnt, poly); // Calculate the intersecting region
                if (m == 0) {
                    System.out.println("0.00");
                    flag = false;
                } else {
                    double area = 0.0;
                    for (int j = 0; j < m; j++) {
                        area += cross(poly[j], poly[(j + 1) % m]) * 0.5; // Compute area of polygon
                    }
                    System.out.println(df.format(Math.abs(area))); // Output the area
                }
            }
            pre = cur; // Update the previous position
        }
    }
}
