-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: tutter
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `POST_ID` int NOT NULL AUTO_INCREMENT,
  `USER_ID` int NOT NULL,
  `DESCRIPTION` varchar(200) COLLATE utf8_bin NOT NULL,
  `CREATED_AT` datetime NOT NULL,
  `PHOTO_LINK` text COLLATE utf8_bin,
  PRIMARY KEY (`POST_ID`),
  UNIQUE KEY `POST_ID_UNIQUE` (`POST_ID`),
  KEY `POST_USER_idx` (`USER_ID`),
  CONSTRAINT `POST_USER` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,'hello','2020-03-01 00:00:00','https://www.himgs.com/imagenes/hello/social/hello-fb-logo.png'),(7,3,'aaaaaa','2020-02-15 15:00:00',NULL),(8,5,'im smoking, you arent smoking','2019-07-10 12:00:00',NULL),(9,1,'its tutter','2020-05-10 19:00:00',NULL),(10,8,'i have sous','2020-01-01 00:00:00',NULL),(11,6,'big boy is in block','2020-03-08 09:00:00',NULL),(12,2,'nature','2020-02-15 18:00:00','https://i0.wp.com/cdn-prod.medicalnewstoday.com/content/images/articles/325/325466/man-walking-dog.jpg?w=1155&h=1541'),(13,7,'my table','2019-08-10 13:00:00','https://www.ikea.com/us/en/images/products/linnmon-adils-table-black-brown-black__0737172_PE740915_S5.JPG'),(14,3,'this laptop is great','2020-03-10 19:00:00','https://images-na.ssl-images-amazon.com/images/I/71h6PpGaz9L._AC_SL1500_.jpg'),(15,5,'father is coming','2020-02-01 00:00:00','https://scx2.b-cdn.net/gfx/news/hires/2016/1-lowtestoster.jpg');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `POST_ID` int NOT NULL,
  `USER_ID` int NOT NULL,
  KEY `LIKE_POST_idx` (`POST_ID`),
  KEY `LIKE_USER_idx` (`USER_ID`),
  CONSTRAINT `LIKE_POST` FOREIGN KEY (`POST_ID`) REFERENCES `post` (`POST_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIKE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

LOCK TABLES `post_like` WRITE;
/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
INSERT INTO `post_like` VALUES (1,2),(1,7),(11,1),(11,5),(12,10),(15,3),(8,2),(10,4),(13,6),(14,4);
/*!40000 ALTER TABLE `post_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_tag`
--

DROP TABLE IF EXISTS `post_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_tag` (
  `POST_ID` int NOT NULL,
  `TAG_ID` int NOT NULL,
  KEY `POST_TAG_POST_idx` (`POST_ID`),
  KEY `POST_TAG_TAG_idx` (`TAG_ID`),
  CONSTRAINT `POST_TAG_POST` FOREIGN KEY (`POST_ID`) REFERENCES `post` (`POST_ID`),
  CONSTRAINT `POST_TAG_TAG` FOREIGN KEY (`TAG_ID`) REFERENCES `tag` (`TAG_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_tag`
--

LOCK TABLES `post_tag` WRITE;
/*!40000 ALTER TABLE `post_tag` DISABLE KEYS */;
INSERT INTO `post_tag` VALUES (1,5),(1,7),(8,3),(10,3),(12,4),(9,7),(11,5),(8,5),(13,3),(11,1);
/*!40000 ALTER TABLE `post_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `TAG_ID` int NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`TAG_ID`),
  UNIQUE KEY `TAG_ID_UNIQUE` (`TAG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'great'),(2,'cool'),(3,'yes'),(4,'wow'),(5,'amazing'),(6,'blood'),(7,'poker'),(8,'table'),(9,'windows'),(10,'mouse');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `USER_ID` int NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_ID_UNIQUE` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'nikmalevich'),(2,'anna'),(3,'nikita'),(4,'nik228'),(5,'andrey_zverio'),(6,'anna'),(7,'andrey_ogrizok'),(8,'tanya_mama'),(9,'baba_masha'),(10,'nikita');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-10 20:35:54
