package streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaStreaming {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("KafkaStreaming")
    val ssc = new  StreamingContext(conf, Seconds(3))


    // 这里用的0.8的kafka版本比较旧，新的版本没有createStream方法！
    val kafkaStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(
      ssc,"s1.hadoop:2181","spark",Map("bigdata"->2)
    )

    val ansDstream: DStream[(String, Int)] = kafkaStream.flatMap(_._2.split(" ")).map((_,1)).reduceByKey(_+_)
    ansDstream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
