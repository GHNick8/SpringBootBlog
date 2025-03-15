package com.techsphere.techsphere.security;

import java.util.random.RandomGenerator;

public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int DEFAULT_LENGTH = 10;

    public static String generateRandomString(int length) {
        RandomGenerator random = RandomGenerator.getDefault(); // Java 17 Random
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Generated String: " + generateRandomString(DEFAULT_LENGTH));
    }
}
