# HBase
- 简介
    - Apache HBase™ is the Hadoop database, a distributed, scalable, big data store.
    - HBase是一个分布式的、可扩展的大数据存储Hadoop NoSQL数据库。
    
- 数据单元
    - row key：确定唯一的一行
    - column family：由若干column组成
    - column：表的字段
    - cell：存储了实际的值或数据
    - (Version Number：类型为Long，默认值是系统时间戳Timestamp，可由用户自定义;用于标记同一份数据的不同版本)
    - ![HBase数据单元](hbase_store_unit.PNG)
    
- 架构组成
    - HBase采用Master/Slave架构搭建集群，它隶属于Hadoop生态系统，由以下类型节点组成：HMaster节点、HRegionServer节点、ZooKeeper集群，而在底层，它将数据存储于HDFS中，因而涉及到HDFS的NameNode、DataNode等，总体结构如下：
    - ![HBase架构](hbase_structure.jpg)
    - Client
        - 使用HBase RPC机制与HMaster和HRegionServer进行通信；
        - Client与HMaster进行通信,进行管理类操作；
        - Client与HRegionServer进行通信,进行数据读写类操作；
    - HMaster: HMaster没有单点问题，HBase中可以启动多个HMaster，通过ZK保证总有一个Master在运行。HMaster主要负责Table和Region的管理工作：
        - 对于表的操作： create, delete, alter
        - 管理HRegionServer的负载均衡，调整Region分布
        - Region Split后，负责新Region的分布
        - 在HRegionServer停机后，负责失效HRegionServer上Region的迁移
    - HRegionServer：HBase中最核心的模块,包含：Write-Ahead logs，HFile，Store，MemStore，Region
        - 对于数据的操作： get, put, delete；
        - 维护region(splitRegion、 compactRegion)，处理对这些region的IO请求
        - RegionServer负责切分在运行过程中变得过大的region
    - Zookeeper
        - ZooKeeper为HBase集群提供协调服务，它管理着HMaster和HRegionServer的状态(available/alive等)
            - 保证集群中只有一个HMaster，会在它们宕机时通知给HMaster，从而HMaster可以实现HMaster之间的故障转移
            - 实时监控HRegionServer的上线和下线信息，并实时通知给HMaster
        - 存储HBase的Meta Table(hbase:meta)的位置，Meta Table表存储了集群中所有用户HRegion的位置信息，且不能split

- HRegionServer组成
    - Write-Ahead logs：
        - HBase的修改记录，当对HBase读写数据的时候，数据不是直接写进磁盘，它会在内存中保留一段时间（时间以及数据量阈值可以设定）。
        - 如果宕机，把数据保存在内存中会引起数据丢失，为了解决这个问题，数据会先写在一个叫做Write-Ahead logfile的文件中，然后再写入内存中。
    - Store：HFile存储在Store中，一个Store对应HBase表中的一个列族
    - HFile：这是在磁盘上保存原始数据的实际的物理文件，是实际的存储文件。(HFILE文件是按**单列**和**row**存储成一行数据的，包括列的名称、版本值、是否被删除)
    - StoreFile: 保存实际数据的物理文件， StoreFile 以 HFile 的形式存储在 HDFS 上。每个 Store 会有一个或多个 StoreFile（HFile），数据在每个 StoreFile 中都是有序的。
    - MemStore：写缓存。它存储尚未写入磁盘的新数据，并会在数据写入磁盘之前对其进行排序。每个Region上的每个列族都有一个MemStore。
    - BlockCache：读缓存。它将频繁读取的数据存储在内存中，如果存储不足，它将按照 最近**最少使用原则**清除多余的数据。
    - Region：Hbase表的分片，HBase表会根据RowKey值被切分成不同的region存储在RegionServer中，在一个RegionServer中可以有多个不同的region
    - ![HRegionServer组成](ReginServer.png)
    
- Meta Table
    - hbase:meta表中，保存了每个表的region地址，还有一些其他信息，例如region的名字，HRegionInfo,服务器的信息。
    - hbase:meta表中每一行对应一个单一的region。
    - hbase:meta表的rowKey:([table],[region start key],[region id])
        - 第二分隔符前存的是region的第一个rowKey，这里两个需要注意:
            1. 如果这个地方为空的话，表明这是table的第一个region。并且如果一个region中startkey和endkey都为空的为，表明这个table只有一个region。
            2. 在mata表中，startkey 靠前的region会排在startkey 靠后的region前面。（Hbase中的keys是按照字段顺序来排序的）
        - region id就是region的id,通常来说就是region创建的时候的timestamp
    - regioninfo 是HRegionInfo的序列化值。
    - server是指服务器的地址和端口
    - serverstartcode 是服务开始的时候的timestamp
    - ![hbase:meta表结构](meta_table_structure.PNG)

- HBase读数据流程
    1. 客户端在本地缓存读取hbase:meta表的所在的位置，即对应的RegionServer位置信息
    1. 如果在缓存中获取不到hbase:meta表所在的位置信息，去zk中获取hbase:meta表的所在的位置信息，并缓存其对应的RegionServer位置信息
    1. 在hbase:meta表查找，获取存放目标数据的Region信息，从而找到对应的RegionServer。 
    1. 根据读取的TableName和RowKey的startkey 、endkey 找到对应的HRegion
    1. 每个regionserver只有一个blockcache（读缓存），读取数据时，先到memestore上读数据，找不到再到blockcahce上找数据，再查不到则到磁盘（storefile）查找，并把读入的数据同时放入blockcache。
    - ![meta表](meta_table.png)
    
- HBase写数据流程
    1. 通过读取数据流程1~3找到该写数据最终需要去的HRegionServer；
    1. Write Ahead Log（WAL）：然后客户端将写请求发送给相应的HRegionServer，在HRegionServer中它首先会将该写操作写入Hlog日志文件中(Flush到磁盘中)。
    1. 写完Hlog日志文件后，HRegionServer根据Put中的TableName和RowKey、startkey、endkey找到对应的HRegion，并根据Column Family找到对应的HStore，并将Put写入到该HStore的MemStore中。如果HLog和Memstore均写入成功，则这条数据写入成功，并返回通知客户端。
    1. 写入MemStore后的操作：
        1. 存入MemStore，一直到MemStore满
        1. Flush成一个StoreFile，直至增长到一定阈值
        1. 触发Compact合并操作 -> 多个StoreFile合并成一个StoreFile，同时进行版本合并和数据删除
        1. 当StoreFiles Compact后，逐步形成越来越大的StoreFile
        1. 单个StoreFile大小超过一定阈值（Region split 阈值）后，触发Split操作，把当前Region Split成2个Region，Region会下线，新Split出的2个子Region会被HMaster分配到相应的HRegionServer上，使得原先1个Region的压力得以分流到2个Region上；

- MemStore Flush机制
    - Region级别的flush
        - 当一个MemStore的大小超过了hbase.hregion.memstore.flush.size(128M)的大小，此时当前的HRegion中所有的MemStore会Flush到HDFS中，不阻塞写操作。
        - 当一个Region的MemStore总量达到hbase.hregion.memstore.block.multiplier * hbase.hregion.memstore.flush.size(默认2*128M=256M)时，会阻塞这个region的写操作，并强制刷写到HDFS。触发这个刷新只会发生在MemStore即将写满128M时put了一个巨大的记录的情况，这时会阻塞写操作，强制刷新成功才能继续写入
    - HRegionServer级别的flush
        - 当RS里所有的MemStore的大小超过了hbase.regionserver.global.memstore.upperLimit(默认0.4，memstores所占最大堆空间比例)的大小，此时当前HRegionServer中所有HRegion中的MemStore都会Flush到HDFS中，阻塞写操作。
        - 当前HRegionServer中HLog的大小超过阈值，当前HRegionServer中所有HRegion中的MemStore都会Flush到HDFS中。（RS级别的flush）

- Region Split机制
    - HRegionServer拆分region步骤
        - 先将该region下线，然后拆分
        - 将其子region加入到hbase:meta表中
        - 再将他们加入到原本的HRegionServer中
        - 最后汇报Master。
    - 拆分策略(hbase.regionserver.region.split.policy)
        - SteppingSplitPolicy现在默认的策略
        - IncreasingToUpperBoundRegionSplitPolicy(以前默认的策略)
        - ConstantSizeRegionSplitPolicy：仅仅当region大小超过常量值（hbase.hregion.max.filesize大小）时，才进行拆分。
        - DelimitedKeyPrefixRegionSplitPolicy
        - KeyPrefixRegionSplitPolicy：保证以分隔符前面的前缀为splitPoint，保证相同RowKey前缀的数据在一个Region中。
        - KeyPrefixRegionSplitPolicy：保证具有相同前缀的row在一个region中（要求设计中前缀具有同样长度）

- StoreFile Compaction
    - 由于memstore每次刷写都会生成一个新的HFile，且同一个字段的不同版本(timestamp)和不同类型（Put/Delete）有可能会分布在不同的 HFile中，因此查询时需要遍历所有的HFile。
    - 为了减少 HFile 的个数，以及清理掉过期和删除的数据，会进行 StoreFile Compaction。
    - Compaction 分为两种，分别是 Minor Compaction 和 Major Compaction。
        - Minor Compaction会将临近的若干个较小的 HFile 合并成一个较大的 HFile，但不会清理过期和删除的数据。
        - Major Compaction 会将一个 Store 下的所有的 HFile 合并成一个大 HFile，并且会清理掉过期和删除的数据。

- HBase数据结构LSM树(Log-Structured Merge-Trees)
    - 将对数据的修改增量保持在内存中，达到指定的大小限制后将这些修改操作批量写入磁盘
    - 不过读取的时候稍微麻烦，需要合并磁盘中历史数据和内存中最近修改操作，所以写入性能大大提升，读取时可能需要先看是否命中内存，否则需要访问较多的磁盘文件。
    - 极端的说，基于LSM树实现的HBase的写性能比MySQL高了一个数量级，读性能低了一个数量级。
    - 基本过程
        - LSM树原理把一棵大树拆分成N棵小树，它首先写入内存中
        - 随着小树越来越大，内存中的小树会flush到磁盘中
        - 磁盘中的树定期可以做merge操作，合并成一棵大树，以优化读性能。
    - ![LSM Tree](LSM_tree.PNG)

- RowKey设计原则
    - 要保证rowkey的唯一性
    - rowkey 长度建议是越短越好，不要超过16个字节
    - Rowkey的散列原则
        - 如果Rowkey是按时间戳的方式递增，不要将时间放在二进制码的前面，建议将散列字段作为Rowkey的高位，由程序循环生成，低位放时间字段，这样将提高数据均衡分布在每个Regionserver实现负载均衡的几率。
        - 如果没有散列字段，首字段直接是时间信息将产生所有新数据都在一个RegionServer上堆积的热点现象，这样在做数据检索的时候负载将会集中在个别RegionServer，降低查询效率。
    - 常用方法
        - 生成随机数、hash、散列值(原本rowKey为1001的，MD5后变成：b8c37e33defde51cf91e1e03e51657da)
        - 字符串反转(20170524000001转成10000042507102)
        - 字符串拼接(20170524000001_a12e)

- [本地连接操作HBase](../java/com/cheng/hbase/HBaseJava.java)
    1. 修改pom添加hbase-client依赖
    1. 把hbase-site.xml放到资源文件目录下[hbase-site.xml](../resources/hbase-site.xml)

- HBase filter
    - 通过Get，Scan，HBase只能根据特性的行键进行查询（Get）或者根据行键的范围来查询（Scan）。
    - 过滤器可以根据列族、列、版本等更多的条件来对数据进行过滤查询
    - 基于 HBase 本身提供的三维有序（行键，列，版本有序），这些过滤器可以高效地完成查询过滤的任务
    - 带有过滤器条件的 RPC 查询请求会把过滤器分发到各个 RegionServer（这是一个服务端过滤器），这样也可以降低网络传输的压力。
    - 比较过滤器(所有比较过滤器均继承自 CompareFilter。创建一个比较过滤器需要两个参数，分别是比较运算符和比较器实例)
        - 比较运算符
            - LESS (<)
            - LESS_OR_EQUAL (<=)
            - EQUAL (=)
            - NOT_EQUAL (!=)
            - GREATER_OR_EQUAL (>=)
            - GREATER (>)
            - NO_OP (排除所有符合条件的值)
        - 比较器(所有比较器均继承自 ByteArrayComparable 抽象类)
            - BinaryComparator : 使用 Bytes.compareTo(byte []，byte []) 按字典序比较指定的字节数组。
            - BinaryPrefixComparator : 按字典序与指定的字节数组进行比较，但只比较到这个字节数组的长度。
            - RegexStringComparator : 使用给定的正则表达式与指定的字节数组进行比较。仅支持 EQUAL 和 NOT_EQUAL 操作。
            - SubStringComparator : 测试给定的子字符串是否出现在指定的字节数组中，比较不区分大小写。仅支持 EQUAL 和 NOT_EQUAL 操作。
            - NullComparator ：判断给定的值是否为空。
            - BitComparator ：按位进行比较。
            ```bash
            scan 'my_table',{ FILTER =>"SingleColumnValueFilter('cf1','name',=,'binary:aaa',true,true)"}
            scan 'my_table',{ FILTER =>"SingleColumnValueFilter('cf1','name',=,'regexstring:^b',true,true)"}
            scan 'my_table',{ FILTER =>"SingleColumnValueFilter('cf1','name',=,'substring:cc',true,true)"}
            ```
        - 比较过滤器种类
            - RowFilter ：基于行键来过滤数据；
            - FamilyFilterr ：基于列族来过滤数据；
            - QualifierFilterr ：基于列限定符（列名）来过滤数据；
            - ValueFilterr ：基于单元格 (cell) 的值来过滤数据；
            - DependentColumnFilter ：指定一个参考列来过滤其他列的过滤器，过滤的原则是基于参考列的时间戳来进行筛选。
    - 专用过滤器(专用过滤器通常直接继承自 FilterBase，适用于范围更小的筛选规则。)
        - 单列列值过滤器 (SingleColumnValueFilter)
        - 单列列值排除器 (SingleColumnValueExcludeFilter)
        - 行键前缀过滤器 (PrefixFilter)
        - 列名前缀过滤器 (ColumnPrefixFilter)
        - 分页过滤器 (PageFilter)
        - 时间戳过滤器 (TimestampsFilter)
        - 首次行键过滤器 (FirstKeyOnlyFilter)
    - 包装过滤器(包装过滤器就是通过包装其他过滤器以实现某些拓展的功能)
        - SkipFilter过滤器
        - WhileMatchFilter过滤器
    - FilterList
        - 以上都是单个过滤器的作用，当需要多个过滤器共同作用于一次查询的时候，就需要使用 FilterList。
        - FilterList 支持通过构造器或者 addFilter 方法传入多个过滤器。
        - 多个过滤器组合的结果由operator参数定义，其可选参数定义在Operator枚举类中。只有MUST_PASS_ALL和MUST_PASS_ONE两个可选的值。
        ```
        List<Filter> filters = new ArrayList<Filter>();
        
        Filter filter1 = new RowFilter(CompareOperator.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes("XXX")));
        filters.add(filter1);
        
        Filter filter2 = new RowFilter(CompareOperator.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("YYY")));
        filters.add(filter2);
        
        Filter filter3 = new QualifierFilter(CompareOperator.EQUAL, new RegexStringComparator("ZZZ"));
        filters.add(filter3);
        
        FilterList filterList = new FilterList(filters);
        
        Scan scan = new Scan();
        scan.setFilter(filterList);
        ```

- load HFile文件到HBase表
    - 将hfile文件放在hdfs的hbase数据目录下(/hbase/data)
    ```bash
    hadoop jar /usr/local/hbase/lib/hbase-shell-1.3.1.jar completebulkload /user/hadoop/hbase/data/input_data my_table
    
    # [hadoop@nn1 hadoop]$ hadoop jar /usr/local/hbase/lib/hbase-shell-1.3.1.jar
    # An example program must be given as the first argument.
    # Valid program names are:
    # CellCounter: Count cells in HBase table.
    # WALPlayer: Replay WAL files.
    # completebulkload: Complete a bulk data load.
    # copytable: Export a table from local cluster to peer cluster.
    # export: Write table data to HDFS.
    # exportsnapshot: Export the specific snapshot to a given FileSystem.
    # import: Import data written by Export.
    # importtsv: Import data in TSV format.
    # rowcounter: Count rows in HBase table.
    # verifyrep: Compare the data from tables in two different clusters. WARNING: It doesn't work for incrementColumnValues'd cells since the timestamp is changed after being appended to the log.
    ```

- 预分region
    - HBase默认建表时有一个region，这个region的rowkey是没有边界的，即没有startkey和endkey，在数据写入时，所有数据都会写入这个默认的region，
    随着数据量的不断增加，此region已经不能承受不断增长的数据量，会进行split，分成2个region。在此过程中，会产生两个问题：
        1. 数据往一个region上写，会有写热点问题。
        1. region split会消耗宝贵的集群I/O资源。
    - 基于此我们可以控制在建表的时候，创建多个空region，并确定每个region的起始和终止rowky，这样只要我们的rowkey设计能均匀的命中各个region，就不会存在写热点问题。
    自然split的几率也会大大降低。当然随着数据量的不断增长，该split的还是要进行split。
    - 步骤
        1. 在hive中抽样分析原始数据key的分布(前n位或者前n位每个位置字符出现次数的乘积)
        1. 分析完后，生成region的key划分配置文件
        ```bash
        #在指定目录创建文件，并将分配内容写入 
        cd /home/hadoop/hbase
        vim split_data
        
        #文件内容
        rk1
        rk2
        rk3
        rk4
        ...
        ```
        1. 创建带有预留region的表
        ```bash
        create 'user_install_status', 'cf', {SPLITS_FILE => '/home/hadoop/hbase/split_data'}
        ```
        1. mapreduce读原始数据，转成hfile 文件
        1. 用命令将hfile文件导入到hbase中
        1. 完成后可在HBase webUI 查看预分region情况

- 用java代码实现预分
    1. 在hive中抽样分析原始数据key的分布(前n位或者前n位每个位置字符出现次数的乘积)
    1. 分析完后，生成region的key划分配置文件
    1. 将预分region的代码打包，创建表并根据自定义split类进行region的预分
        - 自定义split方法，并读取配置文件
            1. 创建split 类，实现 SplitAlgorithm 分割region接口；
            1. 实现接口里的 split 方法；
            1. 读取预分region的配置文件，根据预分region的配置文件生成预分region的规则
            1. 生成jar包，只需要导出预分region的工具类和规则配置文件,上传到服务器的指定目录
            1. 并把这个目录配置到操作机的hbase-env.sh的HBASE_CLASSPATH里
            1. 使用以下命令创建表并根据自定义split类进行region的预分(也可代码整合)
            ```bash
            hbase org.apache.hadoop.hbase.util.RegionSplitter my_table_id_split com.company.hbase.TableRegionSplit -c 2 -f cf
            # -c：是指定分多少个region但是在我的自定义的split中并没有使用，不过这是命令的必填项所以要必须写上。只要大于1就可以。
            # -f：是指定列族的名称。
            ```
    1. mapreduce读原始数据，转成hfile 文件
    1. 用命令将hfile文件导入到hbase中
    1. 完成后可在HBase webUI 查看预分region情况

- HBase表的压缩格式
    ```bash
    # 1. 如果表的数据量很大，region很多，disable过程会比较缓慢，需要等待较长时间。过程可以通过查看hbase master log日志监控。
    disable 'user_install_status_aid_split'
    
    # NAME即column family。HBase修改压缩格式，需要一个列族一个列族的修改。名字一定要与你自己列族的名字一致，否则就会创建一个新的列族并且压缩格式是snappy的。   
    alter 'user_install_status_aid_split', NAME => 'cf', COMPRESSION => 'snappy'
        
    # 重新enable表
    enable 'user_install_status_aid_split'
     
    # enable表后，HBase表的压缩格式并没有生效，还需要执行major_compact，major_compact除了做文件Merge操作，还会将其中的delete项删除。   
    major_compact 'user_install_status_aid_split'
        
    # 整个过程耗时较长，会对hbase的服务造成很大的影响，可以选择在一个服务不忙的时间来做。
    # 或者是在往表里插入数据之前就指定好每个列族的压缩算法
    ```
- HBase数据导出
    - 将hbase表scan的结果，导出到orc文件，再将orc文件导入的hive表。(scan HBase表)
    ```
    // InputFormat是TableInputFormat
    public static class ScanHbase2OrcMapper extends TableMapper<NullWritable, Writable>{
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
        }
        
        @Override
        protected void map(ImmutableBytesWritable key, Result result,Context context) throws IOException, InterruptedException {
        }
    }
    ```
    - 读取hbase表的hfile文件，导出到orc文件，再将orc文件导入的hive表。
    ```
    // 自定义InputFormat去读取hfile文件
    public class HFileInputFormat extends FileInputFormat<ImmutableBytesWritable, Cell>{}
    
    // HFILE文件是按单列和row存储成一行数据的，包括列的名称、版本值、是否被删除
    // 因此读取了一行以后，需要找出最新的未被删除的记录
    // 同一行数据，可能存在于多个HFile中，编写程序时，需要找到最新的未被删除的记录
    ```
        1. 根据hfile中数据存储的格式实现HfileItem类，HfileItem是Hfile文件中物理上的一行记录，如值、版本、是否被删除。
        1. 根据hfile 文件中的逻辑一行的记录，实现HfileRecord，如id、name、age。而且每个里面又都包含HfileItem。
        1. 在map阶段读取hfile文件内容，装到HfileRecord对象中，并输出。
        1. 在combiner阶段，进行多版本合并。
        1. 在reduce阶段，再进行多版本合并，将合并后的 HfileRecord对象中的数据，写入到Orc 文件中。
        1. 设置MapReduce 任务job的各种参数。
        1. 运行

- [HBase与Hive的关联](https://blog.csdn.net/vaq37942/article/details/84876076)

- HBase性能优化
    - 高可用
    - 预分区
    - RowKey 设计
    - 内存优化
     - HBase 操作过程中需要大量的内存开销，毕竟 Table 是可以缓存在内存中的，一般会分配整个可用内存的 70%给 HBase 的 Java 堆。但是不建议分配非常大的堆内存，因为 GC 过
       程持续太久会导致 RegionServer 处于长期不可用状态，一般 16~48G 内存就可以了，如果因为框架占用内存过高导致系统内存不足，框架一样会被系统服务拖死。
    - **基础优化**

- Hive与HBase的对比
    - Hive
        1. 数据仓库，其本质其实就相当于将 HDFS 中已经存储的文件在Mysql中做了一个双射关系，以方便使用 HQL 去管理查询。
        1. 适用于离线的数据分析和清洗，延迟较高。
        1. Hive 存储的数据依旧在 DataNode 上，编写的 HQL 语句终将是转换为 MapReduce 代码执行。
    - HBase
        1. 是一种面向列族存储的非关系型数据库
        1. 适用于单表非关系型数据的存储，不适合做关联查询，类似 JOIN 等操作。
        1. 数据持久化存储的体现形式是 HFile，存放于 DataNode 中，被 ResionServer 以 region 的形式进行管理。
        1. 延迟较低，接入在线业务使用，面对大量的企业数据，HBase可以直线单表大量数据的存储，同时提供了高效的数据访问速度。

- HBase shell commands
    - [HBase shell commands](commands.md)

- 参考
    1. [Apache HBase官方文档](http://hbase.apache.org/book.html#regionserver.arch)
    1. [HBase: The Definitive Guide by Lars George](https://www.oreilly.com/library/view/hbase-the-definitive/9781449314682/ch04.html)