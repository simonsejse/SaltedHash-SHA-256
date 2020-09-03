package com.simonsejse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {

    public static final String algorithm = "SHA-256";

    public static void main(String[] args) {
        new Main().test();
    }

    public void test(){
        String password = "HelloWorld";
        byte[] salt = randomSaltSupplier.get();
        String hashed = hashGeneratorFunc.apply(password, salt);
        System.out.println(hashed);

    }

    public BiFunction<String, byte[], String> hashGeneratorFunc = (password, salt) -> {
        byte[] hash = new byte[password.length()];
        try
        {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();  // * Resets the digest for further use. In case uninitialised components in use.
            md.update(salt); //Adding salt data
            hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return bytesToHex(hash);

    };

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public Supplier<byte[]> randomSaltSupplier = () -> {
        byte[] salt = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    };
}
