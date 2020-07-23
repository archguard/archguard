CREATE TABLE `metrics_module`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `module_name` varchar(255) NOT NULL,
  `outer_instability_avg` decimal(8,4) NOT NULL,
  `outer_instability_med` decimal(8,4) NOT NULL,
  `outer_coupling_avg` decimal(8,4) NOT NULL,
  `outer_coupling_med` decimal(8,4) NOT NULL,
  `inner_instability_avg` decimal(8,4) NOT NULL,
  `inner_instability_med` decimal(8,4) NOT NULL,
  `inner_coupling_avg` decimal(8,4) NOT NULL,
  `inner_coupling_med` decimal(8,4) NOT NULL,
  PRIMARY KEY (`id`)
);

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
  PRIMARY KEY (`id`)
);

CREATE TABLE `metrics_class`  (
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
  PRIMARY KEY (`id`)
);
