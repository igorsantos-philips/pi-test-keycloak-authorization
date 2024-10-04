package com.philips.integrations.pitestkeycloak.model;

public class PasswordEntity {

    private String password;
    private String secretKey;
    public PasswordEntity(){}
    public PasswordEntity(String password,String secretKey){
        this.password=password;
        this.secretKey=secretKey;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    

}
