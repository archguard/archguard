create table logic_module
(
    id      varchar(36)                  not null
        primary key,
    name    varchar(128)                 not null,
    members mediumtext                   not null,
    status  varchar(20) DEFAULT 'NORMAL' not null
);

INSERT INTO logic_module (id, name, members, status)
VALUES ('id1', 'dubbo-provider', 'dubbo-provider', 'NORMAL');
INSERT INTO logic_module (id, name, members, status)
VALUES ('id2', 'dubbo-consumer', 'dubbo-consumer', 'HIDE');