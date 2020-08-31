drop table if exists project_info;
drop table if exists project_info;
CREATE TABLE `project_info`
(
    `id`                      bigint(20)                NOT NULL AUTO_INCREMENT,
    `project_name`            varchar(100)              not null,
    `repo`                    varchar(500)              not null,
    `repo_type`               varchar(20) DEFAULT 'GIT' not null,
    `username`                varchar(256)              null,
    `password`                varchar(256)              null,
    `sql_table`               text,
    `scanned`                 varchar(10)               null,
    `quality_gate_profile_id` bigint(20),
    `updated_time`            DateTime,
    `created_time`            DateTime,
    PRIMARY KEY (`id`)
);

INSERT INTO `project_info`(`id`, `project_name`, `repo`, `repo_type`, `username`, `password`, `sql_table`,
                           `scanned`, `quality_gate_profile_id`,
                           `updated_time`, `created_time`)
VALUES (1, 'projectName1', 'repo1', 'GIT', 'username1', 'WCA5RH/O9J4yxgU40Z+thg==', 'sql1',
        'NONE', 1,
        '2020-08-11 16:06:06',
        '2020-08-11 16:06:06');
