package com.recruit.utils;

import java.util.Random;

/**
 * Created by zhuangjt on 2017/3/16.
 */
public class RandomKeyGenerateUtil {
    private static final int DEFAULT_SEED = 10;

    private static final int DEFAULT_LENGTH = 8;

    public static String getRandomStrKey(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(DEFAULT_LENGTH);

        for(int i=0; i<(length <= 0 ? DEFAULT_LENGTH : length); i++) {
            stringBuilder.append(random.nextInt(DEFAULT_SEED));
        }

        return stringBuilder.toString();
    }
}
