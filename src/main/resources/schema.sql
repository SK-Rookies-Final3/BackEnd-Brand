CREATE DATABASE IF NOT EXISTS branddb;
USE branddb;
INSERT INTO store (id, user_id, name, license_number, status, registered_at)
VALUES (50001, 1, 'Example Store', 12345, 0, NOW());
