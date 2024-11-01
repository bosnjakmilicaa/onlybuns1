INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser2@example.com', 'password123', 'tester2', 'admin', 'Test', 'User2', '456 Test Avenue', TRUE);
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser3@example.com', 'password123', 'tester3', 'admin', 'Test', 'User3', '457 Test Avenue', TRUE);
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser1@example.com', 'password123', 'tester1', 'unregistered_user', 'Test', 'User1', '459 Test Avenue', TRUE);
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser4@example.com', 'password123', 'tester4', 'registered_user', 'Test', 'User4', '450 Test Avenue', TRUE);
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser5@example.com', 'password123', 'tester5', 'registered_user', 'Test', 'User5', '451 Test Avenue', TRUE);
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser6@example.com', 'password123', 'tester8', 'unregistered_user', 'Test', 'User6', '450 Test Avenue', TRUE);
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser7@example.com', 'password123', 'tester7', 'registered_user', 'Test', 'User7', '452 Test Avenue', TRUE);

INSERT INTO posts(user_id, content) VALUES (4, 'content1');
INSERT INTO posts(user_id, content) VALUES (5, 'content1');
INSERT INTO posts(user_id, content) VALUES (5, 'content2');
INSERT INTO posts(user_id, content) VALUES (5, 'content3');
INSERT INTO posts(user_id, content) VALUES (7, 'content1');
INSERT INTO posts(user_id, content) VALUES (7, 'content2');


INSERT INTO comments(post_id,user_id, content) VALUES (1,7, 'com1');
INSERT INTO comments(post_id,user_id, content) VALUES (1,5, 'com2');
INSERT INTO comments(post_id,user_id, content) VALUES (2,7, 'com1');
INSERT INTO comments(post_id,user_id, content) VALUES (1,4, 'com3');
INSERT INTO comments(post_id,user_id, content) VALUES (6,7, 'com1');
INSERT INTO comments(post_id,user_id, content) VALUES (5,5, 'com1');
