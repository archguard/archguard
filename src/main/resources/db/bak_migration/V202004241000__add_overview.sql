-- for mysql
create table system_overview
(
	id char(36) not null primary key,
	overview_type varchar(20) not null,
	overview_value TEXT not null
);
