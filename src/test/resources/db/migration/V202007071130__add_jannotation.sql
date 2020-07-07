create table JAnnotation
(
    id         varchar(36)  not null
        primary key,
    targetType varchar(255) not null,
    targetId   varchar(36)  not null,
    name       varchar(255) not null
);


create table JAnnotationValue
(
    id           varchar(36)  not null
        primary key,
    annotationId varchar(36)  not null,
    `key`        varchar(255) not null,
    value        text         null,
    constraint JAnnotationValue_ibfk_1
        foreign key (annotationId) references JAnnotation (id)
            on delete cascade
)
