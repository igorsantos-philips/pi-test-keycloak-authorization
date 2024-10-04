package com.philips.integrations.pitestkeycloak.model;

public class EncryptioStringEntity {
    private String secretKey;
    private String decryptedString;
    private String encryptedString;
    public EncryptioStringEntity(){}
    public EncryptioStringEntity(String secretKey, String decryptedString, String encryptedString) {
        this.secretKey = secretKey;
        this.decryptedString = decryptedString;
        this.encryptedString = encryptedString;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    public String getDecryptedString() {
        return decryptedString;
    }
    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }
    public String getEncryptedString() {
        return encryptedString;
    }
    public void setEncryptedString(String encryptedString) {
        this.encryptedString = encryptedString;
    }

}
