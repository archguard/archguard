drop table if exists metrics_package;
CREATE TABLE `metrics_package`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `module_id` bigint(20) NOT NULL,
  `package_name` varchar(255) NOT NULL,
  `outer_instability_avg` decimal(8,4) NOT NULL,
  `outer_instability_med` decimal(8,4) NOT NULL,
  `outer_coupling_avg` decimal(8,4) NOT NULL,
  `outer_coupling_med` decimal(8,4) NOT NULL,
  `inner_instability_avg` decimal(8,4) NOT NULL,
  `inner_instability_med` decimal(8,4) NOT NULL,
  `inner_coupling_avg` decimal(8,4) NOT NULL,
  `inner_coupling_med` decimal(8,4) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_module_id` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
