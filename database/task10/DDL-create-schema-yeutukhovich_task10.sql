DROP DATABASE IF EXISTS `yeutukhovich_task10`;
CREATE DATABASE `yeutukhovich_task10`;
USE `yeutukhovich_task10`;

-- yeutukhovich_task10.Product
CREATE TABLE `Product`
(
    `maker` varchar(10) NOT NULL,
    `model` varchar(50) NOT NULL,
    `type`  varchar(50) NOT NULL,
    PRIMARY KEY (`model`)
);

-- yeutukhovich_task10.PC
CREATE TABLE `PC`
(
    `code`  int         NOT NULL,
    `model` varchar(50) NOT NULL,
    `speed` smallint    NOT NULL,
    `ram`   smallint    NOT NULL,
    `hd`    double      NOT NULL,
    `cd`    varchar(10) NOT NULL,
    `price` decimal(15, 2) DEFAULT NULL,
    PRIMARY KEY (`code`),
    CONSTRAINT `FK_PC_Product` FOREIGN KEY (`model`) REFERENCES `Product` (`model`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- yeutukhovich_task10.Laptop
CREATE TABLE `Laptop`
(
    `code`   int         NOT NULL,
    `model`  varchar(50) NOT NULL,
    `speed`  smallint    NOT NULL,
    `ram`    smallint    NOT NULL,
    `hd`     double      NOT NULL,
    `price`  decimal(15, 2) DEFAULT NULL,
    `screen` tinyint     NOT NULL,
    PRIMARY KEY (`code`),
    CONSTRAINT `FK_Laptop_Product` FOREIGN KEY (`model`) REFERENCES `Product` (`model`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- yeutukhovich_task10.Printer
CREATE TABLE `Printer`
(
    `code`  int         NOT NULL,
    `model` varchar(50) NOT NULL,
    `color` char(1)     NOT NULL,
    `type`  varchar(10) NOT NULL,
    `price` decimal(15, 2) DEFAULT NULL,
    PRIMARY KEY (`code`),
    CONSTRAINT `FK_Printer_Product` FOREIGN KEY (`model`) REFERENCES `Product` (`model`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

COMMIT;



