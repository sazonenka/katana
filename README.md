## Overview

**katana** is a web-based editor for constraint configs, formerly stored in XML files.

## Tools

1. JDK 6
2. Maven 2.2.1
3. MySQL 5.5
4. Tomcat 6

## MySQL setup

Execute the following scripts in *katana-web/src/main/db* directory under **root** user:

1. setup\_database\_and\_user.sql (over **information\_schema** database)
2. create\_schema.sql (over **katana** database)
3. populate\_database.sql (over **katana** database)

## Configure source code

Adjust JDBC properties in the *katana-web/src/main/resources/jdbc.properties* file
for your particular DB environment.

## Running locally

Execute the following command on the **root** level to build the entire project:

    mvn clean install

Execute the following command on **katana-web** level to run the application:

    mvn tomcat:run-war

Point your browser to `http://localhost:8080/katana`

## Supported browsers

+ Firefox
+ Chrome
