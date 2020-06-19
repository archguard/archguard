-- for mysql

drop table if exists git_rep;
create table git_rep(
    id          bigint primary key,
    rep_path    varchar(300) not null,
    branch      varchar(100) not null
);

drop table if exists commit_log;
create table commit_log(
    id              varchar(50) primary key,
    commit_time     bigint,
    short_msg       varchar(200),
    cmttr_name      varchar(50),
    cmttr_email     varchar(100),
    chgd_entry_cnt  int,            -- 这次提交中，认知复杂度变更的文件的数量
    rep_id          bigint
);



drop table if exists change_entry;
create table change_entry(
    old_path            varchar(200),
    new_path            varchar(200),
    cgntv_cmplxty       int,
    chng_mode           varchar(10),
    cmt_id              varchar(50),
    prvs_cmt_id         varchar(50),    -- previous commit id
    prvs_cgn_cmplxty    int        -- previous cognitive complexity
);