alter table `project_info` change `project_name` `system_name` varchar(256) not null;
rename table `project_info` to `system_info`;
alter table `dubbo_service_config` change `project_id` `system_id` bigint not null;
alter table `dubbo_reference_config` change `project_id` `system_id` bigint not null;
alter table `dubbo_module` change `project_id` `system_id` bigint not null;
alter table `dubbo_bean` change `project_id` `system_id` bigint not null;
alter table `system_configure` change `project_id` `system_id` bigint not null;
alter table `JAnnotation` change `project_id` `system_id` bigint not null;
alter table `JClass` change `project_id` `system_id` bigint not null;
alter table `JField` change `project_id` `system_id` bigint not null;
alter table `JMethod` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_dependencies` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_fields` change `project_id` `system_id` bigint not null;
alter table `code_refs_class_methods` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_parent` change `project_id` `system_id` bigint not null;
alter table `code_ref_method_callees` change `project_id` `system_id` bigint not null;
alter table `code_ref_method_fields` change `project_id` `system_id` bigint not null;
alter table `metric_class_coupling` change `project_id` `system_id` bigint not null;
alter table `metrics_class` change `project_id` `system_id` bigint not null;

alter table `JAnnotationValue` change `project_id` `system_id` bigint not null;
alter table `logic_module` change `project_id` `system_id` bigint not null;

