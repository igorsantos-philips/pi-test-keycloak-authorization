package com.philips.integrations.pitestkeycloak.model;

public class PasswordEntity {

    private String secretKey;
    
    public PasswordEntity(){}
    public PasswordEntity(String secretKey){
        this.secretKey=secretKey;
    }
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    

}
