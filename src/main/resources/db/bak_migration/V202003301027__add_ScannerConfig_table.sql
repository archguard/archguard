create table system_scanner_configure
(
	id char(36) not null,
	type mediumtext not null,
	`key` mediumtext not null,
	value mediumtext not null,
	updatedAt datetime(3) not null,
	createdAt datetime(3) not null,
	constraint id_UNIQUE
		unique (id)
);
