package util

import rdd.SparkWordCount

object MyPredef {
  implicit def deleteHdfs(o:String) = new SparkWordCount(o)
}
