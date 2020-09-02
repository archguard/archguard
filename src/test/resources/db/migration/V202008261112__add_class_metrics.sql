drop table if exists `class_metrics`;

create table class_metrics
(
    id            bigint auto_increment
        primary key,
    system_id    bigint       not null,
    class_id      varchar(255) not null,
    abc  int,
    noc int,
    dit  int,
    lcom4 int
);

--create index idx_project_id
--    on class_metrics (project_id);

