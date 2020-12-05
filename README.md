# Urldile
Link shortener with REST API.  
https://urldile.herokuapp.com/ 

#### Functionality:
- Converting links to unique short links of type - https://urldile.herokuapp.com/api/eatLink/Jh2 
- Checking a links for compliance with a pattern "(http/https)://www.mylink.com" or "(http/https)://www.mylink.subdomain.com" 
- Host existence check 
- Checking for a successful resource response (for example - if the resource returned not found the link will not be saved) 
- The short link is available for five days 

#### API:
- POST https://urldile.herokuapp.com/api/eatLink with JSON like: 

```
{ 
  "url": "https://mylongurl.com"
}  
```
will return CustomerLink JSON:

```
{
    "id": "5fc91b74f04df740361f35d6",
    "url": "https://mylongurl.com",
    "shortUrl": "aGo",
    "dateOfCreation": "2020-12-03T20:08:04",
    "dateOfDelete": "2020-12-08T20:08:04"
}
```
or in case of invalid request one of error response.

- GET https://urldile.herokuapp.com/api/eatLink/{shortUrl}

will make a redirect to url for which the short link was created.
- GET https://urldile.herokuapp.com/api/spit/{shortUrl}
will return CustomerLink JSON with the entered short url. 

#### Technologies:
- Data Base - MongoDB 
- Spring Boot (MVC, AOP, Data) 
- Hibernate (validator) 
- Lombok
- Angular
- Bootstrap
- Test - JUnit, Mockito (flapdoodle)
- Build project - Maven
- Deploy - Heroku, MongoAtlas
