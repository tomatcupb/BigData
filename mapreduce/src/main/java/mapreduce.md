# MapReduce
1. Writable: hadoop自己开发的一套精简高效的序列化机制
    - Java自带的序列化是一个重量级序列化框架（Serializable），一个对象被序列化后，会附带很多额外的信息（各种校验信息，header，继承体系等），不便于在网络中高效传输。
1. HDFS分块(block)和MapReduce切片(split)
    - 分块：指的是文件在HDFS上的存储，将要存储的数据分成一块又一块，然后放在HDFS上存储；(2.7.3开始从64Mb变成了128Mb)
    - 切片：指的是MapReduce阶段的FileInputFormat阶段对于数据块的切片，默认的切片大小是块大小。
    - 三个文件的大小分别是:64KB 129MB 127MB，分块4块，切片3片
1. job提交流程源码过程
    1. job.waitForCompletion(true)
        - submit()
            - connect(),建立连接
            - submitter.submitJobInternal(Job.this, cluster),提交job
                - JobSubmissionFiles.getStagingDir(cluster, conf);创建给集群提交数据的Stag路径
                - submitClient.getNewJobID();获取jobid ，并创建job路径
                - copyAndConfigureFiles(job, submitJobDir);拷贝jar包到集群
                - writeSplits(job, submitJobDir);计算切片，生成切片规划文件
                    - writeNewSplits(job, jobSubmitDir);
                        - input.getSplits(job);
                - writeConf(conf, submitJobFile);向Stag路径写xml配置文件
                    - conf.writeXml(out);
                - submitClient.submitJob(
                             jobId, submitJobDir.toString(), job.getCredentials());提交job，返回提交状态
        - 向StagingDir下上传了
            - .staging/jobId/job.xml
            - .staging/jobId/job.split
            - .staging/jobId/job.jar
            
1. FileInputFormat源码解析(input.getSplits(job))
    ```
    //minSize参数调的比blockSize大，则可以让切片变得比blocksize还大。
    long minSize = Math.max(getFormatMinSplitSize(), getMinSplitSize(job));
    //maxSize参数如果调得比blocksize小，则会让切片变小，而且就等于配置的这个参数的值。
    long maxSize = getMaxSplitSize(job);
    ```
    
    ```
    long blockSize = file.getBlockSize();
    long splitSize = computeSplitSize(blockSize, minSize, maxSize);获取分片大小
    
    Math.max(minSize, Math.min(maxSize, blockSize));
    ```

    ```
       while (((double) bytesRemaining)/splitSize > SPLIT_SLOP){
            SPLIT_SLOP = 1.1
            当剩余的大小大于分片大小的1.1倍，继续分，否则不再分
       }

    ```
    - 最后将切片信息写入切片规划文件中（job.split？）
    - 数据切片只是在逻辑上对输入数据进行分片，并不会再磁盘上将其切分成分片进行存储。
    - InputSplit只记录了分片的元数据信息，比如起始位置、长度以及所在的节点列表等。
    - 提交切片规划文件到yarn上，yarn上的MrAppMaster就可以根据切片规划文件计算开启maptask个数。

1. CombineTextInputFormat切片机制
    - 在HDFS上存在大量小文件是，默认的分片机制会产生大量的切片，导致开启大量的maptask；为了避免这种情况，使用CombineTextInputFormat进行切片
    - 它的切片逻辑跟TextFileInputFormat不同：它可以将多个小文件从逻辑上规划到一个切片中，这样，多个小文件就可以交给一个maptask。
    - 优先满足最小切片大小，不超过最大切片大小
    ```
    job.setInputFormatClass(CombineTextInputFormat.class);
    CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);// 4m
    CombineTextInputFormat.setMinInputSplitSize(job, 2097152);// 2m
    举例：0.5m+1m+0.3m+5m=2m + 4.8m=2m + 4m + 0.8m
    ```
    
         