ALTER TABLE metric_dataclass
    MODIFY class_id char(36) not null;
ALTER TABLE metric_dataclass
    MODIFY field_id char(36) not null;
ALTER TABLE metric_dataclass collate = utf8mb4_unicode_ci;