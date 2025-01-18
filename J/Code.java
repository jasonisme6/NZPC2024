import java.util.*;

// The problem revolves around evaluating mathematical expressions in a unique format called Snaggle.
// Each expression can either be a single number or a compound expression of the form (p e1 e2), where p is a probability between 0 and 1, and e1 and e2 are either numbers or nested expressions.
// To solve the problem, the goal is to compute the expected value of these expressions based on the formula given:
// with probability p, the result is the sum of e1 and e2, and with probability 1-p, it is their difference.
// The approach involves reading the input, parsing it piece by piece, and calculating the results by handling nested expressions from the innermost level outward.
// A stack is used to manage intermediate values as expressions are processed, ensuring that the calculations respect the nesting and order of operations.
// The output is the computed value for each expression, formatted to two decimal places, making it easy to understand and verify.
public class SnaggleExpression {

    // Method to calculate the expected value of a Snaggle expression (p e1 e2)
    public static double calculate(double e2, double e1, double p){
        // p * (e1 + e2) + (1 - p) * (e1 - e2) computes the expected value
        return p * (e1 + e2) + (1 - p) * (e1 - e2);
    }

    // Method to evaluate a Snaggle expression
    public static double evaluate(String input) {
        // Base case: if the input does not contain parentheses, it is a simple number
        if(!input.contains("(") && !input.contains(")")){
            return Double.parseDouble(input); // Parse and return the number directly
        }

        Stack<Double> values = new Stack<>(); // Stack to hold intermediate values
        String[] expressions = input.split("\\s+"); // Split input into tokens by spaces

        for (String expression : expressions) {
            // Skip opening parentheses
            if (expression.equals("(")){
                continue;
            }

            // Handle expressions that start with an opening parenthesis
            if (expression.charAt(0) == '(') {
                double value = Double.parseDouble(expression.replace("(", ""));
                values.push(value); // Push the value to the stack
                continue;
            }

            // Process closing parentheses
            if (expression.equals(")")){
                // Pop the last three values and calculate the result
                values.push(calculate(values.pop(), values.pop(), values.pop()));
                continue;
            }

            // Handle expressions containing multiple closing parentheses
            if (expression.contains(")")){
                int count = 0; // Count the number of closing parentheses
                for (int i = 0; i < expression.length(); i++) {
                    if (expression.charAt(i) == ')') {
                        count++;
                    }
                }

                double value = Double.parseDouble(expression.replace(")", "")); // Extract the number
                values.push(calculate(value, values.pop(), values.pop())); // Calculate the inner expression
                for (int i = 0; i < count - 1; i++) {
                    // Process remaining nested expressions
                    values.push(calculate(values.pop(), values.pop(), values.pop()));
                }
                continue;
            }

            // Handle simple numbers and push them to the stack
            double value = Double.parseDouble(expression);
            values.push(value);
        }

        // The result of the expression is the final value on the stack
        return values.pop();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> expressions = new ArrayList<>(); // List to store input expressions

        // Read input lines until a line containing "()" is encountered
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("()")) {
                break; // Stop processing if termination line is reached
            }
            expressions.add(line); // Add valid expressions to the list
        }

        // Evaluate and print the result for each expression
        for (String expr : expressions) {
            System.out.printf("%.2f\n", evaluate(expr)); // Format the result to 2 decimal places
        }

        scanner.close(); // Close the scanner
    }
}
