package com.vlageric.tokenizers.simple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tokenizer {
    private final Phrases phrases;

    public Tokenizer() {
        this.phrases = Phrases.empty();
    }

    public Tokenizer(Phrases phrases) {
        this.phrases = phrases;
    }
    public List<String> tokenize(String text) {
        String lowercasedText = text.toLowerCase();
        Map<String, String> placeholders = new HashMap<>();

        for (int i = 0; i < phrases.size(); i++) {
            String phrase = phrases.get(i).toLowerCase();
            if (lowercasedText.contains(phrase)) {
                String placeholder = String.format("$%d", i);
                lowercasedText = lowercasedText.replace(phrase, placeholder);
                placeholders.put(placeholder, phrase);
            }
        }

        String textWithoutApostrophes = lowercasedText.replaceAll("'", "");
        String textWithLettersAndWhitespace = textWithoutApostrophes.replaceAll("[^A-Za-z\\s($\\d+)]", " ");

        List<String> result = Arrays.asList(textWithLettersAndWhitespace.split("\\s+"));
        return result.stream().map(token -> placeholders.getOrDefault(token, token)).collect(Collectors.toList());
    }
}
