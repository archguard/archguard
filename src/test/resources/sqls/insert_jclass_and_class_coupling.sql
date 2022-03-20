INSERT INTO metrics_class (id, system_id, class_id, abc, noc, dit, lcom4, fanin, fanout)
VALUES (13174, 6, 'ffaa84b1-b849-43f1-ab27-3f70d23057e1', 1, 0, 0, 0, 1, 0);
INSERT INTO metrics_class (id, system_id, class_id, abc, noc, dit, lcom4, fanin, fanout)
VALUES (13173, 6, 'ff5b0ec2-23d2-439a-9c7f-5075f84a8861', 3, 0, 1, 0, 4, 0);
INSERT INTO metrics_class (id, system_id, class_id, abc, noc, dit, lcom4, fanin, fanout)
VALUES (13172, 6, 'fe8f5fd7-d3d5-4787-9ad1-da227b3c1a7c', 1, 0, 0, 0, 1, 0);
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('ffaa84b1-b849-43f1-ab27-3f70d23057e1', 6, 'org.apache.dubbo.samples.consul.api.DemoService',
        '2020-09-07 11:14:26', '2020-09-07 11:14:26', 'dubbo-samples-consul', null, '1537', 0);
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('ff5b0ec2-23d2-439a-9c7f-5075f84a8861', 6, 'org.apache.dubbo.samples.configcenter.api.DemoService',
        '2020-09-07 11:14:26', '2020-09-07 11:14:26', 'dubbo-samples-configcenter-xml', null, '1537', 0);
INSERT INTO code_class (id, system_id, name, updatedAt, createdAt, module, loc, access, is_thirdparty)
VALUES ('fe8f5fd7-d3d5-4787-9ad1-da227b3c1a7c', 6, 'org.apache.dubbo.samples.direct.api.DirectService',
        '2020-09-07 11:14:25', '2020-09-07 11:14:25', 'dubbo-samples-direct', null, '1537', 0);