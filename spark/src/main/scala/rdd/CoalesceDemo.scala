package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object CoalesceDemo {
  def main(args: Array[String]): Unit = {
    val conf =  new SparkConf().setMaster("local[*]").setAppName("rddDemo")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(1 to 16,4)
    println("coalece分区前："+rdd.getNumPartitions)

//    val coalRDD: RDD[Int] = rdd.coalesce(10)
    val coalRDD: RDD[Int] = rdd.repartition(16)
    println("coalece分区后："+coalRDD.getNumPartitions)
    coalRDD.saveAsTextFile("./spark/output2")

    sc.stop()
  }
}
