alter table _ClassDependences add column `source` varchar(200) not null default '';
alter table _ClassDependences add column `target` varchar(200) not null default '';