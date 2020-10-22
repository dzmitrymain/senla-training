USE `yeutukhovich_bookstore`;

-- yeutukhovich_bookstore.books
INSERT INTO `yeutukhovich_bookstore`.`books` (`id`, `title`, `is_available`, `edition_year`, `replenishment_date`,
                                              `price`)
VALUES ('1', 'Jonathan Livingston Seagull', '1', '1970', CURRENT_TIMESTAMP, '25.90')
     , ('2', 'Hard to be a god', '1', '1964', CURRENT_TIMESTAMP, '29.99')
     , ('3', 'Hotel \"At a Lost Climber\"', '0', '1970', CURRENT_TIMESTAMP, '22.50')
     , ('4', 'Roadside Picnic', '0', '1972', CURRENT_TIMESTAMP, '19.95')
     , ('5', 'Prisoners of Power', '1', '1971', '2020-01-15 12:00:00', '35.90');

-- yeutukhovich_bookstore.orders
INSERT INTO `yeutukhovich_bookstore`.`orders` (`id`, `book_id`, `state`, `price`, `creation_date`,
                                               `completion_date`, `customer_data`)
VALUES ('1', '1', 'COMPLETED', '25.90', '2020-08-22 15:02:32', '2020-08-22 15:02:32', 'Customer1')
     , ('2', '2', 'COMPLETED', '29.99', '2020-08-22 15:02:38', '2020-08-22 15:02:32', 'Customer2')
     , ('3', '3', 'CREATED', '22.50', '2020-08-22 15:03:13', NULL, 'Customer3')
     , ('4', '4', 'CREATED', '19.95', '2020-08-22 15:03:35', NULL, 'Customer4');

-- yeutukhovich_bookstore.requests
INSERT INTO `yeutukhovich_bookstore`.`requests` (`id`, `book_id`, `is_active`, `requester_data`)
VALUES ('1', '3', '1', 'Customer3'),
       ('2', '4', '1', 'Customer4');

-- yeutukhovich_bookstore.users
INSERT INTO `yeutukhovich_bookstore`.`users` (`id`, `username`, `password`, `role`)
-- password: 'user'
VALUES ('1', 'user', '$2a$10$BYIA9DWnhTZr3VjVogWqleBFtOiajifygf9qYP278UY23Qwv453Gy', 'USER'),
-- password: 'admin'
       ('2', 'admin', '$2a$10$s.WjPYNmCe6tt82pDTosTuak55xpC4TmF26YvFq8ks.gyiQov9Gqm', 'ADMIN');

ALTER TABLE `books`
    AUTO_INCREMENT = 6;
ALTER TABLE `orders`
    AUTO_INCREMENT = 5;
ALTER TABLE `requests`
    AUTO_INCREMENT = 3;
ALTER TABLE `users`
    AUTO_INCREMENT = 3;

COMMIT;
