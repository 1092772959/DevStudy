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

指令重排不可违背的基本原则：

1. 程序顺序原则：一个线程内保证语义的串行性；
2. volatile规则：volatile变量的写先于读发生，这保证了volatile变量的可见性
3. 锁规则：解锁(unlock)必然发生于加锁(lock)前；
4. 传递性：A先于B，B先于C，则A必先于C；
5. 线程的start()方法先于它的每一个动作；
6. 线程的所有操作先于线程的终结Thread.join()；
7. 线程的中断(interrupt())先于被中断线程的代码；
8. 对象的构造函数的执行、结束先于finalize()方法；

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
package consumerProducer;

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

多次聊天：

```java
public class UDPTalk01 {
    public static void main(String[] args) throws SocketException {
        System.out.println("talk01 prepared");
        Thread t1 = new Thread(new TalkSend(10007,9999));
        Thread t2 = new Thread(new TalkReceive(8888));
        t1.start();
        t2.start();
    }
}

class TalkSend implements Runnable{
    private DatagramSocket client;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Integer to;

    TalkSend(int port, int to){
        try {
            client = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.to = to;
    }

    @Override
    public void run() {
        while(true){
            try {
                String data = reader.readLine();
                byte[] bytes = data.getBytes();
                DatagramPacket packet = new DatagramPacket(bytes,0,bytes.length,
                        new InetSocketAddress("localhost",to));
                client.send(packet);
                if(data.equals("bye")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class TalkReceive implements Runnable{
    private DatagramSocket client;
    private BufferedReader reader;

    TalkReceive(int port){
        try {
            client = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            byte[] container = new byte[1024*20];
            DatagramPacket packet = new DatagramPacket(container,0,container.length);

            try {
                client.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet.getData())));
            String msg ="";
            try {
                msg = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(msg);
            if(msg.equals("bye")){
                break;
            }
        }
    }
}

public class UDPTalk02 {
    public static void main(String[] args) {
        System.out.println("talk01 prepared");
        Thread t1 = new Thread(new TalkSend(10008,8888));
        Thread t2 = new Thread(new TalkReceive(9999));
        t1.start();
        t2.start();
    }
}
```

#### TCP

服务端和客户端不平等，服务端需要打开一个端口监听客户端的连接。

多人聊天室-server

```java
package tcp;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server01 {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8888);
        CopyOnWriteArrayList<Listen> connections = new CopyOnWriteArrayList<>();

        Boolean isRun = true;
        while(isRun){
            Socket client = server.accept();
            System.out.println("一个用户建立了连接");

            new Thread(()->{
                //建立了一个连接
                DataInputStream dis =  null;
                DataOutputStream dos = null;
                String name = null, pswd = null;

                try {
                    dos = new DataOutputStream(client.getOutputStream());
                    dis = new DataInputStream(client.getInputStream());
                    name = dis.readUTF();
                    pswd = dis.readUTF();
                    if(!check(name,pswd)){
                        System.out.println(name+"认证失败");
                        dos.writeUTF("fail");
                        return;
                    }
                    else{
                        System.out.println(name+ "进入了聊天室");
                        dos.writeUTF("success");
                    }
                    dos.flush();
                } catch (IOException e) {       //出错就关闭连接
                    IOUtil.close(client);
                    return;
                }

                Listen myListen = new Listen(client,connections,name);

                connections.add(myListen);
                Thread relay = new Thread(myListen);
                relay.start();
            }).start();
        }
        server.close();
    }

    static boolean check(String name, String pswd){
        return true;
    }
}

class Listen implements Runnable{
    Socket client = null;
    CopyOnWriteArrayList<Listen> connections = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    String id;

    public Listen(Socket client, CopyOnWriteArrayList<Listen> conn,String name){
        this.client = client;
        this.connections = conn;
        this.id = name;
        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    public void sendOthers(String msg) throws IOException {
        for(Listen s: connections){
            if(s == this){
                continue;
            }
            s.sendToMe(msg);
        }
    }

    public void sendToMe(String msg) throws IOException {
        dos.writeUTF(msg);
        dos.flush();
    }

    @Override
    public void run() {
        boolean isRunnable = true;
        while(isRunnable){
            String msg = null;
            DataOutputStream dos = null;
            try {
                msg = dis.readUTF();
                String convert = id+":"+msg;
                System.out.println(convert);
                sendOthers(convert);
                //结束
                if(msg.equals("bye")){
                    isRunnable = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                isRunnable = false;
            }
        }
        this.close();
    }

    private void close(){
        connections.remove(this.id);

        IOUtil.close(dis,client);
        System.out.println(connections.size());
    }
}


class IOUtil{
    static void close(Closeable ... closeables){
        for(Closeable t: closeables){
            if(t!=null){
                try {
                    t.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

```

client

```java
package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Talk02 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket client = new Socket("localhost",8888);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("输入用户名:");
        String name = console.readLine();
        System.out.print("输入密码:");
        String pswd = console.readLine();
        DataInputStream dis = new DataInputStream(client.getInputStream());
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        dos.writeUTF(name);
        dos.writeUTF(pswd);
        dos.flush();
        String res = dis.readUTF();
        System.out.println(res);
//        dos.close(); dis.close();

        if(res.equals("success")){

            MyConnect con = new MyConnect(client);
            Thread t1 = new Thread(new ClientListen(con));
            Thread t2 = new Thread(new ClientSend(con));
            t1.start(); t2.start();
            t2.join(); t2.join();
            System.out.println("结束");
        }
    }
}

class MyConnect{
    Socket client;
    DataInputStream dis;
    DataOutputStream dos;
    boolean isUsing = false;

    public MyConnect(Socket client) {
        this.client = client;

        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() throws IOException {
        String msg = null;
        isUsing = true;
        if(!client.isClosed()){
            msg = dis.readUTF();
        }
        isUsing = false;
        return msg;
    }

    public boolean send(String msg) throws IOException {
        if(client.isClosed()){
            return false;
        }

        dos.writeUTF(msg);
        dos.flush();
        if(msg.equals("bye")){
            dos.close();
            dis.close();
            client.close();
        }
        return true;
    }
}

class ClientListen implements Runnable{
    private MyConnect connect;

    public ClientListen(MyConnect connect){
        this.connect = connect;
    }

    @Override
    public void run() {
        boolean isRunnable = true;
        while(isRunnable){
            String msg = null;
            try {
                msg = connect.read();
            } catch (IOException e) {
                e.printStackTrace();
                isRunnable = false;
            }
            if(msg == null){
                isRunnable = false;
            }
            System.out.println(msg);
        }
    }
}

class ClientSend implements Runnable{
    private MyConnect connect;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public ClientSend(MyConnect connect){
        this.connect = connect;
    }

    @Override
    public void run() {
        boolean isRunnable = true;
        while(isRunnable){
            String msg = null;
            try {
                msg = reader.readLine();
                if(msg.equals("")) continue;

                boolean res = connect.send(msg);
                if(!res){
                    isRunnable = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("写报错，退出");
                isRunnable = false;
            }
        }
    }
}
```

