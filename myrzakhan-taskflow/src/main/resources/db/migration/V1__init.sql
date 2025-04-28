CREATE TABLE user_data(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE project(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    owner_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_owner_project FOREIGN KEY (owner_id) REFERENCES user_data(id)
);

CREATE TABLE task(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    priority VARCHAR(50),
    deadline TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    assigned_user_id BIGINT,
    project_id BIGINT,
    CONSTRAINT fk_project_task FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_user_task FOREIGN KEY (assigned_user_id) REFERENCES user_data(id)
);

CREATE TABLE user_project(
    user_id BIGINT,
    project_id BIGINT,
    PRIMARY KEY (user_id, project_id),
    CONSTRAINT fk_user_project_user FOREIGN KEY (user_id) REFERENCES user_data(id),
    CONSTRAINT fk_user_project_project FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE comment(
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    task_id BIGINT,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES task(id),
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES user_data(id)
);