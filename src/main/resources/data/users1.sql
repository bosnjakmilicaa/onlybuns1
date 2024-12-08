-- Insert users
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser2@example.com', 'password123', 'tester2', 'admin', 'Test', 'User2', '456 Test Avenue', TRUE),
    ('testuser3@example.com', 'password123', 'tester3', 'admin', 'Test', 'User3', '457 Test Avenue', TRUE),
    ('testuser1@example.com', 'password123', 'tester1', 'registered_user', 'Test', 'User1', '459 Test Avenue', TRUE),
    ('testuser4@example.com', 'password123', 'tester4', 'registered_user', 'Test', 'User4', '450 Test Avenue', TRUE),
    ('testuser5@example.com', 'password123', 'tester5', 'registered_user', 'Test', 'User5', '451 Test Avenue', TRUE),
    ('testuser6@example.com', 'password123', 'tester8', 'registered_user', 'Test', 'User6', '450 Test Avenue', TRUE),
    ('testuser7@example.com', 'password123', 'tester7', 'registered_user', 'Test', 'User7', '452 Test Avenue', TRUE),
    ('testuser8@example.com', 'password123', 'tester9', 'registered_user', 'Test', 'User8', '452 Test Avenue', TRUE);

-- Insert posts
INSERT INTO posts (is_deleted, user_id, description, image_url, created_at)
VALUES
    (false, 4, 'content1', '/images/4.jpeg', '2024-11-11 14:30:00'),
    (false, 5, 'content1', '/images/5.jpeg', '2024-11-04 10:00:00'),
    (false, 5, 'content2', '/images/7.jpeg', '2024-11-03 18:45:00'),
    (false, 5, 'content3', '/images/3.jpeg', '2024-11-02 09:20:00'),
    (false, 7, 'content1', '/images/6.jpeg', '2024-11-04 13:00:00'),
    (false, 7, 'content2', '/images/2.jpeg', '2024-11-03 11:30:00');

-- Insert comments
INSERT INTO comments (post_id, user_id, content, created_at)
VALUES
    (1, 7, 'com1', '2024-11-06 12:00:00'),
    (1, 5, 'com2', '2024-11-06 11:00:00'),
    (2, 7, 'com1', '2024-11-05 09:00:00'),
    (1, 4, 'com3', '2024-11-06 08:00:00'),
    (6, 7, 'com1', '2024-11-05 12:00:00'),
    (5, 5, 'com1', '2024-11-05 12:10:00');


-- Insert likes
INSERT INTO likes (post_id, user_id, liked_at)
VALUES
    (1, 4, '2024-11-06 13:00:00'),
    (1, 5, '2024-11-06 13:05:00'),
    (2, 7, '2024-11-05 14:00:00'),
    (2, 5, '2024-11-05 14:10:00'),
    (2, 4, '2024-11-05 14:15:00'),
    (3, 4, '2024-11-05 15:00:00'),
    (3, 5, '2024-11-05 15:10:00'),
    (3, 7, '2024-11-05 15:20:00'),
    (5, 5, '2024-11-05 16:00:00');


-- Insert follows
INSERT INTO follows (follower_id, followed_id)
VALUES
    (4, 5),  -- User4 prati User5
    (5, 4),  -- User5 prati User4
    (7, 5),  -- User7 prati User5
    (5, 7),  -- User5 prati User7
    (4, 7)  -- User4 prati User7


