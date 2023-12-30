DROP TABLE IF EXISTS `popular_rank`;
CREATE TABLE `popular_rank` (
  `timestamp` char(14) DEFAULT NULL,
  `id` char(7) DEFAULT NULL,
  `temporalGranularity` char(8) DEFAULT NULL,
  `articleAidList` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `popular_rank` WRITE;
INSERT INTO `popular_rank` VALUES
  ("1515727487000", "2", "week", "{4283, 9600, 8819, 1982, 123}"),
  ("1513740287000", "3", "month", "{1273, 757, 5476, 3479, 9712}");
UNLOCK TABLES;


