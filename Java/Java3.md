### 注解

#### 元注解

负责注解其他注解。有四种meta-annotation类型，他们被y用来提供对其他注解类型作说明

```java
@Target
@Retention
@Documented
@Inherited
```

##### @Target

用于描述注解的使用范围

| 所修饰范围                 | 取值                                                        |
| -------------------------- | ----------------------------------------------------------- |
| package包                  | PACKAGE                                                     |
| 类、接口、枚举、Annotation | TYPE                                                        |
| 类型成员                   | CONSTRUCTOR:构造器; FIELD: 用于描述域; METHOD: 用于描述方法 |
| 方法参数和本地变量         | LOCAL_VARIABLE: 用于描述局部变量; PARAMETER: 用于描述参数   |

##### @Retention

| 取值 RetentionPolicy | 作用                             |
| -------------------- | -------------------------------- |
| SOURCE               | 在源文件中有效                   |
| CLASS                | 在class文件中有效                |
| RUNTIME              | 在运行时有效，可以被反射机制读取 |

```java

@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.SOURCE)
public @interface Annotation {
    String name();
}

public class Test {
    
    @Annotation(name="修文李")
    public void test01(){}
}
```

#### 读取注解

````java
//与数据库表对应
@Target(value=ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
@interface AnnoTable{
    String value();
}

//与数据库字段对应
@Target(value=ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
@interface AnnoField{
    String column();
}


//使用注解
@AnnoTable("st_tablt")
public class Table {

    @AnnoField(column = "id")
    Integer id;
}


//读取注解信息
public class HandleAnnoTation {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        Class obj = Class.forName("annotation.Table");
        AnnoTable anno = (AnnoTable)obj.getAnnotation(AnnoTable.class);
        System.out.println(anno.value());

        Field f = obj.getDeclaredField("id");
        AnnoField annoField = f.getAnnotation(AnnoField.class);
        System.out.println(annoField.column());
    }
}

````



### JVM核心

#### JVM类加载

JVM把class文件加载到内存，并对数据进行校验、解析和初始化，最终形成JVM可以直接使用的Java类型的过程。

1. 加载

将class文件字节码内容加载到内存中，并将这些静态数据结构转换成**方法区**中的运行时数据结构（**静态变量、静态方法、常量池**（类名，字符串，实值）、类的代码），在**堆**中生成一个代表这个类的java.lang.Class对象（反射对象），作为方法区数据结构的访问入口。这个过程需要类加载器参与。

2. 链接

将Java类的二进制代码合并到JVM的运行状态之中的过程

验证：确保加载的类信息符合JVM规范，没有安全方面的问题。

准备：正式为类变量(static变量)分配内存并设置类变量初始值的阶段，这些内存都将在方法去中进行分配。

解析：虚拟机常量池内的符号引用替换为直接引用的过程。

3. 初始化

- 初始化阶段是执行**类构造器**方法的过程。类构造器方法是由编译器自动收集类中的所有**类变量的赋值动作和静态语句块中的语句合并产生的**。
- 当初始化一个类的时候，如果发现其父类还没有进行过初始化、则需要先触发其父类的初始化
- 虚拟机会保证一个类的方法在多线程环境中被正确加锁和同步（初始化时一定是线程安全的）
- 当访问一个Java类的静态域时，只有真正声明这个域的类才会被初始化。
- 接口中**不能**使用static块，但是接口仍然有变量初始化的操作，因此接口也会生成<clinit>方法。但接口和类不同的是，不会先去执行继承接口的<clinit>方法，而是在调用父类变量的时候，才会去调用<clinit>方法。接口的实现类也是一样的。

![ Javaåå­åºååé](https://img-blog.csdn.net/201807232103340?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h4aGFhag==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

#### 静态域

- 将域定义成static后，每个类中只有一个这样的域，与类相关的，也称为类成员。但是每个对象对于所有的实例域却都有自己的一份拷贝。
- 静态域会随着类的加载而加载并初始化，存在于方法区内存中的字节码文件的静态区域中。
- 优先于对象存在，**先有方法区的类加载**，后才可能会有堆内存的对象实例化。
- 静态域会被所有的对象共享，也称为共享区。
- 一般共性用静态，特性用非静态。
- 一般通过类名直接调用，虽然也可以通过对象名调用，但是不推荐，也不合适。

#### 初始化时机

类的主动引用（一定会发生类的初始化）

- new一个类的对象
- 调用类的静态方法或静态变量(除了**final**常量,因为引用了常量池,和类无关)
- 使用java.lang.reflect包的方法对类进行反射调用
- 当虚拟机启动,java Hello, 则一定h会初始化Hello类,就是初始化main方法所在的类
- 初始化一个类时,先初始化它的父类

被动引用(不会发生类的初始化)

- 访问一个静态域时，初始化这个域从属的那个类
- 通过数组定义类引用，不会触发此类的初始化
- 引用常量不会触发此类的初始化（常量在编译阶段就存入调用类的常量池中）

```java
public class TestJavassist {
    public static void main(String[] args) {
        String str = "aa";
        A.hello();
        System.out.println("-------");
        A a = new A();
        A b = new A();
    }
}

class AFather{
    static {
        System.out.println("父类静态初始化块");
    }
}

class A extends AFather{
    public static int cnt;

    public int height = 20;

    static{
        System.out.println("静态初始化块A");
        cnt = 0;
    }

    {
        height = 20;
        cnt++;
        System.out.println("对象初始化块");
    }

    public A(){
        //先执行了静态初始块
        System.out.println(cnt);
        System.out.println("创建类的对象");
    }

    static void hello(){
        System.out.println("类的静态方法");
    }
}
```

类缓存：标准的Java SE类加载器可以按要求查找类，但一旦被加载，会维持一段时间。不过JVM垃圾收集器可以回收这些Class对象。

#### 加载器树状结构

![](D:\技术\学习笔记\Java\加载器扩展1.PNG)

```java
public class ClassLoaderTest {
    public static void main(String[] args) {

        //扩展类加载器
        System.out.println(ClassLoader.getSystemClassLoader());
        //引导类加载器
        System.out.println(ClassLoader.getSystemClassLoader().getParent());
        //null
        System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent());

        //应用程序类加载器
        System.out.println(System.getProperty("java.class.path"));

    }
}
```

#### 双亲委托机制

代理模式：一般交给其他加载器来加载指定的l类

双亲委托机制：（更安全）

某个特定的类加载器在接到加载类的请求时，首先将加载任务委托给父类加载器。父类若可以加载，就成功返回；只有父类无法加载时，才自己去加载。因此，**核心类自己编写也加载不了**，保证了Java核心库的类型安全。

双亲委托模式时代理模式的一种

- 并不是所有的类加载器都采用双亲
- tomcat服务器类加载器也使用代理模式，所不同的是它是首先尝试区加载某个类，找不到再代理给父类加载器，这与一般的是相反的。

```java
public class ClassLoaderTest02 {
    public static void main(String[] args) {
        String a = "bbb";
        System.out.println(a.toString());
        //当前的String类是JAVA_HOME/jre/lib下的rt.jar包中的类
        System.out.println(a.getClass().getClassLoader());
        System.out.println(a);
    }
}
```



### 设计模式

创造型：

- 单例、工厂、抽象工厂、建造者、原型

结构型：

- 适配器、桥接、装饰、组合、外观、享元、代理

结构型：

- 模版、命令、迭代器、观察者、中介者、备忘录、解释器、状态、策略、职责链、访问者



#### 1. 单例模式

保证一个类只有一个实例，并且提供一个访问该实例的全局访问点。

常见应用场景：

- 任务管理器
- 回收站
- 项目中读取配置文件的类
- 网站的计数器，一般也是采用单例模式实现，否则难以同步
- 应用程序的日志应用，一般都可用单例模式实现
- 数据库连接，因为它也是一种数据库资源
- 操作系统的文件系统
- Servlet中的Application对象
- Spring中的每个Bean，易于容器管理
- Servlet单例
- Spring MVC的控制对象

**优点** ：

由于单例模式之生成一个实例，减少了系统开销，需要较少资源。如读取配置、产生其他依赖对象时，则可以通在应用启动启动时产生一个单例对象，然后永久驻留内存。

##### 五种实现方式

- 饿汉式（线程安全，调用效率高。不能延时加载）
- 懒汉式（线程安全，调用效率不高。可以延时加载）

非常用：

- 双重检测锁式（由于JVM底层内部模型的原因，不建议使用，*在多线程那节出现过代码）
- 静态内部类式（线程安全，调用效率高，并且可以延时加载）
- 枚举单例（线程安全，调用效率高，不能延时加载，可天然防止反射和反序列化漏洞）

饿汉式：

```java
public class Hunger {
    private static Hunger s = new Hunger();

    public static void main(String[] args) {
        String a = new String("aaa");
        StringBuffer sb = new StringBuffer("aaa");
        sb.append(1);
        System.out.println(sb);
    }
    //构造器私有化
    private Hunger() {

    }

    public static synchronized Hunger getInstance(){
        return s;
    }
}
```

懒汉式：

```java
public class Lazier {
    private static Lazier instance = null;

    private Lazier(){}

    //必须同步
    public static synchronized Lazier getInstance(){
        if(instance == null){
            instance = new Lazier();
        }
        return instance;
    }
}
```

静态内部类：

```java
public class StaticInnerClass {
    //静态内部类
    private static class SingletonInstance{
        private static final StaticInnerClass instance = new StaticInnerClass();
    }

    public static StaticInnerClass getInstance(){
        return SingletonInstance.instance;
    }

    private StaticInnerClass(){}
}
```

- 外部类没有static属性，则不会像饿汉式那样立即加载
- 只有调用getInstance()，才会加载静态内部类。加载类时是线程安全的，instance是static final类型，保证了内存中只有这样一个实例存在，而且只能被赋值一次，从而保证了线程安全性。
- 兼备延迟加载和并发高效的优势

##### 防止反射和反序列化破解

反射破解：

```java
public class Client {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Singleton06 s1 = Singleton06.getInstance();
        Singleton06 s2 = Singleton06.getInstance();
        System.out.println(s1==s2);

        Class<Singleton06> obj = (Class<Singleton06>) Class.forName("Singleton.Singleton06");

        Constructor<Singleton06> c = obj.getDeclaredConstructor(null);
        c.setAccessible(true);                  //跳过权限检查
        Singleton06 s3 =(Singleton06) c.newInstance();
        System.out.println(s1==s3);
    }
}
```

解决办法：

```java
public class Singleton06 {
    private static Singleton06 instance = null;

    private Singleton06() {
        if(instance != null){
            throw new RuntimeException();
        }
    }

    //必须同步
    public static synchronized Singleton06 getInstance(){
        if(instance == null){
            instance = new Singleton06();
        }
        return instance;
    }
}
```

反序列化破解：

```java
public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Singleton06 s1 = Singleton06.getInstance();


        FileOutputStream fos = new FileOutputStream("D:/a.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(s1);
        oos.flush();
        oos.close();
        fos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:/a.txt"));
        Singleton06 s2 = (Singleton06) ois.readObject();
        System.out.println(s1 == s2);
    }
}
```

解决方法

```java
public class Singleton06 implements Serializable {
    private static Singleton06 instance = null;

    private Singleton06() {}

    //必须同步
    public static synchronized Singleton06 getInstance(){
        if(instance == null){
            instance = new Singleton06();
        }
        return instance;
    }

    //readResolve方法直接返回此方法指定的对象，不需要单独创建对象
    private Object readResolve(){
        return instance;
    }
}
```



#### 2. 工厂模式

实现了创建者和调用者的分离

核心本质：

- 实例化对象，用工厂方法代替new操作
- 将选择实现类、创建对象统一管理和控制。从而将调用者跟我们的实现类解耦。

简单工厂模式：用来生产同一等级结构中的**任意**产品（对于增加新的产品，需要修改已有的代码）

工厂方法模式：用来生产同一等级结构中的**固定**产品（支持增加任意产品）

抽象工厂模式：用来增加不同产品族的全部产品（不能增加新的产品；支持增加产品族）

##### 面向对象的基本原则

开闭原则；迪米特原则

##### 简单工厂模式

没有满足开闭原则，增加新产品需要修改类的代码

```java

public class CarFactory {
    public static Car createCar(String type){
        if(type.equals("奥迪")){
            return new Audi();
        }else if(type.equals("奔驰")){
            return new Benz();
        }else{
            return null;
        }
    }
}

//或
public class CarFactory {
    public static Car createAudi(){
        return new Audi();
    }
    
    public static Car createBenz(){
        return new Benz();
    }
}


```

![](D:\技术\学习笔记\Java\设计模式\factory\简单工厂.PNG)

##### 工厂方法模式

工厂方法模式有一组实现了相同接口的工厂类

扩展时增加接口，因此并不完美

```java
//此时想增加产品只需增加接口

public interface CarFactory {
    Car createCar();
}

public class BenzFactory implements CarFactory {
    @Override
    public Car createCar() {
        return new Benz();
    }
}

public class AudiFactory implements CarFactory {
    @Override
    public Car createCar() {
        return new Audi();
    }
}

```

![](D:\技术\学习笔记\Java\设计模式\factory\工厂方法.PNG)

##### 抽象工厂模式

用来生产不同产品族的全部产品。对于增加新产品无能为力。

```java

public interface CarFactory {
    Engine createEngine();
    Seat createSeat();
    Tyre createTyre();
}

public class LowCarFactory implements CarFactory {

    @Override
    public Engine createEngine() {
        return new LowEngien();
    }

    @Override
    public Seat createSeat() {
        return new LowSeat();
    }

    @Override
    public Tyre createTyre() {
        return new LowTyre();
    }

}

public class LuxuryCarFactory implements CarFactory {
    @Override
    public Engine createEngine() {
        return new LuxuryEngine();
    }

    @Override
    public Seat createSeat() {
        return new LuxurySeat();
    }

    @Override
    public Tyre createTyre() {
        return new LuxuryTyre();
    }
}
```

##### 应用场景

- JDK中Calendar的getInstance方法
- JDBC中Connection对象的获取
- Hibernate中SessionFactory创建Session
- SpringIOC中容器创建管理bean对象
- XML解析中的DocumentBuilderFactory创建解析器对象
- 反射中Class对象的newInstance()

#### 3. 建造者模式

分离了对象子组件的单独构造（由Builder负责）和装配（由Director负责）。从而可以构造出复杂的对象。这个对象适用于：某个对象的构建过程复杂的情况下使用。

实现了构建和装配的解耦。

![](D:\技术\学习笔记\Java\设计模式\建造者\UML.PNG)

#### 4. 原型模式 prototype

通过new产生一个对象需要非常繁琐的数据准备或访问权限，此时可使用原型模式。

使用java中的克隆技术，效率高

和new的区别：new出的对象采用的是默认值，克隆出的对象值完全和原型对象相同。

实现：

- Cloneable接口和clone方法
- 序列化和反序列化

使用场景：

- 很少单独出现，一般和工厂方法模式一起，通过clone的方法创建一个对象，然后由工厂方法提供给调用者
- spring中bean的创建实际就是两种：单例模式和原型模式（当然，原型模式需要和工厂模式搭配起来）

当new耗时很大时，使用原型模式效率更高。





#### 结构型模式

核心作用：从程序的结构上实现松耦合，从而可以扩大整体的类结构，用来解决更大的问题

#### 5. 适配器模式

##### 角色

目标接口：客户所期待的接口，可以是具体或抽象的类或接口 （USB）

需要适配的类：需要适配的类或者适配者类 （PS2）

适配器：通过包装一个需要适配的对象，把原有接口转换成目标接口。（适配器）



两种实现方式：类适配器（继承）、对象适配器（组合）

使用场景：

- java.io.InputSteamReader(InputStream),
- 旧系统改造和升级

#### 6. 代理模式

通过代理，控制对对象的访问，可以详细控制访问某个对象的方法。在调用前做前置处理，在调用后做后置处理

AOP的核心机制。

![](D:\技术\学习笔记\Java\设计模式\代理-1.PNG)

##### 核心角色

抽象角色：定义代理角色和真实角色的公共对外方法。

真实角色：实现抽象角色，定义真实角色索要实现的业务逻辑；**关注真正的业务逻辑**

代理角色：实现抽象角色，是真是角色的代理，通过真实角色的业务逻辑来实现抽象方法，并附加自己的操作。

**将统一的流程控制方法代理角色中处理**

应用场景：

- 安全代理
- 远程代理：通过代理类实现远程方法调用
- 延迟加载：先加载轻量级的代理对象，真正需要再加载真实对象



##### 静态代理



##### 动态代理

使用

- javaassist字节码操作库实现
- CGLIB
- ASM(底层使用指令，可维护性较差)

优点：