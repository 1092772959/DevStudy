### thread

```cpp
#include<iostream>
#include<thread>

//1. 通过函数创建
void func(){
	std::cout << "this is a mutil_thread program" <<  std::endl;
}

//2.通过类创建
class Node{
public:
    void operator() (std:: string msg){
        for(int i=0;i>-100; --i){
            std::cout << "in mutil: " << i << ", " << msg << std::endl;
        } 
    }
};

int main(){
  	std::thread t1((Node()));
  	
  
  	std::string msg = "xwl";
		std::thread t2((Node()), msg);

    try{

        for(int i=0;i<100; i++){
            //如果此处抛出了异常，则未等t1 join, t1已经被销毁；因此要try catch捕获
            std::cout << "from main: " << i << std::endl;
        }

    }catch(...){
        t1.join();
     		t2.join();
        throw;
    }

  t1.join();
  t2.join();
	return 0;
}
```



引用传参

```cpp
class Node{
public:
    void operator() (std:: string &msg){
        msg = "super man";
    }
};

int main(){
  std::string msg = "xwl";
	//此时传递的还是值, c++高版本编译不通过
  //std::thread t2((Node()), msg);	
  
  std::thread t1((Node()), std::ref(msg));
  return 1;
}
```



move传参

```cpp
class Node{
public:
    void operator() (std:: string msg){
        msg = "super man";
    }
};

int main(){
  std::string msg = "xwl";
  
  std::thread t1((Node()), std::move(msg));
  //此时msg已为空
  
  return 1;
}
```



### 同步

#### mutex

```cpp

std::mutex mu;

void shared_print(std::string &msg, int id){
    mu.lock();
    std::cout << msg << id << std::endl;
    mu.unlock();
}

void func(){
    std::string str = "thread";
    for( int i=0;i>-100;--i){   
        shared_print(str , i );
    }
}


int main(){
    std::thread t1(func);
    std::string msg = "main";
    for( int i=0; i<100;++i){
        shared_print(msg, i);
    }
    t1.join();
	return 0;
}
```

但此时如果lock和unlock之间抛出异常，则mu永远不会被释放。为了解决这种情况，引入`std::lock_guard<std::mutex>`



#### lock_guard

in`mutex` header file

lock_guard 对象通常用于管理某个锁(Lock)对象。在一个 lock_guard 对象的**声明周期**内，它所管理的锁对象会一直保持上锁状态；而 lock_guard 的生命周期结束之后，它所管理的锁对象会被解锁(注：类似 shared_ptr 等智能指针管理动态分配的内存资源 )。



模板参数 Mutex 代表互斥量类型，例如 std::mutex 类型，它应该是一个基本的 BasicLockable 类型，标准库中定义几种基本的 BasicLockable 类型，分别 std::mutex, std::recursive_mutex, std::timed_mutex，std::recursive_timed_mutex 以及 std::unique_lock。(BasicLockable 类型的对象只需满足两种操作，lock 和 unlock，另外还有 Lockable 类型，在 BasicLockable 类型的基础上新增了 try_lock 操作，因此一个满足 Lockable 的对象应支持三种操作：lock，unlock 和 try_lock；最后还有一种 TimedLockable 对象，在 Lockable 类型的基础上又新增了 try_lock_for 和 try_lock_until 两种操作，因此一个满足 TimedLockable 的对象应支持五种操作：lock, unlock, try_lock, try_lock_for, try_lock_until)。



```cpp
std::mutex mu;

void shared_print(std::string &msg, int id){
  std::lock_guard<std::mutex> guard(mu);  
    std::cout << msg << id << std::endl;
}
```

此时cout仍未在guard对象保护下，其他地方仍可以使用cout.



#### unique_lock

```c++
void inMsgRecvQueue(){
		for (int i = 0; i < 10000; i++){
			std::unique_lock<std::mutex> sbguard(my_mutex, std::defer_lock);//没有加锁的my_mutex
			
			if (sbguard.try_lock() == true)//返回true表示拿到锁了{
				msgRecvQueue.push_back(i);
				//...
				//其他处理代码
			}
			else{
				//没拿到锁
				cout << "inMsgRecvQueue()执行，但没拿到锁头，只能干点别的事" << i << endl;
      }
		}
	}

```



difference with lock_guard

- `unique_lock`和`lock_guard`都不能复制，`lock_guard`不能移动，但是`unique_lock`可以
- lock_guard只能保证在析构时执行解锁，本身没有提供加锁解锁功能，而是在自己的生命周期内对锁资源保持占有；而unique_lock需手动调用lock/unlock，因此可以实现比lock_guard更为细粒度的锁
- unique_lock提供 try_to_lock, adopt_lock, defer_lock等选项

##### std::lock



```c++
    // don't actually take the locks yet
    std::unique_lock<std::mutex> lock1(_mu);
    std::unique_lock<std::mutex> lock2(_mu);
 
    // lock both unique_locks without deadlock
    std::lock(lock1, lock2);
```



#### 死锁

```cpp
#include<iostream>
#include<thread>
#include<mutex>

class File{
    std::mutex mu;
    std::mutex mu2;

    //被mutex保护的对象

public:
    File(){}
    
    void shared_print(int val){
        std::lock_guard<std::mutex> locker(mu);
        std::lock_guard<std::mutex> locker2(mu2);
        std::cout << "pro-1: " << val << std::endl;
    }

    void shared_print02(int val){
        std::lock_guard<std::mutex> locker(mu2);
        std::lock_guard<std::mutex> locker2(mu);
        std::cout << "pro-2: " << val << std::endl;
    }
};

void func(File & f){
    for(int i=0;i>-100; --i){
        f.shared_print(i);
    }
}

int main(){
    File f;
    std::thread t1(func, std::ref(f));
    
    for(int i=0;i<100;++i){
        f.shared_print02(i);
    }
    
    t1.join();
    return 1;
}
```





避免方法：

```cpp
public:
    File(){}
      
    void shared_print(int val){
        std::lock(mu, mu2);
        std::lock_guard<std::mutex> locker(mu, std::adopt_lock);
        std::lock_guard<std::mutex> locker2(mu2, std::adopt_lock);
        std::cout << "pro-1: " << val << std::endl;
    }

    void shared_print02(int val){
        std::lock(mu, mu2);
        std::lock_guard<std::mutex> locker(mu2, std::adopt_lock);
        std::lock_guard<std::mutex> locker2(mu, std::adopt_lock);
        std::cout << "pro-2: " << val << std::endl;
    }
};
```





#### Sleep

```c++
#include <unistd.h>

uint microsec = 8000; //8ms

usleep(microsec);
```





### Atomic instructions

#### atomic var

| 原子指令 (x均为std::atomic<int>)                 | 作用                                                         |
| :----------------------------------------------- | :----------------------------------------------------------- |
| x.load()                                         | 返回x的值。                                                  |
| x.store(n)                                       | 把x设为n，什么都不返回。                                     |
| x.exchange(n)                                    | 把x设为n，返回设定之前的值。                                 |
| x.compare_exchange_strong(expected_ref, desired) | 若x等于expected_ref，则设为desired，返回成功；否则把最新值写入expected_ref，返回失败。 |
| x.compare_exchange_weak(expected_ref, desired)   | 相比compare_exchange_strong可能有[spurious wakeup](http://en.wikipedia.org/wiki/Spurious_wakeup)。 |
| x.fetch_add(n), x.fetch_sub(n), x.fetch_xxx(n)   | x += n, x-= n（或更多指令），返回修改之前的值。              |



#### cache line

当多个线程同时访问一个cacheline，则会发生竞争。L1 dcache和icache，256K的L2 cache和15M的L3 cache。其中L1和L2cache为每个核心独有，L3则所有核心共享。一个核心写入自己的L1 cache是极快的(4 cycles, 2 ns)，但当另一个核心读或写同一处内存时，它得确认看到其他核心中对应的cacheline。对于软件来说，这个过程是原子的，不能在中间穿插其他代码，只能等待CPU完成[一致性同步](https://en.wikipedia.org/wiki/Cache_coherence)，这个复杂的算法相比其他操作耗时会很长。所以访问被多个线程频繁共享的内存是比较慢的。

samples：

- 一个依赖全局多生产者多消费者队列(MPMC)的程序难有很好的多核扩展性，因为这个队列的极限吞吐取决于同步cache的延时，而不是核心的个数。最好是用多个SPMC或多个MPSC队列，甚至多个SPSC队列代替，在源头就规避掉竞争。
- 另一个例子是全局计数器，如果所有线程都频繁修改一个全局变量，性能就会很差，原因同样在于不同的核心在不停地同步同一个cacheline。如果这个计数器只是用作打打日志之类的，那我们完全可以让每个线程修改thread-local变量，在需要时再合并所有线程中的值，性能可能有几十倍的差别。



#### memory fencing

只使用原子指令并不能完全保证并发访问资源的。因为**编译器**和**CPU**都会对指令进行重排，即编译期重排和执行期重排。只要没有依赖，代码中位于后面的指令（包括访存）都可能被编排到前面。次外，访问L3 cacheline的同步也会导致即使顺序地对多个原子变量进行修改，但其更新已不同的顺序被其他cpu核观察到（第一次被更新的变量未必是第一个被同步的）。

sample:

```cpp
// 线程1
// ready was initialized to false
p = new Node();
ready = true;


//线程2
if (ready) {
    p->do_something();
}
```



从人的角度理解代码，我们希望观察到当指针p在线程1中被赋值之后，才会被线程2调用。然而在多核机器中，这段代码存在问题。

- ready = true 可能被编译期或CPU编排到p = new Node()之前，从而线程2看到ready为true时，p仍被赋值；
- 即使指令没有被重排，ready和p也会被独立地同步到其他核（L3 cacheline），可能线程2可能看到ready为true时，p仍是未赋值状态。

为了解决这些问题，CPU提供了memory fencing(内存屏障)

| memory order         | 作用                                                         |
| :------------------- | :----------------------------------------------------------- |
| memory_order_relaxed | no fence                                                     |
| memory_order_consume | 后面依赖此原子变量的访存指令勿重排至此条指令之前；Writes to data-dependent variables in other threads that release the same atomic variable are visible in the current thread. |
| memory_order_acquire | 后面访存指令勿重排至此条指令之前                             |
| memory_order_release | 前面访存指令勿重排至此条指令之后。当此条指令的结果对其他线程可见后，之前的所有指令都可见（搭配consum/acquire） |
| memory_order_acq_rel | acquire + release语意                                        |
| memory_order_seq_cst | acq_rel语意外加所有使用seq_cst的指令有严格地全序关系         |

##### Acquire vs consume

`memory_order_acquire`

If an atomic store in thread A is tagged memory_order_release and an atomic load in thread B from the same variable is tagged memory_order_acquire, **all memory writes** (non-atomic and relaxed atomic) that *happened-before* the atomic store from the point of view of thread A, become *visible side-effects* in thread B. That is, once the atomic load is completed, thread B is guaranteed to see everything thread A wrote to memory.



`memory_order_consume`

If an atomic store in thread A is tagged memory_order_release and an atomic load in thread B from the same variable that read the stored value is tagged memory_order_consume, all memory writes (non-atomic and relaxed atomic) that *happened-before* the atomic store from the point of view of thread A, become *visible side-effects* within those operations in thread B into which the load operation **carries dependency**, that is, once the atomic load is completed, those operators and functions in thread B that use the value obtained from the load are guaranteed to see what thread A wrote to memory.



核心的区别是acquire线程可以看到另一个release线程中release前的所有变量的内存更新；但consume线程只能看到有数据依赖的变量的内存更新。



test for acquire:

```c++
std::atomic<std::string*> ptr;
int data;
 
void producer()
{
    std::string* p  = new std::string("Hello");
    data = 42;
    ptr.store(p, std::memory_order_release);
}
 
void consumer()
{
    std::string* p2;
    while (!(p2 = ptr.load(std::memory_order_acquire)))
        ;
    assert(*p2 == "Hello"); // never fires
    assert(data == 42); // never fires
}
 
int main()
{
    std::thread t1(producer);
    std::thread t2(consumer);
    t1.join(); t2.join();
}
```





test for consume:

```c++
std::atomic<std::string*> ptr;
int data;
 
void producer()
{
    std::string* p  = new std::string("Hello");
    data = 42;
    ptr.store(p, std::memory_order_release);
}
 
void consumer()
{
    std::string* p2;
    while (!(p2 = ptr.load(std::memory_order_consume)))
        ;
    assert(*p2 == "Hello"); // never fires: *p2 carries dependency from ptr
    assert(data == 42); // may or may not fire: data does not carry dependency from ptr
}
 
int main()
{
    std::thread t1(producer);
    std::thread t2(consumer);
    t1.join(); t2.join();
}
```



#### Happens-before

##### Dependency-ordered before

Between threads, evaluation A is *dependency-ordered before* evaluation B if any of the following is true

1. A performs a **release operation** on some atomic M, and, in a different thread, B performs a **consume operation**on the same atomic M, and B reads a value written by any part of the release sequence headed (until C++20) by A.

2. A is dependency-ordered before X and X carries a dependency into B.

##### Inter-thread happens-before

Between threads, evaluation A *inter-thread happens before* evaluation B if any of the following is true

1. A *synchronizes-with* B
2. A is *dependency-ordered before* B
3. A *synchronizes-with* some evaluation X, and X is *sequenced-before* B
4. A is *sequenced-before* some evaluation X, and X *inter-thread happens-before* B
5. A *inter-thread happens-before* some evaluation X, and X *inter-thread happens-before* B





Ref:

https://en.cppreference.com/w/cpp/atomic/memory_order

https://en.wikipedia.org/wiki/Non-blocking_algorithm#Wait-freedom



#### Wait-free & Lock-free

`Wait-freedom` is the strongest non-blocking guarantee of progress, combining guaranteed system-wide throughput with [starvation](https://en.wikipedia.org/wiki/Resource_starvation)-freedom. An algorithm is wait-free if every operation has a bound on the number of steps the algorithm will take before the operation completes.[[14\]](https://en.wikipedia.org/wiki/Non-blocking_algorithm#cite_note-awilliams-14) This property is critical for real-time systems and is always nice to have as long as the performance cost is not too high.



`Lock-freedom` allows individual threads to starve but guarantees system-wide throughput. An algorithm is lock-free if, when the program threads are run for a sufficiently long time, at least one of the threads makes progress (for some sensible definition of progress). All wait-free algorithms are lock-free.



值得提醒的是，常见想法是lock-free或wait-free的算法会更快，但事实可能相反，因为：

- lock-free和wait-free必须处理复杂的race condition和ABA problem，完成相同目的的代码比用锁更复杂。
- 使用mutex的算法变相带“后退”效果。后退(backoff)指出现竞争时尝试另一个途径以避免激烈的竞争，mutex出现竞争时会使调用者睡眠，在高度竞争时规避了激烈的cacheline同步，使拿到锁的那个线程可以很快地完成一系列流程，总体吞吐可能反而高了。

mutex导致低性能往往是因为临界区过大（限制了并发度），或临界区过小（上下文切换开销变得突出，应考虑用adaptive mutex）。lock-free和wait-free算法的价值在于其避免了deadlock/livelock，在各种情况下的稳定表现，而不是绝对的高性能。但在一种情况下lock-free和wait-free算法的性能多半更高：就是算法本身可以用少量原子指令实现。实现锁也是要用原子指令的，当算法本身用一两条指令就能完成的时候，相比额外用锁肯定是更快了。

### Reference

https://www.cnblogs.com/haippy/p/3346477.html

gejun's doc on brpc(baidu rpc)

