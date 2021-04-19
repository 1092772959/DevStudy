### 编译

##### 查看库文件搜索路径

```
gcc -print-search-dirs | grep libraries | awk -F'=' '{print $2}' | awk -F ':' '{for(i=1;i<=NF;i++) {print $i}}'
```





### 链接器

>链接器的作用，就是完成变量、函数符号和其地址绑定这样的任务。而这里我们所说的符号，就可以理解为变量名和函数名。
>
>**那为什么要让链接器做符号和地址绑定这样一件事儿呢？不绑定的话，又会有什么问题？**
>
>如果地址和符号不做绑定的话，要让机器知道你在操作什么内存地址，你就需要在写代码时给每个指令设好内存地址。
>
>链接器在链接多个目标文件的过程中，会创建一个符号表，用于记录所有已定义的和所有未定义的符号。链接时如果出现相同符号的情况，就会出现“ld: dumplicate symbols”的错误信息；如果在其他目标文件里没有找到符号，就会提示“Undefined symbols”的错误信息。
>
>
>
>说完了链接器解决的问题，我们再一起来看看**链接器对代码主要做了哪几件事儿。**
>
>- 去项目文件里查找目标代码文件里没有定义的变量。
>- 扫描项目中的不同文件，将所有符号定义和引用地址收集起来，并放到全局符号表中。
>- 计算合并后长度及位置，生成同类型的段进行合并，建立绑定。
>- 对项目中不同文件里的变量进行地址重定位。
>
>

#### 链接

> 对动态库而言，在编译阶段链接器(linker)并不会执行真正的链接动作，只是检查代码中所需的符号(函数、变量等)能否在动态库中找到，一旦找到便将库文件的元信息记录到应用程序中，直到所有的符号都被找到，相关的动态库信息都被记录到了应用程序中，链接动作可以便完成了，真正的链接动作在程序运行时由动态加载器(loader)来进一步完成。
>
> 对静态库而言，在编译阶段链接器(linker)依次分析每一个静态库，遍历静态库中的每一个.o文件查找代码中所需的符号，确认包含了待解析的符号，便把该.o文件写入到最终应用程序中，等到所有符号对应的.o文件都被写入到应用程序中，链接动作便结束了，程序在运行时不需要进行额外的链接动作了。
>
> 如果应用程序在编译时依赖了动态库，启动时需要先加载动态库完成链接操作，应用程序才能正常启动。需要注意的是编译时指定的库文件搜索路径与运行时的库文件搜索路径没有任何关系。
>
> 操作系统动态库搜索路径顺序：①LD_RELOAD -> ②RPATH --> ③LD_LIBRARY_PATH

##### LD_PRELOAD

预加载库拥有最高的搜索优先级，加载器loader首先加载这个库。

**LD_RUN_PATH**

LD_RUN_PATH是RPATH对应的环境变量，用于指定程序启动时动态库的加载路径，既可以在编译阶段指定，也可以在运行阶段指定。推荐在编译阶段指定，可以减少服务运维的难度和成本。

#### Internal/External Linkage

##### Scope

Scope of an identifier is the part of the program where the identifier may directly be accessible. In C, all identifiers are lexically (or statically) scoped.

##### Linkage

Linkage describes how names can or can not refer to the same entity throughout the whole program or one single translation unit.
The above sounds similar to Scope, but it is not so. To understand what the above means, let us dig deeper into the compilation process.

##### Translation unit

A translation unit is a file containing source code, header files and other dependencies. All of these sources are grouped together in a file for they are used to produce one single executable object. It is important to link the sources together in a meaningful way. For example, the compiler should know that `printf` definition lies in `stdio`header file(really? not delaration?).

##### Internal linkage

An identifier implementing internal linkage is not accessible outside the translation unit it is declared in. Any identifier within the unit can access an identifier having internal linkage. It is implemented by the keyword `static`. An internally linked identifier is stored in initialized or uninitialized segment of RAM.

Another property of internal linkage is that it is **only implemented when the variable has global scope**, and all constants are by default internally linked.

- Animals.cpp

```cpp
// C code to illustrate Internal Linkage
#include <stdio.h>

static int animals = 8;
const int i = 5;

int call_me(void)
{
	printf("%d %d", i, animals);
}
```

- Feed.cpp

```cpp
// C code to illustrate Internal Linkage
#include <stdio.h>

int main()
{
	call_me();
	animals = 2;
	printf("%d", animals);
  call_me();
	return 0;
}

/*
output:
5 8 2 5 8

*/
```



**Usage**: As we know, an internally linked variable is passed by copy. Thus, if a header file has a function `fun1()` and the source code in which it is included in also has `fun1()` but with a different definition, then the 2 functions will not clash with each other. Thus, we commonly use internal linkage to hide translation-unit-local helper functions from the global scope. For example, we might include a header file that contains a method to read input from the user, in a file that may describe another method to read input from the user. Both of these functions are independent of each other when linked.

##### 

##### Internal Linkage

An identifier implementing **external linkage is visible** to **every translation unit**. Externally linked identifiers are *shared* between translation units and are considered to be located at the outermost level of the program. In practice, this means that you must define an identifier in a place which is visible to all, such that it has only one visible definition. It is the default linkage for globally scoped variables and functions. Thus, all instances of a particular identifier with external linkage refer to the same identifier in the program. The keyword `extern` implements external linkage.

When we use the keyword `extern`, we tell the linker to look for the definition elsewhere. Thus, the declaration of an externally linked identifier does not take up any space. `Extern`identifiers are generally stored in initialized/uninitialized or text segment of RAM.

```cpp
// C code to illustrate External Linkage
#include <stdio.h>

void foo()
{
	int a;
	extern int b; // line 1
}

void bar()
{
	int c;
	c = b; // error
}

int main()
{
	foo();
	bar();
}

/**
Error: 'b' was not declared in this scope
*/
```

Explanation: Although the variable b is declared as external in the same file of fun bar, it does not have a definition. So, during the compiling stage, it is ok. It trusts that there is a definition of `b` somewhere and lets the linker handle the rest. However, the same compiler will go through the `bar()` function and try to find variable `b`. Since `b` has been declared `extern`, it has not been given memory yet by the compiler; it does not exist yet.

But during linkage state, the linker cannot find the definition of variable `b`. Then, it comes out the linkage error above.



#### 静态库

C语言运行库glibc中包含了很多跟系统调用相关的代码，比如输入输出、文件操作、时间管理、内存管理等。glibc本身使用c语言开发的开发的，由上千个c语言源文件组成，编译后有相同数量的目标文件，比如输入输出有printf.o，scans.o，文件操作有fread.o，fwrite.o。如果把这些零散的文件直接提供给使用者，会造成文件传输、组织和管理的不便。把这些目标文件压缩在一起，对其进行编号和索引，以便查找和检索，方便使用，这便形成了libc.a，因此静态库是目标文件(.o)的集合。



linux系统下常用的命令基本上全部依赖了操作系统的运行库libc，如果每个命令在编译阶段采用静态链接，则每个命令都存在一份libc的副本，不仅会造成OS文件体积大幅度增加，运行时也会占用更多的内存，造成了极大的浪费。



#### 动态库

如果动态库在编译阶段连接了某个静态库，该静态库需在编译需要指定-fPIC参数，否则会出现"recompile with -fPIC"的错误提示