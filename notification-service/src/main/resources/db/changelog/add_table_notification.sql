CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT,
    title VARCHAR(255),
    status VARCHAR(255),
    timestamp TIMESTAMP DEFAULT NOW()
);
