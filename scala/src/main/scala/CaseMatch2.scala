object CaseMatch2 {
  val sale = Bundle("书籍",10,Book("漫画",40), Bundle("文学作品",20,Book("围城",30),Book("阳关",80)))

  def main(args: Array[String]): Unit = {
    println(sell(sale))
  }

  def sell(item:Item): Int ={
    item match {
      case Book(_,p) => p
      case Bundle(_,discount,rest@_*) =>rest.map(sell).sum-discount
//      case Bundle(_,_,rest@_*) =>println(rest)
    }
  }
}



abstract class Item
case class Book(desc:String, price:Int) extends Item
case class Bundle(desc:String, discount: Int, item:Item*) extends Item
