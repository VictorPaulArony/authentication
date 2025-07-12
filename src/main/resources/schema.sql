CREATE TABLE IF NOT EXISTS student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--the NOT NULL constraint are for registraition form fields
--the other constrains can be used in the settings of the profile page
CREATE TABLE IF NOT EXISTS teacher (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    professional_level VARCHAR(255), -- Education details of the teacher
    certification VARCHAR(255), -- Certifications held by the teacher
    profile_picture VARCHAR(255), -- URL to the teacher's profile picture
    bio TEXT, -- Short biography of the teacher
    year_of_experience INT, -- Years of experience in teaching
    course VARCHAR(255),
    language VARCHAR(50), -- Language the teacher is proficient in
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS institution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    schoolnamne VARCHAR(255) NOT NULL,
    registraition_number VARCHAR(255) NOT NULL, --the registration number of the school
    school_type ENUM('PRIMARY', 'SECONDARY', 'TERTIARY', 'COLLAGE', 'UNIVERSITY') NOT NULL, -- Type of school
    education_system ENUM('CBC', '8-4-4', 'IGCSE', 'IB', 'OTHER') NOT NULL, -- Education system followed by the school
    location VARCHAR(255) NOT NULL, -- Location of the school
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- these details can be used in the settings of the profile page
    postal_address VARCHAR(255), -- Postal address of the school
    website VARCHAR(255), -- Website of the school
    logo VARCHAR(255), -- URL to the school's logo
    description TEXT, -- Description of the school
    principal_name VARCHAR(255), -- Name of the principal
    principal_email VARCHAR(255) UNIQUE, -- Email of the principal
    principal_phone VARCHAR(20) UNIQUE, -- Phone number of the principal
    established_year INT, -- Year the school was established
    accreditation_status ENUM('ACCREDITED', 'PENDING', 'NOT_ACCREDITED') DEFAULT 'PENDING', -- Accreditation status of the school
    accreditation_body VARCHAR(255), -- Body that accredited the school
    accreditation_date TIMESTAMP, -- Date of accreditation
    created_by BIGINT, -- ID of the teacher who created the institution
    FOREIGN KEY (created_by) REFERENCES teacher(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration INT NOT NULL, -- Duration in weeks/months/years
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    mode ENUM('ONLINE', 'OFFLINE', 'HYBRID') NOT NULL, -- Mode of delivery
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    teacher_id BIGINT, -- ID of the teacher who created the course
    Payment_method ENUM('MPESA', 'CREDIT_CARD', 'PAYPAL', 'BANK_TRANSFER') DEFAULT 'CREDIT_CARD', -- Payment account for the course
    payment_account VARCHAR(255), -- Details of the payment account (e.g., MPESA number, credit card details)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS course_outline (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    order_index INT NOT NULL,
    content_type ENUM('PDF', 'VIDEO', 'LIVE', 'SLIDES') NOT NULL, 
    content_url VARCHAR(255), -- URL for the content (e.g., video, PDF)
    duration INT NOT NULL, -- Duration in minutes for v-- Type of content (PDF, Video, Text, Quiz)ideos or live sessions
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_enrollment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    payment_method ENUM('MPESA','CREDIT_CARD', 'PAYPAL', 'BANK_TRANSFER') DEFAULT 'CREDIT_CARD',
    UNIQUE (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

--payment_status can be used to track the payment status of the course enrollment
CREATE TABLE IF NOT EXISTS payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL, -- Amount paid
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method ENUM('MPESA', 'CREDIT_CARD', 'PAYPAL', 'BANK_TRANSFER') NOT NULL, -- Payment method used
    status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING', -- Status of the payment
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
    
);


CREATE TABLE IF NOT EXISTS course_session (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    course_outline_id BIGINT NOT NULL, -- ID of the course outline this session belongs to
    session_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration INT NOT NULL, -- Duration in minutes
    status ENUM('COMPLETED', 'IN_PROGRESS', 'NOT_STARTED') DEFAULT 'NOT_STARTED', -- Status of the session
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_outline_id) REFERENCES course_outline(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- CREATE TABLE IF NOT EXISTS course_progress (
--     id BIGINT PRIMARY KEY AUTO_INCREMENT,
--     student_id BIGINT NOT NULL,
--     course_id BIGINT NOT NULL,
--     outline_id BIGINT NOT NULL,
--     progress INT DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
--     FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
--     FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
--     FOREIGN KEY (outline_id) REFERENCES course_outline(id) ON DELETE CASCADE
-- );

CREATE TABLE IF NOT EXISTS assignment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content_type ENUM('PDF', 'VIDEO', 'TEXT', 'QUIZ') NOT NULL, -- Type of content (PDF, Video, Text, Quiz)
    content_url VARCHAR(255), 
    duration INT NOT NULL, 
    points INT NOT NULL, 
    created_by BIGINT NOT NULL, 
    due_date TIMESTAMP NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assignment_submission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    assignment_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content TEXT, -- Content of the submission
    grade INT, -- Grade given by the teacher
    feedback TEXT, -- Feedback from the teacher
    UNIQUE (assignment_id, student_id), -- Ensure a student can only submit once per assignment
    FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    session_type ENUM('LIVE', 'RECORDED', 'PDF') NOT NULL,
    content_type ENUM('PDF', 'VIDEO', 'TEXT', 'QUIZ') NOT NULL, -- Type of content (PDF, Video, Text, Quiz)
    content_url VARCHAR(255), -- URL for the content (e.g., video, PDF)

    content_url VARCHAR(255),
    duration INT NOT NULL, -- Duration in minutes
    points INT NOT NULL, -- Total points for the quiz
    
    course_id BIGINT NOT NULL,
    due_date TIMESTAMP NOT NULL,
    created_by BIGINT NOT NULL, -- ID of the teacher who created the quiz
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    question_type ENUM('MULTIPLE_CHOICE', 'TRUE_FALSE', 'SHORT_ANSWER') NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_option (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE, -- Indicates if this option is the correct answer
    FOREIGN KEY (question_id) REFERENCES quiz_question(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_submission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    content TEXT, -- Content of the submission (e.g., answers to questions)
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    score INT, -- Score obtained in the quiz
    feedback TEXT, -- Feedback from the teacher
    UNIQUE (quiz_id, student_id), -- Ensure a student can only submit once per quiz
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS grades (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    grade INT CHECK (grade >= 0 AND grade <= 100), -- Grade between 0 and 100
    feedback TEXT, -- Feedback from the teacher
    UNIQUE (student_id, course_id), -- Ensure a student can only have one grade per course
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    feedback_text TEXT, -- Feedback text provided by the student
    rating INT CHECK (rating >= 1 AND rating <= 5), -- Rating between 1 and 5
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);
