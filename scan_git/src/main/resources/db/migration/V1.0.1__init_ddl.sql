-- for mysql


create table GitRepository(
    id bigint primary key,
    rep_path  varchar(300) not null,
    branch  varchar(100) not null
);

create table CommitLog(
    id varchar(50) primary key,
    commit_time  bigint,
    shortMessage varchar(200),
    committer_name varchar(50),
    committer_email varchar(100),
    rep_id bigint
);


create table ChangeEntry(
    old_path varchar(200),
    new_path varchar(200),
    cognitiveComplexity  int,
    mode  varchar(10),
    commit_id varchar(50)
);



