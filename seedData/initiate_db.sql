-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
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
-- Table structure for table `changerequests`
--
DROP TABLE IF EXISTS ChangeRequests;

CREATE TABLE ChangeRequests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    parkName VARCHAR(255) NOT NULL,
    parkNumber INT NOT NULL,
    capacity INT NOT NULL,
    gap INT NOT NULL,
    staytime INT NOT NULL,
    approved ENUM('REJECTED', 'APPROVAL', 'WAITING_FOR_APPROVAL') NOT NULL
);



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
INSERT INTO `generalparkworker` VALUES (1,'John','Doe','johndoe@example.com','Park Manager','johndoe123','password123',1,0),(2,'Jane','Smith','janesmith@example.com','Park Manager','janesmith456','password456',2,0),(3,'Michael','Johnson','michaeljohnson@example.com','Park Manager','michaelj','password789',3,0),(4,'Emily ','Williams','emilywilliams@example.com','Worker','emilyw','password123',1,0),(5,'Chavi','Alonso','ChaviAlonso@e.braude.ac.il','Worker','userchavi','123456a',2,0),(6,'Yossi','Hanoder','Yossihanoder@e.braude.ac.il','Worker','yossinoder','123456a',3,0),(7,'Emanuel','Braude','emanuelbraude@e.braude.ac.il','Department Manager','emanuelb','123456a',1,0),(8,'lil','bullet','lilbullet@e.braude.ac.il','Department Manager','lilbullet','123456a',2,0),(9,'Rosh','Tirosh','roshtirosh@e.braude.ac.il','Department Manager','roshtirosh','123456a',3,0),(10,'El','Salvador','elsalvador@e.braude.ac.il','Service Worker','salvador','123456a',1,0);
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
 `parkName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`orderId`),
  KEY `travlerId` (`travlerId`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`travlerId`) REFERENCES `travler` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--
SET foreign_key_checks = 0;
SET foreign_key_checks = 0;
DROP TABLE IF EXISTS waitinglist;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE waitinglist (
  orderId int NOT NULL,
  travlerId int NOT NULL,
  parkNumber int NOT NULL,
  amountOfVisitors int NOT NULL,
  price float NOT NULL,
  visitorEmail varchar(255) DEFAULT NULL,
  date date NOT NULL,
  TelephoneNumber varchar(15) DEFAULT NULL,
  visitTime time NOT NULL,
  orderStatus varchar(255) DEFAULT NULL,
  typeOfOrder varchar(255) DEFAULT NULL,
  parkName varchar(255) DEFAULT NULL,
  waitingListId int NOT NULL,
  placeInList int NOT NULL,
  KEY waitinglist_ibfk_1 (`travlerId`),
  KEY waitinglist_ibfk_2 (`parkNumber`),
  CONSTRAINT waitinglist_ibfk_1 FOREIGN KEY (`travlerId`) REFERENCES travler (`id`),
  CONSTRAINT waitinglist_ibfk_2 FOREIGN KEY (`parkNumber`) REFERENCES park (`parkNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table waitinglist
--

LOCK TABLES waitinglist WRITE;
/*!40000 ALTER TABLE waitinglist DISABLE KEYS */;
INSERT INTO waitinglist VALUES (1,1214214,1,5,50,'alice@example.com','2024-07-04','054-7376231','09:00:00','PENDING','FAMILY','Mount Rainier',1,1),(2,2654456,2,3,30,'bob@example.com','2024-07-05','052-7355231','14:00:00','CONFIRMED','FAMILY\r','Yellowstone',2,1),(9,1214214,1,1,85,'asd@walla.com','2024-07-04','1231231231','09:00:00','PENDING','SOLO','Mount Rainier',3,1),(10,1214214,1,1,85,'asd@walla.com','2024-07-04','1231231231','09:00:00','PENDING','SOLO','Mount Rainier',4,2),(14,1214214,1,2,170,'asd@walla.com','2024-03-21','1231231231','09:00:00','PENDING','FAMILY','Mount Rainier',5,1);
/*!40000 ALTER TABLE waitinglist ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
SET foreign_key_checks = 1;




LOCK TABLES `order` WRITE;
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


INSERT INTO `order` (orderId, travlerId, parkNumber, amountOfVisitors, price, visitorEmail, date, TelephoneNumber, visitTime, orderStatus, typeOfOrder) VALUES
(21, 121, 1, 3, 60.00, 'email21@example.com', '2024-07-01', '1234567810', '10:00:00', 'COMPLETED', 'FAMILY'),
(22, 122, 1, 4, 80.00, 'email22@example.com', '2024-07-02', '1234567811', '11:00:00', 'COMPLETED', 'SOLO'),
(23, 123, 1, 2, 40.00, 'email23@example.com', '2024-07-03', '1234567812', '12:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(24, 124, 1, 5, 100.00, 'email24@example.com', '2024-07-04', '1234567813', '13:00:00', 'COMPLETED', 'FAMILY'),
(25, 125, 1, 3, 60.00, 'email25@example.com', '2024-07-05', '1234567814', '14:00:00', 'COMPLETED', 'SOLO'),
(26, 126, 1, 4, 80.00, 'email26@example.com', '2024-07-06', '1234567815', '09:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(27, 127, 1, 2, 40.00, 'email27@example.com', '2024-07-07', '1234567816', '10:30:00', 'COMPLETED', 'FAMILY'),
(28, 128, 1, 5, 100.00, 'email28@example.com', '2024-07-08', '1234567817', '11:30:00', 'COMPLETED', 'SOLO'),
(29, 129, 1, 3, 60.00, 'email29@example.com', '2024-07-09', '1234567818', '12:30:00', 'COMPLETED', 'GUIDEDGROUP'),
(30, 130, 1, 4, 80.00, 'email30@example.com', '2024-07-10', '1234567819', '13:30:00', 'COMPLETED', 'FAMILY'),
(31, 131, 1, 2, 40.00, 'email31@example.com', '2024-07-11', '1234567820', '14:30:00', 'COMPLETED', 'SOLO'),
(32, 132, 1, 5, 100.00, 'email32@example.com', '2024-07-12', '1234567821', '15:30:00', 'COMPLETED', 'GUIDEDGROUP'),
(33, 133, 1, 3, 60.00, 'email33@example.com', '2024-07-13', '1234567822', '16:30:00', 'COMPLETED', 'FAMILY'),
(34, 134, 1, 4, 80.00, 'email34@example.com', '2024-07-14', '1234567823', '09:45:00', 'COMPLETED', 'SOLO'),
(35, 135, 1, 2, 40.00, 'email35@example.com', '2024-07-15', '1234567824', '10:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(36, 136, 1, 5, 100.00, 'email36@example.com', '2024-07-16', '1234567825', '11:45:00', 'COMPLETED', 'FAMILY'),
(37, 137, 1, 3, 60.00, 'email37@example.com', '2024-07-17', '1234567826', '12:45:00', 'COMPLETED', 'SOLO'),
(38, 138, 1, 4, 80.00, 'email38@example.com', '2024-07-18', '1234567827', '13:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(39, 139, 1, 2, 40.00, 'email39@example.com', '2024-07-19', '1234567828', '14:45:00', 'COMPLETED', 'FAMILY'),
(40, 140, 1, 5, 100.00, 'email40@example.com', '2024-07-20', '1234567829', '15:45:00', 'COMPLETED', 'SOLO'),
(41, 141, 1, 3, 60.00, 'email41@example.com', '2024-07-21', '1234567830', '16:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(42, 142, 1, 4, 80.00, 'email42@example.com', '2024-07-22', '1234567831', '09:00:00', 'COMPLETED', 'FAMILY'),
(43, 143, 1, 2, 40.00, 'email43@example.com', '2024-07-23', '1234567832', '10:00:00', 'COMPLETED', 'SOLO'),
(44, 144, 1, 5, 100.00, 'email44@example.com', '2024-07-24', '1234567833', '11:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(45, 145, 1, 3, 60.00, 'email45@example.com', '2024-07-25', '1234567834', '12:00:00', 'COMPLETED', 'FAMILY'),
(46, 146, 1, 4, 80.00, 'email46@example.com', '2024-07-26', '1234567835', '13:00:00', 'COMPLETED', 'SOLO'),
(47, 147, 1, 2, 40.00, 'email47@example.com', '2024-07-27', '1234567836', '14:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(48, 148, 1, 5, 100.00, 'email48@example.com', '2024-07-28', '1234567837', '15:00:00', 'COMPLETED', 'FAMILY'),
(49, 149, 1, 3, 60.00, 'email49@example.com', '2024-07-29', '1234567838', '16:00:00', 'COMPLETED', 'SOLO'),
(50, 150, 1, 4, 80.00, 'email50@example.com', '2024-07-30', '1234567839', '09:30:00', 'COMPLETED', 'GUIDEDGROUP');
(51, 151, 2, 2, 40.00, 'email51@example.com', '2024-01-10', '1234567901', '09:00:00', 'COMPLETED', 'FAMILY'),
(52, 152, 2, 3, 60.00, 'email52@example.com', '2024-02-15', '1234567902', '10:00:00', 'COMPLETED', 'SOLO'),
(53, 153, 2, 4, 80.00, 'email53@example.com', '2024-03-20', '1234567903', '11:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(54, 154, 3, 1, 20.00, 'email54@example.com', '2024-04-25', '1234567904', '12:00:00', 'COMPLETED', 'FAMILY'),
(55, 155, 3, 2, 40.00, 'email55@example.com', '2024-05-30', '1234567905', '13:00:00', 'COMPLETED', 'SOLO'),
(56, 156, 3, 3, 60.00, 'email56@example.com', '2024-06-04', '1234567906', '14:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(57, 157, 4, 4, 80.00, 'email57@example.com', '2024-07-09', '1234567907', '15:00:00', 'COMPLETED', 'FAMILY'),
(58, 158, 4, 5, 100.00, 'email58@example.com', '2024-08-14', '1234567908', '16:00:00', 'COMPLETED', 'SOLO'),
(59, 159, 4, 2, 40.00, 'email59@example.com', '2024-09-19', '1234567909', '17:00:00', 'COMPLETED', 'GUIDEDGROUP'),
(60, 160, 4, 3, 60.00, 'email60@example.com', '2024-10-24', '1234567910', '18:00:00', 'COMPLETED', 'FAMILY'),
(61, 161, 1, 1, 20.00, 'email61@example.com', '2024-11-29', '1234567911', '08:00:00', 'COMPLETED', 'SOLO'),
(62, 162, 1, 4, 80.00, 'email62@example.com', '2024-12-04', '1234567912', '09:30:00', 'COMPLETED', 'GUIDEDGROUP'),
(63, 163, 2, 5, 100.00, 'email63@example.com', '2024-02-07', '1234567913', '10:30:00', 'COMPLETED', 'FAMILY'),
(64, 164, 2, 2, 40.00, 'email64@example.com', '2024-03-14', '1234567914', '11:30:00', 'COMPLETED', 'SOLO'),
(65, 165, 3, 3, 60.00, 'email65@example.com', '2024-04-21', '1234567915', '12:30:00', 'COMPLETED', 'GUIDEDGROUP'),
(66, 166, 3, 4, 80.00, 'email66@example.com', '2024-05-28', '1234567916', '13:30:00', 'COMPLETED', 'FAMILY'),
(67, 167, 4, 1, 20.00, 'email67@example.com', '2024-07-05', '1234567917', '14:30:00', 'COMPLETED', 'SOLO'),
(68, 168, 4, 2, 40.00, 'email68@example.com', '2024-08-12', '1234567918', '15:30:00', 'COMPLETED', 'GUIDEDGROUP'),
(69, 169, 1, 3, 60.00, 'email69@example.com', '2024-09-19', '1234567919', '16:30:00', 'COMPLETED', 'FAMILY'),
(70, 170, 1, 4, 80.00, 'email70@example.com', '2024-10-27', '1234567920', '17:30:00', 'COMPLETED', 'SOLO'),
(71, 171, 2, 5, 100.00, 'email71@example.com', '2024-12-05', '1234567921', '18:30:00', 'COMPLETED', 'GUIDEDGROUP'),
(72, 172, 2, 1, 20.00, 'email72@example.com', '2024-01-12', '1234567922', '08:30:00', 'COMPLETED', 'FAMILY'),
(73, 173, 3, 2, 40.00, 'email73@example.com', '2024-02-18', '1234567923', '09:45:00', 'COMPLETED', 'SOLO'),
(74, 174, 3, 3, 60.00, 'email74@example.com', '2024-03-26', '1234567924', '10:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(75, 175, 4, 4, 80.00, 'email75@example.com', '2024-05-03', '1234567925', '11:45:00', 'COMPLETED', 'FAMILY'),
(76, 176, 4, 1, 20.00, 'email76@example.com', '2024-06-10', '1234567926', '12:45:00', 'COMPLETED', 'SOLO'),
(77, 177, 1, 2, 40.00, 'email77@example.com', '2024-07-17', '1234567927', '13:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(78, 178, 1, 3, 60.00, 'email78@example.com', '2024-08-23', '1234567928', '14:45:00', 'COMPLETED', 'FAMILY'),
(79, 179, 2, 4, 80.00, 'email79@example.com', '2024-09-29', '1234567929', '15:45:00', 'COMPLETED', 'SOLO'),
(80, 180, 2, 5, 100.00, 'email80@example.com', '2024-11-04', '1234567930', '16:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(81, 181, 3, 1, 20.00, 'email81@example.com', '2024-12-10', '1234567931', '17:45:00', 'COMPLETED', 'FAMILY'),
(82, 182, 3, 2, 40.00, 'email82@example.com', '2024-01-15', '1234567932', '18:45:00', 'COMPLETED', 'SOLO'),
(83, 183, 4, 3, 60.00, 'email83@example.com', '2024-02-20', '1234567933', '08:45:00', 'COMPLETED', 'GUIDEDGROUP'),
(84, 184, 4, 4, 80.00, 'email84@example.com', '2024-03-27', '1234567934', '09:55:00', 'COMPLETED', 'FAMILY'),
(85, 185, 1, 1, 20.00, 'email85@example.com', '2024-05-01', '1234567935', '10:55:00', 'COMPLETED', 'SOLO'),
(86, 186, 1, 2, 40.00, 'email86@example.com', '2024-06-07', '1234567936', '11:55:00', 'COMPLETED', 'GUIDEDGROUP'),
(87, 187, 2, 3, 60.00, 'email87@example.com', '2024-07-13', '1234567937', '12:55:00', 'COMPLETED', 'FAMILY'),
(88, 188, 2, 4, 80.00, 'email88@example.com', '2024-08-18', '1234567938', '13:55:00', 'COMPLETED', 'SOLO'),
(89, 189, 3, 5, 100.00, 'email89@example.com', '2024-09-23', '1234567939', '14:55:00', 'COMPLETED', 'GUIDEDGROUP'),
(90, 190, 3, 1, 20.00, 'email90@example.com', '2024-10-28', '1234567940', '15:55:00', 'COMPLETED', 'FAMILY');
SET foreign_key_checks = 1;

UNLOCK TABLES;

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

CREATE TABLE `UsageReport` (
  `reportID` INT NOT NULL,
  `parkNumber` INT NOT NULL,
  `parkCapacity` INT NOT NULL,
  `dailyUsage` TEXT NOT NULL,
  PRIMARY KEY (`reportID`),
  CONSTRAINT `fk_usageReport_report` FOREIGN KEY (`reportID`) REFERENCES `report` (`reportID`) ON DELETE CASCADE
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
  `gap` int DEFAULT NULL,
  `unorderedvisits` int DEFAULT 0, -- Assuming default is 0, indicating no unordered visits initially
  PRIMARY KEY (`parkNumber`),
  KEY `managerId` (`managerId`),
  CONSTRAINT `park_ibfk_1` FOREIGN KEY (`managerId`) REFERENCES `generalparkworker` (`workerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `park`
--

LOCK TABLES `park` WRITE;
/*!40000 ALTER TABLE `park` DISABLE KEYS */;
INSERT INTO `park` VALUES ('Mount Rainier',1,20,30,1,'Washington',4,2,1,8,0),('Yellowstone',2,57,60,1,'Utah',5,1,2,7,0),('Yosemite',3,44,50,1,'Wyoming',6,1,3,8,0);
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
  `orderId` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`orderId`),
  KEY `parkNumber` (`parkNumber`),
  CONSTRAINT `fk_visit_orderId` FOREIGN KEY (`orderId`) REFERENCES `order` (`orderId`),
  CONSTRAINT `visit_ibfk_1` FOREIGN KEY (`parkNumber`) REFERENCES `park` (`parkNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit`
--

LOCK TABLES `visit` WRITE;
/*!40000 ALTER TABLE `visit` DISABLE KEYS */;
INSERT INTO `visit` VALUES ('2024-07-04','09:00:00','12:00:00',1,1),('2024-07-05','14:00:00','17:00:00',2,2),('2024-07-06','08:00:00','11:00:00',1,3),('2024-07-07','10:30:00','13:30:00',3,4);
/*!40000 ALTER TABLE `visit` ENABLE KEYS */;
UNLOCK TABLES;
INSERT INTO `visit` (orderId, date, visitTime, parkNumber, amountOfVisitors)
VALUES
INSERT INTO `visit` (visitId, orderNumber, visitDate, enteringTime, exitingTime, parkNumber) VALUES


INSERT INTO `visit` (visitId, orderNumber, visitDate, enteringTime, exitingTime, parkNumber) VALUES
(41, 50, '2024-07-17', '08:30:00', '10:30:00', 4),
(42, 51, '2024-08-12', '09:00:00', '11:00:00', 1),
(43, 52, '2024-09-19', '09:30:00', '11:30:00', 2),
(44, 53, '2024-10-24', '10:00:00', '12:00:00', 3),
(45, 54, '2024-11-29', '10:30:00', '12:30:00', 4),
(46, 55, '2024-12-04', '11:00:00', '13:00:00', 1),
(47, 56, '2024-01-10', '11:30:00', '13:30:00', 2),
(48, 57, '2024-02-15', '12:00:00', '14:00:00', 3),
(49, 58, '2024-03-20', '12:30:00', '14:30:00', 4),
(50, 59, '2024-04-25', '13:00:00', '15:00:00', 1),
(51, 60, '2024-05-30', '13:30:00', '15:30:00', 2),
(52, 61, '2024-06-04', '14:00:00', '16:00:00', 3),
(53, 62, '2024-07-09', '14:30:00', '16:30:00', 4),
(54, 63, '2024-08-14', '15:00:00', '17:00:00', 1),
(55, 64, '2024-09-19', '15:30:00', '17:30:00', 2),
(56, 65, '2024-10-24', '16:00:00', '18:00:00', 3),
(57, 66, '2024-11-29', '16:30:00', '18:30:00', 4),
(58, 67, '2024-12-04', '17:00:00', '19:00:00', 1),
(59, 68, '2024-01-12', '08:30:00', '10:30:00', 2),
(60, 69, '2024-02-18', '09:00:00', '11:00:00', 3),
(61, 70, '2024-03-26', '09:30:00', '11:30:00', 4),
(62, 71, '2024-05-03', '10:00:00', '12:00:00', 1),
(63, 72, '2024-06-10', '10:30:00', '12:30:00', 2),
(64, 73, '2024-07-17', '11:00:00', '13:00:00', 3),
(65, 74, '2024-08-14', '11:30:00', '13:30:00', 4),
(66, 75, '2024-09-19', '12:00:00', '14:00:00', 1),
(67, 76, '2024-10-27', '12:30:00', '14:30:00', 2),
(68, 77, '2024-12-05', '13:00:00', '15:00:00', 3),
(69, 78, '2024-01-12', '13:30:00', '15:30:00', 4),
(70, 79, '2024-02-18', '14:00:00', '16:00:00', 1),
(71, 80, '2024-03-26', '14:30:00', '16:30:00', 2),
(72, 81, '2024-05-03', '15:00:00', '17:00:00', 3),
(73, 82, '2024-06-10', '15:30:00', '17:30:00', 4),
(74, 83, '2024-07-17', '16:00:00', '18:00:00', 1),
(75, 84, '2024-08-14', '16:30:00', '18:30:00', 2),
(76, 85, '2024-09-19', '17:00:00', '19:00:00', 3),
(77, 86, '2024-10-27', '17:30:00', '19:30:00', 4),
(78, 87, '2024-12-05', '18:00:00', '20:00:00', 1),
(79, 88, '2024-01-12', '08:30:00', '10:30:00', 2),
(80, 89, '2024-02-18', '09:00:00', '11:00:00', 3),
(81, 90, '2024-03-26', '09:30:00', '11:30:00', 4);



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


-- Dump completed on 2024-03-16  0:18:58
