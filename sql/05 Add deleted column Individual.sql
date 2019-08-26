ALTER TABLE `Individual`
ADD COLUMN `deleted` BIT(1) NOT NULL DEFAULT 0 AFTER `deathplace`;