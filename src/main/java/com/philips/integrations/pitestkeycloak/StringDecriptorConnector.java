package com.philips.integrations.pitestkeycloak;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.X509TrustManager;

import org.apache.camel.Exchange;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.integrations.pitestkeycloak.model.EncryptionUtils;
import com.philips.tie.commons.router.IntegrationRouteBuilder;

public class StringDecriptorConnector extends IntegrationRouteBuilder {
    private final ObjectMapper om = (new ObjectMapper()).setSerializationInclusion(Include.NON_EMPTY);
    // @BeanInject
    // private KeycloakAuthentication keycloakAuthentication;
    @Override
    public void configure() throws Exception {

        // changeTrustHttpComponent();
        // NettyConfig.addBootstrapConfiguration(getCamelContext(), "netty_secret_key", "key_example", "classpath:key_example.jks", "bifrost");
        // from("netty-http:https://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.decrypt}}?ssl=true&sslContextParameters=#key_example")
        from("netty-http:http://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.decrypt}}?ssl=false")
                .id("endpoint-decryptor")
                // .process(this::validateClientToken)
                .process(this::doSomething);
    }
    private void doSomething(Exchange exchange) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonMappingException, JsonProcessingException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        String bodyRequest = exchange.getIn().getBody(String.class);
        String encryptedString = om.readTree(bodyRequest).get("encryptedString").asText();
        String secretKey = om.readTree(bodyRequest).get("secretKey").asText();
        String decryptedString = EncryptionUtils.decryptString(encryptedString,secretKey);
        
        String messageResponse = om.createObjectNode().put("decryptedString", decryptedString).toString();
        exchange.getMessage().setBody(messageResponse);
    }
    private void validateClientToken(Exchange exchange){
        String token = exchange.getIn().getHeader("Authorization", String.class).substring(7);
        // keycloakAuthentication.validateToken(token);
        
    }
    private void changeTrustHttpComponent() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        
        trustManagersParameters.setTrustManager(new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() { return null; }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        });
        
        
        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setTrustManagers(trustManagersParameters);   
                              

        HttpComponent httpComponent = getCamelContext().getComponent("https", HttpComponent.class);
        httpComponent.setX509HostnameVerifier(new NoopHostnameVerifier());
        httpComponent.setSslContextParameters(sslContextParameters);

        // NettyHttpComponent nettyHttpComponent = getCamelContext().getComponent("netty-http", NettyHttpComponent.class);
        // nettyHttpComponent.setX509HostnameVerifier(new NoopHostnameVerifier());
        // nettyHttpComponent.setSslContextParameters(sslContextParameters);
    }

}
