INSERT INTO logic_module (id, name, members, status)
VALUES ('id1', 'dubbo-provider', 'dubbo-provider', 'NORMAL');
INSERT INTO logic_module (id, name, members, status)
VALUES ('id2', 'dubbo-consumer', 'dubbo-consumer', 'HIDE');
INSERT INTO logic_module (id, name, lg_members, status)
VALUES ('id3', 'dubbo-all', 'dubbo-provider,dubbo-consumer', 'HIDE');
INSERT INTO logic_module (id, name, members, lg_members, status)
VALUES ('id4', 'dubbo-top', 'dubbo-api.DemoService', 'dubbo-all', 'HIDE');