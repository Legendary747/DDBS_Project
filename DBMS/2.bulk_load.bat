echo off

docker exec dbms-mysql-primary-1-1 sh -c "exec mysql -uroot -prootpassword1 -e 'CREATE DATABASE data_center;'"
echo "Database created in DBMS1 MySQL container."

docker exec dbms-mysql-primary-2-1 sh -c "exec mysql -uroot -prootpassword2 -e 'CREATE DATABASE data_center;'"
echo "Database created in DBMS2 MySQL container."

docker exec dbms-mysql-backup-1-1 sh -c "exec mysql -uroot -pbackuppassword1 -e 'CREATE DATABASE data_center;'"
echo "Database created in DBMS3 MySQL container."

docker exec dbms-mysql-backup-2-1 sh -c "exec mysql -uroot -pbackuppassword2 -e 'CREATE DATABASE data_center;'"
echo "Database created in DBMS4 MySQL container."
pause

SET MYSQL_ROOT_PASSWORD=rootpassword1
docker exec dbms-mysql-primary-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/user_beijing.sql"
echo "User SQL file loaded into DBMS1 MySQL container."
docker exec dbms-mysql-primary-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/article_1.sql"
echo "Article SQL file loaded into DBMS1 MySQL container."
docker exec dbms-mysql-primary-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/user_read_1.sql"
echo "Read SQL file loaded into DBMS1 MySQL container."
docker exec dbms-mysql-primary-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/be_read_1.sql"
echo "Be-Read SQL file loaded into DBMS1 MySQL container."
docker exec dbms-mysql-primary-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/popular_rank_1.sql"
echo "Popular-Rank SQL file loaded into DBMS1 MySQL container."

SET MYSQL_ROOT_PASSWORD=rootpassword2
docker exec dbms-mysql-primary-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/user_hongkong.sql"
echo "User SQL file loaded into DBMS2 MySQL container."
docker exec dbms-mysql-primary-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/article_2.sql"
echo "Article SQL file loaded into DBMS2 MySQL container."
docker exec dbms-mysql-primary-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/user_read_2.sql"
echo "Read SQL file loaded into DBMS2 MySQL container."
docker exec dbms-mysql-primary-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/be_read_2.sql"
echo "Be-Read SQL file loaded into DBMS2 MySQL container."
docker exec dbms-mysql-primary-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/popular_rank_2.sql"
echo "Popular-Rank SQL file loaded into DBMS2 MySQL container."

SET MYSQL_ROOT_PASSWORD=backuppassword1
docker exec dbms-mysql-backup-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/user_beijing.sql"
echo "User SQL file BACKED-UP into DBMS3 MySQL container."
docker exec dbms-mysql-backup-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/article_1.sql"
echo "Article SQL file BACKED-UP into DBMS3 MySQL container."
docker exec dbms-mysql-backup-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/be_read_1.sql"
echo "Be-Read SQL file BACKED-UP into DBMS3 MySQL container."
docker exec dbms-mysql-backup-1-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/popular_rank_1.sql"
echo "Popular-Rank SQL file BACKED-UP into DBMS3 MySQL container."

SET MYSQL_ROOT_PASSWORD=backuppassword2
docker exec dbms-mysql-backup-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/user_hongkong.sql"
echo "User SQL file BACKED-UP into DBMS4 MySQL container."
docker exec dbms-mysql-backup-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/article_2.sql"
echo "Article SQL file BACKED-UP into DBMS4 MySQL container."
docker exec dbms-mysql-backup-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/be_read_2.sql"
echo "Be-Read SQL file BACKED-UP into DBMS4 MySQL container."
docker exec dbms-mysql-backup-2-1 sh -c "exec mysql -uroot -p%MYSQL_ROOT_PASSWORD% data_center < /sql_data/popular_rank_2.sql"
echo "Popular-Rank SQL file BACKED-UP into DBMS4 MySQL container."

pause