package com.example.matbloggen.utility;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";

    public static String generateRandomPassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Längden måste vara större än 0.");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }
        System.out.println(password.toString());

        return password.toString();
    }
}