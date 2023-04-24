# GiftCertificates application

Crud applications for creating gift certificates with tags

## Author: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

### Technologies that I used on the project:

* Java 17
* Gradle 8.0
* Springframework: spring-webmvc:6.0.8
* Springframework: spring-orm:6.0.8
* Jakarta Servlet-api:6.0.0
* Hibernate-core:6.2.1.Final
* Jackson.core: jackson-databind:2.14.2
* Jackson.datatype: jackson-datatype-jsr310:2.14.2
* Lombok plugin 8.0.1
* Postgresql:42.6.0
* Zaxxer: HikariCP:5.0.1
* Slf4j-simple:2.0.7
* Mapstruct:1.5.3.Final
* Assertj-core:3.24.2
* Junit-jupiter-api:5.9.2
* Junit-jupiter-engine:5.9.2
* Junit-jupiter-params:5.9.2
* Mockito-core:5.3.1
* Mockito-junit-jupiter:5.3.1
* H2database:h2:2.1.214
* Springframework: spring-test:6.0.8

### Instructions to run application locally:

1. You must have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/), [Tomcat 10.1](https://tomcat.apache.org/download-10.cgi)
   and [Postgresql](https://www.postgresql.org/download/) installed (P.S.: You can deploy postgresql in
   a [Docker](https://hub.docker.com/_/postgres) container)
2. In Postgresql you have to create a database with name "gift_certificates". DDL: "CREATE DATABASE gift_certificates"
3. In [application.properties](src/main/resources/application.properties) enter your username and password from your
   local postgresql in line №3, №4
4. In the Intellij IDEA settings, Run -> Edit Configurations... you should put Tomcat 10.1. And in the Deployment column
   clear Application context
5. Run [ddl.sql](src/main/resources/sql/ddl.sql). It will create three tables with many-to-many relationship
6. Application is ready to work

### Unit tests

1. Unit tests have been written with 100% coverage of services
2. In memory H2 is used for integration tests . Hibernate creates tables by default and fills them with values
3. Integration tests for dao, also have 100% coverage
4. You can run the tests for this project, by at the root of the project executing:

```
./gradlew test
```

## Functionalities

In summary the application can:
***
***GiftCertificateController [giftCertificates.http](src/main/resources/giftCertificates.http)***
***

* **GET findAll | Finds all gift certificates**
* http://localhost:8080/gift_certificates
* Response example:

````json
[
  {
    "id": 1,
    "name": "Gift",
    "description": "Very Big",
    "price": 10.25,
    "duration": 3,
    "tags": [
      {
        "id": 1,
        "name": "Pepsi"
      },
      {
        "id": 2,
        "name": "Mirinda"
      }
    ],
    "create_date": "2023-04-24T21:57:03:165",
    "last_update_date": "2023-04-24T21:57:03:165"
  },
  {
    "id": 2,
    "name": "Salsa",
    "description": "Little",
    "price": 13.64,
    "duration": 5,
    "tags": [
      {
        "id": 3,
        "name": "7-Up"
      },
      {
        "id": 1,
        "name": "Pepsi"
      },
      {
        "id": 2,
        "name": "Mirinda"
      }
    ],
    "create_date": "2023-04-24T21:57:05:550",
    "last_update_date": "2023-04-24T21:57:11:140"
  }
]
```` 

* **GET findById | Finds one gift certificate by id**
* http://localhost:8080/gift_certificates/1
* Response example:

````json
{
  "id": 1,
  "name": "Gift",
  "description": "Very Big",
  "price": 10.25,
  "duration": 3,
  "tags": [
    {
      "id": 1,
      "name": "Pepsi"
    },
    {
      "id": 2,
      "name": "Mirinda"
    }
  ],
  "create_date": "2023-04-24T21:57:03:165",
  "last_update_date": "2023-04-24T21:57:03:165"
}
````

* Bad Request example:

````json
{
  "errorMessage": "GiftCertificate with ID 3 does not exist",
  "errorCode": "404 NOT_FOUND"
}
````

* **GET findAllWithTags | Finds all gift certificates by certain parameters(all params are optional and can be used in
  conjunction)**
* http://localhost:8080/gift_certificates/findAllWithTags?tagName=Pepsi&part=Litt&sortBy=date&order=desc
* If there are no parameters, it works as a find all
* Param tagName - search by tag name. Example: Pepsi
* Param part - search by part of name or description. Example: Little
* Param sortBy - sort by date or by name (name default). Example: sortBy=date or sortBy=name
* Param order - order by (ASC default). Example: order=desc or order=asc
* You can use all the parameters or several or none
* Response example:

````json
[
  {
    "id": 4,
    "name": "Salsa",
    "description": "Little",
    "price": 13.64,
    "duration": 5,
    "tags": [
      {
        "id": 3,
        "name": "7-Up"
      },
      {
        "id": 1,
        "name": "Pepsi"
      },
      {
        "id": 2,
        "name": "Mirinda"
      }
    ],
    "create_date": "2023-04-24T22:11:41:090",
    "last_update_date": "2023-04-24T22:11:53:458"
  }
]
````

* **POST save | Saves one gift certificate**
* http://localhost:8080/gift_certificates
* Request example:

````json
{
  "name": "Gift",
  "description": "Very Big",
  "price": 10.25,
  "duration": 3,
  "tags": [
    {
      "name": "Coca-cola"
    },
    {
      "name": "Sprite"
    }
  ]
}
````

* Response example:

````json
{
  "id": 1,
  "name": "Gift",
  "description": "Very Big",
  "price": 10.25,
  "duration": 3,
  "tags": [
    {
      "id": 1,
      "name": "Coca-cola"
    },
    {
      "id": 2,
      "name": "Sprite"
    }
  ],
  "create_date": "2023-04-24T22:25:42:020",
  "last_update_date": "2023-04-24T22:25:42:020"
}
````

* **PUT update | Updates one gift certificate**
* http://localhost:8080/gift_certificates
* Request example:

````json
{
  "id": 4,
  "name": "Salsa",
  "description": "Little",
  "price": 13.64,
  "duration": 5,
  "tags": [
    {
      "id": 1,
      "name": "Pepsi"
    },
    {
      "id": 2,
      "name": "Mirinda"
    },
    {
      "id": 3,
      "name": "7-Up"
    }
  ],
  "create_date": "2023-04-24T22:11:41:090",
  "last_update_date": "2023-04-24T22:11:53:458"
}
````

* Bad Request example:

````json
{
  "errorMessage": "GiftCertificate with ID 3 does not exist",
  "errorCode": "404 NOT_FOUND"
}
````

* **DELETE delete | Deletes one gift certificate by id**
* http://localhost:8080/gift_certificates/2
* Request example:

````json
"GiftCertificate with ID 3 was successfully deleted"
````

* Bad Request example:

````json
{
  "errorMessage": "There is no GiftCertificate with ID 2 to delete",
  "errorCode": "404 NOT_FOUND"
}
````

***
***TagController [tags.http](src/main/resources/tags.http)***
***

* **GET findAll | Finds all tags**
* http://localhost:8080/tags
* Response example:

````json
[
  {
    "id": 1,
    "name": "Pepsi"
  },
  {
    "id": 2,
    "name": "Mirinda"
  },
  {
    "id": 3,
    "name": "7-Up"
  },
  {
    "id": 4,
    "name": "Sprite"
  },
  {
    "id": 5,
    "name": "Coca-cola"
  }
]
````

* **GET findById | Finds one tag by id**
* http://localhost:8080/tags/2
* Response example:

````json
{
  "id": 2,
  "name": "Mirinda"
}
````

* Bad Request example:

````json
{
  "errorMessage": "Tag with ID 67 does not exist",
  "errorCode": "404 NOT_FOUND"
}
````

* **POST save | Saves one tag**
* http://localhost:8080/tags
* Response example:

````json
{
  "id": 11,
  "name": "Surprise"
}
````

* **PUT update | Updates one tag**
* http://localhost:8080/tags
* Response example:

````json
{
  "id": 3,
  "name": "Banana"
}
````

* Bad Request example:

````json
{
  "errorMessage": "Tag with ID 67 does not exist",
  "errorCode": "404 NOT_FOUND"
}
````

* **DELETE delete | Delete one tag by id**
* http://localhost:8080/tags/6
* Response example:

````json
"Tag with ID 6 was successfully deleted"
````

* Bad Request example:

````json
{
  "errorMessage": "There is no Tag with ID 6 to delete",
  "errorCode": "404 NOT_FOUND"
}
````
