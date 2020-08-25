INSERT INTO logic_module (id, project_id, name, members, status)
VALUES ('id1', 1,  'dubbo-provider', 'dubbo-provider', 'NORMAL');
INSERT INTO logic_module (id, project_id, name, members, status)
VALUES ('id2', 1, 'dubbo-consumer', 'dubbo-consumer', 'HIDE');
INSERT INTO logic_module (id, project_id, name, lg_members, status)
VALUES ('id3', 1, 'dubbo-all', 'dubbo-provider,dubbo-consumer', 'HIDE');
INSERT INTO logic_module (id, project_id, name, members, lg_members, status)
VALUES ('id4', 1, 'dubbo-top', 'dubbo-api.DemoService', 'dubbo-all', 'HIDE');
