drop table if exists class_access;
create table class_access(
    id           varchar(50) primary key,
    class_id     char(36),
    is_abstract boolean,
    is_interface boolean,
    is_synthetic boolean,
    system_id   int
)
collate=utf8mb4_unicode_ci;

drop table if exists method_access;
create table method_access(
    id           varchar(50) primary key,
    method_id     char(36),
    is_abstract boolean,
    is_synthetic boolean,
    system_id   int
)
collate=utf8mb4_unicode_ci;
