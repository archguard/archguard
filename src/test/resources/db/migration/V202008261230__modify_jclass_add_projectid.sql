drop table if exists `JClass`;

create table JClass
(
    id        char(36)    not null,
    project_id bigint(20)                   NOT NULL,
    name      char(255)   null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    module    mediumtext  null,
    loc       int         null,
    access    mediumtext  null,
    constraint id_UNIQUE
        unique (id)
);
