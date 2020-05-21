object HighOrderFun {
  def main(args: Array[String]): Unit = {
    val list2 = List("Tom","Jerry","Spike","Tuffy")
    println(list2.map(upper)) // List(TOM, JERRY, SPIKE)
    println(list2.flatMap(upper)) // List(T, O, M, J, E, R, R, Y, S, P, I, K, E)
    println(list2.filter(startWithA))

    val list1 = List(1,2,3,4)
    /** (f: (B, A) => B)
      * 先执行f(B,A)，再把得到的返回值作为接下来的参数B,即f(f(B,A),A),递归
      * 执行过程
      * 1. 1-2
      * 2. (1-2)-3
      * 3. ((1-2)-3)-4
      */
    println(list1.reduceLeft(minus))

    /**
      * def reduce[A1 >: A](op: (A1, A1) => A1): A1 = reduceLeft(op)
      * reduce <=>reduceLeft
      */
    println(list1.reduce(minus))

    /** (op: (A, B) => B)
      * 1. 3-4
      * 2. 2-(3-4)
      * 3. 1-(2-(3-4))
      *
      */
    println(list1.reduceRight(minus))


    /**(z: B)(op: (B, A) => B)
      * list1.foldLeft(5)(minus) 可以理解成：List(5,1,2,3,4).reduceLeft(minus)
      * 缩写：/:
      * var i = (10/:list)(minus) <=>list.foldLeft(10).reduceLeft(minus)
      */
    println(list1.foldLeft(5)(minus))
    println((5/:list1)(minus))
    /**
      * list1.foldRight(5)(minus) 可以理解成 List(1,2,3,4,5).reduceRight(minus)
      * 缩写：:\
      * var i = (list:\10)(minus) <=>list.foldRight(10).reduceLeft(minus)
      */
    println(list1.foldRight(5)(minus))
    println((list1:\5)(minus))


    /** (z: B)(op: (B, A) => B)
      * 对集合做fold操作，但是保留中间结果
      * 5
      * 5, (5-1)
      * 5, (5-1), ((5-1)-2)
      * 5, (5-1), ((5-1)-2), (((5-1)-2)-3)
      * 5, (5-1), ((5-1)-2), (((5-1)-2)-3), ((((5-1)-2)-3)-4)
      */
    println(list1.scanLeft(5)(minus))
    /** 5
      * (4-5),5
      * (3-(4-5)),(4-5),5
      * (2-(3-(4-5))), (3-(4-5)),(4-5),5
      * (1-(2-(3-(4-5)))),(2-(3-(4-5))), (3-(4-5)),(4-5),5
      */
    println(list1.scanRight(5)(minus))

    // List((1,Tom), (2,Jerry), (3,Spike), (4,Tuffy))
    // List(Tuple2, Tuple2...)
    println(list1.zip(list2))






  }

  def upper(str: String): String ={
    str.toUpperCase
  }

  def startWithA(str: String): Boolean ={
    str.startsWith("T")
  }

  def minus(num1:Int, num2:Int): Int ={
    num1-num2
  }

}
