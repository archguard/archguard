alter table ProjectInfo
    add username varchar(256) not null default '';

alter table ProjectInfo
    add password varchar(256) not null default '';


UPDATE ProjectInfo t
SET t.username = 'Tom',
    t.password = 'admin123456'
WHERE t.id = 'c06da91f-6742-11ea-8188-0242ac110002';
