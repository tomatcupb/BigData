import scala.beans.BeanProperty

object CatDemo {
  def main(args: Array[String]): Unit = {
    val dog = new Dog("jack",10)
    println(dog)
  }
}

class Cat{
  var name:String = _
  var age:Int = _
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
