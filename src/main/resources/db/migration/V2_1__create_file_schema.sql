CREATE TABLE files
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    content TEXT,
    user_id BIGINT REFERENCES users (id)
);
