# Recipe Service!

### Getting started
This is a simple RESTful microservice based on Java and Spring Boot. Users can add/remove/edit their favorite recipes also they can search/filter based on the provided criteria. 

* Java 11
* Spring Boot 2.7.8
* PostgreSQL and flyway
* Spring Data JPA
* Validation
* Lombok
* Spring Boot Actuator
* JUnit 5 and Mockito

### How to run
First, you need a database for running this service. The database layer is provided by docker-compose. After cloning the project, just navigate to root folder and simply run:

`` docker-compose up -d ``

Then for running the service, you can use:

`` ./mvnw spring-boot:run  ``

Now, you can check application status which should be up and running by browsing [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

### How to use endpoint
There are 5 endpoints described in ``api-documentation.yml``, namely:
```
* GET  /api/v1/recipes
* GET  /api/v1/recipes/filter
* POST /api/v1/recipes
* PUT  /api/v1/recipes
* DELETE /api/v1/recipes/{id}
```
So you can create, update, read, filter all recipes for the specified user (in request header)
BTW! I skipped endpoint for managing users, but you can use these three user ids for using as userId in endpoints:
* mary@gmail.com
* abc@gmail.com
* xyz@gmail.com