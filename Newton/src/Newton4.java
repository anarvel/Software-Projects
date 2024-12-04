import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * The user is repeatedly prompted to enter a number until they input a negative
 * number. The user is asked to input a value for epsilon. The program uses
 * Newton's iteration to compute the square root of each number.
 *
 * @
 *
 */
public final class Newton4 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton4() {
    }

    /**
     * Computes estimate of square root of user input x to within a relative
     * error of a user inputed epsilon value using newton's iteration.
     *
     *
     * @param x
     *            positive number to compute square root of
     * @param e
     *            epsilon value to compute the square root
     * @return estimate of square root
     */
    private static double sqrt(double x, double e) {
        // use a final constant variable to compare x to 0
        final double equalAmount = 0.000001;
        // create a variable for the result of the computation
        double result;

        // if x is 0 then the result of the iteration is 0
        if (Math.abs(x - 0) < equalAmount) {
            result = 0;
        } else { // if x is not 0 then enter the else statement
            // implement the equation by assigning variable r to user input x
            double r = x;
            // while statement that updates r until it converges to the sqrt of x
            while (Math.abs(r * r - x) / x >= e * e) {
                r = (r + x / r) / 2;
            }
            // assign the result variable to r
            result = r;
        }
        // return the result
        return result;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * asks the user for the value of epsilon and a number to compute the
         * sqrt of asks the user for a new value to compute while the value is
         * positive, if it is negative then the program quits
         */
        out.print("Enter the value of epsilon: ");
        double epsilonVal = in.nextDouble();

        out.print("Enter a number: ");
        int userNum = in.nextInteger();

        /**
         * while loop to compute the sqrt of the user inputed value until the
         * user enters a negative val
         */
        while (userNum >= 0) {

            out.println(sqrt(userNum, epsilonVal));

            out.print("Enter a new number: ");
            userNum = in.nextInteger();
        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
