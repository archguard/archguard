alter table code_ref_class_dependencies add column `source` varchar(200) not null default '';
alter table code_ref_class_dependencies add column `target` varchar(200) not null default '';