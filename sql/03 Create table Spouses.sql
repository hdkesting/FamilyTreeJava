CREATE TABLE `spouses` (
   `familyid` bigint(11) NOT NULL,
   `spouseid` bigint(11) NOT NULL,
   PRIMARY KEY (`familyid`,`spouseid`),
   KEY `spouseindividual` (`spouseid`),
   CONSTRAINT `spousefamily` FOREIGN KEY (`familyid`) REFERENCES `family` (`id`),
   CONSTRAINT `spouseindividual` FOREIGN KEY (`spouseid`) REFERENCES `individual` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci