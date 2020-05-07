create table Statistic
(
	id char(36) not null,
	projectName mediumtext not null,
	packageName mediumtext not null,
	typeName mediumtext not null,
	`lines` int not null,
	updateAt datetime not null,
	createAt datetime not null,
	constraint Statistic_pk
		primary key (id)
);
