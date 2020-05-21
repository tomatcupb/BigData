object CaseMatch {
  def main(args: Array[String]): Unit = {
    val str = "+-1"
    for(ch <- str){
      ch match {
        case '+'=>println("+")
        case test =>println(test)
        case '3'=>println("3")
        case _ if(ch>'3')=>println("no match")
      }
    }

    val a = 4
    val obj = if(a==1) 1
    else if(a==2) "1"
    else if(a==3) List[Int]()
    else if(a==4) Map[Int,String]()

    val res = obj match {
      case a:Int=>println("Int")
      case a:String=>println("String")
      case a:List[Int]=>println("List")
      case a:Map[Int,String]=>println("Map[Int,String]")
      case _ =>println("XXX")
    }
    println(res.getClass)

    val tup = (1,3)
    tup match {
      case (0,1)=>println("111")
      case (x,y)=>println("222")
      case _ => println("333")
    }
  }
}
