# API for household budget management

This is a project I made for the *4th Alura Backend Challenge*.
Its an *API* for household budget management developed in *Java* using the *Spring Framework*. I used a *MySQL* database for development, *H2* for testing and deployed the application in *Heroku* using *Docker*.
For the tests I used *JUnit 5*.
It has also features *JWT* authentication.

The application consists in *CRUD* functionalities to manage income, expenses and has a monthly summary.

Base url: https://alura-budget.herokuapp.com/

## Endpoints
The only endpoint you can access without being authenticated is the one for authentication:

### Login
Make a POST request to ```/auth``` passing the following parameters:
```json
"username": "user",
"password": "@herokuTest"
```
You will receive a *JWT* token that lasts 15min.

### Income
Supports GET, POST, PUT and DELETE methods to ```/receitas```.
Contains the following fields:
```json
"Id", //Id (Auto generated)
"Data", //Date (Format: dd/MM/yyyy)
"Descricao", //Description
"Valor" //Value
```

### Expenses
Supports GET, POST, PUT and DELETE methods to ```/despesas```.
Contains the following fields:
```json
"Id", //Id (Auto generated)
"Data", //Date (Format: dd/MM/yyyy)
"Descricao", //Description
"Valor", //Value
"Categoria" // Enum(Alimentação, Saúde, Moradia, Transporte, Educação, Lazer, Imprevistos, Outras)
```
### Monthly Summary
Make a GET request to
```/resumo/{year}/{month}```

E.g.: https://alura-budget.herokuapp.com/resumo/2022/8

It will return an JSON containing the total amount of income, expenses, the balance and the total expense by category.