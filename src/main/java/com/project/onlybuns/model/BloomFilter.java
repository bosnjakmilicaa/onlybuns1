package com.project.onlybuns.model;

import org.springframework.stereotype.Component;

import java.util.BitSet;
import java.util.List;
import java.util.ArrayList;

@Component
public class BloomFilter {
    private static final int DEFAULT_SIZE = 1000; // Size of the bit array
    private BitSet bitSet;
    private List<HashFunction> hashFunctions;

    // Constructor
    public BloomFilter() {
        this(DEFAULT_SIZE, 3); // Default size and number of hash functions
    }

    public BloomFilter(int size, int numHashFunctions) {
        bitSet = new BitSet(size);
        hashFunctions = new ArrayList<>(numHashFunctions);
        for (int i = 0; i < numHashFunctions; i++) {
            final int index = i;
            hashFunctions.add(new HashFunction(size, index));
        }
    }

    // Add a value to the Bloom filter
    public void add(String value) {
        for (HashFunction hashFunction : hashFunctions) {
            int hash = hashFunction.hash(value);
            bitSet.set(hash);
        }
    }

    // Check if a value might exist in the Bloom filter
    public boolean contains(String value) {
        for (HashFunction hashFunction : hashFunctions) {
            int hash = hashFunction.hash(value);
            if (!bitSet.get(hash)) {
                return false; // If any bit is not set, the value is definitely not in the filter
            }
        }
        return true; // If all bits are set, the value might be in the filter
    }
}
