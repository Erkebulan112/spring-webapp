INSERT INTO user_data VALUES
(1, 'Erkebulan', 'Myrzakhan', 'erkebulan.myrzakhan04@gmail.com', True, now(), now()),
(2 ,'John', 'Doe', 'john.doe@example.com', TRUE, NOW(), NOW()),
(3, 'Alice', 'Smith', 'alice.smith@example.com', TRUE, NOW(), NOW()),
(4, 'Bob', 'Brown', 'bob.brown@example.com', TRUE, NOW(), NOW());

INSERT INTO project VALUES
(1, 'Task Management System', 'A system for managing tasks', 'ACTIVE', 1, NOW(), NOW()),
(2, 'E-Commerce Platform', 'Online store with payments', 'ACTIVE', 2, NOW(), NOW());

INSERT INTO task VALUES
(1, 'Setup Database', 'Initialize PostgreSQL database', 'TODO', 'HIGH', NOW(), NOW(), NOW(), 1, 1),
(2, 'Implement Login', 'Create authentication system', 'IN_PROGRESS', 'MEDIUM', NOW(), NOW(), NOW(), 2, 1),
(3, 'Design Homepage', 'Create UI for homepage', 'DONE', 'LOW', NOW(), NOW(), NOW(), 3, 2);

INSERT INTO comment VALUES
(1, 'I have started working on this task.', 1, 1, NOW(), NOW()),
(2, 'Please review my PR.', 2, 2, NOW(), NOW()),
(3, 'Great work! Looks good to me.', 3, 3, NOW(), NOW());

INSERT INTO user_project VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 2);