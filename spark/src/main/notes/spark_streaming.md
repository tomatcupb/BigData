# Spark Streaming

- DStream
    - Spark基于RDD的概念很相似，Spark Streaming使用离散化流(discretized stream)作为抽象表示，叫作DStream。
    - DStream是随时间推移而收到的数据的序列。
    - 在内部，每个时间区间收到的数据都作为RDD存在，而DStream是由这些RDD所组成的序列(因此得名“离散化”)
    - DStream上的原语与RDD的类似，分为Transformations（转换）和Output Operations（输出）两种
    
- Spark Streaming执行过程
    - Spark Streaming为每个输入源启动对应的接收器
    - 接收器以任务的形式运行在应用的执行器进程中（接受器会占用一个executor的一个cpu，所以在local模式下，core必须大于1），从输入源收集数据并保存为RDD
    - 它们收集到输入数据后会把数据复制到另一个执行器进程来保障容错性(默认行为)。
    - 数据保存在执行器进程的内存中，和缓存 RDD 的方式一样。
    - 驱动器程序中的StreamingContext会周期性地运行Spark作业来处理这些数据，把数据与之前时间区间中的RDD进行整合。

- 基本数据源
    - Socket数据流：ssc.socketTextStream(hostname, port)
    - 文件数据源: ssc.textFileStream(directory) //用Flume就可以更好的完成，所以这个功能很鸡肋
    - 自定义数据源: 继承Receiver，并实现onStart、onStop方法来自定义数据源采集。
        - ssc.receiverStream(new CustomReceiver(params*))
        
- 高级数据源
    - Kafka
    - Flume
    
- 无状态转化操作
    - 只根据当前批次的数据进行处理并产生结果，不考虑历史数据
    
- 有状态转化操作
    - 在 DStream 中**跨批次**维护状态
    - updateStateByKey
        1. 定义状态，状态可以是一个任意的数据类型。（Seq\[T]） 
        1. 定义状态更新函数，用此函数阐明如何使用之前的状态和来自输入流的新值对状态进行更新。
            - val updateFunc = (values: Seq[Int], state: Option[Int]) =>{} // 定义更新状态方法，参数values为当前批次单词频度，state为以往批次单词频度
        1. 配置checkpoint目录,会使用检查点来保存状态。
            - ssc.checkpoint(dir)
            - 这里注意Streaming设置的checkpoint与Core设置的checkpoint的区别！！
            
- 窗口函数Window
    - DStream.window(windowDuration: Duration, slideDuration: Duration)
        - windowDuration和slideDuration都是batchDuration的整数倍
    - scala: list.sliding(size: Int, step: Int)
    
- 特别操作
    - transform算子:transform[U: ClassTag](transformFunc: RDD[T] => RDD[U])
        - 与map算子的区别：每个批次会在driver端执行一次位置1的代码，而位置2的代码只会执行一次
            - 场景：动态和driver端的blacklist进行join，blacklist会动态变化，若在位置2只能获得一次最初的blacklist版本
            而位置1会在每个批次获取一次blacklist
            ```
            windowDStream.transform(rdd=>{
                  // 位置1：driver端执行
                  rdd.map(i=>{
                    // executor端执行
                    (i+1)
                  })
                })
            
            位置2：driver端执行
            windowDStream.map(i=>{
              // executor端执行
              (i,1)
            })
            ```
    - join
            
- DStreams输出
    - 与RDD中的惰性求值类似，如果一个 DStream 及其派生出的DStream都没有被执行输出操作，那么这些DStream就都不会被求值。
    如果StreamingContext中没有设定输出操作，整个context 就都不会启动。
    - foreachRDD(): foreachRDD(foreachFunc: RDD[T] => Unit): Unit
        - 在 foreachRDD() 中，可以重用我们在 Spark 中实现的所有行动操作。
        比如，常见的用例之一是把数据写到诸如 MySQL 的外部数据库中。 
            1. 连接不能写在driver层面 // Connection不可序列化
            1. 如果写在foreach则每个RDD都创建，得不偿失
            1. 增加foreachPartition，在分区创建
            1. 可以考虑使用连接池优化~~~~


- [java.lang.NoClassDefFoundError:org/apache/spark/streaming/StreamingContext](https://blog.csdn.net/appleyuchi/article/details/81633335)