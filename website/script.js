document.addEventListener('DOMContentLoaded', () => {
    const textForm = document.getElementById('textForm');
    const textInput = document.getElementById('textInput');
    const analyzeButton = document.getElementById('analyzeButton');
    const replaceButton = document.getElementById('replaceButton');
    const removeButton = document.getElementById('removeButton');
    const wordFrequencyList = document.getElementById('wordFrequencyList');
    const mostFrequentWordText = document.getElementById('mostFrequentWordText');
    const leastFrequentWordsList = document.getElementById('leastFrequentWordsList');

    analyzeButton.addEventListener('click', () => {
        const text = textInput.value;
        fetch('/analyze', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text })
        })
        .then(response => response.json())
        .then(data => {
            updateResults(data);
        })
        .catch(error => console.error('Error:', error));
    });

    replaceButton.addEventListener('click', () => {
        const oldWord = prompt('Enter the word to replace:');
        const newWord = prompt('Enter the new word:');
        fetch('/replace', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ oldWord, newWord })
        })
        .then(response => response.json())
        .then(data => {
            textInput.value = data.newText;
            updateResults(data);
        })
        .catch(error => console.error('Error:', error));
    });

    removeButton.addEventListener('click', () => {
        const word = prompt('Enter the word to remove:');
        fetch('/remove', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ word })
        })
        .then(response => response.json())
        .then(data => {
            textInput.value = data.newText;
            updateResults(data);
        })
        .catch(error => console.error('Error:', error));
    });

    function updateResults(data) {
        wordFrequencyList.innerHTML = '';
        for (const [word, frequency] of Object.entries(data.wordFrequency)) {
            const li = document.createElement('li');
            li.textContent = `${word}: ${frequency}`;
            wordFrequencyList.appendChild(li);
        }

        mostFrequentWordText.textContent = data.mostFrequentWord;

        leastFrequentWordsList.innerHTML = '';
        for (const word of data.leastFrequentWords) {
            const li = document.createElement('li');
            li.textContent = word;
            leastFrequentWordsList.appendChild(li);
        }
    }
});
