INSERT INTO dubbo_service_config (id, interface, ref, version, `group`, module_id)
VALUES ('45aca590-994c-4c96-aa0a-5f3df76b8a1e', 'org.apache.dubbo.samples.group.api.GroupService', 'groupAService',
        'null', 'groupA', 'ce818d60-f54e-41b7-9851-9b0eb212548d');
INSERT INTO dubbo_module (id, name, path)
VALUES ('ce818d60-f54e-41b7-9851-9b0eb212548d', 'dubbo-samples-group', '../dubbo-samples/java/dubbo-samples-group');