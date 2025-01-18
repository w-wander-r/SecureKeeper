DROP DATABASE IF EXISTS `secure_keeper_data`;
CREATE DATABASE `secure_keeper_data`;
USE `secure_keeper_data`;

CREATE TABLE users (
    user_id LONG NOT NULL PRIMARY KEY,
    user_name VARCHAR(255) UNIQUE NOT NULL,
    user_pswrd VARCHAR(255) NOT NULL
);

INSERT INTO users (user_id, user_name, user_pswrd)
VALUES
(1, 'root', 'root')