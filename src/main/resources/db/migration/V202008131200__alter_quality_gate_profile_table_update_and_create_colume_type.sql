DROP TABLE IF EXISTS quality_gate_profile;
CREATE TABLE quality_gate_profile
(
    id         INT(11) AUTO_INCREMENT COMMENT '唯一索引',
    name       VARCHAR(256) NOT NULL COMMENT '名称',
    config     MEDIUMTEXT   NOT NULL COMMENT '质量阈配置',
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;
