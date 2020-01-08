## 一、环境搭建

### Linux

1. 打开官网下载地址，选择对应版本的链接
2. cd进入存放安装包的目录

```bash
# 在 ~ 下创建 go 文件夹，并进入 go 文件夹
mkdir ~/go && cd ~/go
下载的 go 压缩包
wget https://dl.google.com/go/go1.11.5.linux-amd64.tar.gz
```

3. 执行tar解压到/usr/local目录下（官方推荐），得到go文件夹

```bash
tar -C /usr/local -zxvf  go1.11.5.linux-amd64.tar.gz
```

4. 添加`/usr/loacl/go/bin`目录到PATH变量中。添加到`/etc/profile` 或`$HOME/.profile`都可以

```bash
# 习惯用vim，没有的话可以用命令`sudo apt-get install vim`安装一个
vim /etc/profile
# 在最后一行添加
export GOROOT=/usr/local/go
export PATH=$PATH:$GOROOT/bin
# 保存退出后source一下（vim 的使用方法可以自己搜索一下）
source /etc/profile
```

5. 执行go version，查看版本号，验证安装是否成功

*go env查看go环境





## 二、基本语法

### 变量

查看变量类型

```go
fmt.Printf("%T", v)
```



查看变量内存大小

```go
fmt.Printf("%d\n", unsafe.Sizeof(v))
```



- 浮点

golang浮点类型有固定的范围和字段长度，不受具体的os影响；

浮点型默认声明为float64位



- 字符

golang没有专门的字符类型，字符串由byte组成。

当直接输出byte时，会直接输出字符对应的码值；如果希望输出对应字符，应该使用格式化输出

```go
type byte = uint8

type rune = int32
```



```go
var c byte = 'a'

fmt.Printf("c = %c\n", c)

//var c3 byte = '人'	//此时再用byte存unicode会溢出
var c3 int = '人'		//应该使用int使用
fmt.Printf("c3 = %c\n", c3)

```

*需要保存的字符对应码值大于255，可考虑用int存储



- 布尔

bool类型占用一个字节，只允许取值true/false



- 字符串

一旦赋值，不可变

```go
var str = "hello"
//str[0] = 'a'  illegal
```

两种表示形式：

1. 双引号
2. 反引号(`)，以字符串的原声形式输出，包括换行和特殊字符，可以防止攻击、输出源代码的功能

```go
str := `
	package main

  import "fmt"

  func main(){
    var c1 byte = 'a'
    var c2 byte = 'c'
    fmt.Println(c2 - c1)	//字符可看作整型，可以做运算
  }
`
```

拼接

```go
str1 := "hello" + "world"

str1 += " lxw"

//当字符串很长时，换行拼接要把+号留在上面
str := "1" +
	"2" + 
	"3"
```



- 基本数据类型转换

不同数据类型之间一定要显示转换，不能自动转换





### 流程控制

#### 循环

```go
for i:=0; i< 10; i++{
  fmt.Println(i)
}
```



### 函数

Go语言里面拥三种类型的函数：

- 普通的带有名字的函数
- 匿名函数或者 lambda 函数
- 方法



```go
func 函数名(形式参数列表)返回值列表(可省略){
    函数体
}
```

#### 返回值

1. 同一类型

```go
func typedTwoValues() (int, int) {
    return 1, 2
}

```

2. 带变量名

```go
func namedRetValues() (a, b int) {
    a = 1
    b = 2
    return
}
```

当函数使用命名返回值时，可以在 return 中不填写返回值列表，如果填写也是可行的，下面代码的执行效果和上面代码的效果一样。



#### 传参

没有默认值的概念

按值传递和通过函数返回值返回都会发生复制行为（同c++），参数的值不会发生变化

传递指针传递的是指针值，不会复制指针指向的部分。



将函数作为参数：

```go
func fire() {
	    fmt.Println("fire")
}
func main() {
    var f func()			//此时 f 就被俗称为“回调函数”，此时 f 的值为 nil。
    f = fire
    f()
}



//普通函数
func proc(list []int, f func(int)) {
	for _, v := range list {
		f(v)
	}
}

func main() {
	//匿名函数
	list := []int{1, 2, 3, 4, 5, 6, 7}
	proc(list, func(a int) {
		fmt.Println(a)
	})
}
```



#### 闭包

函数 + 引用环境 = 闭包

![img](http://c.biancheng.net/uploads/allimg/180814/1-1PQ41F62I51.jpg)



在某些编程语言中也被称为Lambda表达式（Java）

```go
// 准备一个字符串
str := "hello world"
// 创建一个匿名函数
foo := func() {
   
    // 匿名函数中访问str
    str = "hello dude"
}
// 调用匿名函数
foo()	
//最后str变为hello dude
```



##### 记忆效应

被捕获到闭包中的变量让闭包本身拥有了记忆效应，闭包中的逻辑可以修改闭包捕获的变量，变量会跟随闭包生命期一直存在，闭包本身就如同变量一样拥有了记忆效应。

```go

// 提供一个值, 每次调用函数会指定对值进行累加
func Accumulate(value int) func() int {
	// 返回一个闭包
	return func() int {
		// 累加
		value++
		// 返回一个累加值
		return value
	}
}

func main() {
	//匿名函数
	list := []int{1, 2, 3, 4, 5, 6, 7}
	proc(list, func(a int) {
		fmt.Println(a)
	})

	fmt.Println("闭包")

	// 创建一个累加器, 初始值为1
	accumulator := Accumulate(1)
	// 累加1并打印
	fmt.Println(accumulator())
	fmt.Println(accumulator())
	// 打印累加器的函数地址
	fmt.Printf("%p\n", accumulator)
	// 创建一个累加器, 初始值为1
	accumulator2 := Accumulate(10)
	// 累加1并打印
	fmt.Println(accumulator2())
	// 打印累加器的函数地址
	fmt.Printf("%p\n", accumulator2)
}
```



#### 可变参数

可变参数是指函数传入的参数个数是可变的（同java, python）

```go
func myfunc(args ...int) {
    for _, arg := range args {
        fmt.Println(arg)
    }
}
```

形如`...type`格式的类型只能作为函数的参数类型存在，并且必须是**最后一个参数**



之前的例子中将可变参数类型约束为 int，如果希望传任意类型，可以指定类型为 interface{}，如fmt.Printf()

```go
func Printf(format string, args ...interface{}) {
    // ...
}
```



#### defer

defer 语句会将其后面跟随的语句进行延迟处理，在 defer 归属的函数即将返回时，将延迟处理的语句按 defer 的逆序进行执行。**先defer的后执行**

延迟执行可用于延迟并发解锁

```go
// 根据键读取值
func readValue(key string) int {
    // 对共享资源加锁
    valueByKeyGuard.Lock()
    // 取值
    v := valueByKey[key]
    // 对共享资源解锁
    valueByKeyGuard.Unlock()
    // 返回值
    return v
}

//修改为
func readValue(key string) int {
    valueByKeyGuard.Lock()
   
    // defer后面的语句不会马上调用, 而是延迟到函数结束时调用
    defer valueByKeyGuard.Unlock()
    return valueByKey[key]
}
```



也可用于释放文件句柄



#### 递归

```go
func quickSort(arr []int, L int, R int) {
	elem := arr[L]
	l := L
	r := R

	for l < r {
		for l < r && arr[r] > elem {
			r--
		}
		if l < r {
			arr[l] = arr[r]
		}

		for l < r && arr[l] < elem {
			l++
		}
		if l < r {
			arr[r] = arr[l]
		}
	}
	arr[l] = elem
	if L < l {
		quickSort(arr, L, l-1)
	}
	if r < R {
		quickSort(arr, r+1, R)
	}
}
```



### nil关键字

- 不能用来比较！（而python中两个None值永远相等）

- nil没有默认类型
- 不同类型nil的指针是一样的
- nil 是 map、slice、pointer、channel、func、interface 的零值
- 不同类型的nil值占用的内润大小不同

```go
func main() {
    var p *struct{}
    fmt.Println( unsafe.Sizeof( p ) ) // 8
    var s []int
    fmt.Println( unsafe.Sizeof( s ) ) // 24
    var m map[int]bool
    fmt.Println( unsafe.Sizeof( m ) ) // 8
    var c chan string
    fmt.Println( unsafe.Sizeof( c ) ) // 8
    var f func()
    fmt.Println( unsafe.Sizeof( f ) ) // 8
    var i interface{}
    fmt.Println( unsafe.Sizeof( i ) ) // 16
}
```



### make和new

make 关键字的主要作用是创建切片、哈希表和 Channel 等内置的数据结构，而 new 的主要作用是为类型申请一片内存空间，并返回指向这片内存的指针。



内建函数 make 用来为 slice，map 或 chan 类型分配内存和初始化一个对象(注意：**只能用在这三种类型上**)





### len和cap

```go
func len(v Type) int
```

- 如果 v 是数组：返回的是数组的元素个数
- 如果 v 是个指向数组的指针：返回的是 *v 的元素个数
- 如果 v 是 slice 或者 map ：返回 v 的元素个数
- 如果 v 是 channel：在管道缓存队列中的元素



函数 cap 的格式:

```go
func cap(v Type) int
```

- 数组：返回的是数组的元素个数，同 len(v)
- 指向数组的指针：返回的是 *v 的元素个数，同 len(v)
- slice：返回的是 slice 最大容量，>=len(v)
- channel： 管道缓存容量



对于make slice而言，有两个概念需要搞清楚：长度len跟容量cap。

- 容量表示底层数组的大小，长度是你可以使用的大小。
- 在用 appen d扩展长度时，如果新的长度小于容量，不会更换底层数组，否则，go 会新申请一个底层数组，拷贝这边的值过去，把原来的数组丢掉。也就是说，容量的用途是：在数据拷贝和内存申请的消耗与内存占用之间提供一个权衡。
- 长度，则是为了限制切片可用成员的数量，提供边界查询的。所以用 make 申请好空间后，需要注意访问不要长度越界



## 三、容器

### 1. 数组

```go
var 数组变量名 [元素数量]Type

var a [3] int
l := len(a)

// 打印索引和元素
for i, v := range a {
    fmt.Printf("%d %d\n", i, v)
}
// 仅打印元素
for _, v := range a {
    fmt.Printf("%d\n", v)
}
```



默认情况下，数组的每个元素都会被初始化为元素类型对应的零值，对于数字类型来说就是 0，同时也可以使用数组字面值语法，用一组值来初始化数组：

```go
var q [3]int = [3]int{1, 2, 3}
var r [3]int = [3]int{1, 2}
```

在数组的定义中，如果在数组长度的位置出现“...”省略号，则表示数组的长度是根据初始化值的个数来计算

```go
q = [...]int{1,2,3}
```



数组的长度是数组类型的一个组成部分，因此 [3]int 和 [4]int 是两种不同的数组类型，数组的长度必须是常量表达式，因为数组的长度需要在编译阶段确定。

```go
q := [3]int{1, 2, 3}
q = [4]int{1, 2, 3, 4} // 编译错误：无法将 [4]int 赋给 [3]int
```



比较数组是否相等，只能比较两个类型相同的数组，即元素类型和长度都相同的两个数组。

```go
a := [2]int{1, 2}
b := [...]int{1, 2}
c := [2]int{1, 3}
fmt.Println(a == b, a == c, b == c) // "true false false"
d := [3]int{1, 2}
fmt.Println(a == d) // 编译错误：无法比较 [2]int == [3]int
```





### 2. 切片

可以理解是一种动态数组。切片是一个引用类型，默认指向一段连续内存区域，可以是数组，也可以是切片本身。

![img](http://c.biancheng.net/uploads/allimg/180813/1-1PQ3154340Y9.jpg)



- 声明切片

```go
var name []Type

// 声明字符串切片
var strList []string
// 声明整型切片
var numList []int
// 声明一个空切片
var numListEmpty = []int{}
// 输出3个切片
fmt.Println(strList, numList, numListEmpty)
// 输出3个切片大小
fmt.Println(len(strList), len(numList), len(numListEmpty))
// 切片判定空的结果
fmt.Println(strList == nil)
fmt.Println(numList == nil)
fmt.Println(numListEmpty == nil)
```

#### append

内建函数 append() 可以为切片动态添加元素

```go
var a []int
a = append(a, 1) // 追加1个元素
a = append(a, 1, 2, 3) // 追加多个元素, 手写解包方式
a = append(a, []int{1,2,3}...) // 追加一个切片, 切片需要解包

//链式操作
var a []int
a = append(a[:i], append([]int{x}, a[i:]...)...) // 在第i个位置插入x
a = append(a[:i], append([]int{1,2,3}, a[i:]...)...) // 在第i个位置插入切片
```



#### copy

```go
copy( destSlice, srcSlice []T) int
```

如果加入的两个数组切片不一样大，就会按照其中较小的那个数组切片的元素个数进行复制。copy() 函数的返回值表示实际发生复制的元素个数。



#### 删除元素

可以直接移动数据指针：

```go
a = []int{1, 2, 3}
a = a[1:] // 删除开头1个元素
a = a[N:] // 删除开头N个元素
```

也可以不移动数据指针，但是将后面的数据向开头移动，可以用 append 原地完成（所谓原地完成是指在原有的切片数据对应的内存区间内完成，不会导致内存空间结构的变化）：

```go
a = []int{1, 2, 3}
a = append(a[:0], a[1:]...) // 删除开头1个元素
a = append(a[:0], a[N:]...) // 删除开头N个元素
```

还可以用 copy() 函数来删除开头的元素：

```go
a = []int{1, 2, 3}
a = a[:copy(a, a[1:])] // 删除开头1个元素
a = a[:copy(a, a[N:])] // 删除开头N个元素
```

​	



### 3. range

```go
// 创建一个整型切片，并赋值
slice := []int{10, 20, 30, 40}
// 迭代每一个元素，并显示其值
for index, value := range slice {
    fmt.Printf("Index: %d Value: %d\n", index, value)
}
```



当迭代切片时，关键字 range 会返回两个值，第一个值是当前迭代到的索引位置，第二个值是该位置对应元素值的一份副本。

![使用 range 迭代切片会创建每个元素的副本](http://c.biancheng.net/uploads/allimg/190614/4-1Z614115226164.gif)

```go
// 创建一个整型切片，并赋值
slice := []int{10, 20, 30, 40}
// 迭代每个元素，并显示值和地址
for index, value := range slice {
    fmt.Printf("Value: %d Value-Addr: %X ElemAddr: %X\n", value, &value, &slice[index])
}

/*
Value: 10 Value-Addr: 10500168 ElemAddr: 1052E100
Value: 20 Value-Addr: 10500168 ElemAddr: 1052E104
Value: 30 Value-Addr: 10500168 ElemAddr: 1052E108
Value: 40 Value-Addr: 10500168 ElemAddr: 1052E10C
*/
```

如果不需要索引值，也可以使用下划线`_`来忽略这个值。





### 4. map

- map中的每个key在keys的集合中是唯一的，而且需要支持 == or != 操作
- interface{}类型可以作为key，但是需要加入的key的类型是可以比较的
- slice不支持比较运算，不可以作为map的key
- 要求所有的key的数据类型相同，所有value数据类型相同
- 如果key为结构体，则结构体中的每一个属性都要支持比较运算



#### 创建

1. 字面值

```go
  // 1 字面值
    {
        m1 := map[string]string{
            "m1": "v1", // 定义时指定的初始key/value, 后面可以继续添加
        }
        _ = m1

    }

    // 2 使用make函数
    {
        m2 := make(map[string]string) // 创建时，里面不含元素，元素都需要后续添加
        m2["m2"] = "v2"               // 添加元素
        _ = m2

    }

    // 定义一个空的map
    {
        m3 := map[string]string{}
        m4 := make(map[string]string)
        _ = m3
        _ = m4
    }
```



#### 增删改查

```go
m1 := map[int]string{
		1: "lxw",
	}

	fmt.Println(m1[1])
	delete(m1, 2)
	fmt.Println(m1[1])
	m1[1] = "xwl"
	fmt.Println(m1[1])
	
	m2 := make(map[int]string)
	m2[1] = "shu"
	m2[2] = "neu"
	fmt.Println(m2[1])
```





#### 并发

Go语言在 1.9 版本中提供了一种效率较高的并发安全的 sync.Map，sync.Map 和 map 不同，不是以语言原生形态提供，而是在 sync 包下的特殊结构。

```go

func trySyncMap() {
	var sMap sync.Map //sync.Map 不能使用 make 创建。

	sMap.Store("java", 1)
	sMap.Store("python", 2)
	sMap.Store("go", 3)

	fmt.Println(sMap.Load("java"))

	sMap.Delete("java")
	fmt.Println(sMap.Load("java"))
	//遍历
	sMap.Range(func(k, v interface{}) bool {
		fmt.Println("iterate:", k, v)
		return true
	})
}

```

### 4. List

内部实现为双链表

```go

func tryList() {
	var myList list.List

	//myList := list.New()

	myList.PushBack(1)
	myList.PushBack(2)

	//获得元素句柄后插入
	element := myList.PushFront("lixiuwen")
	myList.InsertBefore("xxx", element)
	myList.InsertAfter("aaa", element)

	//遍历
	for i := myList.Front(); i != nil; i = i.Next() {
		fmt.Println(i.Value)
	}
}
```





## 四、结构体

Go 语言中没有“类”的概念，也不支持“类”的继承等面向对象的概念。

Go 语言的结构体与“类”都是复合结构体，但 Go 语言中结构体的内嵌配合接口比面向对象具有更高的扩展性和灵活性。

Go 语言不仅认为结构体能拥有方法，且每种自定义类型也可以拥有自己的方法。

Go 语言中的类型可以被实例化，使用`new`或`&`构造的类型实例的类型是类型的指针。



结构体的定义：

```go
type structName struct {
    fieldOne fieldOneType
    fieldTwo fieldTwoType
    …
}
```

结构体的定义只是一种内存布局的描述，只有当结构体实例化时，才会真正地分配内存，因此必须在定义结构体并实例化后才能使用结构体的字段。



### 实例化	

1. 基本实例化：

```go
type T struct{
  name string
  age int
}

var obj T
```



2. new实例化：

```go
ptr := new(T)
```

其中：

- T 为类型，可以是结构体、整型、字符串等。
- ptr：T 类型被实例化后保存到 ptr 变量指向的地址空间，ptr 的类型为 *T，属于指针。



3. 取结构体的地址实例化

```go
obj := &T{}
```

在Go语言中，对结构体进行`&`取地址操作时，视为对该类型进行一次 new 的实例化操作，返回实例的指针



### 初始化成员变量

1) 键值对

```go
//初始化成员变量
	obj := T{
		age:  20,
		name: "lixiuwen",
	}
```



2) 多个值列表初始化

```go
obj01 := T{
		"xxx",
		11,
	}
```

使用这种格式初始化时，需要注意：

- 必须初始化结构体的所有字段。
- 每一个初始值的填充顺序必须与字段在结构体中的声明顺序一致。
- 键值对与值列表的初始化形式不能混用。



模拟构造函数：

```go

//模拟构造函数
func initCat(color, name string) *Cat {	//返回指针
	return &Cat{
		color: color,
		name:  name,
	}
}

func testInit() {
	obj := initCat("red", "gg")			//得到该结构体的指针
	fmt.Println(obj.color, " ", obj.name)		//语法糖
}

```

### 接收器

Go语言中“方法”的概念与其他语言一致，只是Go语言建立的“接收器”强调方法的作用对象是接收器。

使用格式：

```go
func (接收器变量 接收器类型) 方法名(参数列表) (返回参数) {
    函数体
}
```

- 接收器变量：接收器中的参数变量名在命名时，官方建议使用接收器类型名的第一个小写字母，而不是 self、this 之类的命名。例如，Socket 类型的接收器变量应该命名为 s，Connector 类型的接收器变量应该命名为 c 等。
- 接收器类型：接收器类型和参数类似，可以是指针类型和非指针类型。
- 方法名、参数列表、返回参数：格式与函数定义一致。

```go

1) 指针型接收器：
指针类型的接收器由一个结构体的指针组成，更接近于面向对象中的 this 或者 self。

//接收器
type Bag struct {
	items []int
}

func (b *Bag) Insert(e ...int) {
	b.items = append(b.items, e...)
}

func testReceiver() {
	b := &Bag{}
	b.Insert(1, 2, 3, 4)
	fmt.Println(b.items)
}

```

2) 非指针类型

当方法作用于非指针接收器时，Go语言会在代码运行时将接收器的值复制一份，在非指针接收器的方法中可以获取接收器的成员值，但修改后无效。

```go

// 定义点结构
type Point struct {
    X int
    Y int
}
// 加方法
func (p Point) Add(other Point) Point {
    return Point{p.X + other.X, p.Y + other.Y}
}
```



### 内嵌结构体

在一个结构体中对于**每一种数据类型只能有一个匿名字段**。

结构体可以包含一个或多个匿名（或内嵌）字段，即这些字段没有显式的名字，只有字段的类型是必须的，此时类型也就是字段的名字。

```go

type A struct {
	ax, ay int
}
type B struct {
	A
	// ax     int
	bx, by float32
}

func testInner() {
	b := B{A{1, 2}, 3.0, 4.0}
	fmt.Println(b.ax, b.ay, b.bx, b.by)
	fmt.Println(b.ax)
}

```



初始化：

```go
//初始化内部匿名类
type Car struct {
	engine struct {
		Power int
		Type  string
	}
}

func testInitInnerAnony() {
	car := &Car{
		engine: struct {
			Power int
			Type  string
		}{
			1,
			"what",
		},
	}
	fmt.Println(car.engine.Power, " ", car.engine.Type)
}

```



#### 模拟类继承

Go语言的结构体内嵌特性就是一种组合特性，使用组合特性可以快速构建对象的不同特性。

```go
package main
import "fmt"
// 可飞行的
type Flying struct{}
func (f *Flying) Fly() {
    fmt.Println("can fly")
}
// 可行走的
type Walkable struct{}
func (f *Walkable) Walk() {
    fmt.Println("can calk")
}
// 人类
type Human struct {
    Walkable // 人类能行走
}
// 鸟类
type Bird struct {
    Walkable // 鸟类能行走
    Flying   // 鸟类能飞行
}
func main() {
    // 实例化鸟类
    b := new(Bird)
    fmt.Println("Bird: ")
    b.Fly()
    b.Walk()
    // 实例化人类
    h := new(Human)
    fmt.Println("Human: ")
    h.Walk()
}
```







#### 模拟单链表

```go
package main

import "fmt"

type Student struct {
	id   int
	name string
}

type Node struct {
	Student
	next *Node
}

type List struct {
	head *Node
	num  int
}

//初始化链表
func InitList() *List {
	list := &List{
		head: nil,
		num:  0,
	}
	return list
}

//打印链表
func (list *List) print() {
	fmt.Println("total: ", list.num)
	for ptr := list.head; ptr != nil; ptr = ptr.next {
		fmt.Println(ptr.id, "->", ptr.name)
	}
}

//头插
func (list *List) insert(stu Student) {
	ptr := list.head
	if ptr == nil {
		ptr = &Node{
			Student: stu,
			next:    nil,
		}
		list.head = ptr
	} else {
		tmp := &Node{
			Student: stu,
			next:    ptr.next,
		}
		tmp.next = ptr
		list.head = tmp
	}
	list.num++
}

//删除
func (list *List) delete(index int, elem *Node) bool {
	if index < 0 || index >= list.num {
		return false
	}

	list.num--
	ptr := list.head

	if index == 0 {
		elem = ptr //取回该元素
		list.head = ptr.next
		return true
	}

	//非头结点
	var pre *Node
	for cnt := 0; ptr != nil && cnt < index; ptr = ptr.next {
		cnt++
		pre = ptr
	}
	pre.next = ptr.next
	elem = ptr //取回该元素

	return true
}

func main() {
	myList := InitList()
	fmt.Println(myList.num)

	var obj Student
	obj.id = 1
	obj.name = "what"
	for i := 0; i < 10; i++ {
		obj.id = i
		myList.insert(obj)
	}

	myList.print()
	var node *Node
	myList.delete(1, node)
	myList.print()
}

```

### finalizer

GC 是自动进行的，如果要手动进行 GC，可以使用 runtime.GC() 函数。显式的进行 GC 只在某些特殊的情况下才有用，比如当内存资源不足时调用 runtime.GC() ，这样会立即释放一大片内存，但是会造成程序短时间的性能下降。

finalizer（终止器）是与对象关联的一个函数，通过 runtime.SetFinalizer 来设置，如果某个对象定义了finalizer，当它被 GC 时候，这个 finalizer 就会被调用，以完成一些特定的任务，例如发信号或者写日志等。

定义：

```go
func SetFinalizer(x, f interface{})
```

- 参数 x 必须是一个指向通过 new 申请的对象的指针，或者通过对复合字面值取址得到的指针。
- 参数 f 必须是一个函数，它接受单个可以直接用 x 类型值赋值的参数，也可以有任意个被忽略的返回值。

```go
package main
import (
    "log"
    "runtime"
    "time"
)
type Road int
func findRoad(r *Road) {
    log.Println("road:", *r)
}
func entry() {
    var rd Road = Road(999)
    r := &rd
    runtime.SetFinalizer(r, findRoad)
}
func main() {
    entry()
    for i := 0; i < 10; i++ {
        time.Sleep(time.Second)
        runtime.GC()
    }
}
```





### json序列化

```go
package main
import (
    "encoding/json"
    "fmt"
)
func main() {
    // 声明技能结构体
    type Skill struct {
        Name  string `json:"name"`
        Level int    `json:"level"`
    }
    // 声明角色结构体
    type Actor struct {
        Name   string
        Age    int
        Skills []Skill
    }
    // 填充基本角色数据
    a := Actor{
        Name: "cow boy",
        Age:  37,
        Skills: []Skill{
            {Name: "Roll and roll", Level: 1},
            {Name: "Flash your dog eye", Level: 2},
            {Name: "Time to have Lunch", Level: 3},
        },
    }
    result, err := json.Marshal(a)
    if err != nil {
        fmt.Println(err)
    }
    jsonStringData := string(result)
    fmt.Println(jsonStringData)
}
```



*忽略缺省字段

```go
type Skill struct {
        Name  string `json:"name,omitempty"`
        Level int    `json:"level"`
}
```





## 五、接口

把所有的具有共性的方法定义在一起，任何其他类型只要实现了这些方法就是实现了这个接口。

格式：

```go
type 接口类型名 interface{
    方法名1( 参数列表1 ) 返回值列表1
    方法名2( 参数列表2 ) 返回值列表2
    …
}
```



**可能出现的错误：**

1. 函数名不一致
2. argument list 不一致





当一个接口中有多个方法时，实现类需要将**所有的方法全部实现**才能通过编译。需要将接口中的方法全部实现。Go语言的接口实现是隐式的，无须让实现接口的类型写出实现了哪些接口。这个设计被称为非侵入式设计。



### 自定义类型排序

```go

type Student struct {
	id   int
	name string
}

type Students []*Student //学生指针的数组

func (n Students) Len() int {
	return len(n)
}

func (n Students) Less(i, j int) bool {
	if n[i].id == n[j].id {
		return n[i].name < n[j].name
	}
	return n[i].id < n[j].id
}

func (n Students) Swap(i, j int) {
	n[i], n[j] = n[j], n[i]
}


func testSelfDefinedSort() {
	nodes := Students{
		&Student{2, "gogo"},
		&Student{1, "xwl"},
		&Student{1, "aaa"},
		&Student{0, "123123"},
	}
	sort.Sort(nodes)
	for i := 0; i < len(nodes); i++ {
		fmt.Println(nodes[i].id, "->", nodes[i].name)
	}
}

```

### 接口嵌套组合

```go
type Writer interface {
    Write(p []byte) (n int, err error)
}
type Closer interface {
    Close() error
}
type WriteCloser interface {
    Writer
    Closer
}
```

![img](http://c.biancheng.net/uploads/allimg/180816/1-1PQ61125141Z.jpg)

![img](http://c.biancheng.net/uploads/allimg/180816/1-1PQ6112952232.jpg)



### 断言

1. 检查接口具体类型

断言是一个使用在接口值上的操作，用于检查接口类型变量所持有的值是否实现了期望的接口或者具体的类型。

```go
value, ok := x.(T)
```

其中，x 表示一个接口的类型，T 表示一个具体的类型（也可为接口类型）。

该断言表达式会返回 x 的值（也就是 value）和一个布尔值（也就是 ok），可根据该布尔值判断 x 是否为 T 类型：

- 如果 T 是具体某个类型，类型断言会检查 x 的动态类型是否等于具体类型 T。如果检查成功，类型断言返回的结果是 x 的动态值，其类型是 T。
- 如果 T 是接口类型，类型断言会检查 x 的动态类型是否满足 T。如果检查成功，x 的动态值不会被提取，返回值是一个类型为 T 的接口值。
- 无论 T 是什么类型，如果 x 是 nil 接口值，类型断言都会失败。

例：

```go
var x interface{}
x = 'c'
value, _  := x.(string)
fmt.Println(value)      //如果断言失败，会返回断言类型的default值   
```



2. 将接口转换成为另一个接口

```go
package main

import (
	"fmt"
	"io"
)

type Writer interface {
	Write(p []byte) (n int, err error)
}
type Closer interface {
	Close() error
}
type WriteCloser interface {
	Writer
	Closer
}

// 声明一个设备结构
type device struct {
}

// 实现io.Writer的Write()方法
func (d *device) Write(p []byte) (n int, err error) {
	return 0, nil
}

// 实现io.Closer的Close()方法
func (d *device) Close() error {
	fmt.Println("has been closed")
	return nil
}

func main() {

	value, ok := wc.(Closer)			//转换成为Closer类型接口
	if ok {
		fmt.Println(value)
		value.Close()
	} else {
		fmt.Println("error")
	}
}

```

3. 将转换成为其他类型

假设pig实现了Walker接口

```go
p1 := new(pig)
var a Walker = p1	//由于 pig 实现了 Walker 接口，因此可以被隐式转换为 Walker 接口类型保存于 a 中
p2 := a.(*pig)		//由于 a 中保存的本来就是 *pig 本体，因此可以转换为 *pig 类型
fmt.Printf("p1=%p p2=%p", p1, p2)
```





### 空接口

可将值保存到空接口

```go
var a interface{}

a = 1

a = "str"

a = false

```



从空接口取值

```go
var a int = 1
var i interface{} = a

b := i	//此时，b和i都是int型
//var b int = i	会报错不能将i变量视为int类型赋值给b。
var b int = i.(int)
```



空值的比较：

1) 类型不同的空接口的比较结果不同

2) 不能比较空接口中的**动态值**（map, 切片）



#### 类型分支

格式：

```go
switch 接口变量.(type) {
    case 类型1:
        // 变量是类型1时的处理
    case 类型2:
        // 变量是类型2时的处理
    …
    default:
        // 变量不是所有case中列举的类型时的处理
}
```



判断基本类型

```go
func printType(v interface{}) {
    switch v.(type) {
    case int:
        fmt.Println(v, "is int")
    case string:
        fmt.Println(v, "is string")
    case bool:
        fmt.Println(v, "is bool")
    }
}
```



判断接口类型

```go

// 电子支付方式
type Alipay struct {
}
// 为Alipay添加CanUseFaceID()方法, 表示电子支付方式支持刷脸
func (a *Alipay) CanUseFaceID() {
}
// 现金支付方式
type Cash struct {
}
// 为Cash添加Stolen()方法, 表示现金支付方式会出现偷窃情况
func (a *Cash) Stolen() {
}
// 具备刷脸特性的接口
type CantainCanUseFaceID interface {
    CanUseFaceID()
}
// 具备被偷特性的接口
type ContainStolen interface {
    Stolen()
}
// 打印支付方式具备的特点
func print(payMethod interface{}) {
    switch payMethod.(type) {
    case CantainCanUseFaceID:  // 可以刷脸
        fmt.Printf("%T can use faceid\n", payMethod)
    case ContainStolen:  // 可能被偷
        fmt.Printf("%T may be stolen\n", payMethod)
    }
}
```





## 六、包

标准包的源码位于 GOROOT/src/ 下面，标准包可以直接引用。自定义的包和第三方包的源码必须放到 GOPATH/src 目录下才能被引用。



### 引用

GOPATH:

使用绝对路径提供项目的工作目录。

```bash
go env 		#其中可查看到GOPATH
```



引用路径：

假设包a和b都在$GOPATH/src/lab/下，则b引用a

```go
// 相对路径引用
import "../a"
// 全路径引用
import "lab/a"
```



引用格式：

1. 标准引用

```go
import "fmt"
```

2. 别名引用

```go
import F "fmt"
```

3. 省略方式

```go
import . "fmt"
```

4. 仅执行初始化init函数

```go
import _ "fmt"
```

- 一个包可以有多个 init 函数，包加载会执行全部的 init 函数，但并不能保证执行顺序，所以不建议在一个包中放入多个 init 函数，将需要初始化的逻辑放到一个 init 函数里面。
- 包不能出现环形引用。比如包 a 引用了包 b，包 b 引用了包 c，如果包 c 又引用了包 a，则编译不能通过。
- 包的重复引用是允许的。比如包 a 引用了包 b 和包 c，包 b 和包 c 都引用了包 d。这种场景相当于重复引用了 d，这种情况是允许的，并且 Go 编译器保证 d 的 init 函数只会执行一次。



包加载：
![Go åçåå§å](http://c.biancheng.net/uploads/allimg/190821/4-1ZR1102245R8.gif)

- 包初始化程序从 main 函数引用的包开始，逐级查找包的引用，直到找到没有引用其他包的包，最终生成一个包引用的有向无环图。
- Go 编译器会将有向无环图转换为一棵树，然后从树的叶子节点开始逐层向上对包进行初始化。
- 单个包的初始化过程如上图所示，先初始化常量，然后是全局变量，最后执行包的 init 函数（如果有）。



#### 包初始化

Go 语言为以上问题提供了一个非常方便的特性：init() 函数。

init() 函数的特性如下：

- 每个源码可以使用 1 个 init() 函数。
- init() 函数会在程序执行前（main() 函数执行前）被自动调用。
- 调用顺序为 main() 中引用的包，以深度优先顺序初始化。

在运行时，被最后导入的包会最先初始化并调用 init() 函数。







### 新建包

两种类型的包，一种是**可执行包**，另一种是**应用包**。

在src目录下创建一个greet目录，并在其中创建一些文件，在每个文件的顶部都写上package greet声明这是一个应用包。

#### 导出包成员

一个应用包应该给导入它的包提供一些变量。就像在 `JavaScript` 中的 `export` 语法一样，Go 语言中如果一个变量的名称以**大写字母**开头就是可导出的，其他所有的名称不以大写字母开头的变量都是这个包私有的。



用 `import` 语法后跟包名来导入这个包。Go 程序首先在 `**GOROOT**/src` 目录中寻找包目录，如果没有找到，则会去 `**GOPATH**/src` 目录中继续寻找。由于 `fmt` 包是位于 `GOROOT/src` 目录的 Go 语言标准库中的一部分，它将会从该目录中导入。因为 Go 不能在 `GOROOT` 目录下找到 `greet` 包，它将在 `GOPATH/src` 目录下搜寻，这正是创建这个包的位置。



可以用分组语法（括号）将 `fmt` 和 `greet` 包组合在一起导入。



#### 嵌套包

可以在一个包中嵌套另外的包。因为对于 Go 而言，包只是一个目录，这就像在一个已经存在的包中生成一个子目录一样。需要做的仅仅只是提供这个要被嵌套的包的相对路径。

```go
import (
	"fmt"
	"io"
	"lxw"
	"lxw/greet"
)

func main() {
	testEmptyInterface()
	fmt.Println(lxw.Morning)
	fmt.Println(greet.Morning)		//内嵌包
  
}



包编译

go run 命令编译并执行一个程序。我们同样明白，go install 命令编译一个包并且生成一些二进制可执行文件或者包存档文件。这是为了避免对这些包每次都
```



### 作用域

**作用域是代码块中可使用已定义变量的区域**。包作用域是包内的一个区域，且可以从包中访问已声明的变量（对于包中的所有文件）。这个区域是在包中所有文件的最顶层块。



init函数

像 `main` 函数一样，`init` 函数在包被初始化时被 Go 调用。它不需要任何参数也不返回任何值。`init` 函数由 Go 隐式声明（译注：应该是由 Go 隐式调用），因此你无法从任何地方引用它（或者像 `init()` 这样来调用它）。在一个文件或包中，你可以有多个 `init` 函数。在文件中执行 `init` 函数的顺序和其出现顺序是一致的。（译注：词法文件名顺序，只是目前编译器的实现，Go 规范并没有要求这个顺序，因此程序不能依赖它）

可以在包中的任何位置使用 `init` 函数。这些 `init` 函数以词法文件名顺序（字母顺序）被调用。

在所有的 `init` 函数被执行之后，`main` 函数被调用。因此，`init` 函数的主要作用是将在全局代码中无法初始化的全局变量初始化。例如，数组的初始化。

因为 `for` 语法在包作用域中不可用，所以我们可以在 `init` 函数中用 `for` 循环将大小为 `10` 的数组 `integers` 初始化。



### 包别名

当导入一个包的时候，Go 使用这个包的包声明创建一个变量。如果用一个名字导入多个包，将会导致冲突。因此，可使用包别名。在关键字 `impot` 和包名之间声明一个变量名作为引用这个包的新变量。



执行顺序

```
go run *.go
├── 被执行的主包
├── 初始化所有被导入的包
|  ├── 初始化所有被导入的包 ( 递归定义 )
|  ├── 初始化所有全局变量
|  └── INIt 函数以字母序被调用
└── 初始化主包
     ├── 初始化所有全局变量
     └── INIt 函数以字母序被调用
     
```



### 常用包

#### sync

Go语言中 sync 包里提供了互斥锁 Mutex 和读写锁 RWMutex 用于处理并发过程中可能出现同时两个或多个协程（或线程）读或写同一个变量的情况。

```go
package main
import (
    "fmt"
    "sync"
    "time"
)
func main() {
    var a = 0
    var lock sync.Mutex
    for i := 0; i < 1000; i++ {
        go func(idx int) {
            lock.Lock()
            defer lock.Unlock()
            a += 1
            fmt.Printf("goroutine %d, a=%d\n", idx, a)
        }(i)
    }
    // 等待 1s 结束主程序
    // 确保所有协程执行完
    time.Sleep(time.Second)
}
```



读写锁



```go
读写锁有如下四个方法：
写操作的锁定和解锁分别是func (*RWMutex) Lock和func (*RWMutex) Unlock；
读操作的锁定和解锁分别是func (*RWMutex) Rlock和func (*RWMutex) RUnlock。
```





## 七、并发

协程：独立的栈空间，共享堆空间，调度由用户自己控制，本质上有点类似于用户级线程，这些用户级线程的调度也是自己实现的。



### Goroutine

使用 go 关键字就可以创建 goroutine，将 go 声明放到一个需调用的函数之前，在相同地址空间调用运行这个函数，这样该函数执行时便会作为一个独立的并发线程，这种线程在Go语言中则被称为 goroutine。

```go
 //go 关键字放在方法调用前新建一个 goroutine 并执行方法体
go GetThingDone(param1, param2);
//新建一个匿名方法并执行
go func(param1, param2) {
}(val1, val2)
//直接新建一个 goroutine 并在 goroutine 中执行代码块
go {
    //do someting...
}
```



例：

```go
func running() {
    var times int
    // 构建一个无限循环
    for {
        times++
        fmt.Println("tick", times)
        // 延时1秒
        time.Sleep(time.Second)
    }
}
func main() {
    // 并发执行程序
    go running()
    // 接受命令行输入, 不做任何事情
    var input string
    fmt.Scanln(&input)
}
```



拿到并行的结果需要使用channel，channel是goroutine之间的通信方式。通过channel 传递对象的过程和调用函数时的参数传递行为比较一致。

channel 是类型相关的，也就是说，一个 channel 只能传递一种类型的值。

定义一个 channel 时，也需要定义发送到 channel 的值的类型，注意，必须使用 make 创建 channel。

```go
ci := make(chan int)
cs := make(chan string)
cf := make(chan interface{})
```



检查工具：`go build -race`

在项目目录下执行这个命令，生成一个可以执行文件，然后再运行这个可执行文件，就可以看到打印出的检测信息。运行生成的可执行文件，效果：

```
==================
WARNING: DATA RACE
Read at 0x000000619cbc by goroutine 8:
  main.incCount()
      D:/code/src/main.go:25 +0x80

Previous write at 0x000000619cbc by goroutine 7:
  main.incCount()
      D:/code/src/main.go:28 +0x9f

Goroutine 8 (running) created at:
  main.main()
      D:/code/src/main.go:17 +0x7e

Goroutine 7 (finished) created at:
  main.main()
      D:/code/src/main.go:16 +0x66
==================
4
Found 1 data race(s)
```



### 加锁

atomic 和 sync 包里的一些函数就可以对共享的资源进行加锁操作。

#### 原子函数

```go
import (
    "fmt"
    "runtime"
    "sync"
    "sync/atomic"
)
var (
    counter int64
    wg      sync.WaitGroup
)
func main() {
    wg.Add(2)
    go incCounter(1)
    go incCounter(2)
    wg.Wait() //等待goroutine结束
    fmt.Println(counter)
}
func incCounter(id int) {
    defer wg.Done()
    for count := 0; count < 2; count++ {
        atomic.AddInt64(&counter, 1) //安全的对counter加1
        runtime.Gosched()
    }
}
```



另外两个有用的原子函数是 LoadInt64 和 StoreInt64。这两个函数提供了一种安全地读和写一个整型值的方式。

```go
var shutdown int64

atomic.StoreInt64(&shutdown, 1)
atomic.LoadInt64(&shutdown)

```



#### 互斥锁

sync.Mutex

```go
import (
    "fmt"
    "runtime"
    "sync"
)
var (
    counter int64
    wg      sync.WaitGroup
    mutex   sync.Mutex
)
func main() {
    wg.Add(2)
    go incCounter(1)
    go incCounter(2)
    wg.Wait()
    fmt.Println(counter)
}
func incCounter(id int) {
    defer wg.Done()
    for count := 0; count < 2; count++ {
        //同一时刻只允许一个goroutine进入这个临界区
        mutex.Lock()
        {
            value := counter
            runtime.Gosched()
            value++
            counter = value
        }
        mutex.Unlock() //释放锁，允许其他正在等待的goroutine进入临界区
    }
}
```



### channel

可以通过通道共享内置类型、命名类型、结构类型和引用类型的值或者指针。

由于chan是引用类型，需要用make创建实例

- 创建

```go
ch1 := make(chan int)                 // 创建一个整型类型的通道
ch2 := make(chan interface{})         // 创建一个空接口类型的通道, 可以存放任意格式
type Equip struct{ /* 一些字段 */ }
ch2 := make(chan *Equip)             // 创建Equip指针类型的通道, 可以存放*Equip
```

- 发送数据

把数据往通道中发送时，如果接收方一直都没有接收，那么发送操作将**持续阻塞**。

```go
// 通道变量 <- 值 		(值的类型必须与ch通道的元素类型一致)

// 创建一个空接口通道
ch := make(chan interface{})
// 将0放入通道中
ch <- 0
// 将hello字符串放入通道中
ch <- "hello"
```

- 接收数据

1. 通道的收发操作在不同的两个 goroutine 间进行
2. 接收将持续阻塞直到发送方发送数据
3. 每次接收一个元素

四种实现方式：

```go
// 1. 阻塞式
data := <-ch

// 2. 非阻塞
data, ok := <-ch
//ok：表示是否接收到数据。
//非阻塞的通道接收方法可能造成高的 CPU 占用，因此使用非常少。如果需要实现接收超时检测，可以配合 select 和计时器 channel 进行

// 3. 忽略值(阻塞)
<-ch

// 4.	循环读
for data := range ch {
}
//遍历的结果就是接收到的数据

```



#### 单向通道

声明格式：

```go
var 通道实例 chan<- 元素类型    // 只能发送通道
var 通道实例 <-chan 元素类型    // 只能接收通道

ch := make(chan int)
// 声明一个只能发送的通道类型, 并赋值为ch
var chSendOnly chan<- int = ch
//声明一个只能接收的通道类型, 并赋值为ch
var chRecvOnly <-chan int = ch


//只创建一个只发送或只读取的通道
ch := make(<-chan int)
var chReadOnly <-chan int = ch
<-chReadOnly
```

关闭管道：

```go
close(ch)
```



#### 带缓冲的通道

```go
ch := make(chan int)
ch <- 1		//此时将引发fatal error: all goroutines are asleep - deadlock!
```



```go
ch := make(chan int, 3)
ch <- 1
ch <- 2			//正常运行
```



#### select

```go
func main() {
    ch := make(chan int)
    quit := make(chan bool)
    //新开一个协程
    go func() {
        for {
            select {
            case num := <-ch:
                fmt.Println("num = ", num)
            case <-time.After(3 * time.Second):
                fmt.Println("超时")
                quit <- true
            }
        }
    }() //别忘了()
    for i := 0; i < 5; i++ {
        ch <- i
        time.Sleep(time.Second)
    }
    <-quit
    fmt.Println("程序结束")
}
```

与 switch 语句相比，select 有比较多的限制，其中最大的一条限制就是每个 case 语句里必须是一个 IO 操作。

在一个 select 语句中，Go语言会按顺序从头至尾评估每一个发送和接收的语句。

如果其中的任意一语句可以继续执行（即没有被阻塞），那么就从那些可以执行的语句中任意选择一条来使用。

如果没有任意一条语句可以执行（即所有的通道都被阻塞），那么有如下两种可能的情况：

- 如果给出了 default 语句，那么就会执行 default 语句，同时程序的执行会从 select 语句后的语句中恢复；
- 如果没有 default 语句，那么 select 语句将被阻塞，直到至少有一个通信可以进行下去。



