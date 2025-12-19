package com.mekongfarm.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Service mã hóa và xác thực mật khẩu (Simple SHA-256)
 */
public class PasswordService {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Hash mật khẩu với salt
     */
    public static String hash(String password) {
        try {
            // Generate salt
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String saltStr = Base64.getEncoder().encodeToString(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
            String hashStr = Base64.getEncoder().encodeToString(hashedBytes);

            // Return salt:hash
            return saltStr + ":" + hashStr;
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Xác thực mật khẩu
     */
    public static boolean verify(String password, String hashedPassword) {
        try {
            if (hashedPassword == null || !hashedPassword.contains(":")) {
                // Plain text password (legacy)
                return password.equals(hashedPassword);
            }

            String[] parts = hashedPassword.split(":");
            if (parts.length != 2)
                return false;

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];

            // Hash input with same salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
            String inputHash = Base64.getEncoder().encodeToString(hashedBytes);

            return storedHash.equals(inputHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Kiểm tra password đã hash chưa
     */
    public static boolean isHashed(String password) {
        return password != null && password.contains(":") && password.length() > 40;
    }
}
