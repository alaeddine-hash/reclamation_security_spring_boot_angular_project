package com.project.un_site_de_planification_et_de_suivi_de_projets.config;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

@Component
public class AESCrypter {

    private static final String KEY_FILE_PATH = "C:/Users/alaed/OneDrive/Bureau/cii_3_éme_/Securité/aes_secret.key"; // Define the path to your key file

    public static SecretKey loadAESKey() {
        try {
           // Vérifie si le fichier de la clé existe
            if (!Files.exists(Paths.get(KEY_FILE_PATH))) {
                // Si le fichier n'existe pas, génère une nouvelle clé et la stocke
                generateAndStoreAESKey();
            }

            // Lit la clé du fichier
            byte[] encodedKey = Files.readAllBytes(Paths.get(KEY_FILE_PATH));
            // Décode la clé en utilisant Base64
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            // Retourne une instance de SecretKey à partir de la clé décodée
            return new SecretKeySpec(decodedKey, "AES");
        } catch (IOException e) {
            // Gère les erreurs d'E/S en lançant une exception RuntimeException
            throw new RuntimeException("AES key loading failed", e);
        }
    }

    public static void generateAndStoreAESKey() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] keyBytes = new byte[32];
            random.nextBytes(keyBytes);

            // Store the encoded key in a file
            String base64EncodedKey = Base64.getEncoder().encodeToString(keyBytes);
            Files.write(Paths.get(KEY_FILE_PATH), base64EncodedKey.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("AES key generation and storage failed", e);
        }
    }

    public static byte[] encryptField(String encryptedData, SecretKey key) {
        try {
            // Crée une instance de Cipher avec l'algorithme AES en mode CBC et le rembourrage PKCS5
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Initialise les paramètres d'initialisation (IV) avec un tableau vide de 16 octets
            AlgorithmParameterSpec ivParams = new IvParameterSpec(new byte[16]);

            // Initialise le chiffrement en mode de chiffrement avec la clé et les paramètres IV
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

            // Chiffre les données d'entrée (encryptedData) en utilisant UTF-8 et stocke le résultat dans encryptedBytes
            byte[] encryptedBytes = cipher.doFinal(encryptedData.getBytes(StandardCharsets.UTF_8));

            // Retourne les données chiffrées
            return encryptedBytes;
        } catch (Exception e) {
            // Gère les erreurs en lançant une exception RuntimeException
            throw new RuntimeException("Échec de chiffrement", e);
        }
    }


    public static String decryptField(byte[] encryptedData, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameterSpec ivParams = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            byte[] decryptedBytes = cipher.doFinal(encryptedData);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
