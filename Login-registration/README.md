Once the server is up and running, go to postman and execute the below APIs:

1 - Add a user:
POST : http://localhost:8081/api/v1/registration/register

{
    "firstName" : "Elias",
    "lastName" : "Abi Atallah",
    "email" : "eabiatallah1@gmail.com",
    "password" : "pass",
    "confirmPassword" : "pass"
  
}

2 - Confirm the Token
GET : http://localhost:8081/api/v1/registration/confirm/xxxxxx-xxxxxx-xxxx-xxxx-xxxxx


Learning in this project:

1 - Basics of Spring Security 
2 - Basics of Spring Validation
3 - AOP, Advice custom exception
4 - Junit unit test example