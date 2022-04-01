create table metric_circular_dependency
(
    id                  bigint auto_increment
        primary key,
    project_id          bigint        not null,
    circular_dependency varchar(3000) not null,
    type                varchar(255)  not null
)
    collate = utf8mb4_unicode_ci;

create index idx_circular_dependency_metrics_project_id
    on metric_circular_dependency (project_id);
