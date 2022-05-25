create table metric_cognitive_complexity(
    id              varchar(50) primary key,
    commit_id     varchar(50),
    changed_cognitive_complexity int,
    system_id int,
    path        varchar(500)
);
