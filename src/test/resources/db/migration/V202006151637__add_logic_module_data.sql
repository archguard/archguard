create table logic_module
(
    id         varchar(36)                  not null
        primary key,
    name       varchar(128)                 not null,
    members    mediumtext,
    lg_members mediumtext,
    status     varchar(20) DEFAULT 'NORMAL' not null
);
