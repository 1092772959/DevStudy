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

Java虚拟机的内存可分为三个区域：栈stack、堆heap、方法区method area

栈：

1. 描述的是**方法执行的内存模型**，每个方法被调用都会创建一个栈帧（存储局部变量、操作数、方法出口等）
2. JVM为**每个线程**创建一个栈，用于存放该线程执行方法的信息（实参、局部变量等）
3. 栈属于**线程私有**，不能实现线程间共享。
4. FILO
5. 由系统自动分配，**速度快**！栈是一个**连续**的内存空间。

堆：

1. 堆用于存储创建好的**对象和数组**（数组也是对象）。
2. JVM只有一个堆，被**所有线程共享**。
3. 堆是一个不连续的内存空间，分配灵活，**速度慢**。

方法区（静态区）：

1. JVM只有一个方法区，被**所有线程共享**。
2. 方法区实际也是堆，只是用于存储类、常量相关的信息
3. 用来存放程序中永远不变或唯一的内容（代码、类信息、静态变量、字符串常量）

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

JDK1.8后，单个桶的长度大于8后，转换为红黑树。

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

##### TreeMap

红黑树的典型实现。效率比HashMap低，一般排序的时候才使用。

若果要对自定义类进行排序，则该类需要实现Comparable接口。

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



