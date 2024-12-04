import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author A.Narvel
 *
 */
public final class XMLTreeIntExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeIntExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        int num1 = 0;

        // base case: if the node is a number initialize num1 with its value
        if (exp.label().equals("number")) {
            num1 = Integer.parseInt(exp.attributeValue("value"));
        } else { // recursively call/evaluate the left side first then the right side of the tree
            int num2 = evaluate(exp.child(0));
            int num3 = evaluate(exp.child(1));
            // evaluate each subproblem and set return value equal to each part
            if (exp.label().equals("plus")) {
                num1 = num2 + num3;
            } else if (exp.label().equals("minus")) {
                num1 = num2 - num3;
            } else if (exp.label().equals("times")) {
                num1 = num2 * num3;
            } else { // if(exp.label().equals("divide")
                num1 = num2 / num3;
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
