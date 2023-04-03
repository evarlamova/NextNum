package com.evarlamova;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomGenTest {

    public static class DeterministicPseudoRandomGenerator implements RandomGen.PseudoRandomGenerator {

        private float randomValue;

        public void setRandomValue(float randomValue) {
            this.randomValue = randomValue;
        }

        public float nextFloat() {
            return randomValue;
        }
    }

    @Test
    void nextNumTest_testIllegalArguments() {

        IllegalArgumentException thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    new RandomGen(new int[]{1, 2, 3}, new float[]{1.0f});
                }, "Expected assertion exception");

        Assertions.assertEquals("Nums arrays length should be equal to probabilities array length", thrown.getMessage());
        thrown = Assertions
                .assertThrows(IllegalArgumentException.class, () -> {
                    new RandomGen(new int[]{1, 2, 3}, new float[]{1.0f, 0.0001f, 0.0002f});
                }, "Expected assertion exception");
        Assertions.assertEquals("Probability sum should be 1", thrown.getMessage());
    }

    @Test
    void nextNumTest_baseCase() {
        var nums = new int[]{-1, 0, 1, 2, 3};
        var probs = new float[]{0.01f, 0.3f, 0.58f, 0.1f, 0.01f};

        var deterministicRandom = new DeterministicPseudoRandomGenerator();
        var testee = new RandomGen(nums, probs, deterministicRandom);
        deterministicRandom.setRandomValue(0f);
        assertEquals(testee.nextNum(), nums[0]);

        deterministicRandom.setRandomValue(0.005f);
        assertEquals(testee.nextNum(), nums[0]);

        deterministicRandom.setRandomValue(0.01f);
        assertEquals(testee.nextNum(), nums[1]);

        deterministicRandom.setRandomValue(0.15f);
        assertEquals(testee.nextNum(), nums[1]);

        deterministicRandom.setRandomValue(0.31f);
        assertEquals(testee.nextNum(), nums[2]);

        deterministicRandom.setRandomValue(0.5f);
        assertEquals(testee.nextNum(), nums[2]);

        deterministicRandom.setRandomValue(0.89f);
        assertEquals(testee.nextNum(), nums[3]);

        deterministicRandom.setRandomValue(0.95f);
        assertEquals(testee.nextNum(), nums[3]);

        deterministicRandom.setRandomValue(0.99f);
        assertEquals(testee.nextNum(), nums[4]);

        deterministicRandom.setRandomValue(0.999999f);
        assertEquals(testee.nextNum(), nums[4]);
    }

    @Test
    void nextNumTest() {
        var rand = new Random();
        int size = rand.nextInt(1000);
        var numsList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            numsList.add(i);
        }
        Collections.shuffle(numsList);
        var probs = new float[size];
        float probability = 1.0f / size;
        Arrays.fill(probs, probability);
        float sum = 0f;
        for (var v : probs) {
            sum += v;
        }
        probs[size - 1] += 1f - sum;

        int[] nums = numsList.stream().mapToInt(e -> (int) e).toArray();
        var deterministicRandom = new DeterministicPseudoRandomGenerator();
        var testee = new RandomGen(nums, probs, deterministicRandom);
        float randomValue = 0f;
        for (int i = 0; i < size; i++) {
            deterministicRandom.setRandomValue(randomValue);
            assertEquals(testee.nextNum(), nums[i]);
            randomValue += probability;
        }
    }
}
