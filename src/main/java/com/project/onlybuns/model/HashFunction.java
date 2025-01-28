package com.project.onlybuns.model;

import java.nio.charset.StandardCharsets;

public class HashFunction {
    private final int size;
    private final int seed;

    public HashFunction(int size, int seed) {
        this.size = size;
        this.seed = seed;
    }

    public int hash(String input) {
        int hash = seed;
        for (char c : input.toCharArray()) {
            hash = 31 * hash + c;
        }
        return Math.abs(hash % size); // Ensure the hash is within bounds
    }
}
