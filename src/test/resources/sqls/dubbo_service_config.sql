INSERT INTO dubbo_service_config (id, system_id, interface, ref, version, `group`, module_id)
VALUES ('45aca590-994c-4c96-aa0a-5f3df76b8a1e', 1, 'org.apache.dubbo.samples.group.api.GroupService', 'groupAService',
        null, 'groupA', 'ce818d60-f54e-41b7-9851-9b0eb212548d');
INSERT INTO dubbo_service_config (id, system_id, interface, ref, version, `group`, module_id)
VALUES ('dba87323-718c-4046-9184-ae9f250691eb', 1, 'org.apache.dubbo.samples.group.api.GroupService', 'groupBService',
        null, 'groupB', '1debc4fc-5fa9-40ad-9939-b69effd54c0f');
INSERT INTO dubbo_service_config (id, system_id, interface, ref, version, `group`, module_id)
VALUES ('eb650760-389e-4aba-abf7-ac92a377b79b', 1, 'org.apache.dubbo.samples.group.api.GroupService', 'groupCService',
        null, 'groupC', 'a4a8b3c5-a17c-42f7-8733-d9823a6c790a');

INSERT INTO dubbo_module (id, system_id, name, path)
VALUES ('ce818d60-f54e-41b7-9851-9b0eb212548d', 1, 'dubbo-samples-groupA',
        '../dubbo-samples/java/dubbo-samples-groupA');
INSERT INTO dubbo_module (id, system_id, name, path)
VALUES ('1debc4fc-5fa9-40ad-9939-b69effd54c0f', 1, 'dubbo-samples-groupB',
        '../dubbo-samples/java/dubbo-samples-groupB');
INSERT INTO dubbo_module (id, system_id, name, path)
VALUES ('a4a8b3c5-a17c-42f7-8733-d9823a6c790a', 1, 'dubbo-samples-groupC',
        '../dubbo-samples/java/dubbo-samples-groupC');
