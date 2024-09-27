package com.philips.integrations;

import org.apache.camel.CamelContext;

import com.philips.tie.commons.application.IntegrationConfiguration;

@org.apache.camel.Configuration
public class Configuration extends IntegrationConfiguration {
    
    @Override
    public void configure(CamelContext camelContext) throws Exception {
        super.configure(camelContext);
    }

}
