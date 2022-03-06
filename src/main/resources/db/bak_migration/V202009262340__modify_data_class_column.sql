ALTER TABLE data_class
    MODIFY class_id char(36) not null;
ALTER TABLE data_class
    MODIFY field_id char(36) not null;
ALTER TABLE data_class collate = utf8mb4_unicode_ci;