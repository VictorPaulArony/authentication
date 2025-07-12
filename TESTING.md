# Authentication Service - Testing Guide

## Overview
This document provides comprehensive guidelines for testing the Authentication Service, which handles user registration and login for both students and teachers.

## Table of Contents
- [Test Environment Setup](#test-environment-setup)
- [Running Tests](#running-tests)
- [Test Categories](#test-categories)
- [Manual Testing](#manual-testing)
- [Test Data Management](#test-data-management)
- [Expected Responses](#expected-responses)
- [Performance Testing](#performance-testing)
- [Security Testing](#security-testing)
- [Troubleshooting](#troubleshooting)

## Test Environment Setup

### Prerequisites
- Java 17 or higher  <!-- have the latest java(24) -->
- Maven 3.6.3 or higher
- Running MySQL database
- cURL API testing

### Configuration
1. Ensure the test database is properly configured in `application-test.properties`
2. The test profile uses an in-memory H2 database for integration tests <!---for now we are using H2 db but soon changing to postgeSQL-->
3. Test properties are automatically loaded from `src/test/resources/application-test.properties`

## Running Tests

### Running All Tests
```bash
mvn clean test
```

### Running Specific Test Classes
```bash
# Run API tests only
mvn test -Dtest=ApiTests

# Run integration tests only
mvn test -Dtest=IntegrationTests
```

### Running Individual Test Methods
```bash
mvn test -Dtest=ApiTests#whenRegisterStudent_thenSuccess
mvn test -Dtest=IntegrationTests#whenRegisterAndLoginStudent_thenSuccess
```

### Test Reports
Test reports are generated in:
- `target/surefire-reports/` - Surefire test reports(this will be where most of the tests will be displayed for the QA)
- `target/site/jacoco/` - Code coverage reports (same to this)

## Test Categories

### 1. API Tests (`ApiTests.java`)
These tests verify the HTTP endpoints using `TestRestTemplate`.

**Test Cases:**
- **Student Registration**
  - Successful registration
  - Registration with existing username
- **Student Login**
  - Successful login with valid credentials
  - Login with invalid password
  - Login with non-existent user
- **Teacher Registration**
  - Successful registration
  - Registration with existing username
- **Teacher Login**
  - Successful login with valid credentials
  - Login with invalid password

### 2. Integration Tests (`IntegrationTests.java`)
These tests verify the integration between controllers, services, and repositories.

**Test Cases:**
- **Student Flow**
  - Successful registration and login
  - Password encryption verification
- **Teacher Flow**
  - Successful registration and login
  - Email storage verification
- **Negative Scenarios**
  - Duplicate username registration
  - Invalid login attempts

## Manual Testing

### Using cURL

#### Student Registration
```bash
curl -X POST http://localhost:8080/api/auth/register/student \
  -H "Content-Type: application/json" \
  -d '{"username":"test_student","password":"test123"}'
```

#### Student Login
```bash
curl -X POST http://localhost:8080/api/auth/login/student \
  -H "Content-Type: application/json" \
  -d '{"username":"test_student","password":"test123"}'
```

#### Teacher Registration
```bash
curl -X POST http://localhost:8080/api/auth/register/teacher \
  -H "Content-Type: application/json" \
  -d '{"username":"test_teacher","password":"teach123","email":"teacher@test.com"}'
```

#### Teacher Login
```bash
curl -X POST http://localhost:8080/api/auth/login/teacher \
  -H "Content-Type: application/json" \
  -d '{"username":"test_teacher","password":"teach123"}'
```

### Using test.sh Script
1. Make the script executable:
   ```bash
   chmod +x test.sh
   ```
2. Run the script:
   ```bash
   ./test.sh
   ```

## Test Data Management
- Test data is automatically cleaned up after each test method
- Each test creates its own test data in the `@BeforeEach` method
- The cleanup script (`cleanup.sql`) resets the database state

## Expected Responses

### Success Responses (200 OK)
```json
{
  "message": "Registration successful",
  "status": 200
}
```

### Error Responses
- **400 Bad Request**
  - Duplicate username
  - Invalid credentials
  - Missing required fields
  ```json
  {
    "message": "Username already exists",
    "status": 400
  }
  ```

- **404 Not Found**
  - Non-existent endpoints

## Performance Testing

### Recommended Tools
- **JMeter** for load testing
- **Apache Benchmark (ab)** for simple load tests
- **Gatling** for advanced performance testing

### Test Scenarios
1. **Concurrent User Registration**
   - Test with 100-1000 concurrent users
   - Measure response times and error rates

2. **Login Performance**
   - Test login with valid and invalid credentials
   - Measure authentication time

3. **Token Validation**
   - Test JWT validation performance
   - Measure token generation and validation times

## Security Testing

### Test Cases
1. **SQL Injection**
   - Attempt SQL injection in all input fields
   - Verify proper input sanitization

2. **XSS (Cross-Site Scripting)**
   - Test input fields with script tags
   - Verify proper output encoding

3. **CSRF Protection**
   - Verify CSRF protection is enabled
   - Test with and without valid CSRF tokens

4. **JWT Security**
   - Test token expiration
   - Verify token signature validation
   - Test token tampering

## Troubleshooting

### Common Issues
1. **Database Connection Issues**
   - Verify database is running
   - Check connection properties in `application-test.properties`
   - Ensure test database is accessible

2. **Test Failures**
   - Check test logs in `target/surefire-reports/`
   - Verify database state before and after tests
   - Run tests with debug logging:
     ```bash
     mvn test -Dmaven.surefire.debug
     ```

3. **Environment Issues**
   - Verify Java and Maven versions
   - Check for port conflicts (default: 8080)
   - Ensure required environment variables are set

## Additional Resources
- [Spring Boot Testing Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [REST API Testing Best Practices](https://smartbear.com/learn/automated-testing/api-testing/)

## Support
For any testing-related issues, please contact the development team.