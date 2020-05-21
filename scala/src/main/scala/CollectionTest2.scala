object CollectionTest2 {
  def main(args: Array[String]): Unit = {
    val list1 = List(5,2,3)
    var list2 = List[Int]()
//    for(i <- list1){
//      list2 = list2:+2*i // 产生新的List
//    }
//    for(i<-list2){println(i)}

//    import scala.collection.mutable.ListBuffer
//
//    var list3 = ListBuffer[Int]()
//    for(i<-list1){list3 += 2*i}
//    for(i<-list3){println(i)}

//    list2 = list1.map(multiple)
//    for(i<-list2){println(i)}

      val demoMap = DemoMap()
      println(demoMap.map(multiple))

    println(list1.reduce(max))
  }

  def max(num1:Int, num2:Int): Int ={
    if(num1>num2) num1 else num2
  }



  def multiple(i:Int): Int ={
    2*i
  }

}

class DemoMap{
  val list = List(17,3)
  def map(f:Int => Int): List[Int] ={
    var list2 = List[Int]()
    for(i<-list){
      list2 = list2:+f(i)
    }
    list2
  }
}

object DemoMap{
  def apply(): DemoMap = new DemoMap()
}
