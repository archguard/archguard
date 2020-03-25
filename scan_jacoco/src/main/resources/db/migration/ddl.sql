-- for mysql

drop  table if exists coverage;
create table coverage(
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
        project varchar(50)
)


