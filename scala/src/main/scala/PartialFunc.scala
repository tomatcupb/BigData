object PartialFunc {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,"HELLO")
    println(list.collect(fun))
    println(list.collect(fun2))
    println(list.collect{
      case i:Int=>i+1
    })
    println(fun,fun2)
  }

  def fun = new PartialFunction[Any, Int] {
    override def isDefinedAt(x: Any): Boolean = x.isInstanceOf[Int]

    override def apply(v1: Any): Int = v1.asInstanceOf[Int]+1
  }

  def fun2:PartialFunction[Any, Int]={
    case i:Int => i+1
  }
}
