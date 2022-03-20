create table metric_test_bad_smell
(
    id varchar(100) primary key ,
    line integer,
    description varchar(500),
    file_name varchar(100),
    type varchar(100) not null
);