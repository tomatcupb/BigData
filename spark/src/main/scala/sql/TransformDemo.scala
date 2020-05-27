package sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TransformDemo {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("userApp").master("local[*]")
      .config("spark.some.config.option", "some-value").getOrCreate()
    val rdd: RDD[(String, Int)] = spark.sparkContext.parallelize(List(("TOM",20),("Jerry",10),("Spike",15)))

    import spark.implicits._
    val df: DataFrame = rdd.toDF("name","age")
    val rdd2: RDD[Row] = df.rdd

    rdd2.foreach(row=>{
      println(row.getString(0))
    })

    val ds: Dataset[Person] = rdd.map(i=>Person(i._1, i._2)).toDS


    val df2: DataFrame = ds.toDF
    val ds2: Dataset[Person] = df2.as[Person]

    df2.foreach(row => println(row.getString(0),row.getInt(1)))
    ds2.foreach(p => println(p.age, p.name))

    ds2.show


    spark.stop()
  }
}

case class Person(name: String, age: Int)
