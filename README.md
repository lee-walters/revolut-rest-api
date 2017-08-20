# Revolut Task

As the Spring framework was disallowed, for this task I decided to learn and adopt the Dropwizard framework, an open source Java framework for developing high performing RESTful applications. The Dropwizard framework has compatibility with supporting libraries:

* Embedded Jetty
* JAX-RS
* JSON
* Logging
* Hibernate
* Metrics

If the above list isn't enough of a reason for choosing this framework which helps satisfy the task requirements, other reasons include a quick project bootstrap, where all we require is a pom of dependencies to get going. Application metrics are also available which provides information such as request/response time, we just have to annotate the endpoint with @Timed. Productivity, every Dropwizard application has one main method program which starts the Jetty container.

## Running The Application

To test the application run the following commands

* To package the app, run the following from the root dropwizard directory

        mvn package

* To setup the h2 database run

        java -jar target/revolut-rest-api-1.0.jar db migrate revolut.yml

* To run the server run

        java -jar target/revolut-rest-api-1.0.jar server revolut.yml

## Endpoints 
    
```javascript
GET     /account-management
```
```javascript
PUT     /account-management/create-new
		JSON Body: {"fullName":"Joe Bloggs","email":"j.bloggs@gmail.com"}
```
```javascript
POST    /account-management/transfer
		JSON Body: {"from":"14589254","to":"78456325","amount":"200"}
```
```javascript
GET     /account-management/{accountNumber} 
```    
```javascript
GET     /transactions
```
