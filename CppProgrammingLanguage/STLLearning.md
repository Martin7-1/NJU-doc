# STLLearning

## 1 STL简介

![img](https://oi-wiki.org/lang/csl/images/container1.png)

 **序列式容器**

1. **vector**：后端可高效增加元素的序列表（有点类似Java的**ArrayList**）
2. **array**：C++11出现的一种容器，订场的顺序表，C风格数组的简单包装。
3. **deque**：双端都可高效增加元素的顺序表
4. **list**：可以沿双向遍历的链表
5. **forward_list**：只能沿一个方向遍历的链表

**关联式容器**

1. **集合**(`set`) 用以有序地存储 **互异** 元素的容器。其实现是由节点组成的红黑树，每个节点都包含着一个元素，节点之间以某种比较元素大小的谓词进行排列。
2. **多重集合**(`multiset`) 用以有序地存储元素的容器。允许存在相等的元素。
3. **映射**(`map`) 由 {键，值} 对组成的集合，以某种比较键大小关系的谓词进行排列。
4. **多重映射**(`multimap`) 由 {键，值} 对组成的多重集合，亦即允许键有相等情况的映射。

> What is Predicate?
>
> 谓词就是返回值为真或假的函数。STL容器中经常会用到谓词，用于参数模板。

**无序（关联式）容器**

- **无序（多重）集合**(`unordered_set`/`unordered_multiset`)**C++11**，与 `set`/`multiset` 的区别在与元素无序，只关心”元素是否存在“，使用哈希实现。
- **无序（多重）映射**(`unordered_map`/`unordered_multimap`)**C++11**，与 `map`/`multimap` 的区别在与键 (key) 无序，只关心 "键与值的对应关系"，使用哈希实现。

**容器适配器**

容器适配器其实并不是容器。它们不具有容器的某些特点（如：有迭代器、有 `clear()` 函数……）。

> ”适配器是使一种事物的行为类似于另外一种事物行为的一种机制”，适配器对容器进行包装，使其表现出另外一种行为。

- **栈** `(stack`) 后进先出 (LIFO) 的容器。
- **队列**(`queue`) 先进先出 (FIFO) 的容器。
- **优先队列**(`priority_queue`) 元素的次序是由作用于所存储的值对上的某种谓词决定的的一种队列

### 1.1 容器的共同点

**容器声明**

都是 `containerName<typeName,...> name` 的形式，但模板参数（`<>` 内的参数）的个数、形式会根据具体容器而变。

本质原因：STL 就是“标准模板库”，所以容器都是模板类。

**迭代器**

请参考 [迭代器](#2 迭代器)。

共有函数

`=`：有赋值运算符以及复制构造函数。

`begin()`：返回指向开头元素的迭代器。

`end()`：返回指向末尾的下一个元素的迭代器。`end()` 不指向某个元素，但它是末尾元素的后继。

`size()`：返回容器内的元素个数。

`max_size()`：返回容器 **理论上** 能存储的最大元素个数。依容器类型和所存储变量的类型而变。

`empty()`：返回容器是否为空。

`swap()`：交换两个容器。

`clear()`：清空容器。

`==`/`!=`/`<`/`>`/`<=`/`>=`：按 **字典序** 比较两个容器的大小。（比较元素大小时 `map` 的每个元素相当于 `set<pair<key, value> >`，无序容器不支持 `<`/`>`/`<=`/`>=`。）



## 2 迭代器

在 STL 中，迭代器（Iterator）用来访问和检查 STL 容器中元素的对象，它的行为模式和指针类似，但是它封装了一些有效性检查，并且提供了统一的访问格式。类似的概念在其他很多高级语言中都存在，如 Python 的 `__iter__` 函数，C# 的 `IEnumerator`。

**How to use iterator**

```cpp
#include <iostream>
#include <vector>
#include <string>

int main()
{
    std::cout << "Enter number amount:";
    int amount;
    std::cin >> amount;

    // vector(amount, value)用于在初始化的时候构造amount个value值的元素
    std::vector<int> list;

    for (int i = 0; i < amount; i++)
    {
        int x;
        std::cin >> x;
        list.push_back(x);
    }

    // 普通的遍历
    std::cout << "Normal traverse: ";
    for (int i = 0; i < list.size(); i++)
    {
        std::cout << list.at(i) << " ";
    }
    std::cout << std::endl;

    // 使用迭代器遍历
    std::cout << "User iterator to traverse: ";
    for (std::vector<int>::iterator iter = list.begin(); iter != list.end(); iter++)
    {
        // 对迭代器解引用来获得元素
        std::cout << *iter << " ";
    }
    std::cout << std::endl;

    // or use auto to reduce input
    std::cout << "User auto to reduce the input: ";
    for (auto iter = list.begin(); iter != list.end(); iter++)
    {
        std::cout << *iter << " ";
    }
    std::cout << std::endl;
    return 0;
}
```

**迭代器的分类**

- InputIterator（输入迭代器）：只要求支持拷贝、自增和解引访问。
- OutputIterator（输出迭代器）：只要求支持拷贝、自增和解引赋值。
- ForwardIterator（向前迭代器）：同时满足 InputIterator 和 OutputIterator 的要求。
- BidirectionalIterator（双向迭代器）：在 ForwardIterator 的基础上支持自减（即反向访问）。
- RandomAccessIterator（随机访问迭代器）：在 BidirectionalIterator 的基础上支持加减运算和比较运算（即随机访问）。

> “输入”指的是“可以从迭代器中获取输入”，而“输出”指的是“可以输出到迭代器”。
>
> “输入”和“输出”的施动者是程序的其它部分，而不是迭代器自身。

其实这个“分类”并不互斥——一个“类别”是可以包含另一个“类别”的。例如，在要求使用向前迭代器的地方，同样可以使用双向迭代器。

不同的STL 容器支持的迭代器类型不同，在使用时需要留意。

指针满足随机访问迭代器的所有要求，可以当作随机访问迭代器使用。

**STL相关函数**

很多 [STL 函数](https://oi-wiki.org/lang/csl/algorithm/) 都使用迭代器作为参数。

可以使用 `std::advance(it, n)` 将迭代器 `it` 向后移动 `n` 步；若 `n` 为负数，则对应向前移动。迭代器必须满足双向迭代器，否则行为未定义。

在 C++11 以后可以使用 `std::next(it)` 获得向前迭代器 `it` 的后继（此时迭代器 `it` 不变），`std::next(it, n)` 获得向前迭代器 `it` 的第 `n` 个后继。

在 C++11 以后可以使用 `std::prev(it)` 获得双向迭代器 `it` 的前驱（此时迭代器 `it` 不变），`std::prev(it, n)` 获得双向迭代器 `it` 的第 `n` 个前驱。

STL 容器一般支持从一端或两端开始的访问，以及对 const 修饰符的支持。例如容器的 `begin()` 函数可以获得指向容器第一个元素的迭代器，`rbegin()` 函数可以获得指向容器最后一个元素的反向迭代器，`cbegin()` 函数可以获得指向容器第一个元素的 const 迭代器，`end()` 函数可以获得指向容器尾端（“尾端”并不是最后一个元素，可以看作是最后一个元素的后继；“尾端”的前驱是容器里的最后一个元素，其本身不指向任何一个元素）的迭代器。



## 3 序列式容器

### 3.1 vector

`std::vector` 是 STL 提供的 **内存连续的**、**可变长度** 的数组（亦称列表）数据结构。能够提供线性复杂度的插入和删除，以及常数复杂度的随机访问。

**使用`std::vector`的好处**

1. `std::vector` 可以动态分配内存

	很多时候我们不能提前开好那么大的空间（eg：预处理 1~n 中所有数的约数）。尽管我们能知道数据总量在空间允许的级别，但是单份数据还可能非常大，这种时候我们就需要 `vector` 来把内存占用量控制在合适的范围内。`vector` 还支持动态扩容，在内存非常紧张的时候这个特性就能派上用场了。

2. `vector` 重写了比较运算符及赋值运算符

	`vector` 重载了六个比较运算符，以字典序实现，这使得我们可以方便的判断两个容器是否相等（复杂度与容器大小成线性关系）。例如可以利用 `vector<char>` 实现字符串比较（当然，还是用 `std::string` 会更快更方便）。另外 `vector` 也重载了赋值运算符，使得数组拷贝更加方便。（c++20中比较运算符的重载被移除）

3. `vector` 便利的初始化

	由于 `vector` 重载了 `=` 运算符，所以我们可以方便的初始化。此外从 C++11 起 `vector` 还支持 [列表初始化](https://zh.cppreference.com/w/cpp/language/list_initialization)，例如 `vector<int> data {1, 2, 3};`。

**`vector` 的使用方法** 

#### 3.1.1 构造函数

```cpp
void TestVector()
{
    // 创建空的vector，常数复杂度
    std::vector<int> list0;
    // 该代码保证向vector中插入前三个元素的时候，是常数时间复杂度
    list0.reserve(3);
    // 创建一个初始空间为3的vector, 元素的默认值为0; 线性复杂度
    std::vector<int> list1(3);
    // 创建一个初始空间为3的vector, 元素的默认值为2; 线性复杂度
    std::vector<int> list2(3, 2);
    // 创建一个初始空间为3的vector, 元素的默认值是1; 
    // 并且使用v2的空间配置器; 线性配置器
    // todo: what is 空间配置器
    std::vector<int> list3(3, 1, list2.get_allocator());
    // 创建一个v2的拷贝vector v4, 其内容元素和v2一样; 线性复杂度
    std::vector<int> list4(list2);
    // 创建一个v4的拷贝vector v5, 其内容是{v4[1], v4[3]}; 线性复杂度
    std::vector<int> list5(list4.begin() + 1, list4.begin() + 3);
    // 移动v2到新创建的vector v6，不发生拷贝; 常数复杂度; 需要 C++11
    std::vector<int> list6(std::move(list2));

    // 以下是测试代码，有兴趣的同学可以自己编译运行一下本代码。
    std::cout << "vector 1 = ";
    std::copy(list1.begin(), list1.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << std::endl;
    std::cout << "vector 2 = ";
    std::copy(list2.begin(), list2.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << std::endl;
    std::cout << "vector 3 = ";
    std::copy(list3.begin(), list3.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << std::endl;
    std::cout << "vector 4 = ";
    std::copy(list4.begin(), list4.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << std::endl;
    std::cout << "vector 5 = ";
    std::copy(list5.begin(), list5.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << std::endl;
    std::cout << "vector 6 = ";
    std::copy(list6.begin(), list6.end(), std::ostream_iterator<int>(std::cout, " "));
    std::cout << std::endl;
}
```

#### 3.1.2 元素访问

`vector` 提供了如下几种方法进行元素访问

1. `at()`

	`v.at(pos)` 返回容器中下标为 `pos` 的引用。如果数组越界抛出 `std::out_of_range` 类型的异常。

2. `operator[]`

	`v[pos]` 返回容器中下标为 `pos` 的引用。不执行越界检查。

3. `front()`

	`v.front()` 返回首元素的引用。

4. `back()`

	`v.back()` 返回末尾元素的引用。

5. `data()`

	`v.data()` 返回指向数组第一个元素的指针。

#### 3.1.3 迭代器

vector 提供了如下几种 [迭代器](https://oi-wiki.org/lang/csl/iterator/)

1. `begin()/cbegin()`

	返回指向首元素的迭代器，其中 `*begin = front`。

2. `end()/cend()`

	返回指向数组尾端占位符的迭代器，注意是没有元素的。

3. `rbegin()/crbegin()`

	返回指向逆向数组的首元素的逆向迭代器，可以理解为正向容器的末元素。

4. `rend()/crend()`

	返回指向逆向数组末元素后一位置的迭代器，对应容器首的前一个位置，没有元素。

以上列出的迭代器中，含有字符 `c`（`const`） 的为只读迭代器，你不能通过只读迭代器去修改 `vector` 中的元素的值。如果一个 `vector` 本身就是只读的，那么它的一般迭代器和只读迭代器完全等价。只读迭代器自 C++11 开始支持。

#### 3.1.4 长度和容量

`vector` 有以下几个与容器长度和容量相关的函数。注意，`vector` 的长度（size）指有效元素数量，而容量（capacity）指其实际分配的内存长度，相关细节请参见后文的实现细节介绍。

**与长度相关**：

- `empty()` 返回一个 `bool` 值，即 `v.begin() == v.end()`，`true` 为空，`false` 为非空。
- `size()` 返回容器长度（元素数量），即 `std::distance(v.begin(), v.end())`。
- `resize()` 改变 `vector` 的长度，多退少补。补充元素可以由参数指定。
- `max_size()` 返回容器的最大可能长度。

**与容量相关**：

- `reserve()` 使得 `vector` 预留一定的内存空间，避免不必要的内存拷贝。
- `capacity()` 返回容器的容量，即不发生拷贝的情况下容器的长度上限。
- `shrink_to_fit()` 使得 `vector` 的容量与长度一致，多退但不会少。

#### 3.1.5 元素增删及修改

- `clear()` 清除所有元素
- `insert()` 支持在某个迭代器位置插入元素、可以插入多个。**复杂度与 `pos` 距离末尾长度成线性而非常数的**
- `erase()` 删除某个迭代器或者区间的元素，返回最后被删除的迭代器。复杂度与 `insert` 一致。
- `push_back()` 在末尾插入一个元素，均摊复杂度为 **常数**，最坏为线性复杂度。
- `pop_back()` 删除末尾元素，常数复杂度。
- `swap()` 与另一个容器进行交换，此操作是 **常数复杂度** 而非线性的。

**`vector` 的实现细节**

`vector` 的底层其实仍然是**定长数组**，它能够实现动态扩容的原因是增加了避免数量溢出的操作。首先需要指明的是 `vector` 中元素的数量（长度 `size`） 与它已分配内存最多能包含元素的数量（容量 `capacity`）$N$ 是不一致的，`vector` 会分开存储这两个量。当向 `vector` 中添加元素时，如发现 $n > N$，那么容器会分配一个尺寸为$2N$ 的数组，然后将旧数据从原本的位置拷贝到新的数组中，再将原来的内存释放。尽管这个操作的渐进复杂度是 $O(n)$ ，但是可以证明其均摊复杂度为 $O(1)$ 。而在末尾删除元素和访问元素则都仍然是 $O(1)$ 的开销。 因此，只要对 `vector` 的尺寸估计得当并善用 `resize()` 和 `reserve()`，就能使得 `vector` 的效率与定长数组不会有太大差距。

**`vector<bool>`**

标准库特别提供了对 `bool` 的 `vector` 特化，每个“`bool`”只占 1 bit，且支持动态增长。但是其 `operator[]` 的返回值的类型不是 `bool&` 而是 `vector<bool>::reference`。因此，使用 `vector<bool>` 使需谨慎，可以考虑使用 `deque<bool>` 或 `vector<char>` 替代。而如果你需要节省空间，请直接使用 [`bitset`](https://oi-wiki.org/lang/csl/bitset/)。



### 3.2 array

