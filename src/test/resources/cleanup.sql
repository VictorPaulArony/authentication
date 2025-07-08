-- Clean up test data after each test
DELETE FROM student WHERE username LIKE 'integration_%';
DELETE FROM teacher WHERE username LIKE 'integration_%';
