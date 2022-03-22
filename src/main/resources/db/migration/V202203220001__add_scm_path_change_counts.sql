CREATE TABLE `scm_path_change_count`
(
    `id`        VARCHAR(36)   NOT NULL PRIMARY KEY,
    `path`      VARCHAR(1024) NOT NULL,
    `changes`   INT           NOT NULL,
    `system_id` BIGINT        NOT NULL
);