CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT,
    title VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT NOW() NOT NULL
);
