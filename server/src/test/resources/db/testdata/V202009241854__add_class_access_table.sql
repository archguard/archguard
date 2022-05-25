create table code_class_access(
    id           varchar(50) primary key,
    class_id     varchar(50),
    is_abstract boolean,
    is_interface boolean,
    is_synthetic boolean,
    system_id   int
);
create table method_access(
    id           varchar(50) primary key,
    method_id     varchar(50),
    is_abstract boolean,
    is_synthetic boolean,
    system_id   int
);
