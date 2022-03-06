drop table if exists data_class;
create table data_class
(
    id        bigint auto_increment
        primary key,
    class_id  varchar(50) not null,
    field_id  varchar(50) not null,
    system_id bigint      not null,
    constraint class_id_field_id_UNIQUE
        unique (class_id, field_id)
);

create index idx_system_id
    on data_class (system_id);

create index idx_class_id
    on data_class (class_id);