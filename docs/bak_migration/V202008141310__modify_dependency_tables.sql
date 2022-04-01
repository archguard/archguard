ALTER TABLE `code_class`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_field`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_method`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_annotation`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_annotation_value`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_ref_class_fields`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_ref_class_methods`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_ref_class_parent`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_ref_method_callees`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_ref_class_dependencies`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;