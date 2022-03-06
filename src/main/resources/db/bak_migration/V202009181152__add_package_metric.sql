drop table if exists package_metrics;
create table package_metrics
(
    id           bigint auto_increment
        primary key,
    system_id    bigint       not null,
    module_name  varchar(255) not null,
    package_name varchar(255) not null,
    fanin        int          null,
    fanout       int          null
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id on package_metrics (system_id);

