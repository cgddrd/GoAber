CREATE DATABASE IF NOT EXISTS `goaber` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `GoAber`;
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
-- Table structure for table `audit`
--

DROP TABLE IF EXISTS `audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit` (
  `idAudit` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `urlAccessed` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`idAudit`),
  UNIQUE KEY `idAudit_UNIQUE` (`idAudit`),
  KEY `UserId_Audit_idx` (`userId`),
  CONSTRAINT `UserId_Audit` FOREIGN KEY (`userId`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `challenge`
--

DROP TABLE IF EXISTS `challenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `challenge` (
  `idChallenge` varchar(128) NOT NULL,
  `categoryUnitId` int(11) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idChallenge`),
  UNIQUE KEY `idChallenge_UNIQUE` (`idChallenge`),
  KEY `catergoryUnit_idx` (`categoryUnitId`),
  CONSTRAINT `catergoryUnit_challenge` FOREIGN KEY (`categoryUnitId`) REFERENCES `categoryunit` (`idCategoryUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `communitychallenge`
--

DROP TABLE IF EXISTS `communitychallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `communitychallenge` (
  `idCommunityChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `communityId` int(11) NOT NULL,
  `challengeId` varchar(128) NOT NULL,
  `startedChallenge` tinyint(1) NOT NULL,
  PRIMARY KEY (`idCommunityChallenge`),
  UNIQUE KEY `idCommunityChallenge_UNIQUE` (`idCommunityChallenge`),
  KEY `community_idx` (`communityId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `challenge_communityChallenge` FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `community_communityChallenge` FOREIGN KEY (`communityId`) REFERENCES `community` (`idCommunity`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dataremovalaudit`
--

DROP TABLE IF EXISTS `dataremovalaudit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dataremovalaudit` (
  `idDataRemovalAudit` int(11) NOT NULL AUTO_INCREMENT,
  `userIdWhoRemoved` int(11) NOT NULL,
  `userIdData` int(11) NOT NULL,
  `dateRemoved` datetime NOT NULL,
  `dataRemoved` varchar(1000) NOT NULL,
  `message` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idDataRemovalAudit`),
  UNIQUE KEY `idDataRemovalAudit_UNIQUE` (`idDataRemovalAudit`),
  KEY `userWhoRemoved_audit_idx` (`userIdWhoRemoved`),
  KEY `userIdData_audit_idx` (`userIdData`),
  CONSTRAINT `userIdData_audit` FOREIGN KEY (`userIdData`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userWhoRemoved_audit` FOREIGN KEY (`userIdWhoRemoved`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `accessToken` varchar(250) DEFAULT NULL,
  `refreshToken` varchar(250) DEFAULT NULL,
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
  `apiEndpoint` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idDeviceType`),
  UNIQUE KEY `idDeviceType_UNIQUE` (`idDeviceType`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groupchallenge`
--

DROP TABLE IF EXISTS `groupchallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupchallenge` (
  `idGroupChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `challengeId` varchar(128) NOT NULL,
  `startedChallenge` tinyint(1) NOT NULL,
  PRIMARY KEY (`idGroupChallenge`),
  UNIQUE KEY `idGroupChallenge_UNIQUE` (`idGroupChallenge`),
  KEY `group_idx` (`groupId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `challenge_groupChallenge` FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `group_groupChallenge` FOREIGN KEY (`groupId`) REFERENCES `team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `result`
--

DROP TABLE IF EXISTS `result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `result` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryUnitId` int(11) NOT NULL,
  `value` int(11) NOT NULL DEFAULT '0',
  `challengeId` varchar(128) NOT NULL,
  `communityId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `categoryunit_result_idx` (`categoryUnitId`),
  KEY `challenge_result_idx` (`challengeId`),
  KEY `community_result_idx` (`communityId`),
  CONSTRAINT `categoryunit_result` FOREIGN KEY (`categoryUnitId`) REFERENCES `categoryunit` (`idCategoryUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `challenge_result` FOREIGN KEY (`challengeId`) REFERENCES `challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `community_result` FOREIGN KEY (`communityId`) REFERENCES `community` (`idCommunity`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `groupId` int(11) DEFAULT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `Email_UNIQUE` (`email`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  KEY `userRole_idx` (`userRoleId`),
  KEY `group_idx` (`groupId`),
  CONSTRAINT `group_user` FOREIGN KEY (`groupId`) REFERENCES `team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userRole_user` FOREIGN KEY (`userRoleId`) REFERENCES `userrole` (`idUserRole`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userchallenge`
--

DROP TABLE IF EXISTS `userchallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userchallenge` (
  `idUserChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `challengeId` varchar(128) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webserviceauth`
--

DROP TABLE IF EXISTS `webserviceauth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `webserviceauth` (
  `authtoken` varchar(255) NOT NULL,
  `appname` varchar(255) NOT NULL,
  `userid` int(11) NOT NULL,
  `expire` datetime DEFAULT NULL,
  `status_flag` int(1) DEFAULT '1',
  PRIMARY KEY (`authtoken`),
  KEY `userid_idx` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-30 11:57:53
