object ClosureTest {
  def main(args: Array[String]): Unit = {
    val f = suffix("jpg")
    println(f("cat.jpg"))
  }

  def suffix(suff:String)={
    (fileName:String) =>{
      if(fileName.endsWith(suff)) fileName else fileName+suff
    }
  }
}
