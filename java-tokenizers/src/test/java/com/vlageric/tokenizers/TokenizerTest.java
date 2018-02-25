package com.vlageric.tokenizers;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class TokenizerTest {
    protected abstract Tokenizer getTokenizer();

    @Test
    public void test_simple() {
        List<String> tokens = getTokenizer().tokenize("This is my first text");
        List<String> expected = Arrays.asList("this", "is", "my", "first", "text");
        assertThat(tokens, is(expected));
    }

    @Test
    public void test_punctuation() {
        List<String> tokens = getTokenizer().tokenize("This is my second text, is it not?");
        List<String> expected = Arrays.asList("this", "is", "my", "second", "text", "is", "it", "not");
        assertThat(tokens, is(expected));
    }

    @Test
    public void test_apostrophe() {
        List<String> tokens = getTokenizer().tokenize("I'm hungry!");
        List<String> expected = Arrays.asList("im", "hungry");
        assertThat(tokens, is(expected));
    }

    @Test
    public void test_phrases() {
        List<String> tokens = getTokenizer().tokenize("Today I'm flying to New York. Next week it's Los Angeles.");
        List<String> expected = Arrays.asList("today", "im", "flying", "to", "new york",
                "next", "week", "its", "los angeles");
        assertThat(tokens, is(expected));
    }
}
