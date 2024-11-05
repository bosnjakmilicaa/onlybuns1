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

INSERT INTO posts(is_deleted,user_id, description,image_url) VALUES (false,4, 'content1','http://www.royalshop.rs/wp-content/uploads/2015/04/zeka.jpg');
INSERT INTO posts(is_deleted,user_id, description,image_url) VALUES (false,5, 'content1','https://www.agrotv.net/wp-content/uploads/2018/12/Divlji-zec-4.jpg');
INSERT INTO posts(is_deleted,user_id, description,image_url) VALUES (false,5, 'content2','https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Arctic_Hare_1.jpg/640px-Arctic_Hare_1.jpg');
INSERT INTO posts(is_deleted,user_id, description,image_url) VALUES (false,5, 'content3','https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Feldhase_Schiermonnikoog.JPG/640px-Feldhase_Schiermonnikoog.JPG');
INSERT INTO posts(is_deleted,user_id, description,image_url) VALUES (false,7, 'content1','https://www.musicar.rs/wp-content/uploads/2017/08/divlji-zec-pogled.jpg');
INSERT INTO posts(is_deleted,user_id, description,image_url) VALUES (false,7, 'content2','https://svijetljubimaca.ba/wp-content/uploads/2017/03/943_1.jpg
');


INSERT INTO comments(post_id,user_id, content) VALUES (1,7, 'com1');
INSERT INTO comments(post_id,user_id, content) VALUES (1,5, 'com2');
INSERT INTO comments(post_id,user_id, content) VALUES (2,7, 'com1');
INSERT INTO comments(post_id,user_id, content) VALUES (1,4, 'com3');
INSERT INTO comments(post_id,user_id, content) VALUES (6,7, 'com1');
INSERT INTO comments(post_id,user_id, content) VALUES (5,5, 'com1');
