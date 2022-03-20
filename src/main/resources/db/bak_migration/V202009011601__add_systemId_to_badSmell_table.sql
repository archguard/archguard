alter table metric_code_bad_smell add column `system_id` varchar(36) not null after id;
alter table testBadSmell add column `system_id` varchar(36) not null after id;