# __About:__
This project implemented via `Spring and Hibernate`.

Database crates via Flyway.

Testcontainers are used to test the repository layer

Mockito are used to test the service layer
 
___
# __Project setup steps__
___
* ```git clone <username>/gift-certification.git ```
* go to the folder ```cd 'your project folder'```
* paste project url from the first step
* open the project in your IDE ```File->Open->'your project folder'```

# __To ```run``` application you need:__

* open folder with project in the terminal ```cd 'your project folder'```
* enter ```gradle clean build```
* checkout to the ```feature-2-migrate-to-hibernate``` branch
* get the database up in Docker by command ```docker compose up -d --build```
* run application from your IDE using Tomcat

___
# __Steps for work with application:__

__Work with certificates:__
* For getting all certificates from database you should go to ```GET.../gifts/>```
* For add certificate to the database you should go to ```POST.../gifts/add``` 
and pass the following parameters in the body of the request:
   ```
   {
       "name": "name",
       "description": "description",
       "price": <price>,
       "duration": <duration>,
       "tags": [
           "Gifts",
           "Or somthing else"
       ]
   } 
  ```
 
* For update information about certificate you should go to ```PUT.../gifts/update```
and pass the following parameters in the body of the request:
  ```
  {
        "id": <id>
        "name": "name",
        "description": "description",
        "price": <price>,
        "duration": <duration>,
        "tags": [
            "Gifts",
            "Or somthing else"
        ]
  } 
   ```
* For get certificate from the database using product's id you should go to ```GET.../gifts/<id>```
* For get certificates from the database using by part of name/description you should go to ```GET.../gifts/search?q=<some description>``` 
* For getting sorted certificates from the database using params which you set you should go to ```GET.../gifts/sort?sortBy=<param>```

Params can be:
```$xslt
        id
        name
        description
        price
        duration
        createDate
        lastUpdateDate
``` 
* For delete certificate from the database you should go to  ```DELETE.../gifts/delete/<id>```
___
__Work with tags:__
* For getting all tags from database you should go to ```GET.../tags/```
* For add tag to the database you should go to ```POST.../tags/add``` 
and pass the following parameters in the body of the request:
   ```
   {
       "name":"Somthing"
   } 
  ```
* For update information about tags you should go to ```POST.../tags/update```
and pass the following parameters in the body of the request:
   ```
   {      
       "id": <id>
       "name":"Somthing"
   } 
  ```
* For get tag from the database using tag's id you should go to ```GET.../tags/<id>```
* For delete tag from the database you should go to  ```DELETE.../tags/delete/<id>```
