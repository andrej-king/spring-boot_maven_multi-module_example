# Example api (Java and Spring Boot)

## Dev

```bash
# run main api module
make main-api
```

```bash
# run admin api module
make admin-api
```

```bash
# run Postgres DB
make init
```

```bash
# stop app with clear build files
make down
```

```bash
# stop app with clear build files and remove docker volumes
make down-clear
```

```bash
# generate certs for api-main module
make generate-jwt-rsa-keypair
```

```bash
# generate certs for api-admin module
make generate-jwt-rsa-keypair-admin
```

## Pass app envs

```bash
# set current env
--spring.profiles.active=dev
```

## For local use should be installed

* [JDK 19](https://openjdk.org) (with IDE is non-required)
* [Maven 3.8.6](https://maven.apache.org) (with IDE is non-required)

## Pages (links)
[//]: # (* [localhost:5050][101] pgadmin4 - Web interface for connecting to Postgres database.)

* [localhost:8001][102] Java application home page (main).
* [localhost:8001/swagger][103] Main api docs.
* [localhost:8002][104] Java application home page (admin).
* [localhost:8002/swagger][105] Admin api docs.

## App sub-modules
* [com.example] admin-api
* [com.example] main-api
* [com.example] common
* [com.example] persistence
* [com.example] security

## Sub-modules dependencies
* parent
    * [org.springframework.boot] spring-boot-starter-validation
    * [org.springframework.boot] spring-boot-starter-data-jpa
    * [org.springframework.boot] spring-boot-starter-web
    * [org.springframework.boot] spring-boot-starter-test
    * [org.springframework.boot] spring-boot-starter-log4j2
    * [org.postgresql] postgresql
    * [org.projectlombok] lombok
    * [org.springdoc] springdoc-openapi-starter-webmvc-ui
    * [org.springdoc] springdoc-openapi-starter-common
* security
    * [com.example] persistence
    * [org.springframework.boot] spring-boot-starter-security
    * [org.springframework.boot] spring-boot-starter-oauth2-resource-server
    * [org.springframework.boot] spring-boot-configuration-processor
* persistence
* common
    * [com.example] persistence
    * [com.example] security
* main-api
    * [com.example] common
* admin-api
    * [com.example] common

## Helpful SQL
```SQL
-- Check table DDL structure
SELECT
   column_name,
   column_default,
   is_nullable,
   data_type,
   character_maximum_length,
   udt_name,
   is_updatable
FROM
   information_schema.columns
WHERE
   table_name = 'users';
```

```SQL
-- Get valid UUID 4 token
SELECT gen_random_uuid();
```

## Helpful shell commands
```sh
# Show app with used port
sudo lsof -i -P | grep LISTEN | grep :$PORT
```

[//]: # (## Main libraries and frameworks used for dev and prod)

[//]: # (Pages links)

[101]: http://localhost:5050

[102]: http://localhost:8001

[103]: http://localhost:8001/swagger

[104]: http://localhost:8002

[105]: http://localhost:8002/swagger

[//]: # (Main libraries and frameworks used for dev and prod)

[//]: # (## Libraries and frameworks used only for prod)

[//]: # (Libraries and frameworks used only for dev)
