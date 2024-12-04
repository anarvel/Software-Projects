import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;

/**
 * @author A.Narvel
 *
 */
public class GlossaryTest {

    /*
     * Tests of generateElements
     */

    // Routine Test
    @Test
    public void testGenerateElementsRoutine() {
        String input = " ,";
        Set<Character> actualSet = new Set1L<>();
        Set<Character> expectedSet = new Set1L<>();
        expectedSet.add(' ');
        expectedSet.add(',');

        Glossary2.generateElements(input, actualSet);

        assertEquals(expectedSet, actualSet);
    }

    // Boundary Test
    @Test
    public void testGenerateElementsBoundary() {
        String input = "";
        Set<Character> actualSet = new Set1L<>();
        Set<Character> expectedSet = new Set1L<>();

        Glossary2.generateElements(input, actualSet);

        assertEquals(expectedSet, actualSet);
    }

    /*
     * Tests of testNextWordOrSeparator_word
     */

    // Routine Test for a word
    @Test
    public void testNextWordOrSeparator_word() {
        String text = "convey, especially";
        int position = 0;
        Set<Character> separators = new Set1L<>();
        Glossary2.generateElements(" ,", separators);
        String expected = "convey";

        String actual = Glossary2.nextWordOrSeparator(text, position, separators);

        assertEquals(expected, actual);
    }

    // Routine Test for a separator
    @Test
    public void testNextWordOrSeparator_separator() {
        String text = "convey, especially";
        int position = 6;
        Set<Character> separators = new Set1L<>();
        Glossary2.generateElements(" ,", separators);
        String expected = ", ";

        String actual = Glossary2.nextWordOrSeparator(text, position, separators);

        assertEquals(expected, actual);
    }

    // Boundary Test
    @Test
    public void testNextWordOrSeparatorBoundary() {
        String text = " ";
        int position = 0;
        Set<Character> separators = new Set1L<>();
        Glossary2.generateElements(" ,", separators);
        String expected = " ";

        String actual = Glossary2.nextWordOrSeparator(text, position, separators);

        assertEquals(expected, actual);
    }

    /*
     * Tests of testAlphabetize
     */

    // Routine Test
    @Test
    public void testAlphabetize() {
        Queue<String> words = new Queue1L<>();
        Queue<String> definitions = new Queue1L<>();
        words.enqueue("meaning");
        definitions
                .enqueue("something that one wishes to convey, especially by language");
        words.enqueue("definition");
        definitions.enqueue("a sequence of words that gives meaning to a term");
        words.enqueue("book");
        definitions.enqueue("a printed or written literary work");

        Glossary2.alphabetize(words, definitions);

        assertEquals("book", words.dequeue());
        assertEquals("a printed or written literary work", definitions.dequeue());
        assertEquals("definition", words.dequeue());
        assertEquals("a sequence of words that gives meaning to a term",
                definitions.dequeue());
        assertEquals("meaning", words.dequeue());
        assertEquals("something that one wishes to convey, especially by language",
                definitions.dequeue());
    }

    // Boundary Test
    @Test
    public void testAlphabetizeBoundary() {
        Queue<String> words = new Queue1L<>();
        Queue<String> definitions = new Queue1L<>();

        Glossary2.alphabetize(words, definitions);

        assertEquals(0, words.length());
        assertEquals(0, definitions.length());
    }

    // Boundary Test
    @Test
    public void testAlphabetizeUnchanged() {
        Queue<String> words = new Queue1L<>();
        Queue<String> definitions = new Queue1L<>();
        words.enqueue("term");
        definitions.enqueue("a word in the glossary");
        words.enqueue("word");
        definitions.enqueue(
                "a string of characters in a language, which has at least one character");

        Glossary2.alphabetize(words, definitions);

        assertEquals("term", words.dequeue());
        assertEquals("a word in the glossary", definitions.dequeue());
        assertEquals("word", words.dequeue());
        assertEquals(
                "a string of characters in a language, which has at least one character",
                definitions.dequeue());
    }
}
