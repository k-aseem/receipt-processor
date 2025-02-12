# Receipt Processor Challenge

This project is an implementation of the **Receipt Processor Challenge**. It exposes two RESTful endpoints:

- **POST /receipts/process:**  
   Accepts a JSON receipt payload and calculates points based on several business rules. The endpoint returns a unique receipt ID.

- **GET /receipts/{id}/points:**  
   Returns a JSON object containing the points awarded for the receipt with the given ID.

The API follows the provided specification, including custom error messages:

- **Bad Request (400):** "The receipt is invalid."
- **Not Found (404):** "No receipt found for that ID."

## Challenge Details

Challenge details can be found here: [Fetch Rewards Receipt Processor Challenge](https://github.com/fetch-rewards/receipt-processor-challenge).

## Technologies Used

- **Java 17** (using Amazon Corretto 17)
- **Spring Boot 3.4.2**
- **Maven** for build management
- **Docker** for containerized execution
- **Jakarta Bean Validation** for input validation
- **Lombok** to reduce boilerplate code

## Getting Started

### Prerequisites

- **Docker:** Ensure Docker is installed and running on your machine.
- **Maven:** (Optional) if you wish to build the jar outside Docker.
- **Java 17:** (Optional) if you wish to run locally without Docker.

### Running the Application with Docker

1. **Clone the repository:**

   ```bash
   git clone https://github.com/k-aseem/receipt-processor.git
   cd receipt-processor
   ```

2. **Build the Docker image:**

   From the project root (where the Dockerfile is located), run:

   ```bash
   docker build -t receipt-processor .
   ```

   This uses a multi-stage build:

   - **Stage 1:** Compiles the project and packages the jar using Maven with Amazon Corretto 17.
   - **Stage 2:** Builds a slim runtime image using Amazon Corretto 17-alpine.

3. **Run the Docker container:**

   ```bash
   docker run -p 8080:8080 receipt-processor
   ```

   The application will start on port 8080.

### Testing the Endpoints

- **Process Receipt (POST):**

  Send a POST request to `http://localhost:8080/receipts/process` with a JSON payload like:

  ```json
  {
    "retailer": "Target",
    "purchaseDate": "2022-02-28",
    "purchaseTime": "13:01",
    "items": [
      { "shortDescription": "Mountain Dew 12PK", "price": "6.49" },
      { "shortDescription": "Emils Cheese Pizza", "price": "12.25" },
      { "shortDescription": "Knorr Creamy Chicken", "price": "1.26" },
      { "shortDescription": "Doritos Nacho Cheese", "price": "3.35" },
      { "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ", "price": "12.00" }
    ],
    "total": "35.35"
  }
  ```

  A successful response will be a JSON object with an `id` field:

  ```json
  { "id": "generated-uuid" }
  ```

- **Get Points (GET):**

  Send a GET request to `http://localhost:8080/receipts/{id}/points` (replace `{id}` with the generated id from the POST endpoint). A successful response will be:

  ```json
  { "points": 28 }
  ```

- **Validation Errors:**

  If the input is invalid (for example, an invalid date like "2022-02-30"), the application will return a 400 error with the message:

  ```json
  { "message": "The receipt is invalid." }
  ```

- **Receipt Not Found:**

  If you request points for a non‑existent receipt ID, the application will return a 404 error with:

  ```json
  { "message": "No receipt found for that ID." }
  ```

### Additional Configuration

No extra configuration is required to run the application via Docker. The provided Dockerfile builds and runs the application using Java 17. Ensure your Docker installation is properly set up and that you have network access to port 8080.

### Running Locally Without Docker

If you prefer to run the application locally:

1. **Build the project with Maven:**

   ```bash
   mvn clean package
   ```

2. **Run the jar file:**

   ```bash
   java -jar target/receipt-processor-0.0.1-SNAPSHOT.jar
   ```

## Project Structure

```plaintext
receipt-processor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── fetchrewards/
│   │   │           └── receipt_processor/
│   │   │               ├── controller/
│   │   │               │    └── ReceiptController.java
│   │   │               ├── exception/
│   │   │               │    ├── ReceiptNotFoundException.java
│   │   │               │    └── GlobalExceptionHandler.java
│   │   │               ├── model/
│   │   │               │    ├── Receipt.java
│   │   │               │    ├── Item.java
│   │   │               │    ├── ReceiptResponse.java
│   │   │               │    ├── PointsResponse.java
│   │   │               │    └── ErrorResponse.java
│   │   │               ├── service/
│   │   │               │    └── ReceiptService.java
│   │   │               └── util/
│   │   │                    └── Strings.java
│   │   └── resources/
│   │         └── application.properties
├── pom.xml
└── Dockerfile
```
