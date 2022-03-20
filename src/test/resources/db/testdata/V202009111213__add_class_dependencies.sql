create table code_class_dependencies
(
    id        char(36) not null,
    system_id bigint   not null,
    a         char(36) null,
    b         char(36) null
);