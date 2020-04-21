-- for mysql

drop  table if exists bundle;
create table bundle(
        instruction_missed  int,
        instruction_covered int,
        line_missed int,
        line_covered    int,
        branch_missed   int,
        branch_covered  int,
        complexity_missed   int,
        complexity_covered  int,
        method_missed   int,
        method_covered  int,
        class_missed    int,
        class_covered   int,
        bundle_name varchar(2000),
        scan_time bigint,
        primary key(bundle_name,scan_time)
);


drop  table if exists item;
create table item(
        instruction_missed  int,
        instruction_covered int,
        line_missed int,
        line_covered    int,
        branch_missed   int,
        branch_covered  int,
        complexity_missed   int,
        complexity_covered  int,
        method_missed   int,
        method_covered  int,
        class_missed    int,
        class_covered   int,
        item_type varchar(10),
        item_name varchar(2000),
        bundle_name varchar(2000),
        scan_time bigint,
        primary key(item_name,bundle_name,scan_time)
);

