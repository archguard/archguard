#         INSERT INTO metric_estimate (system_id, cost,month,people,name,files,lines, blanks, comment, code, complexity)
#         VALUES (:systemId, :cost, :month, :people, :name, :files, :lines, :blanks, :comment, :code, :complexity)
CREATE TABLE `metric_estimate`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `system_id`  BIGINT         NOT NULL,
    `cost`       DECIMAL(10, 2) NOT NULL,
    `month`      INT            NOT NULL,
    `people`     INT            NOT NULL,
    `name`       VARCHAR(255)   NOT NULL,
    `files`      INT            NOT NULL,
    `lines`      INT            NOT NULL,
    `blanks`     INT            NOT NULL,
    `comment`    INT            NOT NULL,
    `code`       INT            NOT NULL,
    `complexity` INT            NOT NULL
) ENGINE = InnoDB;

