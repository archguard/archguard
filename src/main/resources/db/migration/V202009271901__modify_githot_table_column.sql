ALTER TABLE git_hot_file
    MODIFY module_name varchar(1024) null;
ALTER TABLE git_hot_file
    MODIFY repo varchar(256) null;
ALTER TABLE git_hot_file
    MODIFY class_name varchar(256) null;