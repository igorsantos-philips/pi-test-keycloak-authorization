package com.philips.integrations.rename_the_package;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.RoutesBuilder;
import org.junit.jupiter.api.Test;

import com.philips.integrations.pitestkeycloak.SecretKeyGeneratorConnector;
import com.philips.integrations.pitestkeycloak.ExampleRoute;
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

        properties.setProperty("integrationname.connector.port", "9090");
        properties.setProperty("integrationname.connector.path.secretkey", "generate-secret-key");
    }

    @Test   
    public void testConnector() throws Exception {
        Object result = template.requestBody("netty-http:http://localhost:9090/generate-secret-key", "{\"password\":\"teste-password\"}");//as the defined on setup properties
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
