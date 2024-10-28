package com.philips.integrations.pitestkeycloak.components.keycloak;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.apache.camel.PropertyInject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KeycloakAuthentication {
    @PropertyInject("{{keycloak.protocol:http}}://{{keycloak.host:localhost}}:{{keycloak.port:8383}}/auth/realms")
    private String SERVER_URL;
    @PropertyInject("{{keycloak.realm:bifrost}}")
    private String REALM;
    @PropertyInject("{{keycloak.oauth2.client_id}}")
    private String CLIENT_ID;
    @PropertyInject("{{keycloak.oauth2.client_secret}}")
    private String CLIENT_SECRET;
    @PropertyInject("{{keycloak.realm.rsa256.pubkey}}")
    private String rsa256PubKey;
    // private String AUTH_URL = SERVER_URL + REALM + "/protocol/openid-connect/token";
    private final ObjectMapper om = new ObjectMapper();

    public boolean validateToken(String token, String role) {
        try {
            Algorithm algorithm = Algorithm.RSA256(convertToRSAPublicKey(rsa256PubKey), null); // Use RSA256 public key here
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(SERVER_URL + REALM)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            String tokenPayload = jwt.getPayload();
            String roles = om.readTree(tokenPayload).get("realm_access.roles").asText();

            if (roles.contains(role)&& jwt.getExpiresAt().getTime() > System.currentTimeMillis()){
                return true;
            }
            return false;
        } catch (Exception exception) {
            return false;
        }
    }

    private RSAPublicKey convertToRSAPublicKey(String pemKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        try {
            // Remove headers, newlines, etc.
            pemKey = pemKey.replaceAll("\\n", "").replaceAll("\\r", "")
                                             .replace("-----BEGIN PUBLIC KEY-----", "")
                                             .replace("-----END PUBLIC KEY-----", "");

            // Decode Base64 to get bytes
            byte[] keyBytes = Base64.getDecoder().decode(pemKey);

            // Create X509EncodedKeySpec with the key bytes
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            
            // Get RSA KeyFactory instance
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            // Generate RSA PublicKey from spec
            PublicKey publicKey = keyFactory.generatePublic(spec);

            // Cast to RSAPublicKey
            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;

            // Print to confirm
            return rsaPublicKey;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
