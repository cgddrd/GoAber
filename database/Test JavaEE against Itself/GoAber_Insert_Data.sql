-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: goaber2
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

USE `goaber2`;

LOCK TABLES `Community` WRITE;
/*!40000 ALTER TABLE `Community` DISABLE KEYS */;
INSERT INTO `Community` VALUES (1, 'AberUni','http://localhost:8080/','1','goaber2ChallengesWS/goaber2ChallengesWS', null);
/*!40000 ALTER TABLE `Community` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `Team` WRITE;
/*!40000 ALTER TABLE `Team` DISABLE KEYS */;
INSERT INTO `Team` VALUES (1,'AberCompSci', 1), (2,'AberIBERS', 1);
/*!40000 ALTER TABLE `Team` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `Category` WRITE;
/*!40000 ALTER TABLE `Category` DISABLE KEYS */;
INSERT INTO `Category` VALUES (1,'Running'), (2,'Swimming');
/*!40000 ALTER TABLE `Category` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `Unit` WRITE;
/*!40000 ALTER TABLE `Unit` DISABLE KEYS */;
INSERT INTO `Unit` VALUES (1,'Steps'), (2,'Strokes');
/*!40000 ALTER TABLE `Unit` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `CategoryUnit` WRITE;
/*!40000 ALTER TABLE `CategoryUnit` DISABLE KEYS */;
INSERT INTO `CategoryUnit` VALUES (1,1,1), (2,2,2);
/*!40000 ALTER TABLE `CategoryUnit` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES ('administrator'),('coordinator'),('participant');
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `scheduletype` WRITE;
/*!40000 ALTER TABLE `scheduletype` DISABLE KEYS */;
INSERT INTO `scheduletype` VALUES ('O', 'Once'),('R', 'Recurring');
/*!40000 ALTER TABLE `scheduletype` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `tasktype` WRITE;
/*!40000 ALTER TABLE `tasktype` DISABLE KEYS */;
INSERT INTO `tasktype` VALUES ('Fitbit', 'Fitbit'),('Challenge', 'Challenge');
/*!40000 ALTER TABLE `tasktype` ENABLE KEYS */;
UNLOCK TABLES;


INSERT INTO `goaber2`.`community` (`name`,`domain`,`home`,`challengesEndPoint`) VALUES ('AberUni','http://localhost:8080/','1','goaber2ChallengesWS/goaber2ChallengesWS');

INSERT INTO `goaber2`.`team` (`name`, `communityId`) VALUES ('AberCompSci', '1');
INSERT INTO `goaber2`.`team` (`name`, `communityId`) VALUES ('AberIBERS', '1');


--
-- Dumping data for table `User`
--


LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'admin@test.com','+QUCS9lsdwiBAJ/YeyLdjWgxWVByMSolHNP6N/aGG8U=','admin',1,1),(2,'coord@test.com','+QUCS9lsdwiBAJ/YeyLdjWgxWVByMSolHNP6N/aGG8U=','coord',2,4),(3,'user@test.com','+QUCS9lsdwiBAJ/YeyLdjWgxWVByMSolHNP6N/aGG8U=','user',3,2);

/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `UserRole` WRITE;
/*!40000 ALTER TABLE `UserRole` DISABLE KEYS */;
INSERT INTO `UserRole` VALUES (1,'administrator','admin@test.com'),(2,'coordinator','coord@test.com'),(3,'participant','user@test.com');
/*!40000 ALTER TABLE `UserRole` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

INSERT INTO `devicetype` (`name`,`tokenEndpoint`,`consumerKey`,`consumerSecret`,`clientId`,`authorizationEndpoint`, `apiEndpoint`) VALUES ('Jawbone','https://jawbone.com/auth/oauth2/token','','07e4083f111f1a44ccba1bf94d21c95f5486f8f1','mCZQ7V2DbgQ','https://jawbone.com/auth/oauth2/auth', 'https://jawbone.com/nudge/api/v.1.1/users/@me');
INSERT INTO `devicetype` (`name`,`tokenEndpoint`,`consumerKey`,`consumerSecret`,`clientId`,`authorizationEndpoint`, `apiEndpoint`) VALUES ('Fitbit','https://api.fitbit.com/oauth2/token',NULL,'6f961c38f86be0a77f903ca9a2524865','22B2TQ','https://www.fitbit.com/oauth2/authorize ', 'https://jawbone.com/nudge/api/v.1.1/users/@me');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-10 14:41:17

INSERT INTO `goaber2`.`category` (`name`) VALUES ('Walking');

INSERT INTO `goaber2`.`unit` (`name`) VALUES ('steps');

INSERT INTO `goaber2`.`categoryunit` (`categoryId`, `unitId`) VALUES ('1', '1');
