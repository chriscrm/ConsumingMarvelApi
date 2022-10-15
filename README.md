# Marvel Resource API

ConsumingMarvelApi is a Java REST API for dealing with Marvel API and fetch data from Comics Resource, Series, Creators and Characters.

## Installation

Use the Docker Pull Command to install ConsumingMarvelApi.

```bash
docker pull crisr/crm-marvel-api-resource
```

## Usage

By default, the Docker will expose port 8090, so change this within the  Dockerfile if necessary

Once done, run the Docker image and map the port to whatever you wish on  your host. 
In this example, we simply map port 8090 of the host to  port 8090 of the Docker
```bash
docker run -p 8090:8090 --name marvel-api-resource crisr/crm-marvel-api-resource
```

Verify the deployment by navigating to your server address in  your preferred browser.

## Endpoints

### OpenAPI endpoints

api-docs : `http://localhost:8090/api/v1/api-docs`
swagger:  `http://localhost:8090/api/v1/swagger-ui.html`

### API endpoints
base-url: `http://localhost:8090`
context-path: `/api/v1`
web request: `/marvel`


*Endpoint used to fetch all Comics*
```bash
http://localhost:8090/api/v1/marvel/comics
```
-----------------

![enter image description here](https://img.shields.io/badge/java-11-blue) ![enter image description here](https://img.shields.io/badge/spring--boot-2.7.4-green)
