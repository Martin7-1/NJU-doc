# C++ ENUM枚举

## 枚举类型的定义

​	枚举类型(enumeration)是`C++`中的一种派生数据类型，它是由用户定义的若干枚举常量和集合。

### 定义格式

```c++
enum <类型名> {<枚举常量表>};
```

### 格式说明

- 关键字enum——指明其后的标识符是一个枚举类型的名字。
- 枚举常量表——由**枚举常量**构成。"枚举常量"或称"枚举成员"，是以标识符形式表示的**整型量**，表示枚举类型的取值。枚举常量表列出枚举类型的所有取值，**各枚举常量之间以"，"间隔，且必须各不相同**。取值类型与条件表达式相同。

**重要提示**

​	枚举常量代表该枚举类型的变量可能取的值，**编译系统为每个枚举常量指定一个整数值，默认状态下，这个整数就是所列举元素的序号**，序号从0开始。 可以在定义枚举类型时为部分或全部枚举常量指定整数值，在指定值之前的枚举常量仍按默认方式取值，而指定值之后的枚举常量按依次加1的原则取值。 各枚举常量的值可以重复。例如：

```cpp
enum fruit_set {apple, orange, banana=1, peach, grape}
//枚举常量apple=0,orange=1, banana=1,peach=2,grape=3。
```

```cpp
enum week {Sun=7, Mon=1, Tue, Wed, Thu, Fri, Sat};
//枚举常量Sun,Mon,Tue,Wed,Thu,Fri,Sat的值分别为7、1、2、3、4、5、6。
```

**Tips:**

枚举常量只能以标识符形式表示，不能够是整型、字符型等文字常量。

```cpp
enum letter_set {'a', 'd'} // 定义非法
enum year_set {2000, 2001} // 定义非法
```



## 枚举变量的使用

定义枚举类型的主要目的是：增加程序的可读性。枚举类型最常见也最有意义的用处之一就是用来描述状态量。

定义格式：定义枚举类型之后，就可以定义该枚举类型的变量，如：

```cpp
color_set colorOne, colorTwo;
```

### 相关操作

1. 枚举变量的值只能取枚举常量表中所列的值，就是整型数的一个子集。

2. 枚举变量占用内存的大小与整型数相同。

3. 枚举变量只能参与赋值和关系运算以及输出操作，参与运算时用其本身的整数值。例如，设有定义：

```cpp
enum color_set_one {RED, BLUE, WHITE, BLACK} colorOne, colorTwo;
enum color_set_two {GREEN, RED, YELLOW, WHITLE} colorThree, colorFour;

// 则可以进行的操作赋值如下
colorThree = RED;           //将枚举常量值赋给枚举变量
colorFour = colorThree;     //相同类型的枚举变量赋值，colorFour的值为RED
int i = colorThree;        //将枚举变量赋给整型变量，i的值为1
int j = GREEN;         //将枚举常量赋给整型变量，j的值为0

// 允许的关系运算有：==、<、>、<=、>=、!=等
// 比较同类型枚举变量colorThree，colorFour是否相等
if (colorThree == colorFour) {
    std::cout << "相等" << std::endl；
}
// 输出的是变量colorThree与WHITE的比较结果，结果为1
std::cout << colorThree < WHITE;
```



### 重要提示

- 枚举变量可以直接输出，但不能直接输入。如：`std::cout >> colorThree;  //非法`
- 不能直接将常量赋给枚举变量。如： `colorOne = 1; //非法`
- 不同类型的枚举变量之间不能相互赋值。如： `colorOne = colorThree; //非法`
- 枚举变量的输入输出一般都采用 `switch` 语句将其转换为字符或字符串；枚举类型数据的其他处理也往往应用 `switch` 语句，以保证程序的合法性和可读性。
