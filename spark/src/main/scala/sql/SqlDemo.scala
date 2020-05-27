package sql

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}

object SqlDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("userApp").setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val df: DataFrame = spark.read.json("./spark/src/main/resources/input/user.json")
    df.show()

    df.filter(df.col("age")>20).show()
    // 注意，这里的spark不是包名，而是SparkSession对象的名字！！！
    import spark.implicits._
    df.filter($"age" > 20).show()

    df.createOrReplaceTempView("user")
    spark.sql("select * from user where Name='Justin'").show()

    spark.stop()
  }
}
