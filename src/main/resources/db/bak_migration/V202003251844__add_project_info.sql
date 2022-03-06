-- for mysql
drop table if exists ProjectInfo;
create table ProjectInfo(
    id   varchar(100) primary key ,
    name    varchar(100) not null,
    repo    varchar(500) not null
);