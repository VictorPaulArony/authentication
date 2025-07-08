#!/bin/bash

# Test script for Authentication API
# Make this script executable: chmod +x test.sh
# Run with: ./test.sh

# Base URL of the API
BASE_URL="http://localhost:8080/api/auth"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print section headers
print_header() {
    echo -e "\n${BLUE}=== $1 ===${NC}"
}

# Function to print success message
print_success() {
    echo -e "${GREEN}$1${NC}"
}

# Test Student Registration
print_header "Testing Student Registration"
curl -s -X POST "$BASE_URL/student/register" \
-H "Content-Type: application/json" \
-d '{
    "username": "student1",
    "password": "student123"
}'

# Test Student Login
print_header "Testing Student Login"
curl -s -X POST "$BASE_URL/student/login" \
-H "Content-Type: application/json" \
-d '{
    "username": "student1",
    "password": "student123"
}'

# Test Teacher Registration
print_header "Testing Teacher Registration"
curl -s -X POST "$BASE_URL/teacher/register" \
-H "Content-Type: application/json" \
-d '{
    "username": "teacher1",
    "password": "teacher123",
    "email": "teacher1@school.edu"
}'

# Test Teacher Login
print_header "Testing Teacher Login"
curl -s -X POST "$BASE_URL/teacher/login" \
-H "Content-Type: application/json" \
-d '{
    "username": "teacher1",
    "password": "teacher123"
}'

# Test Invalid Login Attempt
print_header "Testing Invalid Login Attempt"
curl -s -X POST "$BASE_URL/student/login" \
-H "Content-Type: application/json" \
-d '{
    "username": "nonexistent",
    "password": "wrongpassword"
}'

# Test Duplicate Username Registration
print_header "Testing Duplicate Username Registration"
curl -s -X POST "$BASE_URL/student/register" \
-H "Content-Type: application/json" \
-d '{
    "username": "student1",
    "password": "anotherpassword"
}'

echo -e "\n${GREEN}Test script completed!${NC}"
