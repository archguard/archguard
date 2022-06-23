CREATE TABLE `insight_custom`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `system_id`  BIGINT        NOT NULL,
    `expression` VARCHAR(1024) NOT NULL,
    `name`       VARCHAR(1024) NOT NULL,
    `schedule`   VARCHAR(1024) NOT NULL DEFAULT '',
    `created_at` TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `publish_at` TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
);
