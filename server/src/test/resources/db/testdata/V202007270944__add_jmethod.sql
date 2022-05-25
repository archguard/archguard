create table code_method
(
	id char(36) not null,
	access mediumtext not null,
	returntype mediumtext null,
	system_id bigint null,
	name mediumtext not null,
	clzname mediumtext null,
	argumenttypes mediumtext null ,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	module mediumtext null
)