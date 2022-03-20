-- for mysql
create table report_evaluation(
        id varchar(50) not null primary key,
        name varchar(20),
        dimensions TEXT ,
        comment TEXT,
        improvements TEXT ,
        createdDate DATETIME
);
