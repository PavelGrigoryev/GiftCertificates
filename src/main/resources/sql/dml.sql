INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
VALUES ('Rick', 'Vabulabudabda', 25.6, 7, '2023-04-23 14:30:59.093', '2023-04-23 14:30:59.093'),
       ('Morty', 'Hi', 45, 3, '2023-04-24 14:30:59.093', '2023-04-28 14:30:59.093'),
       ('Pechkin', 'abda', 75.3, 5, '2023-04-23 14:30:59.093', '2023-04-23 14:30:59.093'),
       ('Fiorello', 'Zero', 89.22, 2, '2023-04-25 14:30:59.093', '2023-04-27 14:30:59.093'),
       ('Donatello', 'Zap', 89.33, 1, '2023-04-25 14:30:59.093', '2023-04-30 14:30:59.093'),
       ('Azanti', 'Query', 55, 9, '2023-04-30 14:30:59.093', '2023-05-30 14:30:59.093');

INSERT INTO tag(name)
VALUES ('Pepsi'),
       ('Fanta'),
       ('Sprite'),
       ('Cola'),
       ('7-Up');

INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 3),
       (3, 5),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4);

INSERT INTO users (username)
VALUES ('Bully'),
       ('Jim'),
       ('Pavel'),
       ('Sid'),
       ('Peter')