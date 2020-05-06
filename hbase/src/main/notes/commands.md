# HBase commands
- DDL
    ```bash
    #创建命名空间
    create_namespace '命名空间名'
    #显示所有命名空间
    list_namespace
    #删除命名空间, 在删除一个命名空间时，该命名空间不能包含任何的表，否则会报错
    drop_namespace '命名空间名'
  
    # 创建默认命名空间的表
    create '表名称', '列族名称1','列族名称2','列族名称N'
    # 创建带有命名空间的表
    create '命名空间:表名称', '列族名称1','列族名称2','列族名称N'
    # 列出所有表  
    list
    # 查看表信息
    describe '表名'
    # 把表下线
    disable '表名'
    # 删除表：先下线，再删除
    drop '表名'
    # 查看表是否存在
    exists '表名'
    # 查看表是否在线/不在线,返回boolean
    is_enabled '表名'/ is_disabled '表名'
  
    # 删除my_table表的c_f3 列族
    alter 'my_table',{NAME=>'c_f3',METHOD=>'delete'}
    # 添加列族
    alter 'my_table', 'cf4'
    ```
    
- DML
    ```bash
    # 语法：put <table>,<rowkey>,<family:column>,<value>,<timestamp>
    # 如果不写timestamp，则系统默认
    # 重新put，put时会覆盖原来的数据
    # 基本信息的列族
    put 'my_table','id01', 'c_f1:name','tom'
    put 'my_table','id01', 'c_f1:age','10'
    put 'my_table','id01', 'c_f1:sex','boy'
    #证件信息的列族
    put 'my_table','id01', 'c_f2:cert_no','110125'
    put 'my_table','id01', 'c_f2:cert_type','身份证'
      
    # 语法：get <table>,<rowkey>,[<family:column>,....]
    #获取一行数据
    get 'my_table','id01'
    #获取一行，一个列族的所有数据
    get 'my_table','id01','c_f1'
    #获取一个id，一个列族中一个列的所有数据
    get 'my_table','id01','c_f1:name'
  
    #获取一行的列族
    get 'my_table', 'id01', {COLUMN => 'c_f1'}
    #获取一行的某列族的列
    get 'my_table', 'id01', {COLUMN => 'c_f1:name'}
    #获取一行某列族的列并匹配时间戳
    get 'my_table', 'id01', {COLUMN => 'c_f1:name', TIMESTAMP => 1517890480644} 
    #获取一行中，值为110125的列
    get 'my_table', 'id01', {FILTER => "ValueFilter(=, 'binary:110125')"}
  
    # 语法：scan <table> ,{COLUMNS => [ <family:column>,.... ], LIMIT => num}
    # 扫描全表，大表操作不可取
    scan 'my_table'
    # 获取表中前两行
    scan 'my_table', {LIMIT => 2}
    # 扫描表中指定列族数据
    scan 'my_table', {COLUMNS => 'c_f1'}
    # 扫描表中执行列族中列的数据
    scan 'my_table', {COLUMNS => 'c_f2:cert_no'}
    # 扫描表中值=110225 的数据
    scan 'my_table', FILTER=>"ValueFilter(=,'binary:110225')"
    # 扫描表中 rowkey 范围在 [sartrow，stoprow)
    scan 'my_table', {STARTROW => 'id01', STOPROW => 'id03'}
  
    # 语法：delete <table>, <rowkey>, <family:column>
    # 必须指定列名
    # 会删除执行列的所有版本数据
    # 删除rowkey为id04的值的‘c_f2:cert_type’字段
    delete 'my_table', 'id04',  'c_f2:cert_type'
    # 语法：deleteall <table>, <rowkey>
    # 删除id05行数据
    deleteall 'my_table', 'id05'
    # 清空表：语法： truncate <table>
    truncate 'my_table'
    ```

- Others
    ```bash
    # 查询表中行数
        # 语法：count <table>, {INTERVAL => intervalNum, CACHE => cacheNum}
        # INTERVAL设置多少行显示一次及对应的rowkey，默认1000；
        # CACHE每次去取的缓存区大小，默认是10，调整该参数可提高查询速度
        # 查询表中数据行数
        count 'my_table'
        # 查询表中数据行数，并按照2行显示输出一次
        count 'my_table', {INTERVAL => 2} 
      
        # 计数器操作
        # 语法： incr <tablename>,<rowkey>,<family:column>,long n
        # 给表的指定行指定列族的指定列设置计数器
        # 执行一次加一次
        incr 'my_table', 'app_button1','c_f1:click_volume',1
        # 执行一次加5次（作弊）
        incr 'my_table', 'app_button2','c_f1:click_volume',5
        #获取'id11' 数据
        get 'my_table','app_button1'
        #获取当前count的值
        get_counter 'hainiu_table', 'app_button2', 'c_f1:click_volume'
    
        # 获取多个版本的数据（VERSIONS代表着设置可以查询出来几个版本的数量）
        # 创建多个版本的列族，查询时，加上版本就可以查出来版本
        create 'test_a1',{ NAME =>'cf_1', VERSIONS => 2}
        # 增加多版本列族alter 'test_a1',{ NAME =>'cf_1', VERSIONS => 2}
        put 'test_a1', 'id02','cf_1:name','ccc'
        put 'test_a1', 'id02','cf_1:name','ddd'
        #此时，可以查询出2个版本的数据, 此处查询的数字可以小于等于设置的VERSIONS，即显示最近的几个版本
        get 'test_a1', 'id02', {COLUMN =>'cf_1:name',  VERSIONS => 2}
    ```