# Complaint Service

This is REST service for managing complaints.

## How to use:

1. To create a new complaint, send a POST request to `/api/v1/complaints` with the following JSON payload:
   ```json
   {
       "content": "The product is faulty",
       "reportedBy": "John Doe",
       "productId": "123"
   }
   ```

2. To get a complaint, send a GET request to `/api/v1/complaints/{id}`.

## Example Response:

For the POST request above, the response would look like:

```json
{
  "productId": "12345",
  "content": "The product is faulty",
  "reportedBy": "John Doe",
  "country": "US"
}
```

3. To update a complaint, send a PUT request to `/api/v1/complaints/{id}`.

## Example Response:

For the POST request above, the response would look like:

```json
{
  "productId": "12345",
  "content": "The product is faulty",
  "reportedBy": "John Doe",
  "country": "US"
}
```

### Dependencies:

- Spring Boot
- H2 Database
- Resilience4j for circuit breaker
- MapStruct for object mapping

### Future improvements

- add optimistic locking to prevent multithreading issues for submissionCount in CompliantServiceImpl
- add migration scripts in liquibase
- add endpoint to return all compliants with filtering
- add test coverage with wiremock and test CountryClient
- think about better fallback method for countryClient
