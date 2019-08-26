ALTER TABLE `Family`
ADD COLUMN `deleted` BIT(1) NOT NULL DEFAULT 0 AFTER `divorceplace`;