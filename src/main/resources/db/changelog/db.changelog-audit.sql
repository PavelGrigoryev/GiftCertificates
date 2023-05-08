--liquibase formatted sql

--changeset Grigoryev_Pavel:1
ALTER TABLE gift_certificate
    ADD COLUMN created_by VARCHAR(30);

ALTER TABLE gift_certificate
    ADD COLUMN last_modified_by VARCHAR(30);

ALTER TABLE gift_certificate
    ADD COLUMN operation VARCHAR(30);

--changeset Grigoryev_Pavel:2
ALTER TABLE tag
    ADD COLUMN created_by VARCHAR(30);

ALTER TABLE tag
    ADD COLUMN last_modified_by VARCHAR(30);

ALTER TABLE tag
    ADD COLUMN operation VARCHAR(30);

--changeset Grigoryev_Pavel:3
ALTER TABLE orders
    ADD COLUMN created_by VARCHAR(30);

ALTER TABLE orders
    ADD COLUMN last_modified_by VARCHAR(30);

ALTER TABLE orders
    ADD COLUMN operation VARCHAR(30);
