mysql -uroot -e "drop database if exists archguard; create database archguard"
mysql --verbose --database=archguard --user=root -e "source /Users/ygdong/git/code-scanners/scan_git/src/main/resources/db/migration/ddl.sql"
mysql --database=archguard --user=root -e "source /Users/ygdong/git/code-scanners/scan_git/output.sql"