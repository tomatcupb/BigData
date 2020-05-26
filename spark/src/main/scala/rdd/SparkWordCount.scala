package rdd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class SparkWordCount(val outPath:String){
  def deletePath = {
    val hadoopConf = new Configuration()
    val fs: FileSystem = FileSystem.get(hadoopConf)
    val path = new Path(outPath)
    if(fs.exists(path)){
      fs.delete(path,true)
    }
  }
}
object SparkWordCount {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("sparkwordcount").setMaster("local[*]")
    val sc = new SparkContext(conf)

    //引入隐式转换给字符串赋予删除HDFS路径的功能
    val output = "C:\\Users\\SirAlex\\Desktop\\wordcount"
    import util.MyPredef.deleteHdfs
    output.deletePath

    //从HDFS目录获取一个RDD，做为本次RDD运算的开始，也就是第一个RDD
    val text: RDD[String] = sc.textFile("C:\\Users\\SirAlex\\Desktop\\input\\test.txt")
    //scala的pairRDD是通过隐式转换得到的，也就是说当RDD的类型为（K,V）的时候会自动赋予了pairRDD的功能
    val mapvalues: RDD[(String, Int)] = text.flatMap(_.split(" ")).map((_,1)).reduceByKey(_ + _)

    //把输出的字符串格式化一下
    val value: RDD[String] = mapvalues.map(x =>s"${x._1}\t${x._2}")

    //这里是3个actioin，所以你的这个程序就会有3个spark job，但是这里使用了cache，所以避免了其它2个job重复的运算
    //就直接从内存中取运算完成的结果
    val cache: RDD[String] = value.cache()

    //把数据从集群的节点上拉回到driver，会占用driver的内存，所以使用的时候要注意别拉取太大的数据
    //如果拉取的数据太大，那就应该调整driver的内存（最好不要这么做）
    val strings:Array[String] = cache.collect()
    //打印一个本地变量
    println(strings.toBuffer)
    ////把数据从集群的节点上拉回到driver并转成map类型，用于rdd里的数据是元组的类型
    //    val stringToInt: collection.Map[String, Int] = mapvalues.collectAsMap()

    //RDD的循环打印，如果是集群模式的它会将结果打印到集群上
    cache.foreach(println)

    //将RDD保存成文件
    cache.saveAsTextFile(output)

  }
}




