# Revolut Assignment

xxx

# Overview

xxx

# Running The Application

To test the application run the following commands.

* To package the app, run the following from the root dropwizard directory.

        mvn package

* To setup the h2 database run.

        java -jar target/revolut-rest-api-1.0-SNAPSHOT.jar db migrate revolut.yml

* To run the server run.

        java -jar target/revolut-rest-api-1.0-SNAPSHOT.jar server revolut.yml

* To hit the xxx endpoint 

	http://localhost:8080/xxx

* To post data into the application.

	curl -H "Content-Type: application/json" -X POST -d '{"xxx":"yyy","aaa":"bbb"}' http://localhost:8080/zzz
	
	open http://localhost:8080/yyy
