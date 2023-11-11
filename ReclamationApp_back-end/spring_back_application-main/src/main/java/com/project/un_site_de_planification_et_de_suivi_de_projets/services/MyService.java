package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.config.AESCrypter;
import com.project.un_site_de_planification_et_de_suivi_de_projets.config.OpenSSLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MyService {
    private final OpenSSLUtil openSSLUtil;
    private final AESCrypter aesCrypter;




    @Autowired
    public MyService(OpenSSLUtil openSSLUtil, AESCrypter aesCrypter) {
        this.openSSLUtil = openSSLUtil;
        this.aesCrypter = aesCrypter;
    }

    public String encryptFieldWithSymmetricKey(String plaintext) {
        String symmetricKeyPath = "C:/Users/alaed/OneDrive/Bureau/cii_3_éme_/Securité/mini_project_reclamation/symmetric_key.txt";
        try {
            return openSSLUtil.encryptWithSymmetricKey(plaintext, symmetricKeyPath);
        } catch (IOException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decryptFieldWithSymmetricKey(String encryptedData) {
        String symmetricKeyPath = "C:/Users/alaed/OneDrive/Bureau/cii_3_éme_/Securité/mini_project_reclamation/symmetric_key.txt";
        try {
            return openSSLUtil.decryptWithSymmetricKey(encryptedData, symmetricKeyPath);
        } catch (IOException e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    public String decrypt(String crypt, String Key){
        try {
            return openSSLUtil.decrypt(crypt, Key);
        } catch (IOException e) {
            throw new RuntimeException("Decryption failed", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] encryptField(String encryptedData, SecretKey key) {
        try {
            return AESCrypter.encryptField(encryptedData, key);
        } catch (Exception e) {
            throw new RuntimeException("runTime : ", e);
        }
    }
    public String decryptField(byte[] encryptedData, SecretKey key) {
        try {
            return AESCrypter.decryptField(encryptedData, key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
