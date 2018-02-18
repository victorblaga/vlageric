package com.vlageric.tokenizers.simple;

import java.util.ArrayList;
import java.util.Arrays;

public class Phrases extends ArrayList<String> {
    public Phrases(String... phrases) {
        super(Arrays.asList(phrases));
    }

    public static Phrases empty() {
        return new Phrases();
    }
}
