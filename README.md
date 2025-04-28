# TaskFlow

**TaskFlow** — микросервисное приложение для управления задачами с поддержкой событий, хранения файлов и уведомлений.

## Стек технологий

- **Базы данных:**
  - PostgreSQL (Hibernate)
  - MongoDB (GridFS для хранения файлов)

- **Месседж-брокеры:**
  - RabbitMQ (Notification Service, DLX, Retry)
  - Kafka

- **Поиск:**
  - Elasticsearch

- **Кэширование:**
  - Redis

- **Безопасность:**
  - Spring Security

- **Миграции:**
  - Flyway (PostgreSQL)
  - Liquibase (PostgreSQL)

- **Контейнеризация:**
  - Docker

- **Сборка проекта:**
  - Gradle

- **Тестирование:**
  - JUnit
  - Mockito
  - Интеграционные тесты
  - Jacoco (отчет о покрытии тестами)

- **Прочее:**
  - Lombok (для сокращения шаблонного кода)
  - Global Exception Handler (`@RestControllerAdvice`)

## Архитектура проекта

Проект состоит из нескольких микросервисов:

- **TaskFlow Service** — управление задачами, публикация событий через Kafka и RabbitMQ.
- **Notification Service** — получение событий о задачах, отправка уведомлений через очереди RabbitMQ.

Каждый сервис взаимодействует через брокеры сообщений и работает со своей собственной базой данных.

## Будущие планы

- Добавление **API Gateway** для маршрутизации запросов между микросервисами.
- Расширение функциональности поиска и кэширования.
- Улучшение авторизации и аутентификации через OAuth2.

## Как запустить проект

1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/Erkebulan112/spring-webapp.git
   ```

2. Перейдите в папку проекта:
   ```bash
   cd taskflow
   ```
3. Запустите все сервисы через Docker:
   ```bash
   docker-compose up --build
   ```
4.   Проверьте доступность микросервисов по портам, указанным в application.yml.

## Генерация отчета покрытия тестами
Для генерации отчета о покрытии тестами с помощью Jacoco выполните:
   ```bash
   ./gradlew test jacocoTestReport
   ```
Отчет будет доступен по пути:
   ```bash
   build/reports/jacoco/test/html/index.html
   ```
## Будущие планы по развитию проекта
Добавить API Gateway для централизованной маршрутизации запросов.
Расширить механизмы поиска с помощью Elasticsearch.
Внедрить OAuth2 для улучшенной аутентификации и авторизации.
Добавить сервис мониторинга (например, Prometheus + Grafana).

## Контакты
✉️ Email: erkebulan.myrzakhan04@gmail.com

💼 LinkedIn: https://www.linkedin.com/in/erkebulan-myrzakhan-b904a0327/
