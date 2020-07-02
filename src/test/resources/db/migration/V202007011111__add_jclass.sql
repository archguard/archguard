create table JClass
(
    id        char(36)    not null,
    name      char(255)   null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    module    mediumtext  null,
    loc       int         null,
    constraint id_UNIQUE
        unique (id)
);