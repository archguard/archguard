alter table container_service add column `system_id` bigint not null default 1;
alter table container_resource add column `system_id` bigint not null default 1;
alter table container_demand add column `system_id` bigint not null default 1;