package com.sollertia.habit.global.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {

    private static final Random RAND = new Random();

    public int getRandomNumber() {
        int min = 0;
        int max = 9;
        return RAND.nextInt((max - min) + 1);
    }

    public int[] getRandomNumbers(int max) {
        int size = 5;
        if ( max < size ) {
            size = max;
        }
        return RAND.ints(0, max)
                .distinct()
                .limit(size)
                .toArray();
    }
}
