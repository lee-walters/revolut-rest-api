# Revolut Assignment

xxx

# Overview

xxx

# Running The Application

To test the application run the following commands

* To package the app, run the following from the root dropwizard directory

        mvn package

* To setup the h2 database run

        java -jar target/revolut-rest-api-1.0.jar db migrate revolut.yml

* To run the server run

        java -jar target/revolut-rest-api-1.0.jar server revolut.yml

* Endpoints 

    GET     /account-holders (io.dropwizard.lee.revolut.resources.AccountHolderResource)
    POST    /account-holders (io.dropwizard.lee.revolut.resources.AccountHolderResource)
    
    GET     /account-management (io.dropwizard.lee.revolut.resources.AccountManagementResource)
    PUT     /account-management/create-new (io.dropwizard.lee.revolut.resources.AccountManagementResource)
    POST    /account-management/transfer (io.dropwizard.lee.revolut.resources.AccountManagementResource)
    GET     /account-management/{accountNumber} (io.dropwizard.lee.revolut.resources.AccountManagementResource)
    
    GET     /accounts (io.dropwizard.lee.revolut.resources.AccountResource)
    POST    /accounts (io.dropwizard.lee.revolut.resources.AccountResource)
    
    GET     /transactions (io.dropwizard.lee.revolut.resources.TransactionResource)
    POST    /transactions (io.dropwizard.lee.revolut.resources.TransactionResource)

* To put a new account-holder into the application

	curl -H "Content-Type: application/json" -X PUT -d '{"fullName":"Joe Bloggs","email":"j.bloggs@gmail.com"}' http://localhost:8080/account-management/create-new
	
	open http://localhost:8080/account-management
