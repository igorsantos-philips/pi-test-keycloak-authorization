package com.philips.integrations.pitestkeycloak;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.integrations.pitestkeycloak.model.EncryptionUtils;
import com.philips.integrations.pitestkeycloak.model.PasswordEntity;
import com.philips.tie.commons.router.IntegrationRouteBuilder;

public class SecretKeyGeneratorConnector extends IntegrationRouteBuilder {
    private final ObjectMapper om = new ObjectMapper();
    // @BeanInject("keycloak")
    // private KeycloakAuthentication keycloakAuthentication;
    
    // public SecretKeyGeneratorConnector(KeycloakAuthentication keycloakAuthentication){
    //     this.keycloakAuthentication = keycloakAuthentication;
    // }
    @Override
    public void configure() throws Exception {
        // onException(null);
        // changeTrustHttpComponent();
        // NettyConfig.addBootstrapConfiguration(getCamelContext(), "netty_secret_key", "key_example", "classpath:key_example.jks", "bifrost");
        // Configs.addKeystore(getCamelContext(), "key_example", "classpath:key_example.jks", "bifrost");
        // from("netty-http:https://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.secretkey}}?bootstrapConfiguration=#netty_secret_key")
        from("netty-http:http://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.secretkey}}?ssl=false")
              .id("endpoint-secretkey-generator")
              .toD("pi-keycloak:https://localhost:8383?realm=teste&rsa256PubKey={{keycloak.realm.rsa256.pubkey}}&roles=user-activity,server-activity")
            // .process(this::verifyAuthenticationAndAuthorization)
            
            // .setBody(simple("client_id={{keycloak.oauth2.client_id}}&amp&amp;token=${header.Authorization}"))
            // .to("netty-http:https://{{keycloak.host}}:{{keycloak.port}}/auth/realms/{{keycloak.realm}}/protocol/openid-connect/token/introspect")
            // .to("netty-http:https://localhost:8383/auth/realms/teste/protocol/openid-connect/token/introspect")
           
            // .choice()
                // .when().jsonpath("active")
                    
                    .process(this::doSomething);
                // .otherwise()
                    // .process(this::unathorizedToken);
    }
    private void verifyAuthenticationAndAuthorization(Exchange ex){
        String authorizationToken = ex.getIn().getHeader("Authorization",String.class);
        // keycloakAuthentication.validateToken(authorizationToken, "{{integrationname.connector.access.roles:default}}");

    }
    private void doSomething(Exchange exchange) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonMappingException, JsonProcessingException {

        String body = exchange.getIn().getBody(String.class);
        String clientPassword = om.readTree(body).get("password").asText();
        String secretKey = EncryptionUtils.getKeyFromPassword(clientPassword);
        PasswordEntity passwordEntity = new PasswordEntity(secretKey);
        exchange.getMessage().setBody(om.writeValueAsString(passwordEntity));
 
    }
    private void unathorizedToken(Exchange exchange){
        // exchange.getIn().setHeader("HTTP/1.1", "401 Unauthorized");
        exchange.getMessage().setBody("{\"message\":\"401 Unauthorized\"}");

    }
    // private void changeTrustHttpComponent() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    //     TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        
    //     trustManagersParameters.setTrustManager(new X509TrustManager() {
    //         @Override
    //         public X509Certificate[] getAcceptedIssuers() { return null; }

    //         @Override
    //         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

    //         @Override
    //         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    //     });
        
    //     SSLContextParameters sslContextParameters = new SSLContextParameters();
    //     sslContextParameters.setTrustManagers(trustManagersParameters);                         

    //     HttpComponent httpComponent = getCamelContext().getComponent("https", HttpComponent.class);
    //     httpComponent.setX509HostnameVerifier(new NoopHostnameVerifier());
    //     httpComponent.setSslContextParameters(sslContextParameters);

        // NettyHttpComponent nettyHttpComponent = getCamelContext().getComponent("netty-http", NettyHttpComponent.class);
        // nettyHttpComponent.setX509HostnameVerifier(new NoopHostnameVerifier());
        // nettyHttpComponent.setSslContextParameters(sslContextParameters);
    // }
}
