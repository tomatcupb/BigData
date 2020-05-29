package streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamingDemo")
    val ssc = new  StreamingContext(conf, Seconds(3))

    // 定义消息输入源来创建DStream
    // 1. Socket数据流
//    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("s3.hadoop", 9999)
    // 2. 文件数据源
    val lines: DStream[String] = ssc.textFileStream("E:\\GitWorkspace\\BigData\\spark\\src\\main\\resources\\input")

    // 转换操作
    val words: DStream[String] = lines.flatMap(_.split(" "))
    val pairs: DStream[(String, Int)] = words.map(word => (word, 1))
    val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)

    wordCounts.print()
    // 启动消息采集和处理
    ssc.start()
    // driver等待采集器的执行
    ssc.awaitTermination()
  }
}
