USE `yeutukhovich_task10`;

-- Задание: 1
SELECT model, speed, hd
FROM PC
WHERE price < 500;

-- Задание: 2
SELECT DISTINCT maker
FROM Product
WHERE type = 'Printer';

-- Задание: 3
SELECT model, ram, screen
FROM Laptop
WHERE price > 1000;

-- Задание: 4
SELECT *
FROM Printer
WHERE color = 'y';

-- Задание: 5
SELECT model, speed, hd
FROM PC
WHERE (cd = '12x' or cd = '24x')
  AND price < 600;

-- Задание: 6
SELECT DISTINCT maker, speed
FROM Laptop
         JOIN Product ON Product.model = Laptop.model
WHERE hd >= 100;

-- Задание: 7
SELECT a.model, price
FROM (SELECT model, price
      FROM PC
      UNION
      SELECT model, price
      FROM Laptop
      UNION
      SELECT model, price
      FROM Printer
     ) AS a
         JOIN
     Product ON a.model = Product.model
WHERE maker = 'B';

-- Задание: 8
SELECT DISTINCT maker
FROM Product
WHERE type = 'PC'
  AND (maker NOT IN (SELECT maker FROM Product WHERE type = 'Laptop'));

-- Задание: 9
SELECT DISTINCT maker
FROM Product
         JOIN PC ON Product.model = PC.model
where speed >= 450;

-- Задание: 10
Select model, price
FROM Printer
WHERE price = (SELECT MAX(price) FROM Printer);

-- Задание: 11
SELECT AVG(speed)
FROM PC;

-- Задание: 12
Select AVG(speed)
FROM Laptop
WHERE price > 1000;

-- Задание: 13
Select AVG(speed)
FROM PC
         JOIN Product ON Product.model = PC.model
WHERE Product.maker = 'A';

-- Задание: 14
SELECT speed, AVG(price)
FROM PC
GROUP BY speed;

-- Задание: 15
SELECT hd
FROM PC
GROUP BY hd
HAVING COUNT(hd) > 1;

-- Задание: 16
SELECT DISTINCT i.model, j.model, i.speed, i.ram
FROM PC AS i,
     PC AS j
WHERE i.speed = j.speed
  AND i.ram = j.ram
  and i.model > j.model;

-- Задание: 17
SELECT DISTINCT type, laptop.model, speed
FROM Laptop
         JOIN Product ON Product.model = Laptop.model
WHERE speed < (SELECT MIN(speed) FROM PC);

-- Задание: 18
SELECT DISTINCT maker, price
FROM Product
         JOIN Printer ON Product.model = Printer.model
WHERE price = (SELECT MIN(price) FROM Printer WHERE color = 'y')
  AND color = 'y';

-- Задание: 19
SELECT maker, AVG(screen)
FROM Product
         JOIN Laptop ON Product.model = Laptop.model
GROUP BY maker;

-- Задание: 20
SELECT maker, COUNT(model)
FROM Product
WHERE type = 'PC'
GROUP BY maker
HAVING COUNT(model) > 2;

-- Задание: 21
SELECT maker, MAX(price)
FROM Product
         JOIN PC ON Product.model = PC.model
GROUP BY maker;

-- Задание: 22
SELECT speed, AVG(price)
FROM PC
WHERE speed > 600
GROUP BY speed;

-- Задание: 23
SELECT DISTINCT maker
FROM PC
         JOIN Product ON PC.model = Product.model
WHERE PC.speed >= 750
  AND maker IN (SELECT maker
                FROM Laptop
                         JOIN Product ON Laptop.model = Product.model
                where Laptop.speed >= 750);

-- Задание: 24
SELECT DISTINCT model
FROM (SELECT model, price
      FROM Laptop
      UNION
      SELECT model, price
      FROM PC
      UNION
      SELECT model, price
      FROM Printer
     ) AS T1
WHERE T1.price = (SELECT MAX(price)
                  FROM (SELECT DISTINCT price
                        FROM Laptop
                        UNION
                        SELECT DISTINCT price
                        FROM PC
                        UNION
                        SELECT DISTINCT price
                        FROM Printer
                       ) AS T2);

-- Задание: 25
SELECT DISTINCT maker
FROM Product
WHERE type = 'Printer'
  AND maker IN (
    SELECT maker
    FROM Product
             JOIN PC ON PC.model = Product.model
    WHERE PC.ram = (SELECT MIN(ram) FROM PC)
      AND PC.speed = (SELECT MAX(speed)
                      FROM (SELECT speed
                            FROM PC
                            WHERE PC.ram = (SELECT MIN(ram) FROM PC)) as T1));