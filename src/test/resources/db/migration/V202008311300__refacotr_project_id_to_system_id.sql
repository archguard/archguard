alter table `logic_module`
    change `project_id` `system_id` bigint not null;
alter table `JClass`
    change `project_id` `system_id` bigint not null;
alter table `dubbo_service_config`
    change `project_id` `system_id` bigint not null;
alter table `dubbo_reference_config`
    change `project_id` `system_id` bigint not null;
alter table `dubbo_module`
    change `project_id` `system_id` bigint not null;
alter table `dubbo_bean`
    change `project_id` `system_id` bigint not null;
alter table JAnnotation
    add `system_id` bigint not null;
alter table JAnnotationValue
    add `system_id` bigint not null;
