# Introduction

This Twitter application allows users to post, show and delete tweets on
Twitter via Command-Line. The application sends the requests via Twitter
`REST API` and the responses are retrieved through the `HttpClient`, the `OAuth
1.0` is used for authentication. The application uses `DAO`and is packaged using Maven
dependencies and deployed using `Docker`. The testing was performed using `Junit` and `Mockito`.


# Quick Start

1. Maven
   Clone the repository and navigate to the twitter root directory and execute
```
mvn clean package -DskipTests
```
This will create a jar and then execute the jar file with the command line arguments.

2. Docker
   To run the app on Docker, first of all, pull the Docker image

```
docker pull payalgupta98558/twitter
```

After that run the image using the following command:
```
docker run -rm /
-e consumerKey="consumerKey goes here" /
-e consumerSecret="consumerSecret goes here" /
-e accessToken="accessToken goes here" /
-e tokenSecret="tokenSecret goes here" /
payalgupta98558/twitter "post|show|delete" [options]"
```

- To `Post` a Tweet
```
docker run -rm /
-e consumerKey="consumerKey goes here" /
-e consumerSecret="consumerSecret goes here" /
-e accessToken="accessToken goes here" /
-e tokenSecret="tokenSecret goes here" /
payalgupta98558/twitter post "tweet" latitude:longitude
```

- To `Show` Tweets
```
docker run -rm /
-e consumerKey="consumerKey goes here" /
-e consumerSecret="consumerSecret goes here" /
-e accessToken="accessToken goes here" /
-e tokenSecret="tokenSecret goes here" /
payalgupta98558/twitter show tweet_id
```
- To `Delete` Tweets
```
docker run -rm /
-e consumerKey="consumerKey goes here" /
-e consumerSecret="consumerSecret goes here" /
-e accessToken="accessToken goes here" /
-e tokenSecret="tokenSecret goes here" /
payalgupta98558/twitter delete s[id1, id2]
```


# Design
## UML diagram
![alt](https://github.com/jarviscanada/jarvis_data_eng_PayalGupta/blob/develop/core_java/twitter/assets/twitter_app_UML.png)

The application has 4 main components:
- **TwitterCLIApp**:
  This initializes the components and dependencies that are passed through the
  command line. It communicated with the controller and returns the relevant response
  based on the passed argument. --


- **TwitterController**:
  The controller layer parses the CLI arguments to the corresponding functions and
  validates the number of inputs and types. If the inputs are formatted correctly then
  it passes to the Service layer for further processing. --


- **TwitterService**:
  The Service layer handles the `Business Logic` and calls the DAO layer in to interact with the
  underlying storage which is Twitter REST API. The service layer first validates the business logic
  like the length of the tweet, status id format and if the validations pass then it passes this data
  to the DAO.--


- **TwitterDAO**:
  The Data Access layer handles models which are implemented with POJOs. The TwitterDAO
  accepts DAO from the Service layer and extracts the relevant data and interacts with
  the corresponding data resources. The DAO calls the HttpHelper to send HTTP requests and get the HTTP responses with a given URI in form of JSON using JSON parser.

## Models
The application has the following models:
- Tweet
- Entities
- Hashtags
- UserMentions
- Coordinates

## Spring
The project uses Spring for dependency management as managing the dependencies manually could be hard.
The Spring Boot was implemented to automate dependencies using annotations to signify the beans. The
following approaches were used:
- TwitterCLIBean uses `@Bean` annotation to indicate dependencies.
- TwitterCLIComponentScan automatically scans the entire project for the dependencies by using
  `@Autowired` annotation to inject dependency through the constructor.
- TwitterCLISpringBoot, Spring Boot is an extension of the Spring Framework and hence eliminates the configuration setup. In this, the `@SpringBootApplication(scanBasePackages)` was used to automatically configure the Spring.

# Test
The application was tested by creating `integration tests` for each component to ensure
the expected working. The `JUnit 4` was used to implement the integration tests using assertions to
test the expected result and the actual result.

The `unit testing` was performed to test the class without the dependencies so that to isolate the
working of a specific class and ensure that it works as expected. `Mockito` was used to stub/mock the
inputs from the other components.

## Deployment
The project was deployed using `Docker` and pushed to `DockerHub` for easy distribution. The following
steps were followed to Dockerize the project:
- Ran the `mvn clean package -DskipTests` command to package the project and create a jar.
- After this, the application was pushed to the DockerHub and now the DockerHub image can be pulled
  to run the project.

# Improvements
- Automatically intercept the location rather than the user giving it while posting the tweet.
- Allow users to filter the tweet using other criteria than just the id.
- An option to display multiple tweets than just one tweet specific to the id.
