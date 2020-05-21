object RecursiveTest {
  def main(args: Array[String]): Unit = {
    // 计算1+..50
    println(Range(1,101).reduce(_+_))
    println(sum2(100))
  }

  def sum(num1:Int, num2:Int): Int ={
    num1+num2
}

def sum2(num2:Int, num1:Int=1): Int ={
  if(num2>num1) num2+sum2(num2-1, num1) else num2
}
}
