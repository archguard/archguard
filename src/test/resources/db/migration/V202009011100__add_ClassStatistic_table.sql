drop table Statistic;
create table ClassStatistic
(
    id          char(36)   not null
        primary key,
    systemId    char(36)   not null,
    moduleName  mediumtext null,
    packageName mediumtext not null,
    typeName    mediumtext not null,
    `lines`     int        not null,
    updateAt    datetime   not null,
    createAt    datetime   not null,
    fanin       int        not null,
    fanout      int        not null
);