create table dubbo_bean
(
    id        varchar(36)  not null
        primary key,
    beanId    varchar(128) null,
    implClass varchar(128) null,
    module_id varchar(36)  null
);

create table dubbo_module
(
    id   varchar(36)  not null
        primary key,
    name varchar(128) not null,
    path varchar(512) not null
);

create table dubbo_reference_config
(
    id          varchar(36)  not null
        primary key,
    referenceId varchar(128) not null,
    interface   varchar(128) not null,
    version     varchar(128) null,
    `group`     varchar(128) null,
    module_id   varchar(36)  null
);

create table dubbo_service_config
(
    id        varchar(36)  not null
        primary key,
    interface varchar(128) not null,
    ref       varchar(128) not null,
    version   varchar(128) null,
    `group`   varchar(128) null,
    module_id varchar(36)  null
);

