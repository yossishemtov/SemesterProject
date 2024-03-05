CREATE TABLE `Order` (
  `orderId` INT NOT NULL,
  `parkNumber` INT NOT NULL,
  `amountOfVisitors` INT NOT NULL,
  `price` FLOAT NOT NULL,
  `visitorEmail` VARCHAR(255),
  `date` DATE NOT NULL,
  `TelephoneNumber` VARCHAR(15),
  `visitTime` TIME NOT NULL,
  `orderStatus` VARCHAR(255),
  PRIMARY KEY (`orderId`)
);


CREATE TABLE `Travler` (
    `id` INT PRIMARY KEY,
    `firstName` VARCHAR(255),
    `lastName` VARCHAR(255),
    `email` VARCHAR(255)
);

CREATE TABLE `GeneralParkWorker` (
    `workerId` INT PRIMARY KEY,
    `firstName` VARCHAR(255),
    `lastName` VARCHAR(255),
    `email` VARCHAR(255),
    `role` VARCHAR(255),
    `userName` VARCHAR(255),
    `password` VARCHAR(255)
);

CREATE TABLE `Park` (
    name VARCHAR(255),
    `parkNumber` INT PRIMARY KEY,
    `maxVisitors` INT,
    `capacity` INT,
    `currentVisitors` INT,
    `location` VARCHAR(255),
    `staytime` INT,
    `workersAmount` INT,
    `managerId` INT,
    `workingTime` INT,
    FOREIGN KEY (managerId) REFERENCES GeneralParkWorker(workerId)
);

CREATE TABLE `GroupGuide` (
    `id` INT PRIMARY KEY,
    `username` VARCHAR(255),
    `password` VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Travler(id)
);

CREATE TABLE `Visit` (
    `visitDate` DATE,
    `enteringTime` TIME,
    `existingTime` TIME,
    `parkName` VARCHAR(255),
    FOREIGN KEY (parkName) REFERENCES Park(name)
);

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.3/Uploads/dataorder.txt'
INTO TABLE project.order;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.3/Uploads/parkdata.txt'
INTO TABLE project.Park;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.3/Uploads/generalparkworkerdata.txt'
INTO TABLE project.generalparkworker;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.3/Uploads/travlerdata.txt'
INTO TABLE project.travler;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.3/Uploads/groupguidedata.txt'
INTO TABLE project.groupguide;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.3/Uploads/visitdata.txt'
INTO TABLE project.visit;