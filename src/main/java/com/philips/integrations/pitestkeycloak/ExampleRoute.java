package com.philips.integrations.pitestkeycloak;

import com.philips.tie.commons.router.IntegrationRouteBuilder;

public class ExampleRoute extends IntegrationRouteBuilder {
    
    @Override
    public void configure() throws Exception {                        
        
        from("tie-router:example_event?consumersSync={{example.sync:1}}&consumersAsync={{example.async:1}}")
            .id("example-route")            
            .toD("logger:example_name?level=INFO&message=Example route called: ${body}")
            .setBody().simple("{ \"example\" : \"${body}\" }");
        
    }

}
