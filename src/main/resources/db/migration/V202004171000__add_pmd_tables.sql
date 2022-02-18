drop table if exists violation;

create table violation(
    `file`  varchar(200),
    beginline  int,
    endline     int,
    priority    int,
    `text`      varchar(500)
);