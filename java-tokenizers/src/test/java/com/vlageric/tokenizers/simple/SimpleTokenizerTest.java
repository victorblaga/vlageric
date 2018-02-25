package com.vlageric.tokenizers.simple;

import com.vlageric.tokenizers.Tokenizer;
import com.vlageric.tokenizers.TokenizerTest;

public class SimpleTokenizerTest extends TokenizerTest {
    private Phrases phrases = new Phrases("New York", "Los Angeles");

    @Override
    protected Tokenizer getTokenizer() {
        return new SimpleTokenizer(phrases);
    }
}