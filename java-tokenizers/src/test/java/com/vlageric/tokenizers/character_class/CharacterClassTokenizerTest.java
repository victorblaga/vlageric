package com.vlageric.tokenizers.character_class;

import com.vlageric.tokenizers.Tokenizer;
import com.vlageric.tokenizers.TokenizerTest;
import com.vlageric.tokenizers.simple.Phrases;

public class CharacterClassTokenizerTest extends TokenizerTest {
    private Phrases phrases = new Phrases("New York", "Los Angeles");

    @Override
    protected Tokenizer getTokenizer() {
        return new CharacterClassTokenizer(phrases);
    }
}