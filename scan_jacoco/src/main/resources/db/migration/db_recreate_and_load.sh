mysql --verbose --database=archguard --user=root -e "source ddl.sql"
mysql --database=archguard --user=root -e "source ../../../../../output.sql"