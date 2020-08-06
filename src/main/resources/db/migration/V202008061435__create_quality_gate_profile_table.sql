CREATE TABLE quality_gate_profile (
    id        CHAR(36)     NOT NULL COMMENT '唯一索引',
    name      VARCHAR(256) NOT NULL COMMENT '名称',
    config    MEDIUMTEXT   NOT NULL COMMENT '质量阈配置',
    updatedAt DATETIME(3)  NOT NULL COMMENT '更新时间',
    createdAt DATETIME(3)  NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;
