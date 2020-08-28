drop table dubbo_bean;
create table dubbo_bean
(
    id        varchar(36)  not null
        primary key,
    project_id bigint(20)                   NOT NULL,
    beanId    varchar(128) null,
    implClass varchar(128) null,
    module_id varchar(36)  null
);
drop table dubbo_module;
create table dubbo_module
(
    id   varchar(36)  not null
        primary key,
    project_id bigint(20)                   NOT NULL,
    name varchar(128) not null,
    path varchar(512) not null
);
drop table dubbo_reference_config;
create table dubbo_reference_config
(
    id          varchar(36)  not null
        primary key,
    project_id bigint(20)                   NOT NULL,
    referenceId varchar(128) not null,
    interface   varchar(128) not null,
    version     varchar(128) null,
    `group`     varchar(128) null,
    module_id   varchar(36)  null
);
drop table dubbo_service_config;
create table dubbo_service_config
(
    id        varchar(36)  not null
        primary key,
    project_id bigint(20)                   NOT NULL,
    interface varchar(128) not null,
    ref       varchar(128) not null,
    version   varchar(128) null,
    `group`   varchar(128) null,
    module_id varchar(36)  null
);

