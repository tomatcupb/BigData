# Hive
1. Hive是基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张表，并提供类SQL查询功能。
1. 可以认为Hive是一个封装了hadoop功能的**客户端**（存储是hdfs，计算是MR/Spark，资源调度是Yarn）,从而更简单的使用hadoop做数据处理
1. 优点
    - 操作接口采用类SQL语法，避免了去写MapReduce，减少开发人员的学习成本，提供快速开发的能力（简单、容易上手）
    - Hive 支持用户自定义函数，用户可以根据自己的需求来实现自己的函数
    - 处理大规模数据、实时性要求较低的数据
1. 缺点
    - HQL表达能力有限（迭代式算法无法表达，不擅长数据挖掘）
    - Hive的效率比较低（Hive 调优比较困难，粒度较粗）
1. 架构
    1. 用户接口：Client：CLI（hive shell）、JDBC/ODBC(java访问hive)、WEBUI（浏览器访问hive）
    1. 元数据：Metastore
        - 这个组件存取Hive的元数据，Hive的元数据存储在关系数据库里，Hive支持的关系数据库有Derby和Mysql,推荐使用MySQL存储Metastore
        - 作用：客户端连接metastore服务，metastore再去连接数据库(Derby/Mysql)来存取元数据。
        - 元数据包括：表名、表所属的数据库（默认是default）、表的拥有者、列/分区字段、表的类型（是否是外部表）、表的数据所在目录等
    1. hadoop: hdfs/yarn/MR/Spark
    1. 驱动器： Driver
        - 解析器（SQLParser）：将SQL字符串转换成抽象语法树AST，这一步一般都用第三方工具库完成，比如antlr；对AST进行语法分析，比如表是否存在、字段是否存在、SQL语义是否有误。
        - 编译器（PhysicalPlan）：将AST编译生成逻辑执行计划
        - 优化器（QueryOptimizer）：对逻辑执行计划进行优化
        - 执行器（Execution）：把逻辑执行计划转换成可以运行的物理计划。对于Hive来说，就是MR/Spark
    ![hive架构](hive_structure.png)
        
1. Hive查询的执行过程
    1. Execute Query：hive界面如命令行或Web UI将查询发送到Driver(任何数据库驱动程序如JDBC、ODBC,等等)来执行。
    1. Get Plan:Driver根据查询编译器解析query语句,验证query语句的语法,查询计划或者查询条件。
    1. Get Metadata：编译器将元数据请求发送给Metastore(数据库)。
    1. Send Metadata：Metastore将元数据作为响应发送给编译器。
    1. Send Plan：编译器检查要求和重新发送Driver的计划。到这里,查询的解析和编译完成。
    1. Execute Plan:Driver将执行计划发送到执行引擎。
        1. Execute Job:hadoop内部执行的是mapreduce工作过程,任务执行引擎发送一个任务到资源管理节点(resourcemanager)，资源管理器分配该任务到任务节点，由任务节点上开始执行mapreduce任务。
        1. Metadata Ops：在执行引擎发送任务的同时,对hive的元数据进行相应操作。
    1. Fetch Result：执行引擎接收数据节点(data node)的结果。
    1. Send Results:执行引擎发送这些合成值到Driver。
    1. Send Results：Driver将结果发送到hive接口。
    ![hive查询执行过程](hive_execution.png)

1. Hive与数据库的比较
    - 查询语言
    - 数据存储位置
    - 数据更新：数据仓库的内容是读多写少的，不建议对数据的改写；数据库的数据经常修改更新
    - 执行
    - 执行延迟
    - 可扩展性
    - 数据规模
    - (Hive从0.7.0版本开始加入了索引!!)索引：hive不建立索引，而由于MapReduce的引入，Hive可以并行访问数据，因此即使没有索引，对于大数据量的访问，Hive仍然可以体现出优势。

1. hive的三种模式
    - 使用内置的derby数据库做元数据的存储，操作derby数据库做元数据的管理
    - 本地模式:使用mysql做元数据的存储，操作mysql数据库做元数据的管理
    - 远程模式:使用mysql做元数据的存储，使用metastore服务做元数据的管理(企业推荐)

1. hive的数据组织
    - 数据库Databases ，表Tables ，分区 Partitions，桶 Buckets

1. 常用命令
    - [hive常用命令](commands.md)

1. 数据类型
    - 基本数据类型
    - 集合数据类型
    - 日期与时间戳

1. 各种表
    - 内部表与外部表

    - 分区表
        - 动态分区

    - AVRO表

    - ORC表

    - Bucket（桶）表

1. 视图操作

1. 数据加载
    - load加载数据
    - select加载

1. 数据导出
    - 将数据写入一个文件
    - 将数据写入多个文件
    -  hive -e 命令 导出

1. HQL语法

1. 窗口函数

1. 自定义函数

1. hive的数据倾斜场景及处理方式