# Реализация аутентификации и авторизации с использованием Spring Security и JWT

### Описание проекта:
Базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей.

### REST API
URL: http://localhost:8080

- ```POST /auth/registration``` - регистрация нового пользователя.
- ```POST /auth/login``` - авторизация пользователя.
- ```POST /auth/token``` - получение нового access токена.
- ```POST /auth/refresh``` - получение новых access и refresh токенов.
- ```GET /resources/user``` - получение доступа к ресурсам пользователя с ролью USER.
- ```GET /resources/admin``` - получение доступа к ресурсам пользователя с ролью ADMIN.

### Запуск приложения:
- Клонировать проект в среду разработки.
- Настроить подключение к базе данных в файле application.properties.
- Запустить метод ```main``` в файле ```SpringSecurityJwtApplication.java```

#### Регистрация нового пользователя
Для регистрации нового пользователя, сформируйте JSON с данными пользователя:

Пример:
```json
{
  "username": "user",
  "email": "user@gmail.com",
  "password": "password",
  "role": "USER"
}
```

#### Авторизация пользователя
Для авторизации пользователя, сформируйте JSON с данными пользователя:

Пример:
```json
{
  "email": "user@gmail.com",
  "password": "password"
}
```

После этого Вы получите действительные access и refresh токены для доступа к защищенным ресурсам.

#### Получение нового access токена.
Для получения нового access токена, сформируйте JSON с refresh токеном,
полученным в процессе авторизации:

Пример:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNUBnbWFpbC5jb20iLCJleHAiOjE3MjUxMzc5MjR9.enWRhihH-Z6hrsoT9myAfWuic23jOrIJEbzLmkIpmPA"
}
```

После этого Вы получите новый access токен для доступа к защищенным ресурсам, а refresh токен останется прежним.

#### Получение новых access и refresh токенов, когда они стали недействительны.
Для получения новых access и refresh токенов, когда они стали недействительны, сформируйте JSON с refresh токеном,
полученным в процессе авторизации:

Пример:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNUBnbWFpbC5jb20iLCJleHAiOjE3MjUxMzc5MjR9.enWRhihH-Z6hrsoT9myAfWuic23jOrIJEbzLmkIpmPA"
}
```

Выберите тип авторизации - Bearer Token, и в поле авторизации введите действующий access токен:

Пример:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNUBnbWFpbC5jb20iLCJleHAiOjE3MjI1NDk1MjQsInJvbGUiOiJVU0VSIiwidXNlcm5hbWUiOiJ1c2VyNSJ9.0rmU-rPkPEuzfgz43qcAgeM9_WTQVuxQOA8jkjv_rb8
```

После этого Вы получите новые access и refresh токены для доступа к защищенным ресурсам.

#### Получение доступа к ресурсам пользователя с ролью `USER`
Для получения доступа к ресурсам пользователя с ролью `USER`, выберите тип авторизации - Bearer Token,
и в поле авторизации введите действующий access токен:

Пример:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNUBnbWFpbC5jb20iLCJleHAiOjE3MjI1NDk1MjQsInJvbGUiOiJVU0VSIiwidXNlcm5hbWUiOiJ1c2VyNSJ9.0rmU-rPkPEuzfgz43qcAgeM9_WTQVuxQOA8jkjv_rb8
```

После этого Вам будет предоставлен доступ к защищенным ресурсам пользователя с ролью `USER`.

#### Получение доступа к ресурсам пользователя с ролью `ADMIN`
Для получения доступа к ресурсам пользователя с ролью `ADMIN`, выберите тип авторизации - Bearer Token,
и в поле авторизации введите действующий access токен:

Пример:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNUBnbWFpbC5jb20iLCJleHAiOjE3MjI1NDk1MjQsInJvbGUiOiJVU0VSIiwidXNlcm5hbWUiOiJ1c2VyNSJ9.0rmU-rPkPEuzfgz43qcAgeM9_WTQVuxQOA8jkjv_rb8
```

После этого Вам будет предоставлен доступ к защищенным ресурсам пользователя с ролью `ADMIN`.


В корне проекта доступен [OpenAPI](./api-docs.yaml) с которым можно работать через [Swagger Editor](https://editor.swagger.io/)
### Технологии, используемые в проекте:
```
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- SpringDoc
- Maven
- REST API
- Lombok
- PostgreSQL
- Liquibase
```
