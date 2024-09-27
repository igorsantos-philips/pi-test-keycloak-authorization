package com.philips.integrations.rename_the_package;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.builder.RouteBuilder;
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
public class ExampleRouteTest extends IntegrationTestSupport {

    @Test   
    public void testRoute() throws Exception {
        Object result = template.requestBody("tie-router:example_event?timeout=5000&type=sync", "testing");
        assertEquals("{ \"example\" : \"testing\" }", result);
    }

    /**
     * Create the single route needed for the test
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new ExampleRoute(); 
    }
 
}
