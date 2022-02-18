drop table if exists ProjectInfo;
CREATE TABLE `project_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(256) not null,
  `repo` varchar(500) not null,
  `repo_type` varchar(20) DEFAULT 'GIT' not null,
  `username` varchar(256) null,
  `password` varchar(256) null,
  `sql_table` text,
  `updated_time` DateTime,
  `created_time` DateTime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
