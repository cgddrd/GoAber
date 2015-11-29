CREATE DATABASE IF NOT EXISTS `goaber` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `GoAber`;
-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: GoAber
-- ------------------------------------------------------
-- Server version	5.5.42

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

-- -----------------------------------------------------

-- Table `GoAber`.`Audit`

-- -----------------------------------------------------

DROP TABLE IF EXISTS `GoAber`.`Audit` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Audit` (
  `idAudit` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NULL,
  `urlAccessed` VARCHAR(255) NULL,
  `timestamp` DATETIME NULL,
  `message` VARCHAR(1000) NULL,
  PRIMARY KEY (`idAudit`),
  INDEX `UserId_Audit_idx` (`userId` ASC),
  UNIQUE INDEX `idAudit_UNIQUE` (`idAudit` ASC),
  CONSTRAINT `UserId_Audit`
    FOREIGN KEY (`userId`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------

-- Table `GoAber`.`DataRemovalAudit`

-- -----------------------------------------------------

DROP TABLE IF EXISTS `GoAber`.`DataRemovalAudit` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`DataRemovalAudit` (
  `idDataRemovalAudit` INT NOT NULL AUTO_INCREMENT,
  `userIdWhoRemoved` INT NOT NULL,
  `userIdData` INT NOT NULL,
  `dateRemoved` DATETIME NOT NULL,
  `dataRemoved` VARCHAR(1000) NOT NULL,
  `message` VARCHAR(200) NULL,
  PRIMARY KEY (`idDataRemovalAudit`),
  UNIQUE INDEX `idDataRemovalAudit_UNIQUE` (`idDataRemovalAudit` ASC),
  INDEX `userWhoRemoved_audit_idx` (`userIdWhoRemoved` ASC),
  INDEX `userIdData_audit_idx` (`userIdData` ASC),
  CONSTRAINT `userWhoRemoved_audit`
    FOREIGN KEY (`userIdWhoRemoved`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `userIdData_audit`
    FOREIGN KEY (`userIdData`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

--
-- Table structure for table `ActivityData`
--

DROP TABLE IF EXISTS `ActivityData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ActivityData` (
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
  CONSTRAINT `user_activityData` FOREIGN KEY (`userId`) REFERENCES `User` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `catergoryUnit_activityData` FOREIGN KEY (`categoryUnitId`) REFERENCES `CategoryUnit` (`idCategoryUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Category`
--

DROP TABLE IF EXISTS `Category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Category` (
  `idCategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`idCategory`),
  UNIQUE KEY `idCategory_UNIQUE` (`idCategory`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CategoryUnit`
--

DROP TABLE IF EXISTS `CategoryUnit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CategoryUnit` (
  `idCategoryUnit` int(11) NOT NULL AUTO_INCREMENT,
  `categoryId` int(11) NOT NULL,
  `unitId` int(11) NOT NULL,
  PRIMARY KEY (`idCategoryUnit`),
  UNIQUE KEY `idCategoryUnit_UNIQUE` (`idCategoryUnit`),
  KEY `categoryId_idx` (`categoryId`),
  KEY `unitId_idx` (`unitId`),
  CONSTRAINT `categoryId_categoryUnit` FOREIGN KEY (`categoryId`) REFERENCES `Category` (`idCategory`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `unitId_categoryUnit` FOREIGN KEY (`unitId`) REFERENCES `Unit` (`idUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Challenge`
--

DROP TABLE IF EXISTS `Challenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Challenge` (
  `idChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `categoryUnitId` int(11) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idChallenge`),
  UNIQUE KEY `idChallenge_UNIQUE` (`idChallenge`), 
  KEY `catergoryUnit_idx` (`categoryUnitId`),
  CONSTRAINT `catergoryUnit_challenge` FOREIGN KEY (`categoryUnitId`) REFERENCES `CategoryUnit` (`idCategoryUnit`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Community`
--

DROP TABLE IF EXISTS `Community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Community` (
  `idCommunity` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `endpointUrl` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idCommunity`),
  UNIQUE KEY `idCommunity_UNIQUE` (`idCommunity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Device`
--

DROP TABLE IF EXISTS `Device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Device` (
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
  CONSTRAINT `deviceType_Device` FOREIGN KEY (`deviceTypeId`) REFERENCES `DeviceType` (`idDeviceType`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_Device` FOREIGN KEY (`userId`) REFERENCES `User` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DeviceType`
--

DROP TABLE IF EXISTS `DeviceType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DeviceType` (
  `idDeviceType` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `tokenEndpoint` varchar(250) DEFAULT NULL,
  `consumerKey` varchar(45) DEFAULT NULL,
  `consumerSecret` varchar(45) DEFAULT NULL,
  `clientId` varchar(45) DEFAULT NULL,
  `authorizationEndpoint` varchar(250) DEFAULT NULL,
  `apiEndpoint` VARCHAR(250) NULL,
  PRIMARY KEY (`idDeviceType`),
  UNIQUE KEY `idDeviceType_UNIQUE` (`idDeviceType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GroupChallenge`
--

DROP TABLE IF EXISTS `GroupChallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GroupChallenge` (
  `idGroupChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `challengeId` int(11) NOT NULL,
  `startedChallenge` bool NOT NULL,
  PRIMARY KEY (`idGroupChallenge`),
  UNIQUE KEY `idGroupChallenge_UNIQUE` (`idGroupChallenge`),
  KEY `group_idx` (`groupId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `group_groupChallenge` FOREIGN KEY (`groupId`) REFERENCES `Team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `challenge_groupChallenge` FOREIGN KEY (`challengeId`) REFERENCES `Challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CommunityChallenge`
--

DROP TABLE IF EXISTS `CommunityChallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CommunityChallenge` (
  `idCommunityChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `communityId` int(11) NOT NULL,
  `challengeId` int(11) NOT NULL,
  `startedChallenge` bool NOT NULL,
  PRIMARY KEY (`idCommunityChallenge`),
  UNIQUE KEY `idCommunityChallenge_UNIQUE` (`idCommunityChallenge`),
  KEY `community_idx` (`communityId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `community_communityChallenge` FOREIGN KEY (`communityId`) REFERENCES `Community` (`idCommunity`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `challenge_communityChallenge` FOREIGN KEY (`challengeId`) REFERENCES `Challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `idRole` varchar(45) NOT NULL,
  PRIMARY KEY (`idRole`),
  UNIQUE KEY `roleid_UNIQUE` (`idRole`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Team`
--

DROP TABLE IF EXISTS `Team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Team` (
  `idGroup` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `communityId` int(11) DEFAULT NULL,
  PRIMARY KEY (`idGroup`),
  UNIQUE KEY `idGroup_UNIQUE` (`idGroup`),
  KEY `community_idx` (`communityId`),
  CONSTRAINT `community_team` FOREIGN KEY (`communityId`) REFERENCES `Community` (`idCommunity`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Unit`
--

DROP TABLE IF EXISTS `Unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Unit` (
  `idUnit` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`idUnit`),
  UNIQUE KEY `idUnit_UNIQUE` (`idUnit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
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
  CONSTRAINT `group_user` FOREIGN KEY (`groupId`) REFERENCES `Team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userRole_user` FOREIGN KEY (`userRoleId`) REFERENCES `UserRole` (`idUserRole`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserChallenge`
--

DROP TABLE IF EXISTS `UserChallenge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserChallenge` (
  `idUserChallenge` int(11) NOT NULL AUTO_INCREMENT,
  `challengeId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`idUserChallenge`),
  UNIQUE KEY `idUserChallenge_UNIQUE` (`idUserChallenge`),
  KEY `user_idx` (`userId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `user_userChallenge` FOREIGN KEY (`userId`) REFERENCES `User` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `challenge_userChallenge` FOREIGN KEY (`challengeId`) REFERENCES `Challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserRole`
--

DROP TABLE IF EXISTS `UserRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserRole` (
  `idUserRole` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` varchar(45) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`idUserRole`),
  UNIQUE KEY `idUserRole_UNIQUE` (`idUserRole`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `JobDetail`
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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-10 11:47:44
