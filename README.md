# AWS Wrapper Wildfly

POC for using AWS advanced JDBC wrapper with Wildfly datasource. 

Using Java 8, Spring 2.1.3, Wildfly 26.1.1, MySQL 8.0.33 and AWS advanced JDBC wrapper 2.3.7.

Crappy code, just for testing purposes.

## Wildfly locally

[Install Wildfly locally]( https://www.wildfly.org/get-started/), for this POC we are using Wildfly 26.1.1.Final.
It is the latest version at this time compatible with Java 8 (max Java 17).

Run wildfly with the following command:

```bash
./bin/standalone.sh
```

Create an admin user with the following command:

```bash
./bin/add-user.sh -m -u admin -p admin
```

Then access the admin console at http://localhost:9990/console/index.html by using the credentials you just created.

### Create datasource

Adapt the following according to your needs. We're using MySQL with `mysql-connector-j:8.0.33` and `aws-advanced-jdbc-wrapper:2.3.7`.

Before creating datasource, you need to deploy the driver module in Wildfly. Copy the `wildfly/modules` folder to the Wildfly installation folder.

Take care to have the following structure, otherwise it'll not work

```
└───wildfly
      └───modules
          ├───software
              ├───amazon
              │   └───jdbc
              │       └───main
              │       │   │───module.xml
              │       │   │───mysql-connector-j.jar
              │       │   └───aws-advanced-jdbc-wrapper.jar
```

Then, create a datasource in Wildfly. For that, you've to open CLI `./wildfly/bin/jboss-cli.sh -c` and copy/paste the following (with your adaptions):

```bash
/subsystem=datasources/jdbc-driver=aws-wrapper:add(driver-module-name=software.amazon.jdbc,driver-name=aws-wrapper,driver-class-name=software.amazon.jdbc.Driver)

/subsystem=datasources/data-source=MyPool:add(jndi-name=java:jboss/datasources/MyPool, driver-name="aws-wrapper", connection-url="jdbc:aws-wrapper:mysql://localhost:3326/myDb?wrapperLoggerLevel=ALL&wrapperPlugins=readWriteSplitting,failover,efm2")
/subsystem=datasources/data-source=MyPool:write-attribute(name=user-name, value=myUser)
/subsystem=datasources/data-source=MyPool:write-attribute(name=password, value=myPassword)
/subsystem=datasources/data-source=MyPool:write-attribute(name=validate-on-match, value=true)
/subsystem=datasources/data-source=MyPool:write-attribute(name=valid-connection-checker-class-name, value="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker")
/subsystem=datasources/data-source=MyPool:write-attribute(name=exception-sorter-class-name, value="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter")
/subsystem=datasources/data-source=MyPool:write-attribute(name=min-pool-size, value=5)
/subsystem=datasources/data-source=MyPool:write-attribute(name=max-pool-size, value=400)
/subsystem=datasources/data-source=MyPool:write-attribute(name=pool-prefill, value=true)
/subsystem=datasources/data-source=MyPool:write-attribute(name=idle-timeout-minutes, value=30)
```

Now, you're ready to deploy your application.

### Deploy the application

You can simply use the management console to deploy the application. Just click on `Deployments` and then `Add`.
Or you drag and drop the `.war` file to the `standalone/deployments` folder.

Run the following maven command to generate the `.war` file:

```bash
./mvnw clean package
```

Get the `.war` file at `target/aws-wrapper-wildfly.war` and deploy it.

## Test the application

When testing the Application against RDS, use SSM to port forward the RDS instance to your local machine.

## Wildfly on Docker

Set up your `.env` file based on the `.env.example` file. It must correspond to your RDS connection with the 
needed JDBC parameters.

Then, build the `.war` artefact with the following command:

```bash
./mvnw clean package
```

Run the following command to start the Wildfly container:

```bash
docker-compose build && docker-compose up
```

> [!IMPORTANT]
> I'm using another customer base image for Wildfly, for the sake of simplicity, the image
> `jboss/wildfly:10.1.0.Final` is used. You can change it to any image if you want.
