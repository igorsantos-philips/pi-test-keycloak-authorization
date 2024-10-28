package com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeycloakTokenEntity {
    private String realm;
    @JsonProperty("public_key")
    private String publicKey;
    @JsonProperty("token-service")
    private String tokenService;
    @JsonProperty("account-service")
    private String accountService;
    @JsonProperty("tokens-not-before")
    private int tokensNotBefore;
    public String getRealm() {
        return realm;
    }
    public void setRealm(String realm) {
        this.realm = realm;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String public_key) {
        this.publicKey = public_key;
    }
    public String getTokenService() {
        return tokenService;
    }
    public void setTokenService(String tokenService) {
        this.tokenService = tokenService;
    }
    public String getAccountService() {
        return accountService;
    }
    public void setAccountService(String accountService) {
        this.accountService = accountService;
    }
    public int getTokensNotBefore() {
        return tokensNotBefore;
    }
    public void setTokensNotBefore(int tokensNotBefore) {
        this.tokensNotBefore = tokensNotBefore;
    }
    
}
