INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Karol', 'Karol', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'dogier2509@gmail.com', 'ADMIN', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Jan', 'Kowalski', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'dasdas@gmail.com', 'DESIGNER', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Andrzej', 'Operator', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'dasdas1@gmail.com', 'OPERATOR', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Jane', 'Smith', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'jane.smith@example.com', 'OPERATOR', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Alice', 'Johnson', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'alice.johnson@example.com', 'DESIGNER', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Eva', 'Anderson', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'eva.anderson@example.com', 'OPERATOR', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Charlie', 'Miller', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'charlie.miller@example.com', 'DESIGNER', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('David', 'Harris', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'david.harris@example.com', 'OPERATOR', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Sophie', 'Martin', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'sophie.martin@example.com', 'DESIGNER', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Tom', 'Johnson', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'tom.johnson@example.com', 'OPERATOR', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Emma', 'White', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'emma.white@example.com', 'DESIGNER', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Oliver', 'Lee', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'oliver.lee@example.com', 'OPERATOR', true);
INSERT INTO user (firstName, lastName, password, email, role, isEnabled) VALUES ('Mia', 'Clark', '$2a$10$QiiOVSnnNeYq.qT.rdGthu6JL1klNn6J0hs63WMXzb12jNOk2.2xi', 'mia.clark@example.com', 'DESIGNER', true);
INSERT INTO task (taskName, description, assigned_designer_id, taskStatus, createdAt) VALUES ('First Task', 'Description for the first task', 1, 'ACTIVE', '2023-01-01 12:00:00');
INSERT INTO task (taskName, description, assigned_designer_id, taskStatus, createdAt) VALUES ('Second Task', 'Description for the second task', 2, 'CLOSED', '2023-01-02 14:30:00');
INSERT INTO task (taskName, description, assigned_designer_id, taskStatus, createdAt) VALUES ('Third Task', 'Description for the third task', 2, 'ACTIVE', '2023-01-03 18:45:00');
INSERT INTO working_time (createdAt, workStarted, workFinished, currentWorkingTime, overallWorkingTime, isWorking, theUser_id, task_id) VALUES ('2023-01-01 08:00:00', '2024-01-03 09:00:00', '2024-01-03 16:00:00', 0, 7, true, 1, 1);
INSERT INTO working_time (createdAt, workStarted, workFinished, currentWorkingTime, overallWorkingTime, isWorking, theUser_id, task_id) VALUES ('2023-01-02 08:30:00', '2023-01-02 09:30:00', '2023-01-02 16:30:00', 0, 8, false, 1, 2);
INSERT INTO working_time (createdAt, workStarted, workFinished, currentWorkingTime, overallWorkingTime, isWorking, theUser_id, task_id) VALUES ('2023-01-03 09:15:00', '2023-01-03 10:00:00', '2023-01-03 18:45:00', 0, 8, false, 1, 3);
