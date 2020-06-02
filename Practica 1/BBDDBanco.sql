CREATE DATABASE  IF NOT EXISTS `Banco` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `Banco`;
-- MySQL dump 10.13  Distrib 5.5.22, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: Banco
-- ------------------------------------------------------
-- Server version	5.5.22-0ubuntu1

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
-- Table structure for table `cuentas`
--

DROP TABLE IF EXISTS `cuentas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuentas` (
  `idcuentas` int(11) NOT NULL AUTO_INCREMENT,
  `idusuario` int(11) NOT NULL,
  `saldo` decimal(15,2) NOT NULL,
  PRIMARY KEY (`idcuentas`),
  KEY `fk_cuentas_1_idx` (`idusuario`),
  CONSTRAINT `fk_cuentas_1` FOREIGN KEY (`idusuario`) REFERENCES `usuarios` (`idusuarios`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuentas`
--

LOCK TABLES `cuentas` WRITE;
/*!40000 ALTER TABLE `cuentas` DISABLE KEYS */;
INSERT INTO `cuentas` VALUES (19,6,400.00),(20,5,80.00),(21,1,65.00),(22,1,900.00),(23,5,65.00),(24,6,8000.00),(25,1,650.00),(26,7,40.00),(27,6,0.00),(28,16,686.99),(30,16,856.99),(31,16,856.99),(32,16,856.99);
/*!40000 ALTER TABLE `cuentas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `retiradas`
--

DROP TABLE IF EXISTS `retiradas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `retiradas` (
  `idretiradas` int(11) NOT NULL AUTO_INCREMENT,
  `cuenta` int(11) NOT NULL,
  `cantidad` decimal(15,2) NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`idretiradas`),
  KEY `fk_retiradas_1_idx` (`cuenta`),
  CONSTRAINT `fk_retiradas_1` FOREIGN KEY (`cuenta`) REFERENCES `cuentas` (`idcuentas`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `retiradas`
--

LOCK TABLES `retiradas` WRITE;
/*!40000 ALTER TABLE `retiradas` DISABLE KEYS */;
INSERT INTO `retiradas` VALUES (1,19,30.00,'1998-04-03'),(2,24,400.00,'2003-05-25');
/*!40000 ALTER TABLE `retiradas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transferencias`
--

DROP TABLE IF EXISTS `transferencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transferencias` (
  `idtransferencias` int(11) NOT NULL AUTO_INCREMENT,
  `cuentaorigen` int(11) NOT NULL,
  `cuentadestino` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `cantidad` decimal(15,2) NOT NULL,
  PRIMARY KEY (`idtransferencias`),
  KEY `fk_transferencias_1_idx` (`cuentaorigen`),
  KEY `fk_transferencias_2_idx` (`cuentadestino`),
  CONSTRAINT `fk_transferencias_1` FOREIGN KEY (`cuentaorigen`) REFERENCES `cuentas` (`idcuentas`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_transferencias_2` FOREIGN KEY (`cuentadestino`) REFERENCES `cuentas` (`idcuentas`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transferencias`
--

LOCK TABLES `transferencias` WRITE;
/*!40000 ALTER TABLE `transferencias` DISABLE KEYS */;
INSERT INTO `transferencias` VALUES (1,19,24,'2003-07-09 00:00:00',320.00),(2,23,25,'2002-06-13 00:00:00',63.00),(4,28,23,'2019-07-01 00:00:00',70.00),(6,28,26,'2019-02-02 00:00:00',20.00),(7,28,26,'2019-02-02 00:00:00',20.00),(8,28,26,'2019-02-02 00:00:00',20.00),(10,28,26,'2019-02-02 00:00:00',15.00),(11,28,26,'2019-02-02 00:00:00',15.00),(12,28,26,'2019-02-02 00:00:00',15.00),(13,28,26,'2019-02-02 00:00:00',10.00);
/*!40000 ALTER TABLE `transferencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `idusuarios` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido1` varchar(45) NOT NULL,
  `apellido2` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  PRIMARY KEY (`idusuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'pepito','carrobles','diaz','ppcarrobles@gmail.com'),(5,'maria','gutierrez','martin','mgm@gmail.com'),(6,'loreto','garcia','tejasa','loreto@gmail.com'),(7,'raul','garcia','ruiz','raulillo@gmail.com'),(12,'mariana','romeral','lopez','mariaaaaana@hotmail.es'),(14,'jesus','romeral','lopez','jesusitodemivida@gmail.com'),(15,'jesus','romeral','lopez','jesusitodemivida@gmail.com'),(16,'pablo','moreno',NULL,'pblillo@gmail.com');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

CREATE USER 'ClienteBanco'@'localhost' IDENTIFIED BY 'restuser';
GRANT ALL PRIVILEGES ON Banco.* TO 'ClienteBanco'@'localhost';

-- Dump completed on 2020-04-12 18:50:35
