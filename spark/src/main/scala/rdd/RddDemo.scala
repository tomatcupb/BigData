package rdd

import org.apache.spark.util.LongAccumulator
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object RddDemo {
  def main(args: Array[String]): Unit = {
    val conf =  new SparkConf().setMaster("local[*]").setAppName("rddDemo")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List(1,2,3,4),2)
    var sum = 0
    // 在excutor端执行sum+i
    // 但是executor之间相互独立，因此不返回各自的累加sum值
    rdd.foreach(i => sum = sum+i)
    // 在driver无法接收excutor的累加值
    // 输出结果sum = 0
    println(sum)

    // 创建累加器
    val ac: LongAccumulator = sc.longAccumulator
    // 累加器强调其只写性，executor端执行完累加会返回driver端
    rdd.foreach(i=>ac.add(i))
    println(ac.value)

    sc.stop()
  }
}
