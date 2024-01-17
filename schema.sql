-- MySQL dump 10.13  Distrib 8.0.31, for macos12.6 (arm64)
--
-- Host: 68.237.123.87    Database: classData
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chad_encoded_data`
--

DROP TABLE IF EXISTS `chad_encoded_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chad_encoded_data` (
  `Q28` int DEFAULT NULL,
  `Q29b` int DEFAULT NULL,
  `S6` int DEFAULT NULL,
  `Q8` int DEFAULT NULL,
  `Q9` int DEFAULT NULL,
  `Q12` int DEFAULT NULL,
  `Q16` int DEFAULT NULL,
  `Q17` int DEFAULT NULL,
  `Q18` int DEFAULT NULL,
  `Q19` int DEFAULT NULL,
  `Q24` int DEFAULT NULL,
  `Q45` int DEFAULT NULL,
  `Q46` int DEFAULT NULL,
  `Q47` int DEFAULT NULL,
  `Q48` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chad_raw`
--

DROP TABLE IF EXISTS `chad_raw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chad_raw` (
  `# cnt` int DEFAULT NULL,
  `Q28` text,
  `Q29b` text,
  `S6` int DEFAULT NULL,
  `Q8` text,
  `Q9` text,
  `Q12` text,
  `Q16` text,
  `Q17` text,
  `Q18` text,
  `Q19` text,
  `Q24` text,
  `Q45` text,
  `Q46` text,
  `Q47` text,
  `Q48` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_dictionary`
--

DROP TABLE IF EXISTS `data_dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `data_dictionary` (
  `data_dictionary_pk` int NOT NULL AUTO_INCREMENT,
  `question_label` varchar(45) NOT NULL,
  `response_encoded_value` int NOT NULL,
  `ref_response_description` int DEFAULT NULL,
  PRIMARY KEY (`data_dictionary_pk`),
  KEY `ref_response_description` (`ref_response_description`),
  CONSTRAINT `data_dictionary_ibfk_1` FOREIGN KEY (`ref_response_description`) REFERENCES `response_description` (`response_description_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Queries`
--

DROP TABLE IF EXISTS `Queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Queries` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_id` int DEFAULT NULL,
  `query` longtext,
  PRIMARY KEY (`id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `queries_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `response_description`
--

DROP TABLE IF EXISTS `response_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `response_description` (
  `response_description_pk` int NOT NULL AUTO_INCREMENT,
  `response_choice` varchar(35) NOT NULL,
  `response_description` varchar(1024) NOT NULL,
  PRIMARY KEY (`response_description_pk`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-23 12:30:00
