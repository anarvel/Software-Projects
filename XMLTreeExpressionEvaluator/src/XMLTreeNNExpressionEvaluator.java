import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author A.Narvel
 *
 */
public final class XMLTreeNNExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires * [exp is a subtree of a well-formed XML arithmetic expression]
     *           and [the label of the root of exp is not "expression"]
     *
     * @ensures evaluate = [the value of the expression]
     */
    private static NaturalNumber evaluate(XMLTree exp) {

        NaturalNumber num1 = new NaturalNumber2(0);

        // base case: if the node is a number initialize num1 with its value
        if (exp.label().equals("number")) {
            num1 = new NaturalNumber2(exp.attributeValue("value"));
        } else { // recursively evaluate the left child first then the right child of the tree
            NaturalNumber num2 = evaluate(exp.child(0));
            NaturalNumber num3 = evaluate(exp.child(1));

            // evaluate each subproblem and set return value equal to each part using NN methods
            if (exp.label().equals("plus")) {
                num2.add(num3);
                num1.copyFrom(num2);
            } else if (exp.label().equals("minus")) {
                if (num3.compareTo(num2) > 0) { // if num3 > num2 report error
                    Reporter.fatalErrorToConsole(
                            "Cannot subtract larger number from smaller one.");
                } else {
                    num2.subtract(num3);
                    num1.copyFrom(num2);
                }
            } else if (exp.label().equals("times")) {
                num2.multiply(num3);
                num1.copyFrom(num2);
            } else if (num3.isZero()) { // if num3 is zero report error
                Reporter.fatalErrorToConsole("Cannot divide by zero.");
            } else { // if(exp.label().equals("divide")
                num2.divide(num3);
                num1.copyFrom(num2);
            }

        }
        return num1; // return the result of the equation

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

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
