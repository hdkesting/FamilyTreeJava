CREATE TABLE `family` (
   `id` bigint(11) NOT NULL,
   `marriagedate` date DEFAULT NULL,
   `marriageplace` varchar(100) DEFAULT NULL,
   `divorcedate` date DEFAULT NULL,
   `divorceplace` varchar(100) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci