import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * @author A.Narvel
 *
 */

public class StringReassemblyTest {

    /**
     * test for combination.
     */
    @Test
    public void testCombination() {
        String s1 = ("helloabc");
        String s2 = ("abcthere");
        int overlap = (3);
        String combinationExpected = ("helloabcthere");
        String combination = StringReassembly.combination(s1, s2, overlap);
        assertEquals(combinationExpected, combination);
    }

    /**
     * test 1 for addToSetAvoidingSubstrings.
     */
    @Test
    public void addToSetAvoidingSubstringsSame() {
        Set<String> s1 = new Set1L<>();
        s1.add("hello");
        s1.add("world");
        s1.add("hi");
        String s2 = ("hell");
        Set<String> s1Expected = new Set1L<>();
        s1Expected.add("hello");
        s1Expected.add("world");
        s1Expected.add("hi");
        StringReassembly.addToSetAvoidingSubstrings(s1, s2);
        assertEquals(s1Expected, s1);
    }

    /**
     * test 2 for addToSetAvoidingSubstrings.
     */
    @Test
    public void addToSetAvoidingSubstringsDiff() {
        Set<String> s1 = new Set1L<>();
        s1.add("hello");
        s1.add("world");
        s1.add("hi");
        String s2 = ("hellothere");
        Set<String> s1Expected = new Set1L<>();
        s1Expected.add("hellothere");
        s1Expected.add("world");
        s1Expected.add("hi");
        StringReassembly.addToSetAvoidingSubstrings(s1, s2);
        assertEquals(s1Expected, s1);
    }

    /**
     * test 1 for linesFromInput.
     */
    @Test
    public void linesFromInput() {
        SimpleReader input = new SimpleReader1L("data/linefrominput1.txt");
        Set<String> s1 = StringReassembly.linesFromInput(input);
        input.close();
        Set<String> s1Expected = new Set1L<>();
        s1Expected.add("Bucks -- Beat");
        s1Expected.add("Go Bucks");
        s1Expected.add("o Bucks -- B");
        s1Expected.add("Beat Mich");
        s1Expected.add("Michigan~");
        assertEquals(s1Expected, s1);
    }

    /**
     * test 2 for linesFromInput.
     */
    @Test
    public void linesFromInput2() {
        SimpleReader input = new SimpleReader1L("data/helloworld.txt");
        Set<String> s1 = StringReassembly.linesFromInput(input);
        input.close();
        Set<String> s1Expected = new Set1L<>();
        s1Expected.add("hello world");
        s1Expected.add("hi");
        assertEquals(s1Expected, s1);
    }

    /**
     * Returns a user entered file as a string.
     *
     * @param fileName
     *            user inputed file with content
     * @return file as a string
     */
    private static String fileContentAsString(String fileName) {
        SimpleReader fileReader = new SimpleReader1L(fileName);
        String fileAsString = "";
        while (!fileReader.atEOS()) {
            fileAsString += fileReader.nextLine();
        }
        fileReader.close();
        return fileAsString;
    }

    /**
     * test for printWithLineSeparators.
     */
    @Test
    public void printWithLineSeparators() {
        SimpleWriter out = new SimpleWriter1L(
                "data/printWithLineSeparators-test1-result.txt");
        String originalText = fileContentAsString(
                "data/printWithLineSeparators-test1-original.txt");
        StringReassembly.printWithLineSeparators(originalText, out);
        String expected = fileContentAsString(
                "data/printWithLineSeparators-test1-expected.txt");
        String result = fileContentAsString(
                "data/printWithLineSeparators-test1-result.txt");
        out.close();
        assertEquals(expected, result);

    }

}
