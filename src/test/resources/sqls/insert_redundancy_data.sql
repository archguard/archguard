DELETE FROM code_class WHERE 1=1;
DELETE FROM code_class_access WHERE 1=1;
DELETE FROM code_method WHERE 1=1;
DELETE FROM method_access WHERE 1=1;


INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('000d417d-9482-4fe4-9411-6df3816d8828', 8, 'org.springframework.validation.support.BindingAwareConcurrentModel',
        '2020-09-11 01:41:50', '2020-09-11 01:41:50', 'spring-context', null, '1537', 0);
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('0b463b79-a8dd-4df7-8dc9-3eee8737c1ad', 8, 'org.springframework.messaging.simp.stomp.StompCommand',
        '2020-09-11 01:41:53', '2020-09-11 01:41:53', 'spring-messaging', null, '16433', 0);
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('23d7905b-7abe-486b-b968-1a8fbca64e9d', 8, 'org.gradle.api.artifacts.Configuration', '2020-09-11 01:41:57',
        '2020-09-11 01:41:57', null, null, '4104', 1);

INSERT INTO code_class_access(id, class_id, is_abstract, is_interface, is_synthetic, system_id)
VALUES ('1', '000d417d-9482-4fe4-9411-6df3816d8828', false , false , false , 8);
INSERT INTO code_class_access(id, class_id, is_abstract, is_interface, is_synthetic, system_id)
VALUES ('2', '0b463b79-a8dd-4df7-8dc9-3eee8737c1ad', false , false , false , 8);

INSERT INTO code_method(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt)
VALUES   ('2020-09-15 16:22:11','org.springframework.validation.support.BindingAwareConcurrentModel','4096',8,'','spring-context','<init>','void',
'16831861-3797-49c4-8711-4c2bf4d28498','2020-09-15 16:22:11');
INSERT INTO code_method(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt)
VALUES   ('2020-09-15 16:30:33','org.springframework.messaging.simp.stomp.StompCommand','4164',8,'','spring-context','<init>','void',
'052c16e9-1999-4829-8710-22a71b6c9ff6','2020-09-15 16:30:33');
INSERT INTO code_method(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt)
VALUES   ('2020-09-15 16:30:33','org.apache.dubbo.rpc.support.Config','4164',8,'',null ,'<init>','void',
'39483920-1999-4829-8710-22a71b6c9ff6','2020-09-15 16:30:33');

INSERT INTO method_access(id, method_id, is_abstract, is_synthetic, system_id)
VALUES ('1', '16831861-3797-49c4-8711-4c2bf4d28498', false , true  , 8);
INSERT INTO method_access(id, method_id, is_abstract, is_synthetic, system_id)
VALUES ('2', '052c16e9-1999-4829-8710-22a71b6c9ff6', true , false  , 8);
