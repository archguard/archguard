-- MySQL dump 10.13  Distrib 8.0.12, for macos10.13 (x86_64)
--
-- Host: ec2-68-79-38-105.cn-northwest-1.compute.amazonaws.com.cn    Database: default@default
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CheckStyle`
--

DROP TABLE IF EXISTS `CheckStyle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `CheckStyle` (
  `id` char(36) NOT NULL,
  `system_id` varchar(36) NOT NULL,
  `file` mediumtext NOT NULL,
  `source` mediumtext NOT NULL,
  `message` mediumtext NOT NULL,
  `line` int NOT NULL,
  `column` int NOT NULL,
  `severity` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ClassStatistic`
--

DROP TABLE IF EXISTS `ClassStatistic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `ClassStatistic` (
  `id` char(36) NOT NULL,
  `systemId` mediumtext,
  `moduleName` mediumtext,
  `packageName` mediumtext NOT NULL,
  `typeName` mediumtext NOT NULL,
  `lines` int NOT NULL,
  `updateAt` datetime NOT NULL,
  `createAt` datetime NOT NULL,
  `fanin` int NOT NULL,
  `fanout` int NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Configure`
--

DROP TABLE IF EXISTS `Configure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `Configure` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `type` mediumtext  NOT NULL,
  `key` mediumtext  NOT NULL,
  `value` mediumtext  NOT NULL,
  `updatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `order` int NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JAnnotation`
--

DROP TABLE IF EXISTS `JAnnotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `JAnnotation` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `targetType` varchar(255)  NOT NULL,
  `targetId` varchar(36)  NOT NULL,
  `name` varchar(255)  NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JAnnotationValue`
--

DROP TABLE IF EXISTS `JAnnotationValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `JAnnotationValue` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `annotationId` varchar(36)  NOT NULL,
  `key` varchar(255)  NOT NULL,
  `value` text ,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `JAnnotationValue_ibfk_1` (`annotationId`),
  CONSTRAINT `JAnnotationValue_ibfk_1` FOREIGN KEY (`annotationId`) REFERENCES `JAnnotation` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JClass`
--

DROP TABLE IF EXISTS `JClass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `JClass` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `name` char(255)  DEFAULT NULL,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  `module` mediumtext ,
  `loc` int DEFAULT NULL,
  `access` mediumtext ,
  `is_thirdparty` tinyint(1) DEFAULT NULL,
  `is_test` tinyint(1) DEFAULT NULL,
  `class_name` mediumtext ,
  `package_name` mediumtext ,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JField`
--

DROP TABLE IF EXISTS `JField`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `JField` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `name` mediumtext  NOT NULL,
  `type` mediumtext  NOT NULL,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  `clzname` mediumtext ,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JMethod`
--

DROP TABLE IF EXISTS `JMethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `JMethod` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `access` mediumtext  NOT NULL,
  `returntype` mediumtext ,
  `name` mediumtext  NOT NULL,
  `clzname` mediumtext ,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  `module` mediumtext ,
  `argumenttypes` mediumtext ,
  `is_test` tinyint(1) DEFAULT NULL,
  `loc` int DEFAULT NULL,
  `package_name` mediumtext ,
  `class_name` mediumtext ,
  PRIMARY KEY (`id`)
--  KEY `JMethod_clzname_name_index` (`clzname`(255),`name`(255)),
--  KEY `JMethod_module_index` (`module`(255))
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JMethodPLProcedure`
--

DROP TABLE IF EXISTS `JMethodPLProcedure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `JMethodPLProcedure` (
  `id` char(36)  NOT NULL,
  `clz` mediumtext  NOT NULL,
  `method` mediumtext  NOT NULL,
  `pkg` mediumtext  NOT NULL,
  `procedure` mediumtext  NOT NULL,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MethodStatistic`
--

DROP TABLE IF EXISTS `MethodStatistic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `MethodStatistic` (
  `id` char(36) NOT NULL,
  `systemId` mediumtext,
  `moduleName` mediumtext,
  `packageName` mediumtext NOT NULL,
  `typeName` mediumtext NOT NULL,
  `methodName` mediumtext NOT NULL,
  `lines` int NOT NULL,
  `updateAt` datetime NOT NULL,
  `createAt` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Overview`
--

DROP TABLE IF EXISTS `Overview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `Overview` (
  `id` char(36) NOT NULL,
  `overview_type` varchar(20) NOT NULL,
  `overview_value` text NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PLProcedure`
--

DROP TABLE IF EXISTS `PLProcedure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `PLProcedure` (
  `id` char(36)  NOT NULL,
  `name` mediumtext  NOT NULL,
  `pkg` mediumtext ,
  `module` mediumtext ,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PLProcedureSqlTable`
--

DROP TABLE IF EXISTS `PLProcedureSqlTable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `PLProcedureSqlTable` (
  `id` char(36)  NOT NULL,
  `clz` mediumtext  NOT NULL,
  `method` mediumtext  NOT NULL,
  `table` mediumtext  NOT NULL,
  `operate` mediumtext  NOT NULL,
  `module` mediumtext ,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ScannerConfigure`
--

DROP TABLE IF EXISTS `ScannerConfigure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `ScannerConfigure` (
  `id` char(36) NOT NULL,
  `type` mediumtext NOT NULL,
  `key` mediumtext NOT NULL,
  `value` mediumtext NOT NULL,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SqlAction`
--

DROP TABLE IF EXISTS `SqlAction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `SqlAction` (
  `id` char(36)  NOT NULL,
  `clz` mediumtext  NOT NULL,
  `method` mediumtext  NOT NULL,
  `action` mediumtext  NOT NULL,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SqlCondition`
--

DROP TABLE IF EXISTS `SqlCondition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `SqlCondition` (
  `id` char(36)  NOT NULL,
  `op` mediumtext  NOT NULL,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SqlConditionValue`
--

DROP TABLE IF EXISTS `SqlConditionValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `SqlConditionValue` (
  `id` char(36)  NOT NULL,
  `table` mediumtext ,
  `column` mediumtext ,
  `value` mediumtext ,
  `updatedAt` datetime(3) NOT NULL,
  `createdAt` datetime(3) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_ClassDependences`
--

DROP TABLE IF EXISTS `_ClassDependences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_ClassDependences` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_ClassDependences_ibfk_1` FOREIGN KEY (`a`) REFERENCES `JClass` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_ClassDependences_ibfk_2` FOREIGN KEY (`b`) REFERENCES `JClass` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_ClassFields`
--

DROP TABLE IF EXISTS `_ClassFields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_ClassFields` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_ClassFields_ibfk_1` FOREIGN KEY (`a`) REFERENCES `JClass` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_ClassFields_ibfk_2` FOREIGN KEY (`b`) REFERENCES `JField` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_ClassMethods`
--

DROP TABLE IF EXISTS `_ClassMethods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_ClassMethods` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_ClassMethods_ibfk_1` FOREIGN KEY (`a`) REFERENCES `JClass` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_ClassMethods_ibfk_2` FOREIGN KEY (`b`) REFERENCES `JMethod` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_ClassParent`
--

DROP TABLE IF EXISTS `_ClassParent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_ClassParent` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_ClassParent_ibfk_1` FOREIGN KEY (`a`) REFERENCES `JClass` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_ClassParent_ibfk_2` FOREIGN KEY (`b`) REFERENCES `JClass` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_MethodCallees`
--

DROP TABLE IF EXISTS `_MethodCallees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_MethodCallees` (
  `id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_MethodCallees_ibfk_1` FOREIGN KEY (`a`) REFERENCES `JMethod` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_MethodCallees_ibfk_2` FOREIGN KEY (`b`) REFERENCES `JMethod` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_MethodFields`
--

DROP TABLE IF EXISTS `_MethodFields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_MethodFields` (
  `id` char(36)  NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  `system_id` bigint NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_MethodFields_ibfk_1` FOREIGN KEY (`a`) REFERENCES `JMethod` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_MethodFields_ibfk_2` FOREIGN KEY (`b`) REFERENCES `JField` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_PLProcedureCallees`
--

DROP TABLE IF EXISTS `_PLProcedureCallees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_PLProcedureCallees` (
  `id` char(36)  NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_PLProcedureCallees_ibfk_1` FOREIGN KEY (`a`) REFERENCES `PLProcedure` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_PLProcedureCallees_ibfk_2` FOREIGN KEY (`b`) REFERENCES `PLProcedure` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_PLProcedureSqlAction`
--

DROP TABLE IF EXISTS `_PLProcedureSqlAction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_PLProcedureSqlAction` (
  `id` char(36)  NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
--  KEY `fk__SqlAction` (`b`),
  CONSTRAINT `fk__SqlAction` FOREIGN KEY (`b`) REFERENCES `SqlAction` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_SqlActionConditions`
--

DROP TABLE IF EXISTS `_SqlActionConditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_SqlActionConditions` (
  `id` char(36)  NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
--  UNIQUE KEY `AB_unique` (`a`,`b`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_SqlActionConditions_ibfk_1` FOREIGN KEY (`a`) REFERENCES `SqlAction` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_SqlActionConditions_ibfk_2` FOREIGN KEY (`b`) REFERENCES `SqlCondition` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_SqlLeftConditionValue`
--

DROP TABLE IF EXISTS `_SqlLeftConditionValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_SqlLeftConditionValue` (
  `id` char(36)  NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
--  UNIQUE KEY `AB_unique` (`a`,`b`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_SqlLeftConditionValue_ibfk_1` FOREIGN KEY (`a`) REFERENCES `SqlCondition` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_SqlLeftConditionValue_ibfk_2` FOREIGN KEY (`b`) REFERENCES `SqlConditionValue` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_SqlRightConditionValue`
--

DROP TABLE IF EXISTS `_SqlRightConditionValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `_SqlRightConditionValue` (
  `id` char(36)  NOT NULL,
  `a` char(36)  DEFAULT NULL,
  `b` char(36)  DEFAULT NULL,
  PRIMARY KEY (`id`),
--  UNIQUE KEY `AB_unique` (`a`,`b`),
--  KEY `A` (`a`),
--  KEY `B` (`b`),
  CONSTRAINT `_SqlRightConditionValue_ibfk_1` FOREIGN KEY (`a`) REFERENCES `SqlCondition` (`id`) ON DELETE CASCADE,
  CONSTRAINT `_SqlRightConditionValue_ibfk_2` FOREIGN KEY (`b`) REFERENCES `SqlConditionValue` (`id`) ON DELETE CASCADE
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `badSmell`
--

DROP TABLE IF EXISTS `badSmell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `badSmell` (
  `id` varchar(100) NOT NULL,
  `system_id` varchar(36) NOT NULL,
  `entity_name` varchar(200) NOT NULL,
  `line` int DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `size` int DEFAULT NULL,
  `type` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bad_smell_threashold_suite`
--

DROP TABLE IF EXISTS `bad_smell_threashold_suite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `bad_smell_threashold_suite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `suite_name` varchar(255)  NOT NULL,
  `is_default` tinyint(1) DEFAULT NULL,
  `thresholds` longtext ,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bundle`
--

DROP TABLE IF EXISTS `bundle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `bundle` (
  `instruction_missed` int DEFAULT NULL,
  `instruction_covered` int DEFAULT NULL,
  `line_missed` int DEFAULT NULL,
  `line_covered` int DEFAULT NULL,
  `branch_missed` int DEFAULT NULL,
  `branch_covered` int DEFAULT NULL,
  `complexity_missed` int DEFAULT NULL,
  `complexity_covered` int DEFAULT NULL,
  `method_missed` int DEFAULT NULL,
  `method_covered` int DEFAULT NULL,
  `class_missed` int DEFAULT NULL,
  `class_covered` int DEFAULT NULL,
  `bundle_name` varchar(200) NOT NULL,
  `scan_time` bigint NOT NULL,
  PRIMARY KEY (`bundle_name`,`scan_time`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `change_entry`
--

DROP TABLE IF EXISTS `change_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `change_entry` (
  `old_path` varchar(500) DEFAULT NULL,
  `new_path` varchar(500) DEFAULT NULL,
  `cognitive_complexity` int DEFAULT NULL,
  `change_mode` varchar(10) DEFAULT NULL,
  `commit_id` varchar(50) DEFAULT NULL,
  `system_id` int DEFAULT NULL,
  `commit_time` bigint DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `circular_dependency_metrics`
--

DROP TABLE IF EXISTS `circular_dependency_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `circular_dependency_metrics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL,
  `circular_dependency` varchar(3000)  NOT NULL,
  `type` varchar(255)  NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_access`
--

DROP TABLE IF EXISTS `class_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `class_access` (
  `id` varchar(50)  NOT NULL,
  `class_id` char(36)  DEFAULT NULL,
  `is_abstract` tinyint(1) DEFAULT NULL,
  `is_interface` tinyint(1) DEFAULT NULL,
  `is_synthetic` tinyint(1) DEFAULT NULL,
  `system_id` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_coupling`
--

DROP TABLE IF EXISTS `class_coupling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `class_coupling` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL,
  `class_id` varchar(255)  NOT NULL,
  `inner_fan_in` int NOT NULL,
  `inner_fan_out` int NOT NULL,
  `outer_fan_in` int NOT NULL,
  `outer_fan_out` int NOT NULL,
  `inner_instability` decimal(8,4) NOT NULL,
  `inner_coupling` decimal(8,4) NOT NULL,
  `outer_instability` decimal(8,4) NOT NULL,
  `outer_coupling` decimal(8,4) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_metrics`
--

DROP TABLE IF EXISTS `class_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `class_metrics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL,
  `class_id` varchar(255)  NOT NULL,
  `abc` int DEFAULT NULL,
  `noc` int DEFAULT NULL,
  `dit` int DEFAULT NULL,
  `lcom4` int DEFAULT NULL,
  `fanin` int DEFAULT NULL,
  `fanout` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cognitive_complexity`
--

DROP TABLE IF EXISTS `cognitive_complexity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `cognitive_complexity` (
  `id` varchar(50) NOT NULL,
  `commit_id` varchar(50) DEFAULT NULL,
  `changed_cognitive_complexity` int DEFAULT NULL,
  `system_id` int DEFAULT NULL,
  `path` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commit_log`
--

DROP TABLE IF EXISTS `commit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `commit_log` (
  `id` varchar(50) NOT NULL,
  `commit_time` bigint DEFAULT NULL,
  `short_msg` varchar(200) DEFAULT NULL,
  `committer_name` varchar(50) DEFAULT NULL,
  `committer_email` varchar(100) DEFAULT NULL,
  `repo_id` varchar(50) DEFAULT NULL,
  `system_id` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_class`
--

DROP TABLE IF EXISTS `data_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `data_class` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_id` char(36)  NOT NULL,
  `field_id` char(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
--  UNIQUE KEY `class_id_field_id_UNIQUE` (`class_id`,`field_id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dubbo_bean`
--

DROP TABLE IF EXISTS `dubbo_bean`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `dubbo_bean` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `beanId` varchar(128)  DEFAULT NULL,
  `implClass` varchar(128)  DEFAULT NULL,
  `module_id` varchar(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dubbo_module`
--

DROP TABLE IF EXISTS `dubbo_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `dubbo_module` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `name` varchar(128)  NOT NULL,
  `path` varchar(512)  NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dubbo_reference_config`
--

DROP TABLE IF EXISTS `dubbo_reference_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `dubbo_reference_config` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `referenceId` varchar(128)  NOT NULL,
  `interface` varchar(128)  NOT NULL,
  `version` varchar(128)  DEFAULT NULL,
  `group` varchar(128)  DEFAULT NULL,
  `module_id` varchar(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dubbo_service_config`
--

DROP TABLE IF EXISTS `dubbo_service_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `dubbo_service_config` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `interface` varchar(128)  NOT NULL,
  `ref` varchar(128)  NOT NULL,
  `version` varchar(128)  DEFAULT NULL,
  `group` varchar(128)  DEFAULT NULL,
  `module_id` varchar(36)  DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evaluationReport`
--

DROP TABLE IF EXISTS `evaluationReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `evaluationReport` (
  `id` varchar(50) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `dimensions` text,
  `comment` text,
  `improvements` text,
  `createdDate` datetime DEFAULT NULL,
  `detail` longtext,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `git_hot_file`
--

DROP TABLE IF EXISTS `git_hot_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `git_hot_file` (
  `system_id` bigint NOT NULL,
  `repo` varchar(256) DEFAULT NULL,
  `path` mediumtext NOT NULL,
  `module_name` varchar(1024) DEFAULT NULL,
  `class_name` varchar(256) DEFAULT NULL,
  `jclass_id` char(36) DEFAULT NULL,
  `modified_count` int NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `item` (
  `instruction_missed` int DEFAULT NULL,
  `instruction_covered` int DEFAULT NULL,
  `line_missed` int DEFAULT NULL,
  `line_covered` int DEFAULT NULL,
  `branch_missed` int DEFAULT NULL,
  `branch_covered` int DEFAULT NULL,
  `complexity_missed` int DEFAULT NULL,
  `complexity_covered` int DEFAULT NULL,
  `method_missed` int DEFAULT NULL,
  `method_covered` int DEFAULT NULL,
  `class_missed` int DEFAULT NULL,
  `class_covered` int DEFAULT NULL,
  `item_type` varchar(10) DEFAULT NULL,
  `item_name` varchar(500) NOT NULL,
  `bundle_name` varchar(200) NOT NULL,
  `scan_time` bigint NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`item_name`,`bundle_name`,`scan_time`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logic_module`
--

DROP TABLE IF EXISTS `logic_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `logic_module` (
  `id` varchar(36)  NOT NULL,
  `system_id` bigint NOT NULL,
  `name` varchar(128)  NOT NULL,
  `members` mediumtext  COMMENT '子模块或类成员',
  `status` varchar(20)  NOT NULL DEFAULT 'NORMAL' COMMENT '显示状态',
  `lg_members` mediumtext  COMMENT '逻辑模块成员',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `method_access`
--

DROP TABLE IF EXISTS `method_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `method_access` (
  `id` varchar(50)  NOT NULL,
  `method_id` char(36)  DEFAULT NULL,
  `is_abstract` tinyint(1) DEFAULT NULL,
  `is_synthetic` tinyint(1) DEFAULT NULL,
  `system_id` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_static` tinyint(1) DEFAULT NULL,
  `is_private` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `method_metrics`
--

DROP TABLE IF EXISTS `method_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `method_metrics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL,
  `method_id` varchar(255)  NOT NULL,
  `fanin` int DEFAULT NULL,
  `fanout` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `module_metrics`
--

DROP TABLE IF EXISTS `module_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `module_metrics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL,
  `module_name` varchar(255)  NOT NULL,
  `fanin` int DEFAULT NULL,
  `fanout` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `package_metrics`
--

DROP TABLE IF EXISTS `package_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `package_metrics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL,
  `module_name` varchar(255)  NOT NULL,
  `package_name` varchar(255)  NOT NULL,
  `fanin` int DEFAULT NULL,
  `fanout` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_gate_profile`
--

DROP TABLE IF EXISTS `quality_gate_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `quality_gate_profile` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一索引',
  `name` varchar(256)  NOT NULL COMMENT '名称',
  `config` mediumtext  NOT NULL COMMENT '质量阈配置',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `system_info`
--

DROP TABLE IF EXISTS `system_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `system_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_name` varchar(256)  NOT NULL,
  `repo` varchar(500)  NOT NULL,
  `repo_type` varchar(20)  NOT NULL DEFAULT 'GIT',
  `username` varchar(256)  DEFAULT NULL,
  `password` varchar(256)  DEFAULT NULL,
  `sql_table` text ,
  `scanned` varchar(10)  DEFAULT NULL,
  `quality_gate_profile_id` bigint DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `threshold_suite_id` bigint DEFAULT NULL,
  `branch` varchar(50)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `testBadSmell`
--

DROP TABLE IF EXISTS `testBadSmell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `testBadSmell` (
  `id` varchar(100) NOT NULL,
  `system_id` varchar(36) NOT NULL,
  `line` int DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `file_name` text,
  `type` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `violation`
--

DROP TABLE IF EXISTS `violation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;

CREATE TABLE `violation` (
  `system_id` varchar(36) NOT NULL,
  `file` varchar(200) DEFAULT NULL,
  `beginline` int DEFAULT NULL,
  `endline` int DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `text` varchar(500) DEFAULT NULL
) ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-23 10:27:17
