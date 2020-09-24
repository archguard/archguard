INSERT INTO _ClassDependences (id, system_id, a, b)
VALUES ('1', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '000d417d-9482-4fe4-9411-6df3816d8828');
INSERT INTO _ClassDependences (id, system_id, a, b)
VALUES ('2', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '0b463b79-a8dd-4df7-8dc9-3eee8737c1ad');
INSERT INTO _ClassDependences (id, system_id, a, b)
VALUES ('3', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '0b463b79-a8dd-4df7-8dc9-3eee8737c1ad');
INSERT INTO _ClassDependences (id, system_id, a, b)
VALUES ('4', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '23d7905b-7abe-486b-b968-1a8fbca64e9d');

INSERT INTO JClass (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('000d417d-9482-4fe4-9411-6df3816d8828', 8, 'org.springframework.validation.support.BindingAwareConcurrentModel',
        '2020-09-11 01:41:50', '2020-09-11 01:41:50', 'spring-context', null, '1537', 0);
INSERT INTO JClass (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('0b463b79-a8dd-4df7-8dc9-3eee8737c1ad', 8, 'org.springframework.messaging.simp.stomp.StompCommand',
        '2020-09-11 01:41:53', '2020-09-11 01:41:53', 'spring-messaging', null, '16433', 0);
INSERT INTO JClass (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('23d7905b-7abe-486b-b968-1a8fbca64e9d', 8, 'org.gradle.api.artifacts.Configuration', '2020-09-11 01:41:57',
        '2020-09-11 01:41:57', null, null, '4104', 1);

INSERT INTO JMethod(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt)
VALUES   ('2020-09-15 16:22:11','org.apache.dubbo.rpc.support.ProtocolUtils','4096',8,'','dubbo-common','<init>','void','16831861-3797-49c4-8711-4c2bf4d28498','2020-09-15 16:22:11');
INSERT INTO JMethod(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt)
VALUES   ('2020-09-15 16:30:33','org.apache.dubbo.rpc.support.ProtocolUtils','4164',8,'','dubbo-common','<init>','void','052c16e9-1999-4829-8710-22a71b6c9ff6','2020-09-15 16:30:33');
INSERT INTO JMethod(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt)
VALUES   ('2020-09-15 16:30:33','org.apache.dubbo.rpc.support.Config','4164',8,'',null ,'<init>','void','39483920-1999-4829-8710-22a71b6c9ff6','2020-09-15 16:30:33');
