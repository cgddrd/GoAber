-- MySQL dump 10.13  Distrib 5.6.23, for Win32 (x86)
--
-- Host: localhost    Database: goaber
-- ------------------------------------------------------
-- Server version	5.6.23-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activitydata`
--

DROP TABLE IF EXISTS `activitydata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activitydata` (
  `idActivityData` int(11) NOT NULL AUTO_INCREMENT,
  `categoryUnitId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `value` int(11) DEFAULT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`idActivityData`),
  UNIQUE KEY `idActivityData_UNIQUE` (`idActivityData`),
  KEY `user_idx` (`userId`),
  KEY `catergoryUnit_idx` (`categoryUnitId`),
  CONSTRAINT `catergoryUnit_activityData` FOREIGN KEY (`categoryUnitId`) REFERENCES `categoryunit` (`idCategoryUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_activityData` FOREIGN KEY (`userId`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitydata`
--

LOCK TABLES `activitydata` WRITE;
/*!40000 ALTER TABLE `activitydata` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitydata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `idCategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`idCategory`),
  UNIQUE KEY `idCategory_UNIQUE` (`idCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoryunit`
--

DROP TABLE IF EXISTS `categoryunit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoryunit` (
  `idCategoryUnit` int(11) NOT NULL AUTO_INCREMENT,
  `categoryId` int(11) NOT NULL,
  `unitId` int(11) NOT NULL,
  PRIMARY KEY (`idCategoryUnit`),
  UNIQUE KEY `idCategoryUnit_UNIQUE` (`idCategoryUnit`),
  KEY `categoryId_idx` (`categoryId`),
  KEY `unitId_idx` (`unitId`),
  CONSTRAINT `categoryId_categoryUnit` FOREIGN KEY (`categoryId`) REFERENCES `category` (`idCategory`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `unitId_categoryUnit` FOREIGN KEY (`unitId`) REFERENCES `unit` (`idUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoryunit`
--

LOCK TABLES `categoryunit` WRITE;
/*!40000 ALTER TABLE `categoryunit` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoryunit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `challenge`
--

DROP TABLE IF EXISTS `challenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `challenge` (
  `idChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `categoryUnit` int(11) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `communityStartedBy` int(11) DEFAULT NULL,
  PRIMARY KEY (`idChallenge`),
  UNIQUE KEY `idChallenge_UNIQUE` (`idChallenge`),
  KEY `communityStartedBy_Challenge_idx` (`communityStartedBy`),
  CONSTRAINT `communityStartedBy_Challenge` FOREIGN KEY (`communityStartedBy`) REFERENCES `community` (`idCommunity`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenge`
--

LOCK TABLES `challenge` WRITE;
/*!40000 ALTER TABLE `challenge` DISABLE KEYS */;
/*!40000 ALTER TABLE `challenge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `idCommunity` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `endpointUrl` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idCommunity`),
  UNIQUE KEY `idCommunity_UNIQUE` (`idCommunity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device` (
  `idDevice` int(11) NOT NULL AUTO_INCREMENT,
  `deviceTypeId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `accessToken` varchar(45) DEFAULT NULL,
  `refreshToken` varchar(45) DEFAULT NULL,
  `tokenExpiration` datetime DEFAULT NULL,
  PRIMARY KEY (`idDevice`),
  UNIQUE KEY `idDevice_UNIQUE` (`idDevice`),
  KEY `deviceType_idx` (`deviceTypeId`),
  KEY `user_idx` (`userId`),
  CONSTRAINT `deviceType_Device` FOREIGN KEY (`deviceTypeId`) REFERENCES `devicetype` (`idDeviceType`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_Device` FOREIGN KEY (`userId`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devicetype`
--

DROP TABLE IF EXISTS `devicetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `devicetype` (
  `idDeviceType` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `tokenEndpoint` varchar(250) DEFAULT NULL,
  `consumerKey` varchar(45) DEFAULT NULL,
  `consumerSecret` varchar(45) DEFAULT NULL,
  `clientId` varchar(45) DEFAULT NULL,
  `authorizationEndpoint` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idDeviceType`),
  UNIQUE KEY `idDeviceType_UNIQUE` (`idDeviceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devicetype`
--

LOCK TABLES `devicetype` WRITE;
/*!40000 ALTER TABLE `devicetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `devicetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupchallenge`
--

DROP TABLE IF EXISTS `groupchallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupchallenge` (
  `idGroupChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `challengeId` int(11) NOT NULL,
  PRIMARY KEY (`idGroupChallenge`),
  UNIQUE KEY `idGroupChallenge_UNIQUE` (`idGroupChallenge`),
  KEY `group_idx` (`groupId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `challenge_groupChallenge` FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `group_groupChallenge` FOREIGN KEY (`groupId`) REFERENCES `team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupchallenge`
--

LOCK TABLES `groupchallenge` WRITE;
/*!40000 ALTER TABLE `groupchallenge` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupchallenge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobdetail`
--

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

--
-- Dumping data for table `jobdetail`
--

LOCK TABLES `jobdetail` WRITE;
/*!40000 ALTER TABLE `jobdetail` DISABLE KEYS */;
INSERT INTO `jobdetail` VALUES ('fitbit1',0,2,0,0),('fitbit2',0,1,0,0),('fitbit3',0,1,0,0),('fitbit4',0,1,0,0),('fitbit5',0,1,0,0),('fitbit6',0,1,0,0);
/*!40000 ALTER TABLE `jobdetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobs` (
  `jobid` varchar(100) NOT NULL,
  `tasktype` int(11) NOT NULL,
  `schedtype` int(11) NOT NULL,
  `shcedtimemins` int(11) NOT NULL,
  `startnow` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `idRole` varchar(45) NOT NULL,
  PRIMARY KEY (`idRole`),
  UNIQUE KEY `roleid_UNIQUE` (`idRole`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team` (
  `idGroup` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `communityId` int(11) DEFAULT NULL,
  PRIMARY KEY (`idGroup`),
  UNIQUE KEY `idGroup_UNIQUE` (`idGroup`),
  KEY `community_idx` (`communityId`),
  CONSTRAINT `community_team` FOREIGN KEY (`communityId`) REFERENCES `community` (`idCommunity`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unit`
--

DROP TABLE IF EXISTS `unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit` (
  `idUnit` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`idUnit`),
  UNIQUE KEY `idUnit_UNIQUE` (`idUnit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit`
--

LOCK TABLES `unit` WRITE;
/*!40000 ALTER TABLE `unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `userRoleId` int(11) NOT NULL,
  `userCredentialsId` int(11) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `roleId` varchar(45) NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `Email_UNIQUE` (`email`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  KEY `userRole_idx` (`userRoleId`),
  KEY `userCredentials_idx` (`userCredentialsId`),
  KEY `group_idx` (`groupId`),
  CONSTRAINT `group_user` FOREIGN KEY (`groupId`) REFERENCES `team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userCredentials_user` FOREIGN KEY (`userCredentialsId`) REFERENCES `usercredentials` (`idUserCredentials`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userRole_user` FOREIGN KEY (`userRoleId`) REFERENCES `userrole` (`idUserRole`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userchallenge`
--

DROP TABLE IF EXISTS `userchallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userchallenge` (
  `idUserChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `challengeId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`idUserChallenge`),
  UNIQUE KEY `idUserChallenge_UNIQUE` (`idUserChallenge`),
  KEY `user_idx` (`userId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `challenge_userChallenge` FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_userChallenge` FOREIGN KEY (`userId`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userchallenge`
--

LOCK TABLES `userchallenge` WRITE;
/*!40000 ALTER TABLE `userchallenge` DISABLE KEYS */;
/*!40000 ALTER TABLE `userchallenge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userrole`
--

DROP TABLE IF EXISTS `userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userrole` (
  `idUserRole` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` varchar(45) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`idUserRole`),
  UNIQUE KEY `idUserRole_UNIQUE` (`idUserRole`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userrole`
--

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;
/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-16 11:25:58
