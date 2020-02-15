### 创建方法

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

lock_guard 对象通常用于管理某个锁(Lock)对象。在某个 lock_guard 对象的声明周期内，它所管理的锁对象会一直保持上锁状态；而 lock_guard 的生命周期结束之后，它所管理的锁对象会被解锁(注：类似 shared_ptr 等智能指针管理动态分配的内存资源 )。



模板参数 Mutex 代表互斥量类型，例如 std::mutex 类型，它应该是一个基本的 BasicLockable 类型，标准库中定义几种基本的 BasicLockable 类型，分别 std::mutex, std::recursive_mutex, std::timed_mutex，std::recursive_timed_mutex (以上四种类型均已在上一篇博客中介绍)以及 std::unique_lock(本文后续会介绍 std::unique_lock)。(注：BasicLockable 类型的对象只需满足两种操作，lock 和 unlock，另外还有 Lockable 类型，在 BasicLockable 类型的基础上新增了 try_lock 操作，因此一个满足 Lockable 的对象应支持三种操作：lock，unlock 和 try_lock；最后还有一种 TimedLockable 对象，在 Lockable 类型的基础上又新增了 try_lock_for 和 try_lock_until 两种操作，因此一个满足 TimedLockable 的对象应支持五种操作：lock, unlock, try_lock, try_lock_for, try_lock_until)。



```cpp
std::mutex mu;

void shared_print(std::string &msg, int id){
  std::lock_guard<std::mutex> guard(mu);  
    std::cout << msg << id << std::endl;
}
```

此时cout仍未在guard对象保护下，其他地方仍可以使用cout.



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







### Reference

https://www.cnblogs.com/haippy/p/3346477.html



