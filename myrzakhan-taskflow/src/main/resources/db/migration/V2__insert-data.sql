INSERT INTO user_data (first_name, last_name, email, active, created_at, updated_at)
VALUES
('Erkebulan', 'Myrzakhan', 'erkebulan.myrzakhan04@gmail.com', True, now(), now()),
('John', 'Doe', 'john.doe@example.com', TRUE, NOW(), NOW()),
('Alice', 'Smith', 'alice.smith@example.com', TRUE, NOW(), NOW()),
('Bob', 'Brown', 'bob.brown@example.com', TRUE, NOW(), NOW());

INSERT INTO project (name, description, status, owner_id, created_at, updated_at)
VALUES
('Task Management System', 'A system for managing tasks', 'ACTIVE', 1, NOW(), NOW()),
('E-Commerce Platform', 'Online store with payments', 'ACTIVE', 2, NOW(), NOW());

INSERT INTO task (title, description, status, priority, deadline, created_at, updated_at, assigned_user_id, project_id)
VALUES
('Setup Database', 'Initialize PostgreSQL database', 'TODO', 'HIGH', NOW(), NOW(), NOW(), 1, 1),
('Implement Login', 'Create authentication system', 'IN_PROGRESS', 'MEDIUM', NOW(), NOW(), NOW(), 2, 1),
('Design Homepage', 'Create UI for homepage', 'DONE', 'LOW', NOW(), NOW(), NOW(), 3, 2);

INSERT INTO comment (content, task_id, user_id, created_at, updated_at)
VALUES
('I have started working on this task.', 1, 1, NOW(), NOW()),
('Please review my PR.', 2, 2, NOW(), NOW()),
('Great work! Looks good to me.', 3, 3, NOW(), NOW());

INSERT INTO user_project VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 2);