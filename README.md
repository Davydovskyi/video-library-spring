Spring Webflux Security
================================


## Tехнологии:
 - Spring Boot 3
 - Spring Security (JWT)
 - Spring WebFlux
 - Spring Data R2DBC
 - MapStruct
 - PostgreSQL
 - Flyway


## Локальный запуск приложения
- Установить PostgreSQL

## Создать БД
```sql
CREATE DATABASE "web_flux_security";
```

## Установить корректные значения в application.yml
```sql
spring:r2dbc:username
```

```sql
spring:r2dbc:password
```

# cURL запросов:

## 1. Регистрация пользователя
```bash
curl --location 'http://localhost:8080/api/v1/auth/register' \
--header 'Content-Type: application/json' \
--data '{
    "username": "test",
    "password": "test",
    "first_name": "Boris",
    "last_name": "Black"
}'
```

Пример ответа:
```json
{
  "id": 1,
  "username": "test",
  "role": "USER",
  "first_name": "Boris",
  "last_name": "Black",
  "enabled": true,
  "created_at": "2024-01-19T14:53:32.36094",
  "updated_at": "2024-01-19T14:53:32.360954"
}
```

## 2. Аутентификация пользователя
```bash
curl --location 'http://localhost:8080/api/v1/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "test",
    "password": "test"
  }'
```

Пример ответа
```json
{
  "user_id": 1,
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QiLCJyb2xlIjoiVVNFUiIsImlzcyI6IndlYi1mbHV4LXNlY3VyaXR5Iiwic3ViIjoiMyIsImlhdCI6MTcwNTY3Njg1NiwianRpIjoiM2I4ZDkzYjctZGE1Ni00MmFiLWJhMDktNzI4NGMwNjcxYjdjIiwiZXhwIjoxNzA1NjgwNDU2fQ.r8LjhWkVszT5r2xAGS_JgWNf9bsnJi1A5LMw0qomw1Q",
  "issued_at": "2024-01-19T11:53:56.390+00:00",
  "expires_at": "2024-01-19T12:53:56.390+00:00"
}
```

## 3. Получение данных пользователя с использованием токена, полученного в предыдущем запросе

```bash
curl --location 'http://localhost:8080/api/v1/auth/info' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QiLCJyb2xlIjoiVVNFUiIsImlzcyI6IndlYi1mbHV4LXNlY3VyaXR5Iiwic3ViIjoiMyIsImlhdCI6MTcwNTY3Njg1NiwianRpIjoiM2I4ZDkzYjctZGE1Ni00MmFiLWJhMDktNzI4NGMwNjcxYjdjIiwiZXhwIjoxNzA1NjgwNDU2fQ.r8LjhWkVszT5r2xAGS_JgWNf9bsnJi1A5LMw0qomw1Q'
```

Пример ответа
```json
{
  "id": 1,
  "username": "test",
  "role": "USER",
  "first_name": "Boris",
  "last_name": "Black",
  "enabled": true,
  "created_at": "2024-01-19T14:02:37.248466",
  "updated_at": "2024-01-19T14:02:37.248482"
}
```
