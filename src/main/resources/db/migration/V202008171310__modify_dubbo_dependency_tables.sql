ALTER TABLE `dubbo_module`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;
