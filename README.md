# Getting Started

### How to run
* [Make sure you have java 1.8, if not, please install following steps](https://medium.com/w-logs/installing-java-11-on-macos-with-homebrew-7f73c1e9fadfhttps://www3.ntu.edu.sg/home/ehchua/programming/howto/JDK_Howto.html)

* highly recommend you to install the [intelliJ IDE](https://www.jetbrains.com/idea/download) for the review purpose

* [import project from intellij](https://www.youtube.com/watch?v=wQyDk4Ji1Gk)

* [run springboot application in intellij](https://blog.jetbrains.com/idea/2018/04/spring-and-spring-boot-in-intellij-idea-2018-1/)

also you can run from your terminal, at the project root directory run the below command, please make sure port 8080 is opened.
* ./gradlew bootRun

you can also build and run artifact
* ./gradlew clean build
* java -jar build/libs/ride-allocation-service-0.0.1-SNAPSHOT.jar

### API Reference
after run application with above command, visit below link,
* [API Reference(Swagger UI)](http://localhost:8080/swagger-ui.html)

* once you visited, you need to sign up first with valid username(email), password and desired Role,
you will get token from response once you sign up, click `Authroize` button from right upper side of the page,
and paste the token with `Bearer ` prefix. now you can consume the APIs, token will be valid for 5minutes,
you can configure from ./src/main/resources/application.yml, security.jwt.token.expire-length

### Persistence, DDL
* RDBMS : SQLite
* DDL : ./src/main/resources/schema.sql

### Development stack
* Kotlin
* Spring Boot, Spring Security, Spring Data Jpa
* Swagger

### Service domain

* This service is for allocating rides provided by drivers to passengers upon their ride requests

* Users(passenger, driver and operator) can sign up with their email and password

* Users can sign in with their email and password

* passengers can request a ride with desired address to ride

* drivers can see list of ride requests

* drivers can respond to a ride request to provide a ride

* drivers cannot respond to a ride request which already responded

* operators can see list of all ride requests with status and request/response time by recently requested order
