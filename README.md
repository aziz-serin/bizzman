# bizzman

This app started as a side project for me to learn more about the spring framework and hibernete, as well as
practicing the development of a RESTFUL application.

The original plan was to use thymeleaf for the frontend application, and stay within the spring ecosystem. But then, I've decided to
turn this project into a REST app, and implement a frontend later using a javascript framework, as this will give me
an opportunity to learn more about javascript frontend frameworks.

## How to run this app

You can use 

`
mvn clean install 
`

to build the app, and then use the command

`
java -jar target/bizzman.war
`

from the root directory of the application to run it. 

To check if the application is alive, you can send a GET request to `/actuator/health/liveness` endpoint of the application.

If you want to run the tests, from the terminal you can run it by using the above method, or run them from your
choice of IDE.

### Docker

To build the docker image, run <br>

`
docker build . -t bizzman
`

from the root directory, then run the docker image with forwarding the port `8080` to your local machine.

## Dependencies

The dependencies are listed in the pom.xml. This app was developed using `java11` and maven version `3.8.6`
I have left two files in the repo,`mvnvm.properties` and `.java-version`. These files are used by mvnvm and jenv respectively,
which are both version managers. You can find more information about these really useful tools [mvnvm](https://mvnvm.org/)
and [jenv](https://www.jenv.be/).

## Contributions

This project was a great learning exercise for me about different technologies. Feel free to fork it and have a look at the code,
play around with it and have fun! Spring is very powerful and has a steep learning curve at first, but do not let it discourage you,
and have a go at it!