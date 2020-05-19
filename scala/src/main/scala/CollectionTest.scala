import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object CollectionTest {
  def main(args: Array[String]): Unit = {
    val arr = new ArrayBuffer[Int]()
    arr.append(2,5,4,7)
    for(i <- arr){
      println(i)
    }
    import scala.collection.mutable.ListBuffer
    val list1 = ListBuffer[Int](1,2,3)
    val list2 = ListBuffer[Int]()

    list1+=4
    val list3 = list1
    println(list3)
    list1.append(5)
    list1 ++= list3
    println(list1)

    val queue = new mutable.Queue[Int]()
    queue.
  }


}
