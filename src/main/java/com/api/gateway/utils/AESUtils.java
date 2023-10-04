package com.api.gateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Singleton
public class AESUtils {
    private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final String IV = "010203040506";
    private static final String PASSWORD = "API_GATEWAY";

    /**
     *  Generate Secret Key
     */
    public SecretKey createKey(String password) throws  java.security.NoSuchAlgorithmException{

        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-512");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        return new SecretKeySpec(key, "AES");

    }

    /**
     *  AES Encryption
     */
    public String encryptionAES(String plainText) {
        byte[] data = plainText.getBytes(StandardCharsets.UTF_8);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = createKey(PASSWORD);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new GCMParameterSpec(TAG_LENGTH_BIT, IV.getBytes()));
            return Base64.getEncoder().encodeToString(cipher.doFinal(data));
        } catch (Exception ex) {
            logger.error("AESUtils.encrypt", ex);
        }
        return null;
    }

    /**
     *  AES Decryption
     */
    public String decryptionAES(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, createKey(PASSWORD),new GCMParameterSpec(TAG_LENGTH_BIT, IV.getBytes()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception ex) {
            logger.error("AESUtils.decryption", ex);
        }
        return "";
    }
}
