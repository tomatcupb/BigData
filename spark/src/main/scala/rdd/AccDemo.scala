package rdd

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

object AccDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("accDemo")
    val sc = new SparkContext(conf)

    val rdd: RDD[String] = sc.parallelize(List("hive","hbase","scala","spark","hadoop"))
    // 创建累加器
    val accumulator = new WordAccumulator
    // 注册累加器
    sc.register(accumulator)
    rdd.foreach(word => accumulator.add(word))
    println(accumulator.value)

    sc.stop()
  }
}


// String, util.ArrayList[String]分别是输入输出参数的泛型
class WordAccumulator extends AccumulatorV2[String, util.ArrayList[String]]{
  private val list = new util.ArrayList[String]()

  // 是否初始化
  override def isZero: Boolean = list.isEmpty

  override def copy(): AccumulatorV2[String, util.ArrayList[String]] = new WordAccumulator

  override def reset(): Unit = list.clear()

  // 累加
  override def add(v: String): Unit = {
    if(v.contains("h")) list.add(v)
  }

  // 多executor的累加器合并
  override def merge(other: AccumulatorV2[String, util.ArrayList[String]]): Unit = list.addAll(other.value)

  // 返回的结果
  override def value: util.ArrayList[String] = list
}
