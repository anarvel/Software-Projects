import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * The user is repeatedly prompted to enter a number until they choose to quit.
 * The program uses Newton's iteration to compute the square root of each
 * number.
 *
 * @author A.Narvel
 *
 */
public final class Newton1 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton1() {
    }

    /**
     * Computes estimate of square root of x to within relative error 0.01%.
     *
     * @param x
     *            positive number to compute square root of
     * @return estimate of square root
     */
    private static double sqrt(double x) {
        // initialize epsilon, e, within a relative error of 0.01%
        final double e = 1E-4;
        // implement the equation by assigning variable r to user input x
        double r = x;
        // while statement that updates r until it converges to the sqrt of x
        while (Math.abs(r * r - x) / x >= e * e) {
            r = (r + x / r) / 2;
        }
        // return r
        return r;
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
         * asks the user if the whether they wish to calculate a square root. If
         * they respond with "y" the program proceeds and asks the user for a
         * number, if not then it quits.
         */
        String response = "y";

        // the user needs to input anything other than y to stop the program
        while (response.equals("y")) {
            out.print("Enter a number: ");
            int userNum = in.nextInteger();

            out.println(sqrt(userNum));

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
