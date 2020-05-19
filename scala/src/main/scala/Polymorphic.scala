object Polymorphic {
  def main(args: Array[String]): Unit = {
    val sub = new Sub
    val sup = new Super

    println(sub.i + "---"+ sup.i)
  }
}

class Super{
  val i:Int = 10
}

class Sub extends Super{
  override val i: Int = 100
}
