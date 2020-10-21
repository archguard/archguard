drop table if exists bad_smell_threashold_suite;
CREATE TABLE `bad_smell_threashold_suite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `suite_name` varchar(255) NOT NULL,
  `is_default` boolean,
  `thresholds` JSON,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

alter table system_info add column `threshold_suite_id` bigint(20);
