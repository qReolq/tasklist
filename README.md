# tasklist

This REST API application allows you to organize the creation and tracking of your tasks.

* [Technologies](#technologies)
* [Features](#features)
* [Quick start](#quick-start)

### Technologies
Spring(Boot, Security, JPA), JWT tokens, Swagger, Docker, PostgreSQL, Redis, Mybatis, MinIO, Liquibase, JUnit, Mockito, JaCoCo
 
### Features
  * User can:
     * Login and register(with jwt tokens)
     * Create tasks
     * Upload profile
     * Upload images for tasks(use MinIO storage)
  * Util
      * Checkstyle and Github Actions
      * Swagger UI
      * JaCoCo plugin

### Sequence diagram

![Sequence diagram](docs/sequence-diagram.png)

### Component diagram

![Component diagram](docs/component-diagram.png)

Main application communicates with cache (Redis), main database (Postgresql), file storage (MinIO).

### Class diagram

![Class diagram](docs/er-diagram.png)

We have two main tables - **Users** and **Tasks**.

**Users** table represents user in this application. User can login, create and update tasks.

User can have roles - `ROLE_USER` or `ROLE_ADMIN`.

**Tasks** table represents task in this application. Task can be created and deleted by user.

Task can have images.

### Quick start
1. Clone this repo into folder.

```Bash
git clone https://github.com/qReolq/hubr.git
cd hubr
```
2. Start docker compose

```Bash
docker compose up
```
3. Go to http://localhost:8080/auth/login

4. You can [register](#registration-settings) or login as an existing user ~ login:timur password:1q2w

5. Stop docker
```
docker compose down
```
