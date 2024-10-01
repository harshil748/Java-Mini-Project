import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.nio.file.Files;
import com.google.gson.Gson;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.net.*;

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
        this.totalWords = words.length; // sets the total words to the length of the words array
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

    private void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String path = exchange.getRequestURI().getPath();
                if (path.equals("/")) {
                    path = "/index.html";
                }
                File file = new File("website" + path);
                if (file.exists()) {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    exchange.sendResponseHeaders(200, bytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    os.close();
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
            }
        });
        server.createContext("/analyze", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    InputStream is = exchange.getRequestBody();
                    String text = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                    analyzeText(text);
                    Map<String, Object> response = new HashMap<>();
                    response.put("wordFrequency", getWordFrequency());
                    response.put("mostFrequentWord", getMostFrequentWord());
                    response.put("leastFrequentWords", getLeastFrequentWords(5));
                    String jsonResponse = new Gson().toJson(response);
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            }
        });
        server.createContext("/replace", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                    Map<String, String> request = new Gson().fromJson(requestBody, Map.class);
                    String oldWord = request.get("oldWord");
                    String newWord = request.get("newWord");
                    String newText = replaceWord(oldWord, newWord);
                    Map<String, Object> response = new HashMap<>();
                    response.put("newText", newText);
                    response.put("wordFrequency", getWordFrequency());
                    response.put("mostFrequentWord", getMostFrequentWord());
                    response.put("leastFrequentWords", getLeastFrequentWords(5));
                    String jsonResponse = new Gson().toJson(response);
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            }
        });
        server.createContext("/remove", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
                    Map<String, String> request = new Gson().fromJson(requestBody, Map.class);
                    String word = request.get("word");
                    String newText = removeWord(word);
                    Map<String, Object> response = new HashMap<>();
                    response.put("newText", newText);
                    response.put("wordFrequency", getWordFrequency());
                    response.put("mostFrequentWord", getMostFrequentWord());
                    response.put("leastFrequentWords", getLeastFrequentWords(5));
                    String jsonResponse = new Gson().toJson(response);
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            }
        });
        server.setExecutor(null);
        server.start();
    }

    public static void main(String[] args) {
        WordAnalyzer analyzer = new WordAnalyzer();
        try {
            analyzer.startServer();
            System.out.println("Server started on port 8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}