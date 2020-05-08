import scala.io.StdIn
import util.control.Breaks._
object Hello {
  def main(args: Array[String]): Unit = {
    table(9)
  }

  def table(num:Int): Unit ={
    for(i <- 1 to num){
      for(j <- 1 to i){
        printf("%d*%d=%d\t",i,j,i*j)
      }
      println()
    }
  }
}
