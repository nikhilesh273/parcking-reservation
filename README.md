# Parking Reservation System
A robust, RESTful parking slot reservation service with concurrent booking protection, time-based availability, vehicle-type validation, and pagination support.

## Features
- Reserve parking slots with time window validation (max 24 hours)
- Real-time slot availability checking (excludes only ACTIVE reservations)
- Concurrent booking protection using pessimistic locking
- Vehicle number validation (Indian format: KA05MH1234)
- Dynamic pricing based on vehicle type and duration
- Paginated & sortable availability endpoint (?sort=slotNumber,asc)
- Comprehensive error handling with '@RestControllerAdvice'
- Full CRUD: Reserve, View, Cancel reservations

##  Tech Stack
- Java 17+
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- H2 Database (for dev; easily switchable to PostgreSQL/MySQL)
- Maven
- Lombok
- Jakarta Validation

## Setup & Run Instructions
1. Clone the Repository
- git clone https://github.com/nikhilesh273/parcking-reservation.git
- cd parking-reservation

2. Build the Project
- ./mvnw clean install
 OR on Windows
- mvnw.cmd clean install

3. Run the Application
- ./mvnw spring-boot:run
- 
- java -jar target/parking-reservation-*.jar

## The app starts on http://localhost:8080
## H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb, User: sa, Password: password)
## Swagger UI: http://localhost:8080/swagger-ui/index.html
## API Docs: http://localhost:8080/v3/api-docs

## API Endpoints
- POST /api/reservations - Create a new reservation
- GET /api/reservations/{id} - Get reservation by ID
- GET /api/reservations - List all reservations
- DELETE /api/reservations/{id} - Cancel a reservation
- GET /api/slots/availability - Check available slots with pagination and sorting
- GET /api/slots/pricing - Get pricing details based on vehicle type and duration
- GET /api/slots/{slotNumber}/reservations - Get reservations for a specific slot
- GET /api/slots/{slotNumber}/availability - Check availability for a specific slot
- GET /api/slots - List all parking slots

## Testing
- Run unit and integration tests using:
- ./mvnw test
 OR on Windows
- mvnw.cmd test
