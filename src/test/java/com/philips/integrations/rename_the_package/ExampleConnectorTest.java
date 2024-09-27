package com.philips.integrations.rename_the_package;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.RoutesBuilder;
import org.junit.jupiter.api.Test;

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
public class ExampleConnectorTest extends IntegrationTestSupport {

    /**
     * Set the properties your integration needs directly on camel context. 
     */
    @Override
    protected void doPreSetup() throws Exception {
        super.doPreSetup();

        properties.setProperty("integrationname.connector.port", "9999");
        properties.setProperty("integrationname.connector.path", "example");
    }

    @Test   
    public void testConnector() throws Exception {
        Object result = template.requestBody("netty-http:http://localhost:9999/example", "testing");//as the defined on setup properties
        assertEquals("{ \"example\" : \"testing > hello world\" }", new String((byte[]) result));
    }
 
    /**
     * Create all routes needed for the test
     */
    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[] {
            new ExampleConnector(),
            new ExampleRoute()
        };
    }
}
