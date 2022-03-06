drop table if exists CheckStyle;
create table CheckStyle
(
	id char(36) not null,
	file mediumtext not null,
	source mediumtext not null,
	message mediumtext not null,
	line int not null,
	`column` int not null,
	severity mediumtext not null,
	constraint CheckStyle_pk
		primary key (id)
);
