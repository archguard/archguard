# container fan in and fan out
CREATE TABLE `container_service`
(
    `id`          VARCHAR(36)  NOT NULL PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL,
    `scm_address` VARCHAR(36)  NOT NULL,
    `type`        VARCHAR(10)  NOT NULL,
    `created_by`  VARCHAR(36)  NOT NULL,
    `created_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP (3)
);

CREATE TABLE `container_demand`
(
    `id`                 VARCHAR(36)  NOT NULL PRIMARY KEY,
    `service_id`         VARCHAR(36)  NOT NULL,
    `target_url`         VARCHAR(255) NOT NULL,
    `target_http_method` VARCHAR(255) NOT NULL,
    `created_by`         VARCHAR(36)  NOT NULL,
    `created_at`         TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`         TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP (3),
    INDEX                `i_service_id` (`service_id`)
);

CREATE TABLE `container_resource`
(
    `id`                 VARCHAR(36)  NOT NULL PRIMARY KEY,
    `service_id`         VARCHAR(36)  NOT NULL,
    `source_url`         VARCHAR(255) NOT NULL,
    `source_http_method` VARCHAR(255) NOT NULL,
    `package_name`       VARCHAR(255) NOT NULL,
    `class_name`         VARCHAR(255) NOT NULL,
    `method_name`        VARCHAR(255) NOT NULL,
    `created_by`         VARCHAR(36)  NOT NULL,
    `created_at`         TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`         TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP (3),
    INDEX                `i_service_id` (`service_id`)
);
