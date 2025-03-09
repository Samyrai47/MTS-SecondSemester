CREATE TABLE users
(
    id BIGINT PRIMARY KEY,
    email VARCHAR(30) NOT NULL,
    name VARCHAR(30) NOT NULL,
    password TEXT NOT NULL
);
