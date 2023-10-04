package com.api.gateway.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.inject.Singleton;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * RSA ENCRYPTION / DECRYPTION METHODS
 */

@Singleton
public class RSAUtils {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);
    private static final String ALGORITHM = "RSA/None/OAEPWITHSHA-256ANDMGF1PADDING";
    String keypassword = "test";

    /**
     * Encrypt the text value
     */
    public  String encryptMessage(String plainText) {
        try {
            java.security.Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey());
            return Base64.getEncoder().
                    encodeToString(cipher.doFinal(plainText.getBytes()));
        }catch (Exception e){
            logger.error("RSAUtils.encrypt - {}",e.getMessage());
        }
        return null;
    }

    /**
     * Decrypt text value
     */
    public String decryptMessage(String encryptedText) {
        try {
            java.security.Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey());
            return new String(cipher.
                    doFinal(Base64.getDecoder().decode(encryptedText)));
        }catch (Exception e){
            logger.error("RSAUtils.decrypt - {}", e.getMessage());
        }
      return null;
    }

    /**
     * Get public key
     */
    public PublicKey getPublicKey() throws java.io.IOException {
        return this.readKeyPair().getPublic();
    }

    /**
     * Get private key
     */
    public PrivateKey getPrivateKey() throws java.io.IOException {
        return this.readKeyPair().getPrivate();
    }

    /**
     * Read key pair from private.pem file
     */
    public KeyPair readKeyPair() throws IOException {
        PEMParser pemParser = new PEMParser(new InputStreamReader(AppProperties.class.getResourceAsStream("/private.pem")));
        Object object = pemParser.readObject();
        PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(keypassword.toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair kp;
        if (object instanceof PEMEncryptedKeyPair) {
            logger.info("Encrypted key - we will use provided password");
            kp = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
        } else {
            logger.info("Unencrypted key - no password needed");
            kp = converter.getKeyPair((PEMKeyPair) object);
        }
        return kp;
    }
}
