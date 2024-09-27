package com.philips.integrations;

import com.philips.tie.commons.application.StartApplication;

/**
 * DO NOT CHANGE
 * <p>
 * Starts all the integrations on the project, all the java and yaml files and
 * include on the classpath all the files on the resources folder.
 * <p>
 * To use in development with a memory broker to replace RabbitMQ or AmazonMQ,
 * you can set the property 'mode' to 'dev' in the application.properties file.
 * <p>
 * If you want to use RabbitMQ or AmazonMQ, you can erase the property 'mode' in the application.properties file.
 */
public class Application extends StartApplication {

    public static void main(String[] args) throws Exception {
        start(args);
    }

}