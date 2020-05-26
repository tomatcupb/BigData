package rdd

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object BroadCastDemo {
  def main(args: Array[String]): Unit = {
    val conf =  new SparkConf().setMaster("local[*]").setAppName("rddDemo")
    val sc = new SparkContext(conf)
    val map: Map[Int, String] = Map((1,"A"),(2,"B"),(3,"C"),(4,"D"))
    val broadCastMap: Broadcast[Map[Int, String]] = sc.broadcast(map)
    val rdd = sc.parallelize(List(1,2,3,4),2)

    val mapRDD: RDD[String] = rdd.map(i=>{
      // 每个task都需要传输一个map到executor
      // 如果一个map100M,1个executor上要执行4个task
      // 需要占用executor400M内存，网络传输400M
       map(i)

      // 一个executor共享一个map
      // 需要占用executor100M内存，网络传输100M
      val value: Map[Int, String] = broadCastMap.value
      value(i)
    })

    mapRDD.foreach(println)

    sc.stop()
  }
}
