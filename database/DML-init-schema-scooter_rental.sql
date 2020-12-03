USE `scooter_rental`;

-- scooter_rental.locations
INSERT INTO `scooter_rental`.`locations` (`id`, `name`)
VALUES ('1', 'Minsk'),
       ('2', 'Brest');

-- scooter_rental.spots
INSERT INTO `scooter_rental`.`spots` (`id`, `location_id`, `phone_number`, `coordinates`)
VALUES ('1', '1', '+375291117161', ST_GeomFromText('POINT(53.952068 27.664864)', 4326))
     , ('2', '1', '+375292227262', ST_GeomFromText('POINT(53.932336 27.650894)', 4326))
     , ('3', '1', '+375293337363', ST_GeomFromText('POINT(53.908691 27.549157)', 4326));

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
VALUES ('1', '1', '2', '2.5')
     , ('2', '2', '2.5', '3')
     , ('3', '3', '3', '3.5');

-- scooter_rental.users
INSERT INTO `scooter_rental`.`users` (`id`, `username`, `password`, `role`)
VALUES ('1', 'admin', '$2a$10$bljEFXZPPwAHZgs.ZB7khuB3U/8Ev2d6k/sUar3LpbSb/yikCA/nK', 'ADMIN'),
       ('2', 'user', '$2a$10$Yl5zSvxQut0OlQKjqREMHurMQlZXU4v9Onb4LJHLs.68cJw1uaZ7G', 'USER');

-- scooter_rental.profiles
INSERT INTO `scooter_rental`.`profiles` (`id`, `user_id`, `location_id`, `name`, `surname`, `email`, `phone_number`)
VALUES ('1', '2', '1', 'userName', 'userSurname', 'user@user.user', '+777551771717');

-- scooter_rental.discounts
INSERT INTO `scooter_rental`.`discounts` (`id`, `model_id`, `start_date`, `end_date`, `discount`)
VALUES ('1', '1', '2020-11-11 08:00:00', '2020-12-31 23:59:59', '15.00'),
       ('2', '1', '2020-11-13 08:00:00', '2020-12-31 23:59:59', '20.00'),
       ('3', '2', '2020-11-11 08:00:00', '2020-12-31 23:59:59', '15.00'),
       ('4', '1', '2020-12-25 08:00:00', '2020-12-31 23:59:59', '25.00');

-- scooter_rental.reviews
INSERT INTO `scooter_rental`.`reviews` (`id`, `profile_id`, `model_id`, `score`, `opinion`)
VALUES ('1', '1', '1', '4', 'Awesome.');

-- scooter_rental.passes
INSERT INTO `scooter_rental`.`passes` (`id`, `user_id`, `model_id`, `expired_date`, `remaining_minutes`,
                                       `total_minutes`, `price`)
VALUES ('1', '1', '1', '2020-12-31 23:59:59', '10000', '10000', '0.00');

COMMIT;
