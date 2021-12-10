# Banking System REST API
> This is a demo REST API for banking application.

## What does this API do?
This API provides following functionalities
- Register
    <br>
    User can register on this API i.e. create their account
- Get Balance
    <br>
    The user can get their account balance through the API
- Deposit amount
    <br>
    The user can deposit amount in their account through the API

- Withdraw amount
    <br>
    The user can withdraw amount in their account through the API

## What does this API not do?
- Actual transactions! This API does not integrate any payment gateways, so actual transactions cannot be performed.

## Accessing the API
> **Note:** All the requesting accessing the API with a request body `must` pass the request body in `application/json` format. Any other format would be rejected by the API.

### Documentation
Start the server and visit the following URL in browser to view Swagger UI API documentation

[http://localhost:3000/swagger-ui/index.html](http://localhost:3000/swagger-ui/index.html)


### Remote repository
View this API on [Gitlab.com](https://gitlab.com/faiyazmujawar/banking)