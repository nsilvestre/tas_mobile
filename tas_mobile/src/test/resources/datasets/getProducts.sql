CREATE DEFINER=`root`@`localhost` PROCEDURE `getProducts`()
BEGIN INSERT INTO `test`.product (id, name, price, stock, length, width, height)
VALUES (2, "radio", 30, 200, 10,20, 18); END