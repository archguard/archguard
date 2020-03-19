#!/bin/bash

PRO_DIR=/Users/ygdong/git/code-scanners/scan_git
REMOTE=jenkin@archguard-pipeline.southeastasia.cloudapp.azure.com
WDR=/home/jenkin/sampleData4Git/
DDL=$WDR/ddl.sql
DML=$WDR/output.sql

ssh $REMOTE 'mkdir $WDR'

scp $PRO_DIR/src/main/resources/db/migration/ddl.sql   $REMOTE:$DDL
scp $PRO_DIR/output.sql   $REMOTE:$DML

ssh $REMOTE <<EOF
mysql -uroot -P13306 -Ddefault@default --default-character-set=utf8  -pprisma -h127.0.0.1  -e "source $DDL"
mysql -uroot -P13306 -Ddefault@default --default-character-set=utf8  -pprisma -h127.0.0.1  -e "source $DML"
rm -rf $WDR
EOF
