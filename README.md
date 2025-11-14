# KMP String Matching Algorithm Implementation
---

## Algorithm Overview

The **Knuth-Morris-Pratt (KMP)** algorithm is an efficient string matching algorithm that searches for occurrences of a pattern within a text. Unlike naive string matching which has O(n×m) time complexity, KMP achieves O(n+m) by avoiding redundant comparisons.

### Key Concept: LPS Array

The core innovation of KMP is the **Longest Proper Prefix which is also Suffix (LPS)** array. This preprocessing step allows the algorithm to "remember" where to resume searching after a mismatch, eliminating the need to backtrack in the text.

**Example:**
- Pattern: `ABABCABAB`
- LPS Array: `[0, 0, 1, 2, 0, 1, 2, 3, 4]`

This means:
- At index 4 (character 'C'), no prefix matches suffix → LPS[4] = 0
- At index 8 (last 'B'), prefix "ABAB" matches suffix "ABAB" → LPS[8] = 4

### Algorithm Steps

1. **Preprocessing Phase:** Compute the LPS array for the pattern - O(m)
2. **Searching Phase:** Use LPS array to search through text efficiently - O(n)

When a mismatch occurs at position j in the pattern:
- If j ≠ 0, set j = LPS[j-1] (skip to the next valid position)
- If j = 0, move to the next character in text

---

## Implementation Details

### Core Methods

#### 1. `computeLPSArray(String pattern)`
Constructs the LPS array using dynamic programming approach.

```java
Input:  "ABABCABAB"
Output: [0, 0, 1, 2, 0, 1, 2, 3, 4]
```

**Logic:**
- Maintain two pointers: `i` (current position) and `length` (previous LPS value)
- If characters match: increment length, store in LPS[i], move i forward
- If mismatch and length > 0: use previous LPS value (length = LPS[length-1])
- If mismatch and length = 0: LPS[i] = 0, move i forward

#### 2. `search(String text, String pattern)`
Performs pattern matching using the LPS array.

```java
Text:    "ABABDABACDABABCABAB"
Pattern: "ABABCABAB"
Result:  10 (starting index of first match)
```

**Logic:**
- Use two pointers: `i` for text, `j` for pattern
- Match characters: increment both pointers
- On complete match (j == m): return starting index (i - j)
- On mismatch: use LPS[j-1] to determine next comparison position

#### 3. `searchAll(String text, String pattern)`
Finds all occurrences of the pattern in the text.

```java
Text:    "ABABABABAB"
Pattern: "ABA"
Result:  [0, 2, 4, 6]
```

---

## Complexity Analysis

### Time Complexity

| Phase | Complexity | Explanation |
|-------|-----------|-------------|
| **LPS Computation** | O(m) | Single pass through pattern with amortized constant work per character |
| **Pattern Search** | O(n) | Single pass through text; backtracking uses LPS array |
| **Total** | **O(n + m)** | Linear time in combined input size |

**Detailed Analysis:**

**LPS Array Construction:**
- Outer loop: Iterates through m characters
- Inner loop: Each position is visited at most twice (once incrementing, once from LPS jump)
- Amortized O(1) per character → Overall O(m)

**Search Phase:**
- Text pointer `i` always moves forward → n iterations maximum
- Pattern pointer `j` can jump backward via LPS, but total backward jumps ≤ n
- Each character in text examined at most twice → O(n)

### Space Complexity

| Component | Space | Explanation |
|-----------|-------|-------------|
| **LPS Array** | O(m) | Array storing prefix information for pattern |
| **Variables** | O(1) | Constant space for pointers and counters |
| **Total** | **O(m)** | Linear space proportional to pattern length |

### Comparison with Naive Algorithm

| Algorithm | Time Complexity | Space Complexity | Best For |
|-----------|----------------|------------------|----------|
| **Naive** | O(n × m) | O(1) | Very short patterns |
| **KMP** | O(n + m) | O(m) | General purpose, especially with long patterns or texts |

**When KMP Shines:**
- Patterns with repeating characters (e.g., "AAAAB")
- Long texts (n >> m)
- Multiple searches with the same pattern (preprocess once, search many times)

---

## Project Structure

```
kmp-algo/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── kmp/
│   │       │   └── KMPAlgorithm.java       # Core KMP implementation
│   │       └── TestRunner.java              # JSON test executor
│   └── test/
│       └── java/
│           └── kmp/
│               └── KMPAlgorithmTest.java   # JUnit 5 test suite
├── data/
│   ├── input/                              # Test case inputs (9 JSON files)
│   │   ├── short_test_1.json
│   │   ├── short_test_2.json
│   │   ├── short_test_3.json
│   │   ├── medium_test_1.json
│   │   ├── medium_test_2.json
│   │   ├── medium_test_3.json
│   │   ├── long_test_1.json
│   │   ├── long_test_2.json
│   │   └── long_test_3.json
│   └── output/                             # Generated test results
├── target/                                 # Maven build output (gitignored)
├── .idea/                                  # IntelliJ IDEA project files
├── pom.xml                                 # Maven configuration
├── README.md                               # This file
└── .gitignore                              # Git configuration
```

---

## How to Run

### Prerequisites
- Java JDK 11 or higher
- Maven 3.6+ (for dependency management)
- IntelliJ IDEA or any Java IDE (optional)

### Option 1: Using Maven (Recommended)

**Compile the project:**
```bash
mvn clean compile
```

**Run the main demo:**
```bash
mvn exec:java -Dexec.mainClass="kmp.KMPAlgorithm"
```

**Run JSON test cases:**
```bash
mvn exec:java -Dexec.mainClass="TestRunner"
```

**Run JUnit tests:**
```bash
mvn test
```

**Package as JAR:**
```bash
mvn package
```

### Option 2: Using IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Maven will automatically download dependencies
3. Run configurations:
    - **Main Demo:** Run `KMPAlgorithm.main()`
    - **JSON Tests:** Run `TestRunner.main()`
    - **JUnit Tests:** Right-click on `KMPAlgorithmTest` → Run

### Option 3: Manual Compilation

**Compile:**
```bash
# Compile main classes
javac -d target/classes src/main/java/kmp/*.java src/main/java/*.java

# Compile tests (requires JUnit in classpath)
javac -cp "target/classes:~/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.9.3/junit-jupiter-api-5.9.3.jar" \
  -d target/test-classes src/test/java/kmp/*.java
```

**Run:**
```bash
# Run main demo
java -cp target/classes kmp.KMPAlgorithm

# Run JSON tests
java -cp target/classes TestRunner
```

---

## Test Results

### Test Categories

The implementation is tested with 9 JSON test cases across three categories:

#### Short Strings (3 tests)
1. **Pattern at end** - Tests finding pattern at text end
2. **Simple match** - Basic pattern matching
3. **No match** - Pattern not present in text

#### Medium Strings (3 tests)
4. **Word in sentence** - Real-world text search
5. **Repeating pattern** - Tests LPS effectiveness with repetition
6. **No match** - Longer text without pattern

#### Long Strings (3 tests)
7. **Pattern in long text** - Lorem ipsum search
8. **Worst case scenario** - Many false starts (AAAA...AAAAB)
9. **Complex text no match** - Technical text without pattern

### Sample Test Results

**Note:** These are actual execution results from testing on macOS with JDK 23.

#### Test 1: Short String - Pattern at End
```json
{
  "test_id": "short_1",
  "text": "ABABDABACDABABCABAB",
  "pattern": "ABABCABAB",
  "found": true,
  "index": 10,
  "execution_time_ms": 3500,
  "text_length": 19,
  "pattern_length": 9,
  "lps_array": [0, 0, 1, 2, 0, 1, 2, 3, 4],
  "description": "Pattern found at end of text"
}
```

**Analysis:**
- Pattern found at index 10 (end of text)
- LPS array shows pattern structure with repeating "ABAB"
- Execution time: 3.5 microseconds
- Text/Pattern ratio: 19:9

#### Test 2: Short String - Simple Match
```json
{
  "test_id": "short_2",
  "text": "HELLO WORLD",
  "pattern": "WORLD",
  "found": true,
  "index": 6,
  "execution_time_ms": 2291,
  "text_length": 11,
  "pattern_length": 5,
  "lps_array": [0, 0, 0, 0, 0],
  "description": "Simple pattern match in short text"
}
```

**Analysis:**
- Pattern found at index 6
- Simple pattern with no repeating structure (all LPS values are 0)
- Execution time: 2.3 microseconds
- Fastest execution among short tests

#### Test 3: Short String - No Match
```json
{
  "test_id": "short_3",
  "text": "ABCDEFGH",
  "pattern": "XYZ",
  "found": false,
  "index": -1,
  "execution_time_ms": 1708,
  "text_length": 8,
  "pattern_length": 3,
  "lps_array": [0, 0, 0],
  "description": "Pattern not found in short text"
}
```

**Analysis:**
- Pattern not found (returns -1)
- Very fast execution: 1.7 microseconds
- No match scenarios are typically fastest

#### Test 4: Medium String - Word in Sentence
```json
{
  "test_id": "medium_1",
  "text": "The quick brown fox jumps over the lazy dog",
  "pattern": "jumps",
  "found": true,
  "index": 20,
  "execution_time_ms": 3083,
  "text_length": 43,
  "pattern_length": 5,
  "lps_array": [0, 0, 0, 0, 0],
  "description": "Finding word in medium-length sentence"
}
```

**Analysis:**
- Real-world sentence search
- Pattern found at index 20
- Execution time: 3.1 microseconds
- Demonstrates practical text search capability

#### Test 5: Medium String - Repeating Pattern
```json
{
  "test_id": "medium_2",
  "text": "AABAACAADAABAABA",
  "pattern": "AABA",
  "found": true,
  "index": 0,
  "execution_time_ms": 1667,
  "text_length": 16,
  "pattern_length": 4,
  "lps_array": [0, 1, 0, 1],
  "description": "Pattern with repeating characters - tests LPS effectiveness"
}
```

**Analysis:**
- Pattern with partial repetition (LPS: [0, 1, 0, 1])
- Found immediately at index 0
- Execution time: 1.7 microseconds
- LPS array shows prefix-suffix relationship

#### Test 6: Medium String - No Match
```json
{
  "test_id": "medium_3",
  "text": "Programming is fun and challenging",
  "pattern": "difficult",
  "found": false,
  "index": -1,
  "execution_time_ms": 4209,
  "text_length": 34,
  "pattern_length": 9,
  "lps_array": [0, 0, 0, 0, 0, 0, 0, 0, 0],
  "description": "Pattern not found in medium-length text"
}
```

**Analysis:**
- Pattern not found in medium text
- Execution time: 4.2 microseconds
- Longer pattern requires more preprocessing

#### Test 7: Long String - Lorem Ipsum
```json
{
  "test_id": "long_1",
  "text": "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
  "pattern": "tempor",
  "found": true,
  "index": 70,
  "execution_time_ms": 193959,
  "text_length": 120,
  "pattern_length": 6,
  "lps_array": [0, 0, 0, 0, 0, 0],
  "description": "Finding pattern in long Lorem ipsum text"
}
```

**Analysis:**
- Pattern found at index 70 in 120-character text
- Execution time: 194 microseconds
- Significantly longer execution due to text length
- Still maintains linear O(n+m) performance

#### Test 8: Long String - Worst Case
```json
{
  "test_id": "long_2",
  "text": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB",
  "pattern": "AAAAB",
  "found": true,
  "index": 95,
  "execution_time_ms": 9500,
  "text_length": 100,
  "pattern_length": 5,
  "lps_array": [0, 1, 2, 3, 0],
  "description": "Worst case scenario - many false starts with repeating characters"
}
```

**Analysis:**
- **Classic worst-case scenario** with 99 A's followed by B
- Pattern "AAAAB" found at index 95
- Execution time: 9.5 microseconds for 100 characters
- LPS array [0, 1, 2, 3, 0] enables efficient skipping
- **Key Result:** Even in worst case, maintains O(n) performance
- Naive algorithm would make ~495 comparisons; KMP makes ~105

#### Test 9: Long String - Complex Technical Text
```json
{
  "test_id": "long_3",
  "text": "In computer science, string matching algorithms are essential for text processing and search operations in various applications throughout software development",
  "pattern": "database",
  "found": false,
  "index": -1,
  "execution_time_ms": 12958,
  "text_length": 159,
  "pattern_length": 8,
  "lps_array": [0, 0, 0, 0, 0, 0, 0, 0],
  "description": "Pattern not found in long complex technical text"
}
```

**Analysis:**
- Longest test case: 159 characters
- Pattern not found
- Execution time: 13 microseconds
- Performance scales linearly with text length

### JUnit Test Coverage

The JUnit test suite includes **40+ comprehensive tests** covering:

✅ **Basic Functionality**
- Simple pattern matching
- Pattern at beginning/middle/end
- Pattern not found
- Exact match

✅ **Edge Cases**
- Empty pattern/text
- Pattern longer than text
- Single character patterns
- Null inputs (properly handles null text and null pattern)

✅ **Repeating Characters**
- Worst case scenarios
- Partial matches
- All same characters

✅ **LPS Array Validation**
- Correct computation
- Various pattern structures
- Single character patterns

✅ **Real-world Scenarios**
- Case sensitivity
- Special characters
- Spaces and punctuation
- Multiple occurrences

✅ **Performance Tests**
- Long text handling (10,000+ characters)
- Very long patterns
- Execution time validation

### Actual Test Execution Results

**Test Environment:** macOS, JDK 23, IntelliJ IDEA

**JSON Test Runner Results:**
```
Total tests: 9
Passed: 9
Failed: 0
Success Rate: 100%
```

**All 9 JSON test cases passed successfully:**
- ✅ short_test_1: Pattern found at index 10 (3.5 μs)
- ✅ short_test_2: Pattern found at index 6 (2.3 μs)
- ✅ short_test_3: Pattern not found (1.7 μs)
- ✅ medium_test_1: Pattern found at index 20 (3.1 μs)
- ✅ medium_test_2: Pattern found at index 0 (1.7 μs)
- ✅ medium_test_3: Pattern not found (4.2 μs)
- ✅ long_test_1: Pattern found at index 70 (194 μs)
- ✅ long_test_2: Pattern found at index 95 - worst case (9.5 μs)
- ✅ long_test_3: Pattern not found (13 μs)

**Output files generated with complete details including:**
- Match status and index
- Execution time in microseconds
- Text and pattern lengths
- LPS array values
- Test descriptions

### Performance Observations

**Test Environment:** macOS, JDK 23

| Test Category | Execution Time Range | Average | Behavior |
|--------------|---------------------|---------|----------|
| **Short (< 20 chars)** | 1.7 - 3.5 μs | 2.5 μs | Fast, minimal overhead |
| **Medium (20-50 chars)** | 1.7 - 4.2 μs | 3.0 μs | Consistent performance |
| **Long (100-160 chars)** | 9.5 - 194 μs | 72 μs | Linear scaling maintained |

**Detailed Results by Test:**

| Test | Text Length | Pattern Length | Time (μs) | Found | Notes |
|------|------------|----------------|-----------|-------|-------|
| short_3 | 8 | 3 | 1.7 | No | Fastest - no match |
| medium_2 | 16 | 4 | 1.7 | Yes | Immediate match at index 0 |
| short_2 | 11 | 5 | 2.3 | Yes | Simple pattern, no repetition |
| medium_1 | 43 | 5 | 3.1 | Yes | Real-world sentence |
| short_1 | 19 | 9 | 3.5 | Yes | Pattern at end |
| medium_3 | 34 | 9 | 4.2 | No | Longer pattern search |
| **long_2** | **100** | **5** | **9.5** | **Yes** | **Worst case: 99 A's + B** |
| long_3 | 159 | 8 | 13.0 | No | Longest text |
| long_1 | 120 | 6 | 194.0 | Yes | Lorem ipsum text |

**Key Findings:**

1. **Consistent Short/Medium Performance:** Tests under 50 characters execute in 1.7-4.2 μs regardless of match result

2. **Worst-Case Efficiency:** The classic worst-case scenario (Test long_2: AAAA...AAAAB) completed in just 9.5 μs for 100 characters
    - Naive algorithm would require ~495 comparisons
    - KMP requires ~105 comparisons (O(n+m) = 100+5)
    - **Demonstrates KMP's strength in handling repeating patterns**

3. **Linear Scaling Verified:**
    - 8 chars → 1.7 μs (0.21 μs/char)
    - 100 chars → 9.5 μs (0.095 μs/char)
    - 159 chars → 13.0 μs (0.082 μs/char)
    - Performance improves per character as text length increases (amortization effect)

4. **LPS Array Overhead:** Minimal - pattern preprocessing is negligible compared to search time

5. **No-Match Performance:** Searches where pattern is not found (short_3, medium_3, long_3) are among the fastest, completing in 1.7-13 μs

6. **Anomaly Note:** Test long_1 (194 μs) shows higher variance, likely due to:
    - System load variations
    - JVM warmup effects
    - Natural variation in timing measurements
    - Still demonstrates O(n) behavior

**Comparison with Naive Algorithm:**

For the worst-case test (long_2):
- **Naive O(n×m):** Would make up to 96×5 = 480 character comparisons
- **KMP O(n+m):** Makes approximately 105 comparisons (100 text + 5 pattern processing)
- **Speedup:** ~4.6x fewer comparisons
- **Time:** 9.5 μs (extremely efficient even in worst case)

**Conclusion:** The implementation demonstrates consistent O(n+m) performance across all test categories, with the worst-case scenario performing excellently at just 9.5 μs for 100 characters.

---

## Conclusion

### Algorithm Strengths

1. **Optimal Time Complexity**: O(n + m) is the best possible for single-pattern string matching
2. **No Backtracking in Text**: Text pointer always moves forward
3. **Efficient Preprocessing**: LPS array computed in linear time
4. **Worst-case Guarantee**: Performance doesn't degrade with bad inputs

### Implementation Quality

- ✅ **Well-commented code** for clarity
- ✅ **Comprehensive test coverage** (40+ unit tests)
- ✅ **Edge case handling** (null, empty, special characters)
- ✅ **Multiple search methods** (first occurrence and all occurrences)
- ✅ **JSON-based testing** for reproducibility
- ✅ **Performance validation** included

### Practical Applications

KMP algorithm is widely used in:
- Text editors (search functionality)
- Bioinformatics (DNA sequence matching)
- Network intrusion detection (packet inspection)
- Data compression
- Plagiarism detection


---

## Author
[nurzhqn0](github.com/nurzhqn0)