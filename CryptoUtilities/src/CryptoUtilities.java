import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.random.Random;
import components.random.Random1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Utilities that could be used with RSA cryptosystems.
 *
 * @author A.Narvel
 *
 */
public final class CryptoUtilities {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private CryptoUtilities() {
    }

    /**
     * Useful constant, not a magic number: 3.
     */
    private static final int THREE = 3;

    /**
     * Pseudo-random number generator.
     */
    private static final Random GENERATOR = new Random1L();

    /**
     * Returns a random number uniformly distributed in the interval [0, n].
     *
     * @param n
     *            top end of interval
     * @return random number in interval
     * @requires n > 0
     * @ensures <pre>
     * randomNumber = [a random number uniformly distributed in [0, n]]
     * </pre>
     */
    public static NaturalNumber randomNumber(NaturalNumber n) {
        assert !n.isZero() : "Violation of: n > 0";
        final int base = 10;
        NaturalNumber result;
        int d = n.divideBy10();
        if (n.isZero()) {
            /*
             * Incoming n has only one digit and it is d, so generate a random
             * number uniformly distributed in [0, d]
             */
            int x = (int) ((d + 1) * GENERATOR.nextDouble());
            result = new NaturalNumber2(x);
            n.multiplyBy10(d);
        } else {
            /*
             * Incoming n has more than one digit, so generate a random number
             * (NaturalNumber) uniformly distributed in [0, n], and another
             * (int) uniformly distributed in [0, 9] (i.e., a random digit)
             */
            result = randomNumber(n);
            int lastDigit = (int) (base * GENERATOR.nextDouble());
            result.multiplyBy10(lastDigit);
            n.multiplyBy10(d);
            if (result.compareTo(n) > 0) {
                /*
                 * In this case, we need to try again because generated number
                 * is greater than n; the recursive call's argument is not
                 * "smaller" than the incoming value of n, but this recursive
                 * call has no more than a 90% chance of being made (and for
                 * large n, far less than that), so the probability of
                 * termination is 1
                 */
                result = randomNumber(n);
            }
        }
        return result;
    }

    /**
     * Finds the greatest common divisor of n and m.
     *
     * @param n
     *            one number
     * @param m
     *            the other number
     * @updates n
     * @clears m
     * @ensures n = [greatest common divisor of #n and #m]
     */
    public static void reduceToGCD(NaturalNumber n, NaturalNumber m) {

        /*
         * Use Euclid's algorithm; in pseudocode: if m = 0 then GCD(n, m) = n
         * else GCD(n, m) = GCD(m, n mod m)
         */

        if (!m.isZero()) { // recursive method until m is zero
            NaturalNumber mod = new NaturalNumber2(n.divide(m)); // mod = remainder
            reduceToGCD(m, mod); // recursive call
            n.transferFrom(m); // n is now m and m is zero
        }

    }

    /**
     * Reports whether n is even.
     *
     * @param n
     *            the number to be checked
     * @return true iff n is even
     * @ensures isEven = (n mod 2 = 0)
     */
    public static boolean isEven(NaturalNumber n) {
        boolean result = false;
        NaturalNumber temp = new NaturalNumber2(n); // temp NN for n to modify
        NaturalNumber two = new NaturalNumber2(2);
        if (temp.divide(two).isZero()) { // if the remainder is Zero then even
            result = true;
        }
        return result;
    }

    /**
     * Updates n to its p-th power modulo m.
     *
     * @param n
     *            number to be raised to a power
     * @param p
     *            the power
     * @param m
     *            the modulus
     * @updates n
     * @requires m > 1
     * @ensures n = #n ^ (p) mod m
     */
    public static void powerMod(NaturalNumber n, NaturalNumber p, NaturalNumber m) {
        assert m.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: m > 1";

        /*
         * Use the fast-powering algorithm as previously discussed in class,
         * with the additional feature that every multiplication is followed
         * immediately by "reducing the result modulo m"
         */

        // initialize constants
        NaturalNumber two = new NaturalNumber2(2);
        NaturalNumber one = new NaturalNumber2(1);
        // base case: if p is zero
        if (p.isZero()) {
            n.copyFrom(one); // set n to one
        } else {

            if (isEven(p)) { // if p is even
                NaturalNumber remainder = p.divide(two); // find the remainder

                powerMod(n, p, m); // recursive call 

                p.multiply(two); // restore p
                p.add(remainder); // add remainder

                NaturalNumber doubleN = n.newInstance(); // new instance to square n
                doubleN.add(n);
                n.multiply(doubleN); // square n

                NaturalNumber remainderN = n.divide(m); // remainder of n/m
                n.copyFrom(remainderN); // set n to remainder

            } else { // is p is odd
                NaturalNumber copyN = n.newInstance(); // copy of n
                copyN.add(n); // set n to the copy of n

                p.divide(two); // divide p by 2
                powerMod(n, p, m); // recursive call

                p.multiply(two); // restore p
                p.increment(); // increment to make odd again

                NaturalNumber doubleN = n.newInstance(); // new instance to square n
                doubleN.add(n);
                n.multiply(doubleN); // square n

                NaturalNumber remainder = n.divide(m); // remainder of n / m
                n.copyFrom(remainder); // set n to remainder

                n.multiply(copyN.divide(m)); // divide copyN by m and multiply to result
                NaturalNumber result = n.divide(m); // result = n / m
                n.copyFrom(result); // set n to result
            }

        }
    }

    /**
     * Reports whether w is a "witness" that n is composite, in the sense that
     * either it is a square root of 1 (mod n), or it fails to satisfy the
     * criterion for primality from Fermat's theorem.
     *
     * @param w
     *            witness candidate
     * @param n
     *            number being checked
     * @return true iff w is a "witness" that n is composite
     * @requires n > 2 and 1 < w < n - 1
     * @ensures <pre>
     * isWitnessToCompositeness =
     *     (w ^ 2 mod n = 1)  or  (w ^ (n-1) mod n /= 1)
     * </pre>
     */
    public static boolean isWitnessToCompositeness(NaturalNumber w, NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(2)) > 0 : "Violation of: n > 2";
        assert (new NaturalNumber2(1)).compareTo(w) < 0 : "Violation of: 1 < w";
        n.decrement();
        assert w.compareTo(n) < 0 : "Violation of: w < n - 1";
        n.increment();

        boolean result = false; // initialize to false
        // new instance of n and copy n to it
        NaturalNumber newN = n.newInstance();
        newN.copyFrom(n);

        // new instance of w and copy w to it
        NaturalNumber newW = w.newInstance();
        newW.copyFrom(w);

        // Initialize constants
        NaturalNumber power = new NaturalNumber2(2);
        NaturalNumber one = new NaturalNumber2(1);

        // call powerMod to compute newW^2 mod newN & store in newW
        powerMod(newW, power, newN);
        // if result is equal to 1 set result to true
        if (newW.compareTo(one) == 0) {
            result = true;
        }

        // reset to og values
        newN.copyFrom(n);
        newW.copyFrom(w);

        // set power to newN - 1 to compute newW^(n - 1) mod newN
        power.copyFrom(newN);
        power.decrement();

        // call powerMod to compute newW^(n-1) mod newN & store in newW
        powerMod(newW, power, newN);
        // if result is not equal to 1 set result to true
        if (newW.compareTo(one) != 0) {
            result = true;
        }

        return result; // return result
    }

    /**
     * Reports whether n is a prime; may be wrong with "low" probability.
     *
     * @param n
     *            number to be checked
     * @return true means n is very likely prime; false means n is definitely
     *         composite
     * @requires n > 1
     * @ensures <pre>
     * isPrime1 = [n is a prime number, with small probability of error
     *         if it is reported to be prime, and no chance of error if it is
     *         reported to be composite]
     * </pre>
     */
    public static boolean isPrime1(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";
        boolean isPrime;
        if (n.compareTo(new NaturalNumber2(THREE)) <= 0) {
            /*
             * 2 and 3 are primes
             */
            isPrime = true;
        } else if (isEven(n)) {
            /*
             * evens are composite
             */
            isPrime = false;
        } else {
            /*
             * odd n >= 5: simply check whether 2 is a witness that n is
             * composite (which works surprisingly well :-)
             */
            isPrime = !isWitnessToCompositeness(new NaturalNumber2(2), n);
        }
        return isPrime;
    }

    /**
     * Reports whether n is a prime; may be wrong with "low" probability.
     *
     * @param n
     *            number to be checked
     * @return true means n is very likely prime; false means n is definitely
     *         composite
     * @requires n > 1
     * @ensures <pre>
     * isPrime2 = [n is a prime number, with small probability of error
     *         if it is reported to be prime, and no chance of error if it is
     *         reported to be composite]
     * </pre>
     */
    public static boolean isPrime2(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";

        /*
         * Use the ability to generate random numbers (provided by the
         * randomNumber method above) to generate several witness candidates --
         * say, 10 to 50 candidates -- guessing that n is prime only if none of
         * these candidates is a witness to n being composite (based on fact #3
         * as described in the project description); use the code for isPrime1
         * as a guide for how to do this, and pay attention to the requires
         * clause of isWitnessToCompositeness
         */

        boolean result = true; // initialize to true
        for (int i = 0; i < 50; i++) { // for loop 50 times
            // if no witness found yet
            if (result) {
                NaturalNumber one = new NaturalNumber2(1);

                NaturalNumber temp = new NaturalNumber2(n); // create temp of n
                temp.decrement(); // decrement temp n

                NaturalNumber w = randomNumber(n);
                // while w =< 1 keep incremementing till w > 1
                while (w.compareTo(one) <= 0) {
                    w.increment();
                }
                // while w >= n - 1 keep decrementing till w <= n - 1
                while (w.compareTo(temp) >= 0) {
                    w.decrement();
                }
                // check if w is a witness to the compositeness of n
                // if witness is true then result is false
                result = !isWitnessToCompositeness(w, n);
            }
        }
        return result; // return result
    }

    /**
     * Generates a likely prime number at least as large as some given number.
     *
     * @param n
     *            minimum value of likely prime
     * @updates n
     * @requires n > 1
     * @ensures n >= #n and [n is very likely a prime number]
     */
    public static void generateNextLikelyPrime(NaturalNumber n) {
        assert n.compareTo(new NaturalNumber2(1)) > 0 : "Violation of: n > 1";

        /*
         * Use isPrime2 to check numbers, starting at n and increasing through
         * the odd numbers only (why?), until n is likely prime
         */
        NaturalNumber two = new NaturalNumber2(2); // initialize constant

        // is n is not prime
        if (!isPrime2(n)) { // call isPrime2 to check if n is prime
            if (isEven(n)) { // check if n is even if it is then increment to make odd
                n.increment();
            } else { // if n is odd & not prime then add 2 to skip to next odd num
                n.add(two);
            }

            while (!isPrime2(n)) { // while loop until a prime is found
                n.add(two); // n + 2 to keep checking only odd numbers
            }
        }
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
         * Sanity check of randomNumber method -- just so everyone can see how
         * it might be "tested"
         */
        final int testValue = 17;
        final int testSamples = 100000;
        NaturalNumber test = new NaturalNumber2(testValue);
        int[] count = new int[testValue + 1];
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
        }
        for (int i = 0; i < testSamples; i++) {
            NaturalNumber rn = randomNumber(test);
            assert rn.compareTo(test) <= 0 : "Help!";
            count[rn.toInt()]++;
        }
        for (int i = 0; i < count.length; i++) {
            out.println("count[" + i + "] = " + count[i]);
        }
        out.println(
                "  expected value = " + (double) testSamples / (double) (testValue + 1));

        /*
         * Check user-supplied numbers for primality, and if a number is not
         * prime, find the next likely prime after it
         */
        while (true) {
            out.print("n = ");
            NaturalNumber n = new NaturalNumber2(in.nextLine());
            if (n.compareTo(new NaturalNumber2(2)) < 0) {
                out.println("Bye!");
                break;
            } else {
                if (isPrime1(n)) {
                    out.println(n + " is probably a prime number"
                            + " according to isPrime1.");
                } else {
                    out.println(n + " is a composite number" + " according to isPrime1.");
                }
                if (isPrime2(n)) {
                    out.println(n + " is probably a prime number"
                            + " according to isPrime2.");
                } else {
                    out.println(n + " is a composite number" + " according to isPrime2.");
                    generateNextLikelyPrime(n);
                    out.println("  next likely prime is " + n);
                }
            }
        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
