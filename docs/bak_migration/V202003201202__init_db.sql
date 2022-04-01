create table metric_code_bad_smell
(
    id varchar(100) primary key ,
    entity_name varchar(100) not null,
    line integer,
    description varchar(500),
    size integer,
    type varchar(100) not null
);