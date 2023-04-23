CREATE TABLE IF NOT EXISTS gift_certificate
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(30) NOT NULL,
    description      VARCHAR(30) NOT NULL,
    price            DECIMAL     NOT NULL,
    duration         INT         NOT NULL,
    create_date      TIMESTAMP   NOT NULL,
    last_update_date TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS tag
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_certificate_tag
(
    gift_certificate_id BIGINT NOT NULL REFERENCES gift_certificate (id),
    tag_id              BIGINT NOT NULL REFERENCES tag (id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);