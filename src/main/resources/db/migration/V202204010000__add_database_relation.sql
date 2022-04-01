CREATE TABLE `data_code_database_relation`
(
    `id`            VARCHAR(36)    NOT NULL PRIMARY KEY,
    `package_name`  VARCHAR(1024)  NOT NULL,
    `class_name`    VARCHAR(1024)  NOT NULL,
    `function_name` VARCHAR(1024)  NOT NULL,
    `tables`        VARCHAR(2048)  NOT NULL,
    `sqls`          TEXT(32768) NOT NULL
);
