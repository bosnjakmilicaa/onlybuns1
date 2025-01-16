-- Insert users
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active, latitude, longitude)
VALUES
    ('testuser2@example.com', 'password123', 'tester2', 'admin', 'Test', 'User2', '456 Test Avenue', TRUE, 45.2671, 19.8335),
    ('testuser3@example.com', 'password123', 'tester3', 'admin', 'Test', 'User3', '457 Test Avenue', TRUE, 45.2680, 19.8345),
    ('testuser1@example.com', 'password123', 'tester1', 'registered_user', 'Test', 'User1', '459 Test Avenue', TRUE, 45.2655, 19.8360),
    ('testuser4@example.com', 'password123', 'tester4', 'registered_user', 'Test', 'User4', '450 Test Avenue', TRUE, 45.2660, 19.8320),
    ('testuser5@example.com', 'password123', 'tester5', 'registered_user', 'Test', 'User5', '451 Test Avenue', TRUE, 45.2675, 19.8330),
    ('testuser6@example.com', 'password123', 'tester6', 'registered_user', 'Test', 'User6', '450 Test Avenue', TRUE, 45.2668, 19.8348),
    ('testuser7@example.com', 'password123', 'tester7', 'registered_user', 'Test', 'User7', '452 Test Avenue', TRUE, 45.2679, 19.8359),
    ('testuser8@example.com', 'password123', 'tester8', 'registered_user', 'Test', 'User8', '452 Test Avenue', TRUE, 45.2672, 19.8327),
    ('tanja7bajunovic@gmail.com', 'password123', 'tanja123', 'registered_user', 'Tanja', 'Civic', '452 Test Avenue', TRUE, 45.2665, 19.8332);

-- Insert posts
INSERT INTO posts (is_deleted, user_id, description, image_url, created_at)
VALUES
    (false, 4, 'content1', '/images/4.jpeg', '2024-11-11 14:30:00'),
    (false, 5, 'content1', '/images/5.jpeg', '2024-11-04 10:00:00'),
    (false, 5, 'content2', '/images/7.jpeg', '2024-11-03 18:45:00'),
    (false, 5, 'content3', '/images/3.jpeg', '2024-11-02 09:20:00'),
    (false, 7, 'content1', '/images/6.jpeg', '2024-11-04 13:00:00'),
    (false, 7, 'content2', '/images/2.jpeg', '2024-11-03 11:30:00'),
    (false, 9, 'content2', '/images/9.jpg', '2024-11-03 11:30:00'),
    (false, 9, 'content2', '/images/13.png', '2024-12-12 11:30:00'),
    (false, 5, 'content2', '/images/10.png', '2024-11-03 11:30:00'),
    (false, 9, 'content2', '/images/11.png', '2024-12-03 11:30:00'),
    (false, 9, 'content2', '/images/12.png', '2024-12-12 11:30:00');

INSERT INTO posts (image_url, description, user_id, is_deleted, created_at, latitude, longitude)
VALUES (
    '/images/14.png',   -- URL slike
    'Post ', -- Opis posta
    7,                                -- ID korisnika (postojeći user_id iz tabele korisnika)
    false,                                -- Nije obrisano (false)
    NOW(),                            -- Trenutno vreme za kreiranje posta
    45.2539,                          -- Geografska širina (latitude)
    19.8482
);


-- Insert comments
INSERT INTO comments (post_id, user_id, content, created_at)
VALUES
    (1, 7, 'com1', '2024-11-06 12:00:00'),
    (1, 3, 'comment', '2024-11-06 12:00:00'),
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
    (2, 4, '2024-12-02 14:15:00'),
    (3, 4, '2024-12-12 15:00:00'),
    (3, 5, '2024-12-12 15:10:00'),
    (3, 7, '2024-12-12 15:20:00'),
    (5, 5, '2024-12-12 16:00:00'),
    (8, 5, '2024-12-12 23:00:00');


-- Insert follows
INSERT INTO follows (follower_id, followed_id)
VALUES
    (4, 5),  -- User4 prati User5
    (5, 4),  -- User5 prati User4
    (7, 5),  -- User7 prati User5
    (5, 7),  -- User5 prati User7
    (4, 7),  -- User4 prati User7
    (5, 9),
    (9, 5);


INSERT INTO chat_group (name, admin_id) VALUES ('Group 1', 4);  -- 4 je ID administrativnog korisnika (tester4)
INSERT INTO chat_group (name, admin_id) VALUES ('Group 2', 5);  -- 5 je ID administrativnog korisnika (tester5)



-- Insert članova u grupu 'Group 1' (user_id 4 je 'tester4', user_id 5 je 'tester5', user_id 7 je 'tester7')
INSERT INTO chat_group_members (group_id, user_id) VALUES (1, 4);  -- tester4 je napravio grupu
INSERT INTO chat_group_members (group_id, user_id) VALUES (1, 5);  -- tester5 je član
INSERT INTO chat_group_members (group_id, user_id) VALUES (1, 7);  -- tester7 je član

-- Insert članova u grupu 'Group 2' (user_id 4 je 'tester4', user_id 5 je 'tester5', user_id 7 je 'tester7')
INSERT INTO chat_group_members (group_id, user_id) VALUES (2, 4);  -- tester4 je napravio grupu
INSERT INTO chat_group_members (group_id, user_id) VALUES (2, 5);  -- tester5 je član
INSERT INTO chat_group_members (group_id, user_id) VALUES (2, 7);  -- tester7 je član


-- Insert messages
INSERT INTO message (sender_id, chat_group_id, content, timestamp)
VALUES
    (4, 1, 'Hello everyone in Group 1!', '2024-11-11 14:35:00'),   -- tester4 u grupi Group 1
    (5, 1, 'Hey tester4, how are you?', '2024-11-11 14:36:00'),     -- tester5 u grupi Group 1
    (7, 2, 'Hello Group 2!', '2024-11-11 14:40:00'),               -- tester7 u grupi Group 2
    (9, 2, 'How are you tester7?', '2024-11-12 10:15:00'),         -- tester9 u grupi Group 2
    (4, 2, 'Test message from tester4 in Group 2', '2024-12-01 09:30:00'); -- tester4 u grupi Group 2

