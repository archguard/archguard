create table ScannerConfigure
(
	id char(36) not null,
	type mediumtext not null,
	`key` mediumtext not null,
	value mediumtext not null,
	updatedAt datetime not null,
	createdAt datetime not null,
	primary key (id)
);
