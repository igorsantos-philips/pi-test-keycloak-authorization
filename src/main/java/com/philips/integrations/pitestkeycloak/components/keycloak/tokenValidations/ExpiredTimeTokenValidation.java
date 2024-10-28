package com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.http.HttpStatus;

import com.philips.integrations.pitestkeycloak.components.keycloak.KeycloakEndpoint;
import com.philips.integrations.pitestkeycloak.components.keycloak.exceptions.KeycloakComponentException;

public class ExpiredTimeTokenValidation extends TokenValidation<Long>{
    
    @SuppressWarnings("rawtypes")
    public ExpiredTimeTokenValidation(TokenValidation tokenValidation,Exchange exchange, KeycloakEndpoint endpoint){
        super(tokenValidation,exchange, endpoint);
    }

    @Override
    public void tokenValidationRule(Long tokenTime) throws KeycloakComponentException {
            if (tokenTime <= System.currentTimeMillis()) {
                String UnauthorizedMessage="Unauthorized";
                Message message = this.getErrorResponse(HttpStatus.SC_UNAUTHORIZED, "Unauthorized", UnauthorizedMessage);
                throw new KeycloakComponentException(UnauthorizedMessage,message);               
            }
    }

}
