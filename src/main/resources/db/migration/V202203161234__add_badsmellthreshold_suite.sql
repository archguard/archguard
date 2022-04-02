create table metric_bad_smell_threshold_suite
(
    id           bigint auto_increment primary key,
    suite_name   varchar(255)                        not null,
    is_default   tinyint(1)                          null,
    thresholds   longtext                            null,
    updated_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
collate = utf8mb4_unicode_ci;

insert into g (suite_name, is_default, thresholds)
values ('架构评估优化指标', 1,
        '[{"name":"体量维度","threshold":[{"name":"过大的包","condition":"包中包含的类个数 > 指标阈值","value":20},{"name":"过大的包","condition":"包的总代码行数（不含import行数） > 指标阈值","value":12000},{"name":"过大的模块","condition":"模块中包含包的个数 > 指标阈值","value":20},{"name":"过大的模块","condition":"模块的总代码行数 > 指标阈值","value":24000},{"name":"过大的类","condition":"类中包含的方法个数 > 指标阈值","value":20},{"name":"过大的类","condition":"类的代码行数 > 指标阈值","value":600},{"name":"过大的方法","condition":"方法代码行数（含空行） > 指标阈值","value":30}]},{"name":"耦合纬度","threshold":[{"name":"枢纽模块","condition":"出向依赖或入向依赖 > 指标阈值","value":8},{"name":"枢纽包","condition":"出向依赖或入向依赖 > 指标阈值","value":8},{"name":"枢纽类","condition":"出向依赖或入向依赖 > 指标阈值","value":8},{"name":"枢纽方法","condition":"出向依赖或入向依赖 > 指标阈值","value":8},{"name":"数据泥团","condition":"缺乏内聚指标（LCOM） > 指标阈值","value":4},{"name":"过深继承","condition":"继承深度 > 指标阈值","value":4},{"name":"循环依赖","condition":"循环依赖的数量 > 指标阈值","value":0}]}]');
insert into metric_bad_smell_threshold_suite (suite_name, is_default, thresholds)
values ('架构评估基线指标', 0,
        '[{"name":"体量维度","threshold":[{"name":"过大的包","condition":"包中包含的类个数 > 指标阈值","value":30},{"name":"过大的包","condition":"包的总代码行数（不含import行数） > 指标阈值","value":20000},{"name":"过大的模块","condition":"模块中包含包的个数 > 指标阈值","value":30},{"name":"过大的模块","condition":"模块的总代码行数 > 指标阈值","value":40000},{"name":"过大的类","condition":"类中包含的方法个数 > 指标阈值","value":30},{"name":"过大的类","condition":"类的代码行数 > 指标阈值","value":2000},{"name":"过大的方法","condition":"方法代码行数（含空行） > 指标阈值","value":80}]},{"name":"耦合纬度","threshold":[{"name":"枢纽模块","condition":"出向依赖或入向依赖 > 指标阈值","value":16},{"name":"枢纽包","condition":"出向依赖或入向依赖 > 指标阈值","value":16},{"name":"枢纽类","condition":"出向依赖或入向依赖 > 指标阈值","value":16},{"name":"枢纽方法","condition":"出向依赖或入向依赖 > 指标阈值","value":16},{"name":"数据泥团","condition":"缺乏内聚指标（LCOM） > 指标阈值","value":8},{"name":"过深继承","condition":"继承深度 > 指标阈值","value":6},{"name":"循环依赖","condition":"循环依赖的数量 > 指标阈值","value":0}]}]');
