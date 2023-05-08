--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS gift_certificate
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(30) NOT NULL,
    description      VARCHAR(30) NOT NULL,
    price            DECIMAL     NOT NULL,
    duration         INT         NOT NULL,
    create_date      TIMESTAMP   NOT NULL,
    last_update_date TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS tag
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS gift_certificate_tag
(
    gift_certificate_id BIGINT NOT NULL REFERENCES gift_certificate (id),
    tag_id              BIGINT NOT NULL REFERENCES tag (id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);

--changeset Grigoryev_Pavel:2
CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id                 BIGSERIAL PRIMARY KEY,
    price              DECIMAL   NOT NULL,
    purchase_time      TIMESTAMP NOT NULL,
    last_addition_time TIMESTAMP NOT NULL,
    user_id            BIGINT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS orders_gift_certificate
(
    orders_id           BIGINT NOT NULL REFERENCES orders (id),
    gift_certificate_id BIGINT REFERENCES gift_certificate (id),
    PRIMARY KEY (orders_id, gift_certificate_id)
);
