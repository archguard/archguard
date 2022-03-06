create table CheckStyle
(
    id        char(36)    not null
        primary key,
    system_id varchar(36) not null,
    file      mediumtext  not null,
    source    mediumtext  not null,
    message   mediumtext  not null,
    line      int         not null,
    `column`  int         not null,
    severity  mediumtext  not null
);

create table Configure
(
    id        char(36)                            not null
        primary key,
    system_id bigint                              not null,
    type      mediumtext                          not null,
    `key`     mediumtext                          not null,
    value     mediumtext                          not null,
    updatedAt timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    createdAt timestamp default CURRENT_TIMESTAMP null,
    `order`   int       default 100               not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on Configure (system_id);

create table JAnnotation
(
    id          varchar(36)                         not null
        primary key,
    system_id   bigint                              not null,
    targetType  varchar(255)                        not null,
    targetId    varchar(36)                         not null,
    name        varchar(255)                        not null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on JAnnotation (system_id);

create table JAnnotationValue
(
    id           varchar(36)                         not null
        primary key,
    system_id    bigint                              not null,
    annotationId varchar(36)                         not null,
    `key`        varchar(255)                        not null,
    value        text                                null,
    create_time  timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint JAnnotationValue_ibfk_1
        foreign key (annotationId) references JAnnotation (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on JAnnotationValue (system_id);

create table JClass
(
    id            char(36)    not null
        primary key,
    system_id     bigint      not null,
    name          char(255)   null,
    updatedAt     datetime(3) not null,
    createdAt     datetime(3) not null,
    module        mediumtext  null,
    loc           int         null,
    access        mediumtext  null,
    is_thirdparty tinyint(1)  null,
    is_test       tinyint(1)  null,
    class_name    mediumtext  null,
    package_name  mediumtext  null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create index idx_JClass_name
    on JClass (name);

create index idx_class_id
    on JClass (id);

create index idx_project_id
    on JClass (system_id);

create table JField
(
    id        char(36)    not null
        primary key,
    system_id bigint      not null,
    name      mediumtext  not null,
    type      mediumtext  not null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    clzname   mediumtext  null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on JField (system_id);

create table JMethod
(
    id            char(36)    not null
        primary key,
    system_id     bigint      not null,
    access        mediumtext  not null,
    returntype    mediumtext  null,
    name          mediumtext  not null,
    clzname       mediumtext  null,
    updatedAt     datetime(3) not null,
    createdAt     datetime(3) not null,
    module        mediumtext  null,
    argumenttypes mediumtext  null,
    is_test       tinyint(1)  null,
    loc           int         null,
    package_name  mediumtext  null,
    class_name    mediumtext  null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create index JMethod_clzname_name_index
    on JMethod (clzname(255), name(255));

create index JMethod_module_index
    on JMethod (module(255));

create index idx_project_id
    on JMethod (system_id);

create table JMethodPLProcedure
(
    id          char(36) charset utf8 not null
        primary key,
    clz         mediumtext            not null,
    method      mediumtext            not null,
    pkg         mediumtext            not null,
    `procedure` mediumtext            not null,
    updatedAt   datetime(3)           not null,
    createdAt   datetime(3)           not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create table Overview
(
    id             char(36)                            not null
        primary key,
    overview_type  varchar(20)                         not null,
    overview_value text                                not null,
    create_time    timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table PLProcedure
(
    id        char(36)    not null
        primary key,
    name      mediumtext  not null,
    pkg       mediumtext  null,
    module    mediumtext  null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create table PLProcedureSqlTable
(
    id        char(36)    not null
        primary key,
    clz       mediumtext  not null,
    method    mediumtext  not null,
    `table`   mediumtext  not null,
    operate   mediumtext  not null,
    module    mediumtext  null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create table ScannerConfigure
(
    id        char(36)    not null,
    type      mediumtext  not null,
    `key`     mediumtext  not null,
    value     mediumtext  not null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    constraint id_UNIQUE
        unique (id)
);

create table SqlAction
(
    id        char(36)    not null
        primary key,
    clz       mediumtext  not null,
    method    mediumtext  not null,
    action    mediumtext  not null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create table SqlCondition
(
    id        char(36)    not null
        primary key,
    op        mediumtext  not null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create table SqlConditionValue
(
    id        char(36)    not null
        primary key,
    `table`   mediumtext  null,
    `column`  mediumtext  null,
    value     mediumtext  null,
    updatedAt datetime(3) not null,
    createdAt datetime(3) not null,
    constraint id_UNIQUE
        unique (id)
)
    collate = utf8mb4_unicode_ci;

create table _ClassDependences
(
    id          char(36)                            not null
        primary key,
    system_id   bigint                              not null,
    a           char(36)                            null,
    b           char(36)                            null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint id_UNIQUE
        unique (id),
    constraint _ClassDependences_ibfk_1
        foreign key (a) references JClass (id)
            on delete cascade,
    constraint _ClassDependences_ibfk_2
        foreign key (b) references JClass (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _ClassDependences (a);

create index B
    on _ClassDependences (b);

create index idx_project_id
    on _ClassDependences (system_id);

create table _ClassFields
(
    id          char(36)                            not null
        primary key,
    system_id   bigint                              not null,
    a           char(36)                            null,
    b           char(36)                            null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint id_UNIQUE
        unique (id),
    constraint _ClassFields_ibfk_1
        foreign key (a) references JClass (id)
            on delete cascade,
    constraint _ClassFields_ibfk_2
        foreign key (b) references JField (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _ClassFields (a);

create index B
    on _ClassFields (b);

create index idx_project_id
    on _ClassFields (system_id);

create table _ClassMethods
(
    id          char(36)                            not null
        primary key,
    system_id   bigint                              not null,
    a           char(36)                            null,
    b           char(36)                            null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint id_UNIQUE
        unique (id),
    constraint _ClassMethods_ibfk_1
        foreign key (a) references JClass (id)
            on delete cascade,
    constraint _ClassMethods_ibfk_2
        foreign key (b) references JMethod (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _ClassMethods (a);

create index B
    on _ClassMethods (b);

create index idx_project_id
    on _ClassMethods (system_id);

create table _ClassParent
(
    id          char(36)                            not null
        primary key,
    system_id   bigint                              not null,
    a           char(36)                            null,
    b           char(36)                            null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint id_UNIQUE
        unique (id),
    constraint _ClassParent_ibfk_1
        foreign key (a) references JClass (id)
            on delete cascade,
    constraint _ClassParent_ibfk_2
        foreign key (b) references JClass (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _ClassParent (a);

create index B
    on _ClassParent (b);

create index idx_project_id
    on _ClassParent (system_id);

create table _MethodCallees
(
    id          char(36)                            not null
        primary key,
    system_id   bigint                              not null,
    a           char(36)                            null,
    b           char(36)                            null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint id_UNIQUE
        unique (id),
    constraint _MethodCallees_ibfk_1
        foreign key (a) references JMethod (id)
            on delete cascade,
    constraint _MethodCallees_ibfk_2
        foreign key (b) references JMethod (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _MethodCallees (a);

create index B
    on _MethodCallees (b);

create index _MethodCallees_B_A_index
    on _MethodCallees (b, a);

create index idx_project_id
    on _MethodCallees (system_id);

create table _MethodFields
(
    id          char(36)                            not null
        primary key,
    a           char(36)                            null,
    b           char(36)                            null,
    system_id   bigint                              not null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint id_UNIQUE
        unique (id),
    constraint _MethodFields_ibfk_1
        foreign key (a) references JMethod (id)
            on delete cascade,
    constraint _MethodFields_ibfk_2
        foreign key (b) references JField (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _MethodFields (a);

create index B
    on _MethodFields (b);

create table _PLProcedureCallees
(
    id char(36) not null
        primary key,
    a  char(36) null,
    b  char(36) null,
    constraint id_UNIQUE
        unique (id),
    constraint _PLProcedureCallees_ibfk_1
        foreign key (a) references PLProcedure (id)
            on delete cascade,
    constraint _PLProcedureCallees_ibfk_2
        foreign key (b) references PLProcedure (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _PLProcedureCallees (a);

create index B
    on _PLProcedureCallees (b);

create table _PLProcedureSqlAction
(
    id char(36) not null
        primary key,
    a  char(36) null,
    b  char(36) null,
    constraint id_UNIQUE
        unique (id),
    constraint fk__SqlAction
        foreign key (b) references SqlAction (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create table _SqlActionConditions
(
    id char(36) not null
        primary key,
    a  char(36) null,
    b  char(36) null,
    constraint AB_unique
        unique (a, b),
    constraint id_UNIQUE
        unique (id),
    constraint _SqlActionConditions_ibfk_1
        foreign key (a) references SqlAction (id)
            on delete cascade,
    constraint _SqlActionConditions_ibfk_2
        foreign key (b) references SqlCondition (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _SqlActionConditions (a);

create index B
    on _SqlActionConditions (b);

create table _SqlLeftConditionValue
(
    id char(36) not null
        primary key,
    a  char(36) null,
    b  char(36) null,
    constraint AB_unique
        unique (a, b),
    constraint id_UNIQUE
        unique (id),
    constraint _SqlLeftConditionValue_ibfk_1
        foreign key (a) references SqlCondition (id)
            on delete cascade,
    constraint _SqlLeftConditionValue_ibfk_2
        foreign key (b) references SqlConditionValue (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _SqlLeftConditionValue (a);

create index B
    on _SqlLeftConditionValue (b);

create table _SqlRightConditionValue
(
    id char(36) not null
        primary key,
    a  char(36) null,
    b  char(36) null,
    constraint AB_unique
        unique (a, b),
    constraint id_UNIQUE
        unique (id),
    constraint _SqlRightConditionValue_ibfk_1
        foreign key (a) references SqlCondition (id)
            on delete cascade,
    constraint _SqlRightConditionValue_ibfk_2
        foreign key (b) references SqlConditionValue (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on _SqlRightConditionValue (a);

create index B
    on _SqlRightConditionValue (b);

create table badSmell
(
    id          varchar(100) not null
        primary key,
    system_id   varchar(36)  not null,
    entity_name varchar(200) not null,
    line        int          null,
    description varchar(500) null,
    size        int          null,
    type        varchar(100) not null
);

create table bad_smell_threshold_suite
(
    id           bigint auto_increment
        primary key,
    suite_name   varchar(255)                        not null,
    is_default   tinyint(1)                          null,
    thresholds   longtext                            null,
    updated_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create table bundle
(
    instruction_missed  int          null,
    instruction_covered int          null,
    line_missed         int          null,
    line_covered        int          null,
    branch_missed       int          null,
    branch_covered      int          null,
    complexity_missed   int          null,
    complexity_covered  int          null,
    method_missed       int          null,
    method_covered      int          null,
    class_missed        int          null,
    class_covered       int          null,
    bundle_name         varchar(200) not null,
    scan_time           bigint       not null,
    primary key (bundle_name, scan_time)
);

create table change_entry
(
    old_path             varchar(500)                        null,
    new_path             varchar(500)                        null,
    cognitive_complexity int                                 null,
    change_mode          varchar(10)                         null,
    commit_id            varchar(50)                         null,
    system_id            int                                 null,
    commit_time          bigint                              null,
    create_time          timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time          timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table circular_dependency_metrics
(
    id                  bigint auto_increment
        primary key,
    system_id           bigint                              not null,
    circular_dependency varchar(3000)                       not null,
    type                varchar(255)                        not null,
    create_time         timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time         timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_circular_dependency_metrics_project_id
    on circular_dependency_metrics (system_id);

create table class_access
(
    id           varchar(50)                         not null
        primary key,
    class_id     char(36)                            null,
    is_abstract  tinyint(1)                          null,
    is_interface tinyint(1)                          null,
    is_synthetic tinyint(1)                          null,
    system_id    int                                 null,
    create_time  timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

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
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on class_coupling (system_id);

create table class_metrics
(
    id        bigint auto_increment
        primary key,
    system_id bigint       not null,
    class_id  varchar(255) not null,
    abc       int          null,
    noc       int          null,
    dit       int          null,
    lcom4     int          null,
    fanin     int          null,
    fanout    int          null
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on class_metrics (system_id);

create table cognitive_complexity
(
    id                           varchar(50)                         not null
        primary key,
    commit_id                    varchar(50)                         null,
    changed_cognitive_complexity int                                 null,
    system_id                    int                                 null,
    path                         varchar(500)                        null,
    create_time                  timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time                  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table commit_log
(
    id              varchar(50)                         not null
        primary key,
    commit_time     bigint                              null,
    short_msg       varchar(200)                        null,
    committer_name  varchar(50)                         null,
    committer_email varchar(100)                        null,
    repo_id         varchar(50)                         null,
    system_id       int                                 null,
    create_time     timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time     timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table data_class
(
    id          bigint auto_increment
        primary key,
    class_id    char(36)                            not null,
    field_id    char(36)                            not null,
    system_id   bigint                              not null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint class_id_field_id_UNIQUE
        unique (class_id, field_id)
)
    collate = utf8mb4_unicode_ci;

create index idx_class_id
    on data_class (class_id);

create index idx_system_id
    on data_class (system_id);

create table dubbo_bean
(
    id          varchar(36)                         not null
        primary key,
    system_id   bigint                              not null,
    beanId      varchar(128)                        null,
    implClass   varchar(128)                        null,
    module_id   varchar(36)                         null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on dubbo_bean (system_id);

create table dubbo_module
(
    id          varchar(36)                         not null
        primary key,
    system_id   bigint                              not null,
    name        varchar(128)                        not null,
    path        varchar(512)                        not null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on dubbo_module (system_id);

create table dubbo_reference_config
(
    id          varchar(36)                         not null
        primary key,
    system_id   bigint                              not null,
    referenceId varchar(128)                        not null,
    interface   varchar(128)                        not null,
    version     varchar(128)                        null,
    `group`     varchar(128)                        null,
    module_id   varchar(36)                         null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on dubbo_reference_config (system_id);

create table dubbo_service_config
(
    id          varchar(36)                         not null
        primary key,
    system_id   bigint                              not null,
    interface   varchar(128)                        not null,
    ref         varchar(128)                        not null,
    version     varchar(128)                        null,
    `group`     varchar(128)                        null,
    module_id   varchar(36)                         null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on dubbo_service_config (system_id);

create table evaluationReport
(
    id           varchar(50) not null
        primary key,
    name         varchar(20) null,
    dimensions   text        null,
    comment      text        null,
    improvements text        null,
    createdDate  datetime    null,
    detail       longtext    null
);

create table git_hot_file
(
    system_id      bigint                              not null,
    repo           varchar(256)                        null,
    path           mediumtext                          not null,
    module_name    varchar(1024)                       null,
    class_name     varchar(256)                        null,
    jclass_id      char(36)                            null,
    modified_count int                                 not null,
    create_time    timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table item
(
    instruction_missed  int                                 null,
    instruction_covered int                                 null,
    line_missed         int                                 null,
    line_covered        int                                 null,
    branch_missed       int                                 null,
    branch_covered      int                                 null,
    complexity_missed   int                                 null,
    complexity_covered  int                                 null,
    method_missed       int                                 null,
    method_covered      int                                 null,
    class_missed        int                                 null,
    class_covered       int                                 null,
    item_type           varchar(10)                         null,
    item_name           varchar(500)                        not null,
    bundle_name         varchar(200)                        not null,
    scan_time           bigint                              not null,
    create_time         timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time         timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    primary key (item_name, bundle_name, scan_time)
);

create table logic_module
(
    id          varchar(36)                           not null
        primary key,
    system_id   bigint                                not null,
    name        varchar(128)                          not null,
    members     mediumtext                            null comment '子模块或类成员',
    status      varchar(20) default 'NORMAL'          not null comment '显示状态',
    lg_members  mediumtext                            null comment '逻辑模块成员',
    create_time timestamp   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on logic_module (system_id);

create table method_access
(
    id           varchar(50)                         not null
        primary key,
    method_id    char(36)                            null,
    is_abstract  tinyint(1)                          null,
    is_synthetic tinyint(1)                          null,
    system_id    int                                 null,
    create_time  timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_static    tinyint(1)                          null,
    is_private   tinyint(1)                          null
)
    collate = utf8mb4_unicode_ci;

create table method_metrics
(
    id        bigint auto_increment
        primary key,
    system_id bigint       not null,
    method_id varchar(255) not null,
    fanin     int          null,
    fanout    int          null
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on method_metrics (system_id);

create table module_metrics
(
    id          bigint auto_increment
        primary key,
    system_id   bigint                              not null,
    module_name varchar(255)                        not null,
    fanin       int                                 null,
    fanout      int                                 null,
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on module_metrics (system_id);

create table package_metrics
(
    id           bigint auto_increment
        primary key,
    system_id    bigint                              not null,
    module_name  varchar(255)                        not null,
    package_name varchar(255)                        not null,
    fanin        int                                 null,
    fanout       int                                 null,
    create_time  timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on package_metrics (system_id);

create table quality_gate_profile
(
    id         int auto_increment comment '唯一索引'
        primary key,
    name       varchar(256)                        not null comment '名称',
    config     mediumtext                          not null comment '质量阈配置',
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    created_at timestamp default CURRENT_TIMESTAMP not null comment '创建时间'
)
    collate = utf8mb4_unicode_ci;

create table system_info
(
    id                      bigint auto_increment
        primary key,
    system_name             varchar(256)              not null,
    repo                    varchar(500)              not null,
    repo_type               varchar(20) default 'GIT' not null,
    username                varchar(256)              null,
    password                varchar(256)              null,
    sql_table               text                      null,
    scanned                 varchar(10)               null,
    quality_gate_profile_id bigint                    null,
    updated_time            datetime                  null,
    created_time            datetime                  null,
    threshold_suite_id      bigint                    null,
    branch                  varchar(50)               null
)
    collate = utf8mb4_unicode_ci;

create table testBadSmell
(
    id          varchar(100) not null
        primary key,
    system_id   varchar(36)  not null,
    line        int          null,
    description varchar(500) null,
    file_name   text         null,
    type        varchar(100) not null
);

create table violation
(
    system_id varchar(36)  not null,
    file      varchar(200) null,
    beginline int          null,
    endline   int          null,
    priority  int          null,
    text      varchar(500) null
);

