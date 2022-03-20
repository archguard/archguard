alter table JField add column `clzname` mediumtext;
drop table if exists code_ref_method_fields;
create table if not exists code_ref_method_fields
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	project_id bigint   not null,
	constraint id_UNIQUE
		unique (id),
	constraint code_ref_method_fields_ibfk_1
		foreign key (a) references JMethod (id)
			on delete cascade,
	constraint code_ref_method_fields_ibfk_2
		foreign key (b) references JField (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on code_ref_method_fields (a);

create index B
	on code_ref_method_fields (b);

alter table code_ref_method_fields
	add primary key (id);
