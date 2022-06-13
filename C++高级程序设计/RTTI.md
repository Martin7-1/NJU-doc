# RTTI

> Run-time type information

## 1 Overview

在 C++ 中，RTTI（运行时类型信息）是一种在运行时公开对象数据类型信息的机制，仅适用于**具有至少一个虚函数**的类。它允许在程序执行期间确定对象的类型。

## 2 Runtime Casts

运行时强制转换检查强制转换是否有效，是使用指针或引用确定对象的运行时类型的最简单方法。当我们需要将指针从基类转换为派生类型时，这尤其有用。在处理类的继承层次结构时，通常需要对对象进行强制转换。有两种类型的铸造：

1. 向上转型：一个派生类的指针或者引用被当作基类的指针对待
2. 向下转型：一个基类的指针或者引用呗转换为派生类的指针

我们可以通过 C++ 中的 `std::dynamic_cast<>` 来进行运行时的类型转换，可以通过使用它来将一个基类的指针变成派生类的指针，如果成功的转换了，那么它会返回指定转换的类型的指针；如果我们想要强制转换的类型并不是某个类的派生类，那么就会转换失败。

比如下面的程序就是错误的，因为在基类 `B` 中没有**虚函数**，错误信息如下：

```bash
cannot dynamic_cast `b’ (of type `class B*’) to type `class D*`(source type is not polymorphic)
```

```cpp
#include <iostream>

class B {};

class D : public B {};

int main()
{
    // base class pointer
    B* b = new D;
    // derived class pointer
    D* d = std::dynamic_cast<D*>(b);

    if (d != nullptr)
    {
        std::cout << "works";
    }
    else
    {
        std::cout << "can not cast B* to D*";
    }

    getchar();
    return 0;
}
```

如果我们在基类中加了虚函数，那么就能够成功转换。

```cpp
#include <iostream>

class B {

    virtual void fun() {}
};

class D : public B {};

int main()
{
    // base class pointer
    B* b = new D;
    // derived class pointer
    D* d = std::dynamic_cast<D*>(b);

    if (d != nullptr)
    {
        std::cout << "works";
    }
    else
    {
        std::cout << "can not cast B* to D*";
    }

    getchar();
    return 0;
}
```

Output:

```bash
works
```