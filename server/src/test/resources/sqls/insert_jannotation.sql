INSERT INTO `code_annotation`(`name`, `system_id`, `targetType`, `id`, `targetId`)
VALUES ('org.junit.Test', 1, 'METHOD', '72f2711f-75a6-4eca-81b6-b01d84af7940', 'ffd83872-e365-46c3-8944-74dda1d91c66'),
       ('org.junit.runner.RunWith', 1, 'TYPE', '5ee38dd4-1910-499c-9a57-53c9aee01357',
        '822127fe-5331-4da8-9e3e-7c9f32550205'),
       ('org.springframework.boot.test.context.SpringBootTest', 1, 'TYPE', 'fac62825-8f45-4e1f-9f7d-b69dfa569ab5',
        '822127fe-5331-4da8-9e3e-7c9f32550205'),
       ('org.springframework.web.bind.annotation.RequestMapping', 1, 'METHOD', 'bbef0b92-103a-47dd-b762-5b8a9bf59dea',
        '42c6568d-75f1-419e-a8dc-dfd9b1469edb'),
       ('org.springframework.web.bind.annotation.RestController', 1, 'TYPE', '8f91de72-9525-44f4-adf6-4546eafb706e',
        '1d2fd434-cfe5-4491-89f9-98ae4622951d'),
       ('org.springframework.boot.autoconfigure.SpringBootApplication', 1, 'TYPE',
        '6ce0337e-bc02-46e8-ae1b-935c61f19bf4',
        'b9d539ea-a55b-4d56-b66f-433f62553666'),
       ('org.springframework.cloud.client.discovery.EnableDiscoveryClient', 1, 'TYPE',
        '46f2a6bd-4c4b-4d59-a28f-964279570c4e', 'b9d539ea-a55b-4d56-b66f-433f62553666'),
       ('org.junit.Test', 1, 'METHOD', '8588e7f9-597f-4078-82d3-91c9a009933b', 'c63e5a7f-f94b-4213-9e44-773ec431d8e3'),
       ('org.junit.runner.RunWith', 1, 'TYPE', '9480a324-a004-46cc-8a86-101e58e2fe7d',
        'b9f508f5-4b7d-484c-8473-df084c23be4a'),
       ('org.springframework.boot.test.context.SpringBootTest', 1, 'TYPE', '8450a772-306a-45d2-92cf-89ee3ff43f08',
        'b9f508f5-4b7d-484c-8473-df084c23be4a'),
       ('org.springframework.boot.autoconfigure.SpringBootApplication', 1, 'TYPE',
        '9ff0e0ba-f2bc-42ca-bed2-a583cc5ec3c7',
        '43efa5b2-54f7-4ea7-a9d1-1fea5e2de89c'),
       ('org.springframework.cloud.netflix.eureka.server.EnableEurekaServer', 1, 'TYPE',
        '9ebf6c3c-3d0d-4736-9e0c-0eb7685f8c62', '43efa5b2-54f7-4ea7-a9d1-1fea5e2de89c'),
       ('org.junit.Test', 1, 'METHOD', '72f7a0cf-54ae-476b-a384-f1069be7317d', 'ffd83872-e365-46c3-8944-74dda1d91c66'),
       ('org.junit.runner.RunWith', 1, 'TYPE', '686e6357-0ca2-4e9a-ae29-7b3cc8b119d6',
        '822127fe-5331-4da8-9e3e-7c9f32550205'),
       ('org.springframework.boot.test.context.SpringBootTest', 1, 'TYPE', '93346022-aed6-409d-99cc-eaba372633da',
        '822127fe-5331-4da8-9e3e-7c9f32550205'),
       ('org.springframework.web.bind.annotation.RequestMapping', 1, 'METHOD', '19781cd0-0b0c-488f-b48f-057384f23d0f',
        '86ae9016-a6a5-4f5e-b53e-25ed7e8c3cea'),
       ('org.springframework.web.bind.annotation.RestController', 1, 'TYPE', '23fde9df-bacb-4a9c-b7f8-cfebe844555c',
        '807de34f-d29e-45ae-beed-9f3bd98ac2f6'),
       ('org.springframework.boot.autoconfigure.SpringBootApplication', 1, 'TYPE',
        'a396e3df-4f24-4248-91a7-186924517996',
        'bf5dd92c-7052-48b1-a0a8-18d6046fbc76'),
       ('org.springframework.cloud.client.discovery.EnableDiscoveryClient', 1, 'TYPE',
        'd84abf6c-1329-486c-bb51-cf9015937b70', 'bf5dd92c-7052-48b1-a0a8-18d6046fbc76'),
       ('org.springframework.cloud.netflix.feign.EnableFeignClients', 1, 'TYPE', '825ef403-4e2d-4a94-9fd7-163058146fce',
        'bf5dd92c-7052-48b1-a0a8-18d6046fbc76'),
       ('org.springframework.web.bind.annotation.RequestMapping', 1, 'METHOD', '89b39964-d06e-4fa2-84e8-a8d0bb3ca287',
        '9fb54b92-a6dd-4864-80f7-a0251ab6a254'),
       ('org.springframework.cloud.netflix.feign.FeignClient', 1, 'TYPE', '3b32c105-137c-4f1b-a064-a209250f729b',
        'a72fd1cd-6ca0-4585-b87c-d5e8b47e33ef');
INSERT INTO `code_annotation_value`(`id`, `system_id`, `annotationId`, `value`, `key`)
VALUES ('b3369842-cd84-4848-8f53-06becd80b0b1', 1, '5ee38dd4-1910-499c-9a57-53c9aee01357',
        'Lorg/springframework/test/context/junit4/SpringRunner;', 'value'),
       ('76696b4b-2a30-4ae4-894b-f03b199c9e21', 1, 'bbef0b92-103a-47dd-b762-5b8a9bf59dea', '["/hello"]', 'value'),
       ('11534c01-35b7-4b71-9366-7bb6c5952004', 1, '9480a324-a004-46cc-8a86-101e58e2fe7d',
        'Lorg/springframework/test/context/junit4/SpringRunner;', 'value'),
       ('a620e393-61b6-46f4-9de6-f38c4d281847', 1, '686e6357-0ca2-4e9a-ae29-7b3cc8b119d6',
        'Lorg/springframework/test/context/junit4/SpringRunner;', 'value'),
       ('6fc68226-cd3a-434a-8003-d30808627f85', 1, '19781cd0-0b0c-488f-b48f-057384f23d0f', '["/hello/{name}"]',
        'value'),
       ('b6b3bf0d-63dd-4f56-8cb8-c30201d8708c', 1, '89b39964-d06e-4fa2-84e8-a8d0bb3ca287', '["/hello"]', 'value'),
       ('4a4c5119-c1b4-4f41-9785-566076033cf8', 1, '3b32c105-137c-4f1b-a064-a209250f729b', '"spring-cloud-producer"',
        'name');
