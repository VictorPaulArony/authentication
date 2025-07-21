# authentication
This is a simple authentication system that uses JWT for authentication.

## Core Features
- Multi-user registration/login (Students, Teachers, Institutions)
- JWT token authentication with configurable expiration
- RESTful API endpoints for authentication operations
- H2 in-memory database (with plans to migrate to PostgreSQL)
- Comprehensive test suite with unit, integration, and API tests using JaCoCo for coverage

## Key Components
- Entities: Student, Teacher, Institution, Courses, Assessments
- Security: Spring Security with JWT filters and password encryption
- Database: Schema supports educational features like courses, assessments, and institutional management
- Testing: Comprehensive test suite with unit, integration, and API tests using JaCoCo for coverage

## API Endpoints
- `POST /api/auth/register/{learner|teacher|institution}`
- `POST /api/auth/login/{learner|teacher|institution}`
- `GET /api/protected/*` - Protected endpoints requiring JWT tokens

## Tech Stack
- Java 21, Spring Boot 3.5.3, Spring Security
- JWT (io.jsonwebtoken), H2 Database, Maven
- JUnit 5, Mockito for testing

## Getting Started
1. Clone the repository
2. Run `mvn clean install`
3. Run `mvn spring-boot:run`
4. The application will start on port 8080
5. You can access the H2 console at `http://localhost:8080/h2-console`
6. The default username is `sa` and the password is empty
7. The database name is `studentdb`

## Testing
1. Run `mvn test` to run all tests
2. Run `mvn jacoco:report` to generate coverage report
3. The coverage report will be generated in `target/site/jacoco/index.html`
4. The tests are also configured to run on GitHub Actions
5. The coverage report will be generated on each push to the repository
6. The coverage report will be available in the GitHub Actions tab