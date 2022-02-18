-- for mysql
ALTER TABLE ProjectInfo ADD COLUMN repo_type varchar(20) DEFAULT 'GIT' not null COMMENT '仓库类型';