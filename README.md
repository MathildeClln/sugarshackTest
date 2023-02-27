# sugarshackTest

## Introduction

This project is the backend part of the Maplr Sugar Shack technical test.
The goal of this test was to create a web application for a sugar shack, handling the selling and stocking of maple syrup.

The complete instructions can be found at [https://github.com/Maplr-Community/Sugar-Shack-test-maplr](https://github.com/Maplr-Community/Sugar-Shack-test-maplr).


This project uses the following technologies and tools: 
- Java 19
- [SpringBoot](https://spring.io/projects/spring-boot) 
- [Maven](https://maven.apache.org/)
- [JUnit](https://junit.org/junit5/) for unit tests
- An [H2](http://h2database.com/html/main.html) embedded database and the API-testing tool [Postman](https://www.postman.com/home) for end-to-end and integration tests.

## Architecture

In the instructions was given an open-api interface contract.
According to this contract, I created a multi-layer web application described in details below.

### The Model Layer
Inside the model layer, the modelization of the data is defined, where each class represent one table of the database.
From the instructions, five Data Transfer Objects were defined: `MapleSyrupDto`, `CatalogueItemDto`, `CartLineDto`, `OrderLineDto`, `OrderValidationResponseDto`.

<p align="center">
<img src="https://github.com/MathildeClln/sugarshackTest/blob/main/src/main/resources/img/dtoclasses.png" height=30% width=30%>
</p>

In order not to repeat information in the database, I decided to model these objects into the following classes.

<p align="center">
<img src="https://github.com/MathildeClln/sugarshackTest/blob/main/src/main/resources/img/modelclasses.png" height=30% width=30%>
</p>

- A `Product` class, which represents a Maple Syrup product.
- A `Stock` class, which stores the current stock or maximum quantity of a product.
- An `OrderLine` class, which contains the ordered quantity of a product.

The Stock and OrderLine tables both only have one business-relevant field, which is not necessarily a good practice.
However, we can imagine that in a real-life context, an OrderLine or a Stock could have more relevant fields (for example a user for an OrderLine or a storage location for a Stock).
Besides, the stock management, the order management, and the product management are all different business processes and I find it better to similarly separate the tables.


### The Repository Layer
The repository layer communicates with the model layer and defines the different operations needed to access the data.
As such, there is one repository for each of the tables in the model layer:
- A `ProductRepository`,
- A `StockRepository` ,
- An `OrderLineRepository`. 

### The Controller Layer
The controller layer defines the APIs of the web application. According to the interface contract, there are three: `/cart`, one for `/product` and one for `/order`.
This is the reason why I created three controllers:
- A `CartController`
- A `ProductController`
- An `OrderControler`.

### The  Service Layer
The service layer implements the business logic of the application. It is the intermediary between the repository layer and the controller layer.

I decided to create three Services, based on the three controllers needed: 
- A `ProductService`, that handles the business logic behind the `getCatalogue` and `getProductInfo` methods.
- A `CartService`, that handles the business logic behing the `getCart`, `addToCart`, `removeFromCart` and `changeQty` methods.
- An `OrderService`, that handles the business logic behind the `placeOrder` method.

The following diagram shows the repository, service and controller classes.

<p align="center">
<img src="https://github.com/MathildeClln/sugarshackTest/blob/main/src/main/resources/img/controller-service-repo.png" height=70% width=70%>
</p>


## Exception Handling
Based on my understanding of the methods described in the interface contract, I decided to create two custom exceptions for this application.
- The `ProductNotFoundException` is thrown in the Service layer when a given `productId` does not correspond to an element in the database. 
In the controller layer, it corresponds to a `404 Not Found` error.
- The `InvalidQuantityException` is thrown in the Service layer when a given `quantity` is negative. In the controller layer, it corresponds to a `400 Bad Request` error.



## Test management
I used several levels of tests during the development of the web application, to ensure its proper functioning. 

### Unit tests
I created unit tests using JUnit and Mockito to test each layer separately (repository, service, controller) and mock the previous layer when necessary.
I tested positive and negative cases whenever possible, for each layer.

### Integration tests
Using Postman and an H2 embedded database, I also performed integration tests.
Running the application, I executed positive and negative cases again to ensure that all layers communicated well with each other and with the database. 

