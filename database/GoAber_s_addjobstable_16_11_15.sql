
DROP TABLE IF EXISTS `jobdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobdetail` (
  `jobid` varchar(255) NOT NULL,
  `schedtype` int(11) DEFAULT NULL,
  `shcedtimemins` int(11) DEFAULT NULL,
  `startnow` tinyint(1) DEFAULT '0',
  `tasktype` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;