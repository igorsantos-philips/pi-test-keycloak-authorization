package com.philips.integrations.pitestkeycloak.components.keycloak;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.support.DefaultProducer;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.http.HttpStatus;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philips.integrations.pitestkeycloak.components.keycloak.exceptions.KeycloakComponentException;
import com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations.model.ExtendedTrustManager;
import com.philips.integrations.pitestkeycloak.components.keycloak.tokenValidations.model.KeycloakTokenEntity;

public class KeycloakProducer extends DefaultProducer {
    private KeycloakEndpoint endpoint;
    private static final String AUTHORIZATION = "Authorization";
    private Exchange exchange;
    private final ObjectMapper om = new ObjectMapper();
    private String keycloakServerURL;

    public KeycloakProducer(KeycloakEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        this.exchange = exchange;
        String authorizationToken = exchange.getIn().getHeader(AUTHORIZATION, String.class);
        String token = authorizationToken.split(" ")[1].trim();
        String[] roles = endpoint.getRolesEndpoint();
        String realm = endpoint.getRealm();
        String keycloakHost = endpoint.getKeycloakHost();
        this.keycloakServerURL = keycloakHost + "/auth/realms/" + realm;
        String body = String.valueOf(exchange.getMessage().getBody());
        body=body.concat("\n\"active\":\""+validateToken(token, roles,  realm, keycloakServerURL)+"\"");
        exchange.getMessage().setBody(body);

    }

    private boolean validateToken(String token, String[] rolesEndpoint, String realm, String keycloakServerURL) {
        // Load RSA256 public key from keycloakServer
        RSAPublicKey rsaPublicKey = loadRSA256PubKey();
        
        Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, null); 
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(keycloakServerURL)
                .build();
        
        try {
            // Validate Expired Time
            DecodedJWT jwt = verifier.verify(token);
            String decodedPayload = new String(Base64.getDecoder().decode(jwt.getPayload()));
            String rolesToken = om.readTree(decodedPayload).get("realm_access").get("roles").toString();
            if (!Stream.of(rolesEndpoint).allMatch(rolesToken::contains)) {
                String forbiddenMessage = "The credential don´t have the right role to access the service";
                Message message = this.getErrorResponse(HttpStatus.SC_FORBIDDEN, "Forbidden", forbiddenMessage);
                throw new KeycloakComponentException(forbiddenMessage, message);
            }
            return true;
        } catch (JsonProcessingException e) {
            Message message = getErrorResponse(HttpStatus.SC_BAD_REQUEST, "Bad Request",
                    "The token doesn't have 'realm_access.roles'");
            throw new KeycloakComponentException(e, e.getMessage(), message);
        } catch(com.auth0.jwt.exceptions.TokenExpiredException e){
            String UnauthorizedMessage = "Unauthorized - Token has Expired";
            Message message = this.getErrorResponse(HttpStatus.SC_UNAUTHORIZED, "Unauthorized", UnauthorizedMessage);
            throw new KeycloakComponentException(e, UnauthorizedMessage, message);
        }
    }

    private RSAPublicKey loadRSA256PubKey() {
        String keycloakRealmRSA256PubKey = getKeycloakPublicKey();
        // Decode Base64 to get bytes
        byte[] keyBytes = Base64.getDecoder().decode(keycloakRealmRSA256PubKey);

        // Create X509EncodedKeySpec with the key bytes
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        // Get RSA KeyFactory instance
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            // Generate RSA PublicKey from spec
            PublicKey publicKey = keyFactory.generatePublic(spec);
            // Cast to RSAPublicKey
            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            // return
            return rsaPublicKey;
        } catch (NoSuchAlgorithmException e) {
            Message message = getErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error",
                    "Internal Server Error");
            throw new KeycloakComponentException(e, "The Public Key Algorithm not found in the server", message);
        } catch (InvalidKeySpecException e) {
            Message message = getErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error",
                    "Internal Server Error");
            throw new KeycloakComponentException(e, "The Public Key specifications are wrong", message);
        }
    }

    private String getKeycloakPublicKey() {
        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        
        trustManagersParameters.setTrustManager(new ExtendedTrustManager());

        TrustManager[] trustAllCerts = new TrustManager[]{new ExtendedTrustManager()};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            // HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // create a client
            HttpClient client = HttpClient.newBuilder()
            .sslContext(sc)
            .build();
            // create a request
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(this.keycloakServerURL))
                    .header("accept", "application/json")
                    .build();
            
            // use the client to send the request
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String body = response.body();
            KeycloakTokenEntity keycloakTokenEntity = om.readValue(body, KeycloakTokenEntity.class);
            // the response:
            return keycloakTokenEntity.getPublicKey();
        } catch (IOException | InterruptedException | NoSuchAlgorithmException | KeyManagementException e) {
            Message message = getErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error",
                    "Internal Server Error");
            throw new KeycloakComponentException(e, "The Public Key specifications are wrong", message);
        }

    }

    private Message getErrorResponse(int httpResponseCode, String httpResponseCodeMessage, String messageResponse) {
        Message message = exchange.getMessage();
        message.setHeader(Exchange.HTTP_RESPONSE_CODE, httpResponseCode);
        Map<String, String> errorResponseMap = new HashMap<>();
        errorResponseMap.put("error", httpResponseCodeMessage);
        errorResponseMap.put("message", messageResponse);
        message.setBody(httpResponseCodeMessage);
        return message;
    }

}
