# Word Analyzer

## Project Description

Word Analyzer is a Java-based project that provides text analysis capabilities. It includes a web interface for users to input text and perform various operations such as analyzing word frequency, replacing words, and removing words. The project is designed to help users gain insights into their text data and perform text manipulation tasks easily.

## Installation

To set up the project, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/harshil748/Java-Mini-Project.git
   cd Java-Mini-Project
   ```

2. Set up the development container (optional but recommended):
   - Ensure you have Docker installed on your machine.
   - Open the project in a code editor that supports development containers (e.g., Visual Studio Code).
   - Follow the prompts to set up the development container.

3. Build the project:
   ```sh
   javac WordAnalyzer.java
   ```

4. Launch the project:
   ```sh
   java WordAnalyzer
   ```

## Usage

### Analyzing Text

1. Open the web interface by navigating to `http://localhost:8080` in your web browser.
2. Enter the text you want to analyze in the provided textarea.
3. Click the "Analyze Text" button to analyze the word frequency in the text.
4. The results will be displayed, showing the word frequency, most frequent word, and least frequent words.

### Replacing Words

1. Click the "Replace Word" button.
2. Enter the word you want to replace and the new word in the prompts.
3. The text will be updated with the replaced word, and the analysis results will be refreshed.

### Removing Words

1. Click the "Remove Word" button.
2. Enter the word you want to remove in the prompt.
3. The text will be updated with the word removed, and the analysis results will be refreshed.

## Contributing

Contributions are welcome! If you would like to contribute to the project, please follow these guidelines:

1. Fork the repository.
2. Create a new branch for your feature or bug fix:
   ```sh
   git checkout -b my-feature-branch
   ```
3. Make your changes and commit them with descriptive commit messages.
4. Push your changes to your forked repository:
   ```sh
   git push origin my-feature-branch
   ```
5. Create a pull request to the main repository, describing your changes and the problem they solve.

Please ensure your code follows the project's coding standards and includes appropriate tests.

Thank you for contributing!
