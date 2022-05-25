CREATE TABLE `scm_diff_change`
(
    `id`           VARCHAR(36)   NOT NULL PRIMARY KEY,
    `system_id`    BIGINT        NOT NULL,
    `since_rev`    VARCHAR(1024) NOT NULL,
    `until_rev`    VARCHAR(1024) NOT NULL,
    `package_name` VARCHAR(1024) NOT NULL,
    `class_name`   VARCHAR(1024) NOT NULL,
    `relations`    mediumtext    NOT NULL,
    `created_at`   TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`   TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
);