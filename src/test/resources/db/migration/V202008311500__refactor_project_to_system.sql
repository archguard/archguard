alter table `project_info` change `project_name` `system_name` varchar(256) not null;
alter table  `project_info` rename to `system_info`;

update `system_info` set `system_name` = 'systemName1' where id = 1;
