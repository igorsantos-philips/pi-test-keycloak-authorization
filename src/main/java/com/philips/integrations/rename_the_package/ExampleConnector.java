package com.philips.integrations.rename_the_package;

import com.philips.tie.commons.Configs;
import com.philips.tie.commons.router.IntegrationRouteBuilder;
import org.apache.camel.Exchange;

public class ExampleConnector extends IntegrationRouteBuilder {

    @Override
    public void configure() throws Exception {

        Configs.addKeystore(getCamelContext(), "key_example", "classpath:key_example.jks", "bifrost");
        from("netty-http:https://0.0.0.0:{{integrationname.connector.port:9000}}/{{integrationname.connector.path}}?ssl=true&sslContextParameters=#key_example")
                .id("example-connector")
                .setHeader("pi.type").constant("sync")
                .setProperty("test", constant("potato"))
                .process(this::doSomething)
                .toD("tie-router:example_event?timeout=5000&type=sync");

    }

    private void doSomething(Exchange exchange) {
        exchange.getMessage().setBody(exchange.getIn().getBody(String.class) + " > hello world");
    }

}
