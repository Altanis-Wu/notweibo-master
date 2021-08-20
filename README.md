# Dockerised NotWeibo

Demo Spring Boot application with some pain points fixed / mitigated.

## Multiple Config Files

An issue people are having is getting the connection strings in their `application.propeties` files correct.
This is because we have two different configurations: one for local development and one for deploying to Docker as a container.
To avoid constanly changing the values in `application.properties` I created 2 separate files: [`application-prod.properties`](https://github.com/Bodomit/notweibo/blob/dockerise/src/main/resources/application-prod.properties) for the docker deployment, and [`application-dev.properties`](https://github.com/Bodomit/notweibo/blob/dockerise/src/main/resources/application-dev.properties) for local development. Each file has the correct connection string.

To select which profile to use when building the app, I added the two profiles to maven's [`pom.xml`](https://github.com/Bodomit/notweibo/blob/dockerise/pom.xml) file:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <spring.profiles.active>prod</spring.profiles.active>
        </properties>
    </profile>
</profiles>
```
This allows us to specify the profile (using the -P argument) when running maven commands. For example, `mvn -Pprod install` will run the maven install command with the "prod" config, wheras `mvn -Pdev ...` will use the `dev` config. This is very convienient when deploying and runnign maven commands in our jenkins pipeline.

Note. The following updates also need to be done for this to work:

- [`application.properties`](https://github.com/Bodomit/notweibo/blob/dockerise/src/main/resources/application.properties) must be updated to just `spring.profiles.active=@spring.profiles.active@`
- In the [`pom.xml`](https://github.com/Bodomit/notweibo/blob/dockerise/pom.xml) file, add the following under the `build` tag:
```xml
<resources>
    <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
    </resource>
</resources>
```

## Tests Causing builds to fail (Communication Error)

If you've implemented unit tests for the controller or repository objects, your builds might be failing because a database is not accessible at build / test time.
The "normal" way to handle this is to create an in-memory test database to run your tests against. 
However, that is far too advanced!
Instead, the best way to handle this for now is for maven to simply ignore those bad tests.

The following addition to the [`pom.xml`](https://github.com/Bodomit/notweibo/blob/dockerise/pom.xml) (under the 'plugins' tag) tells maven to ignore the tests causing the problems in NotWeibo. For your own app, only add the tests that cause the build to fail with the communication error.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>**/NotweiboApplicationTests.java</exclude>
            <exclude>**/*ControllerTests.java</exclude>
            <exclude>**/*RepositoryTests.java</exclude>
        </excludes>
    </configuration>
</plugin>
```
This is better than commenting out / deleting the tests, as the tests can still be ran locally from within Intellij.

## Communication Error after deploying to Docker (and the connection string is correct).

First of all, make sure you are rebuilding your docker images between changing the code / configuration and running the containers.

Next, make sure the app is waiting long enough so the database can get ready to accept connections.
I did this by using the `depends_on` field in [`docker-compose.yaml`](docker-compose.yaml), along with a 10 second sleep in [`Dockerfile-app`](Dockerfile-app), but there are other ways to do it too. For example, added a sleep to your jenkins pipeline.

## Jenkins Example

I've also included a simple [`Jenkinsfile`](Jenkinsfile) example. In it, the app is built and tested in a docker agent, before being deployed to docker.
The only difficult bit is the `stash` and `unstash`. These are required because the building occurs inside a docker agent (container) wheras I need to deploy the jar file outside of any container: we cannot call docker commands from inside a docker container. 
The `stash` and `unstash` simply store and retrieve the jar across the different stages, and the different agents.
If you want to use a docker agent in your own pipeline, ensure the Docker and Docker Pipeline plugins for jenkins are installed.

