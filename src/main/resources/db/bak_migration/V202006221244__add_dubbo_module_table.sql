create table dubbo_module
(
    `id`   varchar(36) primary key,
    `name` varchar(128) not null,
    `path` varchar(512) not null
)
    collate = utf8mb4_unicode_ci;

create table dubbo_reference_config
(
    `id`            varchar(36) primary key,
    `referenceId`   varchar(128) not null,
    `interfaceName` varchar(128) not null,
    `version`       varchar(128),
    `group`         varchar(128)
)
    collate = utf8mb4_unicode_ci;

create table dubbo_service_config
(
    `id`            varchar(36) primary key,
    `interfaceName` varchar(128) not null,
    `ref`           varchar(128) not null,
    `version`       varchar(128),
    `group`         varchar(128)
)
    collate = utf8mb4_unicode_ci;

create table dubbo_bean
(
    `id`        varchar(36) primary key,
    `beanId`    varchar(128),
    `implClass` varchar(128)
)
    collate = utf8mb4_unicode_ci;
