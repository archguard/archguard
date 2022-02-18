create table MethodStatistic
(
    id char(36) not null,
    packageName mediumtext not null,
    typeName mediumtext not null,
    methodName mediumtext not null,
    `lines` int not null,
    updateAt datetime not null,
    createAt datetime not null,
    constraint MethodStatistic_pk
        primary key (id)
);