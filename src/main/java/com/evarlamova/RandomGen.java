package com.evarlamova;

import java.util.Random;

public class RandomGen {

    public interface PseudoRandomGenerator {
        float nextFloat();
    }

    public static class JavaRandomPseudoRandomGenerator
            implements PseudoRandomGenerator {
        private final Random rand = new Random();

        @Override
        public float nextFloat() {
            return rand.nextFloat();
        }
    }

    private final int[] randomNums;
    private final float[] probRanges;

    private final PseudoRandomGenerator randomGenerator;

    public RandomGen(int[] randomNums, float[] probabilities) {
        this(randomNums, probabilities, new JavaRandomPseudoRandomGenerator());
    }

    protected RandomGen(int[] randomNums, float[] probabilities, PseudoRandomGenerator randomGenerator) {
        if (randomNums.length != probabilities.length) {
            throw new IllegalArgumentException("Nums arrays length should be equal to probabilities array length");
        }
        float sum = 0f;
        var probRanges = new float[probabilities.length + 1];
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            probRanges[i + 1] = sum;
        }

        if (sum != 1) {
            throw new IllegalArgumentException("Probability sum should be 1");
        }
//        if (1f - sum >= 0.00001) {
//            throw new IllegalArgumentException("Probability sum should be 1");
//        }
        this.randomNums = randomNums;
        this.probRanges = probRanges;
        this.randomGenerator = randomGenerator;
    }

    /**
     * Returns one of the randomNums. When this method is called
     * multiple times over a long period, it should return the
     * numbers roughly with the initialized probabilities.
     */
    public int nextNum() {
        //Generating evenly distributed float random between 0.0 to 1.0
        var rand = randomGenerator.nextFloat();
        int l = 0, r = randomNums.length - 1;
        //We use binary search to find range where our random can fit.
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (probRanges[m] <= rand && rand < probRanges[m + 1]) {
                return randomNums[m];
            }
            if (probRanges[m + 1] <= rand) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }

        throw new IllegalStateException("Should never happen");
    }
}