alter table scm_change_entry
    add column `committer` varchar(36) not null default '';
alter table scm_change_entry
    add column `line_added` int not null default 0;
alter table scm_change_entry
    add column `line_deleted` int not null default 0;
