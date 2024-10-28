package com.philips.integrations.rename_the_package;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.RoutesBuilder;
import org.junit.jupiter.api.Test;

import com.philips.integrations.pitestkeycloak.ExampleRoute;
import com.philips.integrations.pitestkeycloak.SecretKeyGeneratorConnector;
import com.philips.tie.tests.IntegrationTestSupport;

/**
 * More content about tests on Camel can be found here:
 *  - https://camel.apache.org/manual/testing.html
 *  - https://camel.apache.org/components/4.4.x/others/test-junit5.html
 * 
 * The class IntegrationTestSupport is an internally class that act as helper to write tests. 
 * It already have a embedded QPID broker to replace Rabbitmq locally.
 * Also, properties defined on doPreSetup are automatically set on Camel context. 
 */
public class SecretKeyGeneratorConnectorTest extends IntegrationTestSupport {

    /**
     * Set the properties your integration needs directly on camel context. 
     */
    @Override
    protected void doPreSetup() throws Exception {
        super.doPreSetup();

        properties.setProperty("integrationname.connector.port", "8090");
        properties.setProperty("integrationname.connector.path.secretkey", "generate-secret-key");
        properties.setProperty("keycloak.realm.rsa256.pubkey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxTHvs8QyFRwXARn7DD+lwr8Y6FYfLT102jUCVVi11ZbYiqILDGQnqOyBOyhJcOPS3bBwRSQebf9A13a91Ips+o2ds9QdnRwk/w2ylaxivHtQPKxlkRV3vBi9uVCBq453kLYJavCsUwR6upZK5jvlrI4E0lTTIgUKB4hzbEP5LtlAbrPM1ZhVGV2TWmBDX5Z5fffDgorjO5vuaKDO55j2X4DM9yNaBfOR6EiUUey/PnI8S1ZXfCkR5ShU7A31o0aMv2rqEyek7UmV+SRn4vZxchv0d7BO4gDBPNFwDo3eIkF1Kv5IJCjORUpAQpxh6r67mgbjJUSQfRaepVLhSwAZUwIDAQAB");
        
    }

    @Test   
    public void testConnector() throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJvVFdJNUE5RWdQTmxDblA0WFJhZ2FxY1liamJIdGp0dnlZZVdCZlFPOEIwIn0.eyJleHAiOjE3Mjg5MzcwMjgsImlhdCI6MTcyODkzNjcyOCwianRpIjoiNDcxNzhhNzAtOTdhNi00NGZkLWFjNzAtNTI3NzBmNGM2ZTUwIiwiaXNzIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODM4My9hdXRoL3JlYWxtcy90ZXN0ZSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI1YzdlYTRhMy0wZDA3LTRlMDUtYjc0NS0zOWI2ZjgxY2QyNjgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzdHJpbmctZW5jeXB0aW9uLWJhY2tlbmQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6MzAwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidXNlci1hY3Rpdml0eSIsIm9mZmxpbmVfYWNjZXNzIiwic2VydmVyLWFjdGl2aXR5IiwiZGVmYXVsdC1yb2xlcy10ZXN0ZSIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRJZCI6InN0cmluZy1lbmN5cHRpb24tYmFja2VuZCIsImNsaWVudEhvc3QiOiIxNTAuMTUwLjAuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1zdHJpbmctZW5jeXB0aW9uLWJhY2tlbmQiLCJjbGllbnRBZGRyZXNzIjoiMTUwLjE1MC4wLjEifQ.QJp_q6Kg4TE8xcUIYTmKwfzUk14yakQHBHjX8XFSgkH_dbdnYamjFD40ZmTwUMoIGNPhSl_PafYXX2j1qZteVD_F4eHZvDBcKgrJm1n9SnnucAXBD0D4_wCEwi9DrOzftnsILlf5QOB2suoAu0mZ6BM0nfUkcDnBFTAzFr-1tQXYWO0R0fLBk6VhvTETxeyDdgUXQTPBWquauSTutxNyhnnEyUfrYiKnQubCoI8aNiZocGwyN-Y5thrGlcHRSH1SqWHRyK40Fr7Sczc8K0XhHQKQiKrjJ4HqTpE-iSIMn6BQK3mQt4PiVfLQtyS12_HzmYwiML6vFAFDwIMrNTnAlA");
        Object result = template.requestBodyAndHeader("netty-http:https://localhost:8090/generate-secret-key", "{\"password\":\"teste-password\"}",headers);//as the defined on setup properties
        System.out.println(new String((byte[]) result));
        assertEquals("{\"password\":\"teste-password\",\"secretKey\":\"LwRxIGF6IxXUfq5MMDQ+nIhMpj9mcExyUnHTerfgrks=\"}", new String((byte[]) result));
    }
 
    /**
     * Create all routes needed for the test
     */
    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[] {
            new SecretKeyGeneratorConnector(),
            new ExampleRoute()
        };
    }
}
