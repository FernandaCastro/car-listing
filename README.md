## Car Listing

It provides a platform that receives listings through different channels, standardize and make them available in the platform.

Some providers send listing data via CSV to the endpoint:

```
POST /car-listing/upload/{dealer_id}

CSV Format - A sample file (upload.csv) is available under /resourses:

code,make/model,power-in-ps,year,color,price
1,mercedes/a 180,123,2014,black,15950
2,audi/a3,111,2016,white,17210
3,vw/golf,86,2018,green,14980
4,skoda/octavia,86,2018,16990
````

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

A search is available under the endpoint:
```
GET /car-listing/listings

Search paramenters available:
 make
 model 
 year  
 color
```

### Requirements:
- Listings can have same "code" for different dealers

- A listing is updated if it is sent again by the same dealer (based on the code)

- Integration tests

- Usage of OCI-Containers - Docker
