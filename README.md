# Guideline to use the template

1. Start a new repository using this template as base.
2. On settings.gradle change the 'integration_name' to the defined integration name.
3. Rename the package src/main/java/com/philips/integrations/**rename_the_package** to some significant package name related to the content of your integration.
4. Compile the project running the gradle task 'build'.
5. Structure of the template:
    - **gradle/libs.versions.toml** : All the dependencies your project needs.
        - **core**: It have all the camel dependencies, your integration have access to a great a mount of [Apache Camel components](https://https://camel.apache.org/components/4.4.x/), you just need to add as dependency is this file.
        - **log**: Provide the logs during the development as on production.
        - **tie**: Kepts compability with the integrations wrote on TIE V1 and V2 version. Used to send the integrations to the Rabbitmq, provide features  like data transformations and reprocessing. The documentation of theses components could be found on the main project of [TIE]([https://](https://github.com/philips-internal/tie-components/)).
        - Any other dependency the integration needs could be added here.
    - **build.gradle** : Contains the repositories and dependencies applied to the  project. Also you can create your own tasks and plugins for your project. By default it already have a **jar** task which create a fat jar to run your integration on others environments.
    - **settings.gradle** : Contains the integration name. **CHANGE** the name of the integration here, the generated jar file will have this name.
    - **src/main/java/com/philips/integrations/rename_the_package**: All your   connectors, routes and logic should be written below this package (*don't forget  to rename it*). 
      To create a new route, just extends the class with org.apache.camel.builder.  RouteBuilder and override the **configure** method. Apache Camel will     automatically search for these classes and add to the camel context.
      Use the ExampleConnector.java and ExampleRoute.java as examples, do not forget to remove them after.
    - **src/test/java/com/philips/integrations**: Write all yours unit tests here.
    - **src/main/java/com/philips/integrations/Application.java**: To run or debug your integration. Do not change this file.
    - **src/main/resources/camel**: Its possible to write routes using the YAML language. The sources should be here will be automatically added to the camel context.
    - **src/main/resources/application.properties**: Put here all the configurations your integration be able to handle. On production, customer will write a file like this to provided the configurations of the integration. **This file will be removed on the fat jar.**
        - integrationname.connector.port: only used for example connector, should be removed.
        - integrationname.connector.path: only used for example connector, should be removed.
        - router.host: host address of rabbitmq, configuration needed by tie-router component.
        - router.port: port of rabbitmq, configuration needed by tie-router component.
        - router.username=: username for rabbitmq, configuration needed by tie-router component.
        - router.password: password for rabbitmq, configuration needed by tie-router component.
        - router.secure: true if connection is secure (AMQPS), configuration needed by tie-router component.
        - camel.main.basePackageScan: Camel will search from this package all the routes created (DO NOT CHANGE THIS ONE).
    - **src/main/resources/log4j2.properties**: Log level, location and format configurations. **This file will be removed on the fat jar.**
    - **src/main/resources/**: Any other file included on the resources will be kept on the final jar. TIE transformations files like example.jsonjson, example. datamapping and any others should be placed here at development phase.
    - **src/test/com/philips/integrations/rename_the_package**: Contains the tests for the integration. Rename the package to the integration name.
    - **distribution**: Files to generate the final artifacts to the customer.
    - **distribution/docker**: Base file to generate the docker image to this integration for the customer. If something specific is needed, could be changed here. For reference, the pi-base image source code could be found [here](https://github.com/philips-internal/pi-ci/blob/main/pi-base/Dockerfile) 
    - **distribution/layer.json**: This file will have all the parameters and configurations of the integration. Add the configurations needed to the integrations on this project. By default you will find the configurations to the tie-router component, these should not be changed:
      - router.host
      - router.port
      - router.username
      - router.password
      - router.secure
      - router.virtualhost
      - router.recovery-auto
      - router.recovery-interval
      - router.addresses
6. Configure the application.properties file to access a shared Rabbitmq instance or run one locally. We suggest [the docker image](https://hub.docker.com/_/rabbitmq), the *management* version to have a web access to the queues. The exchange and queues used by tie-router are created automatically.
7. Run/Debug executing the Application.java file. The configurations and routes will be loaded automatically by Camel Main.
   
Below is a suggestion of a read.me file you could use on your repository.

# integration_name

Description of the integration.

## Configurations

These configurations are available for this integration and must be defined on application.properties file.

```text
integrationname.connector.port=9090
integrationname.connector.path=example

#rabbitmq connection
router.host = localhost   
router.port = 5671
router.username = guest
router.password = guest
router.secure = true
```

To get values from environment variables in the operation system, set the value to the following format:

```text
some_property={{env:<environment_name>}}
```

### Environment variable accepted

The available environment variables for this integration are:

```text
#name to identify the integration
name=<name of integration>

#activates telemetry log
telemetry-enabled=false
telemetry-name=philips-integrations
```

## Running from IDE

Provide the necessary configurations for the integration on files:

- **src/main/resources/application.properties** : related to the integration.
- **src/main/resources/log4j2.properties**: related to the log.

*The files above will **not** be exported in the jar file.*

Execute the main java class **com.philips.integrations.Application.java**.

### VsCode

Launch content:

```json
{
    "version": "0.2.0",
    "configurations": [        
        {
            "type": "java",
            "name": "Application",
            "request": "launch",
            "mainClass": "com.philips.integrations.Application",
            "projectName": "integration_name"
        }
    ]
}
```

## Generate executable file

Run the command below, the jar file will be located on folders **./build/libs/** and **./distribution/docker/**.

```shell
gradle clean build jar
```

### Running the jar file

Make sure you have JBang with Apache Camel plugin installed:

- <https://www.jbang.dev/download/>
- <https://camel.apache.org/manual/camel-jbang.html>

Provide the necessary configurations for the integration on files:

- **application.properties** : related to the integration.
- **log4j2.properties**: related to the log.

Run the command:

```shell
camel run ./build/libs/file-all.jar application.properties log4j2.properties
```

### Testing

More content about tests on Camel can be found here:  
- [Apache Camel Test](https://camel.apache.org/manual/testing.html)
- [Apache Camel Junit](https://camel.apache.org/components/4.4.x/others/test-junit5.html)

The class **IntegrationTestSupport** is an internally class that act as helper to write tests. It already have a embedded QPID broker to replace Rabbitmq locally. The default port used by QPID is **10001**, it can be overridden in the field **port**.

**Properties** defined on **doPreSetup** are automatically set on Camel context. It will have the same result as those defined on application.properties, but specifically to the test running.