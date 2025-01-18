import java.util.Scanner;

// The solution idea of this program is to determine whether each year in the input list is a leap year (a year divisible by 4 but not by 100, or divisible by 400).
// Then, it determines the correct tense (past, present, or future) based on the relationship between the year and the current year, 2024.
// Finally, it outputs a complete descriptive sentence in the format, such as "2024 is a leap year."
// The logic is straightforward and accurately handles all cases within the input range.
public class LeapYearChecker {

    // Method to determine if a year is a leap year
    public static boolean isLeapYear(int year) {
        // A year is a leap year if it is divisible by 4 and not divisible by 100,
        // unless it is also divisible by 400.
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0; // True if divisible by 400
            }
            return true; // True if divisible by 4 but not 100
        }
        return false; // Not a leap year
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a scanner to read user input
        int currentYear = 2024; // Define the current year
        int n = scanner.nextInt(); // Read the number of years to check

        // Iterate through the list of years
        for (int i = 0; i < n; i++) {
            int year = scanner.nextInt(); // Read the year to be checked
            String tense; // Variable to store the tense of the sentence

            // Determine the tense based on the relationship with the current year
            if (year < currentYear) {
                tense = "was"; // Past tense for years before the current year
            } else if (year == currentYear) {
                tense = "is"; // Present tense for the current year
            } else {
                tense = "will be"; // Future tense for years after the current year
            }

            // Determine if the year is a leap year or a common year
            String yearType = isLeapYear(year) ? "leap year" : "common year";

            // Print the result in the required format
            System.out.println(year + " " + tense + " a " + yearType + ".");
        }

        scanner.close(); // Close the scanner to release resources
    }
}
