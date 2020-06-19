-- for mysql
create table evaluationReport(
        id varchar(50) not null primary key,
        name varchar(20),
        dimensions TEXT ,
        comment TEXT,
        improvements TEXT ,
        createdDate DATETIME
);
