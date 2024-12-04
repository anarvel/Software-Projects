import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program to make a glossary by creating an HTML page for each word and its
 * definition, with an index linking to them.
 *
 * @author A.Narvel
 */
public final class Glossary2 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Glossary2() {

    }

    /**
     * Outputs the header for the index HTML page.
     *
     * @param out
     *            the SimpleWriter to write to
     */
    private static void outputHeader(SimpleWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<body>");
        out.println("<h2>" + "Sample Glossary" + "</h2>");
        out.println("<hr/>");
        out.println("<h3>" + "Index" + "</h3>");
        out.println("<ul>");
    }

    /**
     * Outputs the footer for the index HTML page.
     *
     * @param out
     *            the SimpleWriter to write to
     */
    private static void outputFooter(SimpleWriter out) {
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";
        charSet.clear();

        // loop for each element to add to Set if it isn't already in it
        for (int i = 0; i < str.length(); i++) {
            if (!charSet.contains(str.charAt(i))) {
                charSet.add(str.charAt(i));
            }
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        // if current character is separator or not
        boolean result = separators.contains(text.charAt(position));
        int endPosition = position;

        // iterate through the text until the end and a diff type of character is found
        while (endPosition < text.length()
                && separators.contains(text.charAt((endPosition))) == result) {
            endPosition++;
        }

        // return substring of first word or separator in text
        return text.substring(position, endPosition);
    }

    /**
     * Outputs a page for a single word and its definition. Hyperlink any terms
     * in the definitions to their respective pages.
     *
     * @param word
     *            the word
     * @param definition
     *            the definition
     * @param out
     *            the SimpleWriter to write to
     * @param words
     *            the set of words if found in the definition
     */
    private static void outputPage(String word, String definition, SimpleWriter out,
            Queue<String> words) {

//        DEBUGGING (COMMENT OUT):
//        SimpleWriter console = new SimpleWriter1L();
//        console.println(words);
//        console.close();

        out.println("<html>");
        out.println("<body>");
        out.println("<h2 style=\"color:Tomato;\"><i>" + word + "</i></h2>");

        // Define separators to populate the set with
        Set<Character> separators = new Set1L<Character>();
        String character = " ,";
        generateElements(character, separators);

        // Build new definitions with links to terms in index
        StringBuilder newDef = new StringBuilder();
        int position = 0;

        // iterate through the definition and get the next word or separator
        while (position < definition.length()) {
            String wordOrSeparator = nextWordOrSeparator(definition, position,
                    separators);
            position += wordOrSeparator.length();

            // if the word is in the set of index words then hyperlink it to its html page
            for (String indexWord : words) {
                if (wordOrSeparator.equals(indexWord)) {
                    wordOrSeparator = "<a href=\"" + indexWord + ".html\">" + indexWord
                            + "</a>";
                }
            }
            //append the word or separator to the new definition
            newDef.append(wordOrSeparator);
        }

        out.println("<p>" + newDef.toString() + "</p>");
        out.println("<hr/>");

        out.print("<p>");
        // return to index link
        out.print("Return to " + "<a href=\"index.html\">index</a>");
        out.println("</p>");
        out.println("</html>");
        out.println("</body>");

    }

    /**
     * Sorts the terms in the given queue in alphabetical order.
     *
     * @param words
     *            the queue of terms to sort
     * @param definitions
     *            the queue of definitions that much match the positions of *
     *            their terms
     *
     * @updates terms
     */
    public static void alphabetize(Queue<String> words, Queue<String> definitions) {
        int length = words.length();

        // Create temporary arrays to hold terms and definitions
        String[] wordsArray = new String[length];
        String[] definitionsArray = new String[length];

        // Transfer all terms and definitions into the arrays
        for (int i = 0; i < length; i++) {
            wordsArray[i] = words.dequeue();
            definitionsArray[i] = definitions.dequeue();
        }

        // Sort the arrays alphabetically by terms
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                if (wordsArray[i].compareTo(wordsArray[j]) > 0) {
                    // Swap terms
                    String tempWord = wordsArray[i];
                    wordsArray[i] = wordsArray[j];
                    wordsArray[j] = tempWord;

                    // Swap corresponding definitions
                    String tempDef = definitionsArray[i];
                    definitionsArray[i] = definitionsArray[j];
                    definitionsArray[j] = tempDef;
                }
            }
        }

        // Transfer the sorted terms and definitions back into the queues
        for (int i = 0; i < length; i++) {
            words.enqueue(wordsArray[i]);
            definitions.enqueue(definitionsArray[i]);
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.println("Enter name of the input file: ");
        String inputFile = in.nextLine();

        out.println("Enter the name of an output folder: ");
        String outputFolder = in.nextLine();

        if (!outputFolder.endsWith("/")) {
            outputFolder += "/";
        }

        String outputFile = outputFolder + "index.html";

        SimpleReader fileReader = new SimpleReader1L(inputFile);
        SimpleWriter fileOut = new SimpleWriter1L(outputFile);

        // output header
        outputHeader(fileOut);

        // create empty Queues for the words and definitions
        Queue<String> words = new Queue1L<>();
        Queue<String> defs = new Queue1L<>();

        // Read terms and definitions and add to queue
        while (!fileReader.atEOS()) {
            String term = fileReader.nextLine().trim();
            String definition = fileReader.nextLine().trim();

            if (!fileReader.atEOS()) {
                fileReader.nextLine(); // Skip blank line
            }

            // add terms and definitions to queues
            words.enqueue(term);
            defs.enqueue(definition);
        }

        // Sort terms and definitions
        alphabetize(words, defs);

        Queue<String> wordsCopy = new Queue1L<>(); // queue copy for the words
        Queue<String> wordsForOutputPage = words.newInstance(); // queue for output

        for (int i = 0; i < words.length(); i++) {
            String currentWord = words.dequeue();
            words.enqueue(currentWord);
            wordsForOutputPage.enqueue(currentWord);
        }

        // iterate over the words and create html files for each
        while (words.length() > 0) {
            String word = words.dequeue();
            String definition = defs.dequeue();

            wordsCopy.enqueue(word);

            String wordFile = outputFolder + word + ".html"; // create file for terms
            SimpleWriter wordWriter = new SimpleWriter1L(wordFile);
            outputPage(word, definition, wordWriter, wordsForOutputPage); // output page

            wordWriter.close();

            // add terms to index with html link
            fileOut.println("<li><a href=\"" + word + ".html\">" + word + "</a></li>");
        }

        outputFooter(fileOut); // output footer

        fileReader.close();
        fileOut.close();
        in.close();
        out.close();
    }
}
