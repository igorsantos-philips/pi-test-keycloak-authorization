package com.philips.integrations.pitestkeycloak.components.keycloak;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;



@Component("pi-keycloak")
public class KeycloakComponent extends DefaultComponent {
    public static final String REALM = "realm";
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String RSA_256_PUBLIC_KEY = "rsa256PubKey";
    public static final String ROLES_ENDPOINT = "rolesEndpoint";

    @Override
    protected Endpoint createEndpoint(String uri, String keycloakHost,Map<String, Object> parameters) throws Exception {
        KeycloakEndpoint endpoint = new KeycloakEndpoint(uri, this);
        endpoint.setKeycloakHost(keycloakHost);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}