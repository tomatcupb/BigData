package streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StateDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("StateDemo").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(3))


    ssc.checkpoint("cp")
    val lineDStream: ReceiverInputDStream[String] = ssc.socketTextStream("s3.hadoop",9999)
    val word: DStream[(String, Int)] = lineDStream.flatMap(_.split(" ")).map((_,1))

    val value: DStream[(String, Int)] = word.updateStateByKey {
      case (seq, buffer) => {
        val sum = buffer.getOrElse(0) + seq.sum
    Option(sum)
  }
}

    value.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
