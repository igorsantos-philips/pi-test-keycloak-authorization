package com.philips.integrations.pitestkeycloak.components.keycloak;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;;
@UriEndpoint(firstVersion = "1.0.15", scheme = "pi-keycloak", title = "PI-Keycloak", syntax="pi-keycloak:keycloak-host", category = {Category.SECURITY})
public class KeycloakEndpoint extends DefaultEndpoint {
    @UriPath(name = "keycloakHost")
    @Metadata(
        required = true, 
        description = "The Keycloak server, with comunication protocol, IP or name server and port.")
    private String keycloakHost;
    @UriParam(name = "realm")
    @Metadata(
        required = true, 
        description = "The Keycloak realm how will manager the service access.")    
    private String realm;
    @UriParam(name = "clientId")
    @Metadata(
        required = false, 
        description = "Client Id which identifies the server (needed to generate a token to the service access another service.")    
    private String clientid;
    @UriParam(name = "clientSecret")
    @Metadata(
        required = false, 
        description = "Client Secret which identifies the server (needed to generate a token to the service access another service.")    
    private String clientSecret;
    @UriParam(name = "rolesEndpoint")
    @Metadata(
        required = true, 
        description = "The endpoint roles.")    
    private String[] rolesEndpoint;

    public KeycloakEndpoint(String uri, KeycloakComponent keycloakComponent) {
        super(uri,keycloakComponent);
    }

    public void setKeycloakHost(String keycloakHost) {
        this.keycloakHost = keycloakHost;
    }
    public String getKeycloakHost(){
        return this.keycloakHost;
    }

    public String getRealm() {
        return realm;
    }

    public String getClientid() {
        return clientid;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String[] getRolesEndpoint() {
        return rolesEndpoint;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setRolesEndpoint(String rolesEndpoint) {
        this.rolesEndpoint = rolesEndpoint.split(",");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'createConsumer'");
    }

    @Override
    public Producer createProducer() throws Exception {
        return new KeycloakProducer(this);
    }
}
