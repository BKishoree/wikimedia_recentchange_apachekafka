# Wikimedia Kafka Real-time Stream Processing

This project is a Spring Boot multi-module application that processes real-time Wikimedia event streams using Apache Kafka. It consists of two main microservices: a producer that fetches data from Wikimedia and a consumer that stores it in a PostgreSQL database.

## Architecture

1.  **Wikimedia Producer**: Fetches real-time changes from the Wikimedia EventStreams API and publishes them to a Kafka topic.
2.  **Apache Kafka**: Acts as the message broker, decoupling the producer and consumer.
3.  **Wikimedia Consumer**: Listens to the Kafka topic and persists the event data into a PostgreSQL database.

## Project Structure

- \kafka-producer-wikimedia\: Spring Boot service that uses \okhttp-eventsource\ to read from \https://stream.wikimedia.org/v2/stream/recentchange\ and sends data to the Kafka topic \wikimedia_info_updates\.
- \kafka-consumer-database\: Spring Boot service that consumes messages from Kafka and saves them to a PostgreSQL database named \wikimedia\.

## Technologies Used

- **Java 17**
- **Spring Boot 3.x / 4.x**
- **Apache Kafka**
- **Spring Kafka**
- **Spring Data JPA**
- **PostgreSQL**
- **OkHttp / EventSource**
- **Lombok**
- **Maven**

## Prerequisites

- JDK 17 or higher
- Apache Kafka (Running on \localhost:9092\)
- PostgreSQL (Running on \localhost:5432\ with a database named \wikimedia\)

## Configuration

### Kafka Producer (\kafka-producer-wikimedia\)
- **Topic:** \wikimedia_info_updates\
- **Source URL:** \https://stream.wikimedia.org/v2/stream/recentchange\

### Kafka Consumer (\kafka-consumer-database\)
- **Group ID:** \myGroup\
- **Database Connection:**
    - URL: \jdbc:postgresql://localhost:5432/wikimedia\
    - Username: \postgres\
    - Password: \kishore\ (Update as per your local setup)

## How to Run

1.  **Start Kafka & Zookeeper**: Ensure your Kafka broker is up and running.
2.  **Create Database**: Create a database named \wikimedia\ in PostgreSQL.
3.  **Build Project**:
    \\\ash
    mvn clean install
    \\\
4.  **Run Consumer**:
    Navigate to \kafka-consumer-database\ and run:
    \\\ash
    mvn spring-boot:run
    \\\
5.  **Run Producer**:
    Navigate to \kafka-producer-wikimedia\ and run:
    \\\ash
    mvn spring-boot:run
    \\\

## Data Model

The data is stored in the \wikimedia_recent_info\ table with the following schema:

| Column | Type | Description |
| :--- | :--- | :--- |
| id | UUID | Primary Key (Auto-generated) |
| wiki_event_data | TEXT (Lob) | The raw event data in JSON format |
