# Authentication Service - Testing Guide

This document provides comprehensive instructions for running and understanding the tests in the authentication service.

## Table of Contents
- [Test Types](#test-types)
- [Running Tests](#running-tests)
- [Test Coverage](#test-coverage)
- [Test Structure](#test-structure)
- [Test Data](#test-data)
- [Continuous Integration](#continuous-integration)
- [Troubleshooting](#troubleshooting)

## Test Types

The project includes three types of tests:

1. **Unit Tests** (`AuthenticationApplicationTests.java`)
   - Test individual components in isolation
   - Use mocking for dependencies
   - Focus on business logic

2. **Integration Tests** (`IntegrationTests.java`)
   - Test the interaction between components
   - Use an in-memory H2 database
   - Verify database operations and business logic

3. **API Tests** (`ApiTests.java`)
   - Test the actual HTTP endpoints
   - Simulate real HTTP requests
   - Verify request/response flows and error handling

## Running Tests

### Prerequisites
- Java 17 or higher
- Maven (included in the project as `mvnw`)

### Running All Tests

```bash
./mvnw clean test
```

### Running Specific Test Classes

1. Run unit tests only:
   ```bash
   ./mvnw test -Dtest=AuthenticationApplicationTests
   ```

2. Run integration tests only:
   ```bash
   ./mvnw test -Dtest=IntegrationTests
   ```

3. Run API tests only:
   ```bash
   ./mvnw test -Dtest=ApiTests
   ```

### Running a Single Test Method

```bash
./mvnw test -Dtest=AuthenticationApplicationTests#testStudentRegistration
```

## Test Coverage

### Generating Coverage Reports

1. Run tests with coverage:
   ```bash
   ./mvnw clean test jacoco:report
   ```

2. View the coverage report:
   - Open `target/site/jacoco/index.html` in a web browser
   - The report shows coverage by package, class, and method

### Coverage Requirements

The build enforces minimum coverage thresholds:
- 80% instruction coverage
- 70% branch coverage

To check if the project meets these thresholds:

```bash
./mvnw clean verify
```

## Test Structure

### Test Classes

1. **`AuthenticationApplicationTests`**
   - Unit tests for service layer
   - Mocks dependencies using Mockito
   - Tests both success and error scenarios

2. **`IntegrationTests`**
   - Tests the full Spring context
   - Uses an in-memory H2 database
   - Tests database operations and transactions

3. **`ApiTests`**
   - Tests REST endpoints using TestRestTemplate
   - Verifies HTTP status codes and responses
   - Tests error scenarios and edge cases

### Test Configuration

- Test properties: `src/test/resources/application-test.properties`
- Test data cleanup: `src/test/resources/cleanup.sql`
- Test database: In-memory H2

## Test Data

### Database Setup

- The test database is automatically created and destroyed
- Schema is created from `schema.sql`
- Test data is inserted by the test methods
- Cleanup scripts run after each test

### Test Users

**Students:**
- Username: `teststudent`
- Password: `password123`

**Teachers:**
- Username: `testteacher`
- Email: `teacher@example.com`
- Password: `password123`

## Continuous Integration

### GitHub Actions

To set up GitHub Actions for CI:

1. Create `.github/workflows/build.yml` with:
   ```yaml
   name: Build and Test

   on: [push, pull_request]

   jobs:
     build:
       runs-on: ubuntu-latest
       
       steps:
       - uses: actions/checkout@v3
       - name: Set up JDK 17
         uses: actions/setup-java@v3
         with:
           java-version: '17'
           distribution: 'temurin'
       - name: Build and test with Maven
         run: ./mvnw clean verify
   ```

## Troubleshooting

### Common Issues

1. **Tests fail with database errors**
   - Make sure the H2 database is properly configured
   - Check that the schema is created correctly

2. **Coverage not updating**
   - Run `mvn clean` before generating reports
   - Make sure tests are actually running

3. **Port conflicts**
   - The tests use random ports by default
   - If you see port conflicts, check for other running instances

### Debugging Tests

To run tests in debug mode:

```bash
./mvnw test -Dmaven.surefire.debug
```

Then attach a remote debugger to port 5005.

### Viewing Database During Tests

To inspect the test database:

1. Add this to `application-test.properties`:
   ```properties
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   ```

2. Access the console at: http://localhost:8080/h2-console
3. Use JDBC URL: `jdbc:h2:mem:testdb`
4. Leave username as `sa` and password empty
