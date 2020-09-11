create table JClass
(
    id        char(36)    not null,
    system_id bigint      not null,
    name      char(255)   null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    module    mediumtext  null,
    loc       int         null,
    access    mediumtext  null
);