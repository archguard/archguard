insert into code_ref_class_dependencies (id, system_id, a, b)
values ('1', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '000d417d-9482-4fe4-9411-6df3816d8828');
insert into code_ref_class_dependencies (id, system_id, a, b)
values ('2', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '0b463b79-a8dd-4df7-8dc9-3eee8737c1ad');
insert into code_ref_class_dependencies (id, system_id, a, b)
values ('3', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '0b463b79-a8dd-4df7-8dc9-3eee8737c1ad');
insert into code_ref_class_dependencies (id, system_id, a, b)
values ('4', 8, '000d417d-9482-4fe4-9411-6df3816d8828', '23d7905b-7abe-486b-b968-1a8fbca64e9d');

insert into code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty, is_test)
values ('000d417d-9482-4fe4-9411-6df3816d8828', 8, 'org.springframework.validation.support.BindingAwareConcurrentModel',
        '2020-09-11 01:41:50', '2020-09-11 01:41:50', 'spring-context', null, '1537', 0, 0);
insert into code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty, is_test)
values ('0b463b79-a8dd-4df7-8dc9-3eee8737c1ad', 8, 'org.springframework.messaging.simp.stomp.StompCommand',
        '2020-09-11 01:41:53', '2020-09-11 01:41:53', 'spring-messaging', null, '16433', 0, 0);
insert into code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty, is_test)
values ('23d7905b-7abe-486b-b968-1a8fbca64e9d', 8, 'org.gradle.api.artifacts.Configuration', '2020-09-11 01:41:57',
        '2020-09-11 01:41:57', null, null, '4104', 1, 1);

insert into code_method(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt, is_test)
values   ('2020-09-15 16:22:11','org.apache.dubbo.rpc.support.ProtocolUtils','4106',8,'','dubbo-common','<init>','void','16831861-3797-49c4-8711-4c2bf4d28498','2020-09-15 16:22:11', 0);
insert into code_method(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt, is_test)
values   ('2020-09-15 16:30:33','org.apache.dubbo.rpc.support.ProtocolUtils','4164',8,'','dubbo-common','<init>','void','052c16e9-1999-4829-8710-22a71b6c9ff6','2020-09-15 16:30:33', 0);
insert into code_method(createdAt,clzname,access,system_id,argumenttypes,module,name,returntype,id,updatedAt, is_test)
values   ('2020-09-15 16:30:33','org.apache.dubbo.rpc.support.Config','4164',8,'',null ,'<init>','void','39483920-1999-4829-8710-22a71b6c9ff6','2020-09-15 16:30:33', 0);
