alter table metric_checkstyle add column `system_id` varchar(36) not null after id;
alter table violation add column `system_id` varchar(36) not null first;