package com.philips.integrations.pitestkeycloak;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.camel.Exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.integrations.pitestkeycloak.model.EncryptionUtils;
import com.philips.tie.commons.router.IntegrationRouteBuilder;

public class StringEncriptorConnector extends IntegrationRouteBuilder {
    private final ObjectMapper om = new ObjectMapper();
    @Override
    public void configure() throws Exception {
        // changeTrustHttpComponent();
        // Configs.addKeystore(getCamelContext(), "key_example", "classpath:key_example.jks", "bifrost");
        // from("netty-http:https://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.encrypt}}?ssl=true&sslContextParameters=#key_example")
        from("netty-http:http://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path.encrypt}}?ssl=false")
                .id("endpoint-encryptor")
                .process(this::doSomething);
    }
    private void doSomething(Exchange exchange) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonMappingException, JsonProcessingException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        String bodyRequest = exchange.getIn().getBody(String.class);
        String decryptedString = om.readTree(bodyRequest).get("decryptedString").asText();
        String secretKey = om.readTree(bodyRequest).get("secretKey").asText();
        String encryptedString = EncryptionUtils.encryptString(decryptedString,secretKey);
        String messageResponse = om.createObjectNode().put("encryptedString", encryptedString).toString();
        exchange.getMessage().setBody(messageResponse);
    }
    //     private void changeTrustHttpComponent() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
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

    //     // NettyHttpComponent nettyHttpComponent = getCamelContext().getComponent("netty-http", NettyHttpComponent.class);
    //     // nettyHttpComponent.setX509HostnameVerifier(new NoopHostnameVerifier());
    //     // nettyHttpComponent.setSslContextParameters(sslContextParameters);
    // }

}
