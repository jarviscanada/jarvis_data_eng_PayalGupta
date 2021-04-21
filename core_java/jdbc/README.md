# Introduction
The `JDBC application` allows the data retrieval from a retail database for the customers and 
their orders using Java from RDBMS through JDBC. The application implements the CRUD(Create Read 
Update Delete) operations using `Data Transfer Objects` and `Data Access Objects`. 
The psql instnace was provisioned using docker container and the dependencies were managed through Maven.

# Implementaiton
## ER Diagram
![alt text](https://github.com/jarviscanada/jarvis_data_eng_PayalGupta/blob/develop/core_java/jdbc/assets/jdbc_schema.PNG)

## Design Patterns
The `Data access Object(DAO)` pattern is an abstraction of data persistence and is considered closer to the 
underlying storage that is table-centric. The DAO matches the database tables which allow a more direct way 
to process data from storage.

`Repository pattern` is the layer between the Business layer and the Data layer. A repository uses DAO to fetch 
the data from the database and populate the domain object. It prepares the data from a domain object and send 
it to storage syatem using a DAO for persistence.

This project uses both the above mentioned design patterns. The OrderDAO class follows DAO pattern for handling
complex queries and the CustomerDAO class uses the repository pattern for simple queries to interact with the 
customer table.

# Test
I created some test data to populate customers and orders table in psql and then executed the application and 
compared the results using DBeaver.
