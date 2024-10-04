package com.philips.integrations.pitestkeycloak;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.integrations.pitestkeycloak.model.EncryptionUtils;
import com.philips.integrations.pitestkeycloak.model.PasswordEntity;
import com.philips.tie.commons.Configs;
import com.philips.tie.commons.router.IntegrationRouteBuilder;

public class SecretKeyGeneratorConnector extends IntegrationRouteBuilder {
    private final ObjectMapper om = new ObjectMapper();
    @Override
    public void configure() throws Exception {

        Configs.addKeystore(getCamelContext(), "key_example", "classpath:key_example.jks", "bifrost");
        // from("netty-http:https://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.secretkey}}?ssl=true&sslContextParameters=#key_example")
        from("netty-http:http://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.secretkey}}?ssl=false&securityConfiguration.realm=karaf")
                .id("endpoint-secretkey-generator")
                .process(this::doSomething);
    }
    private void doSomething(Exchange exchange) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonMappingException, JsonProcessingException {
        String body = exchange.getIn().getBody(String.class);
        String clientPassword = om.readTree(body).get("password").asText();
        String secretKey = EncryptionUtils.getKeyFromPassword(clientPassword);
        PasswordEntity passwordEntity = new PasswordEntity(clientPassword,secretKey);
        exchange.getMessage().setBody(om.writeValueAsString(passwordEntity));
    }
}
