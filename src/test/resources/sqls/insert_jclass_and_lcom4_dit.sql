delete
from code_class
where id is not null;
insert into code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
values ('c1983476-7bd8-4e52-a523-71c4f3f5098e', 1, 'org.apache.dubbo.demo.GreetingService', '2020-06-29 22:03:48',
        '2020-06-29 22:03:48', 'dubbo-demo-interface', null, '512', 0);
insert into code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
values ('c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8', 1, 'org.apache.dubbo.demo.DemoService', '2020-06-29 22:03:48',
        '2020-06-29 22:03:48', 'dubbo-demo-interface', null, 'null', 0);
insert into code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
values ('c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d9', 1, 'org.apache.dubbo.demo.TestService$1', '2020-06-29 23:03:48',
        '2020-06-29 22:03:48', 'test-demo-interface', null, 'null', 0);

delete
from metrics_class
where id is not null;
insert into metrics_class (system_id, class_id, lcom4, dit)
values (1, 'c1983476-7bd8-4e52-a523-71c4f3f5098e', 1, 1);
insert into metrics_class (system_id, class_id, lcom4, dit)
values (1, 'c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8', 3, 3);
insert into metrics_class (system_id, class_id, lcom4, dit)
values (1, 'c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d9', 2, 2);