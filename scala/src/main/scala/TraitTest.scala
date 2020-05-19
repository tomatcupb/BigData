object TraitTest {
  def main(args: Array[String]): Unit = {
//    val stu =  new PrimaryStu with Student with Human

    /**
      * PrimaryStu
      * person
      * student
      * creature
      * human
      */
  }
}

class PrimaryStu{
  println("PrimaryStu")
}

class Person extends Creature {
  println("person")
}

class Creature{
  println("creature")
}

trait Student extends Human {
  println("student")
}

trait Human extends Creature {
  println("human")
}
