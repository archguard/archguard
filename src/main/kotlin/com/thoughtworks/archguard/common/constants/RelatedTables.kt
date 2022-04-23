package com.thoughtworks.archguard.common.constants

val RELATED_TABLES = listOf(
    "code_ref_class_dependencies",
    "code_ref_class_fields",
    "code_ref_class_methods",
    "code_ref_class_parent",
    "code_ref_method_callees",
    "code_ref_method_fields",

    "code_class",
    "code_class_access",
    "code_field",
    "code_method",
    "code_annotation",
    "code_annotation_value",
    "method_access",

    "metric_code_bad_smell",
    "metric_checkstyle",
    "metric_class_coupling",
    "metric_class",
    "metric_test_bad_smell",
    "metric_circular_dependency",
    "metric_cognitive_complexity",
    "metric_dataclass",
    "method_metrics",
    "metric_module",
    "metric_package",

    "system_configure",
    "code_framework_dubbo_bean",
    "code_framework_dubbo_module",
    "code_framework_dubbo_reference_config",
    "code_framework_dubbo_service_config",

    "logic_module",
    "violation",
    "scm_commit_log",
    "scm_change_entry",
    "scm_git_hot_file",
    "scm_path_change_count",

    "container_demand",
    "container_resource",
    "container_service",

    "data_code_database_relation",

    "project_composition_dependencies"
)
