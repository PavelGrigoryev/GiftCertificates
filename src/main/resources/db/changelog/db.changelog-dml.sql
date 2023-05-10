--liquibase formatted sql

--changeset Grigoryev_Pavel:1
INSERT INTO gift_certificate(id, name, description, price, duration, create_date, last_update_date, created_by,
                             last_modified_by, operation)
VALUES (1, 'Rick', 'Vabulabudabda', 25.6, 7, '2023-04-23 14:30:59.093', '2023-04-23 14:30:59.093', 'Pavel', 'Pavel',
        'INSERT'),
       (2, 'Morty', 'Hi', 45, 3, '2023-04-24 14:30:59.093', '2023-04-28 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       (3, 'Pechkin', 'abda', 75.3, 5, '2023-04-23 14:30:59.093', '2023-04-23 14:30:59.093', 'Pavel', 'Pavel',
        'INSERT'),
       (4, 'Fiorello', 'Zero', 89.22, 2, '2023-04-25 14:30:59.093', '2023-04-27 14:30:59.093', 'Pavel', 'Pavel',
        'INSERT'),
       (5, 'Donatello', 'Zap', 89.33, 1, '2023-04-25 14:30:59.093', '2023-04-30 14:30:59.093', 'Pavel', 'Pavel',
        'INSERT'),
       (6, 'Azanti', 'Query', 55, 9, '2023-04-30 14:30:59.093', '2023-05-30 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       (7, 'Sara', 'Little', 33.29, 4, '2023-05-08 14:30:59.093', '2023-05-09 14:30:59.093', 'Pavel', 'Pavel',
        'INSERT');

SELECT setval('gift_certificate_id_seq', (SELECT max(id) FROM gift_certificate));

INSERT INTO tag(id, name, created_by, last_modified_by, operation)
VALUES (1, 'Pepsi', 'Pavel', 'Pavel', 'INSERT'),
       (2, 'Fanta', 'Pavel', 'Pavel', 'INSERT'),
       (3, 'Sprite', 'Pavel', 'Pavel', 'INSERT'),
       (4, 'Cola', 'Pavel', 'Pavel', 'INSERT'),
       (5, '7-Up', 'Pavel', 'Pavel', 'INSERT');

SELECT setval('tag_id_seq', (SELECT max(id) FROM tag));

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 3),
       (3, 5),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4),
       (7, 1),
       (7, 2),
       (7, 3),
       (7, 5);

--changeset Grigoryev_Pavel:2
INSERT INTO users (id, username)
VALUES (1, 'Bully'),
       (2, 'Jim'),
       (3, 'Pavel'),
       (4, 'Sid'),
       (5, 'Arnold');

SELECT setval('users_id_seq', (SELECT max(id) FROM users));

INSERT INTO orders (id, price, purchase_time, last_addition_time, user_id, created_by, last_modified_by, operation)
VALUES (1, 100, '2023-05-08 14:30:59.093', '2023-05-08 14:30:59.093', 1, 'Pavel', 'Pavel', 'INSERT'),
       (2, 58.89, '2023-05-08 14:30:59.093', '2023-05-08 14:30:59.093', 1, 'Pavel', 'Pavel', 'INSERT'),
       (3, 45, '2023-05-08 14:30:59.093', '2023-05-08 14:30:59.093', 1, 'Pavel', 'Pavel', 'INSERT');

SELECT setval('orders_id_seq', (SELECT max(id) FROM orders));

INSERT INTO orders_gift_certificate(orders_id, gift_certificate_id)
VALUES (1, 2),
       (1, 6),
       (2, 1),
       (2, 7),
       (3, 2);
