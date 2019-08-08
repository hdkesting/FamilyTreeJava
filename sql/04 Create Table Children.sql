CREATE TABLE `children` (
   `familyid` bigint(11) NOT NULL,
   `childid` bigint(11) NOT NULL,
   PRIMARY KEY (`familyid`,`childid`),
   KEY `childindividual` (`childid`),
   CONSTRAINT `childfamily` FOREIGN KEY (`familyid`) REFERENCES `family` (`id`),
   CONSTRAINT `childindividual` FOREIGN KEY (`childid`) REFERENCES `individual` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci