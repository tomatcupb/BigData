# Yarn
1. Yarn是一个资源调度平台，负责为运算程序提供服务器运算资源，相当于一个分布式的操作系统平台，而mapreduce等运算程序则相当于运行于操作系统之上的应用程序
2. Yarn并不清楚用户提交的程序的运行机制，只提供运算资源的调度，实现与用户程序**完全解耦**
3. Yarn的工作机制
    - MR\SPARK等程序提交到客户端所在的节点
    - yarnrunner（本地测试即LocalRunner）向ResourceManager申请一个application。
    - RM将该应用程序的资源路径返回给yarnrunner
    - 该程序将运行所需资源（job.split, job.xml, wordcount.jar）提交到HDFS上(./staging/jobID/)
    - 程序资源提交完毕后，申请运行AppMaster
    - RM将用户的请求初始化成一个task
    - 其中一个NodeManager领取到task任务。
    - 该NodeManager创建容器Container，并产生Appmaster
    - Container从HDFS上拷贝资源到本地
    - Appmaster向RM 申请运行maptask容器
    - RM将运行maptask任务分配给另外两个NodeManager，另两个NodeManager分别领取任务并创建容器。
    - MR向两个接收到任务的NodeManager发送程序启动脚本，这两个NodeManager分别启动maptask，maptask对数据分区排序。
    - MRAppmaster向RM申请2个容器，运行reduce task。
    - reduce task向maptask获取相应分区的数据。
    - 程序运行完毕后，MR会向RM注销自己。
