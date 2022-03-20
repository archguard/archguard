ALTER TABLE `JClass`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `JField`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `JMethod`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `JAnnotation`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `JAnnotationValue`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_ref_class_fields`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `code_refs_class_methods`
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