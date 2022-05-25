DELETE
FROM code_class
WHERE id is not null;
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('c1983476-7bd8-4e52-a523-71c4f3f5098e', 1, 'org.apache.dubbo.demo.GreetingService', '2020-06-29 22:03:48',
        '2020-06-29 22:03:48', 'dubbo-demo-interface', null, '512', 0);
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8', 1, 'org.apache.dubbo.demo.DemoService', '2020-06-29 22:03:48',
        '2020-06-29 22:03:48', 'dubbo-demo-interface', null, 'null', 0);
