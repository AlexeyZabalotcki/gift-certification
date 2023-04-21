INSERT INTO gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES
       (1, 'Product 1', 'This is product 1', 19.99, 10, '2022-04-19', '2022-04-19'),
       (2, 'Product 2', 'This is product 2', 90.99, 1, '2022-04-19', '2022-04-19'),
       (3, 'Product 3', 'This is product 3', 0.99, 100, '2022-04-19', '2022-04-19');

INSERT INTO tag (id, name)
VALUES
(1, 'Tag 1'),
(2,'Tag 2');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES
(1, 1),
(1, 2),
(2, 2),
(2, 1),
(3, 2);