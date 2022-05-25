CREATE TABLE `container_service`
(
    `id`             VARCHAR(36)  NOT NULL,
    `name`           VARCHAR(255) NOT NULL,
    `container_type` VARCHAR(10)  NOT NULL,
    `scm_address`    VARCHAR(255) NOT NULL,
    `created_by`     VARCHAR(36)  NOT NULL,
    `system_id`      BIGINT       NOT NULL,
    `created_at`     DATETIME,
    `updated_at`     DATETIME,
    PRIMARY KEY (`id`)
);

CREATE TABLE `container_demand`
(
    `id`                 VARCHAR(36)  NOT NULL,
    `system_id`          BIGINT       NOT NULL,
    `source_package`     VARCHAR(255) NOT NULL,
    `source_class`       VARCHAR(255) NOT NULL,
    `source_method`      VARCHAR(255) NOT NULL,
    `service_id`         VARCHAR(36)  NOT NULL,
    `target_url`         VARCHAR(255) NOT NULL,
    `target_http_method` VARCHAR(255) NOT NULL,
    `created_by`         VARCHAR(36)  NOT NULL,
    `created_at`         DATETIME,
    `updated_at`         DATETIME,
    PRIMARY KEY (`id`)
);

CREATE TABLE `container_resource`
(
    `id`                 VARCHAR(36)  NOT NULL,
    `service_id`         VARCHAR(36)  NOT NULL,
    `system_id`          BIGINT       NOT NULL,
    `source_url`         VARCHAR(255) NOT NULL,
    `source_http_method` VARCHAR(255) NOT NULL,
    `package_name`       VARCHAR(255) NOT NULL,
    `class_name`         VARCHAR(255) NOT NULL,
    `method_name`        VARCHAR(255) NOT NULL,
    `created_by`         VARCHAR(36)  NOT NULL,
    `created_at`         DATETIME,
    `updated_at`         DATETIME,
    PRIMARY KEY (`id`)
);
