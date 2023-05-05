# Car Listing

It provides a first version for a platform that receives listings through different channels. Listings are then standardized and made available in the platform.

## Sending listing data
### 1 - Via CSV file:

```
POST /listings/upload/{dealerId}
```
CSV Format - A good and bad sample files (upload.csv, upload-error.csv) are available under folder ````/tools````:
````
code,make/model,power-in-ps,year,color,price
1,mercedes/a 180,123,2014,black,15950
2,audi/a3,111,2016,white,17210
3,vw/golf,86,2018,green,14980
4,skoda/octavia,86,2018,gray,16990

````

### 2 - Via JSON API:

````
POST /listings/{dealerId}
````
JSON Format:
````
[
   {
    "code": "a",
    "make": "renault",
    "model": "megane",
    "kW": 132,
    "year": 2014,
    "color": "red",
    "price": 13990
   }
]
````
## Searching for listings
A search is available under the endpoint below. Searches can be done using the following search parameters:
- make
- model
- year  
- color

```
GET /listings
```

## A few other requirements:
- Listings can have same "code" for different dealers

- When resending a listing, it is always updated. The match is made by dealer_id and code.

- Create Integration tests

- Usage of OCI-Containers - Docker

---------------------
## Executing Unit Tests and Integration Tests
Unit Tests (src/test) and Integration Tests (src/integration-test) are executed by running:

````mvn clean test -P integration-test````

-------------------------------------------

## Running the application locally
1. Bring up the Postgres database container: 
   - Navigate to: ```$ cd <project folder>\tools``` 
   - Run : ````$ docker compose up -d```` 
   
2. Compile the application: 
   - Navigate to project folder: ``$ cd ..``
   - `````$ mvn clean instal`````
   
4. Start the application: 
   - ````$ java -jar <project folder>\target\car-listing-1.0-SNAPSHOT.jar````
   
-------------------------
## Running the application as a Docker Container
1. Create the local image:
   - ```$ mvn compile jib:dockerBuild```
2. Bring up the application and Postgres database containers:
   - ```$ DOCKER_USER=<your docker user> docker compose up -d```

---------------------------   

## Let's run some tests

### 1- POST listings via JSON API

POST /listings/1

````
$ curl -v -POST -H "Content-type:application/json" -d \
'[
{
"code": "a",
"make": "renault",
"model": "megane",
"kw": 132,
"year": 2015,
"color": "pink",
"price": 13990
},
{
"code": "b",
"make": "mercedes",
"model": "a 180",
"kw": 110,
"year": 2013,
"color": "black",
"price": 10990
}
]' \
http://localhost:8080/listings/1
````

### 2- POST listings via CSV file

POST /listings/upload/2

````
$ cd <project-folder>
$ curl -v -POST -F "file=@tools/upload.csv" http://localhost:8080/listings/upload/2
````

### 3- GET listings by search

GET /listings?color=gray

GET /listings?color=gray&color=black

```
$ curl -v -GET http://localhost:8080/listings?color=gray
$ curl -v -GET http://localhost:8080/listings -d 'color=gray&color=black'
```

