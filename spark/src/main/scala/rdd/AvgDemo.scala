package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AvgDemo {
  def main(args: Array[String]): Unit = {
    val conf =  new SparkConf().setMaster("local[*]").setAppName("rddDemo")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List[(String,Double)](("AAA",1),("AAA",3),("BBB",2),("BBB",4),("BBB",5)))

//    val ans: RDD[(String, Int)] = rdd.map(a => (a._1, (a._2, 1)))
//      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
//      .map(t => (t._1, t._2._1 / t._2._2))
//    ans.foreach(println)

    val ans: RDD[(String, Double)] = rdd.groupByKey().map(i => {
      val value: Iterable[Double] = i._2
      val iterator: Iterator[Double] = value.iterator
      var sum: Double = 0
      var count: Double = 0
      while (iterator.hasNext) {
        val i: Double = iterator.next()
        sum += i
        count += 1
      }
      (i._1, sum / count)
    })
    ans.foreach(println)

    sc.stop()
  }
}
