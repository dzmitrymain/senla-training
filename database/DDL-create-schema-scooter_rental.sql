DROP DATABASE IF EXISTS scooter_rental;
CREATE DATABASE scooter_rental;
USE scooter_rental;

CREATE TABLE scooter_rental.`locations`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    CONSTRAINT `pk_locations_id` PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_index_locations_name` (`name` ASC) VISIBLE
);

CREATE TABLE scooter_rental.`users`
(
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `username`      VARCHAR(255) NOT NULL,
    `password`      VARCHAR(255) NOT NULL,
    `role`          VARCHAR(45)  NOT NULL,
    `creation_date` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `enabled`       TINYINT      NOT NULL,
    CONSTRAINT `pk_users_id` PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_index_users_username` (`username` ASC) VISIBLE
);

CREATE TABLE scooter_rental.`profiles`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `user_id`      INT          NOT NULL,
    `location_id`  INT          NOT NULL,
    `name`         VARCHAR(255) NOT NULL,
    `surname`      VARCHAR(255) NOT NULL,
    `email`        VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(13)  NULL,
    CONSTRAINT `pk_profiles_id` PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_index_profiles_user_id` (`user_id` ASC) VISIBLE,
    UNIQUE INDEX `unique_index_profiles_email` (`email` ASC) VISIBLE,
    UNIQUE INDEX `unique_index_profiles_phone_number` (`phone_number` ASC) VISIBLE
);

CREATE TABLE scooter_rental.`models`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `model` VARCHAR(255) NOT NULL,
    `range` SMALLINT     NOT NULL,
    `speed` SMALLINT     NOT NULL,
    `power` SMALLINT     NOT NULL,
    CONSTRAINT `pk_models_id` PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_index_models_model` (`model` ASC) VISIBLE
);

CREATE TABLE scooter_rental.`spots`
(
    `id`           INT         NOT NULL AUTO_INCREMENT,
    `location_id`  INT         NOT NULL,
    `phone_number` VARCHAR(13) NULL,
    `coordinates`  POINT       NOT NULL,
    CONSTRAINT `pk_spots_id` PRIMARY KEY (`id`)
);

CREATE TABLE scooter_rental.`scooters`
(
    `id`                       INT       NOT NULL AUTO_INCREMENT,
    `model_id`                 INT       NOT NULL,
    `spot_id`                  INT       NOT NULL,
    `begin_operation_date`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `total_distance_travelled` INT       NOT NULL DEFAULT 0,
    CONSTRAINT `pk_scooters_id` PRIMARY KEY (`id`)
);

CREATE TABLE scooter_rental.`rates`
(
    `id`               INT            NOT NULL AUTO_INCREMENT,
    `model_id`         INT            NOT NULL,
    `per_hour`         DECIMAL(15, 2) NOT NULL,
    `weekend_per_hour` DECIMAL(15, 2) NOT NULL,
    CONSTRAINT `pk_rates_id` PRIMARY KEY (`id`)
);

CREATE TABLE scooter_rental.`passes`
(
    `id`                INT            NOT NULL AUTO_INCREMENT,
    `user_id`           INT            NOT NULL,
    `model_id`          INT            NOT NULL,
    `start_date`        TIMESTAMP      NOT NULL,
    `expired_date`      TIMESTAMP      NOT NULL,
    `total_minutes`     INT            NOT NULL,
    `remaining_minutes` INT            NOT NULL,
    `price`             DECIMAL(15, 2) NOT NULL,
    CONSTRAINT `pk_passes_id` PRIMARY KEY (`id`)
);

CREATE TABLE scooter_rental.`discounts`
(
    `id`         INT           NOT NULL AUTO_INCREMENT,
    `model_id`   INT           NOT NULL,
    `start_date` TIMESTAMP     NOT NULL,
    `end_date`   TIMESTAMP     NOT NULL,
    `discount`   DECIMAL(3, 2) NOT NULL,
    CONSTRAINT `pk_discounts_id` PRIMARY KEY (`id`)
);

CREATE TABLE scooter_rental.`rents`
(
    `id`                 INT            NOT NULL AUTO_INCREMENT,
    `user_id`            INT            NOT NULL,
    `scooter_id`         INT            NOT NULL,
    `state`              VARCHAR(45)    NOT NULL,
    `start_date`         TIMESTAMP      NOT NULL,
    `end_date`           TIMESTAMP      NOT NULL,
    `payment_type`       VARCHAR(45)    NOT NULL,
    `price`              DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    `return_date`        TIMESTAMP      NULL,
    `distance_travelled` INT            NULL,
    `overtime_penalty`   DECIMAL(15, 2) NULL,
    CONSTRAINT `pk_rents_id` PRIMARY KEY (`id`)
);

CREATE TABLE scooter_rental.`reviews`
(
    `id`            INT           NOT NULL AUTO_INCREMENT,
    `user_id`       INT           NOT NULL,
    `model_id`      INT           NULL,
    `score`         TINYINT       NOT NULL,
    `opinion`       VARCHAR(2000) NULL,
    `creation_date` TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `pk_reviews_id` PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_index_reviews_user_id` (`user_id` ASC) VISIBLE
);

ALTER TABLE scooter_rental.`profiles`
    ADD CONSTRAINT `fk_profiles_users`
        FOREIGN KEY (`user_id`)
            REFERENCES scooter_rental.`users` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_profiles_locations`
        FOREIGN KEY (`location_id`)
            REFERENCES scooter_rental.`locations` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_profiles_user_id` (`user_id` ASC) VISIBLE,
    ADD INDEX `index_profiles_location_id` (`location_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`spots`
    ADD CONSTRAINT `fk_spots_locations`
        FOREIGN KEY (`location_id`)
            REFERENCES scooter_rental.`locations` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_spots_location_id` (`location_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`scooters`
    ADD CONSTRAINT `fk_scooters_models`
        FOREIGN KEY (`model_id`)
            REFERENCES scooter_rental.`models` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_scooters_spots`
        FOREIGN KEY (`spot_id`)
            REFERENCES scooter_rental.`spots` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_scooters_model_id` (`model_id` ASC) VISIBLE,
    ADD INDEX `index_scooters_spot_id` (`spot_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`rates`
    ADD CONSTRAINT `fk_rates_models`
        FOREIGN KEY (`model_id`)
            REFERENCES scooter_rental.`models` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_rates_model_id` (`model_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`passes`
    ADD CONSTRAINT `fk_passes_users`
        FOREIGN KEY (`user_id`)
            REFERENCES scooter_rental.`users` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_passes_models`
        FOREIGN KEY (`model_id`)
            REFERENCES scooter_rental.`models` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_passes_user_id` (`user_id` ASC) VISIBLE,
    ADD INDEX `index_passes_models_id` (`model_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`discounts`
    ADD CONSTRAINT `fk_discounts_models`
        FOREIGN KEY (`model_id`)
            REFERENCES scooter_rental.`models` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_discount_model_id` (`model_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`rents`
    ADD CONSTRAINT `fk_rents_users`
        FOREIGN KEY (`user_id`)
            REFERENCES scooter_rental.`users` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_rents_scooters`
        FOREIGN KEY (`scooter_id`)
            REFERENCES scooter_rental.`scooters` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_rents_user_id` (`user_id` ASC) VISIBLE,
    ADD INDEX `index_rents_scooter_id` (`scooter_id` ASC) VISIBLE;

ALTER TABLE scooter_rental.`reviews`
    ADD CONSTRAINT `fk_reviews_users`
        FOREIGN KEY (`user_id`)
            REFERENCES scooter_rental.`users` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_reviews_models`
        FOREIGN KEY (`model_id`)
            REFERENCES scooter_rental.`models` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD INDEX `index_reviews_user_id` (`user_id` ASC) VISIBLE,
    ADD INDEX `index_reviews_models_id` (`model_id` ASC) VISIBLE;

COMMIT;
