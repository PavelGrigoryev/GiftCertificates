# GiftCertificates application

Crud applications for creating gift certificates with tags

## Author: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

### Technologies that I used on the project:

* Java 17
* Gradle 8.0
* Lombok plugin 8.0.1
* Postgresql
* Spring-boot-starter-data-jpa
* Spring-boot-starter-web
* Spring-boot-starter-validation
* Liquibase
* Swagger
* Spring-boot-starter-test

### Instructions to run application locally:

1. You must have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/),
   and [Postgresql](https://www.postgresql.org/download/) installed (P.S.: You can deploy postgresql in
   a [Docker](https://hub.docker.com/_/postgres) container)
2. In Postgresql you have to create a database with name "gift_certificates". DDL: "CREATE DATABASE gift_certificates"
3. In [application.yaml](src/main/resources/application.yaml) enter your username and password from your
   local postgresql in line №4, №5
4. Run [GiftCertificatesApplication.java](src/main/java/ru/clevertec/ecl/giftcertificates/GiftCertificatesApplication.java).
Liquibase will create the required tables and fill them with default values.
5. Application is ready to work.

### Unit tests

You can run the tests for this project, by at the root of the project executing:

```
./gradlew test
```

## Documentation

To view the API Swagger documentation, start the application and see:

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)