package SecureKeeper.service;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final SecretKeySpec secretKey;
    private final IvParameterSpec iv;

    public EncryptionService(
        @Value("${encryption.key}") String base64Key,
        @Value("${encryption.iv}") String base64Iv
    ) {
        // Decode Base64 key and IV
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        byte[] ivBytes = Base64.getDecoder().decode(base64Iv);

        this.secretKey = new SecretKeySpec(keyBytes, "AES");
        this.iv = new IvParameterSpec(ivBytes);
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            return new String(cipher.doFinal(decodedBytes));
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}