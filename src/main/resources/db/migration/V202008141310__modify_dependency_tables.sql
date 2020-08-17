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

ALTER TABLE `_ClassFields`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `_ClassMethods`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `_ClassParent`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `_MethodCallees`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;

ALTER TABLE `_ClassDependences`
    ADD COLUMN `project_id` bigint(20) NOT NULL AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE;