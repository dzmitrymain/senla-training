USE `yeutukhovich_bookstore`;

-- yeutukhovich_bookstore.order_states
INSERT INTO `yeutukhovich_bookstore`.`order_states` (`id`, `state_type`)
VALUES ('1', 'CREATED'),
       ('2', 'CANCELED'),
       ('3', 'COMPLETED');

-- yeutukhovich_bookstore.books
INSERT INTO `yeutukhovich_bookstore`.`books` (`id`, `title`, `is_available`, `edition_year`, `replenishment_date`,
                                              `price`)
VALUES ('1', 'Jonathan Livingston Seagull', '1', '1970', CURRENT_TIMESTAMP, '25.90')
     , ('2', 'Hard to be a god', '1', '1964', CURRENT_TIMESTAMP, '29.99')
     , ('3', 'Hotel \"At a Lost Climber\"', '0', '1970', CURRENT_TIMESTAMP, '22.50')
     , ('4', 'Roadside Picnic', '0', '1972', CURRENT_TIMESTAMP, '19.95')
     , ('5', 'Prisoners of Power', '1', '1971', '2020-01-15 12:00:00', '35.90');

-- yeutukhovich_bookstore.orders
INSERT INTO `yeutukhovich_bookstore`.`orders` (`id`, `book_id`, `order_states_id`, `price`, `creation_date`,
                                               `completion_date`, `customer_data`)
VALUES ('1', '1', '3', '25.90', '2020-08-22 15:02:32', '2020-08-22 15:02:32', 'Customer1')
     , ('2', '2', '3', '29.99', '2020-08-22 15:02:38', '2020-08-22 15:02:32', 'Customer2')
     , ('3', '3', '1', '22.50', '2020-08-22 15:03:13', NULL, 'Customer3')
     , ('4', '4', '1', '19.95', '2020-08-22 15:03:35', NULL, 'Customer4');

-- yeutukhovich_bookstore.requests
INSERT INTO `yeutukhovich_bookstore`.`requests` (`id`, `book_id`, `is_active`, `requester_data`)
VALUES ('1', '3', '1', 'Customer3')
     , ('2', '4', '1', 'Customer4');

ALTER TABLE `books` AUTO_INCREMENT=6;
ALTER TABLE `orders` AUTO_INCREMENT=5;
ALTER TABLE `order_states` AUTO_INCREMENT=4;
ALTER TABLE `requests` AUTO_INCREMENT=3;

COMMIT;