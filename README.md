# Article Service with Cached View Counts

---

## **Project Overview**

This project is a **Spring Boot microservice** that manages articles and efficiently tracks view counts using **Caffeine in-memory caching**. Instead of hitting the database on every article view, the service caches view counts temporarily to improve performance and reduce database load.

It also supports lazy loading, manual refresh of cached data, and configurable persistence thresholds to the database.

---

## **Features**

* **CRUD Operations:** Full support for creating, reading, updating, and deleting articles.  
* **Validation & Error Handling:** Ensures data integrity with input validation and structured error responses.
* **View Count Caching:** Article view counts are cached in-memory using Caffeine to prevent frequent DB hits.
* **Auto Expiration:** Cached values expire after 10 minutes by default.
* **Lazy Loading:** If a view count is missing from cache, it is loaded from the database.
* **Manual Refresh Support:** Endpoint to clear and reload a specific article’s view count.
* **Maximum Size Eviction:** Cache is limited to 1000 entries to avoid memory overflow.
* **Metrics & Monitoring:** Integrated with Spring Boot Actuator to monitor cache performance and application health.

---

## **Technical Stack**

* **Java 21**
* **Spring Boot 3.5.5**
* **Spring Data JPA**
* **Caffeine Cache**
* **PostgreSQL** for persistent storage
* **MapStruct** for DTO mapping
* **Lombok** for boilerplate reduction
* **Spring Boot Actuator** for monitoring
* **Gradle** as build tool

---

## **Configuration**

### **Application Properties**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/test-database
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  cache:
    type: caffeine
    cache-names: articleViews, articles
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=10m

article:
  view:
    db-persist-threshold: 5
```

* `db-persist-threshold` defines how many view increments occur in cache before persisting to the database.
* Caches: `articleViews` (for view counts), `articles` (optional for article objects).

---

## **API Endpoints**

| Method   | Endpoint                   | Description                                                      |
| -------- | -------------------------- | ---------------------------------------------------------------- |
| `POST`   | `/articles`                | Create a new article                                             |
| `GET`    | `/articles`                | Get all articles (view counts included)                          |
| `GET`    | `/articles/{id}`           | Get a single article by ID (increments view count automatically) |
| `PUT`    | `/articles/{id}`           | Update an existing article                                       |
| `DELETE` | `/articles/{id}`           | Delete an article                                                |
| `GET`    | `/articles/{id}/views`     | Get the current view count for an article                        |
| `POST`   | `/articles/{id}/increment` | Increment the view count manually                                |
| `POST`   | `/articles/{id}/reset`     | Reset the view count for an article                              |

> **Note:** `GET /articles/{id}` increments the view count automatically. Manual increments can be done using `/increment`.

---

## **Error Handling**

All endpoints return **structured error responses** in case of failures:

* `400` – Validation errors
* `404` – Resource not found
* `409` – Conflict (e.g., duplicate title)
* `500` – Internal server errors

Example error response:

```json
{
  "status": 404,
  "error": "Resource Not Found",
  "message": "Article not found with id: 10",
  "timestamp": "2025-09-08T20:15:30Z"
}
```

---

## **Getting Started**

### **Prerequisites**

* Java 21
* PostgreSQL database
* Gradle

### **Build & Run**

```bash
# Clone repository
git clone https://github.com/Aqsin211/article-view-counter.git
cd article-service

# Build the project
./gradlew clean build

# Run the service
./gradlew bootRun
```

### **Accessing Actuator Endpoints**

* Health: `http://localhost:8080/actuator/health`
* Metrics: `http://localhost:8080/actuator/metrics`
* Info: `http://localhost:8080/actuator/info`

---

## **Project Structure**

```
src/
 ├─ main/
 │   ├─ java/az/company/article/
 │   │   ├─ config/      # Caffeine Cache configuration
 │   │   ├─ controller/  # REST controllers
 │   │   ├─ dao/         # JPA entities and repositories
 │   │   ├─ exception/   # Custom exceptions and handlers
 │   │   ├─ model/       # DTOs, enums, and mappers
 │   │   ├─ service/     # Business logic
 │   │   └─ ArticleApplication.java
 │   └─ resources/
 │       └─ application.yml
```

---

## **Caching Behavior**

* **Increment View Count:** Increments the value in cache first; persists to DB every `db-persist-threshold` increments.
* **Reset View Count:** Evicts cache and resets DB value to 0.
* **Auto Expiration:** Cached view counts expire after 10 minutes of inactivity.
* **Maximum Cache Size:** 1000 entries to prevent memory overuse.

---

## Contact

For questions or support, please open an issue or contact [Aqsin211](https://github.com/Aqsin211).
