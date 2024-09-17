import java.util.*;
import java.util.stream.Collectors;

public class WordAnalyzer {
    private String originalText; // instance variable to store original text
    private Map<String, Integer> wordFrequency; // instance variable to store word frequency
    private int totalWords; // instance variable to store total words

    public void analyzeText(String text) { // method to analyze the text
        this.originalText = text; // takes string input text
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+"); // converts text to lowercase,removes special characters and splits the text into words
        this.wordFrequency = new HashMap<>();// creates a new hashmap to store word frequency
        for (String word : words) {// iterates through the words
            if (!word.isEmpty()) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);// puts the word in the hashmap and increments the frequency for each word
            }
        }
        this.totalWords = words.length;
    }

    public Map<String, Integer> getWordFrequency() {// method to get word frequency returns a hashmap of word frequency
        return new HashMap<>(wordFrequency);
    }

    public String getMostFrequentWord() { // method to get most frequent word
        return wordFrequency.entrySet().stream()// using java streams to get the most frequent word
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(""); // returns an empty string if no word is found or the word itself
    }

    public List<String> getLeastFrequentWords(int n) { // method to get least frequent words
        return wordFrequency.entrySet().stream() // using java streams to get the least frequent words
                .sorted(Map.Entry.comparingByValue())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());// returns a list of least frequent words or an empty list
    }

    public String replaceWord(String oldWord, String newWord) { // method to replace a word
        String newText = originalText.replaceAll("(?i)\\b" + oldWord + "\\b", newWord);
        analyzeText(newText); // reanalyze the text after replacing the word
        return newText;
    }

    public String removeWord(String word) {// method to remove a word (remove all occurrences of the word)
        String newText = originalText.replaceAll("(?i)\\b" + word + "\\b", "").replaceAll("\\s+", " ").trim();
        analyzeText(newText); // reanalyze the text after removing the word
        return newText;
    }

    public String getCurrentText() {// method to get the current text
        return originalText;
    }

    public int getTotalWords() {// method to get the total words
        return totalWords;
    }

    public static void main(String[] args) {
        WordAnalyzer analyzer = new WordAnalyzer();
        Scanner scanner = new Scanner(System.in);
        System.out.println("ENTER TEXT TO ANALYZE : \n");
        String text = scanner.nextLine();
        analyzer.analyzeText(text);
        String[] words = text.split(" ");
        int wordCount = words.length;
        System.out.println("TOTAL WORDS : " + wordCount);
        System.out.println("ANALYSIS COMPLETE.");
        while (true) {
            System.out.println("\nWORD ANALYZER MENU:\n");
            System.out.println("1. GET WORD FREQUENCY.");
            System.out.println("2. GET MOST FREQUENT WORD.");
            System.out.println("3. GET LEAST FREQUENT WORDS.");
            System.out.println("4. REPLACE WORD.");
            System.out.println("5. REMOVE WORD.");
            System.out.println("6. PRINT CURRENT TEXT.");
            System.out.println("7. EXIT.");
            System.out.println("8. ANALYZE NEW SENTENCE.");
            System.out.print("\nENTER YOUR CHOICE : ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        Map<String, Integer> frequency = analyzer.getWordFrequency(); // get word frequency from the analyzer object and store it in a hashmap
                        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {// iterate through the hashmap and print the word frequency
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("ENTER AN INPUT FIRST.");
                    }
                case 2:
                    String mostFrequent = analyzer.getMostFrequentWord();
                    System.out.println("MOST FREQUENT WORD: " + mostFrequent);
                    break;
                case 3:
                    System.out.println("ENTER THE NUMBER OF LEAST FREQUENT WORDS TO DISPLAY : ");
                    int n = scanner.nextInt();
                    scanner.nextLine();
                    List<String> leastFrequent = analyzer.getLeastFrequentWords(n); // get least frequent words from the analyzer object and store it in a list
                    System.out.println("LEAST FREQUENT WORDS : " + String.join(", ", leastFrequent));
                    break;
                case 4:
                    System.out.println("ENTER THE WORD TO REPLACE : ");
                    String oldWord = scanner.nextLine();
                    System.out.println("ENTER THE NEW WORD : ");
                    String newWord = scanner.nextLine();
                    String replacedText = analyzer.replaceWord(oldWord, newWord);
                    System.out.println("UPDATED TEXT : " + replacedText);
                    break;
                case 5:
                    System.out.println("ENTER THE WORD TO REMOVE : ");
                    String wordToRemove = scanner.nextLine();
                    String removedText = analyzer.removeWord(wordToRemove);
                    System.out.println("UPDATED TEXT : " + removedText);
                    System.out.println("TOTAL WORDS : " + analyzer.getTotalWords());
                    break;
                case 6:
                    System.out.println("CURRENT TEXT : " + analyzer.getCurrentText());
                    break;
                case 7:
                    System.out.println("EXITING...");
                    scanner.close();
                    System.exit(0);
                case 8:
                    System.out.println("ENTER NEW TEXT TO ANALYZE : ");
                    String newText = scanner.nextLine();
                    analyzer.analyzeText(newText); // clears the previous text and analyzes the new one
                    System.out.println("ANALYSIS COMPLETE FOR NEW TEXT.");
                    break;
                default:
                    System.out.println("INVALID CHOICE. PLEASE TRY AGAIN.");
            }
        }
    }
}