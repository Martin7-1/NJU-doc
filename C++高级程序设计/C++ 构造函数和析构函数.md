# C++ 构造函数和析构函数

## 类的构造函数

### 定义

1. 类的<span style = 'color: red'>**构造函数**</span>是类的一种特殊的成员函数，它会在每次创建类的新对象时执行。
2. 构造函数的名称与类的名称是完全相同的，并且不会返回任何类型，也不会返回void

下面实例有助于更好地理解构造函数的概念：

```cpp
#include <iostream>

class Line {
public:
    void setLenth(double len);
    double getLength(void);
    Line(); // 这是构造函数
private:
    double length;
};

// 成员函数定义，包括构造函数
Line::Line() {
    std::cout << "Object is being created" << endl;
}

void Line::setLength(double len) {
    length = len;
}

double Line::getLength() {
    return length;
}

// 程序的主函数
int main() {
    Line *line = new Line();
    
    // 设置长度
    line.setLength(6.0);
    std::cout << "Length of line: " << line.getLength() << std::endl;
    
    return 0;
}
```



### 带参数的构造函数

如果需要，构造函数也可以带有参数。这样在创建对象时就会有给对象赋初始值。

```cpp
#include <iostream>

class Line {
public:
    void setLenth(double len);
    double getLength(void);
    Line(double len); // 这是构造函数
private:
    double length;
};

// 成员函数定义，包括构造函数
Line::Line(double len) {
    std::cout << "Object is being created.Length = " << len << endl;
    length = len;
}

void Line::setLength(double len) {
    length = len;
}

double Line::getLength() {
    return length;
}

// 程序的主函数
int main() {
    Line *line = new Line();
    
    // 设置长度
    line.setLength(6.0);
    std::cout << "Length of line: " << line.getLength() << std::endl;
    
    return 0;
}
```

### 使用初始化列表来初始化字段

```cpp
Line::Line(double len): length(len) {
    std::cout << "Object is being created, length = " << len << std::endl;
}
```

上面的语法如同以下的语法：

```cpp
Line::Line(double len) {
    length = len;
    cout << "Object is being created, length = " << len << std::endl;
}
```

> 假设有一个类C，具有多个字段X，Y，Z等需要进行初始化，同理地，可以用逗号分隔来进行初始化。
>
> ```cpp
> C::C(double a, double b, double c): X(a), Y(b), Z(c) {
>     // code here
> }
> ```



## 类的析构函数

### 定义

1. 类的<span style = 'color: red'>**析构函数**</span>是类的一种特殊的成员函数，它会在每次删除所创建的对象时执行。
2. 析构函数的名称与类的名称是完全相同的，只是在前面加了个<span style = 'color: red'>**波浪号（~）**</span>。它需要满足下列的条件：
	1. 它不会返回任何值
	2. 它不能带有任何的参数
3. 析构函数的作用：有助于在跳出程序（比如关闭文件、释放内存等）前释放资源。

下面的实例有助于更好的理解析构函数：

```cpp
#include <iostream>

class Line
{
   public:
      void setLength( double len );
      double getLength();
      Line();   // 这是构造函数声明
      ~Line();  // 这是析构函数声明
 
   private:
      double length;
};
 
// 成员函数定义，包括构造函数
Line::Line()
{
    std::cout << "Object is being created" << std::endl;
}
Line::~Line()
{
    std::cout << "Object is being deleted" << std::endl;
}
 
void Line::setLength(double len)
{
    length = len;
}
 
double Line::getLength()
{
    return length;
}
// 程序的主函数
int main( )
{
   Line line;
 
   // 设置长度
   line.setLength(6.0); 
   std::cout << "Length of line : " << line.getLength() << std::endl;
}
```



## 拷贝构造函数

