# Wikimedia Kafka Real-Time Stream Processing

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-2.x/3.x-231F20?logo=apache-kafka&logoColor=white)](https://kafka.apache.org/)
[![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org/)

A high-performance, event-driven data pipeline designed to ingest, stream, and persist real-time global change events from Wikimedia. This project leverages the scalability of **Apache Kafka** and the robustness of **Spring Boot** to build a decoupled microservices architecture.

---

## 🚀 Project Overview

This project implements a complete **Reactive Stream Processing Pipeline**. It captures live "recent change" events broadcasted by the [Wikimedia EventStreams API](https://stream.wikimedia.org/v2/stream/recentchange) and processes them through a distributed messaging system.

### Key Objectives
*   **Real-Time Ingestion:** Synchronous capture of global Wikimedia edits via Server-Sent Events (SSE).
*   **Asynchronous Processing:** Decoupling data production from consumption using Kafka to ensure system resilience and backpressure management.
*   **Reliable Persistence:** Storing high-velocity event data into a relational database for auditing and analytical purposes.

---

## 🏗️ Architecture & Technical Flow

The system is composed of two primary microservices mediated by a Kafka broker.

```mermaid
graph LR
    subgraph "External Data Source"
        W[Wikimedia API /v2/stream]
    end

    subgraph "Ingestion Microservice (Producer)"
        direction TB
        E[OkHttp EventSource] --> H[Event Handler]
        H --> KT[Kafka Template]
    end

    subgraph "Message Backbone"
        K[(Apache Kafka)]
    end

    subgraph "Persistence Microservice (Consumer)"
        direction TB
        L[Kafka Listener] --> JPA[Spring Data JPA]
        JPA --> DB[(PostgreSQL)]
    end

    W -- "SSE Stream" --> E
    KT -- "Publish" --> K
    K -- "Subscribe" --> L
```

### 1. The Producer Layer (`kafka-producer-wikimedia`)
Uses **OkHttp** and **LaunchDarkly EventSource** to maintain a persistent connection to the Wikimedia stream. 
*   **Reactive Handling:** Implements `BackgroundEventHandler` to process incoming JSON payloads asynchronously.
*   **Topic Publishing:** Messages are serialized and sent to the `wikimedia_info_updates` topic.

### 2. The Storage Layer (`kafka-consumer-database`)
Acts as a dedicated worker to persist the incoming data stream.
*   **Kafka Listener:** Monitors the Kafka topic and triggers on every new message.
*   **JPA Entity Mapping:** Maps the raw event data to a `Wikimedia` entity and saves it to a PostgreSQL table (`wikimedia_recent_info`).

---

## 🛠️ Configuration & Setup

### Prerequisites
*   **Java 17** or higher
*   **Apache Kafka** (running on port 9092)
*   **PostgreSQL** (running on port 5432)
*   **Maven** (for building the modules)

### Step-by-Step Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/BKishoree/wikimedia_recentchange_apachekafka.git
    cd wikimedia
    ```

2.  **Infrastructure Setup:**
    *   Start your Zookeeper and Kafka server.
    *   Create a PostgreSQL database named `wikimedia`.

3.  **Environment Configuration:**
    Update the `application.properties` files in both modules if your local setup differs:
    *   **Producer:** `kafka-producer-wikimedia/src/main/resources/application.properties`
    *   **Consumer:** `kafka-consumer-database/src/main/resources/application.properties`

4.  **Build and Run:**
    ```bash
    # Build the entire project
    mvn clean install

    # Run the Consumer Service
    cd kafka-consumer-database
    mvn spring-boot:run

    # Run the Producer Service
    cd ../kafka-producer-wikimedia
    mvn spring-boot:run
    ```

---

## ⚡ Technical Highlights

### Reactive Stream Consumption (SSE)
The project utilizes **OkHttp EventSource** to handle Server-Sent Events. Unlike traditional polling, SSE allows the server to push updates to the client as they occur, making it ideal for high-frequency streams like Wikimedia's recent changes.

### Scalability and Fault Tolerance
By using Kafka as a buffer:
*   **Backpressure Management:** The consumer can process data at its own pace without overwhelming the database.
*   **Persistence Guarantee:** Even if the consumer service goes down, Kafka retains the messages until the service recovers.

---

## 🧰 Tech Stack
*   **Framework:** Spring Boot (Starter Kafka, Data JPA)
*   **Messaging:** Apache Kafka
*   **Reactive Client:** OkHttp, LaunchDarkly EventSource
*   **Database:** PostgreSQL
*   **Utilities:** Project Lombok, Jackson (JSON processing)

---
*Developed as a reference for Event-Driven Microservices.*
