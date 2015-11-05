-- MySQL Script generated by MySQL Workbench
-- 10/31/15 00:52:46
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema GoAber
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema GoAber
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `GoAber` DEFAULT CHARACTER SET utf8 ;
USE `GoAber` ;

-- -----------------------------------------------------
-- Table `GoAber`.`UserRole`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`UserRole` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`UserRole` (
  `idUserRole` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idUserRole`),
  UNIQUE INDEX `idUserRole_UNIQUE` (`idUserRole` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`UserCredentials`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`UserCredentials` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`UserCredentials` (
  `idUserCredentials` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(100) NULL,
  PRIMARY KEY (`idUserCredentials`),
  UNIQUE INDEX `idUserCredentials_UNIQUE` (`idUserCredentials` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Community`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Community` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Community` (
  `idCommunity` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idCommunity`),
  UNIQUE INDEX `idCommunity_UNIQUE` (`idCommunity` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Team`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Team` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Team` (
  `idGroup` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `communityId` INT NULL,
  PRIMARY KEY (`idGroup`),
  UNIQUE INDEX `idGroup_UNIQUE` (`idGroup` ASC),
  INDEX `community_idx` (`communityId` ASC),
  CONSTRAINT `community_team`
    FOREIGN KEY (`communityId`)
    REFERENCES `GoAber`.`Community` (`idCommunity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`User` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `nickname` VARCHAR(45) NULL,
  `userRoleId` INT NOT NULL,
  `userCredentialsId` INT NULL,
  `groupId` INT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `Email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC),
  INDEX `userRole_idx` (`userRoleId` ASC),
  INDEX `userCredentials_idx` (`userCredentialsId` ASC),
  INDEX `group_idx` (`groupId` ASC),
  CONSTRAINT `userRole_user`
    FOREIGN KEY (`userRoleId`)
    REFERENCES `GoAber`.`UserRole` (`idUserRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `userCredentials_user`
    FOREIGN KEY (`userCredentialsId`)
    REFERENCES `GoAber`.`UserCredentials` (`idUserCredentials`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `group_user`
    FOREIGN KEY (`groupId`)
    REFERENCES `GoAber`.`Team` (`idGroup`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`Category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`Category` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`Category` (
  `idCategory` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
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
  PRIMARY KEY (`idChallenge`),
  UNIQUE INDEX `idChallenge_UNIQUE` (`idChallenge` ASC))
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
  UNIQUE INDEX `idGroupChallenge_UNIQUE` (`idGroupChallenge` ASC),
  INDEX `group_idx` (`groupId` ASC),
  INDEX `challenge_idx` (`challengeId` ASC),
  CONSTRAINT `group_groupChallenge`
    FOREIGN KEY (`groupId`)
    REFERENCES `GoAber`.`Team` (`idGroup`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `challenge_groupChallenge`
    FOREIGN KEY (`challengeId`)
    REFERENCES `GoAber`.`Challenge` (`idChallenge`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `GoAber`.`UserChallenge`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GoAber`.`UserChallenge` ;

CREATE TABLE IF NOT EXISTS `GoAber`.`UserChallenge` (
  `idUserChallenge` INT NOT NULL AUTO_INCREMENT,
  `challengeId` INT NOT NULL,
  `userId` INT NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
