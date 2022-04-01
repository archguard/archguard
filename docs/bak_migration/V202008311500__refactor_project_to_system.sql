alter table `project_info` change `project_name` `system_name` varchar(256) not null;
rename table `project_info` to `system_info`;
alter table `code_framework_dubbo_service_config` change `project_id` `system_id` bigint not null;
alter table `code_framework_dubbo_reference_config` change `project_id` `system_id` bigint not null;
alter table `code_framework_dubbo_module` change `project_id` `system_id` bigint not null;
alter table `code_framework_dubbo_bean` change `project_id` `system_id` bigint not null;
alter table `system_configure` change `project_id` `system_id` bigint not null;
alter table `code_annotation` change `project_id` `system_id` bigint not null;
alter table `code_class` change `project_id` `system_id` bigint not null;
alter table `code_field` change `project_id` `system_id` bigint not null;
alter table `code_method` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_dependencies` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_fields` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_methods` change `project_id` `system_id` bigint not null;
alter table `code_ref_class_parent` change `project_id` `system_id` bigint not null;
alter table `code_ref_method_callees` change `project_id` `system_id` bigint not null;
alter table `code_ref_method_fields` change `project_id` `system_id` bigint not null;
alter table `metric_class_coupling` change `project_id` `system_id` bigint not null;
alter table `metric_class` change `project_id` `system_id` bigint not null;

alter table `code_annotation_value` change `project_id` `system_id` bigint not null;
alter table `logic_module` change `project_id` `system_id` bigint not null;

