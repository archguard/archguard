DELETE FROM JClass WHERE id is not null;
INSERT INTO JClass (id, system_id, name, updatedAt, createdAt, module, loc, access)
VALUES ('c1983476-7bd8-4e52-a523-71c4f3f5098e', 1, 'org.apache.dubbo.demo.GreetingService', '2020-06-29 22:03:48',
        '2020-06-29 22:03:48', 'dubbo-demo-interface', null, '512');
INSERT INTO JClass (id, system_id, name, updatedAt, createdAt, module, loc, access)
VALUES ('c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8', 1, 'org.apache.dubbo.demo.DemoService', '2020-06-29 22:03:48',
        '2020-06-29 22:03:48', 'dubbo-demo-interface', null, 'null');
INSERT INTO JClass (id, system_id, name, updatedAt, createdAt, module, loc, access)
VALUES ('c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d9', 1, 'org.apache.dubbo.demo.TestService', '2020-06-29 23:03:48',
        '2020-06-29 22:03:48', 'test-demo-interface', null, 'null');

DELETE FROM class_metrics WHERE id is not null;
INSERT INTO class_metrics (system_id, class_id, lcom4, dit)
VALUES (1, 'c1983476-7bd8-4e52-a523-71c4f3f5098e', 1, 1);
INSERT INTO class_metrics (system_id, class_id, lcom4, dit)
VALUES (1, 'c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d8', 3, 3);
INSERT INTO class_metrics (system_id, class_id, lcom4, dit)
VALUES (1, 'c65ee9c2-dab5-4ebb-8a0f-b8682eddd9d9', 2, 2);