## 一、基础知识

### 本质：

1. KV键值对
2. Cache
3. Persistence

### 3V+3高

大数据时代的3V：

1. 海量Volume
2. 多样Variety
3. 实时Velocity

互联网需求的3高：

1. 高并发
2. 高可扩
3. 高性能

### 常用架构

![](D:\技术\学习笔记\Redis\阿里架构.PNG)

## 二、基础和应用

### 1. 数据结构

string, list, hash, set, zset(有序集合)

#### 1) string

内部表示为动态字符数组。Redis所有数据结构都以唯一的key字符串作为名称，并获取相应的value值。

扩容：当字符串长度小于1MB时，扩容都是加倍现有的空间；如果字符串长度唱过IMB，一次扩容只会多扩1MB。*字符串最大长度为512MB。



当value是一个整数，还可以对它进行自增操作。

```bash
set age 30

incr age			#31

increby age 5		#36

increby age -5 		#31
```

*注意：自增的范围在signed long的最大值和最小值之间。



#### 2) list

相当于java中的LinkedList，是链表，因此插入和删除很快，为O(1)时间复杂度；索引定位慢，O(n)时间复杂度。

每个节点都是用双向指针，支持向前和向后遍历。

redis的列表结构常用来做异步队列使用。将需要延后处理的任务结构体序列化成字符串，塞进redis的列表，另一个线程从这个列表中轮询数据进行处理。

```bash
rpush books python java golang

lindex books 1		#O(n)

lrange books 0 -1 	#获取所有元素

ltrim books 1 -1	#保留[1,end)

ltrim books 1 0		#清空列表
```

##### 快速列表

Redis底层存储不是一个简单的linkedList，而是一个quickList。在列表元素比较少的情况下，会使用一块连续的内存存储，这个结构是ziplist（压缩列表）。它将所有的元素紧挨着一起存储在一块连续的内存。当数据量比较多时会变成quickList，不会像普通链表出现太多空间的空间冗余。

#### 3) hash

数组+链表的二维结构。

和Java Hashmap区别：Redis的字典的key值只能是字符串，并且rehash方式不一样。

当字典很大时，rehash是个耗时操作，Redis为了追求高性能，不能堵塞服务，所以采用了渐进式rehash策略。它会在rehash的同时保留新旧两个hash结构。查询时会同时查询两个hash结构，然后在后续的定时任务以及hash操作指令中，循序渐进地将旧hash的内容一点点迁移到新的hash结构中。搬迁结束后用新的hash结构取代。

和字符串结构不同：

1）字符串需要一次性全部序列化整个对象，hash可以对用户结构中的每个字段单独存储。需要时进行部分获取。

2）hash存储消耗高于字符串。

#### 4) set

相当于一个特殊的字典，所有的value都是null

#### 5) zset

有序列表。类似于SortedSet和HashMap的结合体。

赋予每个value一个score，代表这个value的排序权重。zset的底层实现是用“跳跃列表”实现的。

zset可以用来存储粉丝列表，value值是粉丝的用户ID，score是关注时间。可以按照粉丝关注事件进行排序。

zset可以用来存储学生的成绩，value值是学生的ID，score是考试成绩，按照成绩排序。







