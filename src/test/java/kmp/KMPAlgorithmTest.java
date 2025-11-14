package kmp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 test suite for KMP Algorithm
 * Tests various scenarios including edge cases, normal cases, and performance
 *
 * @author Nuraly
 */
public class KMPAlgorithmTest {

    @BeforeEach
    public void setUp() {
        // Setup if needed
    }

    // ========== Basic Functionality Tests ==========

    @Test
    @DisplayName("Test simple pattern match")
    public void testSimpleMatch() {
        String text = "HELLO WORLD";
        String pattern = "WORLD";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(6, result, "Pattern should be found at index 6");
    }

    @Test
    @DisplayName("Test pattern at beginning")
    public void testPatternAtBeginning() {
        String text = "ABCDEFGH";
        String pattern = "ABC";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Pattern at beginning should return index 0");
    }

    @Test
    @DisplayName("Test pattern at end")
    public void testPatternAtEnd() {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(10, result, "Pattern should be found at index 10");
    }

    @Test
    @DisplayName("Test pattern not found")
    public void testPatternNotFound() {
        String text = "ABCDEFGH";
        String pattern = "XYZ";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Pattern not present should return -1");
    }

    @Test
    @DisplayName("Test exact match (pattern equals text)")
    public void testExactMatch() {
        String text = "ABCD";
        String pattern = "ABCD";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Exact match should return index 0");
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Test empty pattern")
    public void testEmptyPattern() {
        String text = "HELLO";
        String pattern = "";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Empty pattern should match at index 0");
    }

    @Test
    @DisplayName("Test empty text")
    public void testEmptyText() {
        String text = "";
        String pattern = "ABC";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Empty text should return -1 for non-empty pattern");
    }

    @Test
    @DisplayName("Test both empty")
    public void testBothEmpty() {
        String text = "";
        String pattern = "";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Both empty should return 0");
    }

    @Test
    @DisplayName("Test pattern longer than text")
    public void testPatternLongerThanText() {
        String text = "ABC";
        String pattern = "ABCDEF";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Pattern longer than text should return -1");
    }

    @Test
    @DisplayName("Test single character match")
    public void testSingleCharacterMatch() {
        String text = "ABCDEF";
        String pattern = "C";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(2, result, "Single character should be found");
    }

    @Test
    @DisplayName("Test single character not found")
    public void testSingleCharacterNotFound() {
        String text = "ABCDEF";
        String pattern = "Z";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Single character not present should return -1");
    }

    // ========== Repeating Characters Tests ==========

    @Test
    @DisplayName("Test repeating pattern - worst case")
    public void testRepeatingPatternWorstCase() {
        String text = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB";
        String pattern = "AAAAB";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(35, result, "Should handle worst case with many false starts");
    }

    @Test
    @DisplayName("Test repeating pattern with partial matches")
    public void testRepeatingWithPartialMatches() {
        String text = "AABAACAADAABAABA";
        String pattern = "AABA";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Should find pattern with repeating characters");
    }

    @Test
    @DisplayName("Test all same characters")
    public void testAllSameCharacters() {
        String text = "AAAAAAAA";
        String pattern = "AAA";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Should find pattern in all same characters");
    }

    // ========== LPS Array Tests ==========

    @Test
    @DisplayName("Test LPS array computation - simple")
    public void testLPSArraySimple() {
        String pattern = "ABABCABAB";
        int[] expected = {0, 0, 1, 2, 0, 1, 2, 3, 4};
        int[] result = KMPAlgorithm.computeLPSArray(pattern);
        assertArrayEquals(expected, result, "LPS array should be computed correctly");
    }

    @Test
    @DisplayName("Test LPS array - no prefix suffix match")
    public void testLPSArrayNoMatch() {
        String pattern = "ABCDEF";
        int[] expected = {0, 0, 0, 0, 0, 0};
        int[] result = KMPAlgorithm.computeLPSArray(pattern);
        assertArrayEquals(expected, result, "LPS array with no matches should be all zeros");
    }

    @Test
    @DisplayName("Test LPS array - all same characters")
    public void testLPSArrayAllSame() {
        String pattern = "AAAAA";
        int[] expected = {0, 1, 2, 3, 4};
        int[] result = KMPAlgorithm.computeLPSArray(pattern);
        assertArrayEquals(expected, result, "LPS array for repeated characters");
    }

    @Test
    @DisplayName("Test LPS array - single character")
    public void testLPSArraySingleChar() {
        String pattern = "A";
        int[] expected = {0};
        int[] result = KMPAlgorithm.computeLPSArray(pattern);
        assertArrayEquals(expected, result, "LPS array for single character should be [0]");
    }

    // ========== Case Sensitivity Tests ==========

    @Test
    @DisplayName("Test case sensitivity")
    public void testCaseSensitivity() {
        String text = "Hello World";
        String pattern = "world";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Search should be case sensitive");
    }

    @Test
    @DisplayName("Test case sensitive match")
    public void testCaseSensitiveMatch() {
        String text = "Hello World";
        String pattern = "World";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(6, result, "Should match with correct case");
    }

    // ========== Special Characters Tests ==========

    @Test
    @DisplayName("Test with spaces")
    public void testWithSpaces() {
        String text = "The quick brown fox";
        String pattern = "quick brown";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(4, result, "Should handle spaces correctly");
    }

    @Test
    @DisplayName("Test with special characters")
    public void testWithSpecialCharacters() {
        String text = "user@example.com";
        String pattern = "@example";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(4, result, "Should handle special characters");
    }

    @Test
    @DisplayName("Test with numbers")
    public void testWithNumbers() {
        String text = "abc123def456";
        String pattern = "123";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(3, result, "Should handle numeric characters");
    }

    // ========== Multiple Occurrences Tests ==========

    @Test
    @DisplayName("Test search all occurrences")
    public void testSearchAllOccurrences() {
        String text = "ABABABABAB";
        String pattern = "ABA";
        int[] result = KMPAlgorithm.searchAll(text, pattern);
        int[] expected = {0, 2, 4, 6};
        assertArrayEquals(expected, result, "Should find all occurrences");
    }

    @Test
    @DisplayName("Test search all - single occurrence")
    public void testSearchAllSingleOccurrence() {
        String text = "ABCDEFGH";
        String pattern = "DEF";
        int[] result = KMPAlgorithm.searchAll(text, pattern);
        int[] expected = {3};
        assertArrayEquals(expected, result, "Should find single occurrence");
    }

    @Test
    @DisplayName("Test search all - no occurrences")
    public void testSearchAllNoOccurrences() {
        String text = "ABCDEFGH";
        String pattern = "XYZ";
        int[] result = KMPAlgorithm.searchAll(text, pattern);
        int[] expected = {};
        assertArrayEquals(expected, result, "Should return empty array for no matches");
    }

    // ========== Performance and Long String Tests ==========

    @Test
    @DisplayName("Test long text performance")
    public void testLongTextPerformance() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("ABCD");
        }
        sb.append("PATTERN");
        String text = sb.toString();
        String pattern = "PATTERN";

        long startTime = System.nanoTime();
        int result = KMPAlgorithm.search(text, pattern);
        long endTime = System.nanoTime();

        assertEquals(40000, result, "Should find pattern in long text");
        assertTrue((endTime - startTime) < 10_000_000, "Should complete in reasonable time (<10ms)");
    }

    @Test
    @DisplayName("Test very long pattern")
    public void testVeryLongPattern() {
        String text = "A".repeat(1000) + "B".repeat(100) + "C".repeat(1000);
        String pattern = "B".repeat(100);
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(1000, result, "Should handle very long patterns");
    }

    // ========== Real-world Scenarios ==========

    @Test
    @DisplayName("Test real-world sentence")
    public void testRealWorldSentence() {
        String text = "The quick brown fox jumps over the lazy dog";
        String pattern = "jumps";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(20, result, "Should find word in sentence");
    }

    @Test
    @DisplayName("Test Lorem Ipsum")
    public void testLoremIpsum() {
        String text = "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt";
        String pattern = "tempor";
        int result = KMPAlgorithm.search(text, pattern);
        assertTrue(result >= 0, "Should find pattern in Lorem Ipsum text");
    }

    @Test
    @DisplayName("Test technical text")
    public void testTechnicalText() {
        String text = "In computer science, string matching algorithms are essential for text processing";
        String pattern = "algorithms";
        int result = KMPAlgorithm.search(text, pattern);
        assertTrue(result >= 0, "Should find technical term");
    }

    // ========== Null Safety Tests ==========

    @Test
    @DisplayName("Test null text")
    public void testNullText() {
        String text = null;
        String pattern = "ABC";
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Null text should return -1");
    }

    @Test
    @DisplayName("Test null pattern")
    public void testNullPattern() {
        String text = "HELLO";
        String pattern = null;
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(0, result, "Null pattern should be treated as empty");
    }

    @Test
    @DisplayName("Test both null")
    public void testBothNull() {
        String text = null;
        String pattern = null;
        int result = KMPAlgorithm.search(text, pattern);
        assertEquals(-1, result, "Both null should return -1");
    }
}