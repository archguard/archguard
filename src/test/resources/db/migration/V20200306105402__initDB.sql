create table JMethod
(
    id         char(36)    not null,
    access     mediumtext  not null,
    returntype mediumtext  null,
    name       mediumtext  not null,
    clzname    mediumtext  null,
    updatedAt  datetime(3) not null,
    createdAt  datetime(3) not null,
    module     mediumtext  null
);

alter table JMethod
    add primary key (id);

create table _MethodCallees
(
    id char(36) not null,
    a  char(36) null,
    b  char(36) null,
    constraint _MethodCallees_ibfk_1
        foreign key (a) references JMethod (id)
            on delete cascade,
    constraint _MethodCallees_ibfk_2
        foreign key (b) references JMethod (id)
            on delete cascade
);

alter table _MethodCallees
    add primary key (id);

