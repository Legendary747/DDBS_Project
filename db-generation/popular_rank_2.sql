DROP TABLE IF EXISTS `popular_rank`;
CREATE TABLE `popular_rank` (
  `timestamp` char(14) DEFAULT NULL,
  `id` char(7) DEFAULT NULL,
  `temporalGranularity` char(8) DEFAULT NULL,
  `articleAidList` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `popular_rank` WRITE;
INSERT INTO `popular_rank` VALUES
  ("1515727487000", "2", "week", "{4690, 1060, 7944, 5353, 1693}"),
  ("1513740287000", "3", "month", "{2274, 8070, 8757, 6206, 6342}");
UNLOCK TABLES;


