# Operator Overloading in Cpp



## 1 概述

C++与Java最不同的地方在我看来可能就是操作符重载了，Java对于自定义类的加减法操作可能需要通过使用自定义的 `add()` 和 `sub()` 方法来实现，而在 C++ 中，可以通过简单的操作符重载来赋予相同的符号以不同的意义（<span style='color: red'>**操作符重载也是一种函数重载**</span>）。这是 C++ 这个语言给予开发者的一个极大的权利与特性，让开发者能够根据自己的设想来实现不同的操作符，但是其也有一定的坏处，是否能够很好地使用这种特性考验的还是开发者的水平。

## 2 能够重载的符号

带来了较为方便快捷的操作，在 C++ 中，能够被重载的操作符有以下几种：

| Expression |  As member function  | As non-member function |                           Example                            |
| :--------: | :------------------: | :--------------------: | :----------------------------------------------------------: |
|     @a     |  (a).operator@ ( )   |     operator@ (a)      |           `std::cin` calls `std::cin.operator!()`            |
|    a@b     |  (a).operator@ (b)   |    operator@ (a, b)    |     `std::cout << 42` calls `std::cout.operator << (42)`     |
|    a=b     |  (a).operator= (b)   |  cannot be non-member  | Given `std::string s;, s = "abc";` calls `s.operator=("abc")` |
|  a(b...)   | (a).operator()(b...) |  cannot be non-member  | Given `std::random_device r;, auto n = r();` calls `r.operator()()` |
|    a[b]    |  (a).operator[](b)   |  cannot be non-member  | Given `std::map<int, int> m;, m[1] = 2;` calls `m.operator[](1)` |
|    a->     |  (a).operator-> ( )  |  cannot be non-member  | Given `std::unique_ptr<S> p;, p->bar()` calls `p.operator->()` |
|     a@     |  (a).operator@ (0)   |    operator@ (a, 0)    | Given `std::vector<int>::iterator i;, i++` calls `i.operator++(0)` |

> in this table, `@` is a placeholder representing all matching operators: all prefix operators in @a, all postfix operators other than -> in a@, all infix operators other than = in a@b

需要注意的有以下几点：

1. 如果你重载了 `&&` 或者 `||`，那么这两个运算符会失去<span style='color: red'>**“短路特性”**</span>。

	> 什么是短路特性：比如一个判断语句：`if (1 == 0 && 0 == 0)` 此时 `1 == 0` 不成立的情况下，原生的 `&&` 为了性能会直接跳过下一个条件返回 `false`，这被称为短路特性。

2. 以下几种操作符不能够被重载：`::`、`.`、`.*`、`?:`。

3. 重载运算符并不能够改变运算符的优先级或者操作符的数量

4. 运算符 `->` 的重载必须要么返回一个原始指针，要么返回一个对象（按引用或按值），运算符 `->` 又为其重载。



## 3 例子

我们通过一个例子来看一下 C++ 中典型的操作符重载是如何实现的，通过一个自定义的复数类来作为例子：

```cpp
/**
 * @file operatorOverloading.cpp
 * @author ZY in NJU (201250182@smail.nju.edu.cn)
 * @brief Brief Introduction of Cpp Operator Overloading
 * @version 0.1
 * @date 2022-05-11
 * 
 * @copyright Copyright (c) 2022
 * 
 */
#include <iostream>

class Complex {
private:
    double m_Real {0.0};
    double m_Image {0.0};
public:
    /**
     * @brief Construct a new Complex object
     * 
     * @param real 实部
     * @param image 虚部
     */
    Complex(double real = 0.0, double image = 0.0)
        : m_Real(real), m_Image(image)
    {}

    /**
     * @brief operator = overloading
     * 
     * @param complex anthoer complex
     * @return Complex& complex object reference
     */
    Complex& operator=(const Complex& complex)
    {
        m_Real = complex.m_Real;
        m_Image = complex.m_Image;
        return *this;
    }

    /**
     * @brief prefix ++
     * 
     * @return Complex& complex object reference 
     */
    Complex& operator++()
    {
        m_Real++;
        m_Image++;
        return *this;
    }

    /**
     * @brief postfix ++, int is dummy parameter
     * 
     * @return Complex& comlex object reference
     */
    Complex& operator++(int)
    {
        // 拷贝构造
        Complex temp(*this);
        m_Real++;
        m_Image++;
        // 返回的是拷贝的老对象
        return temp;

        // 返回但是自身++了
    }

    // 友元函数 << >> 的操作符重载
    // 如果直接在类中重载会与原先的操作符写法不一样
    friend std::ostream& operator<<(std::ostream& out, const Complex& complex);
    friend std::istream& operator>>(std::istream& in, Complex& complex);
};

std::ostream& operator<<(std::ostream& out, const Complex& complex)
{
    out << complex.m_Real << "+" << complex.m_Image << "i";
    return out;
}

/**
 * @brief operator >> overloading
 * 
 * @param in in stream, represent cin
 * @param complex comlex object
 * @return std::istream& istream object reference, due to link call
 */
std::istream& operator>>(std::istream& in, Complex& complex)
{
    in >> complex.m_Real >> complex.m_Image;
    return in;
}

/**
 * @brief main function
 * 
 * @return int exit code
 */
int main()
{
    Complex c;
    std::cin >> c;
    std::cout << c << std::endl;
    Complex c1 = c++;
    std::cout << c << std::endl;
    std::cout << c1 << std::endl;
    Complex c2 = ++c;
    std::cout << c << std::endl;
    std::cout << c2 << std::endl;

    return 0;
}
```

需要注意的有几点：

1. 对于 `<<` 和 `>>` 的重载，实际上重载的是 `ostream` 与 `instream` 对该类的操作符，所以需要定义为友元函数，才能够访问该类的成员变量，如果不定义为友元函数进行操作符重载，而是直接在类中进行重载，则写法就会变成 `complext << cin`。

	> 注意函数定义：
	>
	> ```cpp
	> std::ostream& operator<<(std::ostream& out const Complex& complext); // cout不需要更改原有内容，所以设为const变量
	> ```

2. 前缀++与后缀++的区别：我们会发现后缀++有一个没有名字的参数，其实是为了区分前缀++与后缀++的，该参数其实没有任何的作用，被称为“Dummy Parameter”。同时，后缀++在实现的时候经过了一次**拷贝构造**，然后对原有对象进行自增，然后返回拷贝构造的对象，这也是为什么我们认为后缀++是在当前语句执行完后才会对值进行改变的一个原因，不是因为它在语句完之后才调用了后缀++函数，而是因为它返回的是一个++前的拷贝构造出的对象。



## 4 prefix and postfix ++

这里主要是讲一个比较有趣的点，我们会看到很多 C++ 程序员在写循环的时候，喜欢以下的写法：

```cpp
for (int i = 0; i < n ++i)
{
    // do something
}
```

其实这就跟上面讲到的前缀++和后缀++有关，后缀++由于进行了一次拷贝，所以性能上会比前缀++稍微差一点。



## 5 new and delete

与其他操作符一样，`new` 和 `delete` 也可以被重载，他们可以被全局重载或者被特定的类重载（作为类的成员方法）

1. 如果被全局重载了，那么所有对象的 `new` 和 `delete` 都会调用全局重载后的 `new` 和 `delete`。
2. 如果只是被特定的类作为成员方法重载了，那么只有该类在创建对象或释放对象时才会调用重载的 `new` 和 `delete`

**Syntax for overloading the new operator**

```cpp
void* operator new(size_t size);
```

重载的 `new` 运算符接收 `size_t` 类型的大小，它指定要分配的内存字节数（会自动分配并传参，不需要手动传参）。重载的 `new` 的返回类型必须是 `void*`。重载的函数返回一个指针，指向分配的内存块的开头。

**Syntax for overloading the delete operator**

```cpp
void opeartor delete(void*);
```

该函数接收一个 `void*` 类型的参数，该参数必须被删除(deallocate)。函数不应该返回任何东西。

> 需要注意的是，`new` 和 `delete` 都是作为静态成员来重载的，但是它们不需要被显式声明为 `static`，同时它们的参数也没有隐藏的 `this` 指针。

下面我们来看一个 `new` 和 `delete` 作为成员方法被重载的例子：

```cpp
// CPP program to demonstrate
// Overloading new and delete operator
// for a specific class
#include <iostream>
#include <cstdlib>

class Student {
private:
    std::string m_Name;
    int m_Age;
public:
    Student()
    {
        std::cout << "Constructor is called" << std::endl;
    }
    
    Student(std::string name, int age)
        : m_Name(name), m_Age(age)
    {}
    
    void display();
    void* operator new(size_t size);
    void operator delete(void* p);
};

void Student::display()
{
    std::cout << "Name: " << m_Name << std::endl;
    std::cout << "Age: " << m_Age << std::endl;
}

void* Student::operator new(size_t size)
{
    std::cout << "Overloading new opeartor with size: " << size << std::endl;
    void* p = ::operator new(size);
    // malloc will also work fine
    // void* p = malloc(size);
    return p;
}

void Student::operator delete(void* p)
{
    std::cout << "Overloading delete operator" << std::endl;
    free(p);
}

int main()
{
    Student* p = new Student("ZY", 21);
    p->display();
    
    delete p;
    return 0;
}
```

```bash
Overloading new opeartor with size: 40
Name: ZY
Age: 21
Overloading delete operator
```

> 需要注意的是上面的 `::operator new(size)` 这其实是调用了全局的 `new` 操作符，如果不调用全局的话则会陷入无限递归当中。



下面来看一个 `new` 和 `delete` 作为全局函数被重载的例子：

```cpp
// CPP program to demonstrate
// Global overloading of
// new and delete operator
#include <iostream>
#include <cstdlib>

void* operator new(size_t size)
{
    std::cout << "New operator overloading" << std::endl;
    // only use malloc
    void* p = malloc(size);
    return p;
}

void operator delete(void* p)
{
    std::cout << "Delete operator overloading" << std::endl;
    free(p);
}

int main()
{
    int n = 5;
    int* arr = new int[n];

    for (int i = 0; i < n; i++)
    {
        arr[i] = i;
        std::cout << arr[i] << " ";
    }
    std::cout << std::endl;

    delete[] arr;
}
```

Output:

```bash
New operator overloading
0 1 2 3 4
Delete operator overloading
```

> 需要注意的是，这里只能用 `malloc` 来申请内存，因为全局的 `new` 函数调用会陷入无线递归。



### 5.1 为什么需要重载 new 和 delete

`new` 操作符的重载是可以接受参数的，因此，一个类可以**有多个被重载的 `new`** 方法，比如我们可以：

```cpp
void* operator new(size_t size, char c)
{
    void* ptr = malloc(size);
    if (ptr != null)
    {
        *ptr = c;
    }
    
    return ptr;
}

int main()
{
    char* ch = new('#') char;
}
```





## Reference

1. [CppReference](https://en.cppreference.com/w/cpp/language/operators)
1. [Overloading new delete operator in C++](https://www.geeksforgeeks.org/overloading-new-delete-operator-c/)