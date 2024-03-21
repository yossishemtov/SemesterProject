-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: project
-- ------------------------------------------------------
-- Server version	8.3.0
CREATE DATABASE  IF NOT EXISTS `project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `project`;

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
INSERT INTO `generalparkworker` (`workerId`, `firstName`, `lastName`, `email`, `role`, `userName`, `password`, `worksAtPark`, `isloggedin`) VALUES
(1, 'John', 'Doe', 'johndoe@example.com', 'Department Manager', 'joss', '123a', 1, 0),
(2, 'Jane', 'Smith', 'janesmith@example.com', 'Park Manager', 'yos', '123a', 2, 0),
(3, 'Alex', 'Johnson', 'alexjohnson@example.com', 'Park Worker', 'itay', '123a', 3, 0),
(4, 'Emily', 'Williams', 'emilywilliams@example.com', 'Service Worker', 'shai', '123a', 1, 0),
(5, 'Chris', 'Brown', 'chrisbrown@example.com', 'Department Manager', 'moti', '123a', 2, 0),
(6, 'Pat', 'Green', 'patgreen@example.com', 'Park Manager', 'emanuel', '123a', 3, 0),
(7, 'Taylor', 'Adams', 'tayloradams@example.com', 'Park Worker', 'daniel', '123a', 1, 0),
(8, 'Jordan', 'Miller', 'jordanmiller@example.com', 'Service Worker', 'moshe', '123a', 2, 0),
(9, 'Casey', 'Wilson', 'caseywilson@example.com', 'Department Manager', 'alon', '123a', 3, 0),
(10, 'Morgan', 'Moore', 'morganmoore@example.com', 'Park Manager', 'mor', '123a', 1, 0),
(11, 'Jamie', 'Taylor', 'jamietaylor@example.com', 'Park Worker', 'taylor', '123a', 2, 0),
(12, 'Drew', 'Anderson', 'drewanderson@example.com', 'Service Worker', 'david', '123a', 3, 0);
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




LOCK TABLES `order` WRITE;SET foreign_key_checks = 0;
 INSERT INTO `order` (`orderId`, `travelerId`, `parkNumber`, `amountOfVisitors`, `price`, `visitorEmail`, `date`, `TelephoneNumber`, `visitTime`, `orderStatus`, `typeOfOrder`, `parkName`) VALUES
(1, 8521529, 1, 5, 150.0, 'traveler2@example.com', '2024-02-11', '555-1179', '15:50:00', 'CONFIRMED', 'GUIDEDGROUP', 'Mount Rainier'),
(2, 8521529, 1, 5, 150.0, 'traveler2@example.com', '2024-02-11', '555-1179', '15:50:00', 'CONFIRMED', 'GUIDEDGROUP', 'Mount Rainier'),
(3, 5561795, 3, 1, 30.0, 'traveler3@example.com', '2024-03-06', '555-9811', '13:07:00', 'NOTARRIVED', 'SOLO', 'Yosemite'),

 (4, 1459530, 1, 1, 30.0, 'traveler4@example.com', '2024-03-27', '555-3733', '13:44:00', 'CONFIRMED', 'SOLO', 'Mount Rainier'),
 (5, 9453513, 2, 1, 30.0, 'traveler5@example.com', '2024-01-04', '555-3331', '16:34:00', 'CONFIRMED', 'SOLO', 'Yellow Stone'),
 (6, 4400742, 1, 1, 30.0, 'traveler6@example.com', '2024-03-16', '555-7637', '10:46:00', 'CONFIRMED', 'SOLO', 'Mount Rainier'),
 (7, 8786577, 1, 1, 30.0, 'traveler7@example.com', '2024-02-27', '555-4775', '17:24:00', 'NOTARRIVED', 'SOLO', 'Mount Rainier'),
 (8, 7255123, 1, 2, 60.0, 'traveler8@example.com', '2024-02-12', '555-8715', '16:12:00', 'PENDING', 'GUIDEDGROUP', 'Mount Rainier'),
 (9, 7282710, 1, 2, 60.0, 'traveler9@example.com', '2024-03-05', '555-4831', '10:49:00', 'CONFIRMED', 'GUIDEDGROUP', 'Mount Rainier'),
 (10, 9820734, 1, 2, 60.0, 'traveler10@example.com', '2024-02-18', '555-2174', '13:26:00', 'PENDING', 'FAMILY', 'Mount Rainier'),
 (11, 4798188, 3, 1, 30.0, 'traveler11@example.com', '2024-01-11', '555-6433', '14:14:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yosemite'),
 (12, 6460540, 1, 4, 120.0, 'traveler12@example.com', '2024-02-20', '555-6800', '15:46:00', 'NOTARRIVED', 'FAMILY', 'Mount Rainier'),
 (13, 8144519, 1, 1, 30.0, 'traveler13@example.com', '2024-03-13', '555-5732', '18:56:00', 'PENDING', 'SOLO', 'Mount Rainier'),
 (14, 1640012, 3, 1, 30.0, 'traveler14@example.com', '2024-01-20', '555-5401', '10:33:00', 'PENDING', 'SOLO', 'Yosemite'),
 (15, 9530968, 3, 5, 150.0, 'traveler15@example.com', '2024-03-23', '555-9909', '14:44:00', 'PENDING', 'GUIDEDGROUP', 'Yosemite'),
 (16, 4213045, 2, 1, 30.0, 'traveler16@example.com', '2024-01-20', '555-9357', '14:14:00', 'PENDING', 'SOLO', 'Yellow Stone'),
 (17, 1917736, 1, 1, 30.0, 'traveler17@example.com', '2024-02-05', '555-2685', '14:34:00', 'CANCELED', 'SOLO', 'Mount Rainier'),
 (18, 7767529, 1, 4, 120.0, 'traveler18@example.com', '2024-02-02', '555-2023', '09:35:00', 'CONFIRMED', 'FAMILY', 'Mount Rainier'),
 (19, 1746104, 1, 5, 150.0, 'traveler19@example.com', '2024-03-08', '555-8263', '18:20:00', 'NOTARRIVED', 'FAMILY', 'Mount Rainier'),
 (20, 6548853, 3, 3, 90.0, 'traveler20@example.com', '2024-02-18', '555-8634', '10:05:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yosemite'),
 (21, 8417637, 1, 1, 30.0, 'traveler21@example.com', '2024-02-14', '555-2722', '12:14:00', 'PENDING', 'FAMILY', 'Mount Rainier'),
 (22, 7487414, 1, 1, 30.0, 'traveler22@example.com', '2024-02-06', '555-1441', '14:03:00', 'CONFIRMED', 'SOLO', 'Mount Rainier'),
 (23, 7745584, 3, 1, 30.0, 'traveler23@example.com', '2024-01-04', '555-1240', '15:25:00', 'NOTARRIVED', 'SOLO', 'Yosemite'),
 (24, 7589809, 1, 1, 30.0, 'traveler24@example.com', '2024-01-31', '555-7709', '09:46:00', 'CANCELED', 'GUIDEDGROUP', 'Mount Rainier'),
 (25, 3005829, 2, 1, 30.0, 'traveler25@example.com', '2024-03-23', '555-3234', '14:57:00', 'CONFIRMED', 'SOLO', 'Yellow Stone'),
 (26, 2879468, 3, 1, 30.0, 'traveler26@example.com', '2024-02-14', '555-7428', '10:37:00', 'CANCELED', 'SOLO', 'Yosemite'),
 (27, 4922396, 2, 5, 150.0, 'traveler27@example.com', '2024-03-23', '555-6171', '14:42:00', 'NOTARRIVED', 'FAMILY', 'Yellow Stone'),
 (28, 1668064, 2, 1, 30.0, 'traveler28@example.com', '2024-03-11', '555-4234', '11:52:00', 'CONFIRMED', 'SOLO', 'Yellow Stone'),
 (29, 8730038, 3, 5, 150.0, 'traveler29@example.com', '2024-02-07', '555-3474', '18:54:00', 'CONFIRMED', 'FAMILY', 'Yosemite'),
 (30, 9070994, 3, 5, 150.0, 'traveler30@example.com', '2024-03-19', '555-1794', '16:06:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yosemite'),
 (31, 3534268, 2, 1, 30.0, 'traveler31@example.com', '2024-01-24', '555-5694', '11:07:00', 'CONFIRMED', 'GUIDEDGROUP', 'Yellow Stone'),
 (32, 4850083, 2, 4, 120.0, 'traveler32@example.com', '2024-02-16', '555-6248', '10:08:00', 'CANCELED', 'FAMILY', 'Yellow Stone'),
 (33, 7630238, 2, 3, 90.0, 'traveler33@example.com', '2024-02-26', '555-8171', '16:19:00', 'CONFIRMED', 'FAMILY', 'Yellow Stone'),
 (34, 1303740, 3, 2, 60.0, 'traveler34@example.com', '2024-01-25', '555-3473', '15:02:00', 'CONFIRMED', 'GUIDEDGROUP', 'Yosemite'),
 (35, 3575435, 3, 4, 120.0, 'traveler35@example.com', '2024-02-11', '555-1167', '12:59:00', 'CANCELED', 'FAMILY', 'Yosemite'),
 (36, 5038292, 3, 3, 90.0, 'traveler36@example.com', '2024-03-21', '555-1000', '18:29:00', 'PENDING', 'GUIDEDGROUP', 'Yosemite'),
 (37, 5432820, 1, 2, 60.0, 'traveler37@example.com', '2024-01-12', '555-6482', '15:02:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Mount Rainier'),
 (38, 3925582, 1, 4, 120.0, 'traveler38@example.com', '2024-03-05', '555-2243', '13:37:00', 'CONFIRMED', 'GUIDEDGROUP', 'Mount Rainier'),
 (39, 5980214, 2, 3, 90.0, 'traveler39@example.com', '2024-01-22', '555-3183', '16:14:00', 'NOTARRIVED', 'FAMILY', 'Yellow Stone'),
 (40, 1710135, 2, 5, 150.0, 'traveler40@example.com', '2024-01-02', '555-4143', '16:59:00', 'NOTARRIVED', 'FAMILY', 'Yellow Stone'),
 (41, 4757693, 3, 1, 30.0, 'traveler41@example.com', '2024-03-20', '555-8262', '13:53:00', 'PENDING', 'GUIDEDGROUP', 'Yosemite'),
 (42, 3254852, 1, 4, 120.0, 'traveler42@example.com', '2024-03-12', '555-5749', '13:35:00', 'PENDING', 'GUIDEDGROUP', 'Mount Rainier'),
 (43, 2044526, 2, 1, 30.0, 'traveler43@example.com', '2024-03-26', '555-7888', '16:28:00', 'CONFIRMED', 'SOLO', 'Yellow Stone'),
 (44, 6192056, 1, 2, 60.0, 'traveler44@example.com', '2024-03-26', '555-7112', '16:53:00', 'NOTARRIVED', 'FAMILY', 'Mount Rainier'),
 (45, 3928485, 1, 3, 90.0, 'traveler45@example.com', '2024-03-29', '555-4292', '10:45:00', 'CONFIRMED', 'GUIDEDGROUP', 'Mount Rainier'),
 (46, 2410771, 1, 4, 120.0, 'traveler46@example.com', '2024-03-22', '555-2374', '11:15:00', 'PENDING', 'GUIDEDGROUP', 'Mount Rainier'),
 (47, 2979552, 2, 1, 30.0, 'traveler47@example.com', '2024-02-07', '555-2829', '11:36:00', 'NOTARRIVED', 'SOLO', 'Yellow Stone'),
 (48, 8598682, 3, 3, 90.0, 'traveler48@example.com', '2024-02-20', '555-9515', '14:59:00', 'PENDING', 'FAMILY', 'Yosemite'),
 (49, 5867079, 3, 1, 30.0, 'traveler49@example.com', '2024-01-29', '555-2364', '09:38:00', 'PENDING', 'SOLO', 'Yosemite'),
 (50, 2220055, 1, 3, 90.0, 'traveler50@example.com', '2024-03-06', '555-4206', '17:28:00', 'PENDING', 'FAMILY', 'Mount Rainier'),
 (51, 2734644, 3, 3, 90.0, 'traveler51@example.com', '2024-01-31', '555-3576', '09:18:00', 'CANCELED', 'FAMILY', 'Yosemite'),
 (52, 8219512, 3, 4, 120.0, 'traveler52@example.com', '2024-01-02', '555-7874', '18:52:00', 'CANCELED', 'GUIDEDGROUP', 'Yosemite'),
 (53, 9130785, 3, 1, 30.0, 'traveler53@example.com', '2024-01-02', '555-1255', '12:28:00', 'NOTARRIVED', 'SOLO', 'Yosemite'),
 (54, 8507876, 1, 1, 30.0, 'traveler54@example.com', '2024-01-04', '555-3503', '18:45:00', 'CANCELED', 'SOLO', 'Mount Rainier'),
 (55, 4018632, 2, 1, 30.0, 'traveler55@example.com', '2024-01-11', '555-9643', '13:10:00', 'PENDING', 'SOLO', 'Yellow Stone'),
 (56, 8378739, 2, 2, 60.0, 'traveler56@example.com', '2024-01-22', '555-4380', '11:41:00', 'PENDING', 'GUIDEDGROUP', 'Yellow Stone'),
 (57, 8256824, 2, 2, 60.0, 'traveler57@example.com', '2024-02-09', '555-7029', '09:04:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yellow Stone'),
 (58, 6139703, 3, 4, 120.0, 'traveler58@example.com', '2024-03-08', '555-9259', '12:11:00', 'NOTARRIVED', 'FAMILY', 'Yosemite'),
 (59, 7500347, 2, 3, 90.0, 'traveler59@example.com', '2024-01-08', '555-8987', '12:19:00', 'NOTARRIVED', 'FAMILY', 'Yellow Stone'),
 (60, 7352337, 2, 4, 120.0, 'traveler60@example.com', '2024-01-15', '555-2175', '15:14:00', 'CANCELED', 'GUIDEDGROUP', 'Yellow Stone'),
 (61, 6311558, 2, 4, 120.0, 'traveler61@example.com', '2024-03-16', '555-4311', '14:36:00', 'PENDING', 'FAMILY', 'Yellow Stone'),
 (62, 5506063, 1, 1, 30.0, 'traveler62@example.com', '2024-01-12', '555-6998', '09:00:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Mount Rainier'),
 (63, 6145157, 2, 4, 120.0, 'traveler63@example.com', '2024-01-11', '555-1053', '11:38:00', 'CONFIRMED', 'FAMILY', 'Yellow Stone'),
 (64, 3910236, 3, 5, 150.0, 'traveler64@example.com', '2024-03-29', '555-1959', '10:51:00', 'NOTARRIVED', 'FAMILY', 'Yosemite'),
 (65, 8773692, 1, 4, 120.0, 'traveler65@example.com', '2024-01-21', '555-8029', '17:55:00', 'PENDING', 'GUIDEDGROUP', 'Mount Rainier'),
 (66, 9590670, 2, 4, 120.0, 'traveler66@example.com', '2024-02-13', '555-5646', '09:48:00', 'PENDING', 'GUIDEDGROUP', 'Yellow Stone'),
 (67, 8171150, 1, 1, 30.0, 'traveler67@example.com', '2024-03-08', '555-9965', '13:14:00', 'CANCELED', 'GUIDEDGROUP', 'Mount Rainier'),
 (68, 5874706, 3, 3, 90.0, 'traveler68@example.com', '2024-01-29', '555-4611', '16:13:00', 'CANCELED', 'GUIDEDGROUP', 'Yosemite'),
 (69, 6229344, 3, 2, 60.0, 'traveler69@example.com', '2024-03-08', '555-7827', '10:56:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yosemite'),
 (70, 5156273, 1, 2, 60.0, 'traveler70@example.com', '2024-03-28', '555-7310', '11:29:00', 'CANCELED', 'FAMILY', 'Mount Rainier'),
 (71, 3267230, 3, 1, 30.0, 'traveler71@example.com', '2024-03-30', '555-2175', '12:00:00', 'PENDING', 'FAMILY', 'Yosemite'),
 (72, 4116180, 3, 3, 90.0, 'traveler72@example.com', '2024-01-25', '555-8718', '10:04:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yosemite'),
 (73, 4961625, 1, 1, 30.0, 'traveler73@example.com', '2024-03-01', '555-3247', '18:38:00', 'CANCELED', 'SOLO', 'Mount Rainier'),
 (74, 1348739, 2, 4, 120.0, 'traveler74@example.com', '2024-01-22', '555-9438', '09:00:00', 'PENDING', 'FAMILY', 'Yellow Stone'),
 (75, 6770984, 3, 4, 120.0, 'traveler75@example.com', '2024-03-07', '555-7151', '11:35:00', 'NOTARRIVED', 'FAMILY', 'Yosemite'),
 (76, 9564900, 1, 4, 120.0, 'traveler76@example.com', '2024-03-15', '555-5502', '09:00:00', 'CONFIRMED', 'FAMILY', 'Mount Rainier'),
 (77, 1680237, 3, 1, 30.0, 'traveler77@example.com', '2024-03-23', '555-8597', '12:03:00', 'NOTARRIVED', 'SOLO', 'Yosemite'),
 (78, 4732209, 3, 2, 60.0, 'traveler78@example.com', '2024-01-02', '555-3220', '13:14:00', 'PENDING', 'GUIDEDGROUP', 'Yosemite'),
 (79, 2331806, 1, 1, 30.0, 'traveler79@example.com', '2024-01-12', '555-4278', '17:39:00', 'PENDING', 'SOLO', 'Mount Rainier'),
 (80, 4280838, 1, 3, 90.0, 'traveler80@example.com', '2024-01-07', '555-5621', '12:44:00', 'CANCELED', 'FAMILY', 'Mount Rainier'),
 (81, 3779385, 1, 1, 30.0, 'traveler81@example.com', '2024-01-16', '555-9675', '11:47:00', 'PENDING', 'SOLO', 'Mount Rainier'),
 (82, 9171032, 1, 1, 30.0, 'traveler82@example.com', '2024-01-29', '555-9698', '17:55:00', 'CANCELED', 'SOLO', 'Mount Rainier'),
 (83, 7063637, 1, 5, 150.0, 'traveler83@example.com', '2024-02-10', '555-8526', '10:28:00', 'CONFIRMED', 'GUIDEDGROUP', 'Mount Rainier'),
 (84, 4465294, 2, 1, 30.0, 'traveler84@example.com', '2024-01-02', '555-4672', '12:14:00', 'CONFIRMED', 'SOLO', 'Yellow Stone'),
 (85, 2828936, 3, 1, 30.0, 'traveler85@example.com', '2024-01-01', '555-1406', '10:18:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Yosemite'),
 (86, 6429105, 1, 5, 150.0, 'traveler86@example.com', '2024-01-19', '555-9392', '16:17:00', 'NOTARRIVED', 'FAMILY', 'Mount Rainier'),
 (87, 8031916, 2, 4, 120.0, 'traveler87@example.com', '2024-03-12', '555-2006', '11:04:00', 'NOTARRIVED', 'FAMILY', 'Yellow Stone'),
 (88, 3395760, 2, 1, 30.0, 'traveler88@example.com', '2024-02-21', '555-3411', '14:02:00', 'CONFIRMED', 'FAMILY', 'Yellow Stone'),
 (89, 4832064, 2, 1, 30.0, 'traveler89@example.com', '2024-03-04', '555-4951', '15:35:00', 'CONFIRMED', 'SOLO', 'Yellow Stone'),
 (90, 7339652, 1, 1, 30.0, 'traveler90@example.com', '2024-03-11', '555-5381', '14:24:00', 'PENDING', 'SOLO', 'Mount Rainier'),
 (91, 5121746, 2, 1, 30.0, 'traveler91@example.com', '2024-02-17', '555-4103', '13:18:00', 'CONFIRMED', 'FAMILY', 'Yellow Stone'),
 (92, 7807897, 2, 5, 150.0, 'traveler92@example.com', '2024-03-21', '555-8372', '10:58:00', 'PENDING', 'FAMILY', 'Yellow Stone'),
 (93, 7345042, 1, 2, 60.0, 'traveler93@example.com', '2024-01-25', '555-4031', '16:25:00', 'CANCELED', 'GUIDEDGROUP', 'Mount Rainier'),
 (94, 7394710, 2, 1, 30.0, 'traveler94@example.com', '2024-02-20', '555-4018', '18:38:00', 'PENDING', 'SOLO', 'Yellow Stone'),
 (95, 5232357, 1, 2, 60.0, 'traveler95@example.com', '2024-01-31', '555-5265', '15:56:00', 'PENDING', 'GUIDEDGROUP', 'Mount Rainier'),
 (96, 8170373, 3, 1, 30.0, 'traveler96@example.com', '2024-01-27', '555-8149', '09:44:00', 'CONFIRMED', 'SOLO', 'Yosemite'),
 (97, 6993154, 2, 5, 150.0, 'traveler97@example.com', '2024-01-27', '555-2995', '16:15:00', 'NOTARRIVED', 'FAMILY', 'Yellow Stone'),
 (98, 6739314, 3, 5, 150.0, 'traveler98@example.com', '2024-01-25', '555-7144', '16:46:00', 'PENDING', 'FAMILY', 'Yosemite'),
 (99, 2421875, 1, 3, 90.0, 'traveler99@example.com', '2024-03-27', '555-8754', '16:15:00', 'NOTARRIVED', 'GUIDEDGROUP', 'Mount Rainier'),
 (100, 3885380, 2, 4, 120.0, 'traveler100@example.com', '2024-01-28', '555-7175', '17:18:00', 'PENDING', 'GUIDEDGROUP', 'Yellow Stone');
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-03' WHERE `orderId` = 2;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-04' WHERE `orderId` = 3;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-05' WHERE `orderId` = 4;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-06' WHERE `orderId` = 5;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-08' WHERE `orderId` = 7;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-09' WHERE `orderId` = 8;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-11' WHERE `orderId` = 10;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-13' WHERE `orderId` = 12;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-14' WHERE `orderId` = 13;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-16' WHERE `orderId` = 15;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-17' WHERE `orderId` = 16;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-19' WHERE `orderId` = 18;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-21' WHERE `orderId` = 20;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-24' WHERE `orderId` = 23;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-26' WHERE `orderId` = 25;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-28' WHERE `orderId` = 27;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-29' WHERE `orderId` = 28;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-01-30' WHERE `orderId` = 29;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-01' WHERE `orderId` = 31;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-02' WHERE `orderId` = 32;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-03' WHERE `orderId` = 33;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-05' WHERE `orderId` = 35;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-06' WHERE `orderId` = 36;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-07' WHERE `orderId` = 37;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-10' WHERE `orderId` = 40;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-11' WHERE `orderId` = 41;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-14' WHERE `orderId` = 44;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-15' WHERE `orderId` = 45;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-17' WHERE `orderId` = 47;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-19' WHERE `orderId` = 49;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-20' WHERE `orderId` = 50;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-25' WHERE `orderId` = 55;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-26' WHERE `orderId` = 56;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-27' WHERE `orderId` = 57;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-02-28' WHERE `orderId` = 58;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-02' WHERE `orderId` = 61;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-03' WHERE `orderId` = 62;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-05' WHERE `orderId` = 64;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-07' WHERE `orderId` = 66;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-12' WHERE `orderId` = 71;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-14' WHERE `orderId` = 73;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-16' WHERE `orderId` = 75;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-17' WHERE `orderId` = 76;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-18' WHERE `orderId` = 77;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-19' WHERE `orderId` = 78;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-22' WHERE `orderId` = 81;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-23' WHERE `orderId` = 82;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-25' WHERE `orderId` = 84;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-03-29' WHERE `orderId` = 88;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-04-01' WHERE `orderId` = 91;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-04-02' WHERE `orderId` = 92;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-04-04' WHERE `orderId` = 94;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-04-05' WHERE `orderId` = 95;
UPDATE `order` SET `orderStatus` = 'COMPLETED', `date` = '2024-04-06' WHERE `orderId` = 96;


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
