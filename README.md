# Real-Time Stream Processing System: Wikimedia Event Ingestion

A decoupled microservices architecture designed to ingest, process, and persist high-velocity real-time data streams using Apache Kafka and Spring Boot.

## 1. Executive Summary (The 5W1H)

*   **What:** A distributed system that captures live "recent change" events from Wikimedia and stores them for analytical or auditing purposes.
*   **Why:** To solve the challenge of processing asynchronous, high-frequency data streams while maintaining system scalability and fault tolerance through decoupling.
*   **Who:** The system interfaces with the public **Wikimedia EventStreams API** as the primary data provider.
*   **When:** Data processing is reactive; the system responds immediately as events are broadcasted by the source.
*   **Where:** Events travel from the global Wikimedia edge servers, through a local **Kafka Broker**, and are persisted into a **Relational Database (RDBMS)**.
*   **How:** Leveraging **Spring Kafka** for messaging, **Project Lombok** for boilerplate reduction, and **EventSource (OkHttp)** for reactive stream consumption.

## 2. System Architecture & Concept

The project implements a **Producer-Consumer pattern** mediated by a message broker to ensure that high traffic from the source does not overwhelm the database (Backpressure Management).

### A. The Producer (Data Ingestion)
Located in \kafka-producer-wikimedia\, this service acts as a reactive client.
- **Mechanism:** It uses a non-blocking EventSource to maintain a persistent HTTP connection to the Wikimedia stream.
- **Abstraction:** The \BackgroundEventHandler\ transforms raw stream events into Kafka messages, ensuring the ingestion logic is separated from the transport logic.

### B. The Message Broker (Distributed Log)
- **Role:** Apache Kafka provides the durability layer.
- **Topic Strategy:** Messages are partitioned in the \wikimedia_info_updates\ topic, allowing for horizontal scaling of consumers if data volume increases.

### C. The Consumer (Persistence Layer)
Located in \kafka-consumer-database\, this service handles the data lifecycle.
- **Strategy:** It utilizes a **Kafka Listener** to pull data asynchronously.
- **Object Mapping:** Raw JSON payloads are mapped to JPA Entities (\Wikimedia.java\) and persisted via the Repository pattern.

## 3. Data Flow Overview

1.  **Stream Connection:** Producer opens a Server-Sent Events (SSE) connection.
2.  **Event Capture:** Wikimedia broadcasts a "Recent Change" event.
3.  **Publishing:** The Producer wraps the event and sends it to the Kafka Cluster.
4.  **Buffering:** Kafka stores the message, ensuring it's available even if the consumer is temporarily offline.
5.  **Consumption:** The Consumer retrieves the message and validates the payload.
6.  **Persistence:** Data is committed to the database using an 'Update' DDL strategy for schema evolution.

## 4. Technical Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Framework** | Spring Boot 3.x/4.x | Microservice orchestration |
| **Messaging** | Apache Kafka | Event streaming and decoupling |
| **Ingestion** | OkHttp EventSource | Reactive stream consumption |
| **Persistence** | Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) |
| **Database** | PostgreSQL | Long-term data storage |
| **Utilities** | Lombok | Clean code and boilerplate reduction |

## 5. Setup & Extensibility

### Environment Requirements
- Java 17+
- A running Kafka Broker (Bootstrap Server)
- A PostgreSQL Instance

### Flexibility
The system is designed to be environment-agnostic. All critical parameters—such as **Kafka Bootstrap Servers**, **Stream URLs**, and **Database Credentials**—should be configured via \pplication.properties\ or Environment Variables to suit different deployment stages (Dev, Test, Prod).

---
*This project serves as a foundational blueprint for Event-Driven Architectures (EDA) and Real-time Data Pipelines.*
