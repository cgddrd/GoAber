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

DROP TABLE IF EXISTS `Audit` ;

CREATE TABLE IF NOT EXISTS `Audit` (
`idAudit` INT NOT NULL,
`userId` INT NULL,
`urlAccessed` VARCHAR(255) NULL,
`timestamp` DATETIME NULL,
`message` VARCHAR(1000) NULL,
PRIMARY KEY (`idAudit`),
KEY `UserId_Audit_idx` (`userId` ASC),
CONSTRAINT `UserId_Audit`
FOREIGN KEY (`userId`)
REFERENCES `GoAber`.`User` (`idUser`)
ON DELETE NO ACTION
ON UPDATE NO ACTION)
ENGINE = InnoDB DEFAULT CHARSET=utf8;

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
  UNIQUE INDEX `idCategory_UNIQUE` (`idCategory` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Unit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Unit` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Unit` (
  `idUnit` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUnit`),
  UNIQUE INDEX `idUnit_UNIQUE` (`idUnit` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`CategoryUnit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`CategoryUnit` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`CategoryUnit` (
  `idCategoryUnit` INT NOT NULL AUTO_INCREMENT,
  `categoryId` INT NOT NULL,
  `unitId` INT NOT NULL,
  PRIMARY KEY (`idCategoryUnit`),
  UNIQUE INDEX `idCategoryUnit_UNIQUE` (`idCategoryUnit` ASC),
  INDEX `categoryId_idx` (`categoryId` ASC),
  INDEX `unitId_idx` (`unitId` ASC),
  CONSTRAINT `categoryId_categoryUnit`
    FOREIGN KEY (`categoryId`)
    REFERENCES `GoAber`.`Category` (`idCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `unitId_categoryUnit`
    FOREIGN KEY (`unitId`)
    REFERENCES `GoAber`.`Unit` (`idUnit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`ActivityData`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`ActivityData` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`ActivityData` (
  `idActivityData` INT NOT NULL AUTO_INCREMENT,
  `categoryUnitId` INT NOT NULL,
  `userId` INT NOT NULL,
  `value` INT NULL,
  `lastUpdated` DATETIME NULL,
  `date` DATE NULL,
  PRIMARY KEY (`idActivityData`),
  UNIQUE INDEX `idActivityData_UNIQUE` (`idActivityData` ASC),
  INDEX `user_idx` (`userId` ASC),
  INDEX `catergoryUnit_idx` (`categoryUnitId` ASC),
  CONSTRAINT `user_activityData`
    FOREIGN KEY (`userId`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `catergoryUnit_activityData`
    FOREIGN KEY (`categoryUnitId`)
    REFERENCES `GoAber`.`CategoryUnit` (`idCategoryUnit`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Challenge`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Challenge` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Challenge` (
  `idChallenge` INT NOT NULL AUTO_INCREMENT,
  `categoryUnit` INT NOT NULL,
  `startTime` DATETIME NOT NULL,
  `endTime` DATETIME NULL,
  `name` VARCHAR(100) NULL,
  `communityStartedBy` INT NULL,
  PRIMARY KEY (`idChallenge`),
  UNIQUE INDEX `idChallenge_UNIQUE` (`idChallenge` ASC),
  INDEX `communityStartedBy_Challenge_idx` (`communityStartedBy` ASC),
  CONSTRAINT `communityStartedBy_Challenge`
    FOREIGN KEY (`communityStartedBy`)
    REFERENCES `GoAber`.`Community` (`idCommunity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`GroupChallenge`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`GroupChallenge` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`GroupChallenge` (
  `idGroupChallenge` INT NOT NULL AUTO_INCREMENT,
  `groupId` INT NOT NULL,
  `challengeId` INT NOT NULL,
  PRIMARY KEY (`idGroupChallenge`),
  UNIQUE KEY `idGroupChallenge_UNIQUE` (`idGroupChallenge`),
  KEY `group_idx` (`groupId`),
  KEY `challenge_idx` (`challengeId`),
  CONSTRAINT `group_groupChallenge` FOREIGN KEY (`groupId`) REFERENCES `Team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `challenge_groupChallenge` FOREIGN KEY (`challengeId`) REFERENCES `Challenge` (`idChallenge`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `userCredentialsId` int(11) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `roleId` varchar(45) NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `Email_UNIQUE` (`email`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  KEY `userRole_idx` (`userRoleId`),
  KEY `userCredentials_idx` (`userCredentialsId`),
  KEY `group_idx` (`groupId`),
  CONSTRAINT `group_user` FOREIGN KEY (`groupId`) REFERENCES `Team` (`idGroup`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userCredentials_user` FOREIGN KEY (`userCredentialsId`) REFERENCES `UserCredentials` (`idUserCredentials`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
  UNIQUE INDEX `idUserChallenge_UNIQUE` (`idUserChallenge` ASC),
  INDEX `user_idx` (`userId` ASC),
  INDEX `challenge_idx` (`challengeId` ASC),
  CONSTRAINT `user_userChallenge`
    FOREIGN KEY (`userId`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `challenge_userChallenge`
    FOREIGN KEY (`challengeId`)
    REFERENCES `GoAber`.`Challenge` (`idChallenge`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`DeviceType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`DeviceType` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`DeviceType` (
  `idDeviceType` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `tokenEndpoint` VARCHAR(250) NULL,
  `consumerKey` VARCHAR(45) NULL,
  `consumerSecret` VARCHAR(45) NULL,
  `clientId` VARCHAR(45) NULL,
  `authorizationEndpoint` VARCHAR(250) NULL,
  PRIMARY KEY (`idDeviceType`),
  UNIQUE INDEX `idDeviceType_UNIQUE` (`idDeviceType` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Device`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Device` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Device` (
  `idDevice` INT NOT NULL AUTO_INCREMENT,
  `deviceTypeId` INT NOT NULL,
  `userId` INT NOT NULL,
  `accessToken` VARCHAR(45) NULL,
  `refreshToken` VARCHAR(45) NULL,
  `tokenExpiration` DATETIME NULL,
  PRIMARY KEY (`idDevice`),
  UNIQUE INDEX `idDevice_UNIQUE` (`idDevice` ASC),
  INDEX `deviceType_idx` (`deviceTypeId` ASC),
  INDEX `user_idx` (`userId` ASC),
  CONSTRAINT `deviceType_Device`
    FOREIGN KEY (`deviceTypeId`)
    REFERENCES `GoAber`.`DeviceType` (`idDeviceType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_Device`
    FOREIGN KEY (`userId`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Audit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Audit` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Audit` (
  `idAudit` INT NOT NULL,
  `userId` INT NULL,
  `urlAccessed` VARCHAR(255) NULL,
  `timestamp` DATETIME NULL,
  `message` VARCHAR(1000) NULL,
  PRIMARY KEY (`idAudit`),
  INDEX `UserId_Audit_idx` (`userId` ASC),
  CONSTRAINT `UserId_Audit`
    FOREIGN KEY (`userId`)
    REFERENCES `GoAber`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
