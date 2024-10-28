package com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations;

import java.util.stream.Stream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.integrations.pitestkeycloak.components.keycloak.KeycloakEndpoint;
import com.philips.integrations.pitestkeycloak.components.keycloak.exceptions.KeycloakComponentException;

public class RolesTokenValidation extends TokenValidation<String>{
    private ObjectMapper om = new ObjectMapper();
    public RolesTokenValidation( Exchange exchange, KeycloakEndpoint endpoint){
        super(exchange,endpoint);
    }

    @Override
    public void tokenValidationRule(String tokenPayload) throws KeycloakComponentException {
            String[] rolesEndpoint = this.getEndpoint().getRolesEndpoint();
            try {
                String rolesToken = om.readTree(tokenPayload).get("realm_access.roles").asText();
                if (!Stream.of(rolesEndpoint).allMatch(rolesToken::contains)) {
                    String forbiddenMessage = "The credential don´t have the right role to access the service";
                    Message message = this.getErrorResponse(HttpStatus.SC_FORBIDDEN, "Forbidden", forbiddenMessage);
                    throw new KeycloakComponentException(forbiddenMessage,message);
                }
            } catch (JsonProcessingException e) {
                Message message = getErrorResponse(HttpStatus.SC_BAD_REQUEST,"Bad Request","The token doesn't have 'realm_access.roles'");
                throw new KeycloakComponentException(e, e.getMessage(),message);
            }          
    }

}
