## python特性



```python
#语法
cnt = 0
ret = False
cnt += 1 if ret else 2

print(cnt)
```





python不支持重载构造函数

可迭代对象：list, tuple, str, dict, set



Python内置的`enumerate`函数可以把一个list变成索引-元素对，这样就可以在`for`循环中同时迭代索引和元素本身

默认情况下，dict迭代的是key。如果要迭代value，可以用`for value in d.values()`，如果要同时迭代key和value，可以用`for k, v in d.items()`。



字典推导式：

```python

keys = ['li','xi','bi']
vals = [1,2,3]

dic = {x:y for x,y in zip(keys,vals)}

for key, val in dic.items():
    print(key, val)
```





字符串转回原类型

```python
dic = eval(string)

```

*如果包含了复杂对象，如date等，需要写一个decoder





### 生成器

通过列表生成式，我们可以直接创建一个列表。但是，受到内存限制，列表容量肯定是有限的。而且，创建一个包含100万个元素的列表，不仅占用很大的存储空间，如果我们仅仅需要访问前面几个元素，那后面绝大多数元素占用的空间都白白浪费了。

所以，如果列表元素可以按照某种算法推算出来，那我们是否可以在循环的过程中不断推算出后续的元素呢？这样就不必创建完整的list，从而节省大量的空间。在Python中，这种一边循环一边计算的机制，称为生成器：generator。

```python
g = (x **2 for x in range(20))

for i in range(20):
    print(next(g))

```

模拟斐波那契生成器：

```python

def fib(up_limit):
    n, a, b = 0, 0, 1
    while n < up_limit:
        yield b
        a, b = b, a + b
        n += 1
    
    return


# 得到
f = fib(100)

for i in range(100):
    print(next(f))
```





### 内建函数

#### map

```python
def f(x):
    return x * x

r = map(f, [1,2,3,4,5])

print(r)
```



`map()`函数接收两个参数，一个是函数，一个是`Iterable`，`map`将传入的函数依次作用到序列的每个元素，并把结果作为新的`Iterator`返回。



#### reduce

r`educe`把一个函数作用在一个序列`[x1, x2, x3, ...]`上，这个函数必须接收两个参数，`reduce`把结果继续和序列的下一个元素做累积计算。效果相当于：reduce(f, [x1, x2, x3, x4]) = f(f(f(x1, x2), x3), x4)

```python
from functools import reduce

DIGITS = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}

def char2num(s):
    return DIGITS[s]

def str2int(s):
    return reduce(lambda x, y: x * 10 + y, map(char2num, s))

print(str2int("123"))
```





#### filter

用法和map类似

```python
def is_palidrome(string):
    size = len(string)
    for i in range(size//2):
        if string[i] != string[size-1-i]:
            return False
    return True


res = filter(is_palidrome,["123456","1221","1","1001"])
print(res)
```



#### sorted







## 正则

*易忘

### re.match





例：

```python
   #若一开始不匹配则终止
    matchObj = re.match( r'(.*) are (.*?) (.*)', line, re.M|re.I)
    
    if matchObj:
        print(matchObj.span())
        print ("matchObj.group() : ", matchObj.group(), " ", matchObj.start())
        print ("matchObj.group(1) : ", matchObj.group(1), " ", matchObj.span(1))
        print ("matchObj.group(2) : ", matchObj.group(2))
        print ("matchObj.group(3) : ", matchObj.group(3))
    else:   
        print ("No match!!")   
```







### re.search



```python

    #找到第一个匹配的，与match不同的是，它会一直查找到字符串尾
    obj = re.search('Cats', line ) 
    print(obj.group(0))   

```





match和search的区别：re.match只匹配字符串的开始，如果字符串开始不符合正则表达式，则匹配失败，函数返回None；而re.search匹配整个字符串，直到找到一个匹配。





### 替换

语法：

```python
re.sub(pattern, repl, string, count=0, flags=0)
```



- pattern : 正则中的模式字符串。
- repl : 替换的字符串，也可为一个函数。
- string : 要被查找替换的原始字符串。
- count : 模式匹配后替换的最大次数，默认 0 表示替换所有的匹配。



### compile

compile 函数用于编译正则表达式，生成一个正则表达式（ Pattern ）对象，供 match() 和 search() 这两个函数使用。

语法格式为：

```python
re.compile(pattern[, flags])
```



- **pattern** : 一个字符串形式的正则表达式
- **flags** : 可选，表示匹配模式，比如忽略大小写，多行模式等，具体参数为：
  1. **re.I** 忽略大小写
  2. **re.L** 表示特殊字符集 \w, \W, \b, \B, \s, \S 依赖于当前环境
  3. **re.M** 多行模式
  4. **re.S** 即为 **.** 并且包括换行符在内的任意字符（**.** 不包括换行符）
  5. **re.U** 表示特殊字符集 \w, \W, \b, \B, \d, \D, \s, \S 依赖于 Unicode 字符属性数据库
  6. **re.X** 为了增加可读性，忽略空格和 **#** 后面的注释





### findall

在字符串中找到正则表达式所匹配的所有子串，并返回一个列表，如果没有找到匹配的，则返回空列表。

**注意：** match 和 search 是匹配一次 findall 匹配所有。

格式：

```python
findall(string[, pos[, endpos]])

```

- **string** : 待匹配的字符串。
- **pos** : 可选参数，指定字符串的起始位置，默认为 0。
- **endpos** : 可选参数，指定字符串的结束位置，默认为字符串的长度。



### finditer

和 findall 类似，在字符串中找到正则表达式所匹配的所有子串，并把它们作为一个迭代器返回。

```python
re.finditer(pattern, string, flags=0)

```

```python
res2 = reg.finditer(string, 5, 15)       #超尾
    for item in res2:
        print(item.group())
```





### split

split 方法按照能够匹配的子串将字符串分割后返回列表，它的使用形式如下：

```python
re.split(pattern, string[, maxsplit=0, flags=0])
```



```python
string = "cpu,gpu,mem"
reg = re.compile(",")
res = reg.split(string)
print(res)
```





### Flag标志修饰符

1. **re.I** 忽略大小写
2. **re.L** 表示特殊字符集 \w, \W, \b, \B, \s, \S 依赖于当前环境
3. **re.M** 多行模式
4. **re.S** 即为 **.** 并且包括换行符在内的任意字符（**.** 不包括换行符）
5. **re.U** 表示特殊字符集 \w, \W, \b, \B, \d, \D, \s, \S 依赖于 Unicode 字符属性数据库
6. **re.X** 为了增加可读性，忽略空格和 **#** 后面的注释



#### re.S



```python
import re
a = '''asdfhellopass:
    worldaf
    '''
b = re.findall('hello(.*?)world',a)
c = re.findall('hello(.*?)world',a,re.S)
print 'b is ' , b
print 'c is ' , c
　　

#运行结果：
#b is  []
#c is  ['pass:\n\t123\n\t']
```



#### re.M

```python
import re
>>> s= '12 34/n56 78/n90'
 
>>> re.findall( r'^/d+' , s , re.M )          # 匹配位于行首的数字
 
['12', '56', '90']
 
>>> re.findall( r’/A/d+’, s , re.M )        # 匹配位于字符串开头的数字
 
['12']
 
>>> re.findall( r'/d+$' , s , re.M )          # 匹配位于行尾的数字
 
['34', '78', '90']
 
>>> re.findall( r’/d+/Z’ , s , re.M )        # 匹配位于字符串尾的数字
 
['90']
```





### 常用模式

反斜杠本身需要使用反斜杠转义。

由于正则表达式通常都包含反斜杠，所以最好使用原始字符串来表示它们。模式元素(如 r'\t'，等价于 '\\t')匹配相应的特殊字符。



| ^           | 匹配字符串的开头                                             |
| ----------- | ------------------------------------------------------------ |
| $           | 匹配字符串的末尾。                                           |
| .           | 匹配任意字符，除了换行符，当re.DOTALL标记被指定时，则可以匹配包括换行符的任意字符。 |
| [...]       | 用来表示一组字符,单独列出：[amk] 匹配 'a'，'m'或'k'          |
| [^...]      | 不在[]中的字符：[^abc] 匹配除了a,b,c之外的字符。             |
| re*         | 匹配0个或多个的表达式。                                      |
| re+         | 匹配1个或多个的表达式。                                      |
| re?         | 匹配0个或1个由前面的正则表达式定义的片段，非贪婪方式         |
| re{ n}      | 精确匹配 n 个前面表达式。例如， **o{2}** 不能匹配 "Bob" 中的 "o"，但是能匹配 "food" 中的两个 o。 |
| re{ n,}     | 匹配 n 个前面表达式。例如， o{2,} 不能匹配"Bob"中的"o"，但能匹配 "foooood"中的所有 o。"o{1,}" 等价于 "o+"。"o{0,}" 则等价于 "o*"。 |
| re{ n, m}   | 匹配 n 到 m 次由前面的正则表达式定义的片段，贪婪方式         |
| a\| b       | 匹配a或b                                                     |
| (re)        | 对正则表达式分组并记住匹配的文本                             |
| (?imx)      | 正则表达式包含三种可选标志：i, m, 或 x 。只影响括号中的区域。 |
| (?-imx)     | 正则表达式关闭 i, m, 或 x 可选标志。只影响括号中的区域。     |
| (?: re)     | 类似 (...), 但是不表示一个组                                 |
| (?imx: re)  | 在括号中使用i, m, 或 x 可选标志                              |
| (?-imx: re) | 在括号中不使用i, m, 或 x 可选标志                            |
| (?#...)     | 注释.                                                        |
| (?= re)     | 前向肯定界定符。如果所含正则表达式，以 ... 表示，在当前位置成功匹配时成功，否则失败。但一旦所含表达式已经尝试，匹配引擎根本没有提高；模式的剩余部分还要尝试界定符的右边。 |
| (?! re)     | 前向否定界定符。与肯定界定符相反；当所含表达式不能在字符串当前位置匹配时成功 |
| (?> re)     | 匹配的独立模式，省去回溯。                                   |
| \w          | 匹配字母数字及下划线                                         |
| \W          | 匹配非字母数字及下划线                                       |
| \s          | 匹配任意空白字符，等价于 [\t\n\r\f].                         |
| \S          | 匹配任意非空字符                                             |
| \d          | 匹配任意数字，等价于 [0-9].                                  |
| \D          | 匹配任意非数字                                               |
| \A          | 匹配字符串开始                                               |
| \Z          | 匹配字符串结束，如果是存在换行，只匹配到换行前的结束字符串。 |
| \z          | 匹配字符串结束                                               |
| \G          | 匹配最后匹配完成的位置。                                     |
| \b          | 匹配一个单词边界，也就是指单词和空格间的位置。例如， 'er\b' 可以匹配"never" 中的 'er'，但不能匹配 "verb" 中的 'er'。 |
| \B          | 匹配非单词边界。'er\B' 能匹配 "verb" 中的 'er'，但不能匹配 "never" 中的 'er'。 |
| \n, \t, 等. | 匹配一个换行符。匹配一个制表符。等                           |
| \1...\9     | 匹配第n个分组的内容。                                        |
| \10         | 匹配第n个分组的内容，如果它经匹配。否则指的是八进制字符码的表达式。 |



| 实例        | 描述                              |
| :---------- | :-------------------------------- |
| [Pp]ython   | 匹配 "Python" 或 "python"         |
| rub[ye]     | 匹配 "ruby" 或 "rube"             |
| [aeiou]     | 匹配中括号内的任意一个字母        |
| [0-9]       | 匹配任何数字。类似于 [0123456789] |
| [a-z]       | 匹配任何小写字母                  |
| [A-Z]       | 匹配任何大写字母                  |
| [a-zA-Z0-9] | 匹配任何字母及数字                |
| [^aeiou]    | 除了aeiou字母以外的所有字符       |
| [^0-9]      | 匹配除了数字外的字符              |





| 实例 | 描述                                                         |
| :--- | :----------------------------------------------------------- |
| .    | 匹配除 "\n" 之外的任何单个字符。要匹配包括 '\n' 在内的任何字符，请使用象 '[.\n]' 的模式。 |
| \d   | 匹配一个数字字符。等价于 [0-9]。                             |
| \D   | 匹配一个非数字字符。等价于 [^0-9]。                          |
| \s   | 匹配任何空白字符，包括空格、制表符、换页符等等。等价于 [ \f\n\r\t\v]。 |
| \S   | 匹配任何非空白字符。等价于 [^ \f\n\r\t\v]。                  |
| \w   | 匹配包括下划线的任何单词字符。等价于'[A-Za-z0-9_]'。         |
| \W   | 匹配任何非单词字符。等价于 [^A-Za-z0-9_]。                   |



## 环境

### virtualenv

- 安装

```bash
sudo pip install virtualenv

sudo pip install virtualenvwrapper
```

- 创建虚拟环境

```bash
virtualenv <venv_name>		#创建环境
```

- 激活该环境

```bash
source venv_name/bin/activate

#退出
deactivate
```



使用virtualenvwrapper进行管理







### 环境变量

- 打印当前工作目录

```python
import os
print os.getwcd()
```

- 运行时添加环境变量

```python
import sys
sys.path.append('path')
```



### 系统









## 多线程

Python中使用线程有两种方式：函数或者用类来包装线程对象。

### 实现

#### 1. 函数式

语法：

```python
thread.start_new_thread ( function, args[, kwargs] )
```



- function - 线程函数。
- args - 传递给线程函数的参数,他必须是个tuple类型。
- kwargs - 可选参数。



```python
import thread
import threading
import time

def print_time(thread_name, delay):
    count = 0
    while count < 5:
        count += 1
        time.sleep(delay)
        print("{} {}".format(thread_name, time.ctime()))


# try:
#     thread.start_new_thread(print_time, ("thread_1", 2))
#     thread.start_new_thread(print_time, ("thread_2", 4))
# except:
#     print("error")

t1 = threading.Thread(target=print_time, args=("thread_1",2))
t2 = threading.Thread(target=print_time, args=("thread_2",4))

t1.start()
t2.start()

t2.join()

# 如果不能让主线程提前结束
# while True:
#     pass

```



线程的结束一般依靠线程函数的自然结束；也可以在线程函数中调用thread.exit()，他抛出SystemExit exception，达到退出线程的目的。

#### 2. 继承threading.Thread

Python通过两个标准库thread和threading提供对线程的支持。thread提供了低级别的、原始的线程以及一个简单的锁。

threading 模块提供的其他方法：

- threading.currentThread(): 返回当前的线程变量。
- threading.enumerate(): 返回一个包含正在运行的线程的list。正在运行指线程启动后、结束前，不包括启动前和终止后的线程。
- threading.activeCount(): 返回正在运行的线程数量，与len(threading.enumerate())有相同的结果。

除了使用方法外，线程模块同样提供了Thread类来处理线程，Thread类提供了以下方法:

- **run():** 用以表示线程活动的方法。

- start():

  启动线程活动。

  

- **join([time]):** 等待至线程中止。这阻塞调用线程直至线程的join() 方法被调用中止-正常退出或者抛出未处理的异常-或者是可选的超时发生。

- **isAlive():** 返回线程是否活动的。

- **getName():** 返回线程名。

- **setName():** 设置线程名。





使用Threading模块创建线程，直接从threading.Thread继承，然后重写__init__方法和run方法：(类似java)

```python
import threading
import time

exitFlag = 0

class myThread (threading.Thread):   #继承父类threading.Thread
    def __init__(self, threadID, name, counter):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.counter = counter
    def run(self):                   #把要执行的代码写到run函数里面 线程在创建后会直接运行run函数
        print ("Starting " + self.name)
        print_time(self.name, self.counter, 5)
        print ("Exiting " + self.name)

def print_time(thread_name, delay, counter):
    while counter:
        if exitFlag:
            (threading.Thread).exit()
        time.sleep(delay)
        print("{} {}".format(thread_name, time.ctime()))
        counter -= 1

# 创建新线程
thread1 = myThread(1, "Thread-1", 1)
thread2 = myThread(2, "Thread-2", 2)

# 开启线程
thread1.start()
thread2.start()

thread1.join()
thread2.join()

print ("Exiting Main Thread")

```





### 锁

```python
import threading
import thread

lock = threading.Lock()
cnt = 0

def proc():
    global cnt

    lock.acquire()	#加锁
    cnt += 1
    lock.release()	#释放


def run_thread(n):
    for i in range(n):
        proc()


t1 = threading.Thread(target=run_thread, args=(10000,), name="thread_1")
t2 = threading.Thread(target=run_thread, args=(10000,), name="thread_2")

t1.start()
t2.start()

t1.join()
t2.join()

print(cnt)
```



### ThreadLocal

```python
import threading

local_storage = threading.local()

def process_stu():
    stu = local_storage.stu
    print("hello, this is {} at {}".format(stu, threading.current_thread().name))

def process_thread(stu):
    local_storage.stu = stu
    process_stu()

t1 = threading.Thread(target=process_thread, args=("Alice",), name="thread_1")
t2 = threading.Thread(target=process_thread, args=("Bob",), name="thread_2")

t1.start()
t2.start()

t1.join()
t2.join()
```



全局变量`local_school`就是一个`ThreadLocal`对象，每个`Thread`对它都可以读写`student`属性，但互不影响。你可以把`local_school`看成全局变量，但每个属性如`local_school.student`都是线程的局部变量，可以任意读写而互不干扰，也不用管理锁的问题，`ThreadLocal`内部会处理。

可以理解为全局变量`local_school`是一个`dict`，不但可以用`local_school.student`，还可以绑定其他变量，如`local_school.teacher`等等。

`ThreadLocal`最常用的地方就是为每个线程绑定一个数据库连接，HTTP请求，用户身份信息等，这样一个线程的所有调用到的处理函数都可以非常方便地访问这些资源。





### *问题

如果主线程提前退出，则开启的多线程中无法使用import的包（time.ctime()报错为：NoneType）



因为Python的线程虽然是真正的线程，但解释器执行代码时，有一个GIL锁：Global Interpreter Lock，任何Python线程执行前，必须先获得GIL锁，然后，每执行100条字节码，解释器就自动释放GIL锁，让别的线程有机会执行。这个GIL全局锁实际上把所有线程的执行代码都给上了锁，所以，多线程在Python中只能交替执行，即使100个线程跑在100核CPU上，也只能用到1个核。

GIL是Python解释器设计的历史遗留问题，通常用的解释器是官方实现的CPython，要真正利用多核，除非重写一个不带GIL的解释器。



## I/O

### 文件I/O

当文件被打开后，获取到file对象

file对象的属性

| file.closed    | 返回true如果文件已被关闭，否则返回false。                    |
| -------------- | ------------------------------------------------------------ |
| file.mode      | 返回被打开文件的访问模式。                                   |
| file.name      | 返回文件的名称。                                             |
| file.softspace | 如果用print输出后，必须跟一个空格符，则返回false。否则返回true。 |







读

```python

with open("/Users/lixiuwen/code/py/service-tree-stats/service_data.csv", "r") as f:
    for line in f.readlines():
        print()
    
```



read(size)指定每次读的字节数



写：



以`'w'`模式写入文件时，如果文件已存在，会直接覆盖（相当于删掉后新写入一个文件）。如果我们希望追加到文件末尾怎么办？可以传入`'a'`以追加（append）模式写入。



### BytesIO & StringIO

```python
from io import StringIO
from io import BytesIO


def test_string():
    f = BytesIO("xxx")
    f.write('hello ')   #覆盖原来的字符流
    f.write('lixiuwen')
    print(f.getvalue())

    f.seek(0)   #需要重新定位
    res = f.readline()
    print(res)


test_string()

```



seek（offset [,from]）方法改变当前文件的位置。Offset变量表示要移动的字节数。From变量指定开始移动字节的参考位置。

如果from被设为0，这意味着将文件的开头作为移动字节的参考位置。如果设为1，则使用当前的位置作为参考位置。如果它被设为2，那么该文件的末尾将作为参考位置。







### 序列化

json.dumps 用于将 Python 对象编码成 JSON 字符串；json.loads将已编码的 JSON 字符串解码为 Python 对象

Python语言特定的序列化模块是`pickle`，但如果要把序列化搞得更通用、更符合Web标准，就可以使用`json`模块。

#### pickle

可以自由序列化对象

```python
import pickle


d = dict(name="xwl", age = 21, score = 90)

string = pickle.dumps(d)
#也可以将序列化后的结果io
#f = open('dump.txt', 'wb')
#pickle.dump(d,f)

obj = pickle.loads(string)

print(obj)
```







## 库

### time

获取当前时间

```python
from datetime import datetime
now = datetime.now() # 获取当前datetime

#获取指定日期
dt = datetime(2019,9,9,12,55,55)

#获取timestamp
ptr = dt.timestamp()
print(ptr)

#timestamp -> datetime
print(datetime.fromtimestamp(ptr+100))		#本地时间

print(datetime.utcfromtimestamp(ptr+100)) 	# UTC时间


#str转datetime
cday = datetime.strptime('2015-6-1 18:19:59', '%Y-%m-%d %H:%M:%S')






```





### datetime

做差

```python
from datetime import datetime
import time
dt1 = datetime.now()

time.sleep(5)

dt2 = datetime.now()

res = dt2 - dt1

print res




```

和time转换

```python
import datetime
import time

# 日期时间字符串
st = "2017-11-23 16:10:10"
# 当前日期时间
dt = datetime.datetime.now()
# 当前时间戳
sp = time.time()

# 1.把datetime转成字符串
def datetime_toString(dt):
    print("1.把datetime转成字符串: ", dt.strftime("%Y-%m-%d %H:%M:%S"))


# 2.把字符串转成datetime
def string_toDatetime(st):
    print("2.把字符串转成datetime: ", datetime.datetime.strptime(st, "%Y-%m-%d %H:%M:%S"))


# 3.把字符串转成时间戳形式
def string_toTimestamp(st):
    print("3.把字符串转成时间戳形式:", time.mktime(time.strptime(st, "%Y-%m-%d %H:%M:%S")))


# 4.把时间戳转成字符串形式
def timestamp_toString(sp):
    print("4.把时间戳转成字符串形式: ", time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(sp)))


# 5.把datetime类型转外时间戳形式
def datetime_toTimestamp(dt):
    print("5.把datetime类型转外时间戳形式:", time.mktime(dt.timetuple()))


```







### logging

```python
import logging

logging.basicConfig(
        level=logging.INFO,		#设置提示的
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

```





### yapf格式化代码

```bash
find . -name '*.py' -print0 | xargs -0 yapf -i		#对当前目录的python代码格式化
```



