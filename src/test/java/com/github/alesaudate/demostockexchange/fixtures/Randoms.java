package com.github.alesaudate.demostockexchange.fixtures;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Randoms {

    private static final Faker FAKER = new Faker();
    private static final Random RANDOM = new Random();

    public static String randomNYSEStock() {
        return FAKER.stock().nyseSymbol();
    }

    public static String randomBlankString() {
        return FAKER.regexify("\\s{10}");
    }

    public static BigDecimal moneyValue(int min, int max) {
        int topValue = max - min;
        Double d = RANDOM.nextDouble() * max;
        return BigDecimal.valueOf(d.doubleValue() + min).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal moneyValue(int max) {
        return moneyValue(0, max);
    }

    public static long randomLong(int max) {
        return Double.valueOf(RANDOM.nextDouble() * max).longValue();
    }

    public static String randomRapidAPIKey() {
        return FAKER.regexify("[a-z0-9]{50}");
    }

    public static String randomInternetHost() {
        return FAKER.internet().url();
    }
}
