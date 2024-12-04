import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * Uses the de Jager formula to find the values for a,b,c,d such that they are
 * powers of bases w, x, y, and z respectively and should be within the relative
 * error of μ.
 *
 * @author A.Narvel
 *
 */
public final class ABCDGuesser2 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private ABCDGuesser2() {
    }

    /**
     * Repeatedly asks the user for a positive real number until the user enters
     * one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number entered by the user
     */
    private static double getPositiveDouble(SimpleReader in, SimpleWriter out) {
        boolean validInput = false; // initializes a boolean for valid input
        double num = 0.0; // initializes num to 0

        // while validInput is false ask the user for a positive integer
        while (!validInput) {
            out.print("Enter a positive real number that μ should be approximated to: ");
            String input = in.nextLine();

            // if the integer is a integer convert to int
            if (FormatChecker.canParseInt(input)) {
                num = Integer.parseInt(input);

                // if num is positive validInput is true
                if (num > 0) {
                    validInput = true;
                } else {
                    out.println("Error: The number must be positive.");
                }
            } else {
                out.println("Error: Invalid input. Enter a positive real number.");

            }
        }
        return num;
    }

    /**
     * Repeatedly asks the user for a positive real number not equal to 1.0
     * until the user enters one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number not equal to 1.0 entered by the user
     */
    private static double getPositiveDoubleNotOne(SimpleReader in, SimpleWriter out) {
        boolean validInput = false; // initializes a boolean for valid input
        double num = 0.0; // initializes num to 0

        // while validInput is false ask the user for a positive integer
        while (!validInput) {
            out.print("Enter a positive real number not equal to 1.0: ");
            String input = in.nextLine();

            // if the integer is a integer convert to int
            if (FormatChecker.canParseInt(input)) {
                num = Integer.parseInt(input);

                // if num is positive & greater than 1.0, validInput is true
                if (num > 1.0) {
                    validInput = true;
                } else { // if not positive or greater than 1.0 output error msg
                    out.println(
                            "Error: The number must be positive and greater than 1.0.");
                }
            } else { // if not integer output error msg
                out.println("Error: Invalid input. Enter a positive real number "
                        + "not equal to 1.0:");

            }
        }
        return num;
    }

    /**
     * Method to calculate the best approximation of mean given w, x, y, and z
     * with the possible exponents a, b, c, d.
     *
     * @param w
     *            first personal number
     * @param x
     *            second personal number
     * @param y
     *            third personal number
     * @param z
     *            fourth personal number
     * @param mean
     *            constant to approximate
     * @param out
     *            the output stream
     */
    private static void findBestApprox(double mean, double w, double x, double y,
            double z, SimpleWriter out) {
        // array of possible exponents for a, b, c, d
        final double[] arrayExponent = { -5, -4, -3, -2, -1, -1.0 / 2, -1.0 / 3, -1.0 / 4,
                0, 1.0 / 4, 1.0 / 3, 1.0 / 2, 1, 2, 3, 4, 5 };

        double bestA = 0;
        double bestB = 0;
        double bestC = 0;
        double bestD = 0;

        // stores smallest relative error
        double bestApprox = 0;
        double minError = Double.MAX_VALUE;

        // for loops to find the values of exponents a,b,c,d
        for (int i = 0; i < arrayExponent.length; i++) {
            double a = arrayExponent[i];

            for (int j = 0; j < arrayExponent.length; j++) {
                double b = arrayExponent[j];

                for (int k = 0; k < arrayExponent.length; k++) {
                    double c = arrayExponent[k];

                    for (int l = 0; l < arrayExponent.length; l++) {
                        double d = arrayExponent[l];

                        //calculation for the approximation
                        double approx = Math.pow(w, a) * Math.pow(x, b) * Math.pow(y, c)
                                * Math.pow(z, d);

                        //calculation for the relative error
                        final int hundred = 100;
                        double relError = Math.abs(approx - mean) / mean * hundred;

                        // check if the combination of exponents has the best approx
                        if (relError < minError) {
                            minError = relError;
                            bestApprox = approx;
                            bestA = a;
                            bestB = b;
                            bestC = c;
                            bestD = d;
                        }

                    }
                }
            }
        }

        String exponents = String.format("a = %.2f, b = %.2f, c = %.2f, d = %.2f%n",
                bestA, bestB, bestC, bestD);
        String error = String.format("Relative error: %.2f%%%n", minError);

        out.println("µ: " + bestApprox);
        out.print(exponents);
        out.println(error);
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
         * Gets valid numbers from methods for the approximation of μ and each
         * of the four personal numbers w,x,y, and z.
         */
        double mean = getPositiveDouble(in, out);

        out.print("Enter the first personal number. ");
        double w = getPositiveDoubleNotOne(in, out);
        out.print("Enter the second personal number. ");
        double x = getPositiveDoubleNotOne(in, out);
        out.print("Enter the third personal number. ");
        double y = getPositiveDoubleNotOne(in, out);
        out.print("Enter the fourth personal number. ");
        double z = getPositiveDoubleNotOne(in, out);

        findBestApprox(mean, w, x, y, z, out);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
