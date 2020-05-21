import scala.collection.mutable

object CollectionTest3 {
  def main(args: Array[String]): Unit = {
    val str = "AAAAAAAAAAAAAAABBBBBBBBBCCCCCCCCCCDDDDDDDD"
    println(str.foldLeft(Map[Char,Int]())(charCount))

    val words = List("hello world","one world one dream","hello one")
    println(words.flatMap(spitWord).foldLeft(Map[String, Int]())(wordCount))
  }

  def charCount(map: Map[Char, Int], c:Char): Map[Char, Int] ={
    map+(c->(map.getOrElse(c,0)+1))
  }

  def spitWord(word:String)={
    word.split(" ")
  }

  def wordCount(map: Map[String, Int], word:String): Map[String, Int] ={
    map + (word->(map.getOrElse(word,0)+1))
  }


}
