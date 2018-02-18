package com.vlageric.tokenizers.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.vlageric.tokenizers.simple.Phrases;
import com.vlageric.tokenizers.simple.Tokenizer;
import org.junit.Test;

public class TokenizerTest {
    private Tokenizer tokenizer = new Tokenizer();

    @Test
    public void test_simple() {
        Tokenizer tokenizer = new Tokenizer();
        List<String> tokens = tokenizer.tokenize("This is my first text");
        List<String> expected = Arrays.asList("this", "is", "my", "first", "text");
        assertThat(tokens, is(expected));
    }

    @Test
    public void test_punctuation() {
        List<String> tokens = tokenizer.tokenize("This is my second text, is it not?");
        List<String> expected = Arrays.asList("this", "is", "my", "second", "text", "is", "it", "not");
        assertThat(tokens, is(expected));
    }

    @Test
    public void test_apostrophe() {
        List<String> tokens = tokenizer.tokenize("I'm hungry!");
        List<String> expected = Arrays.asList("im", "hungry");
        assertThat(tokens, is(expected));
    }

    @Test
    public void test_phrases() {
        Phrases phrases = new Phrases("New York", "Los Angeles");
        Tokenizer tokenizer = new Tokenizer(phrases);
        List<String> tokens = tokenizer.tokenize("Today I'm flying to New York. Next week it's Los Angeles.");
        List<String> expected = Arrays.asList("today", "im", "flying", "to", "new york",
                "next", "week", "its", "los angeles");
        assertThat(tokens, is(expected));
    }
}