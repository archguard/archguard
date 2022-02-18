alter table ClassStatistic modify column `moduleName` mediumtext after `systemId`;
alter table MethodStatistic modify column `moduleName` mediumtext after `systemId`;
alter table ClassStatistic drop column `projectName`;

