drop table if exists metric_dataclass;
create table metric_dataclass
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
    on metric_dataclass (system_id);

create index idx_class_id
    on metric_dataclass (class_id);