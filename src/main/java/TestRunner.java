import kmp.KMPAlgorithm;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestRunner {

    private static final String INPUT_DIR = "data/input/";
    private static final String OUTPUT_DIR = "data/output/";

    private static class TestInput {
        String testId;
        String text;
        String pattern;
        String description;

        public TestInput(String json) {
            this.testId = extractValue(json, "test_id");
            this.text = extractValue(json, "text");
            this.pattern = extractValue(json, "pattern");
            this.description = extractValue(json, "description");
        }

        private String extractValue(String json, String key) {
            String searchKey = "\"" + key + "\"";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) return "";

            int colonIndex = json.indexOf(":", keyIndex);
            int startQuote = json.indexOf("\"", colonIndex);
            int endQuote = json.indexOf("\"", startQuote + 1);

            return json.substring(startQuote + 1, endQuote);
        }
    }

    private static String formatOutputJSON(TestInput input, int index, int[] lps, long executionTime) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"test_id\": \"").append(input.testId).append("\",\n");
        json.append("  \"text\": \"").append(escapeJSON(input.text)).append("\",\n");
        json.append("  \"pattern\": \"").append(escapeJSON(input.pattern)).append("\",\n");
        json.append("  \"found\": ").append(index != -1).append(",\n");
        json.append("  \"index\": ").append(index).append(",\n");
        json.append("  \"execution_time_ms\": ").append(executionTime).append(",\n");
        json.append("  \"text_length\": ").append(input.text.length()).append(",\n");
        json.append("  \"pattern_length\": ").append(input.pattern.length()).append(",\n");
        json.append("  \"lps_array\": [");

        for (int i = 0; i < lps.length; i++) {
            json.append(lps[i]);
            if (i < lps.length - 1) {
                json.append(", ");
            }
        }

        json.append("],\n");
        json.append("  \"description\": \"").append(escapeJSON(input.description)).append("\"\n");
        json.append("}\n");

        return json.toString();
    }

    private static String escapeJSON(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void processTestCase(File inputFile) {
        try {
            String jsonContent = new String(Files.readAllBytes(inputFile.toPath()));
            TestInput input = new TestInput(jsonContent);

            System.out.println("\n" + "=".repeat(80));
            System.out.println("Processing: " + inputFile.getName());
            System.out.println("Test ID: " + input.testId);
            System.out.println("Description: " + input.description);
            System.out.println("-".repeat(80));

            long startTime = System.nanoTime();
            int index = KMPAlgorithm.search(input.text, input.pattern);
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;

            int[] lps = KMPAlgorithm.computeLPSArray(input.pattern);

            System.out.println("Text: \"" + input.text + "\"");
            System.out.println("Pattern: \"" + input.pattern + "\"");
            System.out.println("Result: " + (index != -1 ? "Found at index " + index : "Not found"));
            System.out.print("LPS Array: [");
            for (int i = 0; i < lps.length; i++) {
                System.out.print(lps[i]);
                if (i < lps.length - 1) System.out.print(", ");
            }
            System.out.println("]");
            System.out.println("Execution Time: " + executionTime + " ns (" +
                    String.format("%.3f", executionTime / 1000.0) + " μs)");

            String outputJSON = formatOutputJSON(input, index, lps, executionTime);

            String outputFileName = inputFile.getName().replace(".json", "_output.json");
            File outputFile = new File(OUTPUT_DIR + outputFileName);

            outputFile.getParentFile().mkdirs();

            Files.write(outputFile.toPath(), outputJSON.getBytes());
            System.out.println("Output written to: " + outputFile.getPath());

        } catch (IOException e) {
            System.err.println("Error processing " + inputFile.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("KMP Algorithm Test Runner");
        System.out.println("=" .repeat(80));

        File inputDir = new File(INPUT_DIR);

        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.err.println("Error: Input directory not found: " + INPUT_DIR);
            return;
        }

        File[] inputFiles = inputDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (inputFiles == null || inputFiles.length == 0) {
            System.err.println("No test files found in " + INPUT_DIR);
            return;
        }

        Arrays.sort(inputFiles, Comparator.comparing(File::getName));

        System.out.println("Found " + inputFiles.length + " test case(s)");

        int passed = 0;
        int failed = 0;

        for (File inputFile : inputFiles) {
            try {
                processTestCase(inputFile);
                passed++;
            } catch (Exception e) {
                System.err.println("Failed to process: " + inputFile.getName());
                e.printStackTrace();
                failed++;
            }
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("Total tests: " + inputFiles.length);
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("\nAll output files written to: " + OUTPUT_DIR);
        System.out.println("=".repeat(80));
    }
}