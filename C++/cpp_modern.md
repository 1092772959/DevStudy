## C++ 17

#### Structure bindings

##### c++11/14

```cpp
#include <bits/stdc++.h>
#include <tuple>
using namespace std;

// Creating a structure named Point
struct Point
{
	int x, y;
	// Default Constructor
	Point() : x(0), y(0) { }
	// Parameterized Constructor for Init List
	Point(int x, int y) : x(x), y(y) {}
	auto operator()() {
		// returns a tuple to make it work with std::tie
		return make_tuple(x, y);
	}
};

// Driver code
int main() {
	Point p = {1, 2};
	int x_coord, y_coord;
	tie(x_coord, y_coord) = p(); //use the tie keyword
	cout << "X Coordinate : " << x_coord << endl;
	cout << "Y Coordinate : " << y_coord << endl;
	return 0;
}
```

##### c++17

```cpp
struct Point;

void test_cpp17(){
  auto [x, y] = Point{1, 2};
  cout << "X coordinate: " << x << endl;
  cout << "Y coordinate: " << y << endl;
}


struct MyStruct {
    int i;
    std::string s;
};
MyStruct getStruct() {
    return MyStruct{42, "hello"};
}
auto [id, val] = getStruct(); // id = i, val = s
//数组也适用
int arr[] = {1, 2};
auto [x, y] = arr;
```



##### visit map

```cpp
#include <bits/stdc++.h>
#include <map>
using namespace std;

int main() {
	// Creating a map with key and value
	// fields as String
	map<string, string> sites;
	
	sites.insert({ "GeeksforGeeks", "Coding Resources" });
	sites.insert({ "StackOverflow", "Q-A type" });
	sites.insert({ "Wikipedia", "Resources + References" });

	for (auto & [ key, value ] : sites) {
	cout << key.c_str() << " " << value.c_str() << endl;
	}
	return 0;
}
```



Conditon: 

1. each variable in the struct/class is public
2. each location tyep like`tuple-like` is : `std::pair`, `std::tuple`, `std::array`
3. must bind each element of the returned object



#### inline variable

##### Before

if we want to define some constant variables in a header file and then use them in other source files, we will do like this:

```c++
#ifndef CONSTANTS_H
#define CONSTANTS_H
 
// define your own namespace to hold constants
namespace constants
{
    // constants have internal linkage by default
    constexpr double pi { 3.14159 };
    constexpr double avogadro { 6.0221413e23 };
    constexpr double my_gravity { 9.2 }; // m/s^2 -- gravity is light on this planet
    // ... other related constants
}
#endif
```

- Downsides:

- every time constants.h gets #included into a different code file, each of these variables is **copied into the including code file**. Therefore, if constants.h gets included into 20 different code files, each of these variables is duplicated 20 times. If constants are large, they can use a lot of memory.
- Changing a single constants requires recompiling every file that includes the constant header file.



##### Alternative before c++17

- header file

```cpp
#ifndef CONSTANTS_H
#define CONSTANTS_H
namespace constants
{
    // since the actual variables are inside a namespace, the forward declarations need to be inside a namespace as well
    extern const double pi;
    extern const double avogadro;
    extern const double my_gravity;
}
#endif
```

- source file

```c++
#include "constants.h"
namespace constants
{
    // actual global variables
    extern const double pi { 3.14159 };
    extern const double avogadro { 6.0221413e23 };
    extern const double my_gravity { 9.2 }; // m/s^2 -- gravity is light on this planet
}
```

Now the symbolic constants will get instantiated only once (in `constants.cpp`), instead of once every time `constants.h` is #included, and the other uses will simply refer to the version in `constants.cpp`. Any changes made to `constants.cpp` will require recompiling only `constants.cpp`.



##### Downside

- these constants are now considered compile-time constants only within the file they are actually defined in (`constants.cpp`), not anywhere else they are used. This means that outside of `constants.cpp`, they can’t be used anywhere that requires a compile-time constant. So, they cannot be used as `array sizes`



 In order for variables to be usable in compile-time contexts, such as array sizes, the compiler has to see the variable’s definition. Because the compiler compiles each source file individually, it can only see variable definition that appear in the active source file or in one of the included headers. Variable definitions in `constants.cpp` are not visible when the compiler compiles `main.cpp`. For this reason, `constexpr` variables **cannot be separated into header and source file**, they have to be defined in the header file.



##### Global constants as inline variables

```c++
#ifndef CONSTANTS_H
#define CONSTANTS_H
 
// define your own namespace to hold constants
namespace constants
{
    inline constexpr double pi { 3.14159 }; // note: now inline constexpr
    inline constexpr double avogadro { 6.0221413e23 };
    inline constexpr double my_gravity { 9.2 }; // m/s^2 -- gravity is light on this planet
    // ... other related constants
}
#endif
```

C++17 introduced a new concept called `inline variables`. In C++, the term `inline` has evolved to mean “multiple definitions are allowed”. Thus, an **inline variable** is one that is allowed to be defined in multiple files without violating the one definition rule. Inline global variables have external linkage by default.

Inline variables have two primary restrictions that must be obeyed:
1) All definitions of the inline variable must be **identical** (otherwise, undefined behavior will result).
2) The inline variable definition (not a forward declaration) must be present in any file that uses the variable.



#### If init expression

In many cases, we have to check the returned value from functions and decide what to do. The code will be like:

```cpp
auto res = func();
if(res != nullptr){
  // normal process
}else{
  // error handler
}
```



In c++17, you can write code like this:

```cpp
// if (initialization; condition) {...}
// os的生存期持续到if和else的结束
if (std::ofstream os = open_log_file(); coll.empty()) {
    os << "<no data>\n";
} else {
    for (const auto& elem : coll) {
        os << elem << '\n';
    }
}
//before c++17
{
    std::lock_gurard<std::mutex> lck{mtx};
    if (!coll.empty()) {
        std::cout << coll.front() << '\n';
    }
}
//now, c++17支持类模板类型自动推导, 不需要显示写std::lock_gurard<std::mutex>
if (std::lock_guard lck(mtx); !coll.empty()) {
    std::cout << coll.front() << '\n';
}
//before c++17
auto ret = coll.insert({"new", 42});
if (!ret.second) {
    const auto& elem = *(ret.first);
    std::cout << "already there: " << elem.first << '\n';
}
//now
if (auto [pos, ok] = coll.insert({"new", 42}); !ok) {
    const auto& [key, val] = *pos;
    std::cout << "already there: " << key << '\n';
}
```





##### namespace 

```cpp
//before C++17
namespace A {
namespace B {
namespace C {
    ...
}
}
}
//C++17
namespace A::B::C {
    ...
}
```

