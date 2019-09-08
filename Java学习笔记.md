## Java 学习笔记

### 一、基础

#### 运算符

逻辑运算符：&和&&都能表示逻辑与，但是&&是短路运算，即之前的表达式如果能确定整个表达式的逻辑值，就会停止后面的运算以提高效率。

位运算符：&既能表示逻辑运算，也能表示位运算

字符串连接符：3+"4" = "34"

优先级：逻辑非>逻辑与>逻辑或



#### 基本数据类型

| 序号 | 数据类型        | 位数 | 默认值 | 取值范围                | 举例说明          |
| ---- | --------------- | ---- | ------ | ----------------------- | ----------------- |
| 1    | byte(位)        | 8    | 0      | -2^7 - 2^7-1            | byte b = 10;      |
| 2    | short(短整数)   | 16   | 0      | -2^15 - 2^15-1          | short s = 10;     |
| 3    | int(整数)       | 32   | 0      | -2^31 - 2^31-1          | int i = 10;       |
| 4    | long(长整数)    | 64   | 0      | -2^63 - 2^63-1          | long l = 10l;     |
| 5    | float(单精度)   | 32   | 0.0    | -3.40E+38 ~ +3.40E+38   | float f = 10.0f;  |
| 6    | double(双精度)  | 64   | 0.0    | -1.79E+308 ~ +1.79E+308 | double d = 10.0d; |
| 7    | char(字符)      | 16   | 空     | 0 - 2^16-1              | char c = 'c';     |
| 8    | boolean(布尔值) | 8    | false  | true、false             | boolean b = true; |

##### 自动类型转换

自动类型转换指的是**容量小**的数据类型可以自动转换为**容量大**的数据类型（注意不是字节大小，而是容量大小）

![](D:\技术\学习笔记\Java\类型转换.PNG)

*实线表示无数据丢失的自动类型转换，虚线表示在转换时可能有精度上的损失。

*可以将整型常量直接赋值给byte、short、char等类型变量，而不需要进行强制类型转换，只要不超出其表数范围即可。

```java
short b = 12;			//合法
short b = 1234567;		//非法
```

##### 强制类型转换

又称造型。有可能丢失信息的情况下进行的转换是通过造型来完成的，但可能造成精度降低或溢出。

```java
int a = 100000000;
int b = 20;
int total = a*b ;			//溢出
Long total2 = a*b;			//溢出，存储a*b的临时变量还是int
Long total3 = a*(Long)b;		//合法

//可以通过提前改变变量的类型来确保正确性
```



#### 控制语句

带标签的break和continue

```java
outer:for(int i=101; i<150;++i){
    for(int j = 2;j< i/2;++j){
        if(i %j == 0){
			continue outer;
        }
    }
    System.out.println("质数:"+i);
}
```

### 二、面向对象

类：抽象的概念，不是具体存在的东西，对象的模板，静态的概念

对象：类的具体，实际存在的东西，动态的概念

引用类型：数组、对象、接口

#### 内存分析

Java虚拟机的内存可分为三个区域：栈stack、堆heap、方法区method area、程序计数器、本地方法栈(Native Method Stack)

栈：

1. 描述的是**方法执行的内存模型**，每个方法被调用都会创建一个栈帧（存储局部变量、操作数、方法出口等）
2. 局部变量表中存放了编译期间可知的各种基本数据类型和引用对象（maybe一个指向对象起始地址的引用指针），float和double占两个局部变量空间，其余只占一个。
3. JVM为**每个线程**创建一个栈，用于存放该线程执行方法的信息（实参、局部变量等），栈属于**线程私有**，不能实现线程间共享。
4. FILO
5. 由系统自动分配，**速度快**！栈是一个**连续**的内存空间。
6. 线程请求深度大于虚拟机栈所允许的深度，将抛出StackOverflowError；若虚拟机栈动态扩展时无法申请到足够的内存就会抛出OutOfMemoryError异常。

堆：

1. 堆用于存储创建好的**对象和数组**（数组也是对象）。
2. JVM只有一个堆，被**所有线程共享**。
3. 堆是一个不连续的内存空间，分配灵活，**速度慢**。

方法区（静态区）：

1. JVM只有一个方法区，被**所有线程共享**。
2. 方法区实际也是堆，只是用于存储类、常量相关的信息
3. 用来存放程序中永远不变或唯一的内容（代码、类信息、静态变量、字符串常量）

本地方法栈：执行本地方法的栈，其他与虚拟机栈很相似

程序计数器：

可视作行号指示器。分支、跳转、循环、异常处理、线程恢复等都要依赖这个计数器来完成。多线程执行时，在任何一个确定时刻，一个处理器只会执行一个线程中的指令。为了线程切换后能恢复到正确的执行位置，每个线程都需要一个独立的计数器。java方法记录的是虚拟机字节码指令的地址。

总结：

其中程序计数器、虚拟机栈、本地方法栈是每个线程私有的内存空间，随线程而生，随线程而亡。例如栈中每一个栈帧中分配多少内存基本上在类结构确定是哪个时就已知了，因此这3个区域的内存分配和回收都是确定的，无需考虑内存回收的问题。

但方法区和堆就不同了，一个接口的多个实现类需要的内存可能不一样，我们只有在程序运行期间才会知道会创建哪些对象，这部分内存的分配和回收都是动态的，GC主要关注的是这部分内存。

![](D:\技术\学习笔记\Java\内存模型.PNG)

#### 构造器

要点：

1. 要通过new 调用
2. 返回值为本类对象，但是不能定义返回值类型，不能在构造器中使用return返回某个值
3. 如果没有定义构造器，编译器自动定义一个无参的构造函数。如果已定义则编译器不会添加
4. 构造器名字必须和类名一致
5. 构造方法的**第一句总是super()**

创建一个对象的过程：

1. 分配对象空间，并将对象成员变量初始化为0或空
2. 执行属性值的显式初始化
3. 执行构造方法
4. 返回对象的地址给相关的变量

#### 垃圾回收机制

garbage collection

对象空间的释放：将对象赋值null即可。垃圾回收器将负责回收所有“不可达”对象的内存空间。

##### 1. 回收过程

1. 发现无用对象
2. 回收无用对象占用的内存空间

*无用对象：没有任何变量引用该对象

##### 2. 相关算法

1. 引用计数器：每个对象都设一个引用计数，被引用一次，计数+1。

   - 优点：算法简单

   - 缺点：”循环引用的无用对象“无法识别

   - ```java
     Student s1 = new Student()；
     Student s2 = new Student()；
     
     s1.friend = s2;
     s2.friend = s1;
     
     s1 = null;
     s2 = null;
     //此时s1和s2引用计数不为0，但实际已经无用，但无法被识别
     ```

2. 引用可达法（根搜索算法）

   - 从根节点开始，不断递归寻找其引用节点，找完之后剩余的节点则被认为是没有被引用的节点。

##### 3. 分代垃圾回收机制

基于这样的事实：不同的对象的生命周期是不一样的。因此，不同生命周期的对象可以采取不同的回收算法，以提高效率。分为三种状态：年轻代、年老代、持久代。JVM将堆内存划分为Eden、Survivor和Tenured/Old空间。



#### 关键字

##### this

常用用法：

1. 产生二义性时指明当前对象
2. 调用重载的构造方法，避免相同的初始化代码，只能在构造方法中用，并且必须位于构造方法的第一句

构造器中调用另一构造器

```java
A(int a, int b){
    this.a = a;
    this.b = b;
}

A(int a,int b,int c){
    this(a,b);				//this调用另一构造器
    this.c = c;
}
```

3. this**不能**用于static方法中。

##### static

类变量的生命周期和类相同，在整个应用程序执行期间都有效。

static 需放在方法的类型前面

##### 静态初始化块

用于类的初始化操作，不可使用this关键字，不能访问属于实例的属性和方法。在继承关系中，先调用父类的静态初始化块。

```java
public class Son extends Father {
    static int sNum = 0;
    int num = 0;
    public static void main(String[] args){
        System.out.println("-----");
        new Father();
        System.out.println("-----");
        new Son();
        System.out.println("-----");
        System.out.println("sNum = " + sNum);
    }
    
    public Son() {
        System.out.println("Son构造方法");
    }
    
    static {
        //num = 1;    //报错：无法为非静态变量赋值
        sNum = 1;
        System.out.println("Son静态初始化器");
    }
}
```

#### 包

一定是非注释性语句的第一句

java.lang包不用导入

```java
import java.util.*;				//导入该包下所有类，会降低编译速度，但不降低运行速度

import static java.lang.Math.*;			//导入Math类下所有静态属性
import static java.lang.Math.PI;		//导入Math下的静态属性PI

//之后便可直接使用，不需要加类名

System.out.println(PI);
```

#### 继承

1. 子类继承父类，可以得到父类的全部属性和方法（除了父类的构造方法）
2. 没有调用extends方法则它的父类是：java.lang.Object

##### 1. 重写

需要符合的要求：

1. 方法名、形参列表**相同**
2. 返回值类型、声明异常类型，子类**小于等于**父类
3. 访问权限子类**大于等于**父类

小于是指继承关系中在下层

##### 2. Object类

所有java类的根基类，所有java对象都有Object类的属性和方法！

toString()：System.out.println等输出时调用的方法

==：比较双方是否相同。若是基本类型则需值相等；引用类型则需地址相同

equals：Object类中提供的方法，判断对象内容是否相同

##### 3. super

可通过super访问父类中被子类覆盖的方法或属性

构造方法的第一句总是默认super(...)调用父类的构造方法，写与不写都是这样

##### 4. 优先级

在继承链中对象方法的调用存在一个优先级：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)。



#### 封装

访问权限

| 修饰符    | 同一个类 | 同一个包 | 子类 | 所有类 |
| --------- | -------- | -------- | ---- | ------ |
| private   | *        |          |      |        |
| default   | *        | *        |      |        |
| protected | *        | *        | *    |        |
| public    | *        | *        | *    | *      |

#### 多态

1. 多态是方法的多态
2. 多态的存在必须有三个必要条件：继承、方法重写、父类引用指向子类对象
3. 父类引用指向子类对象后，用该父类引用调用子类重写的方法，就出现多态。

强制向下转型

```java
Animal d = new Dog();			//此时d不能调用Dog中新写的方法，只能调用Animal类中的方法

Dog d2 = (Dog)d;
```

##### final

可修饰

变量：被修饰的变量不可修改，被赋了初值之后就不能被重新赋值

方法：**不可被子类重写**，但可以被重载

类：修饰的类不能被继承。如Math、String等



#### 数组

数组变量属于**引用类型**，数组可看成对象，每个元素看作成员。

默认初始化

```java
//基础数据类型
int [] str = new int[10];
```

静态初始化

```java
int []a = {2,4,6};
User []b = {
    new User(1001,"aa");
    new User(1002,"bb");
}
```

动态初始化

```java
//引用数据类型
User []mans = new User[2];
mans[0] = new User("...");
mans[1] = new User("...");
```

for each遍历：用于读取数组元素的值，不能修改元素的值

```java
for(int m : a){
    //print
}
```



#### 抽象类

抽象方法：使用abstract定义的方法，没有方法体，只有声明。

抽象类：包含抽象方法的类。通过abstract定义。限制子类的实现，子类继承之后必须实现这些抽象方法。不能new创建对象

#### 接口

接口中只有：抽象方法。接口完全面向规范。

接口和实现类不是父子关系，是实现规则的关系。

定义的详细说明：

1. 接口中的访问修饰符只能是public或默认
2. extends：接口可以多继承
3. 常量：接口中的属性只能是常量，总是**public static final**修饰
4. 方法：只能是public abstract。省略的话，也是**public abstract**

Gist:

1. 子类通过implements来实现接口中的规范
2. 接口不能创建实例，但可用于声明引用变量类型
3. 子类继承接口后，必须实现所有方法
4. JDK1.8后，接口中包含普通的静态方法

#### 内部类

内部类主要分为：成员内部类（静态内部类、非静态内部类）、匿名内部类、局部内部类

##### 1. 非静态内部类

```java
class Outer{
    private int age = 10;
    
    class Inner{
        int age =20 ; 
        System.out.println("外部类的成员："+Outer.this.age);
        System.out.println("内部类的成员："+this.age);
    }
}

public static void main(){
    //创建内部类对象 依托于外部类对象
    Outer.Inner inner = new Outer().new Inner();
	
    inner.show();
}
```

Gist:

1. 表示方法为Outer&Inner
2. 一个非静态内部类对象一定存在一个对应的外部类对象，相当于类的成员
3. 非静态内部类对象可以直接访问外部类成员，但是外部类成员**不能直接**访问内部类成员
4. 非静态内部类**不能**有静态方法、静态属性和静态初始化块
5. 外部类的静态方法、静态代码块不能访问非晶态内部类

访问要点：

1. 内部类属性：this.变量名
2. 外部类属性：外部类名.this.变量名

##### 2. 静态内部类

Gist：

1. 当一个静态内部类存在时，并不一定存在一个对应的外部类对象，因此静态内部类的实例方法不能直接访问外部类的实例方法
2. 静态内部类可以看作外部类的一个静态成员。因此，外部类的方法中可以通过：“静态内部类.名字”的方法访问其静态成员，通过new 静态内部类()访问静态内部类的实例。

```java
class Outer{
    static class Inner{
    	    
    } 
}

public static void main(){
    //创建对象
    Outer.Inner inner = new Outer.Inner();
}
```

##### 3. 匿名内部类

适合那种只需要使用一次的类

```java
public void test(A a){
    a.call();
}

interface A{
    void call();
}

public static void main(){
    test(new A(){
        
        @Override
        public void call(){
            System.out.println("实现了匿名内部类");
        }
    });
}
```



### 三、常用类

#### 1. Array

提供静态工厂

```java
int [] a = {1,3,2,6,5,4};

a.toString()：			//静态方法，和Object的方法不同

Arrays.sort(a);			//排序，对自定义的类若需要自定义排序规则，该类要implements Comparable接口

Arrays.binarySearch(a,12);		//二分查找，返回索引位置, -1表示不存在

```

多维数组：

```java
//多维数组的声明和初始化应从低维到高维
int [][]a = new int[3][];
a[0] = new int[]{20,30};
a[1] = new int [3];
a[2] = new int [4];

//int a1[][] = new int [][4];		非法！

//静态初始化
int [][]b = {
    {20,30,40},
    {50,20},
    {100,200,300}
};
```



#### 2. String

常量池的概念	，不可变字符串序列

```java
String str1 = "xwl";
String str2 = "xwl";				// str1==str2
String str3 = new String("xwl");		//str1 != str3

//"xwl"被放入字符串常量池中
//str3 则是新建了一个字符串对象
```

通常使用equals比较字符串是否相等

##### StringBuilder/Buffer

```java
StringBuilder sb = new StringBuilder();
//初始化
sb.reverse();			//逆序
sb.setCharAt(3,'d');		//修改
sb.insert(0,'a');			//在index位置插入, 返沪值为this

sb.delete(20,23);		//删除在 [start,end) 区间中的值
```

不可变字符串序列使用**陷阱!**

```java
String str = "";
for(int i=0;i<5000;++i){
    str = str + i;
}
//相当于产生10000个对象


//应该使用这种方式
for(int i=0;i<5000;++i){
    str.append(i);
}
```

#### 3. Date

用long类型的变量来表示时间

```java
long now = System.currentTimeMillis();

Date d = new Date();			//什么都不传默认当前时刻
Date d = new Date(2000);			//	1970/1/1 0:00:00 + 2000毫秒
```

遇到日期，使用Calendar类.

##### DateFormat

完成字符串和时间对象的转化，抽象类

```java
DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//使用子类创建
//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

//format方法将Date转为指定格式的字符串
String str = df.format(new Date (4000000));

//parse方法将指定格式的字符串转成Date
Date df2 = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
Date date = df2.parse("2010年10月10日 21时43分28秒");

//其他格式
//大D 表示返回今天是本年的第几天
Date df3 = new SimpleDateFormat("D");
String str3 = df3.format(new Date());
```

![](D:\技术\学习笔记\Java\DateFormat.PNG)

##### Calendar

抽象类，提供关于日期计算的相关功能.

```java
//使用 年 月 日
Calendar calendar = new GregorianCalendar(2019, 9,9,23, 50 50);
int year = calendar.get(Calendar.YEAR);
int month = calendar.get(Calendar.MONTH);
int weekday = calendar.get(Calendar.DAY_OF_WEEK);


calendar.set(Calendar.YEAR, 2020);	//设置指定项的数值
calendar.add(Calendar.DATE, 100);			//增加100天

Date d = calendar.getTime();
calendar.setTime(new Date());
```



#### 4. File

```java
File file = new File("D:/a.txt");

//文件重命名
file.renameTo(new File("D/b.txt"));

//打印项目目录路径
System.out.println(System.getProperty("user.dir"));

File f2 = new File("g.cpp");
f2.createNewFile();
```

若不加路径直接new，则默认在类路径下创建

mkdirs可在没有父文件夹的时候创建

mkdir则不行

#### 5. 枚举

```java
enum Season{
    SPRING, SUMMER, AUTUMN, WINTER
}
```



#### 6. 异常

抛出异常：执行方法时，如果发生异常，方法生成一个异常对象，把异常抛给JRE

捕获异常：JRE得到异常后，JRE在方法的调用栈中查找，从生成异常的地方回溯，直到找到相应的异常处理代码为止。

RunTimeException JRE会接收，不用自己编写catch

而Exception 需要自己往外抛，或者方法内catch

*异常往往在高层处理

可分类catch

```java
try{
    //...
}catch(SonException e){			//子类异常在父类异常之前
    
}catch(Exception e){
    
}
```

#### 7. 包装类

包装类（Wrapper Class）。基本数据类型不是对象，但有时需要把其转成对象。

| 基本数据类型 | 包装类    |
| ------------ | --------- |
| byte         | Byte      |
| boolean      | Boolean   |
| short        | Short     |
| char         | Character |
| int          | Integer   |
| long         | Long      |
| float        | Float     |
| double       | Double    |

Integer：

```java
Integer i = new Integer(10);
Integer j = new Integer(50);
```

内存：

![](D:\技术\学习笔记\Java\包装类内存.PNG)

使用方法：

```java
//基本类型转Integer对象
Integer int1 = new Integer(10);
Integer int2 = Integer.valueOf(20);			//官方推荐

int a = int1.intValue();
double d = int2.doubleValue();

Integer int3 = new Integer("334");
Integer int4 = Integer.parseInt("999");

String str1 = int3.toString();
```

##### 自动装箱和拆箱

编译器自动处理

```java
Integer a = 234;		//自动装箱：Integer a = Integer.valueOf(234);

int b = a;				//自动拆箱：int b = a.intValue();
```

缓存问题：

Integer包装类中缓存[-128,127]的值。在系统初始的时候，创建了[-128,127]之间的一个缓存数组，当我们调用valueOf()的时候，首先检查是否在[-128,127]之间，如果在这个范围内直接从缓存中取，否则在堆里new一个包装类对象。

```java
Integer int1 = -128;
Integer int2 = -128;
sout(int1 == int2);				//true

Integer int3 = 1234;
Integer int4 = 1234;
sout(int3 == int4);				//false
```







### 四、容器

![](D:\技术\学习笔记\Java\容器分类.PNG)

#### *泛型

帮助建立类型安全的集合。泛型本质是”数据类型的参数化“。使用占位符表示数据类型，告诉编译器在调用泛型时必须传入实际类型。

```java
class MyCollection<E> {				//E表示泛型
    Object []objs = new Object[5];
    
    public E get(int index){
        return (E) objs[index];
    }
    
    public void set(E e, int index){
        objs[index] = e;
    }
}
```

若创建对象时不传类型参数，则类型都默认为Object



#### 1. List

##### ArrayList

基于数组实现；插入删除数据慢；初始化时构建空数组；可以sort

　　1）如果是第一次添加元素，数组的长度被扩容到默认的capacity，也就是10.

　　2）当发觉同时添加一个或者是多个元素，数组长度不够时，就扩容，这里有两种情况：

　　只添加一个元素，例如：原来数组的capacity为10，size已经为10，不能再添加了。需要扩容，新的capacity=old capacity+old capacity>>1=10+10/2=15。即新的容量为15。

　　当同时添加多个元素时，原来数组的capacity为10，size为10，当同时添加6个元素时。它需要的min capacity为16，而按照capacity=old capacity+old capacity>>1=10+10/2=15。new capacity小于min capacity，则取min capacity。

JDK1.8源码：

```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;		//容量上限

private static final int DEFAULT_CAPACITY = 10;					//第一次默认阔扩展的值


private static int calculateCapacity(Object[] elementData, int minCapacity) {
    //如果此时容器为空，则取minC和10的较大值    
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
}
	
//所有增加成员的方法都要执行的方法
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private void ensureExplicitCapacity(int minCapacity) {
        //记录修改次数
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }


/**
	minCapacity:当前容器需要的最小容量
*/
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);		//扩展为1.5倍
    //若扩展后仍<minCapacity，则取minCapacity
    if (newCapacity - minCapacity < 0)						
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);			//复制到扩展后的数组上
}

private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

```



##### LinkedList

基于双向链表，按照插入顺序排列；不可sort

头部插入和尾部插入复杂度为O(1)；

删除：根据给定的下标index，判断它first节点、last直接距离，如果index<size（数组元素个数)/2,就从first开始。如果大于，就从last开始。

源码：

```java
	//头插	
private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        modCount++;
    }

	//尾插
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }

//删除f节点
 private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

//根据下表获取， 除2优化
Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
```



#### 2. Set

##### HashSet 

无序，不重复，没有索引。

基于HashMap来实现的，只使用了HashMap的key来实现各种特性，而HashMap的value始终都是一个PRESENT对象。

HashSet不允许重复（HashMap的key不允许重复，如果出现重复就覆盖），允许null值，**非线程安全**。默认初始容量是 16，加载因子是 0.75。构造函数可指定初始容量和加载因子。

```java
//底层用HashMap实现
private transient HashMap<E,Object> map;

//默认的value
private static final Object PRESENT = new Object();
```

##### TreeSet

底层使用TreeMap实现，内部维持了一个简化版的TreeMap



#### 3. Map

##### HashMap

键值对，键不能重复，值可以重复。键重复的话，第一次的值会被第二次的值覆盖。底层采用了哈希表。基本结构为数组+链表。

哈希：

作为键的类重写了hashCode方法，返回一个哈希数，根据这个数值确定自己的index.

```java
//取余的快速算法
hash = hash & (length - 1); 
```

JDK1.8后，单个桶的长度大于8后，转换为红黑树。当数量小于6后，就把红黑树变回链表。

查找：通过hash值找到index后，对链表中元素的key按次序equals匹配，直至找到。Java中规定，两个内容相同的对象（即equals为true）必须具有相同的hashCode。

扩容：初始大小为16。如果位桶数组中的元素达到(0.75*数组length)，就重新调整数组大小为原来2倍

单个桶的结构：

![](D:\技术\学习笔记\Java\哈希1.PNG)

表的结构：

![](D:\技术\学习笔记\Java\哈希2.PNG)



源码：

```java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

static final int MAXIMUM_CAPACITY = 1 << 30;

static final float DEFAULT_LOAD_FACTOR = 0.75f;

//计算表内哈希地址
static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。当get()方法返回null值时，可能是 HashMap中没有该键，也可能使该键所对应的值为null。因此，在HashMap中不能由get()方法来判断HashMap中是否存在某个键， 而应该用containsKey()方法来判断。

##### TreeMap

红黑树的典型实现。效率比HashMap低，一般排序的时候才使用。

若果要对自定义类进行排序，则该类需要实现Comparable接口。

##### HashMap多线程问题

线程不安全；可能造成死锁

```java
复制代码
 public V put(K key, V value) {
        if (key == null)
            return putForNullKey(value);
        int hash = hash(key.hashCode());
        int i = indexFor(hash, table.length);
        for (Entry<K,V> e = table[i]; e != null; e = e.next) { 	//374行处会导致死锁
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i);
        return null;
    }
```

①互斥条件：链表上的节点同一时间此时被两个线程占用，两个线程占用访问节点的权利，符合该条件

②请求和保持条件：Thread1保持着节点e1，又提出了占用节点e2(此时尚未释放e2)；而Thread2此时占用e2,又提出了占用节点e1，Thread1占用着Thread2接下来要用的e1,而Thread2又占用着Thread1接下来要用的e2，符合该条件

③：不剥夺条件：线程是由自己的退出的，此时并没有任何中断机制（sleep或者wait方法或者interuppted中断），只能由自己释放，满足条件

④：环路等待条件：e1、e2、e3等形成了资源的环形链条，满足该条件

![img](https://images2018.cnblogs.com/blog/1066538/201803/1066538-20180330163632395-564709036.png)

如果存在线程1和线程2，在rehash之前中，a、b、c在table[1]形成了链表，a的next指向了b，这时发生了put操作，两个线程同时进行了rehash。

线程1在遍历Hash表元素中，取a.next时**被挂起**。

线程2继续完成了rehash操作，重组了链表（**头插**，为了避免遍历链表），重组结束后，b.next指向了a。

线程1继续执行，a.next又指向了b，环形链表因此产生了。

*JDK8已经解决该问题

##### HashTable

也是一个散列表。它存储的内容是键值对(key-value)映射。

继承自Dictionary，实现了Map、Cloneable、java.io.Serializable接口。

线程安全。它的key、value都不可以为null。

此外，Hashtable中的映射不是有序的。

初始容量为11；默认加载因子为0.75；超过threshould就容量*2。哈希的之后与完max_int模表的size。

##### HashTable和HashMap区别

1、继承的父类不同：Hashtable继承自Dictionary类，而HashMap继承自AbstractMap类。但二者都实现了Map接口。

2、线程安全性不同： Hashtable 中的方法是Synchronize的，而HashMap中的方法在缺省情况下是非Synchronize的。在多线程并发的环境下，可以直接使用Hashtable，不需要自己为它的方法实现同步，但使用HashMap时就必须要自己增加同步处理。

```java
Map m = Collections.synchronizedMap(new HashMap(...));
```

3、是否提供contains方法

HashMap把Hashtable的contains方法去掉了，改成containsValue和containsKey，因为contains方法容易让人引起误解。

4、key和value是否允许null值

Hashtable中，key和value都不允许出现null值。但是如果在Hashtable中有类似put(null,null)的操作，编译同样可以通过，因为key和value都是Object类型，但运行时会抛出NullPointerException异常。
HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。当get()方法返回null值时，可能是 HashMap中没有该键，也可能使该键所对应的值为null。因此，在HashMap中不能由get()方法来判断HashMap中是否存在某个键， 而应该用containsKey()方法来判断。

5、两个遍历方式的内部实现上不同

Hashtable、HashMap都使用了 Iterator。而由于历史原因，Hashtable还使用了Enumeration的方式 。Hashtable与HashMap另一个区别是HashMap的迭代器（Iterator）是fail-fast迭代器，而Hashtable的enumerator迭代器不是fail-fast的。所以当有其它线程改变了HashMap的结构（增加或者移除元素），将会抛出ConcurrentModificationException，但迭代器本身的remove()方法移除元素则不会抛出ConcurrentModificationException异常。但这并不是一个一定发生的行为，要看JVM。

6、hash值不同

哈希值的使用不同，HashTable直接使用对象的hashCode。而HashMap重新计算hash值。

hashCode是jdk根据对象的地址或者字符串或者数字算出来的int类型的数值。

Hashtable计算hash值，直接用key的hashCode()，而HashMap重新计算了key的hash值，Hashtable在求hash值对应的位置索引时，用取模运算，而HashMap在求位置索引时，则用与运算，且这里一般先用hash&0x7FFFFFFF后，再对length取模，&0x7FFFFFFF的目的是为了将负的hash值转化为正值，因为hash值有可能为负数，而&0x7FFFFFFF后，只有符号外改变，而后面的位都不变。

 7、内部实现使用的数组初始化和扩容方式不同

​      HashTable在不指定容量的情况下的默认容量为11，而HashMap为16，Hashtable不要求底层数组的容量一定要为2的整数次幂，而HashMap则要求一定为2的整数次幂。

​      Hashtable和HashMap它们两个内部实现方式的数组的初始大小和扩容的方式。HashTable中hash数组默认大小是11，增加的方式是 old*2+1。

##### ConcurrentHashMap

- 底层采用分段的数组+链表实现，线程**安全**
- 通过把整个Map分为N个Segment，可以提供相同的线程安全，但是效率提升N倍，默认提升16倍。(读操作不加锁，由于HashEntry的value变量是 volatile的，也能保证读取到最新的值。)
- Hashtable的synchronized是针对整张Hash表的，即每次锁住整张表让线程独占，ConcurrentHashMap允许多个修改操作并发进行，其关键在于使用了锁分离技术
- 有些方法需要跨段，比如size()和containsValue()，它们可能需要锁定整个表而而不仅仅是某个段，这需要按顺序锁定所有段，操作完毕后，又按顺序释放所有段的锁
- 扩容：段内扩容（段内元素超过该段对应Entry数组长度的75%触发扩容，不会对整个Map进行扩容），插入前检测需不需要扩容，有效避免无效扩容

ConcurrentHashMap是使用了锁分段技术来保证线程安全的。

**锁分段技术**：首先将数据分成一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。 

ConcurrentHashMap提供了与Hashtable和SynchronizedMap不同的锁机制。Hashtable中采用的锁机制是一次锁住整个hash表，从而在同一时刻只能由一个线程对其进行操作；而ConcurrentHashMap中则是一次锁住一个桶。

ConcurrentHashMap默认将hash表分为16个桶，诸如get、put、remove等常用操作只锁住当前需要用到的桶。这样，原来只能一个线程进入，现在却能同时有16个写线程执行，并发性能的提升是显而易见的。

#### 4. 迭代器

提供了统一的遍历容器的方式。

```java
List<String> alist = new ArrayList<String>();
//初始化

for(Iterator<String> iter = aList.iterator(); iter.hasNext(); ){
    String tmp = iter.next();
    if(tmp.endWiths('3')){
        iter.remove();				//删除以3结尾的字符串
    }
}
```

##### 遍历Map

1、获取键值对集合

```java
Map<String,String> map = new HashMap<String,String>();

//初始化

Set<Entry<String,String> > ss = map.entrySet();			//获取entry集合

for(Iterator<Entry<String, String> > iterator = ss.iterator(); iterator.hasNext(); ){
    Entry<String, String> e = iterator.next();
    System.out.println(e.getKey()+" "+e.getValue());
}
```

2、获取键集合

```java
Map<Integer,String> map = new HashMap<Integer,String>();

//初始化

Set<Integer,String> ss = map.entrySet();			//获取entry集合

for(Iterator<Integer> iterator = ss.iterator(); iterator.hasNext(); ){
    Integer key = iterator.next();
  	System.out.println(key+" "+map.get(key) );
}
```



### 五、Java反射

#### 1. 规范

JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。

要想解剖一个类,必须先要获取到该类的字节码文件对象。而解剖使用的就是Class类中的方法.所以先要获取到每一个字节码文件对应的Class类型的对象。

反射就是把java类中的各种成分映射成一个个的Java对象。例如：一个类有：成员变量、方法、构造方法、包等等信息，利用反射技术可以对一个类进行解剖，把个个组成部分映射成一个个对象。

如图是类的正常加载过程：反射的原理在于class对象。

熟悉一下加载的时候：**Class对象**的由来是将class文件读入内存，并为之创建一个Class对象。

![img](https://img-blog.csdn.net/20170513133210763)

#### 2. Class类

`Class` 类的实例表示正在运行的 Java 应用程序中的类和接口。也就是jvm中有N多的实例，每个类都有该Class对象。（包括基本数据类型）

`Class` 没有公共构造方法。`Class` 对象是在加载类时由 Java 虚拟机以及通过调用类加载器中的**defineClass 方法**自动构造的。也就是这不需要我们自己去处理创建，JVM已经帮我们创建好了。

#### 3. 反射的使用

##### 1. 获取Class对象的三种方式

1.1 Object ——> getClass(); 

1.2 任何数据类型（包括基本数据类型）都有一个“静态”的class属性 

1.3 通过Class类的静态方法：forName（String  className）(常用)

```java
/** 
 * 获取Class对象的三种方式 
 * 1 Object ——> getClass(); 
 * 2 任何数据类型（包括基本数据类型）都有一个“静态”的class属性 
 * 3 通过Class类的静态方法：forName（String  className）(常用) 
 */  
public class Fanshe {  
    public static void main(String[] args) {  
        //第一种方式
        Student stu1 = new Student();//这一new 产生一个Student对象，一个Class对象。  
        Class stuClass = stu1.getClass();//获取Class对象  
        System.out.println(stuClass.getName());  
          
        //第二种方式
        Class stuClass2 = Student.class;  
        System.out.println(stuClass == stuClass2);//判断第一种方式获取的Class对象和第二种方式获取的是否是同一个  
          
        //第三种方式
        try {  
            Class stuClass3 = Class.forName(”fanshe.Student”);//注意此字符串必须是真实路径，就是带包名的类路径，包名.类名  
            System.out.println(stuClass3 == stuClass2);//判断三种方式是否获取的是同一个Class对象  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }     
    }  
```

*在运行期间，一个类只有一个Class对象产生。三种方式常用第三种，第一种对象都有了还要反射干什么。第二种需要导入类的包，依赖太强，不导包就抛编译错误。一般都第三种，一个字符串可以传入也可写在配置文件中等多种方法。

##### 2. 通过反射获取构造方法

Student类：

```java
package fanshe;  
  
public class Student {  
      
    //—————构造方法——————-  
    //（默认的构造方法）  
    Student(String str){  
        System.out.println(”(默认)的构造方法 s = ” + str);  
    }  
      
    //无参构造方法  
    public Student(){  
        System.out.println(”调用了公有、无参构造方法执行了。。。”);  
    }  
      
    //有一个参数的构造方法  
    public Student(char name){  
        System.out.println(”姓名：” + name);  
    }  
      
    //有多个参数的构造方法  
    public Student(String name ,int age){  
        System.out.println(”姓名：”+name+“年龄：”+ age);//这的执行效率有问题，以后解决。  
    }  
      
    //受保护的构造方法  
    protected Student(boolean n){  
        System.out.println(”受保护的构造方法 n = ” + n);  
    }  
      
    //私有构造方法  
    private Student(int age){  
        System.out.println(”私有的构造方法   年龄：”+ age);  
    }  
}  
```

测试类：

```java
package fanshe;  
  
import java.lang.reflect.Constructor;  
/* 
 * 通过Class对象可以获取某个类中的：构造方法、成员变量、成员方法；并访问成员； 
 *  
 * 1.获取构造方法： 
 *      1).批量的方法： 
 *          public Constructor[] getConstructors()：所有”公有的”构造方法 
            public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有) 
      
 *      2).获取单个的方法，并调用： 
 *          public Constructor getConstructor(Class… parameterTypes):获取单个的”公有的”构造方法： 
 *          public Constructor getDeclaredConstructor(Class… parameterTypes):获取”某个构造方法”可以是私有的，或受保护、默认、公有； 
 *       
 *          调用构造方法： 
 *          Constructor–>newInstance(Object… initargs) 
 */  
public class Constructors {  
  
    public static void main(String[] args) throws Exception {  
        //1.加载Class对象  
        Class clazz = Class.forName(”fanshe.Student”);  
          
        //2.获取所有公有构造方法  
        System.out.println("**********************所有公有构造方法*********************************");  
        Constructor[] conArray = clazz.getConstructors();  
        for(Constructor c : conArray){  
            System.out.println(c);  
        }  
          
        System.out.println("************所有的构造方法(包括：私有、受保护、默认、公有)***************");  
        conArray = clazz.getDeclaredConstructors();  
        for(Constructor c : conArray){  
            System.out.println(c);  
        }  
          
        System.out.println("*****************获取公有、无参的构造方法*******************************");  
        Constructor con = clazz.getConstructor(null);  
        //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型  
        //2>、返回的是描述这个无参构造函数的类对象。  
      
        System.out.println("con = ” + con");  
        //调用构造方法  
        Object obj = con.newInstance();  
          
        System.out.println("******************获取私有构造方法，并调用*******************************");  
        con = clazz.getDeclaredConstructor(char.class);  
        System.out.println(con);  
        //调用构造方法  
        con.setAccessible(true);				//暴力访问(忽略掉访问修饰符)  
        obj = con.newInstance('男');  
    }  
}  
```

**调用方法：**

1.获取构造方法：

  1).批量的方法：
public Constructor[] getConstructors()：所有”公有的”构造方法
​            public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有)

  2).获取单个的方法，并调用：
public Constructor getConstructor(Class… parameterTypes):获取单个的”公有的”构造方法：
public Constructor getDeclaredConstructor(Class… parameterTypes):获取”某个构造方法”可以是私有的，或受保护、默认、公有；

  调用构造方法：

Constructor–>newInstance(Object… initargs)



2、newInstance是 Constructor类的方法（管理构造函数的类）

api的解释为：

``newInstance(Object… initargs). 使用此 `Constructor` 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。

它的返回值是T类型，所以newInstance是创建了一个构造方法的声明类的新实例对象，并为之调用。

##### 3. 获取成员变量

实体类：

```java
public class Student {  
    public Student(){  
          
    }  
    //**********字段*************//  
    public String name;  
    protected int age;  
    char sex;  
    private String phoneNum;  
      
    @Override  
    public String toString() {  
        return “Student [name=” + name + “, age=” + age + “, sex=” + sex  
                + ”, phoneNum=” + phoneNum + “]”;  
    }  
}
```

测试类：

```java
public class Fields {  
  
        public static void main(String[] args) throws Exception {  
            //1.获取Class对象  
            Class stuClass = Class.forName(”fanshe.field.Student”);  
            //2.获取字段  
            System.out.println("************获取所有公有的字段********************");  
            Field[] fieldArray = stuClass.getFields();  
            for(Field f : fieldArray){  
                System.out.println(f);  
            }  
            System.out.println("************获取所有的字段(包括私有、受保护、默认的)********************");  
            fieldArray = stuClass.getDeclaredFields();  
            for(Field f : fieldArray){  
                System.out.println(f);  
            }  
            System.out.println("*************获取公有字段**并调用***********************************");  
            Field f = stuClass.getField("name");  
            System.out.println(f);  
            //获取一个对象  
            Object obj = stuClass.getConstructor().newInstance();//产生Student对象–》Student stu = new Student();  
            //为字段设置值  
            f.set(obj, "刘德华");	//为Student对象中的name属性赋值–》stu.name = ”刘德华”  
            //验证  
            Student stu = (Student)obj;  
            System.out.println("验证姓名：" + stu.name);  
              
            System.out.println("**************获取私有字段****并调用********************************");  
            f = stuClass.getDeclaredField("phoneNum");  
            System.out.println(f);  
            f.setAccessible(true);//暴力反射，解除私有限定  
            f.set(obj, "18888889999");  
            System.out.println("验证电话：" + stu);  
        }  
    }
```

调用字段时：需要传递两个参数：

Object obj = stuClass.getConstructor().newInstance();//产生Student对象–》Student stu = new Student();
//为字段设置值
f.set(obj, “刘德华”);//为Student对象中的name属性赋值–》stu.name = “刘德华”

第一个参数：要传入设置的对象，第二个参数：要传入实参

##### 4. 获取成员方法

实体类：

```java
public class Student {  
    //**************成员方法***************//  
    public void show1(String s){  
        System.out.println(”调用了：公有的，String参数的show1(): s = ” + s);  
    }  
    protected void show2(){  
        System.out.println(”调用了：受保护的，无参的show2()”);  
    }  
    void show3(){  
        System.out.println(”调用了：默认的，无参的show3()”);  
    }  
    private String show4(int age){  
        System.out.println(”调用了，私有的，并且有返回值的，int参数的show4(): age = ” + age);  
        return “abcd”;  
    }  
}  
```

测试类：

```java
import java.lang.reflect.Method;  
  
/* 
 * 获取成员方法并调用： 
 *  
 * 1.批量的： 
 *      public Method[] getMethods():获取所有”公有方法”；（包含了父类的方法也包含Object类） 
 *      public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的) 
 * 2.获取单个的： 
 *      public Method getMethod(String name,Class<?>… parameterTypes): 
 *                  参数： 
 *                      name : 方法名； 
 *                      Class … : 形参的Class类型对象 
 *      public Method getDeclaredMethod(String name,Class<?>… parameterTypes) 
 *  
 *   调用方法： 
 *      Method –> public Object invoke(Object obj,Object… args): 
 *                  参数说明： 
 *                  obj : 要调用方法的对象； 
 *                  args:调用方式时所传递的实参； 
 
): 
 */  
public class MethodClass {  
  
    public static void main(String[] args) throws Exception {  
        //1.获取Class对象  
        Class stuClass = Class.forName(”fanshe.method.Student”);  
        //2.获取所有公有方法  
        System.out.println(”***************获取所有的”公有“方法*******************”);  
        stuClass.getMethods();  
        Method[] methodArray = stuClass.getMethods();  
        for(Method m : methodArray){  
            System.out.println(m);  
        }  
        System.out.println(”***************获取所有的方法，包括私有的*******************”);  
        methodArray = stuClass.getDeclaredMethods();  
        for(Method m : methodArray){  
            System.out.println(m);  
        }  
        System.out.println(”***************获取公有的show1()方法*******************”);  
        Method m = stuClass.getMethod(”show1”, String.class);  
        System.out.println(m);  
        //实例化一个Student对象  
        Object obj = stuClass.getConstructor().newInstance();  
        m.invoke(obj, ”刘德华”);  
          
        System.out.println(”***************获取私有的show4()方法******************”);  
        m = stuClass.getDeclaredMethod(”show4”, int.class);  
        System.out.println(m);  
        m.setAccessible(true);//解除私有限定  
        Object result = m.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参  
        System.out.println("返回值：" + result); 
    }  
}  
```

由此可见：

m = stuClass.getDeclaredMethod(“show4”, int.class);//调用制定方法（所有包括私有的），需要传入两个参数，第一个是调用的方法名称，第二个是方法的形参类型，切记是类型。
Object result = m.invoke(obj, 20);	//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
System.out.println(“返回值：” + result);

##### 5. 反射main方法

```java
public class Student {  
  
    public static void main(String[] args) {  
        System.out.println(”main方法执行了。。。”);  
    }  
}  
```

测试类：

```java
public void Main{
    public static void main(String[] args) {  
        try {  
            //1、获取Student对象的字节码  
            Class clazz = Class.forName(”fanshe.main.Student”);  
              
            //2、获取main方法  
             Method methodMain = clazz.getMethod("main", String[].class);//第一个参数：方法名称，第二个参数：方法形参的类型，  
            //3、调用main方法  
            // methodMain.invoke(null, new String[]{“a”,”b”,”c”});  
             //第一个参数，对象类型，因为方法是static静态的，所以为null可以，第二个参数是String数组，这里要注意在jdk1.4时是数组，jdk1.5之后是可变参数  
             //这里拆的时候将  new String[]{“a”,”b”,”c”} 拆成3个对象。。。所以需要将它强转。  
             methodMain.invoke(null, (Object)new String[]{“a”,“b”,“c”});//方式一  
            // methodMain.invoke(null, new Object[]{new String[]{“a”,”b”,”c”}});//方式二  
              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
}

```

##### 6. 通过反射运行配置文件内容

实体类：

```java
public class Student {  
    public void show(){  
        System.out.println(”is show()”);  
    }  
}  

```

配置文件pro.txt:

```properties
className = cn.fanshe.Student  
methodName = show  

```

测试类：

```java
/* 
 * 我们利用反射和配置文件，可以使：应用程序更新时，对源码无需进行任何修改 
 * 我们只需要将新类发送给客户端，并修改配置文件即可 
 */  
public class Demo {  
    public static void main(String[] args) throws Exception {  
        //通过反射获取Class对象  
        Class stuClass = Class.forName(getValue(”className”));//”cn.fanshe.Student”  
        //2获取show()方法  
        Method m = stuClass.getMethod(getValue(”methodName”));//show  
        //3.调用show()方法  
        m.invoke(stuClass.getConstructor().newInstance());  
          
    }  
      
    //此方法接收一个key，在配置文件中获取相应的value  
    public static String getValue(String key) throws IOException{  
        Properties pro = new Properties();//获取配置文件的对象  
        FileReader in = new FileReader("pro.txt");//获取输入流  
        pro.load(in);//将流加载到配置文件对象中  
        in.close();  
        return pro.getProperty(key);//返回根据key获取的value值  
    }  
}  

```

##### 7. 通过反射越过泛型检查

```java
/* 
 * 通过反射越过泛型检查 
 *  
 * 例如：有一个String泛型的集合，怎样能向这个集合中添加一个Integer类型的值？ 
 */  
public class Demo {  
    public static void main(String[] args) throws Exception{  
        ArrayList<String> strList = new ArrayList<>();  
        strList.add("aaa");  
        strList.add("bbb");  
          
    //  strList.add(100);  
        //获取ArrayList的Class对象，反向的调用add()方法，添加数据  
        Class listClass = strList.getClass(); //得到 strList 对象的字节码 对象  
        //获取add()方法  
        Method m = listClass.getMethod("add", Object.class);  
        //调用add()方法  
        m.invoke(strList, 100);  
          
        //遍历集合  
        for(Object obj : strList){  
            System.out.println(obj);  
        }  
    }  
}  
```



### 六、多线程

#### 实现的方式

1、继承Thread

2、实现Runnable接口（有利于共享数据）

3、实现Callable接口

JUC并发包下，接口泛型指定call方法返回值；创建进程时

```java
ExecutorService ser = Executors.newFixedThreadPool(3);	//调用线程池
Future<Boolean> res1 = ser.submit(cd1);		//cd为Callable实现对象

//获取结果
boolean r1 = res1.get();		//此处会抛出异常
ser.shutdownNow();
```

#### 静态代理

真实角色、代理角色

new Thread(new Runnable()).start()

#### Lamda表达式

用于简化只用一次的线程体的编写

*需保证接口只有一个没有实现的方法

*lambda推导的时候一定要提供类型（引用变量/形参）

带形参

```java
public class LamdaTest02 {

    static class Test1 implements Speak{
        @Override
        public void speak(int a) {
            System.out.println("静态内部类: "+a);
        }
    }

    public static void main(String[] args) {
        test1();
    }


    public static void test1(){
        new Test1().speak(1);

        class Test2 implements Speak{
            @Override
            public void speak(int a) {
                System.out.println("局部内部类: "+a);
            }
        }

        new Test2().speak(2);

        new Speak() {
            @Override
            public void speak(int a) {
                System.out.println("匿名内部类: "+a);
            }
        }.speak(3);

        Speak tmp = (int a)->{
            System.out.println("lambda: "+a);
        };
        tmp.speak(4);

        //如果只有一行代码：可省略花括号
        tmp = (int a)-> System.out.println("new lambda: "+a);
        tmp.speak(5);
    }
}

interface Speak{
    void speak(int a);
}
```

带返回值

```java
public class LamdaTest03 {

    public static void main(String[] args) {
        /**
         * 可省略类型，但要省一起省
         * 只有一个参数时，可省略圆括号
         */
        Run tmp = b -> {
            System.out.println("lambda: "+b);
            return b+1;
        };
        System.out.println(tmp.speak(3));
    }
}

interface Run{
    int speak(int a);
}

```





#### volatile

当一个变量定义为 volatile 之后，将具备两种特性：

1.保证此变量对所有的线程的可见性，这里的“可见性”：当一个线程修改了这个变量的值，volatile 保证了新值能立即同步到主内存，以及每次使用前立即从主内存刷新。但普通变量做不到这点，普通变量的值在线程间传递均需要通过主内存（详见：[Java内存模型](http://www.cnblogs.com/zhengbin/p/6407137.html)）来完成。

2.禁止指令重排序优化。有volatile修饰的变量，赋值后多执行了一个“load addl $0x0, (%esp)”操作，这个操作相当于一个**内存屏障**（指令重排序时不能把后面的指令重排序到内存屏障之前的位置），只有一个CPU访问内存时，并不需要内存屏障；（什么是指令重排序：是指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各相应电路单元处理。）

volatile可以保证可见性和有序性，一个例子：

```java
public class NoVisibility {
    private static boolean ready;
    private static int number;
    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while(!ready) {		//可能永远也看不到主线程中的修改，此时用volatile修饰ready即可解决这个问题
                Thread.yield();
            }
            System.out.println(number);
        }
    }
    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}
```

| 是否重排序 | 第二个操作 |            |            |
| ---------- | ---------- | ---------- | ---------- |
| 第一个操作 | 普通读/写  | volatile读 | volatile写 |
| 普通读/写  |            |            |            |
| volatile读 | NO         | NO         | NO         |
| volatile写 |            | NO         | NO         |

```java
public class Volatile {
    /**
     * volatile用于保证数据的同步性
     * @param args
     */
    private volatile static int num = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            while(num ==0){             //此处让CPU很忙，以至于之后主线程中的修改都无法读取到

            }
        });

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        num = 1;
    }
}

```

##### DCL单例模式-双重锁

```java
/**
 * 单例模式：懒汉式套路 在多线程环境下，对外存在一个对象
 * 1. 构造器私有化 --> 避免外部new构造器
 * 2. 提供私有的静态属性 --> 存储一个对象的地址
 * 3、提供公共的静态方法  --> 获取属性
 */

public class DoubleCheckedLocking {
    private static volatile DoubleCheckedLocking instance;       //懒汉式
    //没有volatile,其他线程可能访问一个没有初始化的对象

    //1.构造器私有化
    private DoubleCheckedLocking(){}

    //3. 公共的静态方法 --> 获取属性
    public static DoubleCheckedLocking getInstance(){
        if(instance != null) {          //避免不必要的同步，已经存在对象
            return instance;
        }
        synchronized (DoubleCheckedLocking.class) {             //类锁
            if (instance == null) {
                instance = new DoubleCheckedLocking();          //此处可能发生指令重排
                /*
                     new 一个对象时
                     1. 开辟空间
                     2. 初始化对象信息
                     3. 返回对象的地址给引用
                 */
            }
        }
        return instance;
    }

    public static void main(String[] args) {

    }
}
```



#### Java内存模型(JMM)

**可见性：**指线程之间的可见性，一个线程修改的状态对另一个线程是可见的。也就是一个线程修改的结果。另一个线程马上就能看到。比如：用volatile修饰的变量，就会具有可见性。volatile修饰的变量不允许线程内部缓存和重排序，即直接修改内存。所以对其他线程是可见的。但是这里需要注意一个问题，volatile只能让被他修饰内容具有可见性，但不能保证它具有原子性。比如 volatile int a = 0；之后有一个操作 a++；这个变量a具有可见性，但是a++ 依然是一个非原子操作，也就是这个操作同样存在线程安全问题。

**原子性：**原子是世界上的最小单位，具有不可分割性。比如 a=0；（a非long和double类型） 这个操作是不可分割的，那么我们说这个操作时原子操作。再比如：a++； 这个操作实际是a = a + 1；是可分割的，所以他不是一个原子操作。非原子操作都会存在线程安全问题，需要我们使用同步技术（sychronized）来让它变成一个原子操作。一个操作是原子操作，那么我们称它具有原子性。java的concurrent包下提供了一些原子类，我们可以通过阅读API来了解这些原子类的用法。比如：AtomicInteger、AtomicLong、AtomicReference等。在 Java 中 synchronized 和在 lock、unlock 中操作保证原子性。

**有序性：**程序在执行时，指令会重排。但会保持串行语义的一致性。

##### Happen-Before原则

当CPU在运行时，存在后序指令先于之前的指令结束的可能。代码执行顺序与预期的不一致，目的提高性能。

1. 指令重排不可违背的基本原则：
2. 程序顺序原则：一个线程内保证语义的串行性；
3. volatile规则：volatile变量的写先于读发生，这保证了volatile变量的可见性
4. 锁规则：解锁(unlock)必然发生于加锁(lock)前；
5. 传递性：A先于B，B先于C，则A必先于C；
6. 线程的start()方法先于它的每一个动作；
7. 线程的所有操作先于线程的终结Thread.join()；
8. 线程的中断(interrupt())先于被中断线程的代码；
9. 对象的构造函数的执行、结束先于finalize()方法；

```java
public class HappenBefore {
    private static int a = 0;
    private static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        //线程1读取数据
        for(int i=0;i<10000;++i){
            a = 0;
            flag = false;

            Thread t1 = new Thread(()->{
                a = 1;
                flag = true;
            });

            //线程2读取数据
            Thread t2 = new Thread(()->{
                if(flag) {
                    a *= 1;
                }
                if(a == 0){
                    System.out.println("happen before ->"+a);
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }
    }
}

```



#### 线程组

管理线程，将同一类功能的线程放在同一个线程组中。

#### 守护线程

在后台完成一些系统性的服务，如：垃圾回收线程、JIT线程。与之对应的是用户线程。当一个Java应用只有守护线程时，JVM会自然退出。

```java
t1.setDaemon(true);				//设置为守护线程
t1.start();
```

#### 线程优先级

饥饿；范围为1-10

```java
public final static int MIN_PRIORITY = 1;
public final static int NORM_PRIORITY = 5;
public final static int MAX_PRIORITY = 10;


t1.setPriority(Tread.MAX_PRIORITY);
```

#### Synchronized关键字

即使使用volatile，原子性也无法保证，对同一变量的写操作仍是不安全的。

1、指定加锁对象：给对象加锁，进入同步代码前要获得给定对象的锁；

```java
public class AccountingSync implements Runnable{
    static AccountingSync instance = new AccountingSync();
    static int i = 0;
    
    public void run(){
        for(int j = 0;j<100000;++j){
            synchronized(instance){
                i++;
            }
        }
    }
}  

public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance,"A");
        Thread t2 = new Thread(instance,"B");
        t1.start();t2.start();

        t1.join(); t2.join();
        System.out.println(i);
    }
//main方法
```

2、直接作用于实例方法：相当于对当前实例加锁，进入同步代码前要获得当前实例的锁；

```java
import java.io.IOException;

public class MyThread implements Runnable {
    static int i = 0;
    static MyThread instance = new MyThread();

    public synchronized void increase(){
        i++;
    }

    @Override
    public void run() {
        for(int j = 0;j<100000;++j){
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for(int t = 0; t<10; ++t){
            threads[t] = new Thread(instance);          //此处必须指向同一个实例,如果new 一个新的实例则没有加锁的效果
            threads[t].start();
        }

        for(int i=0;i<10;++i){
            threads[i].join();
        }

        System.out.println(i);
    }
}

```

3、直接作用于静态方法：相当于对当前类加锁，进入同步代码前要获得当前类的锁。

使用静态方法时，即使两个线程指向不同Runnable对象，但由于方法块需要请求的是当前类的锁，而非当前实例的锁，因此还是可以正确同步的。



##### 双重检查锁

```java
public  class DoubleCheckedLocking{
  private static Instance instance;                 
 
  public static Instance getInstance(){             
    if(instance ==null){                            
      synchronized (DoubleCheckedLocking.class){    
        if(instance ==null)                         
          instance=new Instance();                  
      }
    }
    return instance;
  }
}
```



##### 存在的问题

```java
static int i = 0; 
synchronized(i){
    i++;
}
```

线程不安全：首先Integer属于不变对象，即一被创建不可能被修改。因此实际使用了Integer.valueOf()方法新建了一个Integer对象。i++在执行时变为了 i = Integer.valueOf(i.intValue()+ 1)，而Integer.valueOf是一个工厂方法,他会返回一个指定数值的Integer对象实例。因此i++的本质是创建一个新的Integer对象，并将它的引用赋值给i。因此多个线程之间看到的不是一个i对象，加锁可能加在了不同的对象实例上。

改为synchronized(instance)即可。

#### 并发包

##### 重入锁

JDK5中，性能优于synchronized关键字；6开始两者性能相近。

```java
public class MyThread implements Runnable {
    static int i = 0;
    static ReentrantLock lock = new ReentrantLock();
    static MyThread instance = new MyThread();

    @Override
    public void run() {
        for(int j = 0;j<100000;++j){
            lock.lock();
            i++;
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        ReentrantLock myLock = new ReentrantLock();
        for(int t = 0; t<10; ++t){
            threads[t] = new Thread(instance);          //此处必须指向同一个实例,如果new 一个新的实例则没有加锁的效果
            threads[t].start();
        }

        for(int i=0;i<10;++i){
            threads[i].join();
        }

        System.out.println(i);
    }
}
```

可重入是指对同一个线程，可以多次lock，但必须同样次数地unlock

1、中断响应

调用lockInterruptily()方法可以在加锁过程中响应中断。

```java
t1.lockInterruptibly();

t2.lockInterruptibly();

t1.interrupt();				//中断一个线程
```

2、锁申请等待时限

```java
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MyThread implements Runnable {
    public static ReentrantLock lock1 = new ReentrantLock();
    int lock;

    MyThread(int lock){this.lock = lock;}

    @Override
    public void run() {
        try {
            if(lock1.tryLock(5, TimeUnit.SECONDS)){			
                Thread.sleep(6000);
            }else{
                System.out.println("get failed");			//超时直接fail
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (lock1.isHeldByCurrentThread()) lock1.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread lock1 = new MyThread(1);

        Thread t1 = new Thread(lock1);
        Thread t2 = new Thread(lock2);
        t1.start(); t2.start();
    }
}
```

也可以用tryLock()不指定时间的方式避免死锁，其方法一旦申请成功会立即返回true，否则立即返回false

##### 死锁与解决死锁

```java
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MyThread implements Runnable {

    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();

    int lock;

    MyThread(int lock){this.lock = lock;}

    @Override
    public void run() {
        if(lock ==1){
            while(true){
                if(lock1.tryLock()){
                    try{
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(lock2.tryLock()){
                            try{
                                System.out.println(Thread.currentThread().getId()+": done");
                                return;
                            }finally{
                                lock2.unlock();
                            }
                        }
                    }finally{
                        lock1.unlock();
                    }
                }
            }
        }else{
            while(true){
                if(lock2.tryLock()){
                    try {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(lock1.tryLock()){
                            try{
                                System.out.println(Thread.currentThread().getId()+": done");
                                return;
                            }finally {
                                lock1.unlock();
                            }
                        }
                    }finally {
                        lock2.unlock();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread lock1 = new MyThread(1);
        MyThread lock2 = new MyThread(2);

        Thread t1 = new Thread(lock1);
        Thread t2 = new Thread(lock2);
        t1.start(); t2.start();

    }
}

```

3、公平锁

若使用synchronized关键字，是不公平锁，从等待队列中随机抽一个，会产生饥饿现象。

而公平锁需要维护一个有序队列，实现成本高，性能低。

```java
new ReentrantLock(true);
```

##### Condition对象

作用与object.wait()和object.notify()大致相同。与重入锁相关联。

```java
public static ReentrantLock lock1 = new ReentrantLock();
public static Condition condition = lock1.newCondition();

lock2.lock();
condition.await();
lock2.lock();
condition.signal();
lock2.unlock();

```

##### 生产者消费者

使用wait和notifyAll实现

```java 
/**
 * 生产者消费者
 */

public class CoTest01 {
    public static void main(String[] args) {
        SysContainer container = new SysContainer();
        Thread t1 = new Thread(new Consumer(container));
        Thread t2 = new Thread(new Producer(container));
        t1.start();
        t2.start();
    }
}

class Consumer implements Runnable{
    SysContainer container;

    public Consumer(SysContainer container){
        this.container = container;
    }

    @Override
    public void run() {
        for(int i=0;i<SysContainer.maxn*10;++i){
            System.out.println("消费："+container.pop().id);
        }
    }
}

class Producer implements Runnable{
    SysContainer container;

    public Producer(SysContainer container){
        this.container = container;
    }

    @Override
    public void run() {
        for(int i=0;i<SysContainer.maxn*10;++i){
            container.push(new Item(i));
            System.out.println("生产："+i);
        }
    }
}

class SysContainer{
    static int maxn = 1000;
    Item[] items = new Item[maxn];

    int head = 0,rear = 0;
    int cnt = 0;

    private int inc(int p){
        return (p+1)%maxn;
    }

    public synchronized void push(Item in){
        while(cnt >= maxn){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        items[head] = in;
        head = inc(head);
        cnt++;
        this.notifyAll();
    }

    public synchronized Item pop(){

        while(cnt == 0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Item res = null;
        res = items[rear];
        items[rear] = null;
        cnt--;
        this.notifyAll();
        rear = inc(rear);

        return res;
    }
}

class Item{
    int id;
    Item(int id){
        this.id = id;
    }
}
```



```java
package consumerProducer;//还有bug

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerProducer {

    public static void main(String[] args) throws InterruptedException {
        Thread producer[] = new Thread[10];
        Thread consumer[] = new Thread[10];
        Container container = new Container();

        for(int i=0;i<10;++i){
            producer[i] = new Thread(new Producor(container));
            producer[i].start();
        }

        for(int i=0;i<5;++i){
            consumer[i] = new Thread(new Consumer(container));
            consumer[i].start();
        }
    }
}

class Container{

    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    final int size = 10;
    int count = 0;
    int putIndex = 0;
    int takeIndex = 0;
    private Integer elem[] = new Integer[size];             //循环队列
    int ptr = 0;

    int inc(int index){
        return (index+size+1) % size;
    }

    void debug() {
        for (int i = 0; i < size; ++i) {
            System.out.print(elem[i] + " ");
        }
        System.out.println();
    }

    public void put() throws InterruptedException {
        try{
            lock.lockInterruptibly();
            while(count == size){
                notFull.await();            //如果队列满则等待
            }
            insert(ptr);
            System.out.println("生产产品: "+ ptr + ", 位置: "+ putIndex + "数量: " + count);
            debug();
            ptr++;
        }finally {
            lock.unlock();
        }
    }

    void insert(int a){
        elem[putIndex] = a;
        putIndex = inc(putIndex);
        ++count;
        notEmpty.signal();
    }

    //消费

    int Get() throws InterruptedException{
        lock.lockInterruptibly();
        try {
            while(count ==0){
                notEmpty.await();
                //System.out.println("消费者等待");
            }
            int e = extract();
            System.out.println("得到产品: " + e+" 位置: "+(takeIndex-1)+" 数量: "+count);
            debug();
            return e;
        }finally {
            lock.unlock();
        }
    }

    private int extract(){
        int e = elem[takeIndex];
        elem[takeIndex] = null;
        takeIndex = inc(takeIndex);
        --count;
        notFull.signal();
        return e;
    }
}


class Producor implements Runnable {
    Container container;

    Producor(Container container){
        this.container = container;
    }

    @Override
    public void run() {

        while(true){
            try {
                container.put();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable{
    Container container;

    Consumer(Container container){
        this.container = container;
    }

    @Override
    public void run() {
        while(true) {
            int e;
            try {
                e = container.Get();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
```



#### 并行基础

六种状态：NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING、TERMINATED

![](D:\技术\学习笔记\Java\线程状态.PNG)

![](D:\技术\学习笔记\Java\线程状态2.PNG)

进入就绪状态的4种情况：

1. start()
2. 由阻塞状态结束
3. yield()
4. JVM调度

进入阻塞的情况：

1. sleep
2. wait
3. join
4. read/write



RUN：new Thread之后

RUNNABLE：start之后

WAITING：sleep, join

TIME_WAITING：指定时间的sleep

BLOCKED：IO阻塞或者wait

TERMINATE：线程结束



基本操作：

1、新建线程

```java
Thread t1 = new Thread();
t1.start();							//start方法执行run方法，但如果只执行run方法，其实不会新创建线程
```

继承Runnable接口，重写run方法

2、终止线程

```java
t1.stop();				//此方法已经被废弃，且随意终止线程可能造成数据不一致错误
```

3、线程中断

```java
public void Thread.interrupt();				//线程中断
public boolean Thread.isInterrupted();		//判断是否被中断
public static boolean Thread.interrupted();	//判断是否被中断，并清除当前中断状态
```

```java
Thread t1 = new Thread(){
  	public void run(){
        while(true){
            if(Thread.currentThread().isInterrupted()){			//判断的是否被中断
                System.out.println("Interrupted");				
                break;
            }
        }
    }  
};
```

Thread.sleep方法：让当前线程休眠若干时间，当睡眠时被中断会抛出InterruptedException，它不是运行时异常，程序必须捕获并处理它。

4、等待**wait**和通知**notify**：都属于Object类。

如果一个线程调用wait，则会进入object对象的等待队列；notify方法被调用后，随机选择一个线程唤醒。

wait和sleep：wait会释放目标对象的锁；而sleep不释放任何资源。

5、等待线程结束**join**和谦让**yield**

join：本质是调用wait方法，是成员方法

！**不要在应用程序中，在Thread对象上使用类似wait()和notify()方法。可能会影响系统API。**

对优先级较低的线程，可以在合适的地方调用yield方法让出CPU资源。

yield： 不是阻塞线程，而是将线程从**运行状态**转入**就绪状态**，有利于公平竞争。静态方法，在哪个线程中调用，就对哪个线程作用

#### 线程池

线程的数量若不加控制则会产生不利的影响。线程虽小，但本身也要占用内存空间的，处理不佳会导致Out of Memory异常。即使没有，也会给GC带来很大压力。为了避免频繁地创建和小虎线程，可以复用线程。

在线程池中，总有一些激活状态的线程。当需要使用的时候，可以从池子中随便拿一个空闲线程，当完成工作后，并不着急关闭它，而是将其退回线程池

```java
public class MyThread implements Runnable{

    @Override
    public void run() {
        System.out.println(System.currentTimeMillis()+":Thread ID: "+Thread.currentThread().getId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread task = new MyThread();
        ExecutorService es = Executors.newFixedThreadPool(5);		//数量为5
        for(int i=0;i<10;++i){			
            es.submit(task);										//提交任务，可以发现第一波的五个线程早于第二波的五个线程，且线程ID都是同样的五个
        }
    }
}
```

Executors类似线程工厂，ThreadPoolExecutor类实现了Executors接口，通过这个接口，任何Runnable对象都可以被它调用。

#### 定时调度

```JAVA
/**
 * 借助Timer类和TimeTask类实现
 */

public class TimeTask01 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        Calendar cal = new GregorianCalendar(2099,12,31,21,53,54);
        timer.schedule(new MyTask01(),cal.getTime(),1000);
    }
}

class MyTask01 extends TimerTask {
    @Override
    public void run() {
        System.out.println("xwl");
    }
}


```

#### ThreadLocal

```java
public class ThreadLocalTest01 {
//    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>(){
//        @Override
//        protected Integer initialValue() {
//            return 200;
//        }
//    };
    private static ThreadLocal threadLocal = ThreadLocal.withInitial(()->{
        return 200;
    });

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName()+ "-->"+ threadLocal.get());
        threadLocal.set(100);

        new Thread(new MyRun()).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName()+ "-->"+ threadLocal.get());
    }

    public static class MyRun implements Runnable{
        @Override
        public void run() {
            threadLocal.set((int)Math.random()*99);     //不影响主线程的值
            System.out.println(Thread.currentThread().getName()+ "-->"+ threadLocal.get());
        }
    }
}

```

线程之间独立的工作区，不共享；分析上下文，看是谁调用的threadLocal，则调用的是谁的工作区

InheritableThreadLocal：继承上下文环境的数据，拷贝一份给自线程

#### 自定义可重入锁

```java
public class ReentrantLock {
    public static void main(String[] args) {
        ReLock lock = new ReLock();
        lock.lock();
        lock.doSomething();
        lock.unLock();
    }
}

class ReLock{
    private boolean locked = false;
    private Thread lockedBy = null;
    private int holdCount = 0;

    public synchronized void lock(){
        Thread t = Thread.currentThread();
        while(locked &&  lockedBy != t){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        locked = true;
        lockedBy = t;
        holdCount++;
    }

    public synchronized void unLock(){
        Thread t = Thread.currentThread();
        if(lockedBy == t){
            holdCount--;
            if(holdCount ==0 ){
                lockedBy = null;
                locked = false;
                notify();
            }
        }
    }

    public void doSomething(){
        System.out.println(holdCount);
        lock();
        System.out.println(holdCount);
        unLock();
        System.out.println(holdCount);
    }
}
```

#### 原子操作

悲观锁：synchronized是独占锁即悲观所，会导致其它所有需要锁的线程挂起，等待持有锁的线程释放锁；

乐观锁：每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。

CAS：比较并交换，java中可使用并发包中的AtomicInteger实现秒杀。

### 七、IO

#### IO工具类

```java
public class IOUtil {

    public static void copyFile(String inputFileSrc, String outputFilesrc) throws FileNotFoundException {
        InputStream in = new FileInputStream(inputFileSrc);
        OutputStream out = new FileOutputStream(outputFilesrc);
        copyStream(in,out);
        close(in,out);      //关闭流
    }

    public static void copyStream(InputStream in, OutputStream out){
        byte[] bytes = InputStreamToBytes(in);
        bytesToOutputStream(out,bytes);
    }


    public static byte[] InputStreamToBytes(InputStream in){
        byte[] dest = null;
        ByteArrayOutputStream baos = null;
        in = new BufferedInputStream(in);				//提速
        try {
            baos  = new ByteArrayOutputStream();
            dest = new byte[1024*10];
            int len = -1;
            while(-1 != (len = in.read(dest))){
                baos.write(dest,0,len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(baos);
        }
        return null;
    }

    /**
     * 使用ByteArrayInputStream(不用也行) 和 FileOutputStream
     * @param bytes

     */
    public static void bytesToOutputStream(OutputStream out, byte[] bytes){
        out = new BufferedOutputStream(out);				//提速
        try {
            out.write(bytes,0,bytes.length);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable ...ios){
        for(Closeable io: ios){
            if(io!=null){
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

转换流

```java

/**
 * 转换流：InputStreamReader OutputStreamReader
 */

public class ConvertTest {
    public static void main(String[] args) {
        try {
            web();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void web() throws IOException {
        BufferedReader is = new BufferedReader( new InputStreamReader(
                new URL("http://www.baidu.com").openStream(),"UTF-8"));          //读取源码

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("baidu.html"),"UTF-8"));

        int tmp = -1;
        while((tmp = is.read())!=-1){
            System.out.print((char)tmp);
            writer.write(tmp);
//            writer.newLine();
        }
        writer.flush();
    }

    public static void keyboard(){
        //由键盘读取的字节流转化为字符流并输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            String msg = "";
            while (!msg.equals("exit")) {
                msg = reader.readLine();
                writer.write(msg);
                writer.newLine();
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
```



#### 包装类

##### 缓冲流

BufferedInputStream/BufferedReader (out/writer)

##### 数据流

DataInputStream(out)

在存取数据的同时，保持数据类型，传输的自定义对象要实现Serializable接口

transient关键字：该数据不需要序列化

##### 打印流

```java
public class PrintTest {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream ps = System.out;
        ps.println("打印流");
        ps.println(true);

        ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("out.txt")),
                true);      //创建时可指定是否自动flush
        ps.println("stream");
        ps.println(1);
//        ps.flush();

        //重定向
        System.setOut(ps);
        System.out.println("change");
        //重定向回控制台
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out)),true));
        System.out.println("I am back...");


        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream("out.txt")), true);
        writer.println("writer");
    }
}
```



### 八、网络编程

URI = URL+URN(统一资源名称)

```java
URL url = new URL("http://www.xiuwenli.cn:8080/index.html?name=xwl&pswd=123#a");
```

#### URLConnection

```java

public class Srawler {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.dianping.com");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent" ,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String msg = null;
        while(null != (msg= reader.readLine())){
            System.out.println(msg);
        }
    }
}

```



#### UDP

```java
/**
 * 接收端
 * 1. 使用DatagramSocket 指定端口 创建接收端
 * 2. 准备容器 封装成DataGramPacket 包裹
 * 3. 阻塞式接收包裹receive(DataGramPacket p)
 * 4. 分析数据
 *      byte[] getData()
 *              getLength()
 */
public class UDPReceive {
    public static void main(String[] args) throws SocketException {
        System.out.println("接收端开启");
        DatagramSocket server = new DatagramSocket(9999);
        byte[] container = new byte[1024*60];                   //容器
        DatagramPacket packet = new DatagramPacket(container,0,container.length);
        try {
            server.receive(packet);             //阻塞式接收
            byte[] data = packet.getData();
            int len  = packet.getLength();
//            System.out.println(new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            server.close();
        }
    }
}
```

```java
/**
 * 接收端
 * 1. 使用DatagramSocket 指定端口 创建接收端
 * 2. 准备数据 转成字节数组
 * 3. 封装成DataGramPacket 指定目的地和端口
 * 4 阻塞式接收包裹send(DataGramPacket p)
 * 5. 释放资源
 */
public class UDPSend {
    public static void main(String[] args) throws SocketException {
        System.out.println("发送方准备中...");
        DatagramSocket client = new DatagramSocket(8888);
        String data = "XWL";
        byte[] bytes = data.getBytes();
        DatagramPacket packet = new DatagramPacket(bytes,0, data.length(),
                new InetSocketAddress("localhost",9999));

        try {
            client.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }
}

```

传对象：

```java
public class UDPFileReceive {
    public static void main(String[] args) throws SocketException {
        System.out.println("接收端开启");
        DatagramSocket server = new DatagramSocket(9999);
        byte[] container = new byte[1024*60];                   //容器
        DatagramPacket packet = new DatagramPacket(container,0,container.length);
        try {
            server.receive(packet);             //阻塞式接收
//            DataInputStream dis = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(packet.getData())));
//            System.out.println(dis.readInt());
//            System.out.println(dis.readUTF());
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(packet.getData())));
            MyPacket mp = (MyPacket) ois.readObject();
            System.out.println(mp.id+" "+mp.name);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

```

```java
public class UDPFileSend {
    public static void main(String[] args) throws IOException {
        System.out.println("发送方准备中...");
        DatagramSocket client = new DatagramSocket(8888);
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
//        DataOutputStream dos = new DataOutputStream(bais);
//        dos.writeInt(1);
//        dos.writeUTF("李修文");
//        dos.flush();

        MyPacket obj = new MyPacket();
        obj.id = 15;
        obj.name ="lxw";

        ObjectOutputStream oos = new ObjectOutputStream(bais);
        oos.writeObject(obj);
        oos.flush();

        byte[] bytes = bais.toByteArray();


        DatagramPacket packet = new DatagramPacket(bytes,0, bytes.length,
                new InetSocketAddress("localhost",9999));

        try {
            client.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }
}
```

### *其他

#### System.out.println

语句：System.out.println();

System是一个类，继承自根类Object

out是类PrintStream类实例化的一个静态变量。

println()是类PrintStream的成员方法，被对象out调用

![img](https://img-blog.csdn.net/20180601211607892?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzE5MDEwNjI1/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

详细讲解：https://www.cnblogs.com/skywang12345/p/io_17.html



