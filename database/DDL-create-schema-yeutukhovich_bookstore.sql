DROP DATABASE IF EXISTS `yeutukhovich_bookstore`;
CREATE DATABASE `yeutukhovich_bookstore`;
USE `yeutukhovich_bookstore`;

CREATE TABLE `yeutukhovich_bookstore`.`books`
(
    `id`                 INT            NOT NULL AUTO_INCREMENT,
    `title`              VARCHAR(45)    NOT NULL,
    `is_available`       TINYINT        NOT NULL,
    `edition_year`       INT            NOT NULL,
    `replenishment_date` TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `price`              DECIMAL(15, 2) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `yeutukhovich_bookstore`.`order_states`
(
    `id`         INT         NOT NULL AUTO_INCREMENT,
    `state_type` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `yeutukhovich_bookstore`.`orders`
(
    `id`              INT            NOT NULL AUTO_INCREMENT,
    `book_id`         INT            NOT NULL,
    `order_states_id` INT            NOT NULL,
    `price`           DECIMAL(15, 2) NOT NULL,
    `creation_date`   TIMESTAMP      NOT NULL,
    `completion_date` TIMESTAMP      NULL DEFAULT NULL,
    `customer_data`   VARCHAR(45)    NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `yeutukhovich_bookstore`.`requests`
(
    `id`             INT         NOT NULL AUTO_INCREMENT,
    `book_id`        INT         NOT NULL,
    `is_active`      TINYINT     NOT NULL,
    `requester_data` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
);

ALTER TABLE `yeutukhovich_bookstore`.`orders`
    ADD INDEX `fk_orders_books_idx` (`book_id` ASC) VISIBLE,
    ADD INDEX `fk_orders_order_states_idx` (`order_states_id` ASC) VISIBLE,
    ADD CONSTRAINT `fk_orders_books`
        FOREIGN KEY (`book_id`)
            REFERENCES `yeutukhovich_bookstore`.`books` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,
    ADD CONSTRAINT `fk_orders_order_states`
        FOREIGN KEY (`order_states_id`)
            REFERENCES `yeutukhovich_bookstore`.`order_states` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

ALTER TABLE `yeutukhovich_bookstore`.`requests`
    ADD INDEX `fk_requests_books_idx` (`book_id` ASC) VISIBLE,
    ADD CONSTRAINT `fk_requests_books`
        FOREIGN KEY (`book_id`)
            REFERENCES `yeutukhovich_bookstore`.`books` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;

COMMIT;