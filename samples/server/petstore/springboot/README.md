# OpenAPI generated server

Spring Boot Server

## Overview
This server was generated by the [OpenAPI Generator](https://openapi-generator.tech) project.
By using the [OpenAPI-Spec](https://openapis.org), you can easily generate a server stub.
This is an example of building a OpenAPI-enabled server in Java using the SpringBoot framework.

The underlying library integrating OpenAPI to Spring Boot is [springfox](https://github.com/springfox/springfox).
Springfox will generate an OpenAPI v2 (fka Swagger RESTful API Documentation Specification) specification based on the
generated Controller and Model classes. The specification is available to download using the following url:
http://localhost:80/v2/api-docs/

**HEADS-UP**: Springfox is deprecated for removal in version 6.0.0 of openapi-generator. The project seems to be no longer
maintained (last commit is of Oct 14, 2020). It works with Spring Boot 2.5.x but not with 2.6. Spring Boot 2.5 is
supported until 2022-05-19. Users of openapi-generator should migrate to the springdoc documentation provider which is,
as an added bonus, OpenAPI v3 compatible.



Start your server as a simple java application

You can view the api documentation in swagger-ui by pointing to
http://localhost:80/swagger-ui.html

Change default port value in application.properties