CREATE TABLE `governance_issue`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `system_id`  BIGINT        NOT NULL,
    `position`   VARCHAR(1024) NOT NULL DEFAULT '',
    `rule_id`    VARCHAR(1024) NOT NULL,
    `name`       VARCHAR(1024) NOT NULL DEFAULT '',
    `detail`     MEDIUMTEXT    NOT NULL,
    `rule_type`  VARCHAR(1024) NOT NULL DEFAULT '',
    `severity`   VARCHAR(1024) NOT NULL DEFAULT '',
    `full_name`  VARCHAR(1024) NOT NULL DEFAULT '',
    `source`     VARCHAR(1024) NOT NULL DEFAULT '',
    `created_at` TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `publish_at` TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);
