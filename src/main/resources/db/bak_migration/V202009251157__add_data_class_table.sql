create table bad_smell_dataclass
(
    id        varchar(50) primary key,
    class_id  varchar(50) not null,
    field_id  varchar(50) not null,
    system_id bigint      not null,
    constraint id_UNIQUE
        unique (id),
    constraint class_id_field_id_UNIQUE
        unique (class_id, field_id)
);

create index idx_system_id
    on bad_smell_dataclass (system_id);

create index idx_class_id
    on bad_smell_dataclass (class_id);