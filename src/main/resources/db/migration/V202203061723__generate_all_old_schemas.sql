create table metric_checkstyle
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

create table system_configure
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
    on system_configure (system_id);

create table code_annotation
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
    on code_annotation (system_id);

create table code_annotation_value
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
        foreign key (annotationId) references code_annotation (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_project_id
    on code_annotation_value (system_id);

create table code_class
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
    on code_class (name);

create index idx_class_id
    on code_class (id);

create index idx_project_id
    on code_class (system_id);

create table code_field
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
    on code_field (system_id);

create table code_method
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
    on code_method (clzname(255), name(255));

create index JMethod_module_index
    on code_method (module(255));

create index idx_project_id
    on code_method (system_id);

create table system_overview
(
    id             char(36)                            not null
        primary key,
    overview_type  varchar(20)                         not null,
    overview_value text                                not null,
    create_time    timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table system_scanner_configure
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

create table code_ref_class_dependencies
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
    constraint code_ref_class_dependencies_ibfk_1
        foreign key (a) references code_class (id)
            on delete cascade,
    constraint code_ref_class_dependencies_ibfk_2
        foreign key (b) references code_class (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on code_ref_class_dependencies (a);

create index B
    on code_ref_class_dependencies (b);

create index idx_project_id
    on code_ref_class_dependencies (system_id);

create table code_ref_class_fields
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
    constraint code_ref_class_fields_ibfk_1
        foreign key (a) references code_class (id)
            on delete cascade,
    constraint code_ref_class_fields_ibfk_2
        foreign key (b) references code_field (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on code_ref_class_fields (a);

create index B
    on code_ref_class_fields (b);

create index idx_project_id
    on code_ref_class_fields (system_id);

create table code_refs_class_methods
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
    constraint code_refs_class_methods_ibfk_1
        foreign key (a) references code_class (id)
            on delete cascade,
    constraint code_refs_class_methods_ibfk_2
        foreign key (b) references code_method (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on code_refs_class_methods (a);

create index B
    on code_refs_class_methods (b);

create index idx_project_id
    on code_refs_class_methods (system_id);

create table code_ref_class_parent
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
    constraint code_ref_class_parent_ibfk_1
        foreign key (a) references code_class (id)
            on delete cascade,
    constraint code_ref_class_parent_ibfk_2
        foreign key (b) references code_class (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on code_ref_class_parent (a);

create index B
    on code_ref_class_parent (b);

create index idx_project_id
    on code_ref_class_parent (system_id);

create table code_ref_method_callees
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
    constraint code_ref_method_callees_ibfk_1
        foreign key (a) references code_method (id)
            on delete cascade,
    constraint code_ref_method_callees_ibfk_2
        foreign key (b) references code_method (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on code_ref_method_callees (a);

create index B
    on code_ref_method_callees (b);

create index code_ref_method_callees_B_A_index
    on code_ref_method_callees (b, a);

create index idx_project_id
    on code_ref_method_callees (system_id);

create table code_ref_method_fields
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
    constraint code_ref_method_fields_ibfk_1
        foreign key (a) references code_method (id)
            on delete cascade,
    constraint code_ref_method_fields_ibfk_2
        foreign key (b) references code_field (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index A
    on code_ref_method_fields (a);

create index B
    on code_ref_method_fields (b);

create table metric_code_bad_smell
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

create table metric_bad_smell_threshold_suite
(
    id           bigint auto_increment
        primary key,
    suite_name   varchar(255)                        not null,
    is_default   tinyint(1)                          null,
    thresholds   longtext                            null,
    updated_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create table test_coverage_bundle
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

create table scm_change_entry
(
    old_path             varchar(500)                        null,
    new_path             varchar(500)                        null,
    metric_cognitive_complexity int                                 null,
    change_mode          varchar(10)                         null,
    commit_id            varchar(50)                         null,
    system_id            int                                 null,
    commit_time          bigint                              null,
    create_time          timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time          timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
);

create table metric_circular_dependency
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
    on metric_circular_dependency (system_id);

create table code_class_access
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

create table metric_class_coupling
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
    on metric_class_coupling (system_id);

create table metric_class
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
    on metric_class (system_id);

create table metric_cognitive_complexity
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

create table scm_commit_log
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

create table bad_smell_dataclass
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
    on bad_smell_dataclass (class_id);

create index idx_system_id
    on bad_smell_dataclass (system_id);

create table code_framework_dubbo_bean
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
    on code_framework_dubbo_bean (system_id);

create table code_framework_dubbo_module
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
    on code_framework_dubbo_module (system_id);

create table code_framework_dubbo_reference_config
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
    on code_framework_dubbo_reference_config (system_id);

create table code_framework_dubbo_service_config
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
    on code_framework_dubbo_service_config (system_id);

create table report_evaluation
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

create table scm_git_hot_file
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

create table test_coverage_item
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

create table metric_module
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
    on metric_module (system_id);

create table metric_package
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
    on metric_package (system_id);

create table system_quality_gate_profile
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

create table metric_test_bad_smell
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

