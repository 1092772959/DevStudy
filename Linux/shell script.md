### 变量

#### 定义

```shell
your_name="runoob.com"
```

- 变量名和等号之间不能有空格
- 命名只能使用英文字母，数字和下划线，首个字符不能以数字开头。
- 中间不能有空格，可以使用下划线（_）。
- 不能使用标点符号。
- 不能使用bash里的关键字（可用help命令查看保留关键字）。



#### 使用

使用一个定义过的变量，只要在变量名前面加美元符号即可，如：

```shell
your_name="qinjx"
echo $your_name
echo ${your_name}
```

变量名外面的花括号是可选的，加不加都行，加花括号是为了帮助解释器识别变量的边界。

第二次赋值的时候不能写

```shell
$your_name="alibaba"		# wrong usage
```

使用变量的时候才加美元符（$）



只读变量

```shell
#!/bin/bash
myUrl="http://www.google.com"
readonly myUrl
myUrl="http://www.runoob.com"

#结果
#/bin/sh: NAME: This variable is read only.
```



#### 删除

```shell
#!/bin/sh
myUrl="http://www.runoob.com"
unset myUrl
echo $myUrl

#将没有任何输出
```



运行shell时，会同时存在三种变量：

- **1) 局部变量** 局部变量在脚本或命令中定义，仅在当前shell实例中有效，其他shell启动的程序不能访问局部变量。
- **2) 环境变量** 所有的程序，包括shell启动的程序，都能访问环境变量，有些程序需要环境变量来保证其正常运行。必要的时候shell脚本也可以定义环境变量。
- **3) shell变量** shell变量是由shell程序设置的特殊变量。shell变量中有一部分是环境变量，有一部分是局部变量，这些变量保证了shell的正常运行



#### 字符串变量

使用单引号和双引号都可以，但是单引号中无法使用变量

1) 由单引号`' '`

- 任何字符都会原样输出，在其中使用变量是无效的。
- 字符串中不能出现单引号，即使对单引号进行转义也不行。

2) 由双引号包围的字符串：

- 不被引号包围的字符串中出现变量时也会被解析，这一点和双引号`" "`包围的字符串一样。
- 字符串中不能出现空格，否则空格后边的字符串会作为其他变量或者命令解析。



3) 不被引号包围的字符串

- 不被引号包围的字符串中出现变量时也会被解析，这一点和双引号`" "`包围的字符串一样。
- 字符串中不能出现空格，否则空格后边的字符串会作为其他变量或者命令解析。





```shell
your_name='runoob'
str="Hello, I know you are \"$your_name\"! \n"
echo -e $str
```



拼接

```shell
gt_1='wrry, '$your_name' !'
echo $gt_1


name="Shell"
url="http://c.biancheng.net/shell/"
str1=$name$url  #中间不能有空格
str2="$name $url"  #如果被双引号包围，那么中间可以有空格
str3=$name": "$url  #中间可以出现别的字符串
str4="$name: $url"  #这样写也可以
str5="${name}Script: ${url}index.html"  #这个时候需要给变量名加上大括号

```



获取字符串长度

```shell
string="abcd"
echo ${#string} #输出 4
```



提取子串

```shell
string="runoob is a great site"
echo ${string:1:4} # 输出 unoo 第一个是开始截取的位置， 第二个是截取的长度

echo ${string:1}	#输出unoob is a great site, 不指定长度


```



从指定字符开始截取

使用`#`

```shell
${string#*chars}
```

其中，string 表示要截取的字符，chars 是指定的字符（或者子字符串），`*``*chars`

| 格式                       | 说明                                                         |
| -------------------------- | ------------------------------------------------------------ |
| ${string: start :length}   | 从 string 字符串的左边第 start 个字符开始，向右截取 length 个字符。 |
| ${string: start}           | 从 string 字符串的左边第 start 个字符开始截取，直到最后。    |
| ${string: 0-start :length} | 从 string 字符串的右边第 start 个字符开始，向右截取 length 个字符。 |
| ${string: 0-start}         | 从 string 字符串的右边第 start 个字符开始截取，直到最后。    |
| ${string#*chars}           | 从 string 字符串第一次出现 *chars 的位置开始，截取 *chars 右边的所有字符。 |
| ${string##*chars}          | 从 string 字符串最后一次出现 *chars 的位置开始，截取 *chars 右边的所有字符。 |
| ${string%*chars}           | 从 string 字符串第一次出现 *chars 的位置开始，截取 *chars 左边的所有字符。（从右往左） |
| ${string%%*chars}          | 从 string 字符串最后一次出现 *chars 的位置开始，截取 *chars 左边的所有字符。（从右往左） |





查找子串

查找字符 **i** 或 **o** 的位置(哪个字母先出现就计算哪个)；下标从1开始

```shell
string="runoob is a great site"
echo `expr index "$string" io`  # 输出 4
```



#### 数组

支持一维数组，不支持多维数组，不需要限定数组大小，下标从0开始。

```bash
arr=(1 2 3 4)

echo ${arr[2]}

echo ${arr[@]}		#取全部元素

# 取得数组元素的个数
length=${#array_name[@]}
# 或者
length=${#array_name[*]}
```



- 拼接数组

```shell
#!/bin/bash
array1=(23 56)
array2=(99 "http://c.biancheng.net/shell/")
array_new=(${array1[@]} ${array2[*]})
```

- 删除数组元素

```shell
#!/bin/bash
arr=(23 56 99 "http://c.biancheng.net/shell/")
unset arr[1]			#若不指定下标则会删除整个数组
echo ${arr[@]}
```



#### 关联数组

Bash支持关联数组，它可以使用字符串作为数组索引。（字典）

```shell
declare -A assArray
assArray=([lucy]=beijing [yoona]=shanghai)
echo ${assArray[lucy]}

assArray[lily]=shandong
assArray[sunny]=xian
echo ${assArray[sunny]}


#列出数组索引
echo ${!assArray[*]}


#获取所有键值对
#! /bin/bash
declare -A cityArray
cityArray=([yoona]=beijing [lucy]=shanghai [lily]=shandong)
for key in ${!cityArray[*]}
do
 echo "${key} come from ${cityArray[$key]}"
done
```





#### 特殊变量

| 变量      | 含义                                                         |
| --------- | ------------------------------------------------------------ |
| $0        | 当前脚本的文件名。                                           |
| $n（n≥1） | 传递给脚本或函数的参数。n 是一个数字，表示第几个参数。       |
| $#        | 传递给脚本或函数的参数个数。                                 |
| $*        | 传递给脚本或函数的所有参数。                                 |
| $@        | 传递给脚本或函数的所有参数。当被双引号`" "`包含时，@ 与 * 稍有不同 |
| $?        | 上个命令的退出状态，或函数的返回值                           |
| $$        | 当前 Shell 进程 ID。对于 [Shell 脚本](http://c.biancheng.net/shell/)，就是这些脚本所在的进程 ID。 |



#### 注释

- 单行注释

```shell
#--------------------------------------------
# 这是一个注释
```

- 多行注释

```shell
:<<EOF
注释内容...
注释内容...
注释内容...
EOF
```



### 命令

#### 别名

```bash
#!/bin/bash

alias timestamp='date +%s'
begin=`timestamp`  
sleep 20s
finish=$(timestamp)
difference=$((finish - begin))
echo "run time: ${difference}s"
```



```shell
unalias ll		#删除别名
```



#### echo

不换行输出

```bash
#!/bin/bash
name="Tom"
age=20
height=175
weight=62
echo -n "${name} is ${age} years old, "
echo -n "${height}cm in height "
echo "and ${weight}kg in weight."
echo "Thank you!"

#Tom is 20 years old, 175cm in height and 62kg in weight.
#Thank you!
```



转义字符

默认情况下，echo 不会解析以反斜杠`\`开头的转义字符。比如，`\n`表示换行，echo 默认会将它作为普通字符对待。

```bash
echo -e "hello \nworld"

#hello
#world
```



#### read

| 选项         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| -a array     | 把读取的数据赋值给数组 array，从下标 0 开始。                |
| -d delimiter | 用字符串 delimiter 指定读取结束的位置，而不是一个换行符（读取到的数据不包括 delimiter）。 |
| -e           | 在获取用户输入的时候，对功能键进行编码转换，不会直接显式功能键对应的字符。 |
| -n num       | 读取 num 个字符，而不是整行字符。                            |
| -p prompt    | 显示提示信息，提示内容为 prompt。                            |
| -r           | 原样读取（Raw mode），不把反斜杠字符解释为转义字符。         |
| -s           | 静默模式（Silent mode），不会在屏幕上显示输入的字符。当输入密码和其它确认信息的时候，这是很有必要的。 |
| -t seconds   | 设置超时时间，单位为秒。如果用户没有在指定时间内输入完成，那么 read 将会返回一个非 0 的退出状态，表示读取失败。 |
| -u fd        | 使用文件描述符 fd 作为输入源，而不是标准输入，类似于重定向。 |



读入多个变量

```bash
#!/bin/bash
read -p "Enter some information > " name url age
echo "网站名字：$name"
echo "网址：$url"
echo "年龄：$age"
```



只读一个字符

```bash
#!/bin/bash
read -n 1 -p "Enter a char > " char
printf "\n"  #换行
echo $char
```



#### exit

```bash
#!/bin/bash
echo "befor exit"
exit 8
echo "after exit"
```



可以使用`$?`来查看退出状态。



#### declare

| 选项            | 含义                                                       |
| --------------- | ---------------------------------------------------------- |
| -f [name]       | 列出之前由用户在脚本中定义的函数名称和函数体。             |
| -F [name]       | 仅列出自定义函数名称。                                     |
| -g name         | 在 Shell 函数内部创建全局变量。                            |
| -p [name]       | 显示指定变量的属性和值。                                   |
| -a name         | 声明变量为普通数组。                                       |
| -A name         | 声明变量为关联数组（支持索引下标为字符串）。               |
| -i name         | 将变量定义为整数型。                                       |
| -r name[=value] | 将变量定义为只读（不可修改和删除），等价于 readonly name。 |
| -x name[=value] | 将变量设置为环境变量，等价于 export name[=value]。         |



将变量声明为整数并进行计算

```bash
#!/bin/bash
declare -i m n ret  #将多个变量声明为整数
m=10
n=30	
ret=$m+$n
echo $ret
```





### 运算

#### (())

| 运算操作符/运算命令                | 说明                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| ((a=10+66) ((b=a-15)) ((c=a+b))    | 这种写法可以在计算完成后给变量赋值。以 ((b=a-15)) 为例，即将 a-15 的运算结果赋值给变量 c。  注意，使用变量时不用加`$`前缀，(( )) 会自动解析变量名。 |
| a=$((10+66) b=$((a-15)) c=$((a+b)) | 可以在 (( )) 前面加上`$`符号获取 (( )) 命令的执行结果，也即获取整个表达式的值。以 c=$((a+b)) 为例，即将 a+b 这个表达式的运算结果赋值给变量 c。  注意，类似 c=((a+b)) 这样的写法是错误的，不加`$`就不能取得表达式的结果。 |
| ((a>7 && b==c))                    | (( )) 也可以进行逻辑运算，在 if 语句中常会使用逻辑运算。     |
| echo $((a+10))                     | 需要立即输出表达式的运算结果时，可以在 (( )) 前面加`$`符号。 |
| ((a=3+5, b=a+10))                  | 对多个表达式同时进行计算。                                   |



#### let

- let 命令以空格来分隔多个表达式；
- (( )) 以逗号`,`来分隔多个表达式。

```shell
let a=10 b=20

let 'c=a+b a++'
echo $c $a

echo let a+b  #错误，echo会把 let a+b作为一个字符串输出
```





### 函数

语法：

```shell
function name() {
    statements
    [return value]
}
```







#### 位置参数

- 脚本文件的位置参数

```shell
#!/bin/bash
echo "Language: $1"
echo "URL: $2"
```

- 函数传递位置参数

```shell

#!/bin/bash
#定义函数
function func(){
    echo "Language: $1"
    echo "URL: $2"
}
#调用函数
func C++ http://c.biancheng.net/cplus/
```

#### 获取函数返回值

```shell
variable=`func`
variable=$(func)
```



#### 获取程序的退出状态

```shell
#!/bin/bash
if [ $1 == 100 ]
then
   exit 0  #参数正确，退出状态为0
else
   exit 1  #参数错误，退出状态1
fi
```

结果：

```shell
[mozhiyan@localhost ~]$ cd demo
[mozhiyan@localhost demo]$ bash ./test.sh 100  #作为一个新进程运行
[mozhiyan@localhost demo]$ echo $?
0
```





#### 命令替换

将命令的输出结果赋值给某个变量

```shell
#!/bin/bash
begin_time=`date`    #开始时间，使用``替换
sleep 20s            #休眠20秒
finish_time=$(date)  #结束时间，使用$()替换
echo "Begin time: $begin_time"
echo "Finish time: $finish_time"
```

使用 data 命令的`%s`格式控制符可以得到当前的 UNIX 时间戳，这样就可以直接计算脚本的运行时间了。



当输出内容包含多行时，应用双引号包围，否则系统会使用默认的空白符来填充

```shell
#!/bin/bash
LSL=`ls -l`
echo $LSL  #不使用双引号包围
echo "--------------------------"  #输出分隔符
echo "$LSL"  #使用引号包围


```





### 流程

#### if

```shell
if  condition
then
    statement(s)
fi

# or
if  condition;  then
    statement(s)
fi
```

当 if 和 then 位于同一行的时候，这个分号是必须的，否则会有语法错误。

```shell
read a
read b
if (( $a == $b && $a > 0 )) ;then
    echo "a和b相等, 且a大于0"
fi
```



#### if else

```shell
if (( $a == $b && $a > 0 )) ;then
    echo "a和b相等, 且a大于0"
else
    echo "不合法"
fi
```



#### if elif else

```shell
#!/bin/bash
printf "Input integer number: "
read num
if ((num==1)); then
    echo "Monday"
elif ((num==2)); then
    echo "Tuesday"
elif ((num==3)); then
    echo "Wednesday"
elif ((num==4)); then
    echo "Thursday"
elif ((num==5)); then
    echo "Friday"
elif ((num==6)); then
    echo "Saturday"
elif ((num==7)); then
    echo "Sunday"
else
    echo "error"
fi
```



#### test

test 和 [] 是等价的。

注意`[]`和`expression`之间的空格，这两个空格是必须的，否则会导致语法错误。`[]`的写法更加简洁，比 test 使用频率高。

```shell
#!/bin/bash
read age
if test $age -le 2; then
    echo "婴儿"
elif test $age -ge 3 && test $age -le 8; then
    echo "幼儿"
elif [ $age -ge 9 ] && [ $age -le 17 ]; then
    echo "少年"
elif [ $age -ge 18 ] && [ $age -le 25 ]; then
    echo "成年"
elif test $age -ge 26 && test $age -le 40; then
    echo "青年"
elif test $age -ge 41 && [ $age -le 60 ]; then
    echo "中年"
else
    echo "老年"
fi
```

其中，`-le`选项表示小于等于，`-ge`选项表示大于等于，`&&`是逻辑与运算符。



- 与文件相关的选项

| 文件类型判断            |                                                              |
| ----------------------- | ------------------------------------------------------------ |
| 选 项                   | 作 用                                                        |
| -b filename             | 判断文件是否存在，并且是否为块设备文件。                     |
| -c filename             | 判断文件是否存在，并且是否为字符设备文件。                   |
| -d filename             | 判断文件是否存在，并且是否为目录文件。                       |
| -e filename             | 判断文件是否存在。                                           |
| -f filename             | 判断文件是否存在，井且是否为普通文件。                       |
| -L filename             | 判断文件是否存在，并且是否为符号链接文件。                   |
| -p filename             | 判断文件是否存在，并且是否为管道文件。                       |
| -s filename             | 判断文件是否存在，并且是否为非空。                           |
| -S filename             | 判断该文件是否存在，并且是否为套接字文件。                   |
| 文件权限判断            |                                                              |
| 选 项                   | 作 用                                                        |
| -r filename             | 判断文件是否存在，并且是否拥有读权限。                       |
| -w filename             | 判断文件是否存在，并且是否拥有写权限。                       |
| -x filename             | 判断文件是否存在，并且是否拥有执行权限。                     |
| -u filename             | 判断文件是否存在，并且是否拥有 SUID 权限。                   |
| -g filename             | 判断文件是否存在，并且是否拥有 SGID 权限。                   |
| -k filename             | 判断该文件是否存在，并且是否拥有 SBIT 权限。                 |
| 文件比较                |                                                              |
| 选 项                   | 作 用                                                        |
| filename1 -nt filename2 | 判断 filename1 的修改时间是否比 filename2 的新。             |
| filename -ot filename2  | 判断 filename1 的修改时间是否比 filename2 的旧。             |
| filename1 -ef filename2 | 判断 filename1 是否和 filename2 的 inode 号一致，可以理解为两个文件是否为同一个文件。这个判断用于判断硬链接是很好的方法 |



- 与数值比较相关的test选项

| 选 项         | 作 用                          |
| ------------- | ------------------------------ |
| num1 -eq num2 | 判断 num1 是否和 num2 相等。   |
| num1 -ne num2 | 判断 num1 是否和 num2 不相等。 |
| num1 -gt num2 | 判断 num1 是否大于 num2 。     |
| num1 -lt num2 | 判断 num1 是否小于 num2。      |
| num1 -ge num2 | 判断 num1 是否大于等于 num2。  |
| num1 -le num2 | 判断 num1 是否小于等于 num2。  |



- 字符串相关

| 选 项                    | 作 用                                                        |
| ------------------------ | ------------------------------------------------------------ |
| -z str                   | 判断字符串 str 是否为空。                                    |
| -n str                   | 判断宇符串 str 是否为非空。                                  |
| str1 = str2 str1 == str2 | `=`和`==`是等价的，都用来判断 str1 是否和 str2 相等。        |
| str1 != str2             | 判断 str1 是否和 str2 不相等。                               |
| str1 \> str2             | 判断 str1 是否大于 str2。`\>`是`>`的转义字符，这样写是为了防止`>`被误认为成重定向运算符。 |
| str1 \< str2             | 判断 str1 是否小于 str2。同样，`\<`也是转义字符。            |



- 逻辑运算

| 选 项                      | 作 用                                                        |
| -------------------------- | ------------------------------------------------------------ |
| expression1 -a expression  | 逻辑与，表达式 expression1 和 expression2 都成立，最终的结果才是成立的。 |
| expression1 -o expression2 | 逻辑或，表达式 expression1 和 expression2 有一个成立，最终的结果就成立。 |
| !expression                | 逻辑非，对 expression 进行取反。                             |





test \>、<、== 只能用来比较字符串，不能用来比较数字，比较数字需要使用 -eq、-gt 等选项；不管是比较字符串还是数字，test 都不支持 >= 和 <=。有经验的程序员需要慢慢习惯 test 命令的这些奇葩用法。

整型数字的比较，建议使用 (())



#### [[]]

test 能做到的，[[ ]] 也能做到，而且 [[ ]] 做的更好

[[ ]] 剔除了 test 命令的`-o`和`-a`选项，你只能使用 || 和 &&



```shell
#!/bin/bash
read str1
read str2
if [[ -z $str1 ]] || [[ -z $str2 ]]  #不需要对变量名加双引号
then
    echo "字符串不能为空"
elif [[ $str1 < $str2 ]]  #不需要也不能对 < 进行转义
then
    echo "str1 < str2"
else
    echo "str1 >= str2"
fi
```



正则

```shell
#用法 [[ str =~ regex ]]

#!/bin/bash
read tel
if [[ $tel =~ ^1[0-9]{10}$ ]]
then
    echo "你输入的是手机号码"
else
    echo "你输入的不是手机号码"
fi
```



#### case in

```shell
case expression in
    pattern1)
        statement1
        ;;
    pattern2)
        statement2
        ;;
    pattern3)
        statement3
        ;;
    ……
    *)
        statementn
esac
```

#### while

```shell
while condition
do
    statements
done
```



例：

```shell
#!/bin/bash
i=1
sum=0
while ((i <= 100))
do
    ((sum += i))
    ((i++))
done
echo "The sum is: $sum"
```



Until:

```shell
until condition
do
    statements
done
```



#### for

```shell
for((exp1; exp2; exp3))
do
    statements
done
```



```shell
#!/bin/bash
sum=0
i=1
for ((; i<=100; i++))
do
    ((sum += i))
done
echo "The sum is: $sum"
```



python风格

```shell
sum=0
for n in 1 2 3 4 5 6
do
    echo $n
     ((sum+=n))
done
echo "The sum is "$sum
```



```shell
sum=0
for n in {1..100}
do
    ((sum+=n))
done
echo $sum
```



使用命令的执行结果：

```shell
sum=0
for n in $(seq 2 2 100)
do
    ((sum+=n))
done
echo $sum
```

使用shell通配符：

```shell
for filename in *.sh
do
    echo $filename
done
```

使用特殊变量：

```shell
function func(){
    for str in $@
    do
        echo $str
    done
}
func C++ Java Python C#
```

若省略 in $@，效果一样

#### select in

select 是无限循环（死循环），输入空值，或者输入的值无效，都不会结束循环，只有遇到 break 语句，或者按下 Ctrl+D 组合键才能结束循环。

```shell

echo "What is your favourite OS?"
select name in "Linux" "Windows" "Mac OS" "UNIX" "Android"
do
    echo $name
done
echo "You have selected $name"
```



select in 常和case in一起用

```shell
echo "What is your favourite OS?"
select name in "Linux" "Windows" "Mac OS" "UNIX" "Android"
do
    case $name in
        "Linux")
            echo "Linux是一个类UNIX操作系统，它开源免费，运行在各种服务器设备和嵌入式设备。"
            break
            ;;
        "Windows")
            echo "Windows是微软开发的个人电脑操作系统，它是闭源收费的。"
            break
            ;;
        "Mac OS")
            echo "Mac OS是苹果公司基于UNIX开发的一款图形界面操作系统，只能运行与苹果提供的硬件之上。"
            break
            ;;
        "UNIX")
            echo "UNIX是操作系统的开山鼻祖，现在已经逐渐退出历史舞台，只应用在特殊场合。"
            break
            ;;
        "Android")
            echo "Android是由Google开发的手机操作系统，目前已经占据了70%的市场份额。"
            break
            ;;
        *)
            echo "输入错误，请重新输入"
    esac
done
```





