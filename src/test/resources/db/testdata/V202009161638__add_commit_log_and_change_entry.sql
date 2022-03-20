-- for mysql

drop table if exists git_rep;

drop table if exists scm_commit_log;
create table scm_commit_log(
    id              varchar(50) primary key,
    commit_time     bigint,
    short_msg       varchar(200),
    committer_name      varchar(50),
    committer_email     varchar(100),
    repo_id          varchar(50),
    system_id        int
);



drop table if exists change_entry;
create table change_entry(
    old_path            varchar(200),
    new_path            varchar(200),
    cognitive_complexity       int,
    change_mode           varchar(10),
    commit_id              varchar(50)
);
