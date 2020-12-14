# Scooter Rental
## Information

*This is a RESTful web service, final project at Senla training courses.*

## Installation

Before deploying this application, you need to have installed Maven. Also you need MySql (with spatial extensions) and
Tomcat servers running.
* Run the script: **database/install-schema-script** to create and fill the Scooter Rental Database schema on your MySql
 server.
* Run the script: **deploy-script-scooter_rental** to pack and deploy the WAR file on your Tomcat server.

You can change the database settings at **scooter-rental-web/src/main/resources/application.properties**. Also you can
change Tomcat plugin configuration at **pom.xml**.


## Implementation information

* **Programming language:** Java 
* **Java version:** "11.0.7"

## Contacts

* **Author:** Dzmitry Yeutukhovich
