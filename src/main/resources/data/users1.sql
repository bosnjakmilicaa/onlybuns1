-- Insert users
INSERT INTO users (email, password, username, user_type, first_name, last_name, address, is_active)
VALUES
    ('testuser2@example.com', 'password123', 'tester2', 'admin', 'Test', 'User2', '456 Test Avenue', TRUE),
    ('testuser3@example.com', 'password123', 'tester3', 'admin', 'Test', 'User3', '457 Test Avenue', TRUE),
    ('testuser1@example.com', 'password123', 'tester1', 'unregistered_user', 'Test', 'User1', '459 Test Avenue', TRUE),
    ('testuser4@example.com', 'password123', 'tester4', 'registered_user', 'Test', 'User4', '450 Test Avenue', TRUE),
    ('testuser5@example.com', 'password123', 'tester5', 'registered_user', 'Test', 'User5', '451 Test Avenue', TRUE),
    ('testuser6@example.com', 'password123', 'tester8', 'unregistered_user', 'Test', 'User6', '450 Test Avenue', TRUE),
    ('testuser7@example.com', 'password123', 'tester7', 'registered_user', 'Test', 'User7', '452 Test Avenue', TRUE);

-- Insert posts
INSERT INTO posts (is_deleted, user_id, description, image_url, created_at,count_likes)
VALUES
    (false, 4, 'content1', 'http://www.royalshop.rs/wp-content/uploads/2015/04/zeka.jpg', '2024-11-05 14:30:00',2),
    (false, 5, 'content1', 'https://www.agrotv.net/wp-content/uploads/2018/12/Divlji-zec-4.jpg', '2024-11-04 10:00:00',3),
    (false, 5, 'content2', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Arctic_Hare_1.jpg/640px-Arctic_Hare_1.jpg', '2024-11-03 18:45:00',4),
    (false, 5, 'content3', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Feldhase_Schiermonnikoog.JPG/640px-Feldhase_Schiermonnikoog.JPG', '2024-11-02 09:20:00',0),
    (false, 7, 'content1', 'https://www.musicar.rs/wp-content/uploads/2017/08/divlji-zec-pogled.jpg', '2024-11-04 13:00:00',1),
    (false, 7, 'content2', 'https://svijetljubimaca.ba/wp-content/uploads/2017/03/943_1.jpg', '2024-11-03 11:30:00',0);

-- Insert comments
INSERT INTO comments (post_id, user_id, content, created_at)
VALUES
    (1, 7, 'com1', '2024-11-06 12:00:00'),
    (1, 5, 'com2', '2024-11-06 11:00:00'),
    (2, 7, 'com1', '2024-11-05 09:00:00'),
    (1, 4, 'com3', '2024-11-06 08:00:00'),
    (6, 7, 'com1', '2024-11-05 12:00:00'),
    (5, 5, 'com1', '2024-11-05 12:10:00');
