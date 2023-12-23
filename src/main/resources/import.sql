INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Karol', 'Karol', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'dogier2509@gmail.com', 'ADMIN', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Jan', 'Kowalski', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'dasdas@gmail.com', 'USER', true);
INSERT INTO designer (firstName, lastName) VALUES ('John', 'Doe'), ('Alice', 'Smith');
INSERT INTO task (taskName, description, assigned_designer_id, taskStatus, createdAt) VALUES ('First Task', 'Description for the first task', 1, 'ACTIVE', '2023-01-01 12:00:00');
INSERT INTO task (taskName, description, assigned_designer_id, taskStatus, createdAt) VALUES ('Second Task', 'Description for the second task', 2, 'CLOSED', '2023-01-02 14:30:00');
INSERT INTO task (taskName, description, assigned_designer_id, taskStatus, createdAt) VALUES ('Third Task', 'Description for the third task', 2, 'ACTIVE', '2023-01-03 18:45:00');
