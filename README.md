##Company Service
This app demonstrate how to build api using HATEOS and spring data rest. It uses java as a programming language and mongodb as a persistence unit.

If you are interested in consuming API via frontend, then please have a look at :
[Angular-based-gui](https://github.com/nklmish/company-service-angular-gui)


##Tech stack
- Spring boot and Spring data
- Java 8
- Gradle
- Groovy (unit testing)
- Spock (unit testing)
- Docker
- MongoDB
- Swagger (for REST documentation)
- HATEOS (HAL implementation)

##Launch
### Manual Mode
* Install and run mongodb locally
* Launch project in a particular profile. E.g.
```./gradlew bootRun -Dspring.profiles.active=dev```

### Docker image
* ```docker run --name mymongodb -d mongo```
* ```docker run -i -t -d --name company-service --link mymongodb:mongodb -p 8080:8080 nklmish/company-service:latest```
* ```docker run -d --name swagger-ui-company-service-angular-swagger -p 8888:8888 -e "API_URL=http://LOCALHOST_OR_DOCKER_IP:8080/v2/api-docs/" sjeandeaux/docker-swagger-ui```


NOTE: By default app starts on port 8080

#gradle.properties
Please create/update your's ```gradle.properties``` file to include docker's IP address. E.g. <br/>
For Mac Users: 
```
dockerServerUrl=https://<REPLACE_WITH_DOCKER_MACHINE_IP_ADDRESS>:2376/
```
For Linux Users: 
```
dockerServerUrl=unix:///var/run/docker.sock
```

## Building docker image
```./gradlew clean build dockerBuildImage```

## REST documentation
You can visit [swagger-ui](http://ec2-52-36-37-125.us-west-2.compute.amazonaws.com:8888/swagger-ui/)

If you are running the app locally , then please use

http://LOCALHOST_OR_DOCKER_IP:PORT_ON_WHICH_APP_IS_RUNNING/swagger-ui.html

Next in swagger gui you can visit company-controller to find out more about company's API

## Interacting with API
If you don't want to open Swagger UI then you can use command prompt to interact via API. Below are example of using ```curl```
## Note - Make sure to use correct host and port in the commands listed below providing you have change default configurations i.e. not running on localhost and/or port 8080. Also you need to replace text "REPLACE_THIS_WITH_VALID_ID" with a valid "id" (i.e. company id) 
Add new company:
```
curl -X POST --header "Content-Type: application/json" --header "Accept: application/hal+json" -d "{
  \"address\": {
    \"city\": \"string\",
    \"country\": \"string\",
    \"locationDescription\": \"string\"
  },
  \"beneficialOwners\": [
    {
      \"name\": \"string\"
    }
  ],
  \"contact\": {
    \"email\": \"\",
    \"phoneNumber\": \"\"
  },
  \"name\": \"string\"
}" "http://localhost:8080/api/companies"
```

Fetch all companies:
```
curl -X GET --header "Accept: application/hal+json" "http://localhost:8080/api/companies"
```

Fetch particular company:
```
curl -X GET --header "Accept: application/hal+json" "http://localhost:8080/api/companies/REPLACE_THIS_WITH_VALID_ID"
```

Update beneficial owners (Illustration of Patch request):
```
curl -X PATCH --header "Content-Type: application/json" --header "Accept: application/hal+json" -d "{
    \"beneficialOwners\": [
    {
      \"name\": \"yyyy\"
    }
  ]
}" "http://localhost:8080/api/companies/REPLACE_THIS_WITH_VALID_ID"
```

Update company:
```
curl -X PUT --header "Content-Type: application/json" --header "Accept: application/hal+json" -d "{
  \"address\": {
    \"city\": \"string\",
    \"country\": \"string\",
    \"locationDescription\": \"string\"
  },
  \"beneficialOwners\": [
    {
      \"name\": \"string\"
    }
  ],
  \"contact\": {
    \"email\": \"\",
    \"phoneNumber\": \"123456\"
  },
  \"name\": \"string\"
}" "http://localhost:8080/api/companies/REPLACE_THIS_WITH_VALID_ID"
```

##Delete company:
```
curl -X DELETE --header "Accept: application/hal+json" "http://localhost:8080/api/companies/REPLACE_THIS_WITH_VALID_ID"
```

##Health/Monitoring
If you are running it locally or inside local docker container then please change IP address and/or port as per your requirement.
```json
{
  "_links": {
    "self": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/actuator"
    },
    "metrics": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/metrics"
    },
    "info": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/info"
    },
    "dump": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/dump"
    },
    "trace": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/trace"
    },
    "autoconfig": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/autoconfig"
    },
    "health": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/health"
    },
    "beans": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/beans"
    },
    "mappings": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/mappings"
    },
    "env": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/env"
    },
    "configprops": {
      "href": "http://ec2-52-36-229-250.us-west-2.compute.amazonaws.com/configprops"
    }
  }
}
```
