# GiftCertificates application

Crud applications for creating gift certificates with tags

## Author: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

### Technologies that I used on the project:

* Java 17
* Gradle 8.0
* Lombok plugin 8.0.1
* Postgresql
* Spring-boot 3.0.6
* Spring-boot-starter-data-jpa
* Spring-boot-starter-web
* Spring-boot-starter-validation
* Liquibase
* Swagger
* Spring-boot-starter-test
* Testcontainers-Postgresql-1.18.0

### Instructions to run application locally:

1. You must have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/),
   and [Postgresql](https://www.postgresql.org/download/) installed (P.S.: You can deploy postgresql in
   a [Docker](https://hub.docker.com/_/postgres) container)
2. You need [Docker](https://www.docker.com/products/docker-desktop/) for integration test with testcontainers
3. In Postgresql you have to create a database with name "gift_certificates". DDL: "CREATE DATABASE gift_certificates"
4. In [application.yaml](src/main/resources/application.yaml) enter your username and password from your
   local postgresql in line №4, №5
5. Run [GiftCertificatesApplication.java](src/main/java/ru/clevertec/ecl/giftcertificates/GiftCertificatesApplication.java).
Liquibase will create the required tables and fill them with default values.
6. Application is ready to work.

### Unit tests

1. Tests have been written with 100% coverage of services and controllers
2. Integration tests for repository with testcontainers are also written
3. You can run the tests for this project, by at the root of the project executing:

```
./gradlew test
```

### Documentation

To view the API Swagger documentation
[GiftCertificateSwagger](src/main/java/ru/clevertec/ecl/giftcertificates/swagger/GiftCertificateSwagger.java),
[TagSwagger](src/main/java/ru/clevertec/ecl/giftcertificates/swagger/TagSwagger.java),
[UserSwagger](src/main/java/ru/clevertec/ecl/giftcertificates/swagger/UserSwagger.java),
[OrderSwagger](src/main/java/ru/clevertec/ecl/giftcertificates/swagger/OrderSwagger.java).
Also, you can start the application and see:

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Demonstration SQL execution (explain):
Plan for query in [TagRepository](src/main/java/ru/clevertec/ecl/giftcertificates/repository/TagRepository.java).
findTheMostWidelyUsedWithTheHighestCost() method: Get the most widely used tag of a user with the highest cost of 
all orders.

| QUERY PLAN                                                                                                                                                               |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Limit  \(cost=57.70..57.71 rows=1 width=352\) \(actual time=0.038..0.040 rows=1 loops=1\)                                                                                |
| -&gt;  Sort  \(cost=57.70..57.91 rows=81 width=352\) \(actual time=0.038..0.039 rows=1 loops=1\)                                                                         |
| Sort Key: \(sum\(o.price\)\) DESC                                                                                                                                        |
| Sort Method: top-N heapsort  Memory: 25kB                                                                                                                                |
| -&gt;  HashAggregate  \(cost=56.29..57.30 rows=81 width=352\) \(actual time=0.033..0.035 rows=3 loops=1\)                                                                |
| Group Key: t.id                                                                                                                                                          |
| Batches: 1  Memory Usage: 32kB                                                                                                                                           |
| -&gt;  Nested Loop  \(cost=4.81..55.88 rows=81 width=352\) \(actual time=0.021..0.028 rows=3 loops=1\)                                                                   |
| -&gt;  Nested Loop  \(cost=4.67..40.62 rows=81 width=40\) \(actual time=0.018..0.024 rows=3 loops=1\)                                                                    |
| Join Filter: \(gc.id = gct.gift\_certificate\_id\)                                                                                                                       |
| -&gt;  Nested Loop  \(cost=4.52..37.48 rows=7 width=48\) \(actual time=0.016..0.018 rows=2 loops=1\)                                                                     |
| -&gt;  Nested Loop  \(cost=4.37..36.16 rows=7 width=40\) \(actual time=0.014..0.015 rows=2 loops=1\)                                                                     |
| -&gt;  Nested Loop  \(cost=0.15..21.30 rows=1 width=40\) \(actual time=0.009..0.010 rows=1 loops=1\)                                                                     |
| -&gt;  Seq Scan on orders o  \(cost=0.00..13.12 rows=1 width=48\) \(actual time=0.005..0.005 rows=1 loops=1\)                                                            |
| Filter: \(user\_id = 2\)                                                                                                                                                 |
| Rows Removed by Filter: 3                                                                                                                                                |
| -&gt;  Index Only Scan using users\_pkey on users u  \(cost=0.15..8.17 rows=1 width=8\) \(actual time=0.003..0.003 rows=1 loops=1\)                                      |
| Index Cond: \(id = 2\)                                                                                                                                                   |
| Heap Fetches: 1                                                                                                                                                          |
| -&gt;  Bitmap Heap Scan on orders\_gift\_certificate ogc  \(cost=4.22..14.76 rows=9 width=16\) \(actual time=0.004..0.004 rows=2 loops=1\)                               |
| Recheck Cond: \(orders\_id = o.id\)                                                                                                                                      |
| Heap Blocks: exact=1                                                                                                                                                     |
| -&gt;  Bitmap Index Scan on orders\_gift\_certificate\_pkey  \(cost=0.00..4.22 rows=9 width=0\) \(actual time=0.002..0.002 rows=2 loops=1\)                              |
| Index Cond: \(orders\_id = o.id\)                                                                                                                                        |
| -&gt;  Index Only Scan using gift\_certificate\_pkey on gift\_certificate gc  \(cost=0.14..0.19 rows=1 width=8\) \(actual time=0.001..0.001 rows=1 loops=2\)             |
| Index Cond: \(id = ogc.gift\_certificate\_id\)                                                                                                                           |
| Heap Fetches: 2                                                                                                                                                          |
| -&gt;  Index Only Scan using gift\_certificate\_tag\_pkey on gift\_certificate\_tag gct  \(cost=0.15..0.34 rows=9 width=16\) \(actual time=0.001..0.002 rows=2 loops=2\) |
| Index Cond: \(gift\_certificate\_id = ogc.gift\_certificate\_id\)                                                                                                        |
| Heap Fetches: 3                                                                                                                                                          |
| -&gt;  Index Scan using tag\_pkey on tag t  \(cost=0.14..0.19 rows=1 width=320\) \(actual time=0.001..0.001 rows=1 loops=3\)                                             |
| Index Cond: \(id = gct.tag\_id\)                                                                                                                                         |
| Planning Time: 1.005 ms                                                                                                                                                  |
| Execution Time: 0.076 ms                                                                                                                                                 |
