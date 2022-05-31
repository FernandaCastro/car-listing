# Car Listing

It provides a platform that receives listings through different channels. Listings are then standardized and made available in the platform.

## How to send listing data:
1 - Via CSV file:

Some providers send listing data via CSV to the endpoint:

```
POST /car-listing/upload/{dealer_id}

CSV Format - A sample file (upload.csv) is available under /resourses:

code,make/model,power-in-ps,year,color,price
1,mercedes/a 180,123,2014,black,15950
2,audi/a3,111,2016,white,17210
3,vw/golf,86,2018,green,14980
4,skoda/octavia,86,2018,gray,16990

````

2 - Via JSON API:

Some providers send listing data via JSON API to the endpoint:
````
POST /car-listing/listings/{dealer_id}

JSON Format:

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
## How to search for a listing:
A search is available under the endpoint:
```
GET /car-listing/listings

Search paramenters available:
 make
 model 
 year  
 color
```

### A few other requirements:
- Listings can have same "code" for different dealers

- When resending a listing, it is always updated. The match is made by dealer_id and code.

- Create Integration tests (I'm using TestContainer)

- Usage of OCI-Containers - Docker
