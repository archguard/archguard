drop table if exists metric_bad_smell_threshold_suite;
CREATE TABLE `metric_bad_smell_threshold_suite` (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  suite_name varchar(255) NOT NULL,
  is_default boolean,
  thresholds longtext,
  updated_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

insert into metric_bad_smell_threshold_suite (suite_name, is_default, thresholds) values ('架构评估一级指标', 1,'[{"name":"体量维度","threshold":[{"name":"过大的方法","condition":"方法代码行数 \u003e 指标阈值","value":30},{"name":"过大的类","condition":"方法个数 \u003e 指标阈值","value":20},{"name":"过大的类","condition":"方法代码行数 \u003e 指标阈值","value":600}]},{"name":"耦合纬度","threshold":[{"name":"枢纽模块","condition":"出向依赖或入向依赖 \u003e 指标阈值","value":8},{"name":"枢纽包","condition":"出向依赖或入向依赖  \u003e 指标阈值","value":8},{"name":"枢纽类","condition":"出向依赖或入向依赖 \u003e 指标阈值","value":8}]}]');
insert into metric_bad_smell_threshold_suite (suite_name, is_default, thresholds) values ('架构评估二级指标', 0,'[{"name":"体量维度","threshold":[{"name":"过大的方法","condition":"方法代码行数 \u003e 指标阈值","value":80},{"name":"过大的类","condition":"方法个数 \u003e 指标阈值","value":30},{"name":"过大的类","condition":"方法代码行数 \u003e 指标阈值","value":1200}]},{"name":"耦合纬度","threshold":[{"name":"枢纽模块","condition":"出向依赖或入向依赖 \u003e 指标阈值","value":12},{"name":"枢纽包","condition":"出向依赖或入向依赖  \u003e 指标阈值","value":12},{"name":"枢纽类","condition":"出向依赖或入向依赖 \u003e 指标阈值","value":12}]}]');

alter table system_info add column `threshold_suite_id` bigint(20);
update system_info set threshold_suite_id =1 where id is not null;