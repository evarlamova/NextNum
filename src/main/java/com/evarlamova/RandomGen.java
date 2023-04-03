package com.evarlamova;

import java.util.Random;

public class RandomGen {

    public interface PseudoRandomGenerator {
        float nextFloat(float bound);
    }

    public static class JavaRandomPseudoRandomGenerator
            implements PseudoRandomGenerator {
        private final Random rand = new Random();

        @Override
        public float nextFloat(float bound) {
            return rand.nextFloat(bound);
        }
    }

    private final int[] randomNums;
    private final float[] probRanges;

    public static final float DELTA = 0.00001f;

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

//      I am assuming that float can loose precision,
//      that's why doing checks like that, introducing small delt
        if (Math.abs(1f - sum) >= DELTA) {
            throw new IllegalArgumentException("Probability sum should be 1");
        }
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
        int l = 0, r = randomNums.length - 1;
        //We're doing float summing, due to possible loose of precision, upper bound can be lesser by DELTA interval
        var rand = randomGenerator.nextFloat(probRanges[r]);
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