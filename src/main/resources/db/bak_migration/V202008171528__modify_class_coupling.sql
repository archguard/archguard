DROP TABLE `class_coupling`;
CREATE TABLE `class_coupling`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` BIGINT      NOT NULL,
  `class_id` varchar(255) NOT NULL,
  `inner_fan_in` int NOT NULL,
  `inner_fan_out` int NOT NULL,
  `outer_fan_in` int NOT NULL,
  `outer_fan_out` int NOT NULL,
  `inner_instability` int NOT NULL,
  `inner_coupling` int NOT NULL,
  `outer_instability` int NOT NULL,
  `outer_coupling` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;