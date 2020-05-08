# scala面向对象
- Java虽然是面向对象语言，但是由于历史原因，存在一些非面向对象的内容：基本类型，null，静态方法等
- Scala中，一切皆对象
- 案例分析
    - CatDemo.scala
    ```scala
    object CatDemo {
      def main(args: Array[String]): Unit = {
        val cat = new Cat
        cat.name = "小白"//底层：cat.name_$eq("小白")
        cat.age = 3//底层：cat.age_$eq(3)
        //cat.name:底层cat.name()
        printf("this is %s, it is %d years old", cat.name, cat.age)
      }
    }
    
    // class Cat对应的字节码文件只有Cat.class,默认是public
    class Cat{
      /**
      * 当声明var name:String = _，字节码对应private String name;
      * 同时生成两个方法:
      *   public String name()（相当于java的getter()）
      * ``  public void name_$eq(String x$1)（相当于java的setter()）
      */
      var name:String = _
      var age:Int = _
    }
    ```
    - Cat.class
    ```java
    public class Cat
    {
      private String name;
      private int age;
    
      public String name()
      {
        return this.name; } 
      public void name_$eq(String x$1) { this.name = x$1; } 
      public int age() { return this.age; } 
      public void age_$eq(int x$1) { this.age = x$1; }
    
    }
    ```
- scala源文件可包含多个类，且默认为public

- 属性
    - [访问修饰符] var 属性名[:类型] = 初始值
    - scala声明属性，需要显示初始化
    
- 构造器
    - 形式：[修饰符] 方法名(参数列表){}
    - scala有主构造器和辅构造器
    ```
    class 类名(参数列表){ // 主构造器
        def this(参数列表) //辅构造器
        def this(参数列表) //辅构造器
        def this(参数列表) //辅构造器可以有多个，函数名为this，编译器通过参数列表来区分
    }
    
    class 类名{ // 主构造器
        def this(参数列表) //辅构造器
        def this(参数列表) //辅构造器
        def this(参数列表) //辅构造器可以有多个，函数名为this，编译器通过参数列表来区分
    }
    ```
    - 主构造器会执行类定义中的所有语句(除了方法)。这里体现了oop与函数式编程思想的融合：即构造器也是方法(函数)
        - 主构造器
        ```scala
        class Dog(inName:String, inAge:Int, inGender:Boolean){
          var name = inName
          var age = inAge
          var gender:Boolean = _
        
          age += 10
          println("======")
        
          override def toString: String = {
            "its name is " + name + "，it is "+age+" years old"
          }
        }
        ```
        - 编译后的字节码
        ```
          public Dog(String inName, int inAge, boolean inGender)
          {
            this.name = inName;
            this.age = inAge;
        
            age_$eq(age() + 10);
            Predef..MODULE$.println("======");
          }
        ```
    - 若主构造器无参数，可省略小括号，构建对象的构造方法也可以省略小括号
    - 如果想让用户只能通过辅助构造器，可以主构造器成为私有的
        ```scala
        class Dog private(inName:String, inAge:Int){}
        ```
    - 辅构造器可以有多个，函数名为this，编译器通过参数列表来区分
        ```scala
          def this(name:String){
            /**
             * 第一行必须直接或者间接调用父类构造器！！！
             * 这是为了保持继承关系.若这个Dog类继承了Animal类，调用主构造器的时候会先调用父类构造器super(),保持继承关系
             * 如果辅助构造器没有调用主构造器直接操作的话，将会割裂与父类的关系
            */
            this("jack",10) //直接调用主构造器
            this.name = name
          }
          
          def this(age:Int){
            this("rose") //间接调用主构造器，this("haha")会调用主构造器
            this.age = age
          }
        ```
    - 辅助构造器不能和主构造器声明一致(即形参一致)，发生构造器重复错误（cannot resolve constructor）
    - 主构造器的形参修饰符（var,val）
        - 如果主构造器的形参没有修饰符（var,val）,这个形参是一个局部变量
        - 如果主构造器的形参是val修饰，这个参数会作为类私有的只读属性
        - 如果主构造器的形参是var修饰，
        - scala代码
        ```scala
        class Dog (val inName:String, var inAge:Int){
          var name = inName
          var age = inAge
        }
        ```
        - 编译字节码
        ```java
        public class Dog{
            private final String inName;
            private int inAge;
            
            public String inName(){return this.inName; } 
            public int inAge() { return this.inAge; } 
            public void inAge_$eq(int x$1) { this.inAge = x$1; } 
          }
        ```
    - Bean属性
        - 为了实现与java的相似性，将属性字段XXX加上注解@BeanProperty可隐式生成getXXX()和setXXX()方法
        - 与底层原生的XXX(),XXX_$eq()不冲突，可以共存
    - 对象创建流程分析（待补充）