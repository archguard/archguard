drop table if exists metric_class;
CREATE TABLE `metric_class`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_id` bigint(20) NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `inner_fan_in` int(11) NOT NULL,
  `inner_fan_out` int(11) NOT NULL,
  `outer_fan_in` int(11) NOT NULL,
  `outer_fan_out` int(11) NOT NULL,
  `inner_instability` decimal(8,4) NOT NULL,
  `inner_coupling` decimal(8,4) NOT NULL,
  `outer_instability` decimal(8,4) NOT NULL,
  `outer_coupling` decimal(8,4) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_package_id` (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
