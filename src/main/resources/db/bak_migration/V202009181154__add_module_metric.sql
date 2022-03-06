drop table if exists module_metrics;
create table module_metrics
(
    id           bigint auto_increment
        primary key,
    system_id    bigint       not null,
    module_name  varchar(255) not null,
    fanin        int          null,
    fanout       int          null
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id on module_metrics (system_id);

