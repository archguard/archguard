drop table if exists system_configure;
create table if not exists system_configure
(
	id char(36) not null,
	type mediumtext not null,
	`key` mediumtext not null,
	value mediumtext not null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	`order` int default 100 not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table system_configure
	add primary key (id);

drop table if exists JClass;
create table if not exists JClass
(
	id char(36) not null,
	name char(255) null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	module mediumtext null,
	loc int null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

create index idx_JClass_name
	on JClass (name);

alter table JClass
	add primary key (id);

drop table if exists JField;
create table if not exists JField
(
	id char(36) not null,
	name mediumtext not null,
	type mediumtext not null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table JField
	add primary key (id);

drop table if exists JMethod;
create table if not exists JMethod
(
	id char(36) not null,
	access mediumtext not null,
	returntype mediumtext null,
	name mediumtext not null,
	clzname mediumtext null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	module mediumtext null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

create index JMethod_clzname_name_index
	on JMethod (clzname(255), name(255));

create index JMethod_module_index
	on JMethod (module(255));

alter table JMethod
	add primary key (id);

drop table if exists JMethodPLProcedure;
create table if not exists JMethodPLProcedure
(
	id char(36) charset utf8 not null,
	clz mediumtext not null,
	method mediumtext not null,
	pkg mediumtext not null,
	`procedure` mediumtext not null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table JMethodPLProcedure
	add primary key (id);

drop table if exists PLProcedure;
create table if not exists PLProcedure
(
	id char(36) not null,
	name mediumtext not null,
	pkg mediumtext null,
	module mediumtext null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table PLProcedure
	add primary key (id);

drop table if exists PLProcedureSqlTable;
create table if not exists PLProcedureSqlTable
(
	id char(36) not null,
	clz mediumtext not null,
	method mediumtext not null,
	`table` mediumtext not null,
	operate mediumtext not null,
	module mediumtext null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table PLProcedureSqlTable
	add primary key (id);

drop table if exists SqlAction;
create table if not exists SqlAction
(
	id char(36) not null,
	clz mediumtext not null,
	method mediumtext not null,
	action mediumtext not null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table SqlAction
	add primary key (id);

drop table if exists SqlCondition;
create table if not exists SqlCondition
(
	id char(36) not null,
	op mediumtext not null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table SqlCondition
	add primary key (id);

drop table if exists SqlConditionValue;
create table if not exists SqlConditionValue
(
	id char(36) not null,
	`table` mediumtext null,
	`column` mediumtext null,
	value mediumtext null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
)
collate=utf8mb4_unicode_ci;

alter table SqlConditionValue
	add primary key (id);

drop table if exists code_ref_class_dependencies;
create table if not exists code_ref_class_dependencies
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint code_ref_class_dependencies_ibfk_1
		foreign key (a) references JClass (id)
			on delete cascade,
	constraint code_ref_class_dependencies_ibfk_2
		foreign key (b) references JClass (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on code_ref_class_dependencies (a);

create index B
	on code_ref_class_dependencies (b);

alter table code_ref_class_dependencies
	add primary key (id);

drop table if exists code_ref_class_fields;
create table if not exists code_ref_class_fields
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint code_ref_class_fields_ibfk_1
		foreign key (a) references JClass (id)
			on delete cascade,
	constraint code_ref_class_fields_ibfk_2
		foreign key (b) references JField (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on code_ref_class_fields (a);

create index B
	on code_ref_class_fields (b);

alter table code_ref_class_fields
	add primary key (id);

drop table if exists code_refs_class_methods;
create table if not exists code_refs_class_methods
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint code_refs_class_methods_ibfk_1
		foreign key (a) references JClass (id)
			on delete cascade,
	constraint code_refs_class_methods_ibfk_2
		foreign key (b) references JMethod (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on code_refs_class_methods (a);

create index B
	on code_refs_class_methods (b);

alter table code_refs_class_methods
	add primary key (id);

drop table if exists code_ref_class_parent;
create table if not exists code_ref_class_parent
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint code_ref_class_parent_ibfk_1
		foreign key (a) references JClass (id)
			on delete cascade,
	constraint code_ref_class_parent_ibfk_2
		foreign key (b) references JClass (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on code_ref_class_parent (a);

create index B
	on code_ref_class_parent (b);

alter table code_ref_class_parent
	add primary key (id);

drop table if exists code_ref_method_callees;
create table if not exists code_ref_method_callees
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint code_ref_method_callees_ibfk_1
		foreign key (a) references JMethod (id)
			on delete cascade,
	constraint code_ref_method_callees_ibfk_2
		foreign key (b) references JMethod (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on code_ref_method_callees (a);

create index B
	on code_ref_method_callees (b);

create index code_ref_method_callees_B_A_index
	on code_ref_method_callees (b, a);

alter table code_ref_method_callees
	add primary key (id);

drop table if exists _PLProcedureCallees;
create table if not exists _PLProcedureCallees
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint _PLProcedureCallees_ibfk_1
		foreign key (a) references PLProcedure (id)
			on delete cascade,
	constraint _PLProcedureCallees_ibfk_2
		foreign key (b) references PLProcedure (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

create index A
	on _PLProcedureCallees (a);

create index B
	on _PLProcedureCallees (b);

alter table _PLProcedureCallees
	add primary key (id);

drop table if exists _PLProcedureSqlAction;
create table if not exists _PLProcedureSqlAction
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
	constraint id_UNIQUE
		unique (id),
	constraint fk__SqlAction
		foreign key (b) references SqlAction (id)
			on delete cascade
)
collate=utf8mb4_unicode_ci;

alter table _PLProcedureSqlAction
	add primary key (id);

drop table if exists _SqlActionConditions;
create table if not exists _SqlActionConditions
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
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
collate=utf8mb4_unicode_ci;

create index A
	on _SqlActionConditions (a);

create index B
	on _SqlActionConditions (b);

alter table _SqlActionConditions
	add primary key (id);

drop table if exists _SqlLeftConditionValue;
create table if not exists _SqlLeftConditionValue
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
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
collate=utf8mb4_unicode_ci;

create index A
	on _SqlLeftConditionValue (a);

create index B
	on _SqlLeftConditionValue (b);

alter table _SqlLeftConditionValue
	add primary key (id);

drop table if exists _SqlRightConditionValue;
create table if not exists _SqlRightConditionValue
(
	id char(36) not null,
	a char(36) null,
	b char(36) null,
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
collate=utf8mb4_unicode_ci;

create index A
	on _SqlRightConditionValue (a);

create index B
	on _SqlRightConditionValue (b);

alter table _SqlRightConditionValue
	add primary key (id);
