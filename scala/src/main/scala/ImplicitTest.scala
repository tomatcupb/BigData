object ImplicitTest {
  def main(args: Array[String]): Unit = {

    implicit def SQLToDB(mySQL: MySQL):DB={
      return new DB
    }

    val sql = new MySQL
    sql.remove()


    implicit val str:String = "hello"
//    implicit val str2:String = "TEST" //只能有一个对应类型的，否则运行报错
    def sayHello(implicit greeting:String="hi"): Unit ={
      println(greeting)
    }

    sayHello

  }
}


class DB{
  def insert(): Unit ={

  }

  def update(): Unit ={

  }

  def remove(): Unit ={

  }
}

class MySQL{
  def insert(): Unit ={

  }
}
