## 数据类型

- byte: 有符号字节
- i16: 16位有符号整数
- I32: 32位有符号整数
- I64: 
- double: 64位浮点数
- string: 字符串
- list: 有序列表，元素可重复
- set: 无序集合，元素不可重复
- map: 字典结构，键值对，相当于java中的hashmap



struct类型

```c
struct People {
    1:string name;
    2:i32 age;
    3:str
```



枚举类型

```c
enum Gender(
	MALE,
	FEMALE
)
```

异常

```c
exception RequestException {
    1:i32 code;
    2:string reason;
}
```



定义别名

```c
typedef i32 int
typedef i64 long
```





## 搭建环境

### linux

下载thrift

```bash
$ wget http://www-us.apache.org/dist/thrift/0.10.0/thrift-0.10.0.tar.gz
$ tar -xzvf thrift-0.10.tar.gz
$ cd thrift-0.10.0
$ ./configure  --prefix=/usr
$ make -j8
$ [sudo] make install 
```



### MAC os

使用brew安装

1. 先安装brew

```bash
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

2. 使用brew安装thrit

```bash
brew install thrift
#卸载
brew remove xxx
#搜素
brew search xxx








```



