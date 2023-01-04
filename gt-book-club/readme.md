# Book Club Backend Service

## Development Environment
1. Java - 11
2. Maven - 3.6.3
3. MongoDB - 6.0.3

## Steps to Setup
1. open `config/application-dev.properties`
change `spring.data.mongodb.uri`, `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

2. build and run the project
```
mvn clean package
java -jar target/gt-book-club-0.0.1-SNAPSHOT.jar --spring.config.locations=config/ --spring.profiles.active=dev
```

The app will start running at <http://http://localhost:8091/>

The api documentation can be found at <http://localhost:8091/swagger-ui.html#>

## Project Folder Structure 
- The folder structure uses maven project folder structure. 
- The system log stores at `log` folder. and The property file is placed at `config` folder. 
- The `log` folder and `config` folder is for running and monitoring app in the production server.

## API design
- Use `Nouns` to describe URLs
- Describe resource functionality with `HTTP methods`
- Give meaningful feedback in HTTP Responses to help developers succeed

The app defines below api:
    
    POST /login
    POST /register

    POST /book
    DELETE /book/{id}
    PUT /book/{id}
    PUT /book/{id}/borrow
    PUT /book/{id}/return
    GET /books

    DELETE /deactivate/{email}
    GET /profile
    PUT /profile/{email}

## Code Implmentation
- This project uses spring boot framework for backend service development and spring security for authentication and authorization
- The requirement mentions only admin can add/register users. During the app startup, it will create a `default admin` account (email: `admin@email.com`, password: `123PASS`)
- Token based authentication (JWT) is implemented in this project
- This project I did not write much unit test code, as it is not stated in the assessment requirements explicitly

## Business Logic Assumption
- Two users can not share the same email during registration
- User email can not be updated after registration
- Book id is the only identifier. book name can be repeated.
- Book last borrower must match the user who return the book
- Book last borrower records the user email

## Scalability
- The application is designed as a stateless application. It is using JWT to manage user authentication and access control. So the application can scale without service disruption.
- In the production environment, use api gateway as the single entry point and load balancer to split api traffics to different service instances
- For DB scalability, we may consider to implement `replication`. Comparing to `sharding`, `replication` provides fault-tolerance. It is more recommended for production environment. 