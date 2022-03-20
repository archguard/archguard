drop table if exists metric_checkstyle;
create table metric_checkstyle
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
