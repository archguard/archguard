drop table if exists method_metrics;
create table method_metrics
(
    id        bigint auto_increment
        primary key,
    system_id bigint       not null,
    method_id  varchar(255) not null,
    fanin     int          null,
    fanout    int          null
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id on method_metrics (system_id);

