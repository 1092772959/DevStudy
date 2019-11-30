## 环境搭建

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





## 基本语法



### 函数

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


匿名函数

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

defer 语句会将其后面跟随的语句进行延迟处理，在 defer 归属的函数即将返回时，将延迟处理的语句按 defer 的逆序进行执行。先defer的后执行

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



## 容器

### 1. 数组



### 2. 切片

可以理解是一种动态数组



### 3. map



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





## 结构体

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



在Go语言中，对结构体进行`&`取地址操作时，视为对该类型进行一次 new 的实例化操作



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

在一个结构体中对于每一种数据类型只能有一个匿名字段。

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

## 接口

把所有的具有共性的方法定义在一起，任何其他类型只要实现了这些方法就是实现了这个接口。

格式：

```go
type 接口类型名 interface{
    方法名1( 参数列表1 ) 返回值列表1
    方法名2( 参数列表2 ) 返回值列表2
    …
}
```



可能出现的错误：

1. 函数名不一致
2. argument list 不一致





当一个接口中有多个方法时，实现类需要将所有的方法全部实现才能通过编译。需要将接口中的方法全部实现。Go语言的接口实现是隐式的，无须让实现接口的类型写出实现了哪些接口。这个设计被称为非侵入式设计。



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

使用断言将接口转换成另一个接口，也可将接口转换为另外的类型。

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
```



空值的比较：

1) 类型不同的空接口的比较结果不同

2) 不能比较空接口中的**动态值**（map, 切片）



## 包

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

在所有的 `init` 函数被执行之后，`main` 函数被调用。因此，**`init` 函数的主要作用是将在全局代码中无法初始化的全局变量初始化。例如，数组的初始化。

*因为 `for` 语法在包作用域中不可用，所以我们可以在 `init` 函数中用 `for` 循环将大小为 `10` 的数组 `integers` 初始化。



### 包别名

当导入一个包的时候，Go 使用这个包的包声明创建一个变量。如果用一个名字导入多个包，将会导致冲突。

![](https://raw.githubusercontent.com/studygolang/gctt-images/master/everything-you-need-to-know-about-packages-in-go/22.png)

因此，可使用包别名。在关键字 `impot` 和包名之间声明一个变量名作为引用这个包的新变量。

![å¾ç 23](https://raw.githubusercontent.com/studygolang/gctt-images/master/everything-you-need-to-know-about-packages-in-go/23.png)



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

