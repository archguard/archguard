alter table system_info add column `code_path` varchar(50) not null default 'src';
alter table system_info add column `build_command` varchar(50);