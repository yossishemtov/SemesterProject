-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: localhost    Database: project
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `generalparkworker`
--

DROP TABLE IF EXISTS `generalparkworker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generalparkworker` (
  `workerId` int NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `worksAtPark` int DEFAULT NULL,
  `isloggedin` int DEFAULT '0',
  PRIMARY KEY (`workerId`),
  KEY `worksAtPark` (`worksAtPark`),
  CONSTRAINT `generalparkworker_ibfk_1` FOREIGN KEY (`worksAtPark`) REFERENCES `park` (`parkNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `generalparkworker`
--

LOCK TABLES `generalparkworker` WRITE;
/*!40000 ALTER TABLE `generalparkworker` DISABLE KEYS */;
INSERT INTO `generalparkworker` VALUES (1,'John','Doe','johndoe@example.com','Park Manager','johndoe123','password123',1,0),(2,'Jane','Smith','janesmith@example.com','Park Manager','janesmith456','password456',2,0),(3,'Michael','Johnson','michaeljohnson@example.com','Park Manager','michaelj','password789',3,0),(4,'Emily ','Williams','emilywilliams@example.com','Worker','emilyw','password123',1,0),(5,'Chavi','Alonso','ChaviAlonso@e.braude.ac.il','Worker','userchavi','123456a',2,0),(6,'Yossi','Hanoder','Yossihanoder@e.braude.ac.il','Worker','yossinoder','123456a',3,0),(7,'Emanuel','Braude','emanuelbraude@e.braude.ac.il','Department Manager','emanuelb','123456a',1,0),(8,'lil','bullet','lilbullet@e.braude.ac.il','Department Manager','lilbullet','123456a',2,0),(9,'Rosh','Tirosh','roshtirosh@e.braude.ac.il','Department Manager','roshtirosh','123456a',3,0);
/*!40000 ALTER TABLE `generalparkworker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `orderId` int NOT NULL,
  `travlerId` int NOT NULL,
  `parkNumber` int NOT NULL,
  `amountOfVisitors` int NOT NULL,
  `price` float NOT NULL,
  `visitorEmail` varchar(255) DEFAULT NULL,
  `date` date NOT NULL,
  `TelephoneNumber` varchar(15) DEFAULT NULL,
  `visitTime` time NOT NULL,
  `orderStatus` varchar(255) DEFAULT NULL,
  `typeOfOrder` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`orderId`),
  KEY `travlerId` (`travlerId`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`travlerId`) REFERENCES `travler` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,1214214,1,5,50,'alice@example.com','2024-07-04','054-7376231','09:00:00','PENDING','FAMILY\r'),(2,2654456,2,3,30,'bob@example.com','2024-07-05','052-7355231','14:00:00','CONFIRMED','FAMILY\r'),(3,3123123,3,6,60,'claire@example.com','2024-07-06','054-7376431','08:00:00','PENDING','GUIDEDGROUP\r'),(4,4643544,2,4,40,'david@example.com','2024-07-07','054-2136231','10:30:00','CONFIRMED','FAMILY\r'),(5,4643544,2,1,10,'david@example.com','2024-08-07','054-2136231','10:30:00','CANCEL','SOLO'),(6,3847485,1,14,100,'JoeBoden@e.braude.ac.il','2024-08-07','054-2232574','12:00:00','PENDING','GUIDEDGROUP\rGUIDEDGROUP\r'),(7,3847482,3,3,10,'deadpool@e.braude.ac.il','2024-07-15','054-2232594','14:00:00','CONFIRMED','FAMILY'),(8,3847486,2,1,40,'Barakobomo@e.braude.ac.il','2024-07-07','054-2232511','08:00:00','PENDING','SOLO');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `park`
--

DROP TABLE IF EXISTS `park`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `park` (
  `name` varchar(255) DEFAULT NULL,
  `parkNumber` int NOT NULL,
  `maxVisitors` int DEFAULT NULL,
  `capacity` int DEFAULT NULL,
  `currentVisitors` int DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `staytime` int DEFAULT NULL,
  `workersAmount` int DEFAULT NULL,
  `managerId` int DEFAULT NULL,
  `workingTime` int DEFAULT NULL,
  PRIMARY KEY (`parkNumber`),
  KEY `managerId` (`managerId`),
  CONSTRAINT `park_ibfk_1` FOREIGN KEY (`managerId`) REFERENCES `generalparkworker` (`workerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `park`
--

LOCK TABLES `park` WRITE;
/*!40000 ALTER TABLE `park` DISABLE KEYS */;
INSERT INTO `park` VALUES ('Mount Rainier',1,20,30,1,'Washington',4,2,1,8),('Yellowstone',2,57,60,1,'Utah',5,1,2,7),('Yosemite',3,44,50,1,'Wyoming',6,1,3,8);
/*!40000 ALTER TABLE `park` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `travler`
--

DROP TABLE IF EXISTS `travler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `travler` (
  `id` int NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `GroupGuide` tinyint(1) DEFAULT '0',
  `isloggedin` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `travler`
--

LOCK TABLES `travler` WRITE;
/*!40000 ALTER TABLE `travler` DISABLE KEYS */;
INSERT INTO `travler` VALUES (1214214,'Alice','Johnson','alice@example.com','054-7376231',0,0),(2654456,'Bob','Smith','bob@example.com','052-7355231',0,0),(3123123,'Claire','Williams','claire@example.com','054-7376431',1,0),(3847482,'Dead','Pool','deadpool@e.braude.ac.il','054-2232594',0,0),(3847485,'Joe','Buden','JoeBoden@e.braude.ac.il','054-2232574',1,0),(3847486,'Barak','Obomo','Barakobomo@e.braude.ac.il','054-2232511',0,0),(3847487,'Donald','Brump','Donalbrump@e.braude.ac.il','054-2232564',0,0),(3847488,'Vladimir','Butin','vladimirbutin@e.braude.ac.il','054-2232114',0,0),(4643544,'David','Brown','david@example.com','054-2136231 ',0,0),(5634523,'Emanuel','Davidov','emanuel.davidov@e.braude.ac.il','053-3324478',0,0);
/*!40000 ALTER TABLE `travler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit`
--

DROP TABLE IF EXISTS `visit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visit` (
  `visitDate` date DEFAULT NULL,
  `enteringTime` time DEFAULT NULL,
  `exitingTime` time DEFAULT NULL,
  `parkNumber` int DEFAULT NULL,
  KEY `parkNumber` (`parkNumber`),
  CONSTRAINT `visit_ibfk_1` FOREIGN KEY (`parkNumber`) REFERENCES `park` (`parkNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit`
--

LOCK TABLES `visit` WRITE;
/*!40000 ALTER TABLE `visit` DISABLE KEYS */;
INSERT INTO `visit` VALUES ('2024-07-04','09:00:00','12:00:00',1),('2024-07-05','14:00:00','17:00:00',2),('2024-07-06','08:00:00','11:00:00',1),('2024-07-07','10:30:00','13:30:00',3);
/*!40000 ALTER TABLE `visit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waitinglist`
--

DROP TABLE IF EXISTS `waitinglist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitinglist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `count` int NOT NULL,
  `travelerId` int NOT NULL,
  `parkNumber` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `travelerId` (`travelerId`),
  KEY `parkNumber` (`parkNumber`),
  CONSTRAINT `waitinglist_ibfk_1` FOREIGN KEY (`travelerId`) REFERENCES `travler` (`id`),
  CONSTRAINT `waitinglist_ibfk_2` FOREIGN KEY (`parkNumber`) REFERENCES `park` (`parkNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waitinglist`
--

LOCK TABLES `waitinglist` WRITE;
/*!40000 ALTER TABLE `waitinglist` DISABLE KEYS */;
INSERT INTO `waitinglist` VALUES (1,3,1214214,1),(2,2,2654456,2),(3,1,4643544,3),(4,4,2654456,2);
/*!40000 ALTER TABLE `waitinglist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-13 18:13:54
