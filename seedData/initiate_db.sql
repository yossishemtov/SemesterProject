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
CREATE TABLE ChangeRequests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    parkName VARCHAR(255) NOT NULL,
    parkNumber INT NOT NULL,
    maxVisitors INT NOT NULL,
    gap DOUBLE NOT NULL,
    staytime INT NOT NULL,
    approved ENUM('REJECTED', 'APPROVAL', 'WAITING_FOR_APPROVAL') NOT NULL
);


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

INSERT INTO `order` (orderId, travlerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder) VALUES
(1, 101, 1, 5, 100.00, 'email1@example.com', '2024-03-12', '1234567890', '09:00:00', 'CONFIRMED', 'FAMILY'),
(2, 102, 1, 3, 60.00, 'email2@example.com', '2024-03-13', '1234567891', '10:00:00', 'CONFIRMED', 'SOLO'),
(3, 103, 1, 2, 40.00, 'email3@example.com', '2024-03-14', '1234567892', '11:00:00', 'CONFIRMED', 'GUIDEDGROUP'),
(4, 104, 1, 4, 80.00, 'email4@example.com', '2024-03-15', '1234567893', '12:00:00', 'CONFIRMED', 'FAMILY'),
(5, 105, 1, 5, 100.00, 'email5@example.com', '2024-03-16', '1234567894', '13:00:00', 'CONFIRMED', 'SOLO'),
(6, 106, 1, 3, 60.00, 'email6@example.com', '2024-03-17', '1234567895', '14:00:00', 'CONFIRMED', 'GUIDEDGROUP'),
(7, 107, 1, 2, 40.00, 'email7@example.com', '2024-03-18', '1234567896', '15:00:00', 'CONFIRMED', 'FAMILY'),
(8, 108, 1, 4, 80.00, 'email8@example.com', '2024-03-19', '1234567897', '16:00:00', 'CONFIRMED', 'SOLO'),
(9, 109, 1, 5, 100.00, 'email9@example.com', '2024-03-20', '1234567898', '09:30:00', 'CONFIRMED', 'GUIDEDGROUP'),
(10, 110, 1, 3, 60.00, 'email10@example.com', '2024-03-21', '1234567899', '10:30:00', 'CONFIRMED', 'FAMILY'),
(11, 111, 1, 2, 40.00, 'email11@example.com', '2024-03-22', '1234567800', '11:30:00', 'CONFIRMED', 'SOLO'),
(12, 112, 1, 4, 80.00, 'email12@example.com', '2024-03-23', '1234567801', '12:30:00', 'CONFIRMED', 'GUIDEDGROUP'),
(13, 113, 1, 5, 100.00, 'email13@example.com', '2024-03-24', '1234567802', '13:30:00', 'CONFIRMED', 'FAMILY'),
(14, 114, 1, 3, 60.00, 'email14@example.com', '2024-03-25', '1234567803', '14:30:00', 'CONFIRMED', 'SOLO'),
(15, 115, 1, 2, 40.00, 'email15@example.com', '2024-03-26', '1234567804', '15:30:00', 'CONFIRMED', 'GUIDEDGROUP'),
(16, 116, 1, 4, 80.00, 'email16@example.com', '2024-03-27', '1234567805', '16:30:00', 'CONFIRMED', 'FAMILY'),
(17, 117, 1, 5, 100.00, 'email17@example.com', '2024-03-28', '1234567806', '09:45:00', 'CONFIRMED', 'SOLO'),
(18, 118, 1, 3, 60.00, 'email18@example.com', '2024-03-29', '1234567807', '10:45:00', 'CONFIRMED', 'GUIDEDGROUP'),
(19, 119, 1, 2, 40.00, 'email19@example.com', '2024-03-30', '1234567808', '11:45:00', 'CONFIRMED', 'FAMILY'),
(20, 120, 1, 4, 80.00, 'email20@example.com', '2024-03-31', '1234567809', '12:45:00', 'CONFIRMED', 'SOLO');
-- Drop the existing tables if they exist
DROP TABLE IF EXISTS VisitorsReport;
DROP TABLE IF EXISTS Report;

-- Create the Report table
CREATE TABLE Report (
    reportID INT AUTO_INCREMENT PRIMARY KEY,
    reportType VARCHAR(255) NOT NULL,
    parkID INT NOT NULL,
    date DATE NOT NULL,
    comment TEXT
);

-- Create the VisitorsReport table with a foreign key reference to Report
CREATE TABLE VisitorsReport (
    reportID INT,
    parkNumber INT NOT NULL,
    totalIndividualVisitors INT NOT NULL,
    totalGroupVisitors INT NOT NULL,
    totalFamilyVisitors INT NOT NULL,
    totalVisitors INT NOT NULL,
    PRIMARY KEY (reportID),
    FOREIGN KEY (reportID) REFERENCES Report(reportID)
);


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
