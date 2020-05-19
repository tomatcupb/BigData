import scala.beans.BeanProperty

object CatDemo {
  def main(args: Array[String]): Unit = {
//    val dog = new Dog("jack",10)
//    println(dog)
    val animalCat:Animal = new Cat
    println(animalCat.name)
    testFood(new Cat)
    testFood(new Pig)

  }

  def testFood(animal: Animal):Unit={
    animal.eat()
  }
}

class Cat extends Animal {
  override val name = "cat"
  var age:Int = _

  override def eat(): Unit ={
    println("cat eat fish")
  }
}

class Animal{
  val name = "animal"
  private var gender:String = _

  def eat(): Unit ={
    println("animal is hungry")
  }
}


class Pig extends Animal {
  override def eat(): Unit = {
    println("pig eat everything")
  }
}

class Dog (val inName:String, var inAge:Int){
  var name = inName
  var age = inAge

  def this(name:String){
    this("jack",10)
    this.name = name
  }


  override def toString: String = {
    "its name is " + name + "，it is "+age+" years old"
  }
}
