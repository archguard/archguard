create table class_coupling
(
    id                bigint auto_increment
        primary key,
    system_id         bigint        not null,
    class_id          varchar(255)  not null,
    inner_fan_in      int           not null,
    inner_fan_out     int           not null,
    outer_fan_in      int           not null,
    outer_fan_out     int           not null,
    inner_instability decimal(8, 4) not null,
    inner_coupling    decimal(8, 4) not null,
    outer_instability decimal(8, 4) not null,
    outer_coupling    decimal(8, 4) not null
);