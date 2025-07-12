# API Endpoints Summary 

## Authentication
- POST `/api/auth/register/learner` - Register as learner 
- POST `/api/auth/register/teacher` - Register as teacher 
- POST `/api/auth/login/learner` - Login as learner 
- POST `/api/auth/login/teacher` - Login as teacher 

## Learner
- GET `/api/learner/courses` - Get all courses available 
- GET `/api/learner/courses/{courseId}` - Get course details by id 
- GET `/api/learner/courses/{courseId}/enrollments` - Get enrollments for a course 
- GET `/api/learner/courses/{courseId}/enrollments/{studentId}` - Get enrollment for a course by student id 
- POST `/api/learner/courses/{courseId}/enrollments` - Enroll in a course 
- DELETE `/api/learner/courses/{courseId}/enrollments/{studentId}` - Unenroll from a course 
- GET `/api/learner/courses/{courseId}/enrollments/{studentId}/progress` - Get progress for a course by student id 
- GET `/api/learner/sessions/{courseId}/{studentId}` - Get sessions for a course by student id 
- GET `/api/learner/assignments/{courseId}/{studentId}/{assignmentId}` - Get assignment for a course by student id
- POST `/api/learner/assignments/{courseId}/{studentId}/submissions` - Submit assignment for a course by student id 
- POST `/api/learner/quizzes/{courseId}/{studentId}/{quizId}/submissions` - Submit quiz for a course by student id
- GET `/api/learner/grades/{courseId}` - Get grades for a course 
- GET `/api/learner/feedback/{courseId}` - Get feedback/reviews for a course 

## Teacher
- GET `/api/teacher/courses` - Get all courses available 
- GET `/api/teacher/courses/{courseId}` - Get course details by id 
- GET `/api/teacher/courses/{courseId}/enrollments` - Get enrollments for a course 
- GET `/api/teacher/courses/{courseId}/enrollments/{studentId}` - Get enrollment for a course by student id 
- POST `/api/teacher/courses/{courseId}/enrollments` - Enroll in a course 
- DELETE `/api/teacher/courses/{courseId}/enrollments/{studentId}` - Unenroll from a course 
- GET `/api/teacher/courses/{courseId}/enrollments/{studentId}/progress` - Get progress for a course by student id 
- GET `/api/teacher/sessions/{sessionId}/attendance` - Get attendance for a session 
- POST `/api/teacher/courses/{courseId}/assignments` - Create assignment for a course 
- GET `/api/teacher/assignments/{assignmentId}/submissions` - Get submissions for an assignment 
- POST `/api/teacher/submissions/{assignmentId}/grade` - Grade a submission 
- POST `/api/teacher/courses/{courseId}/quizzes` - Create quiz for a course 
- GET `/api/teacher/quizzes/{quizId}/submissions` - Get submissions for a quiz 
- POST `/api/teacher/quizzes/{quizId}/submissions/{submissionId}/grade` - Grade a quiz submission 
- GET `/api/teacher/courses/{courseId}/feedback` - Get feedback/reviews for a course



