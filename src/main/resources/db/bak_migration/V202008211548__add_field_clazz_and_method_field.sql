alter table JField add column `clzname` mediumtext;
drop table if exists _MethodFields;
create table if not exists _MethodFields
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	project_id bigint   not null,
	constraint id_UNIQUE
		unique (id),
	constraint _MethodFields_ibfk_1
		foreign key (a) references JMethod (id)
			on delete cascade,
	constraint _MethodFields_ibfk_2
		foreign key (b) references JField (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on _MethodFields (a);

create index B
	on _MethodFields (b);

alter table _MethodFields
	add primary key (id);
