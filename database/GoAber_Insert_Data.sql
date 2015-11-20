-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: goaber
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

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES ('admin'),('coordinator'),('participant');
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (5,'admin@test.com','J8xplPwcAc5mWca93Km2nExqlBgGXmEsadEQs/exH4o=','admin',6,NULL,NULL,'admin'),(6,'coord@test.com','J8xplPwcAc5mWca93Km2nExqlBgGXmEsadEQs/exH4o=','coord',7,NULL,NULL,'admin'),(8,'user@test.com','J8xplPwcAc5mWca93Km2nExqlBgGXmEsadEQs/exH4o=','user',9,NULL,NULL,'participant');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `UserRole`
--

LOCK TABLES `UserRole` WRITE;
/*!40000 ALTER TABLE `UserRole` DISABLE KEYS */;
INSERT INTO `UserRole` VALUES (6,'admin','admin@test.com'),(7,'coordinator','coord@test.com'),(9,'participant','user@test.com'),(12,'participant','helen@test.com');
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
