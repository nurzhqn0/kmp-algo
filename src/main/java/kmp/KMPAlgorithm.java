package kmp;

/**
 * KMP (Knuth-Morris-Pratt)
 * Time Complexity: O(n + m)
 * Space Complexity: O(m)
 */
public class KMPAlgorithm {
    // computes the Longest Proper Prefix
    public static int[] computeLPSArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];

        int length = 0;

        lps[0] = 0;

        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }
    public static int search(String text, String pattern) {
        if (text == null) {
            return -1;
        }
        if (pattern == null || pattern.isEmpty()) {
            return 0;
        }
        if (text.isEmpty()) {
            return -1;
        }
        if (pattern.length() > text.length()) {
            return -1;
        }

        int n = text.length();
        int m = pattern.length();

        int[] lps = computeLPSArray(pattern);

        int i = 0;
        int j = 0;

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                return i - j;
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return -1;
    }

    public static int[] searchAll(String text, String pattern) {
        if (pattern == null || pattern.isEmpty() || text == null || text.isEmpty()) {
            return new int[0];
        }
        if (pattern.length() > text.length()) {
            return new int[0];
        }

        int n = text.length();
        int m = pattern.length();

        int[] lps = computeLPSArray(pattern);

        java.util.ArrayList<Integer> matches = new java.util.ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        int[] result = new int[matches.size()];
        for (int k = 0; k < matches.size(); k++) {
            result[k] = matches.get(k);
        }

        return result;
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";

        System.out.println("Text: " + text);
        System.out.println("Pattern: " + pattern);

        int index = search(text, pattern);
        if (index != -1) {
            System.out.println("Pattern found at index: " + index);
        } else {
            System.out.println("Pattern not found");
        }

        int[] lps = computeLPSArray(pattern);
        System.out.print("LPS Array: [");
        for (int i = 0; i < lps.length; i++) {
            System.out.print(lps[i]);
            if (i < lps.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}