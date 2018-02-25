package com.vlageric.tokenizers.character_class;

import com.vlageric.tokenizers.simple.Phrases;
import com.vlageric.tokenizers.Tokenizer;

import java.util.*;

public class CharacterClassTokenizer implements Tokenizer {
    private final Phrases phrases;
    private final Set<Character> PUNCTUATION_SIGNS = new HashSet<>(Arrays.asList('.', ',', '!', '?', ' '));

    public CharacterClassTokenizer() {
        this.phrases = Phrases.empty();
    }

    public CharacterClassTokenizer(Phrases phrases) {
        this.phrases = phrases;
    }

    @Override
    public List<String> tokenize(String text) {
        text = text.toLowerCase();
        if (text.length() == 0) {
            return Collections.emptyList();
        }

        CharacterClass[] characterClasses = initializeClasses(text);
        markPhrases(text, characterClasses);
        handleApostrophesAndPunctuation(text, characterClasses);

        return extractTokens(text, characterClasses);
    }

    private CharacterClass[] initializeClasses(String text) {
        CharacterClass[] result = new CharacterClass[text.length()];
        Arrays.fill(result, CharacterClass.INSIDE);
        result[0] = CharacterClass.BEGINNING;

        return result;
    }

    private void markPhrases(String text, CharacterClass[] characterClasses) {
        for (String phrase : phrases) {
            String lowercasePhrase = phrase.toLowerCase();
            int indexOf = text.indexOf(lowercasePhrase);
            if (indexOf >= 0) {
                characterClasses[indexOf] = CharacterClass.START_PHRASE;
                for (int i = indexOf + 1; i < indexOf + phrase.length(); i++) {
                    characterClasses[i] = CharacterClass.INSIDE_PHRASE;
                }
                int indexFirstAfter = indexOf + lowercasePhrase.length();
                if (indexFirstAfter < characterClasses.length && characterClasses[indexFirstAfter] != CharacterClass.START_PHRASE) {
                    characterClasses[indexFirstAfter] = CharacterClass.BEGINNING;
                }
            }
        }
    }

    private void handleApostrophesAndPunctuation(String text, CharacterClass[] characterClasses) {
        for (int i = 0; i < text.length(); i++) {
            if (isApostropheBetweenLetters(text, i)) {
                characterClasses[i] = CharacterClass.DELETE;
            } else if (isPunctuationSign(text, i) && !isPhrase(characterClasses[i])) {
                characterClasses[i] = CharacterClass.OUTSIDE;
                if (i + 1 < characterClasses.length && characterClasses[i + 1] != CharacterClass.START_PHRASE) {
                    characterClasses[i + 1] = CharacterClass.BEGINNING;
                }
            }
        }
    }

    private boolean isApostropheBetweenLetters(String text, int i) {
        if (i > 0 && i < text.length() - 1) {
            char character = text.charAt(i);
            char charBefore = text.charAt(i - 1);
            char charAfter = text.charAt(i + 1);

            return character == '\'' && Character.isAlphabetic(charBefore) && Character.isAlphabetic(charAfter);
        } else {
            return false;
        }
    }

    private boolean isPunctuationSign(String text, int i) {
        char character = text.charAt(i);
        return PUNCTUATION_SIGNS.contains(character);
    }

    private boolean isPhrase(CharacterClass characterClass) {
        return characterClass == CharacterClass.INSIDE_PHRASE || characterClass == CharacterClass.START_PHRASE;
    }

    private List<String> extractTokens(String text, CharacterClass[] characterClasses) {
        List<String> result = new ArrayList<>();
        StringBuilder currentToken = null;
        CharacterClass previousClass = CharacterClass.OUTSIDE;

        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            CharacterClass currentClass = characterClasses[i];
            switch (currentClass) {
                case BEGINNING:
                case START_PHRASE:
                    if (currentToken != null && previousClass != CharacterClass.OUTSIDE) {
                        result.add(currentToken.toString());
                    }
                    currentToken = new StringBuilder();
                    currentToken.append(currentChar);
                    break;
                case INSIDE:
                case INSIDE_PHRASE:
                    currentToken.append(currentChar);
                    break;
                case OUTSIDE:
                    if (previousClass != CharacterClass.OUTSIDE) {
                        result.add(currentToken.toString());
                        currentToken = new StringBuilder();
                    }
                    break;
            }
            previousClass = currentClass;
        }

        if (currentToken != null && currentToken.length() > 0) {
            result.add(currentToken.toString());
        }
        return result;
    }
}
