-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 172.16.130.1    Database: bkris
-- ------------------------------------------------------
-- Server version	5.6.51

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
-- Table structure for table `xray_aes`
--

DROP TABLE IF EXISTS `xray_aes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_aes` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SECRET` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `STATUS` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SECRET_UNIQUE` (`SECRET`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_auto`
--

DROP TABLE IF EXISTS `xray_auto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_auto` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LAST_REQUEST_NO` varchar(10) NOT NULL,
  `YEAR` year(4) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_billing`
--

DROP TABLE IF EXISTS `xray_billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_billing` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `XRAY_BILL_NO` varchar(10) NOT NULL,
  `REQUEST_NO` varchar(10) NOT NULL,
  `XRAY_CODE` varchar(10) NOT NULL,
  `XRAY_PRICE` int(10) unsigned NOT NULL,
  `DF` int(10) unsigned DEFAULT NULL,
  `CHARGED` int(1) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_birad`
--

DROP TABLE IF EXISTS `xray_birad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_birad` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BIRAD` varchar(10) NOT NULL,
  `LEVEL` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(60) NOT NULL,
  `DETAIL` varchar(200) NOT NULL,
  `DETAIL_THAI` varchar(200) NOT NULL,
  `FOLLOW_UP` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_body_part`
--

DROP TABLE IF EXISTS `xray_body_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_body_part` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BODY_PART` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=315 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_center`
--

DROP TABLE IF EXISTS `xray_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_center` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(20) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `NAME_ENG` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_clinical_element`
--

DROP TABLE IF EXISTS `xray_clinical_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_clinical_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DESCRIPTION` text COLLATE utf8mb4_unicode_ci,
  `DEFAULT_VAL` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UNIT` varchar(25) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ENABLED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_code`
--

DROP TABLE IF EXISTS `xray_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_code` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CENTER` varchar(10) NOT NULL,
  `XRAY_CODE` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `XRAY_TYPE_CODE` varchar(50) NOT NULL,
  `BODY_PART` varchar(15) NOT NULL,
  `CHARGE_TOTAL` float unsigned NOT NULL,
  `PORTABLE_CHARGE` int(4) NOT NULL,
  `DF` int(5) unsigned DEFAULT NULL,
  `TIME_USE` int(3) unsigned DEFAULT NULL,
  `BIRAD_FLAG` varchar(1) NOT NULL DEFAULT '0',
  `PREP_ID` varchar(10) NOT NULL,
  `DELETE_FLAG` int(1) NOT NULL DEFAULT '0',
  `TEMPLATE` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `XRAY_CODE` (`XRAY_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=201 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_conclusion`
--

DROP TABLE IF EXISTS `xray_conclusion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_conclusion` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(10) NOT NULL,
  `TERM` varchar(30) NOT NULL,
  `VALUE` text NOT NULL,
  `STATUS` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TERM` (`TERM`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_country`
--

DROP TABLE IF EXISTS `xray_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_country` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COUNTRY_CODE` varchar(20) NOT NULL,
  `COUNTRY_NAME` varchar(50) NOT NULL,
  `COUNTRY_NAME_ENG` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `COUNTRY_CODE` (`COUNTRY_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_delete`
--

DROP TABLE IF EXISTS `xray_delete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_delete` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BUNDLE_ID` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BUNDLE_ID_UNIQUE` (`BUNDLE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_department`
--

DROP TABLE IF EXISTS `xray_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_department` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `CENTER` varchar(10) NOT NULL,
  `DEPARTMENT_ID` varchar(10) NOT NULL,
  `NAME_VIE` varchar(50) NOT NULL,
  `NAME_ENG` varchar(50) NOT NULL,
  `TYPE` varchar(5) NOT NULL,
  `XRAY_TYPE_CODE` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `DEPARTMENT_ID` (`DEPARTMENT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_describe`
--

DROP TABLE IF EXISTS `xray_describe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_describe` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(10) NOT NULL,
  `TERM` varchar(30) NOT NULL,
  `VALUE` text NOT NULL,
  `STATUS` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TERM` (`TERM`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_describe_auto`
--

DROP TABLE IF EXISTS `xray_describe_auto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_describe_auto` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `XRAY_CODE` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `MOTA_AUTO` text NOT NULL,
  `KL_AUTO` text NOT NULL,
  `NOTE_AUTO` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `XRAY_CODE` (`XRAY_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_device`
--

DROP TABLE IF EXISTS `xray_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_device` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MODALITY` varchar(6) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MANUFACTURER` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MODEL` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SERIAL_NO` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DATE` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE_UNIQUE` (`CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_dicom`
--

DROP TABLE IF EXISTS `xray_dicom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_dicom` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MRN` varchar(20) NOT NULL,
  `ACCESSION` varchar(15) NOT NULL,
  `STUDY` varchar(100) NOT NULL,
  `STUDY_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION` (`ACCESSION`),
  UNIQUE KEY `STUDY_UNIQUE` (`STUDY`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_dicom_instances`
--

DROP TABLE IF EXISTS `xray_dicom_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_dicom_instances` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SERIES` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `OBJECT` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_dicom_series`
--

DROP TABLE IF EXISTS `xray_dicom_series`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_dicom_series` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SERIES` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_digi_form_mammo`
--

DROP TABLE IF EXISTS `xray_digi_form_mammo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_digi_form_mammo` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` int(30) NOT NULL,
  `EVER` varchar(1) NOT NULL,
  `FAMILY_MOM` varchar(1) NOT NULL,
  `FAMILY_SIS` varchar(1) NOT NULL,
  `FAMILY__AUN` varchar(1) NOT NULL,
  `FAMILY_GRAN_MOM` varchar(1) NOT NULL,
  `FAMILY_DAUG` varchar(1) NOT NULL,
  `FAMILY_COU` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_doituong`
--

DROP TABLE IF EXISTS `xray_doituong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_doituong` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_ID` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_exam`
--

DROP TABLE IF EXISTS `xray_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_exam` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DIRECTORY` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `STATUS` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NW',
  `LAST_STATUS` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `REMOVE` tinyint(1) NOT NULL DEFAULT '0',
  `CREATED` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_UPDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `VERSION` int(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION_UNIQUE` (`ACCESSION`)
) ENGINE=InnoDB AUTO_INCREMENT=1833 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_fhir`
--

DROP TABLE IF EXISTS `xray_fhir`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_fhir` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BUNDLE_ID` varchar(20) NOT NULL,
  `SERVICE_ID` varchar(20) NOT NULL,
  `ACCESSION_NUMBER` varchar(50) NOT NULL,
  `IUID` varchar(50) DEFAULT NULL,
  `ENCOUNTER_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SERVICE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `STATUS` varchar(5) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BUNDLE_ID` (`BUNDLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26847 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_fhir1`
--

DROP TABLE IF EXISTS `xray_fhir1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_fhir1` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BUNDLE_ID` varchar(50) NOT NULL,
  `SERVICE_ID` varchar(50) NOT NULL,
  `ACCESSION_NUMBER` varchar(50) NOT NULL,
  `IUID` varchar(50) DEFAULT NULL,
  `ENCOUNTER_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `SERVICE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATED_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `STATUS` varchar(5) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `BUNDLE_ID` (`BUNDLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_film_folder`
--

DROP TABLE IF EXISTS `xray_film_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_film_folder` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_hapi_server`
--

DROP TABLE IF EXISTS `xray_hapi_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_hapi_server` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IP` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PORT` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PATH` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `USER` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PASS` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_his_log`
--

DROP TABLE IF EXISTS `xray_his_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_his_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `BUNDLE_ID` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ACCESSION_NUMBER` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `CODE` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MESSAGE` text COLLATE utf8mb4_unicode_ci,
  `STATUS` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CREATED_TIME` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'now()',
  PRIMARY KEY (`id`),
  UNIQUE KEY `BUNDLE_ID_UNIQUE` (`BUNDLE_ID`),
  UNIQUE KEY `ACCESSION_NUMBER_UNIQUE` (`ACCESSION_NUMBER`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_hl7`
--

DROP TABLE IF EXISTS `xray_hl7`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_hl7` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION_NUMBER` varchar(50) NOT NULL,
  `PATIENT_ID` varchar(50) DEFAULT NULL,
  `STUDY_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `STATUS` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION_NUMBER` (`ACCESSION_NUMBER`)
) ENGINE=InnoDB AUTO_INCREMENT=10651 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_hl7_msh`
--

DROP TABLE IF EXISTS `xray_hl7_msh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_hl7_msh` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEND_APP` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SEND_FAC` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `RECE_APP` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `RECE_FAC` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `COUNTRY_CODE` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT 'VNM',
  `CHARACTER` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT 'UNICODE',
  `LANGUAGE_MESSAGE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `DIRECTORY` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_hl7_obx`
--

DROP TABLE IF EXISTS `xray_hl7_obx`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_hl7_obx` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `VALUE_TYPE` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `OBS_IDEN` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `OBS_SUB_ID` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `OBS_VALUE` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UNITS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_hl7_terminology_country`
--

DROP TABLE IF EXISTS `xray_hl7_terminology_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_hl7_terminology_country` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ALPHA` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `REGION` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SUB_REGION` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DEFAULT` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ALPHA_UNIQUE` (`ALPHA`)
) ENGINE=MyISAM AUTO_INCREMENT=248 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_hl7_terminology_race`
--

DROP TABLE IF EXISTS `xray_hl7_terminology_race`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_hl7_terminology_race` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DEFAULT` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_icd`
--

DROP TABLE IF EXISTS `xray_icd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_icd` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(150) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_image_status`
--

DROP TABLE IF EXISTS `xray_image_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_image_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `HN` varchar(10) NOT NULL,
  `XN` varchar(10) NOT NULL,
  `DEPARTMENT_ID` varchar(10) NOT NULL,
  `MOVE_BY` varchar(50) NOT NULL,
  `DATE` date NOT NULL,
  `TYPE_STATUS` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `HN` (`HN`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_log`
--

DROP TABLE IF EXISTS `xray_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER` varchar(20) NOT NULL,
  `TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IP` varchar(30) NOT NULL,
  `URL` varchar(100) NOT NULL,
  `EVENT` varchar(20) NOT NULL,
  `MRN` varchar(20) NOT NULL,
  `ACCESSION` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_login`
--

DROP TABLE IF EXISTS `xray_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_login` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(2) NOT NULL,
  `TODAY` int(2) NOT NULL DEFAULT '0',
  `WEEK` int(5) NOT NULL DEFAULT '0',
  `MONTH` int(5) NOT NULL DEFAULT '0',
  `YEAR` int(6) NOT NULL DEFAULT '0',
  `TOTAL` int(11) NOT NULL DEFAULT '0',
  `LAST_LOGIN` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USER_ID_UNIQUE` (`USER_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_major_problem`
--

DROP TABLE IF EXISTS `xray_major_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_major_problem` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `VALUE` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ENABLED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_message`
--

DROP TABLE IF EXISTS `xray_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_CODE` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MRN` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MESSAGE` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `MESS_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_modalities`
--

DROP TABLE IF EXISTS `xray_modalities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_modalities` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MOD_NAME` varchar(20) NOT NULL,
  `MOD_TYPE` varchar(20) NOT NULL,
  `MOD_DESCRIPTION` varchar(50) NOT NULL,
  `AE_TITLE` varchar(50) NOT NULL,
  `IP_ADDRESS` varchar(50) NOT NULL,
  `PORT` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MOD_NAME` (`MOD_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_modalities_type`
--

DROP TABLE IF EXISTS `xray_modalities_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_modalities_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MOD_TYPE` varchar(20) NOT NULL,
  `MOD_DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MOD_TYPE` (`MOD_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_mwl`
--

DROP TABLE IF EXISTS `xray_mwl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_mwl` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IP` varchar(20) NOT NULL,
  `AE` varchar(50) NOT NULL,
  `PORT` int(5) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_name_prefix`
--

DROP TABLE IF EXISTS `xray_name_prefix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_name_prefix` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `THAI` varchar(50) NOT NULL,
  `ENG` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_news`
--

DROP TABLE IF EXISTS `xray_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_news` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CENTER_CODE` varchar(20) NOT NULL,
  `NEWS` varchar(1000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_note`
--

DROP TABLE IF EXISTS `xray_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_note` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(10) COLLATE utf8_bin NOT NULL,
  `TERM` varchar(30) COLLATE utf8_bin NOT NULL,
  `VALUE` text COLLATE utf8_bin NOT NULL,
  `STATUS` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TERM_UNIQUE` (`TERM`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_order_cart`
--

DROP TABLE IF EXISTS `xray_order_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_order_cart` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SESSION_ID` varchar(50) NOT NULL,
  `MRN` varchar(10) NOT NULL,
  `XRAY_CODE` varchar(10) NOT NULL,
  `DATE` date NOT NULL,
  `REFERRER_ID` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_pacs`
--

DROP TABLE IF EXISTS `xray_pacs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_pacs` (
  `ID` int(2) NOT NULL AUTO_INCREMENT,
  `HOST` varchar(16) NOT NULL,
  `PORT` varchar(5) NOT NULL,
  `AETITLE` varchar(15) NOT NULL,
  `HL7PORT` varchar(5) DEFAULT NULL,
  `WADOPORT` varchar(5) NOT NULL,
  `WADOCONTEXT` varchar(50) NOT NULL,
  `LOCAL_ARCHIVE_RIS` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `AETITLE` (`AETITLE`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_pathology`
--

DROP TABLE IF EXISTS `xray_pathology`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_pathology` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `STUDY` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SERIES` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `INSTANCE` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SOPCLASS` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NUMBER` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MODALITY` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INSTANCE_UNIQUE` (`INSTANCE`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_patient_diagnosis`
--

DROP TABLE IF EXISTS `xray_patient_diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_patient_diagnosis` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MRN` varchar(45) NOT NULL,
  `ACCESSION` varchar(45) NOT NULL,
  `CE` int(11) NOT NULL,
  `CE_VAL` varchar(100) NOT NULL,
  `MP` int(11) NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ENABLED` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_patient_info`
--

DROP TABLE IF EXISTS `xray_patient_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_patient_info` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `CENTER_CODE` varchar(10) CHARACTER SET utf8 NOT NULL,
  `MRN` varchar(20) CHARACTER SET utf8 NOT NULL,
  `XN` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `SSN` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PREFIX` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `NAME` varchar(120) CHARACTER SET utf8 NOT NULL,
  `LASTNAME` varchar(120) CHARACTER SET utf8 NOT NULL,
  `NAME_ENG` varchar(120) CHARACTER SET utf8 DEFAULT NULL,
  `LASTNAME_ENG` varchar(120) CHARACTER SET utf8 DEFAULT NULL,
  `NAME_OLD` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `LASTNAME_OLD` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `SEX` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `BIRTH_DATE` date DEFAULT NULL,
  `TELEPHONE` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  `EMAIL` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `NOTE` varchar(2000) CHARACTER SET utf8 DEFAULT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `FIRSTVISITDATE` date DEFAULT NULL,
  `LASTVISITDATE` date DEFAULT NULL,
  `RIGHT_CODE` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `ADDRESS` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `VILLAGE` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `ROAD` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `TAMBON_CODE` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `AMPHOE_CODE` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `PROVINCE_CODE` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `COUNTRY_CODE` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MRN` (`MRN`)
) ENGINE=MyISAM AUTO_INCREMENT=6190 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_patient_right`
--

DROP TABLE IF EXISTS `xray_patient_right`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_patient_right` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RIGHT_CODE` varchar(10) NOT NULL,
  `RIGHT_NAME` varchar(50) NOT NULL,
  `DISCOUNT` int(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `RIGHT_CODE` (`RIGHT_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_preparation`
--

DROP TABLE IF EXISTS `xray_preparation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_preparation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PREP_ID` int(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `MODALITY` varchar(20) NOT NULL,
  `BODY_PART` varchar(20) NOT NULL,
  `DESCRIPTION_THAI` varchar(5000) NOT NULL,
  `DESCRIPTION_ENG` varchar(5000) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PREP_ID` (`PREP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_printer`
--

DROP TABLE IF EXISTS `xray_printer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_printer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LOCATION` varchar(20) NOT NULL,
  `IP` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_province`
--

DROP TABLE IF EXISTS `xray_province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_province` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROVINCE_CODE` varchar(20) NOT NULL,
  `PROVINCE_NAME` varchar(50) NOT NULL,
  `PROVINCE_NAME_ENG` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PROVINCE_CODE` (`PROVINCE_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_referrer`
--

DROP TABLE IF EXISTS `xray_referrer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_referrer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REFERRER_ID` varchar(10) NOT NULL,
  `DEGREE` varchar(50) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `LASTNAME` varchar(50) DEFAULT NULL,
  `NAME_ENG` varchar(50) DEFAULT NULL,
  `LASTNAME_ENG` varchar(50) DEFAULT NULL,
  `PREFIX` varchar(3) DEFAULT NULL,
  `SEX` varchar(5) DEFAULT NULL,
  `CENTER_CODE` varchar(10) DEFAULT NULL,
  `PRACTITIONER_ID` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `REFERRER_ID` (`REFERRER_ID`),
  UNIQUE KEY `PRACTITIONER_ID` (`PRACTITIONER_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report`
--

DROP TABLE IF EXISTS `xray_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `DESCRIBEREPORT` text CHARACTER SET utf8,
  `CONCLUSION` text CHARACTER SET utf8,
  `NOTE` text CHARACTER SET utf8,
  `TITLE` text COLLATE utf8mb4_unicode_ci,
  `TECHNICAL` text CHARACTER SET utf8,
  `WORKSTATION` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `DOITUONG` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `BIRAD` varchar(1) CHARACTER SET utf8 DEFAULT NULL,
  `HISTORY` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `CALCIUM` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `CORONARY` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `KEY_IMAGE_LINK` varchar(5000) CHARACTER SET utf8 DEFAULT NULL,
  `DICTATE_BY` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `DICTATE_DATE` date DEFAULT NULL,
  `DICTATE_TIME` time DEFAULT NULL,
  `APPROVE_BY` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `APPROVE_DATE` date DEFAULT NULL,
  `APPROVE_TIME` time DEFAULT NULL,
  `STATUS` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION_UNIQUE` (`ACCESSION`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_check`
--

DROP TABLE IF EXISTS `xray_report_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_check` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION_UNIQUE` (`ACCESSION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_detail`
--

DROP TABLE IF EXISTS `xray_report_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_detail` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DESCRIBE_NICE` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `CONCLUSION_NICE` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `NOTE_NICE` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION_UNIQUE` (`ACCESSION`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_extends`
--

DROP TABLE IF EXISTS `xray_report_extends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_extends` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REQ_DETAIL_ID` int(11) NOT NULL,
  `ACCESSION` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `START_TIME` datetime NOT NULL,
  `REPORT_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `REPORT_STATUS` tinyint(1) NOT NULL DEFAULT '0',
  `CANCEL_STATUS` tinyint(1) NOT NULL DEFAULT '0',
  `APPROVED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ASSIGN` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ASSIGN_BY` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `STATUS` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PAGE` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `LOCKBY` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `URGENT` tinyint(1) DEFAULT '0',
  `DESCRIBEREPORT` longtext COLLATE utf8mb4_unicode_ci,
  `CONCLUSION` mediumtext COLLATE utf8mb4_unicode_ci,
  `NOTE` mediumtext COLLATE utf8mb4_unicode_ci,
  `TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_structure`
--

DROP TABLE IF EXISTS `xray_report_structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_structure` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MODALITY_TYPE` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `TYPE` varchar(20) NOT NULL,
  `GROUP` varchar(20) NOT NULL,
  `DETAIL` varchar(2000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_template`
--

DROP TABLE IF EXISTS `xray_report_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `XRAY_CODE` varchar(50) NOT NULL,
  `XRAY_TYPE_CODE` varchar(10) NOT NULL,
  `BODY_PART` varchar(15) NOT NULL,
  `USER_ID` varchar(20) NOT NULL,
  `PROCEDURE_VN` longtext NOT NULL,
  `PROCEDURE_EN` longtext NOT NULL,
  `ALL_USER` varchar(1) NOT NULL DEFAULT '0',
  `TIME_CREATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `XRAY_CODE_UNIQUE` (`XRAY_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_template_detail`
--

DROP TABLE IF EXISTS `xray_report_template_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_template_detail` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEMPLATE_ID` int(3) NOT NULL,
  `DESCRIBE_VN` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `DESCRIBE_EN` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `CONCLUSION_VN` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `CONCLUSION_EN` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `STATUS` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TEMPLATE_ID_UNIQUE` (`TEMPLATE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_title`
--

DROP TABLE IF EXISTS `xray_report_title`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_title` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `XRAY_TYPE_CODE` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `XRAY_CODE` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DES_VN` varchar(2555) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `DES_EN` varchar(2555) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `TITLE` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE_UNIQUE` (`XRAY_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_vic_clinical_elements`
--

DROP TABLE IF EXISTS `xray_report_vic_clinical_elements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_vic_clinical_elements` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `XRAY_CODE` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `CLINICAL_ELEMENTS` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_report_vic_major_problem`
--

DROP TABLE IF EXISTS `xray_report_vic_major_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_report_vic_major_problem` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `XRAY_CODE` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MAJOR_PROBLEM` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_request`
--

DROP TABLE IF EXISTS `xray_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REQUEST_NO` varchar(30) NOT NULL,
  `XN` varchar(50) DEFAULT NULL,
  `MRN` varchar(10) NOT NULL,
  `REFERRER` varchar(20) DEFAULT NULL,
  `REQUEST_DATE` date NOT NULL,
  `REQUEST_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DEPARTMENT_ID` varchar(10) DEFAULT NULL,
  `PORTABLE` int(1) unsigned DEFAULT NULL,
  `USER` varchar(10) DEFAULT NULL,
  `CANCEL_STATUS` int(1) unsigned NOT NULL DEFAULT '0',
  `USER_ID_CANCLE` varchar(10) DEFAULT NULL,
  `CANCEL_DATE` date DEFAULT NULL,
  `CANCEL_TIME` time DEFAULT NULL,
  `STATUS` varchar(10) NOT NULL DEFAULT '1',
  `ICON` varchar(50) DEFAULT NULL,
  `NOTE` varchar(500) DEFAULT NULL,
  `CENTER_CODE` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `REQUEST_NO` (`REQUEST_NO`)
) ENGINE=MyISAM AUTO_INCREMENT=10735 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_request_detail`
--

DROP TABLE IF EXISTS `xray_request_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_request_detail` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `REQUEST_NO` varchar(30) NOT NULL,
  `ACCESSION` varchar(30) NOT NULL,
  `XRAY_CODE` varchar(30) NOT NULL,
  `WORKSTATION` varchar(5) DEFAULT NULL,
  `DOITUONG` varchar(5) DEFAULT NULL,
  `GHICHU` text,
  `CHARGED` int(1) DEFAULT NULL,
  `REQUEST_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `REQUEST_TIME` time NOT NULL DEFAULT '00:00:00',
  `REQUEST_DATE` date DEFAULT NULL,
  `SCHEDULE_DATE` date DEFAULT NULL,
  `SCHEDULE_TIME` time DEFAULT NULL,
  `REPORT_TIME` time DEFAULT NULL,
  `REPORT_DATE` date DEFAULT NULL,
  `REPORT_STATUS` varchar(1) DEFAULT '0',
  `CANCEL_STATUS` varchar(1) DEFAULT '0',
  `USER_ID_CANCEL` varchar(10) DEFAULT NULL,
  `ARRIVAL_TIME` timestamp NULL DEFAULT NULL,
  `READY_TIME` timestamp NULL DEFAULT NULL,
  `START_TIME` timestamp NULL DEFAULT NULL,
  `COMPLETE_TIME` timestamp NULL DEFAULT NULL,
  `ASSIGN_TIME` timestamp NULL DEFAULT NULL,
  `APPROVED_TIME` timestamp NULL DEFAULT NULL,
  `ASSIGN` varchar(20) DEFAULT NULL,
  `ASSIGN_BY` varchar(20) DEFAULT NULL,
  `STATUS` varchar(10) NOT NULL DEFAULT 'NEW',
  `PAGE` varchar(15) NOT NULL DEFAULT 'ORDER LIST',
  `LOCKBY` varchar(15) DEFAULT NULL,
  `URGENT` varchar(1) NOT NULL DEFAULT '0',
  `LASTREPORT_ID` varchar(20) DEFAULT NULL,
  `TEMP_REPORT` varchar(15000) DEFAULT NULL,
  `AUTO_SAVE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `TECH1` varchar(5) DEFAULT NULL,
  `TECH2` varchar(5) DEFAULT NULL,
  `TECH3` varchar(5) DEFAULT NULL,
  `FLAG1` int(1) DEFAULT NULL,
  `FLAG2` int(1) DEFAULT NULL,
  `FLAG3` int(1) DEFAULT NULL,
  `REPORT_BOOK` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION` (`ACCESSION`)
) ENGINE=MyISAM AUTO_INCREMENT=10631 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_room`
--

DROP TABLE IF EXISTS `xray_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_room` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CENTER` varchar(10) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(20) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_sc_calendar`
--

DROP TABLE IF EXISTS `xray_sc_calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_sc_calendar` (
  `calendar_id` int(11) NOT NULL AUTO_INCREMENT,
  `calendar_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`calendar_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_sc_events`
--

DROP TABLE IF EXISTS `xray_sc_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_sc_events` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `event_id` int(11) NOT NULL,
  `MRN` varchar(10) NOT NULL,
  `ACCESSION` varchar(16) NOT NULL,
  `event_name` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `event_description` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `calendar_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `all_day` smallint(6) DEFAULT '0',
  `create_user` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `event_id` (`event_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_schedule`
--

DROP TABLE IF EXISTS `xray_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_schedule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `HN` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `LASTNAME` varchar(50) NOT NULL,
  `AGE` varchar(3) NOT NULL,
  `WARD` varchar(20) NOT NULL,
  `DATE` date NOT NULL,
  `TIME_START` time NOT NULL,
  `TIME_END` time NOT NULL,
  `DATE_REG` date NOT NULL,
  `ROOM` varchar(20) NOT NULL,
  `XRAY_CODE` varchar(10) NOT NULL,
  `USER_ID` varchar(10) NOT NULL,
  `DOCTOR_ID_REQUEST` varchar(10) DEFAULT NULL,
  `REASON_FOR_STUDY` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `HN` (`HN`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_signature`
--

DROP TABLE IF EXISTS `xray_signature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_signature` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESSION` varchar(50) NOT NULL,
  `SIGNATURE` text NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ACCESSION` (`ACCESSION`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_status`
--

DROP TABLE IF EXISTS `xray_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(10) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_stock`
--

DROP TABLE IF EXISTS `xray_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_stock` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STOCK_CODE` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `TYPE` varchar(50) NOT NULL,
  `AMOUNT` int(3) unsigned NOT NULL,
  `PRICE` int(7) NOT NULL,
  `UNIT` varchar(20) NOT NULL,
  `ACTIVE` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_stock_type`
--

DROP TABLE IF EXISTS `xray_stock_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_stock_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_tambon`
--

DROP TABLE IF EXISTS `xray_tambon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_tambon` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TAMBON_CODE` varchar(20) NOT NULL,
  `TAMBON_NAME` varchar(50) NOT NULL,
  `TAMBON_NAME_ENG` varchar(50) NOT NULL,
  `AMPHOE_CODE` varchar(20) NOT NULL,
  `POST_CODE` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TAMBON_CODE` (`TAMBON_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_teaching_cases`
--

DROP TABLE IF EXISTS `xray_teaching_cases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_teaching_cases` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SECTION_ID` varchar(20) NOT NULL,
  `MRN` varchar(10) NOT NULL,
  `ACCESSION` varchar(16) NOT NULL,
  `USER_ID` varchar(10) NOT NULL,
  `USER_GROUP` varchar(10) NOT NULL,
  `USER_ALL` varchar(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_teaching_cat`
--

DROP TABLE IF EXISTS `xray_teaching_cat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_teaching_cat` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SECTION` varchar(40) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_type`
--

DROP TABLE IF EXISTS `xray_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_type` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `XRAY_TYPE_CODE` varchar(10) NOT NULL,
  `TYPE_NAME` varchar(50) NOT NULL,
  `TYPE_NAME_ENG` varchar(50) NOT NULL,
  `MOD_TYPE` varchar(20) DEFAULT NULL,
  `DETAIL` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `XRAY_TYPE_CODE` (`XRAY_TYPE_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_usage`
--

DROP TABLE IF EXISTS `xray_usage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_usage` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REQUEST_NO` varchar(10) NOT NULL,
  `ACCESSION` varchar(10) NOT NULL,
  `FILM_SIZE` varchar(10) NOT NULL,
  `FILM_QULITY` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_user`
--

DROP TABLE IF EXISTS `xray_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_user` (
  `ID` int(5) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(50) NOT NULL,
  `DF_CODE` varchar(50) NOT NULL,
  `LOGIN` varchar(20) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `LASTNAME` varchar(50) NOT NULL,
  `NAME_ENG` varchar(50) NOT NULL,
  `LASTNAME_ENG` varchar(50) NOT NULL,
  `SEX` varchar(1) DEFAULT 'M',
  `USER_TYPE_CODE` varchar(20) NOT NULL,
  `PREFIX` varchar(20) DEFAULT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `CENTER_CODE` varchar(10) NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SESSION` varchar(100) NOT NULL,
  `ENABLE` varchar(1) NOT NULL DEFAULT '1',
  `ALL_CENTER` tinyint(1) NOT NULL DEFAULT '0',
  `LOGINTIME` time DEFAULT NULL,
  `TEXT_SIGNATURE` varchar(200) NOT NULL,
  `PACS_LOGIN` varchar(20) DEFAULT NULL,
  `api_id` varchar(255) DEFAULT NULL,
  `api_secret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `LOGIN_UNIQUE` (`LOGIN`),
  UNIQUE KEY `DF_CODE_UNIQUE` (`DF_CODE`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_user_df_code`
--

DROP TABLE IF EXISTS `xray_user_df_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_user_df_code` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(10) NOT NULL,
  `DF_CODE` varchar(20) NOT NULL,
  `NAME_THAI` varchar(50) NOT NULL,
  `NAME_ENG` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_user_right`
--

DROP TABLE IF EXISTS `xray_user_right`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_user_right` (
  `USER_ID` int(3) NOT NULL,
  `SUPER_ADMIN` int(1) NOT NULL DEFAULT '0',
  `ADMIN` int(1) NOT NULL DEFAULT '0',
  `DELETE_ORDER` int(1) NOT NULL DEFAULT '0',
  `CHANGE_STATUS` int(1) NOT NULL DEFAULT '0',
  `EDIT_PATIENT` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`USER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_user_type`
--

DROP TABLE IF EXISTS `xray_user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_user_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(10) NOT NULL,
  `TYPE` varchar(15) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_version`
--

DROP TABLE IF EXISTS `xray_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_version` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `VERSION` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `xray_workstation`
--

DROP TABLE IF EXISTS `xray_workstation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xray_workstation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_ID` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `CODE` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AETITLE` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `WS_ID` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-28  9:49:37
