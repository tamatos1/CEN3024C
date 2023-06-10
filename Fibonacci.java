/**
 * Implement the Fibonacci function in both a recursive and iterative fashion.
 * The sequence follows the rule that each number is equal to the sum of the preceding two numbers.
 */
public class Fibonacci {
    /**
     * This is the main method where the program starts.  
     *
     * @param args   optional string array.  Not used in the program
     */
    public static void main(String[] args) {
        long fibonacciNumber = 0;
        long startTime = 0;
        long stopTime = 0;
        long duration = 0;

        int n = 40;


        System.out.println("Fibonacci - Recursive");
        System.out.println("Iteration/Number/Duration");

        for (int i = 0; i <= n; i++) {
            startTime = System.nanoTime();
            fibonacciNumber = fibonacciRecursive(i);
            stopTime = System.nanoTime();
            duration = stopTime - startTime;
            System.out.println(i + "/" + fibonacciNumber + "/" + duration);
        }

        System.out.println();
        System.out.println("Fibonacci - Iterative");
        System.out.println("Iteration/Number/Duration");
        
        for (int i = 0; i <= n; i++) {
            startTime = System.nanoTime();
            fibonacciNumber = fibonacciIterative(i);
            stopTime = System.nanoTime();
            duration = stopTime - startTime;
            System.out.println(i + "/" + fibonacciNumber + "/" + duration);
        }
    }


    /**
     * Implements the Fibonacci function in recursive fashion.  
     * Allows the code to call itself to compute the output by using the same logic on a series of input to calculate the new number.
     *
     * @param number the integer that will be used as a starting point
     * @return       the fibonacci number
     */
    private static long fibonacciRecursive(int number) {
        if (number <= 1) {
            return number;
        } else {
            return fibonacciRecursive(number - 1) + fibonacciRecursive(number - 2);
        }
    }

    /**
     * Implements the Fibonacci function in iterative fashion.  
     * Loops through code until the current number from the main function has been reached.
     *
     * @param number the integer that will be used as a starting point
     * @return       the fibonacci number
     */
    private static long fibonacciIterative(int number) {
        if (number <= 1) {
            return number;
        }

        long fibonacciNumber = 1;
        long previousFibonacciNumber = 1;

        for (int i = 2; i < number; i++) {
            long temp = fibonacciNumber;
            fibonacciNumber += previousFibonacciNumber;
            previousFibonacciNumber = temp;
        }

        return fibonacciNumber;
    }
}