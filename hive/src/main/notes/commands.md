# Hive Commands(记得分号结尾！)
1. HDFS
```sql
-- 在 hive cli 命令窗口中查看 hdfs 文件系统
dfs -ls /;
```

1. 数据库操作
```sql
-- 查看hive中的所有数据库：
show databases;

-- 创建数据库（数据库的位置：hdfs://ns1/hive/warehouse/hainiu.db）
create database db_name;
create database if not exists db_name;
create database db_name location '/db_name.db';--指定在数据库在hdfs的位置

-- 查看数据库信息
show create database db_name;

-- 选择使用数据库：
use db_name;

-- 删除数据库
drop database db_name;
drop database if exists db_name; -- 删除空数据库
drop database db_name cascade; -- 强制删除非空数据库

```

1. 表操作
```sql
-- 查看所有的表
show tables;

-- 创建表
create table if not exists table_name(id int,name string);

CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name
[(col_name data_type [COMMENT col_comment], ...)]
[COMMENT table_comment]
[PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]
[CLUSTERED BY (col_name, col_name, ...)
[SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
[ROW FORMAT row_format]
[STORED AS file_format]
[LOCATION hdfs_path]

-- 查询表字段信息
desc table_name;

-- 查询表详细信息
show create table table_name;

-- 插入数据
-- 1. 执行mapruduce，在表里创建一个文件
-- 2. 会在数据库中，生成一个临时表，从临时表把数据导出到hdfs上。
insert into table_name(id, name) values (1, "tom");
```

1. 汇总
```
--创建语句
CREATE DATABASE/SCHEMA, TABLE, VIEW, FUNCTION, INDEX
--删除语句
DROP DATABASE/SCHEMA, TABLE, VIEW, INDEX
--清空语句
TRUNCATE TABLE
--修改语句
ALTER DATABASE/SCHEMA, TABLE, VIEW
--查看创建语句
SHOW DATABASES/SCHEMAS, TABLES, TBLPROPERTIES, PARTITIONS, FUNCTIONS, INDEX[ES], COLUMNS, CREATE TABLE
--查看结构语句
DESCRIBE DATABASE/SCHEMA, table_name, view_name
```