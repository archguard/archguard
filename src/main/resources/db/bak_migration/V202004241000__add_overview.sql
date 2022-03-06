-- for mysql
create table Overview
(
	id char(36) not null primary key,
	overview_type varchar(20) not null,
	overview_value TEXT not null
);
