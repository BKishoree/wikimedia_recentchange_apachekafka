# Distributed Event Streaming: Wikimedia Real-Time Data Pipeline

This project demonstrates a robust, event-driven architecture designed to ingest, stream, and persist high-velocity data in real-time. By leveraging the power of Apache Kafka and Spring Boot, it provides a scalable solution for processing asynchronous event streams from the Wikimedia Foundation.

## Project Overview

The system is engineered to handle the full lifecycle of real-time data, from ingestion to long-term persistence. At its core, the project utilizes a decoupled microservices approach to manage the flow of "recent change" events broadcasted by Wikimedia. 

- **Purpose:** To implement a resilient data pipeline that captures live global edits, ensuring data integrity and system availability through asynchronous message brokering.
- **Scope:** The architecture bridges the gap between a high-frequency external data source (Wikimedia EventStreams) and a reliable internal storage layer (PostgreSQL), using Kafka as a distributed commit log.
- **Design Philosophy:** The system prioritizes loose coupling, allowing the data producer and database consumer to scale independently while managing backpressure and ensuring fault tolerance.

## Architectural Architecture & Concept

The pipeline is structured into three primary layers, each responsible for a distinct phase of the data lifecycle:

### 1. Ingestion Layer (The Producer)
The \kafka-producer-wikimedia\ module acts as a reactive gateway. It establishes a persistent, non-blocking connection to the Wikimedia SSE (Server-Sent Events) stream. 
- **Reactive Flow:** Using a background event handler, it transforms live stream data into discrete messages without blocking the main execution thread.
- **Message Dispatch:** Events are dispatched to a dedicated Kafka topic, abstracting the source stream from the rest of the ecosystem.

### 2. Orchestration Layer (The Message Broker)
Apache Kafka serves as the backbone of the system, providing a durable and partitioned transport mechanism.
- **Decoupling:** By acting as a buffer, Kafka ensures that fluctuations in stream velocity do not impact database performance.
- **Scalability:** The use of topics and partitions allows for horizontal scaling, enabling multiple consumer instances to process data in parallel if required.

### 3. Persistence Layer (The Consumer)
The \kafka-consumer-database\ module is responsible for the final data transformation and storage.
- **Asynchronous Consumption:** It utilizes a Kafka Listener to pull messages from the broker, ensuring the system remains responsive.
- **Data Modeling:** Using Spring Data JPA, raw event payloads are mapped to structured entities and persisted, providing a reliable audit trail of global Wikimedia activity.

## Technical Foundation

| Component | Technology | Role |
| :--- | :--- | :--- |
| **Microservices** | Spring Boot | Application framework and dependency management |
| **Stream Processing** | Spring Kafka | Integration with the Kafka ecosystem |
| **Data Ingestion** | OkHttp / EventSource | Connection management for real-time SSE streams |
| **Persistence** | JPA / Hibernate | Object-Relational Mapping and database abstraction |
| **Storage** | PostgreSQL | Relational storage for event metadata |

## Extensibility and Configuration

The system is designed with a "configure once, run anywhere" mindset. All infrastructure details—including broker addresses, stream endpoints, and database connection strings—are managed through externalized configuration. This ensures the pipeline remains flexible and can be seamlessly deployed across different environments (Development, Staging, or Production) without requiring code modifications.

---
*This architecture serves as a reference implementation for modern Event-Driven Architectures (EDA) and real-time analytical pipelines.*
