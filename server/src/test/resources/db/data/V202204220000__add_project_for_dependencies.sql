CREATE TABLE `project_composition_dependencies`
(
    `id`              VARCHAR(36)  NOT NULL,
    `system_id`       VARCHAR(36)  NOT NULL,
    `name`            VARCHAR(36)  NULL,
    `version`         VARCHAR(256) NOT NULL,
    `package_manager` VARCHAR(256) NOT NULL,
    `parent_id`       VARCHAR(36)  NULL,
    `dep_name`        VARCHAR(256) NOT NULL,
    `dep_group`       VARCHAR(256) NULL,
    `dep_artifact`    VARCHAR(256) NOT NULL,
    `dep_metadata`    TEXT(32768)  NULL,
    `dep_source`      TEXT(32768)  NULL,
    `dep_scope`       VARCHAR(256) NULL,
    `dep_version`     VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`)
);
