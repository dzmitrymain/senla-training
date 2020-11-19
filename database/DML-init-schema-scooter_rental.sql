USE `scooter_rental`;

-- scooter_rental.locations
INSERT INTO `scooter_rental`.`locations` (`id`, `name`)
VALUES ('1', 'Minsk');

-- scooter_rental.spots
INSERT INTO `scooter_rental`.`spots` (`id`, `location_id`, `phone_number`, `coordinates`)
VALUES ('1', '1', '+375291117161', POINT(53.952068, 27.664864))
     , ('2', '1', '+375292227262', POINT(53.932336, 27.650894))
     , ('3', '1', '+375293337363', POINT(53.908691, 27.549157));

-- scooter_rental.models
INSERT INTO `scooter_rental`.`models` (`id`, `name`, `range`, `speed`, `power`)
VALUES ('1', 'XIAOMI m365', '25', '25', '250')
     , ('2', 'XIAOMI m365 Pro', '40', '25', '350')
     , ('3', 'Ninebot Max', '50', '25', '350');

-- scooter_rental.scooters
INSERT INTO `scooter_rental`.`scooters` (`id`, `model_id`, `spot_id`)
VALUES ('1', '1', '1')
     , ('2', '2', '1')
     , ('3', '3', '1')
     , ('4', '1', '2')
     , ('5', '2', '2')
     , ('6', '3', '2')
     , ('7', '1', '3')
     , ('8', '2', '3')
     , ('9', '3', '3');

-- scooter_rental.rates
INSERT INTO `scooter_rental`.`rates` (`id`, `model_id`, `per_hour`, `weekend_per_hour`)
VALUES ('1', '1', '6', '8')
     , ('2', '2', '7', '9')
     , ('3', '3', '8', '10');

-- scooter_rental.users
INSERT INTO `scooter_rental`.`users` (`id`, `username`, `password`, `role`)
VALUES ('1', 'test', 'test', 'ADMIN');

-- scooter_rental.profiles
INSERT INTO `scooter_rental`.`profiles` (`id`, `user_id`, `location_id`, `name`, `surname`, `email`, `phone_number`)
VALUES ('1', '1', '1', 'test', 'test', 'test', 'test');

-- scooter_rental.discounts
INSERT INTO `scooter_rental`.`discounts` (`id`, `model_id`, `start_date`, `end_date`, `discount`)
VALUES ('1', '1', '2020-11-11 08:00:00', '2020-12-31 23:59:59', '15.00'),
       ('2', '1', '2020-11-13 08:00:00', '2020-12-31 23:59:59', '20.00'),
       ('3', '2', '2020-11-11 08:00:00', '2020-12-31 23:59:59', '15.00'),
       ('4', '1', '2020-12-25 08:00:00', '2020-12-31 23:59:59', '25.00');

COMMIT;