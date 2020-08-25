drop table if exists `logic_module`;

create table logic_module
(
    id         varchar(36)                  not null
        primary key,
    project_id bigint(20)                   NOT NULL,
    name       varchar(128)                 not null,
    members    mediumtext,
    lg_members mediumtext,
    status     varchar(20) DEFAULT 'NORMAL' not null
);

create index idx_project_id
    on logic_module (project_id);
