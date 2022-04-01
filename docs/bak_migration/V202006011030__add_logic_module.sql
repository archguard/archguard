create table logic_module
(
    `id` varchar(36) primary key,
    `name` varchar(128) not null,
    `members` mediumtext not null
)
collate=utf8mb4_unicode_ci;
