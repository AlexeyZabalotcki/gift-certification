DROP TABLE IF EXISTS gift_certificate;

CREATE TABLE gift_certificate(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255)   NOT NULL,
    description      VARCHAR,
    price            NUMERIC(10, 2) NOT NULL,
    duration         INT            NOT NULL,
    create_date      TIMESTAMP DEFAULT NOW(),
    last_update_date TIMESTAMP DEFAULT NOW()
);

DROP TABLE IF EXISTS tag;
CREATE TABLE tag(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS gift_certificate_tag;
CREATE TABLE gift_certificate_tag(
    gift_certificate_id INT NOT NULL,
    tag_id              INT NOT NULL,
    PRIMARY KEY (gift_certificate_id, tag_id),
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE
);