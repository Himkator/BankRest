# Система управления банковскими картами

# 💳 Система управления банковскими картами

Spring Boot REST API для управления банковскими картами. Сервис позволяет создавать карты, получать список карт с фильтрацией, выполнять переводы между картами и осуществлять другие операции с JWT-аутентификацией.

## 📌 Функциональность

- Регистрация и вход по JWT
- Создание банковской карты
- Получение всех карт пользователя с фильтрами:
    - по номеру карты
    - по статусу (ACTIVE, BLOCKED, EXPIRED, PENDING_BLOCK)
    - по балансу (от / до)
    - по сроку действия (после / до определённой даты)
- Перевод средств между картами
- Блокировка карты
- Просмотр деталей карты

## 🛠️ Технологии

- Java 17+
- Spring Boot 3+
- Spring Security (JWT)
- JPA (Hibernate)
- PostgreSQL
- Maven
- Testcontainers (для интеграционных тестов)
- JUnit 5, Mockito

---

## 🚀 Как запустить проект

### 1. Клонируйте репозиторий

```bash
https://github.com/Himkator/BankRest.git
cd BankRest
```
### 2. Настройка БД
Убедитесь, что у вас установлен PostgreSQL. Создайте базу данных, например:

```sql
    CREATE DATABASE bankcards;
```

### 3. Измените настройки в src/main/resources/application.yml:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bankcards
    username: your_db_user
    password: your_db_password
```

### 4. Сборка и запуск

```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
```

Приложение будет доступно по адресу:
📍 http://localhost:8080

### 🔐 Авторизация (JWT)

-После регистрации/логина пользователь получает JWT токен.
-Для доступа к защищённым маршрутам необходимо передавать токен в заголовке:
```bash
Authorization: Bearer <your_token>
```
### Тестирование

```bash
./mvnw test
```

### API Примеры
-POST /api/v1/cards/createCard — создать карту
-GET /api/v1/cards — получить список карт (можно добавить параметры фильтрации)
-POST /api/v1/cards/transfer — перевод между картами
Все API можно посмотреть в docs/openapi.yaml
Или же в http://localhost:8080/swagger-ui/index.html#/

### Контакты
Разработчик: Нурым
Git: https://github.com/Himkator/
Email: nurymsejtkazy@gmail.com