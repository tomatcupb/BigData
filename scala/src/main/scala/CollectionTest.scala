import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object CollectionTest {
  def main(args: Array[String]): Unit = {
//    val arr = new ArrayBuffer[Int]()
//    arr.append(2,5,4,7)
//    for(i <- arr){
//      println(i)
//    }
//    import scala.collection.mutable.ListBuffer
//    val list1 = ListBuffer[Int](1,2,3)
//    val list2 = ListBuffer[Int]()
//
//    list1+=4
//    val list3 = list1
//    println(list3)
//    list1.append(5)
//    list1 ++= list3
//    println(list1)

//    val queue = new mutable.Queue[Int]()

//
//    val map = mutable.HashMap("Tom"->108);
//    val map2 = new mutable.HashMap[String, Int] ()
//
//    map2.put("test",1000)
//    map2.put("test2",2)
//    map2.put("tes3",3)
//    map2+=("tett4"-> 10, "tewsts"->50)
//    println(map2.get("tett4").get)
//
//    for((k,v)<-map2){println(k+":"+v)}
//    for(k<-map2.keys){println(k+":"+map2(k))}
//    for(v<-map2.values){println(v)}
//    for(entry<-map2){println(entry._1+":"+entry._2)}

    val set = mutable.Set(1,1,3)
    for(i<-set) println(i)
  }


}
