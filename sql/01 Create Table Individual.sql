CREATE TABLE `individual` (
   `id` bigint(11) NOT NULL,
   `firstnames` varchar(100) DEFAULT NULL,
   `lastname` varchar(50) DEFAULT NULL,
   `sex` char(1) DEFAULT NULL,
   `birthdate` date DEFAULT NULL,
   `birthplace` varchar(100) DEFAULT NULL,
   `deathdate` date DEFAULT NULL,
   `deathplace` varchar(100) DEFAULT NULL,
   `individualcol` varchar(45) DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `id_UNIQUE` (`id`) /*!80000 INVISIBLE */,
   KEY `lastnames` (`lastname`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci