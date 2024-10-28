package com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import java.util.HashMap;
import java.util.Map;

import com.philips.integrations.pitestkeycloak.components.keycloak.KeycloakEndpoint;
import com.philips.integrations.pitestkeycloak.components.keycloak.exceptions.KeycloakComponentException;
@SuppressWarnings("rawtypes")
public abstract class TokenValidation<T> {
    
    private TokenValidation nextValidation;
    private Exchange exchange;
    private KeycloakEndpoint endpoint;
    public TokenValidation(){}
    public TokenValidation( Exchange exchange, KeycloakEndpoint endpoint){
        this.exchange = exchange;
        this.endpoint = endpoint;
    }
    public TokenValidation(TokenValidation nextValidation, Exchange exchange, KeycloakEndpoint endpoint){
        this.nextValidation = nextValidation;
        this.exchange = exchange;
        this.endpoint = endpoint;
    }
    public TokenValidation validateToken(T token){
        this.tokenValidationRule(token);
        return nextValidation;

    }
    public Message getErrorResponse(int httpResponseCode, String httpResponseCodeMessage, String messageResponse ) {
        Message message = exchange.getMessage();
        message.setHeader(Exchange.HTTP_RESPONSE_CODE, httpResponseCode);
        Map<String, String> errorResponseMap = new HashMap<>();
        errorResponseMap.put("error", httpResponseCodeMessage);
        errorResponseMap.put("message", messageResponse);
        message.setBody(httpResponseCodeMessage);
        return message;
    }    
    protected abstract void tokenValidationRule(T token) throws KeycloakComponentException;
    public TokenValidation getNextValidation() {
        return nextValidation;
    }
    public Exchange getExchange() {
        return exchange;
    }
    public KeycloakEndpoint getEndpoint() {
        return endpoint;
    }

}
