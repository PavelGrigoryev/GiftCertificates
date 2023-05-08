--liquibase formatted sql

--changeset Grigoryev_Pavel:1
INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date, created_by,
                             last_modified_by, operation)
VALUES ('Rick', 'Vabulabudabda', 25.6, 7, '2023-04-23 14:30:59.093', '2023-04-23 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       ('Morty', 'Hi', 45, 3, '2023-04-24 14:30:59.093', '2023-04-28 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       ('Pechkin', 'abda', 75.3, 5, '2023-04-23 14:30:59.093', '2023-04-23 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       ('Fiorello', 'Zero', 89.22, 2, '2023-04-25 14:30:59.093', '2023-04-27 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       ('Donatello', 'Zap', 89.33, 1, '2023-04-25 14:30:59.093', '2023-04-30 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       ('Azanti', 'Query', 55, 9, '2023-04-30 14:30:59.093', '2023-05-30 14:30:59.093', 'Pavel', 'Pavel', 'INSERT'),
       ('Sara', 'Little', 33.29, 4, '2023-05-08 14:30:59.093', '2023-05-09 14:30:59.093', 'Pavel', 'Pavel', 'INSERT');

INSERT INTO tag(name, created_by, last_modified_by, operation)
VALUES ('Pepsi', 'Pavel', 'Pavel', 'INSERT'),
       ('Fanta', 'Pavel', 'Pavel', 'INSERT'),
       ('Sprite', 'Pavel', 'Pavel', 'INSERT'),
       ('Cola', 'Pavel', 'Pavel', 'INSERT'),
       ('7-Up', 'Pavel', 'Pavel', 'INSERT');

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

INSERT INTO users (username)
VALUES ('Bully'),
       ('Jim'),
       ('Pavel'),
       ('Sid'),
       ('Peter')
