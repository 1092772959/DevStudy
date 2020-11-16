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

### Nosql

Not only sql, 非关系型数据库。nosql也分为以下几类：

#### kv数据库

涉及一个哈希表，表中有特定的键和一个指针指向特定的数据。

> redis, Voldemort

优：快速查询

劣：数据缺少结构化

#### 列存储数据库

通常用于分布式存储海量数据。一个键指向多个列，这些列由列家族安排。

> Cassandra, HBase

以列簇式存储，将同一列数据存在一起。

优：查找速度快、分布式扩展

劣：功能有局限

#### 文档型数据库

与kv数据库类似，数据模型为版本化的文档，半结构化的文档以特定方式存储，例如JSON。允许之间嵌套键值。

> Mongodb

优：数据结构要求不严格

劣：查询性能不佳、缺乏统一的查询语法



### Redis特点

#### 优

- 高性能：读速度110000次/s, 写81000次/s。 不同于Mysql，不会使用lock，因此效率非常高
- 丰富的数据类型
- 原子性：所有操作都是原子性，多个操作支持事务，通过MULTI和EXEC指令包起来。
- 丰富的特性：publish/subscribe，通知、key过期等特性
- 分布式

#### 劣

- 持久化：使用两种方式持久化数据 
  - snapshot：每次写全部数据，开销较大
  - aof：基于语句追加，但追加的log可能过大，同时所有操作均重新执行一遍，回复速度慢
- 耗内存



#### 使用场景

- 缓存
- 排行榜：使用有序集合(zset)数据类型实现复杂排序
- 计数器：incr命令
- 分布式会话：存储session信息，不在单机中存储
- 分布式锁：高并发（秒杀）
- 社交网络：点赞、关注等操作，由于访问量巨大，不适合用sql存储
- 最新列表：LPUSH
- 异步通信：发布/订阅和阻塞队列，可当作mq使用



### 安装

https://note.youdao.com/ynoteshare1/index.html?id=bfcc478547c920926146675e678e4a1f&type=note

#### 配置

```
redis.conf 配置文件详解
redis.conf 配置项说明如下：
1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程
    daemonize no
2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定
    pidfile /var/run/redis.pid
3. 指定Redis监听端口，默认端口为6379，为什么选用6379作为默认端口，因为6379在手机按键上MERZ对应的号码，而MERZ取自意大利歌女Alessia Merz的名字
    port 6379
4. 绑定的主机地址
    bind 127.0.0.1
5.当 客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
    timeout 300
6. 指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose
    loglevel verbose
7. 日志记录方式，默认为标准输出，如果配置Redis为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给/dev/null
    logfile stdout
8. 设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id
    databases 16
9. 指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合
    save <seconds> <changes>
    Redis默认配置文件中提供了三个条件：
    save 900 1
    save 300 10
    save 60 10000
    分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改。
 
10. 指定存储至本地数据库时是否压缩数据，默认为yes，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大
    rdbcompression yes
11. 指定本地数据库文件名，默认值为dump.rdb
    dbfilename dump.rdb
12. 指定本地数据库存放目录
    dir ./
13. 设置当本机为slav服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步
    slaveof <masterip> <masterport>
14. 当master服务设置了密码保护时，slav服务连接master的密码
    masterauth <master-password>
15. 设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH <password>命令提供密码，默认关闭
    requirepass foobared
16. 设置同一时间最大客户端连接数，默认无限制，Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis会关闭新的连接并向客户端返回max number of clients reached错误信息
    maxclients 128
17. 指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区
    maxmemory <bytes>
18. 指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为no
    appendonly no
19. 指定更新日志文件名，默认为appendonly.aof
     appendfilename appendonly.aof
20. 指定更新日志条件，共有3个可选值： 
    no：表示等操作系统进行数据缓存同步到磁盘（快） 
    always：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全） 
    everysec：表示每秒同步一次（折中，默认值）
    appendfsync everysec
 
21. 指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）
     vm-enabled no
22. 虚拟内存文件路径，默认值为/tmp/redis.swap，不可多个Redis实例共享
     vm-swap-file /tmp/redis.swap
23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0
     vm-max-memory 0
24. Redis swap文件分成了很多的page，一个对象可以保存在多个page上面，但一个page上不能被多个对象共享，vm-page-size是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page大小最好设置为32或者64bytes；如果存储很大大对象，则可以使用更大的page，如果不 确定，就使用默认值
     vm-page-size 32
25. 设置swap文件中的page数量，由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的，，在磁盘上每8个pages将消耗1byte的内存。
     vm-pages 134217728
26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4
     vm-max-threads 4
27. 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启
    glueoutputbuf yes
28. 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法
    hash-max-zipmap-entries 64
    hash-max-zipmap-value 512
29. 指定是否激活重置哈希，默认为开启（后面在介绍Redis的哈希算法时具体介绍）
    activerehashing yes
30. 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件
    include /path/to/local.conf
```



#### docker安装

```
> docker search redis							#查询镜像

> docker pull redis:latest				#获取镜像

> docker run -d --name redis-test -p 6379:6379 redis 		#创建并启动容器

> docker exec -it $CONTAINER /bin/bash 	#进入容器

$ redis-cli		#使用客户端工具连接数据库
```



自定义配置启动redis

```
1. 获取redis.conf 配置文件
> sudo mkdir -p /usr/local/docker/redis
> sudp cp redis.conf /usr/local/docker/redis

2. 根据需求修改conf中的值，例如绑定端口、是否使用密码

3. 创建容器
> docker run -p 6379:6379 --name redis-test -v /usr/local/docker/redis/redis.conf:/etc/redis/redis.conf -v /usr/local/docker/redis/data:/data -d redis redis-server
```





### Redis内存维护策略

即使采用集群部署来动态扩容，也应及时地整理内存，维持性能。

两种策略：

#### 设置超时时间

```
expire key time		#second unit, for normal case
setex(String key, int seconds, String value)	# unique for string type
```

> - til key 查看该key当前剩余的有效时间 
> - 如果没有设置过期时间（>ttl key 得到-1）,则永久有效
> - persist key使之永久有效

#### 过期策略

采用**定期删除+惰性删除策略**

定期：redis默认每个100ms检查，是否有过期的key,有过期key则删除。需要说明的是，redis不是每个100ms将所有的key检查一次，而是随机抽取进行检查(如果每隔100ms,全部key进行检查，redis岂不是卡死)。因此，如果只采用定期删除策略，会导致很多key到时间没有删除。

惰性：在你获取某个key的时候，redis会检查一下，这个key如果设置了过期时间那么是否过期了？如果过期了此时就会删除。



这样就没有问题了吗？非。

如果一个key，定期删除没删除，但又没及时去请求key，内存占用会越来越高，则应该采用内存淘汰机制。

#### 内存淘汰机制

>LRU: 内存管理的一种页面置换算法，核心思想“如果数据最近被访问过，那么将来被访问的几率也更高”。

1. volatile-lru: 设定超时时间的数据中，删除最不常用的数据
2. allkeys-lru: 查询所有key，删除最近最不常使用的数据进行删除，应用**最广**
3. volatile-lfu: 从所有配置了过期时间的健中**删除使用频率最少**的键
4. allkeys-lfu: 从所有键中做lfu
5. random：随机删除
6. volatile-ttl: 查询所有设置了过期时间的键，将其排序，并删除即将过期的键
7. noeviction: 不会删除，内存溢出则返回报错



## 二、基础和应用

### 1. 数据结构

指令查询：http://redisdoc.com/index.html

string, list, hash, set, zset(有序集合)

![img](https://pics6.baidu.com/feed/023b5bb5c9ea15ce598be46bdbb071f53b87b2a5.jpeg?token=3f5ccd21917fb33a2472bbb06b480a56&s=5AA834629B8961495EFDB0C70000E0B1)



redisObject 最主要的信息如上图所示：type 表示一个 value 对象具体是何种数据类型，encoding 是不同数据类型在 Redis 内部的存储方式。

比如：type=string 表示 value 存储的是一个普通字符串，那么 encoding 可以是 raw 或者 int。



#### 1) string

内部表示为动态字符数组。Redis所有数据结构都以唯一的key字符串作为名称，并获取相应的value值。其值不仅能是字符串，还能是数字或其他很多类型。由于string类型为二进制安全的，可以存储如序列化后的图片二进制。

扩容：当字符串长度小于1MB时，扩容都是加倍现有的空间；如果字符串长度超过1MB，一次扩容只会多扩1MB。字符串**最大长度为512MB**。



当value是一个整数，还可以对它进行自增操作。

```bash
set age 30

incr age			#31

increby age 5		#36

increby age -5 		#31

setnx $KEY VALUE	#if exists, do nothing and return -1; otherwise set the value
#分布式锁的解决方案之一

setrange string range value

getrange string start end		#负数偏移量表示从字符串的末尾开始计数， -1 表示最后一个字符， -2 表示倒数第二个字符， 以此类推

strlen KEY

```

*注意：自增的范围在**signed long**的最大值和最小值之间。



应用场景：

1. 保存单个字符串或json字符串
2. 由于是二进制安全的，可以存放序列化后的文件内容
3. 计数器

> 由于incr, decr等指令本身具有原子操作的特性，因此可以此实现计数功能。三个客户端对一个值+1，最后这个值一定加了3



#### 2) list

相当于**java中的LinkedList**，是链表，因此插入和删除很快，为O(1)时间复杂度；索引定位慢，O(n)时间复杂度。

每个节点都是用双向指针，支持向前和向后遍历。

redis的列表结构常用来做**异步队列**使用。将需要延后处理的任务结构体序列化成字符串，塞进redis的列表，另一个线程从这个列表中轮询数据进行处理。

```bash
rpush books python java golang

lindex books 1		#O(n)

lrange books 0 -1 	#获取所有元素

ltrim books 1 -1	#保留[1,end] 两端都是闭区间

ltrim books 1 0		#清空列表 

lpop books

rpop books

rpoplpush src dest	
#将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端；将 source 弹出的元素插入到列表 destination ，作为 destination 列表的的头元素。如果 source 不存在，值 nil 被返回，并且不执行其他动作。若destination不存在则会创建一个新的dest

rpoplpush src src 	#循环列表

linsert key BEFORE|AFTER pivot value #在值pivot之前|之后插入 
#当 pivot 不存在于列表 key 时，不执行任何操作; 当 key 不存在时， key 被视为空列表，不执行任何操作。

BLPOP key [key …] timeout 	#lpop的阻塞版，当给定列表内没有任何元素可供弹出的时候，连接将被 BLPOP 命令阻塞，直到等待超时或发现可弹出元素为止。当给定多个 key 参数时，按参数 key 的先后顺序依次检查各个列表，弹出第一个非空列表的头元素。
#details: http://redisdoc.com/list/blpop.html

brpoplpush src dest 		#阻塞版，当给定列表 src 不为空时， BRPOPLPUSH 的表现和 RPOPLPUSH  一样。

```

##### 快速列表

Redis底层存储不是一个简单的linkedList，而是一个quickList。在列表元素比较少的情况下，会使用一块连续的内存存储，这个结构是ziplist（压缩列表）。它将所有的元素紧挨着一起存储在一块连续的内存。当数据量比较多时会变成quickList，不会像普通链表出现太多空间的空间冗余。

##### 应用场景

1. 对数据量大的集合数据删减

关注列表、粉丝列表、留言评论、分页、热点新闻。LRANGE可以实现分页。每篇文章的评论存在一个单独的list中。

2. 任务队列

> RPOPLPUSH src dest
>
> 可以保证先后顺序



#### 3) hash

数组+链表的二维结构。适合存储对象。

和Java Hashmap区别：Redis的字典的key值只能是字符串，并且rehash方式不一样。

当字典很大时，rehash是个耗时操作，Redis为了追求高性能，不能堵塞服务，所以采用了渐进式rehash策略。它会在rehash的同时保留新旧两个hash结构。查询时会同时查询两个hash结构，然后在后续的定时任务以及hash操作指令中，循序渐进地将旧hash的内容一点点迁移到新的hash结构中。搬迁结束后用新的hash结构取代。

和字符串结构不同：

1）字符串需要一次性全部序列化整个对象，hash可以对用户结构中的每个字段单独存储。需要时进行部分获取。

2）hash存储消耗高于字符串。

```
hset key field value

hget key filed

hlen key 	#字段个数

hstrlen	key field	#字符串长度

hmset key filed value [field value...]

hmget key field1 field2...

hgetall key		#返回全部域和值

hsetnx key filed value	#不存在时修改值

hexists key filed
```



应用场景：存储对象

为什么不用string存储对象

>Hash是最接近关系型数据库结构的数据类型，可以将数据库一条记录或程序中一个对象转化成hashmap存储到redis中。
>
>1. 使用string存储，增加了序列化/反序列化的开销，并且修改或查询某一个字段的内容时需要将整个对象取出，并且修改操作需要对并发进行保护，引入CAS等复杂问题。
>
>2. 使用hash存储，使用id作为key，但是id存储了两次。如果存在大量这样的数据，产生内存浪费。



#### 4) set

相当于一个特殊的字典，所有的value都是null。string类型的无序集合，自动去重。

```
sdd、spop、smembers、sunion等命令
```



#### 5) zset

有序列表。类似于SortedSet和HashMap的结合体。

赋予每个value一个score，代表这个value的排序权重。zset的底层实现是用“跳跃表”实现的。

zset可以用来存储粉丝列表，value值是粉丝的用户ID，score是关注时间。可以按照粉丝关注事件进行排序。

zset可以用来存储学生的成绩，value值是学生的ID，score是考试成绩，按照成绩排序。

##### 跳跃表

https://www.jianshu.com/p/61f8cad04177

https://www.cnblogs.com/hunternet/p/11248192.html

java实现

![Redis跳跃表](https://hunter-image.oss-cn-beijing.aliyuncs.com/redis/skiplist/Redis%E8%B7%B3%E8%B7%83%E8%A1%A8.png)

```java

import java.util.Random;
 
public class SkipList<T extends Comparable<? super T>> {
    private int maxLevel;
    private SkipListNode<T>[] root;
    private int[] powers;
    private Random rd = new Random();
    SkipList() {
        this(4);
    }
    SkipList(int i) {
        maxLevel = i;
        root = new SkipListNode[maxLevel];
        powers = new int[maxLevel];
        for (int j = 0; j < maxLevel; j++)
            root[j] = null;
        choosePowers();
    }
    public boolean isEmpty() {
        return root[0] == null;
    }
    public void choosePowers() {
        powers[maxLevel-1] = (2 << (maxLevel-1)) - 1;    // 2^maxLevel - 1
        for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++)
           powers[i] = powers[i+1] - (2 << j);           // 2^(j+1)
    }
    public int chooseLevel() {
        int i, r = Math.abs(rd.nextInt()) % powers[maxLevel-1] + 1;
        for (i = 1; i < maxLevel; i++)
            if (r < powers[i])
                return i-1; // return a level < the highest level;
        return i-1;         // return the highest level;
    }
  // make sure (with isEmpty()) that search() is called for a nonempty list;
    public T search(T key) { 
        int lvl;
        SkipListNode<T> prev, curr;            // find the highest nonnull
        for (lvl = maxLevel-1; lvl >= 0 && root[lvl] == null; lvl--); // level;
        prev = curr = root[lvl];
        while (true) {
            if (key.equals(curr.key))          // success if equal;
                 return curr.key;
            else if (key.compareTo(curr.key) < 0) { // if smaller, go down,
                 if (lvl == 0)                 // if possible
                      return null;      
                 else if (curr == root[lvl])   // by one level
                      curr = root[--lvl];      // starting from the
                 else curr = prev.next[--lvl]; // predecessor which
            }                                  // can be the root;
            else {                             // if greater,
                 prev = curr;                  // go to the next
                 if (curr.next[lvl] != null)   // non-null node
                      curr = curr.next[lvl];   // on the same level
                 else {                        // or to a list on a lower level;
                      for (lvl--; lvl >= 0 && curr.next[lvl] == null; lvl--);
                      if (lvl >= 0)
                           curr = curr.next[lvl];
                      else return null;
                 }
            }
        }
    }
  public void insert(T key) {
        SkipListNode<T>[] curr = new SkipListNode[maxLevel];
        SkipListNode<T>[] prev = new SkipListNode[maxLevel];
        SkipListNode<T> newNode;
        int lvl, i;
        curr[maxLevel-1] = root[maxLevel-1];
        prev[maxLevel-1] = null;
        for (lvl = maxLevel - 1; lvl >= 0; lvl--) {
            while (curr[lvl] != null && curr[lvl].key.compareTo(key) < 0) { 
                prev[lvl] = curr[lvl];           // go to the next
                curr[lvl] = curr[lvl].next[lvl]; // if smaller;
            }
            if (curr[lvl] != null && key.equals(curr[lvl].key)) // don't 
                return;                          // include duplicates;
            if (lvl > 0)                         // go one level down
                if (prev[lvl] == null) {         // if not the lowest
                      curr[lvl-1] = root[lvl-1]; // level, using a link
                      prev[lvl-1] = null;        // either from the root
                }
                else {                           // or from the predecessor;
                     curr[lvl-1] = prev[lvl].next[lvl-1];
                     prev[lvl-1] = prev[lvl];
                }
        }
        lvl = chooseLevel();                // generate randomly level 
        newNode = new SkipListNode<T>(key,lvl+1); // for newNode;
        for (i = 0; i <= lvl; i++) {        // initialize next fields of
            newNode.next[i] = curr[i];      // newNode and reset to newNode
            if (prev[i] == null)            // either fields of the root
                 root[i] = newNode;         // or next fields of newNode's
            else prev[i].next[i] = newNode; // predecessors;
        }
    }
}

```





### 常用指令

#### key管理

```redis
keys *

exists $KEY

ttl $KEY		#查看键剩余时间

pttl $KEY		#以毫秒为单位

expire $KEY 20	#seconds

pexpire $KEY 20		#milliseconds

persist $KEY		#取消过期时间

del key [key...]		#删除，可以操作多个

rename $KEY $NEW_KEY		#对key重命名

type $KEY		#查看key类型
```



key命名建议

1. 不要太长，不超过1024字节
2. 名称区分大小写
3. 命名统一



## 三、senior

### 持久化机制

Redis是一个支持持久化的内存数据库，通过持久化机制把内存中的数据同步到硬盘文件来保证数据持久化。当Redis重启后通过把硬盘文件重新加载到内存，就能达到恢复数据的目的。
实现：单独创建fork()一个子进程，将当前父进程的数据库数据复制到子进程的内存中，然后由子进程写入到临时文件中，持久化的过程结束了，再用这个临时文件替换上次的快照文件，然后子进程退出，内存释放。

**RDB**是Redis默认的持久化方式。按照一定的时间周期策略把内存的数据以快照的形式保存到硬盘的二进制文件。即Snapshot快照存储，对应产生的数据文件为dump.rdb，通过配置文件中的save参数来定义快照的周期。（ 快照可以是其所表示的数据的一个副本，也可以是数据的一个复制品。）

需要注意的是，每次快照持久化都会将主进程的数据库数据复制一遍，导致内存开销加倍，若此时内存不足，则会阻塞服务器运行，直到复制结束释放内存；都会将内存数据完整写入磁盘一次，所以如果数据量大的话，而且写操作频繁，必然会引起大量的磁盘I/O操作，严重影响性能，并且最后一次持久化后的数据可能会丢失。

**AOF**：Redis会将每一个收到的写命令都通过Write函数追加到文件最后，类似于MySQL的binlog。当Redis重启是会通过重新执行文件中保存的写命令来在内存中重建整个数据库的内容。主要有两种方式触发：有写操作就写、每秒定时写（也会丢数据）。

当两种方式同时开启时，数据恢复Redis会优先选择AOF恢复。

**比较**：

1、aof文件比rdb更新频率高，优先使用aof还原数据。

2、aof比rdb更安全也更大

3、rdb性能比aof好

4、如果两个都配了优先加载AOF

**存储结构**

redis通讯协议（RESP）格式的命令文本存储。

#### RESP

RESP 是redis客户端和服务端之前使用的一种通讯协议；

For Simple Strings the first byte of the reply is "+" 回复

For Errors the first byte of the reply is "-" 错误

For Integers the first byte of the reply is ":" 整数

For Bulk Strings the first byte of the reply is "$" 字符串

For Arrays the first byte of the reply is "*" 数组

### 缓存

#### 缓存雪崩

由于原有缓存失效，新缓存未到期间

(例如：我们设置缓存时采用了相同的过期时间，在同一时刻出现大面积的缓存过期)，所有原本应该访问缓存的请求都去查询数据库了，而对数据库CPU和内存造成巨大压力，严重的会造成数据库宕机。从而形成一系列连锁反应，造成整个系统崩溃。
**解决办法**
大多数系统设计者考虑用加锁（ 最多的解决方案）或者队列的方式保证来保证不会有大量的线程对数据库一次性进行读写，从而避免失效时大量的并发请求落到底层存储系统上。

还有一个简单方案就时讲缓存失效时间分散开。

#### 缓存穿透

缓存穿透是指用户查询数据，在数据库没有，自然在缓存中也不会有。这样就导致用户查询的时候，在缓存中找不到，每次都要去数据库再查询一遍，然后返回空（相当于进行了两次无用的查询）。这样请求就绕过缓存直接查数据库，这也是经常提的缓存命中率问题。
**解决办法**
最常见的则是采用**布隆过滤器**，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被这个bitmap拦截掉，从而避免了对底层存储系统的查询压力。
另外也有一个更为**简单粗暴的方法**，如果一个查询返回的数据为空（不管是数据不存在，还是系统故障），我们仍然把这个空结果进行缓存，但它的过期时间会很短，最长不超过五分钟。通过这个直接设置的默认值存放到缓存，这样第二次到缓冲中获取就有值了，而不会继续访问数据库，这种办法最简单粗暴。
5TB的硬盘上放满了数据，请写一个算法将这些数据进行排重。如果这些数据是一些32bit大小的数据该如何解决？如果是64bit的呢？

对于空间的利用到达了一种极致，那就是Bitmap和布隆过滤器(Bloom Filter)。
Bitmap： 典型的就是哈希表
缺点是，Bitmap对于每个元素只能记录1bit信息，如果还想完成额外的功能，恐怕只能靠牺牲更多的空间、时间来完成了。

#### 缓存击穿

指一个key非常热点，大并发集中对这个key进行访问，当这个key在失效的瞬间，仍然持续的大并发访问就穿破缓存，转而直接请求数据库。
**解决方案**

在访问key之前，采用SETNX（set if not exists）来设置另一个短期key来锁住当前key的访问，访问结束再删除该短期key。

#### 缓存预热

缓存预热这个应该是一个比较常见的概念，相信很多小伙伴都应该可以很容易的理解，缓存预热就是系统上线后，将相关的缓存数据直接加载到缓存系统。这样就可以避免在用户请求的时候，先查询数据库，然后再将数据缓存的问题！用户直接查询事先被预热的缓存数据！
解决思路：
1、直接写个缓存刷新页面，上线时手工操作下；
2、数据量不大，可以在项目启动的时候自动进行加载；
3、定时刷新缓存；

#### 缓存更新

除了缓存服务器自带的缓存失效策略之外（Redis默认的有6中策略可供选择），我们还可以根据具体的业务需求进行自定义的缓存淘汰，常见的策略有两种：
（1）定时去清理过期的缓存；
（2）当有用户请求过来时，再判断这个请求所用到的缓存是否过期，过期的话就去底层系统得到新数据并更新缓存。
两者各有优劣，第一种的缺点是维护大量缓存的key是比较麻烦的，第二种的缺点就是每次用户请求过来都要判断缓存失效，逻辑相对比较复杂！具体用哪种方案，大家可以根据自己的应用场景来权衡。

#### **缓存降级**

当访问量剧增、服务出现问题（如响应时间慢或不响应）或非核心服务影响到核心流程的性能时，仍然需要保证服务还是可用的，即使是有损服务。系统可以根据一些关键数据进行自动降级，也可以配置开关实现人工降级。
降级的最终目的是保证核心服务可用，即使是有损的。而且有些服务是无法降级的（如加入购物车、结算）。
以参考日志级别设置预案：
（1）一般：比如有些服务偶尔因为网络抖动或者服务正在上线而超时，可以自动降级；
（2）警告：有些服务在一段时间内成功率有波动（如在95~100%之间），可以自动降级或人工降级，并发送告警；
（3）错误：比如可用率低于90%，或者数据库连接池被打爆了，或者访问量突然猛增到系统能承受的最大阀值，此时可以根据情况自动降级或者人工降级；
（4）严重错误：比如因为特殊原因数据错误了，此时需要紧急人工降级。

服务降级的目的，是为了防止Redis服务故障，导致数据库跟着一起发生雪崩问题。因此，对于不重要的缓存数据，可以采取服务降级策略，例如一个比较常见的做法就是，Redis出现问题，不去数据库查询，而是直接返回默认值给用户。

### 布隆过滤器

就是引入了k(k>1)k(k>1)个相互独立的哈希函数，保证在给定的空间、误判率下，完成元素判重的过程。
它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率和删除困难。
Bloom-Filter算法的**核心思想**就是利用多个不同的Hash函数来解决“冲突”。
Hash存在一个冲突（碰撞）的问题，用同一个Hash得到的两个URL的值有可能相同。为了减少冲突，我们可以多引入几个Hash，如果通过其中的一个Hash值我们得出某元素不在集合中，那么该元素肯定不在集合中。只有在所有的Hash函数告诉我们该元素在集合中时，才能确定该元素存在于集合中。这便是Bloom-Filter的基本思想。
Bloom-Filter一般用于在大数据量的集合中判定某元素是否存在。



### 线程模型

#### 单线程

因为Redis是基于内存的操作，CPU不是Redis的瓶颈，Redis的瓶颈最有可能是机器内存的大小或者网络带宽。既然单线程容易实现，而且CPU不会成为瓶颈，那就顺理成章地采用单线程的方案了（毕竟采用多线程会有很多麻烦！）Redis利用队列技术将并发访问变为串行访问
1）绝大部分请求是纯粹的内存操作（非常快速）

2）采用单线程,避免了不必要的上下文切换和竞争条件

3）非阻塞IO优点：

1. 速度快，因为数据存在内存中，类似于HashMap，HashMap的优势就是查找和操作的时间复杂度都是O(1)

2. 支持丰富数据类型，支持string，list，set，sorted set，hash
3. 支持事务，操作都是原子性，所谓的原子性就是对数据的更改要么全部执行，要么全部不执行
4. 丰富的特性：可用于缓存，消息，按key设置过期时间，过期后将会自动删除如何解决redis的并发竞争key问题。



同时有多个子系统去set一个key。这个时候要注意什么呢？ 

不推荐使用redis的事务机制。因为我们的生产环境，基本都是redis集群环境，做了数据分片操作。你一个事务中有涉及到多个key操作的时候，这多个key不一定都存储在同一个redis-server上。因此，redis的事务机制，十分鸡肋。
(1)如果对这个key操作，不要求顺序： 准备一个分布式锁，大家去抢锁，抢到锁就做set操作即可
(2)如果对这个key操作，要求顺序： 分布式锁+时间戳。 假设这会系统B先抢到锁，将key1设置为{valueB 3:05}。接下来系统A抢到锁，发现自己的valueA的时间戳早于缓存中的时间戳，那就不做set操作了。以此类推。
(3) 利用队列，将set方法变成串行访问也可以redis遇到高并发，如果保证读写key的一致性
对redis的操作都是具有原子性的,是线程安全的操作,你不用考虑并发问题,redis内部已经帮你处理好并发的问题了。



文件事件处理器包括分别是**套接字、 I/O 多路复用程序、 文件事件分派器（dispatcher）、 以及事件处理器**。使用 I/O 多路复用程序来同时监听多个套接字， 并根据套接字目前执行的任务来为套接字关联不同的事件处理器。当被监听的套接字准备好执行连接应答（accept）、读取（read）、写入（write）、关闭（close）等操作时， 与操作相对应的文件事件就会产生， 这时文件事件处理器就会调用套接字之前关联好的事件处理器来处理这些事件。
I/O 多路复用程序负责监听多个套接字， 并向文件事件分派器传送那些产生了事件的套接字。
**工作原理：**
1)I/O 多路复用程序负责监听多个套接字， 并向文件事件分派器传送那些产生了事件的套接字。
尽管多个文件事件可能会并发地出现， 但 I/O 多路复用程序总是会将所有产生事件的套接字都入队到一个队列里面， 然后通过这个队列， 以有序（sequentially）、同步（synchronously）、每次一个套接字的方式向文件事件分派器传送套接字： 当上一个套接字产生的事件被处理完毕之后（该套接字为事件所关联的事件处理器执行完毕）， I/O 多路复用程序才会继续向文件事件分派器传送下一个套接字。如果一个套接字又可读又可写的话， 那么服务器将先读套接字， 后写套接字.

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190429094050254.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0J1dHRlcmZseV9yZXN0aW5n,size_16,color_FFFFFF,t_70)

其它开源软件采用的模型
Nginx：多进程单线程模型
Memcached：单进程多线程模型



### redis架构

#### 集群

1.twemproxy，大概概念是，它类似于一个代理方式， 使用时在本需要连接 redis 的地方改为连接 twemproxy， 它会以一个代理的身份接收请求并使用一致性 hash 算法，将请求转接到具体 redis，将结果再返回 twemproxy。
缺点： twemproxy 自身单端口实例的压力，使用一致性 hash 后，对 redis 节点数量改变时候的计算值的改变，数据无法自动移动到新的节点。

![img](https://img2018.cnblogs.com/blog/1481291/201809/1481291-20180925142206124-913246424.png)

2.codis，目前用的最多的集群方案，基本和 twemproxy 一致的效果，但它支持在 节点数量改变情况下，旧节点数据可恢复到新 hash 节点

3.redis cluster3.0 自带的集群，特点在于他的分布式算法不是一致性 hash，而是 hash 槽的概念，以及自身支持节点设置从节点。具体看官方文档介绍。

#### 主从

![img](https://img2018.cnblogs.com/blog/1481291/201809/1481291-20180925142118041-1727225479.png)



工作原理：Slave从节点服务启动并连接到Master之后，它将主动发送一个SYNC命令。Master服务主节点收到同步命令后将启动后台存盘进程，同时收集所有接收到的用于修改数据集的命令，在后台进程执行完毕后，Master将传送整个数据库文件到Slave，以完成一次完全同步。而Slave从节点服务在接收到数据库文件数据之后将其存盘并加载到内存中。此后，Master主节点继续将所有已经收集到的修改命令，和新的修改命令依次传送给Slaves，Slave将在本次执行这些数据修改命令，从而达到最终的数据同步。

**主从同步**：全量同步和增量同步。只有从机第一次连接上主机是全量同步。



#### 哨兵

Redis提供了哨兵的命令，哨兵是一个独立的进程，作为进程，它会独立运行。其原理是**哨兵通过发送命令，等待Redis服务器响应，从而监控运行的多个Redis实例。**

这里的哨兵有两个作用

- 通过发送命令，让Redis服务器返回监控其运行状态，包括主服务器和从服务器。
- 当哨兵监测到master宕机，会自动将slave切换成master，然后通过**发布订阅模式**通知其他的从服务器，修改配置文件，让它们切换主机。

然而一个哨兵进程对Redis服务器进行监控，可能会出现问题，为此，我们可以使用多个哨兵进行监控。各个哨兵之间还会进行监控，这样就形成了多哨兵模式。

用文字描述一下**故障切换（failover）**的过程。假设主服务器宕机，哨兵1先检测到这个结果，系统并不会马上进行failover过程，仅仅是哨兵1主观的认为主服务器不可用，这个现象成为**主观下线**。当后面的哨兵也检测到主服务器不可用，并且数量达到一定值时，那么哨兵之间就会进行一次投票，投票的结果由一个哨兵发起，进行failover操作。切换成功后，就会通过发布订阅模式，让各个哨兵把自己监控的从服务器实现切换主机，这个过程称为**客观下线**。这样对于客户端而言，一切都是透明的。





### 事务

Redis事务功能是通过MULTI、EXEC、DISCARD和WATCH 四个原语实现的
Redis会将一个事务中的所有命令序列化，然后按顺序执行。
1.redis 不支持回滚“Redis 在事务失败时不进行回滚，而是继续执行余下的命令”， 所以 Redis 的内部可以保持简单且快速。
2.如果在一个事务中的**命令**出现错误，那么**所有的命令**都不会执行；
3.如果在一个事务中出现**运行错误**，那么**正确的命令**会被执行。
注：redis的discard只是结束本次事务,正确命令造成的影响仍然存在.

1）MULTI命令用于开启一个事务，它总是返回OK。 MULTI执行之后，客户端可以继续向服务器发送任意多条命令，这些命令不会立即被执行，而是被放到一个队列中，当EXEC命令被调用时，所有队列中的命令才会被执行。
2）EXEC：执行所有事务块内的命令。返回事务块内所有命令的返回值，按命令执行的先后顺序排列。 当操作被打断时，返回空值 nil 。
3）通过调用DISCARD，客户端可以清空事务队列，并放弃执行事务， 并且客户端会从事务状态中退出。
4）WATCH 命令可以为 Redis 事务提供 check-and-set （CAS）行为。 可以监控一个或多个键，一旦其中有一个键被修改（或删除），之后的事务就不会执行，监控一直持续到EXEC命令。



#### 原子性

对于Redis而言，命令的原子性指的是：一个操作的不可以再分，操作要么执行，要么不执行。
Redis的操作之所以是原子性的，是因为Redis是单线程的。
Redis本身提供的所有API都是原子操作，Redis中的事务其实是要保证批量操作的原子性。
多个命令在并发中也是原子性的吗？
不一定， 将get和set改成单命令操作，incr 。使用Redis的事务，或者使用Redis+Lua==的方式实现.

### 

### 分布式锁

Redis为单进程单线程模式，采用队列模式将并发访问变成串行访问，且多客户端对Redis的连接并不存在竞争关系Redis中可以使用SETNX命令实现分布式锁。
将 key 的值设为 value ，当且仅当 key 不存在。 若给定的 key 已经存在，则 SETNX 不做任何动作

解锁：使用 del key 命令就能释放锁

解决死锁：
1）通过Redis中expire()给锁设定最大持有时间，如果超过，则Redis来帮我们释放锁。
2） 使用 setnx key “当前系统时间+锁持有的时间”和getset key “当前系统时间+锁持有的时间”组合的命令就可以实现。



### 限流

#### setnx key

把当前10位（秒级）时间戳当做key，incr



#### 滑动窗口



#### 令牌桶

