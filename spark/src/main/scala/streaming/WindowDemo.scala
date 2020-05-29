package streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WindowDemo {
  def main(args: Array[String]): Unit = {
    /* scala的sliding函数
    val ints = List(1,2,3,4,5,6,7,8)
    val iterator: Iterator[List[Int]] = ints.sliding(3,2)
    for (elem <- iterator) {
      println(elem.mkString(","))
    }
    */

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WindowDemo")
    val ssc = new StreamingContext(conf,Seconds(3))

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("s3.hadoop",9999)

    val windowDStream: DStream[String] = lines.window(Seconds(9), Seconds(3))
    val wcDStream: DStream[(String, Int)] = windowDStream.map((_,1)).reduceByKey(_+_)


//    windowDStream.transform(rdd=>{
//      // driver端执行
//      rdd.map(i=>{
//        // executor端执行
//        (i+1)
//      })
//    })
//
//    windowDStream.map(i=>{
//      // executor端执行
//      (i,1)
//    })

    wcDStream.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
