# 🚀 Distributed Event Streaming: Wikimedia Real-Time Data Pipeline

This project demonstrates a robust, **Event-Driven Architecture (EDA)** designed to ingest, stream, and persist high-velocity data in real-time. By leveraging **Apache Kafka** and **Spring Boot**, it provides a scalable solution for processing asynchronous event streams from the Wikimedia Foundation.

---

## 📖 Project Overview

The system is engineered to handle the full lifecycle of real-time data, from ingestion to long-term persistence. It bridges the gap between a high-frequency external data source and a reliable storage layer.

- **🎯 Purpose:** To implement a resilient data pipeline that captures live global edits, ensuring data integrity through asynchronous message brokering.
- **🏗️ Design Philosophy:** The system prioritizes **Loose Coupling**, allowing the data producer and database consumer to scale independently while managing backpressure and ensuring fault tolerance.

---

## 🛠️ Architectural Flow

\\\mermaid
graph LR
    subgraph "External Source"
        W[Wikimedia SSE Stream]
    end

    subgraph "Producer Service (Spring Boot)"
        P[OkHttp EventSource] --> H[Event Handler]
        H --> KT[Kafka Template]
    end

    subgraph "Message Broker"
        K[(Apache Kafka)]
    end

    subgraph "Consumer Service (Spring Boot)"
        L[Kafka Listener] --> R[JPA Repository]
    end

    subgraph "Storage"
        DB[(PostgreSQL)]
    end

    W -- "Real-time SSE" --> P
    KT -- "Publish" --> K
    K -- "Subscribe" --> L
    R -- "Persist" --> DB
\\\

---

## ⚡ Reactive Stream Consumption: OkHttp EventSource

A critical component of this architecture is the use of **OkHttp EventSource** for data ingestion. 

### Why OkHttp EventSource?
Standard REST APIs are "request-response," which is inefficient for live updates. This project uses **Server-Sent Events (SSE)**, where the server keeps a single connection open and "pushes" data to us as it happens.

> [!IMPORTANT]
> **Reactive Ingestion:** The \okhttp-eventsource\ library allows the Producer to maintain a persistent, non-blocking connection. This ensures the application doesn't "wait" for data; instead, it reacts only when an event arrives, significantly reducing CPU and memory overhead.

- **Non-blocking IO:** Processes events in a background thread, keeping the main application responsive.
- **Auto-reconnection:** Automatically handles connection drops to ensure the data pipeline is continuous.
- **Backpressure Ready:** By handing off events to Kafka immediately, the Producer stays "light" and never gets overwhelmed by the source stream.

---

## 🧩 Core Components

### 1️⃣ Ingestion Layer (The Producer)
The \kafka-producer-wikimedia\ module acts as a reactive gateway. 
- **Mechanism:** Transforms live SSE data into discrete Kafka messages.
- **Abstraction:** The background handler ensures the ingestion logic is separated from the transport logic.

### 2️⃣ Orchestration Layer (The Message Broker)
**Apache Kafka** serves as the backbone, providing a durable and partitioned transport mechanism.
- **Buffering:** Kafka ensures that fluctuations in stream velocity do not impact database performance.
- **Durability:** Even if the consumer is offline, Kafka "remembers" the data until it is processed.

### 3️⃣ Persistence Layer (The Consumer)
The \kafka-consumer-database\ module manages the data lifecycle.
- **Asynchronous Pull:** Uses a Kafka Listener to pull messages at its own pace.
- **ORM Mapping:** Structured payloads are mapped to JPA Entities for reliable storage.

---

## 🧰 Technical Stack

| Category | Technology |
| :--- | :--- |
| **Framework** | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white) |
| **Messaging** | ![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=flat&logo=apache-kafka&logoColor=white) |
| **Inbound Stream** | ![OkHttp](https://img.shields.io/badge/OkHttp-4EA94B?style=flat) (EventSource) |
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white) |
| **Persistence** | Hibernate / Spring Data JPA |

---

## ⚙️ Extensibility

The system is designed with an **Environment-Agnostic** mindset. All infrastructure details—including broker addresses and stream endpoints—are managed through externalized configuration. This allows the pipeline to be seamlessly deployed across Development, Staging, or Production environments.

---
*This project serves as a foundational blueprint for modern Event-Driven Architectures (EDA) and Real-time Data Pipelines.*
