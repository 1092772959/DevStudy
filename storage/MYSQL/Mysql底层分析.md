### 常用指令

终端可使用mycli作为客户端连接数据库；可视化界面可使用navicat，但是要教育注册或者破解版

```mysql
#安装 mycli (for mac)
brew install mycli

```



### 存储引擎

常见存储引擎有Innodb, MyISAM, Memory等。数据在不同存储引擎中存放的格式也是不同的，比如Memory不用磁盘来存储数据，直接存储在内存中。

#### Innodb页结构 

现在互联网公司常用的为Innodb。在Innodb中数据会存储到磁盘上，在真正处理数据时需要先将数据加载到内存中，表中读取某些记录时，不需要一条条把记录从磁盘上读出来，它将数据划分为若干个**页**，以页作为磁盘和内存交互的基本单位，一**页大小为16K**。

页结构：

![WX20200218-160121@2x](./pic/WX20200218-160121@2x.png)

Page Header: 数据页

File Header: 所有类型页公用的头部

**User Records:** 用户记录，插入、删除等行为的记录 

Free Space: 用户记录区的空闲部分，其实也是User Records的一部分

Page Directory: 页中某些记录的相对位置，**多个行记录并为一组，加速查询**；由于是顺序排列的，所以可以二分查找，加速。

#### 行格式

Innodb行格式有：Compact, Redundant, Dynamic, Compressed。默认为Dynamic

```mysql
create table my_table (coloumns...) ROW_FORMAT=行格式名称
```

Innodb一行的字段（**除去blob类型**）空间和最多不超过**65535字节**(64KB)，否则不让创建；



##### Compact格式

<img src="./pic/image-20200315163652852.png" alt="image-20200315163652852" style="zoom:50%;" />

包含：变长字段长度列表、NULL值列表、记录头信息、列1数据、列2数据。。。

1. 变长字段长度列表（2字节）

MySql支持一些变长的数据类型，例如varchar、varbinary、text、blob类型。变长数据类型存储的字节是不固定的，所以在存储真实数据时要把这些数据占用的字节数也存起来。Compact格式中把所有变长字段的真实数据占用的字节长度都存放在开头部位，变成一个列表（由于字段是有序的，所以不需要用kv键值对）。

VARCHAR(M)，M代表能存多少个字符。(mysql5.0.3以前是字节，现在是字符)

*没有可变字段时，这个列表可以去掉

2. NULL值列表（1字节）

处理空字段有两种方法：1）用$标志空字段，但是浪费空间；2）使用NULL值列表

Compact会把可以为NULL的列统一管理起来，存一个标记为在NULL值列表中，如果表中没有允许存储NULL的列，则NULL值列表页不存在。

- 二进制为1时，表示该列值为NULL
- 二进制为0时，表示该列值不为NULL

*没有可为NULL的字段时这个列表可以去掉



3. 记录头信息

用于描述的记录头信息，由固定的5个字节（40位二进制）组成。

- delete_mask: 1bit, 标记该条目是否被删除
- min_rec_mask: 1bit, B+树的每层非叶子节点中的最小记录都会添加该标记
- n_owned: 4bit, 表示当前记录拥有的记录数
- next_record: 16bit, 表示下一条记录的相对位置(innodb排序)



4. 隐藏列

- **row_id**: 非必须，6bit, 行ID，唯一标识一条记录；在既没有主键也没有唯一索引时才会存在
- Transaction_id: 必须有 6bit, 事务ID
- roll_pointer: 必须有，7bit，回滚指针 

#### 行溢出数据

```mysql
create table var_size_test(
	c varchar(65535)
) charset=ascii ROW_FORMAT=Compact;

#会报错，每行字段数据（除开blob类型）的和必须<65535个字节
```



当一行数据超过页大小（16kB）时，将会被存到多个页中，这种情况称为行溢出。

此时有两种解决方法：

1. 存部分数据+下一页的地址
2. 只存下一页的地址：一页可以存更多行的记录



**在Compact和Redundant**行格式中，对于占用存储空间大的列，在记录真实数据处只会存储该列的一部分数据，把剩余的数据分散存储到其他几个页中，然后记录的真实数据处用20个字节存指向这些页的地址和这些页中包含数据的字节数。

在Dynamic和Compressed行格式中，它们不会在记录的真实数据处存储一部分数据，而是把所有的数据存储到其他页面中，只在真实数据处存储其他页面的地址。此外，Compressed行格式会采用压缩算法对页面进行压缩。



### 索引

MyISAM引擎，如果插入数据之后不加任何索引，查出来的顺序和插入的顺序保持一致。



Innodb在插入后会自动根据主键排好序。如果没有定义主键，则会看有没有定义唯一索引；如果还没有唯一索引，则会使用额外的**row_id(隐藏列**)

#### 主键索引

方法：页+页目录+数据

排序的好处：顺序查找时可剪枝。

原理：B+树

![](./pic/WX20200218-214134@2x.png)



#### 起始页频繁变动

![WX20200315-173255@2x](./pic/WX20200315-173255@2x.png)

建表的时候开一个页；需要扩展页的时候不会直接开第二页，会先把第一页copy一份再开辟第二页，然后把原先的第一页改成起始页，这样起始页就会一直是一个页；因此就可以把这个起始页**加载到内存**中，加速查找



#### 聚集索引

特点：

1. 按主键值的大小进行记录和页的排序





#### 多列索引

![WX20200315-175707@2x](./pic/WX20200315-175707@2x.png)

index_bcd用b,c,d三列作为索引；红色的部分只存一个主键，之后再用主键索引去查找真实数据。

```mysql
explain select * from my_table where b = 1 and c = 1 and d = 1;	#全值匹配
```

使用explain关键字查看是否用到索引

查找的过程可能是：辅助索引+回表；或全表暴力匹配

决定查询是否使用索引的是条件语句和已建的索引。



##### 最左前缀原则

```mysql
select * from my_table where b = 1;
```

这样的非全值匹配也能用到index_bcd，因为1**可以和多列索引中的关键字进行比较



```mysql
select * from my_table where c = 1; 
```

但是这样的条件语句是不能用index_bcd的，因为\*1\*不可以做比较



如果只给出后缀或者中间的某个字符串，是用不到索引的

```mysql
select * from my_table where b like '%101%';
```



##### 匹配范围值

```mysql
select * from my_table where b > 1 and b < 8;
```

先找到b为1最大的节点，再一路往后遍历，直到遇到b=8的节点。





可能用不到索引，只在查第一个条件的时候肯定用索引，但是第一个查询条件返回的结果c不一定是有序的；此时查询优化器会做出全表查询的判断。

```mysql
select * from my_table where b > 1 and c > 1;
```



用索引

```mysql
select * from my_table where b = 1 and c > 1;
```



##### order by

索引其实已经做了排序的功能，利用最左前缀原则，可以利用索引加速查询

此时按第一个条件从index_bcd检索出来的数据已经是按c,d排好序的了。当然orderby的字段顺序也要和索引的顺序保持一致。如果不利用索引的话，则会将所有数据取到内存，并在内存中进行排序。

```mysql
select * from my_table where b = 1 order by c,d;
```



##### group by

对已经排好序的数据分组会更快，但前提也得利用到最左前缀

```mysql
select * from my_table group by c,d		#不能利用到索引

select * from my_table where b = 1 group by c,d #能够利用索引加速
```





### 事务

#### ACID

**原子性（Atomicity）:**
原子性是指事务包含的所有操作要么全部成功，要么全部失败回滚，因此事务的操作如果成功就必须要完全应用到数据库，如果操作失败则不能对数据库有任何影响。

**一致性（Consistency）:**
事务开始前和结束后，数据库的完整性约束没有被破坏。比如A向B转账，不可能A扣了钱，B却没收到。

**隔离性（Isolation）:**
隔离性是当多个用户并发访问数据库时，比如操作同一张表时，数据库为每一个用户开启的事务，不能被其他事务的操作所干扰，多个并发事务之间要相互隔离。同一时间，只允许一个事务请求同一数据，不同的事务之间彼此没有任何干扰。比如A正在从一张银行卡中取钱，在A取钱的过程结束前，B不能向这张卡转账。

**持久性（Durability）:**
持久性是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的，即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作。



自动提交：

```mysql
show variables like 'autocommit';
#如果是on，那么如果不显示地使用start transaction或者begin开启一个事务，那么每条语句视为一个独立的事务，这个特性称之为事务的自动提交。
```

关闭这个功能：

- 显示地使用start transaction或者begin
- set autocommit=OFF;



##### 隐式提交

- DDL语句（定义或修改数据库对象，即数据库、表、索引、视图、存储过程、触发器等）：使用ALTER, CREATE, DROP等语句去修改这些对象时，就会隐式提交前面语句所属的事务。
- 隐式修改数据库表的语句：ALTER USER, CREATE USER, DROP USER, GRANT, RENAME USER, REVOKE, SET PASSWORD
- 事务控制或关于锁定的语句：当在一个事务还没有提交或者回滚时就又使用START TRANSACTION或者BEGIN语句开启一个事务，则隐式提交上一个事务；当autocommit从off设成on的时候；使用Lock tables, unlock tables等关于锁定的语句也会隐式提交
- 加载数据：LOAD DATA批量往数据库中导数据
- 其他：ANALYZE TABLE, CACHE INDEX, CHECK TABLE, FLUSH, LOAD INDEX INTO CACHE, OPTIMIZE TABLE,  REPAIR TABLE, RESET等语句。



##### 保存点

savepoint

```mysql
begin;

```





#### 锁

