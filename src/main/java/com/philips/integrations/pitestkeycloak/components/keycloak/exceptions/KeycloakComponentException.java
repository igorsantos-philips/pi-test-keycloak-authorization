package com.philips.integrations.pitestkeycloak.components.keycloak.exceptions;

import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;

public class KeycloakComponentException extends RuntimeCamelException{
    private Message messageResponse;
    public KeycloakComponentException(){}
    public KeycloakComponentException(Exception e, String message){
        super(message,e);
    }
    public KeycloakComponentException(Exception e, String message, Message messageResponse) {
        super(message,e);
        this.messageResponse = messageResponse;
    }
    public KeycloakComponentException(String message, Message messageResponse) {
        super(message);
        this.messageResponse = messageResponse;
    }
    public Message getMessageResponse() {
        return messageResponse;
    }

}
