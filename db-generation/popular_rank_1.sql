DROP TABLE IF EXISTS `popular_rank`;
CREATE TABLE `popular_rank` (
  `timestamp` char(14) DEFAULT NULL,
  `id` char(7) DEFAULT NULL,
  `temporalGranularity` char(8) DEFAULT NULL,
  `articleAidList` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `popular_rank` WRITE;
INSERT INTO `popular_rank` VALUES
  ("1516245887000", "1", "day", "{2877, 9092, 9370, 5289, 6649}");
UNLOCK TABLES;


