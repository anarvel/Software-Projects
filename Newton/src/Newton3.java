import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * The user is repeatedly prompted to enter a number until they choose to quit.
 * The user is asked to input a value for epsilon. The program uses Newton's
 * iteration to compute the square root of each number.
 *
 * @author A.Narvel
 *
 */
public final class Newton3 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton3() {
    }

    /**
     * Computes estimate of square root of user input x to within a relative
     * error of a user inputed epsilon value using newton's iteration.
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
         * asks the user whether they wish to calculate a square root. If they
         * respond with "y" the program proceeds and asks the user for the value
         * of epsilon and a number, if not then it quits.
         */
        String response = "y";

        out.print("Enter a number: ");
        int userNum = in.nextInteger();

        out.print("Enter the value of epsilon: ");
        double epsilonVal = in.nextDouble();

        out.println(sqrt(userNum, epsilonVal));

        out.println("Do you wish to calculate a square root?");
        response = in.nextLine();

        while (response.equals("y")) {
            out.print("Enter a number: ");
            userNum = in.nextInteger();

            out.println(sqrt(userNum, epsilonVal));

            out.println("Do you wish to calculate a square root?");
            response = in.nextLine();

        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
