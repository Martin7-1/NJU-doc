# Review

[toc]

## 1 顺序、选择、循环

1. 一个`.java`文件中只能有一个`public class`且这个`class`名字必须与文件名相一致
2. 代码是用来读的
	1. 团队的需要
	2. 维护的需要

顺序：略

选择：

``` java
if () {
    
} else if () {
    
} else {
    
}

switch(a) {
    case 0: 
    	break;
    default:
}
```

循环

``` java
for (int i = 0; i < length; i++) {
    
}

while (index < length) {
    index++;
}

do {
    
} while ();

foreach
```

**注意do-while循环最后的分号**

## 2 文件读写

``` java
import java.io.*;

public class ReadFile {
    public static void main(String[] args) {
        String filePath = ReadFile.class.getClassLoader().getResource("data.txt").getPath();
        printFile(filePath);
    }
    
    public static void printFile(String filePath) {
        // 创建File对象
        File file = new File(filePath);
        // 读取file中的数据
        BufferedReader textFile = new BufferedReader(new FileReader(file));
        try {
            String lines = "";
            while ((lines = textFile.readLine()) != null) {
                // TODO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
```

***



## 3 `String`

**`String`常用方法**

```java
public class Test {
    
    public static void main(String[] args) {
        String string = "abcdefg";
        
        // 获取子串
        String subString = string.substring(int startIndex, int endIndex);
        // 获取某个位置的对应的char字符
        char letter = string.charAt(0);
        // String.valueOf()将其他类型变为String
        String number = String.valueOf(1);
        // 去掉字符串两头的空白
        String newString = string.trim();
        // 将string分割成char数组
        char[] letters = string.toCharArray();
        // 根据某个字符将string分开
        String[] words = string.split(" ");
    }
}
```

***



## 4 `ArrayList`

**`ArrayList`常用方法**

```java
import java.util.*;

public class Test {
    
    public static void main(String[] args) {
    	ArrayList<Integer> numbers = new ArrayList<>();
        
        // 增加操作
        numbers.add(0);
        numbers.add(1);
        // 清空所有元素
        numbers.clear();
        // 是否包含某个元素
        boolean isContain = numbers.contains(0);
        // 得到某个元素
        numbers.get(0);
        // 检查是否是空的
        boolean isEmpty = numbers.isEmpty();
        // 查找某个元素第一次出现的位置
        int firstIndex = numbers.indexOf(0);
        // 查找某个元素最后一次出现的位置
        int lastIndex = numebrs.lastIndexOf(0);
        // 移除某个元素，如果有多个会移除第一个
        numbers.remove(0);
        // 更改某个元素
        numbers.set(index, 0);
        // 获取ArrayList的大小
        int size = numbers.size();
    }
    
}
```

***



## 5 数据建模、算法建模

1. 降低复杂度——**分解与抽象**
	1. 分解：
		1. 分解之后，每一部分复杂度要变小
		2. 分解之后相互之间关联要小，相对独立
	2. 抽象：
		1. 抽象之后，接口的复杂度变小
		2. 接口和实现之间达成一种契约
2. **程序 = 算法+数据结构**
	1. 算法是计划、过程、步骤。
	2. 数据结构是操作的目标、对象。
	3. 算法和数据结构是配合的。
3. 算法建模——三种机制（基本表达式、分解、抽象）
	1. 基本表达式：数学运算、逻辑运算
	2. 分解（组合）
	3. 抽象
4. 算法建模——两种思路（**迭代与递归**）
5. 数据建模
	1. 基础数据：整数、浮点数、布尔值
	2. 数据的组合：数组（同一类数据的组合）、结构体`struct`（不同数据的组合）、对象（**数据和行为的组合**）
	3. 数据的抽象——有序对
6. 编程典型场景——数据处理
	1. 输入、处理、输出
7. 用日志来记录数据
8. Dynamic vs Static
	1. Static: 代码、存储、数据结构
	2. Dynamic: 执行、处理器、算法
9. 存储空间和计算速度之间的衡量问题
	1. 处理器：主动的、计算速度、快
	2. 存储：被动的、存储空间、大
	3. 例子：想要得到 $f(x)$ 的值
		1. 算法驱动：只在需要 $f(x)$ 的值的时候通过计算得到
		2. 数据驱动：先通过计算，保存所有 $x$ 对应的 $f(x)$ 的值，需要计算的时候直接访问

***



## 6 递归、高阶函数

1. 现代数字计算机可以计算的本质上都是递归函数（图灵可计算函数）；
2. 非递归函数则是计算机不可计算的



**高阶函数**

1. 也是一种抽象

2. 过程作为参数 类似递归的感觉

3. 即可以将函数作为另一个函数的参数

4. 递归模板：(注意初始的条件)

  (define (\<name\> a b)

  ​	(if (a > b)

  ​		0

  ​		else : ((\<term\> a) + (\<name\> (\<next\> a) b))))

5. 形参是函数

***



## 7 Lambda演算

1. <表达式> ::= <标识符>
2. <表达式> ::= ($\lambda$ <标识符>.<表达式>)
3. <表达式> ::= (<表达式> <表达式>)

> 头两条规则用来生成函数，第三条描述了函数是如何作用在参数上的

**Definition($\lambda$ 项)**

$\lambda$ 项的定义如下：

1. 所有变量都是 $\lambda$ 项（名为原子）
2. 若 $M$ 和 $N$ 是 $\lambda$ 项，那么 $(MN)$ 也是$\lambda$ 项（名为应用）
3. 若 $M$ 是 $\lambda$ 项而 $\phi$ 是一个变量，那么 $(\lambda \phi.M)$ 也是 $\lambda$ 项（名为抽象）

**Theorem(符号约定1-省略)**

1. $\lambda$ 项最外层的括号可以省略
2. 满足左结合应用的 $\lambda$ 项可以省略
3. 抽象型的 $\lambda$ 项的 $M$ 最外层括号可以省略

**Definition(语法全等、自由变量)**

1. 自由变量是指抽象 $\lambda \phi .M$ 中 $M$ 不包含 $\phi$ 的部分
2. 若没有 $\lambda \phi$ 则自由变量直接表示为 $M$
3. 语法全等是指两个 $\lambda$ 项完全相同
4. 若没有自由变量，则称这个 $\lambda$ 项为组合子

**Definition($\alpha$ 变换)**

1. 即将 $\lambda \phi.M$ 中的 $\phi$ 变换成 $\psi$ ，且将 $M$ 中包含 $\phi$ 的字母都变成 $\psi$
2. 这样经过有限步变换得到的项和原来的项是等价的。
	1. 即：**变量的名称是不重要的，我们可以任意修改函数参数的名称，只要我们同时修改函数体内对它的自由引用**

**Definition($\beta$ 规约)**

1. 形如 $(\lambda \phi.M)N$ 的项被称为 $\beta$ 可约式，即我们可以将 $N$ 代替 $M$ 中的 $\phi$，这样得到的项 $[N/\phi]M$ 被称为 $\beta$ 缩减项
2. **计算过程是左结合的**



**模拟自然数**

1. **ZERO** = $\lambda f.\lambda x. x$
2. **SUCC** = $\lambda n.\lambda f.\lambda x.f(n\ f\ x)$
3. **PLUS** = $\lambda m.\lambda n. m $ **SUCC** $n$
4. **MULT** = $\lambda m.\lambda n.\lambda f.m\ (n\ f)$
5. **POW** = $\lambda b.\lambda e. e\ b$
6. **PRED** = $\lambda n. \lambda f. \lambda x.n(\lambda g. \lambda h. h\ (g\ f))\ (\lambda u.x) \ (\lambda u.u)$
7. **SUB** = $\lambda m. \lambda n.n$ **PRED** $m$

**模拟逻辑**

1. **TRUE** = $\lambda x. \lambda y.x$
2. **FALSE** = $\lambda x.\lambda y.y$
3. **AND** = $\lambda p.\lambda q. p\ q\ p$
4. **OR** = $\lambda p. \lambda q. p\ p\ q$
5. **NOT** = $\lambda p. \lambda a.\lambda b. p\ b\ a$
6. **IF** = $\lambda p. \lambda a. \lambda b. p\ a\ b$

**模拟谓词**

1. **ISZERO** = $\lambda n. n(\lambda x.$ FALSE $)$ TRUE
2. **LEQ** = $\lambda m. \lambda n.$ ISZERO $($SUB $m\ n)$
3. **EQ** = $\lambda m. \lambda n.$ AND $($LEQ $m\ n)$ $($LEQ $n\ m)$

**模拟函数**

1. **MAX** = $\lambda m. \lambda n.$ IF (LEQ m n) n m
2. **MIN** = $\lambda m. \lambda n.$ IF (LEQ m n ) m n

**模拟递归**

**Y Combinator**

**Y** = $\lambda g.(\lambda x.g (x\ x)) \lambda x.g (x\ x)$

1. **Y** $F$ = $F$ (**Y** $F$)

**有序对**

***



## 8 邵栋部分

### 大型软件的四个属性

1. 复杂性：大型软件开发与小型软件完全不同
2. 可变性
3. 不一致性
4. 不可见性



> **形成工程化的习惯**
>
> **工具养成习惯**
>
> **理解工程化**



1. 通过实践才能学会！
2. 大量练习！
3. 比赛！



### 软件过程和生命周期模型

软件工程发展趋势：

1. 架构逐渐去中心化
2. 技术趋于自动化
3. 过程趋于增量和迭代
4. 组织趋于自治



**软件开发过程**——简单来说，研究开发过程中的分工

* 一组相互关联的活动组成，将一个或多个输入转换成输出
* 软件开发过程是指一个软件产品开发的方法，它描述了软件开发中的活动和任务。简单的说，过程就是软件开发中的一系列活动，如果能够按照这些活动进行工作，我们就可以获得预想的结果



> 常见软件开发过程模型有瀑布（线性）、迭代和敏捷软件开发等，有很多开发的方法。



**软件开发生命周期**

* 指软件产品从开发到报废的生命周期，通常周期中包括了需求分析、软件设计、实现与调试、测试与验收、部署、维护等活动
* 生命周期模型提供了软件过程时序关系
* 软件报废：不需要其功能；很难在合理的成本范围内继续演进开发原有软件



<span style='color: red'>**几种常见的模型**</span>

1. Build-Fix 创建-修补

	1. 适用于软件规模小，没有合理的开发计划
	2. 问题：大规模、高质量

2. 瀑布模型(Waterfall Model)

	1. 制定计划、需求分析、软件设计、程序编写、软件测试、运行维护
	2. 每个阶段都会产生循环反馈，出现问题则回到上个阶段
	3. 优点：帮助开发人员理解软件开发中的活动和任务，界定清晰软件开发检查点
	4. 有缺陷，很少有项目能够以纯线性的方式进行，成本增加和系统开发的混乱。但仍然可以帮助我们认识软件开发活动，并对其提供指导

	**贝姆定律(Boehm's law)** ：在开发过程中越晚修正缺陷，代价就会越高

3. 迭代式软件开发

	1. 根据软件项目的不同功能子集来分解项目
	2. 每个迭代周期结束时都应该得到一个经过测试，集成起来的基本可用的软件产品
	3. 整个开发工作被组织为一系列小的项目，被称为一系列的迭代。
	4. 优点：
		1. 易于应对需求更改，用户在看到迭代结果会重新审视提出需求，修改原来的需求或增加新的需求。
		2. 提高团队士气，开发人员的士气和对项目的热情部分决定了软件项目的成败。
	5. “人决定质量”
	6. 为什么要使用迭代式软件开发？
		1. 测试与集成都是⾮常难于估算的开发活动，不能把这样的活动放到迭代开发的最后进⾏，否则就⽆法得到迭代式开发的益处。
		2. 每次迭代的结果是开发团队在本迭代内⼯作真实有效的反馈，它有助于开发团队及时调整开发计划和软件开发实践。
		3. 测试和集成⼯作应该做到：即使本次迭代的软件不进⾏发布，那么如果要进⾏发布的话，也不需要⼤量的⼯作。

***



## 9 结构化方法和面向对象方法

### 函数式编程范式

* **避免重复**
	1. 消除重复——抽象
	2. 消除重复——数据过滤
* **避免过程和数据的重复**
* **数据**
* Java8 Steam API
* Python语言函数式变成
	* 基本函数：map()、reduce()、filter()
	* 算子：lambda

### 结构化编程

1. 思想

	1. **自顶向下逐步求精**
	2. 算法+数据结构

2. 模型

	1. 数据流图
	2. 结构图
	3. 流程图

	

#### 1 数据流图

1. 将系统看成过程的集合
2. 过程就是对数据的处理
	1. 接收输入、进行数据转换、输出结果
	2. 代表数据对象在穿过系统时如何被转换
3. 可能需要和软件系统外的实体尤其是和人进行交互
4. 数据的变化包括：被转换、被存储、或者被分布
5. 图例：![image-20210628193135061](D:/Typora Image/image-20210628193135061.png)
6. 外部实体：
	1. 数据的生产者或者消费者
		1. 人、设备、传感器
		2. 计算机系统
	2. 数据总是从某处来，然后流向其他的地方
7. 过程
	1. 数据的转换器
		1. 计算纳税金额、计算面积、格式化报告、显示图标
	2. 数据总是被处理然后完成某项业务功能
8. 数据流
	1. 通过系统的数据流总是从输入被转换为输出
9. 数据的存储
	1. 数据有时候会被存储起来为以后使用做准备
10. 语法规则：
	1. 过程是对数据的处理，必须有输入，也必须有输出，输入数据集应该和输出数据集存在**差异**
	2. ![image-20210628194148913](D:/Typora Image/image-20210628194148913.png)
	3. 数据流是必须和过程产生关联的，它要么是过程的数据输入，要么是过程的数据输出
	4. 所有的对象都应该有一个可以唯一标识自己的名称。
11. 理解数据流图：
	1. 处理
		1. 并不一定是程序
		2. 它可以是一系统程序、单个程序或程序的一个模块，甚至可以是人工处理过程；
	2. 数据存储
		1. 并不等同于一个文件
		2. 它可以是一个文件、文件的一部分、数据库元素或记录的一部分；它代表的是**静态**的数据
	3. 数据流
		1. 也是数据，是**动态**的数据

#### 2 结构图

1. 实际上就是分为：控制、输入、操作、输出
2. 图例：
	1. ![image-20210628195456754](D:/Typora Image/image-20210628195456754.png)
	2. 其中实心的箭头表示控制、空心的箭头表示数据流向（Data处少了箭头、Flag应为实心且有个箭头）
3. ![image-20210628195419555](D:/Typora Image/image-20210628195419555.png)

#### 3 流程图

图例：![img](D:/Typora Image/1614731-20190621153234196-1096438098.png)

### 面向对象方法

1. 方法的概念
	1. 物理的角度：指令块
	2. 逻辑的角度：抽象指令单元
	3. 语义的角度：行为
2. 分类
	1. 类的行为：静态方法
	2. 对象的行为：成员方法

**所有者**

1. 类的行为
	1. 静态方法
	2. 通过类名来调用，比如 `Math.sqrt()`
2. 对象的行为
	1. 成员方法
	2. 对象的引用来调用，比如`Dog dog = new Dog(); ` `dog.bark()`

**接口**

1. 返回值
2. 方法名
3. 参数
	1. 个数
	2. 类型
	3. 顺序
4. 抛出的异常

**运行期**

1. 程序执行的时间段
2. 方法调用
	1. 同步：调用方法和被调用方法相关（等待被调用方法执行完）
	2. 异步：调用方法和被调用方法不相关

**可见性**

1. public：全局可见
2. protected：子类可见
3. 缺省：包内可见
4. private：类内可见

**类图的画法**

![image-20210628211743222](D:/Typora Image/image-20210628211743222.png)

> 1. 方法是形参，调用方法时传递实参
> 2. 如果声明了返回值，那么必须要返回一个对应类型的value
> 3. Java是值传递的，即通过复制来传递，所以在方法内部改变形式参数的值，实际参数的值不会发生改变。

**break 和 continue**

1. break
	1. 退出最内层的循环
	2. 退出标签所在的循环，但是不会再重新进入循环
2. continue
	1. continue回到内层循环的最开始
	2. continue label会重新进入循环

**面向对象方法的三要素**

​	<span style='color:red'>**封装、继承、多态**</span>

**面向对象方法的原则**

​	<span style='color: red'>**职责与协作**</span>

**面向对象方法中的一些概念**

1. 软件 = 一组相互作用的对象
2. 对象 = 一个或多个角色的实现（状态和行为）
3. 责任 = 执行一项任务或掌握某种信息的义务
4. 角色 = 一组相关的责任
5. 协作 = 对象或角色（或两者）之间的互动

***



## 10 整数、浮点数操作

**变量**

1. 物理的角度：计算机的数据存储单元
2. 逻辑的角度：软件模型中的抽象数据单元
3. 语义的角度：类的属性、方法的状态

> 赋值打破了引用透明性

Java：静态强类型

Python：动态强类型

C、C++：静态弱类型

JavaScript：动态弱类型

**Java中的类型**

1. 基础类型
	1. 整数：`byte/short/int/long/char`
	2. 浮点数：`float/double`
	3. 布尔类型：`boolean`
2. 引用类型
	1. 类的类型
	2. 接口的类型
	3. 数组的类型
3. 特殊的类型
	1. `null`

> 1. 基础数据类型
> 	1. 来源于物理视角
> 	2. 预定义好的、基础的数据类型
> 2. 引用类型
> 	1. 来源于语义视角
> 	2. 对象的遥控器、类似于指针

**Remark:**

1. 引用类型的值其实类似于指向对象的指针
2. 所有对象都会继承`object`类，即所有对象都可以使用`object`类的方法

3. Java是完全面向对象的，但基础数据类型不是
4. 所有数据类型都有一个严格的定义的范围
5. 基础数据类型是其他类型数据的基础

**关于`Java`中的引用变量**

1. 其实并没有“对象变量”这个东西，只有“引用变量”。
2. 引用变量实际上存储了能够达到**目标对象**的一个地址，相当于指针。且所有引用变量所占的字节数量都是**相同**的
3. 在`java`中我们不需要知道什么在引用变量里，我们只要知道一个引用变量只能指向一个对象，JVM会知道怎么来通过引用变量来获得对象。

**一些变量的范围**

1. `byte`：-128 to 127
2. `short`：-32768 to 32767
3. `int`：-2147483648 to 2147483647（$-2^{31}$ to $2^{31} - 1$）
4. `long`：-9223372036854775808 to 9223372036854775807
5. `char`：'\u0000' to '\uffff' 即 0 to 65535

**Java中整数和浮点数的操作**

1. `java`会根据变量的类型来选择对应的操作符，比如`int+int`则会选择int加法，若`long+int`则会选择long加法，即**会向大类型进行转换**。
2. `float`是32位的，`double`是64位的。
3. "Not-a-Number" 即 NaN，如果两边的操作数都是NaN，则$<, \le, >, \ge,==,!=$ 都是`false`
4. 在逐渐向下溢出`double/float`时，会损失精度。
5. `0.0/0.0`是NaN
6. `double`的精度比`float`高

**`java`变量的命名规则**

1. 变量名应该是以第一个单词首字母小写，其余单词的首字母大写
2. 类应该是所有单词的首字母都大写
3. 类常量的声明应该全部大写，且单词间用下划线隔开。(`java`中以`static final`来作为常量)

**成员变量的初始化**

成员变量会自动初始化，遵循以下规则：

1. 整数被初始化为0
2. 浮点数被初始化为0.0
3. `boolean`值被初始化为`false`
4. 引用变量被初始化为`null`

**Remark:**

1. 局部变量不会自动初始化，且在使用前必须经过初始化。
2. 在一个方法中声明的是局部变量，该变量只对该方法可见
3. 成员变量对整个类可见，它是非局部变量

**常用变量的用法**

1. 局部变量：方法内部
2. 成员变量：对象内部
3. 静态变量：全局可见

> 如何访问一个变量？
>
> 1. 直接访问——内部使用者。
> 2. 间接访问——通过get()方法访问，外部使用者。

***



## 11 Overriding vs Overloading

#### Overloading

1. **overloading**指的是**不同**的方法恰好有了相同的名字
2. **overloading**中相同名字的两个方法可能：
	1. 参数个数不同
	2. 参数的类型不同
	3. 参数的顺序不同
	4. 返回值不同且上述任意一个不同
	5. 可以随意更改可达性，比如改成`private`

**remark：**如果只有返回值不同那么是非法重载，无法通过编译。只有参数标识符不同也同理



#### Overriding

1. 参数必须一致，返回值必须要兼容
2. 方法的可达性不能降低（即原本为`public`，覆盖时不能够变成`private`）
3. 是同一种方法在父类和子类中的不同实现
4. **不能够Overriding父类的私有方法**，因为父类私有方法会自动加上**`final`**的标签。

***



## 12 封装

<span style='color: red'>**数据职责与行为职责在一起**</span>



#### 有关`static/final`和构造函数

**有关静态(`static`)**

1. 静态方法的调用不需要创建对象，而是直接通过类名来调用

2. 静态方法不能够使用**非静态的成员变量**。
3. 静态方法不能够调用**非静态的方法**
4. 静态变量：在构建不同对象的同时，这些对象共享一个静态变量
5. 非静态的方法是能够获取静态变量的。
6. 初始化静态变量
	1. 当类被加载的时候静态变量就被初始化了
	2. JVM会决定何时加载一个类
	3. 一个类中的静态变量会优先于这个类中的其他东西被初始化
	4. 一个类中的静态变量会在其他静态方法运行之前被初始化
	5. `static final`标识的变量可以看作是常量，即一被初始化就不能够更改

**有关`final`**

1. 未被设定值的final变量可以初始化
2. final变量不能够对其进行更改，即一旦初始化它就不能够更改了
3. 有关final方法
	1. 把方法锁定，以预防任何继承它的类修改它。**即禁止这个方法被子类overriding**
	2. 效率问题。指明final可以使得编译器将针对该方法的所有调用都转为内嵌(inline)调用
4. 如果一个类是final的，那么你无需对方法标记fianl，**该类永远无法被继承**。

**有关构造函数**

1. 每个类都有构造函数，即使你不写他，也会自动帮你写一个构造函数
2. 构造函数是以**类的名字**命名的，**没有返回值**
3. 构造函数在对象被赋值给引用对象之前就运行
4. 构造函数给予了在new的中间操作的机会。
5. 可以通过overloading来写多个构造函数。
6. 如果你自己写了一个构造函数，那么系统就**不会**再帮你自动实现一个构造函数，你必须自己写没有参数的构造函数。

***



## 13 职责、协作

#### 职责

1. 数据职责和行为职责在一起

2. 数据和行为要相互对应

3. 类：职责的抽象

4. 对象：职责的实现

5. **每个类应当只有单一职责**

6. 给对象分配职责的策略：

	1. 覆盖到所有重要的方面
	2. 寻找需要执行的动作以及需要维护和生成的信息

	> 类和对象是抽象和具体的关系
	>
	> 在面向对象世界观中，函数的调用变成了对象之间的交互

**视角的变化**

1. 行为视角：结构化方法
2. 数据视角：数据为中心方法
3. 职责视角：面向对象方法

**类的职责与封装**

1. 数据职责
	1. 表征对象的本质特征
	2. 行为所需要的数据
2. 行为职责
	1. 表征对象的本质行为
	2. 拥有数据所应该体现的行为



#### 协作

**Definition**

1. 一组对象共同协作履行整个应用软件的责任
2. 设计的焦点是从**发现对象及其责任**转移到**对象之间如何通过互相协作来履行责任**。

> 协作模型描述的是一些关于“如何做”，“何时做”，“与谁工作”的动态行为

**职责分配：分与聚**

1. 从小到大，将对象的小职责聚合形成大职责
2. 从大到小，将大职责分配给各个小对象
3. 两种方法同时运用，共同来完成对协作的抽象

**可以协作的对象**

1. 该对象自身
2. 任何以参数形式传入的对象
3. 被该对象直接创建的对象
4. 其所持有的对象引用

***



## 14 类之间的关系

**类之间的关系**

1. General Relationship	
	1. 依赖
2. Instance Level Relationship
	1. 连接
	2. 关联
3. Class Level Relationship
	1. 继承
	2. 实现



**General Relationship**

1. 依赖：物理关系，用到对方即代表我对对方的依赖，只是一种短暂的使用

**Instance Level Relationship**

1. 连接：对象之间的连接，是一种实例之间的联系
2. 关联：
	1. 逻辑关系
	2. 关联的分类：普通关联、聚合、组合
	3. 强弱关系：依赖 < 普通关联 < 聚合 < 组合

**Class Level Relationship**

1. 继承
2. 实现



<span style='color: red'>**依赖、普通关联、聚合、组合的差别**</span>

1. 依赖：“...uses a...”
	1. 依赖指的是某个对象的功能依赖于另外的某个对象，而被依赖的对象只是作为一种工具，并不持有引用。
	2. 代码实现：在需要的方法中才获得所需对象的引用，而不是在成员变量中获得对方的引用
	3. 图例画法![image-20210629111428524](D:/Typora Image/image-20210629111428524.png)
2. 普通关联：“...has a...”
	1. 关联指的是某个对象会**长期**的持有另一个对象的引用，两者的关系往往是相互的。普通关联的两个对象批次间没有任何**强制性的约束**，可以随时解除关系或者进行关联，在**生命周期问题上没有任何约定**。关联可以共享。
	2. 代码实现：一般是在方法的参数中将其添加到某个成员变量数组中。即可以实现随时解绑或随时绑定
	3. 图例画法![image-20210629111757458](D:/Typora Image/image-20210629111757458.png)
3. 聚合：“...owns a...”
	1. 聚合是**强版本**的关联。它暗含着一种**所属关系**以及**生命期关系**。被聚合的对象**还**可以再被别的对象关联，所以被聚合对象是可以共享的，代表一种更亲密的关系。
	2. 代码实现：一般在构造函数中获得聚合的对象，比如`Torch torch = new Torch(battery)`这里的`torch`和`battery`就是聚合的关系。也可以直接在成员变量里持有对方的引用
	3. 图例画法![image-20210629112440816](D:/Typora Image/image-20210629112440816.png)
4. 组合：“...is a part of...”
	1. 关系当中的最强版本，它直接要求包含对象对被包含对象的拥有以及包含对象与被包含对象生命期的关系。被包含的对象还可以再被别的对象关联，所以被包含对象是可以共享的，然而绝不存在两个包含对象对同一个被包含对象的共享。
	2. 代码实现：直接在成员变量持有组合对象的引用。
	3. 图例画法：![image-20210629113338553](D:/Typora Image/image-20210629113338553.png)

***



## 15 继承

#### 有关继承

1. 子类继承了父类的所有的成员变量和方法（包括私有变量）

2. 继承得到的私有变量**无法直接访问**，需要通过getter方法来访问

3. 继承类图的画法![image-20210629114040969](D:/Typora Image/image-20210629114040969.png)

4. 子类可以在继承父类的方法的基础上添加属于自己的独特的成员变量和成员方法，也可以override父类的方法。**成员变量不可以被overriding**。

5. 运行期调用方法的时候会调用继承树最底层的方法。

	> 这只是简单的理解，具体需要看你构造的实际对象

6. 使用“IS-A”测试来判断是否使用继承。

7. 子类是更具体更特殊的类，父类是更普遍更抽象的类，比如`Animal`和`Dog`的关系。

8. 不要只是为了减少重复而使用继承关系，如果无法通过“IS-A”测试也不能够使用继承关系。



#### 有关抽象类和抽象方法

1. 目的：一些对象不应该被创建实例，比如`Animal`类，这时我们可以让这个类变成抽象类，即变成`abstract class Animal`

2. 抽象类本身是没有用的，除非他被继承，有了子类，抽象类的子类可以被实例化。

3. 抽象方法是没有内容的。`public abstract void eat()`

4. **抽象方法必须要放在抽象类当中**

5. **抽象类中可以有非抽象的方法**

6. 抽象方法的存在就是为了多态，具体的类**必须**要实现所有的抽象方法，实现抽象方法就像子类覆盖父类方法一样。

7. 抽象方法的末尾需要以**分号**结尾

	`abstract public void go();`



<span style='color: red'>**两句关键**</span>

1. 编译时，编译器根据**引用变量的类型**来决定你是否能够调用某个方法。（依据引用变量的类型，而不是引用变量指向的对象的类型）
2. 执行时，$JVM$根据引用变量**实际指向的对象的类型**来决定哪个方法被调用

<span style='color: red'>**两句关键**</span>

1. 编译期：

	1. 静态绑定：编译时决定选择overloading的哪种方法

	2. 多分派：查找所有overloading的方法来匹配对应类型，没有对应类型会自动匹配到最相近的能包容的类型（既看类型也看参数）

		> 匹配优先级：char > int > long > Character > Serializable ...

2. 运行期：

	1. 动态绑定：运行时决定选择overriding的哪种方法
	2. 单分派：方法已经决定，只是决定选择父类还是子类的实现（根据引用变量实际指向对象的类型。）
	3. 都是invokevirtual命令



> 有时候我们需要通过强制类型转换来将某个对象从父类变回子类，前提是你知道该对象实际上是某个子类的对象。



#### 继承和构造方法

1. 父类的构造方法率先调用，子类的构造方法后调用

	> 原因：子类的构造方法先被压在方法栈，然后子类的构造方法调用了父类的构造方法，父类构造方法压在了方法栈。然后执行父类的方法，执行完后父类的构造方法被弹出，继续执行子类的方法。

2. 子类构造方法中有隐藏的`super()`

  ```java
  class Animal {
      public Animal {
          System.out.println("Making an animal...");
      }
  }
  
  class Hippo extends Animal{
      public Hippo() {
          super();
          System.out.println("Making a Hippo...");
      }
      
      public Hippo(String name) {
          this();
          System.out.println("My name is " + name);
      }
  }
  ```

  > **注意**：即使没有写`super()`，编译器会自动为你加上`super()`

3. `super()`一定要在子类构造方法的**第一行**。

4. 可以通过`this()`来调用自己类中的其他overloading的构造方法，`this()`也必须放在构造方法的第一行。但注意：**`this()`和`super()`不能同时出现**

***



## 16 多态

#### 多态的思想

1. 多态分离了**“做什么”**和**“怎么做”**，从另一个角度将接口和实现分离开来。
2. 多态的作用则是消除类型之间的耦合关系，多态方法调用允许一种类型表现出与其他相似类型之间的区别，只要它们都是从同一个父类导出而来的。这种区别是根据方法行为的不同而表示出来的，虽然这些方法都可以通过同一个父类来调用。
3. 多态可以表达不同的计算类型，并且在运行的时候**动态**的确定正确的计算。
4. 多态是指**多个方法使用同一个名字有多种解释**，当使用这个名字去调用方法时，系统将选择重载自动的选择其中的一个方法。在多态中**只关心**一个对象做什么，而不关心如何去做。



**多态——引用变量**

```java
Animal[] animals = new Animals[5];
animals[0] = new Dog();
animals[1] = new Cat();

for (Animal animal : animals) {
    animal.eat();
    animal.roam();
}
```

**多态——参数和返回值**

```java
class Vet {
    public void giveShot(Animal a) {
        a.makeNoise();
    }
}

class PetOwner {
    public void start() {
        Vet v = new Vet();
        Dog d = new Dog();
        Hippo h = new Hippo();
        
        v.giveShot(d);
        v.giveShot(h);
    }
}
```

**Remark：**假如要引入新的子类

1. 只需要继承父类。
2. 通过多态，不需要修改原有表达**“做什么”**的代码。
3. 新的子类特有的**“怎么做”**的行为通过方法覆盖Overriding来动态绑定

***



## 17 继承vs组合

**继承的缺点**

1. 继承是个很重的关系，子类和父类之间的契约关联性极强
2. **”脆弱的基类“**问题
3. 父类接口的修改会导致大量应用这个接口的子类的代码(Client代码)的修改

> 组合没有绑定关系，对接口的修改不会影响组合对象的接口。



**组合的选择**

1. 组合通常用于你想要在新类中使用现有类的功能而非它的接口的情形。即，你在新类中嵌入某个对象，借其实现你所需要的功能。新类的用户看到的只是你为新类所定义的接口，而非嵌入对象的接口。要实现这个，你只需要在新类中嵌入一个private的现有类的对象。
2. 有时，允许类的用户直接访问新类中的组合成分是极具意义的；用户能够了解到你在组装一组部件的时候，会使得端口更加易于理解

***



## 18 类的初始化

#### 对象初始化初步

1. 成员变量是**最先**初始化的，比任何方法（包括构造方法）都先
2. 静态变量先初始化，然后再是非静态变量
3. 静态变量初始化结束后就是按照文字顺序执行



#### 带继承关系的类的初始化顺序

1. 加载被调用的类，加载这个类的父类，一直加载到`Object`类

	> JVM会决定何时该加载哪个类

2. 从继承树的**最顶端**开始**初始化静态变量**，一直逐层往下初始化

	> 原因：子类可能会用到父类的静态变量

3. 成员变量都被设置为默认的初始值

	> 即整数被设置为0，浮点数被设置为0.0，引用变量被设置为null

4. 父类的构造方法被调用。

5. 本类的成员变量按照顺序来进行初始化（如果有）。

	> 即如果成语变量有初始化(=)就执行这个等于号

6. 本类的构造方法的剩余部分（即除了`super()`）被调用。

> 如果你在一个构造函数中调用了一个正在构造的对象的动态绑定的方法？
>
> ans：那么覆盖的方法将在对象完全构造之前被调用。
>
> 即：调用类的构造方法还没有用完，但我已经用到了该类的其他方法，则该类中所用到的成员变量都会是默认的初始值。



#### 类的初始化

1. 加载（Loading）：由类加载器执行，查找字节码，并创建一个Class对象（**只是创建**）
2. 链接（Linking）：验证字节码，为静态域分配储存空间（只是分配，并不初始化该存储空间），解析该类创建所需要的对其它类的应用。
3. 初始化（Initialization）：首先执行静态初始化块`static{}`，初始化静态变量，执行静态方法（如构造方法）。



**可能造成类被初始化的操作**

1. 创建一个Java类的实例例对象
2. 调用一个Java类的静态⽅方法
3. 为类或接口中的静态域赋值
4. 访问类或接口中声明的静态域，并且该域的值不是常值变量
5. 在一个顶层Java类中执行`assert`语句
6. 调用Class类和反射API中进行反射操作

> 1. 常量在编译阶段会存入调用它的类的常量池中，本质上没有直接引用到定义该常量的类，因此**不会触发**定义常量的类的初始化。
>
> 2. 通过数组定义来引用类，不会触发类的初始化
>
> 	比如：`Animal[] animals = new Animal[5]`不会触发`Animal`类的初始化。

***



## 19 接口

**接口的初始化**

1. 与类的初始化的区别：
	1. 类的初始化要求其父类全部被初始化过了
	2. 而接口的初始化并不一定要求父接口都被初始化过。只有在真正使用到父接口的时候才会初始化该父接口。
	3. 调用父类的`static final`常量时不会触发类的初始化，但是调用接口中的`static final`会触发该接口的初始化
2. 接口不能使用`static{}`语句块，但编译器会为接口生成类构造器用于初始化接口中定义的成员变量（实际上是`static final`修饰的全局变量）



**Definition(接口(Interface))**

1. 接口就像百分百抽象的类，即**所有方法都是抽象方法**。

2. 关键字：

	```java
	public interface Pet {}
	public class Dog extends Canine implements Pet {}
	```

3. 一个类可以实现多个接口，但一个类只能继承一个父类。

4. 接口中定义的成员变量自动是`static final`的。



**有关接口的注意点**

1. 接口默认方法不能覆盖`Object`类的`equals()、hashCode()、toString()`方法

***



## 20 针对接口编程

#### 具体类和接口的使用

1. 显示地使用具体的类
	1. 锁定某个具体的实现
	2. 丧失了可扩展性
	3. 丧失了灵活性
2. 按接口编程
	1. 增加开发的可并行性

***



## 21 可修改性

**关于Client代码默认的知识**

1. 大量的。
2. 分散的。
3. 如果发生修改重新编译的话，是需要大量的时间的。



**可修改性**

1. （狭义）可修改性：对已有实现的修改，希望不影响Client代码（封装）
2. 可扩展性：对新的实现的扩展，希望不影响Client使用类的代码（继承、多态）
3. 灵活性：对实现的动态配置，希望不影响Client使用类的代码（组合+接口）

***



## 22 异常

> 在阅读doc查询API的时候需要知道该方法会抛出什么异常



1. 使用`try-catch`来捕获异常，使用`throw`来抛出异常

	```java
	public void takeRisk() throws BadException {
	    if (abandonAllHope) {
	        throw new BadException();
	    }
	}
	
	public void crossFingers() {
	    try {
	        anObject.takeRisk();
	    } catch (BadException e) {
	        e.printStackTrace();
	    }
	}
	```

2. `RuntimeException`不会被编译器检测出，只有在运行的时候会被发现，比如`NullPointerException、NumberFormatException`等。这些问题大部分来自代码逻辑。我们不需要通过`try-catch`来捕获这种异常。

3. 异常是某个有风险的方法抛出异常给调用者，调用者应该要选择是捕获这个异常还是继续抛出这个异常



**关于`try-catch`**

1. 如果没有发生异常，那么`catch`代码块中的代码不会执行，会直接跳到`try-catch`的后面执行代码

2. 如果发生了异常，那么在`try`当中发生异常的语句**之后**的代码都不会被执行，而会直接执行`catch`中的代码，然后**继续执行**`try-catch`之后的代码。

3. 如果有无论是否发生异常都需要执行的代码，请使用`finally`

	```java
	try {
	    
	} catch() {
	    
	} finally {
	    
	}
	```

4. 一个方法可以抛出多个异常，只要在异常之间以逗号相连就可以。

5. `try-catch`也可以捕获多个异常，通过多个`catch`并列来捕获异常，但要注意我们应该先**从小异常开始捕获**（即从继承树中层次最低的异常开始）。

6. 如果你不想捕获这个异常，只要通过`throws`抛出它就可以了

7. main()方法**必须要处理**所有异常，不然JVM会中断程序。



> 面对所有的异常(除了`RuntimeException`)，对付其他异常的手段只有两种：
>
> 1. 抛出这个异常
> 2. 捕获这个异常



**一些注意点**

1. 不能够只有`catch`或`finally`而没有`try`。
2. `try`后面必须要跟着`catch`或者`finally`或者同时
3. 不能够在`try`代码块和`catch`代码块之间放代码
4. 如果没有`catch`，那么方法依然要**抛出**这个异常



**使用异常机制的建议**

1. 异常的声明是API的一部分

2. 异常处理**不能**代替简单的测试

3. 不要过分地细化异常

4. 利用异常的层次结构

	> 不要只抛出`RuntimeException`，应该寻找更合适的子类或者创建自己的异常类

5. 不要压制异常

6. 在检测错误时，“苛刻”要比放任更好

	> 在出错的地方抛出一个`EmptyStackException`比在后面抛出一个`NullPointerException`异常更好

7. 不要羞于传递异常

	> 早抛出，晚捕获



**try-with-resources**

好处：系统自动关闭，节省资源

***



## 23 GUI控件、布局、事件响应

**GUI**

1. WIMP: window、icon、menu、pointer
2. WYSIWYG: What-You-See-Is-What-You-Get



**GUI的关键元素**

1. Component 组件
2. Layout 布局
3. Event 事件



**实例代码**

**GUI基本结构**

``` java
import javax.swing.*;
import java.awt.event.*;

// 基本GUI结构，只有一个button
public class SimpleGuiB implements ActionListener {
    JButton button;
    
    public static void main(String[] args) {
        SimpleGuiB gui = new SimpleGuiB();
        gui.go();
    }
    
    public void go() {
        JFrame frame = new JFrame();
        button = new JButton("click me");
        // 对这个按钮添加事件监听
        button.addActionListener(this);
        
        frame.getContentPane().add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
    
    // 实现接口
    public void actionPerformed(ActionEvent event) {
        button.setText("I've been clicked");
    }
}
```

**实现两个按钮的代码**

```java
import javax.swing.*;
import java.awt.event.*;

// 如果实现监听多个对象
// 运用内部类来实现接口
public class TwoButtons {
    
    JFrame frame;
    JLabel label;
    
    public static void main(String[] args) {
        TwoButtons gui = new TwoButtons();
        gui.go();
    }
    
    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JButton labelButton = new JButton("Change Label");
        // 注册事件的监听
        labelButton.addActionListener(new LabelListener());
        
        JButton colorButton = new JButton("Change Circle");
        // 注册事件的监听
        colorButton.addActionListener(new ColorListener());
        
        label = new JLabel("I'm a label");
        MyDrawPanel drawPanel = new MyDrawPanel();
        
        frame.getContentPane().add(BorderLayout.SOUTH, colorButton);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.getContentPane().add(BorderLayout.EAST, labelButton);
        frame.getContentPane().add(BorderLayout.WEST, label);
        
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
    
    class LabelListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            label.setText("Ouch!");
        }
    }
    
    class ColorListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            frame.repaint();
        }
    }
}
```

> 有关内部类：
>
> 1. 内部类可以存取外部类的所有内容，包括私有的
> 2. 并非所有外部的类都可以，内部类只能存取它所属的那一个
> 3. 如果遇到了多个名字相同的变量，那么会遵循**就近原则**
> 	1. 比如内部类有一个名为`name`的成员变量，外部类也有一个名为`name`的成员变量，那么内部类的方法在使用该变量时会先使用内部的这个。



**GUI的补充细节**

- 布局管理器会控制嵌套在其他组件中组件的大小和位置；
- 当某个组件添加到背景组件上面时，被加入的组件是由背景组件的布局管理器管理的；
- 布局管理器在做决定之前会询问组建的理想大小，并根据策略来决定选择采用哪些数据（但是你自己setSize得到的尺寸只能是向系统提供一个建议，并不能起决定性作用）；
- `pack()`方法会使窗口的大小符合内含组件的大小；
- 框架（JFrame）默认使用BoxLayout布局，然而面板默认使用FlowLayout布局；
- 框架上面不能直接加组件，框架的布局管理器可以自己换掉，也可以在关闭布局管理器之后直接定位；
- 给panel来add一个scroller就可以让他成为可以滚动的面板；

***



## 24 网络、线程、synchronize

#### 网络

**客户端工作重要的三件事**

1. 连接：用户通过建立**socket**连接来连接服务器
2. 传送：用户送出信息给服务器
3. 接收：用户从服务器接收信息

> 如何创建`socket`连接？你需要知道两项关于服务器的信息！
>
> 1. 服务器在哪里（服务器的IP地址）
> 2. 服务器用哪个端口来收发数据（端口号）
> 	1. TCP端口只是一个16位宽，用来识别服务器上特定程序的数字
> 	2. 0-1023的端口号是保留给已知的特定服务使用



##### 服务器与客户端(Server and Client)

**Socket上的读写数据**

```java
// 此处省略库的引用
// 从Socket上读取数据
class Read {
    public static void main(String[] args) {
        // IP地址与端口号
        // 127.0.0.1通常代表本机的IP地址
        Socket socket = new Socket("127.0.0.1", 5000);
        InputSteamReader stream = new InputStreamReader(socket.getInputStream());
        
        // 此处省略异常的捕获
        BufferedReader reader = new BufferReader(Stream);
        String message = reader.readLine();
    }
}

// 往Socket上写数据
class Writer {
    public static void main(String[] args) {
        Socket socket = new Socket("127.0.0.1", 5000);
        PrintWritet writer = new PrintWriter(socket.getOutputStream());
        
        writer.println("message to send");
    }
}
```

**DailyAdviceClient代码**

```java
import java.io.*;
import java.net.*;

public class DailyAdviceClient {
    public void go() {
        try {
            Socket socket = new Socket("127.0.0.1", 4242);
            InputSteamReader stream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
            
            String advice = reader.readLine();
            System.out.println("Today you should " + advice);
            
            // 关闭所有的串流
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        DailyAdviceClient client = new DayilyAdviceClient();
        client.go();
    }
}
```

**DailyAdviceServer代码**

```java
import java.io.*;
import java.net.*;

public class DailyAdviceServer {
    
    String[] adviceList = {"Take smaller bites", "Go for the tight jeans. No they do NOT make you look fat",
        "One word: inappropriate", "Just for today, be honest.  Tell your boss what you *really* think", 
        "You might want to rethink that haircut"};
    
    public void go() {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            
            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                System.out.println(advice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getAdvice() {
        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }
    
    public static void main(String[] args) {
        DailyAdviceServer server = new DailyAdviceServer();
        server.go();
    }
}
```



##### 要点

1. 客户端(Client)必须要知道服务器(Server)应用程序的IP地址和端口号
2. 客户端通过建立Socket来连接服务器
3. InputStreamReader是个可以转换字节成字符的**”桥梁“**。主要用来连接BufferedReader和底层的Socket输入串流
4. 服务器使用ServerSocket来等待用户对特定端口的请求
5. ServerSocket接到请求时，会新建一个Socket连接来接受客户端的请求。



#### 线程

**代码的基本框架**

1. 实现`Runnable`接口，该接口中只有`run()`一个方法。

2. 构建`Thread`对象，来创建新的线程

3. `Thread.start()`来启动这个线程

4. 代码示例：

	```java
	public class MyRunnable implements Runnable {
	    public void run() {
	        System.out.println("...");
	    }
	}
	
	class ThreadTestDrive {
	    public static void main(String[] args) {
	        // 新建线程的是三个步骤
	        Runnable threadJob = new MyRunnable();
	        Thread t = new Thread(threadJob);
	        t.start();
	        
	        System.out.println(".....");
	    }
	}
	```



##### 要点

1. 线程调度器不能够保证任何的执行时间和顺序，所以我们不能够依赖线程调度器来控制线程

	> 考虑使用`sleep()`方法来适度控制

2. Java中的每个线程都有独立的执行空间，在执行空间最上面的线程会被执行

3. 可以把我们实现Runnable接口的`run()`方法看成构建了一个新线程的任务，而Thread就是为了实现这个任务

4. `run()`会是新线程所执行的第一项方法

5. Runnable这个接口**只有一个**方法

6. 有些线程会因为某些原因而**阻塞**



#### 线程的并发性问题以及解决办法——synchronize

1. 并发性问题一：**“线程同步问题”**
	1. 用`synchronize`来修饰方法，使得某线程在执行该方法的时候必须要执行完另一个线程才能执行
	2. 线程同步问题的解决方法——用`synchronize`来修饰存取数据的方法，从而保护重要、多个线程共享的数据。
2. 并发性问题二：**“丢失更新(Lost Update)”**
	1. 两个线程在执行同一个任务时并不知道对方所做过的事情，可能会掩盖掉对方所做过的事情。
	2. 解决方法：`synchronize`标记方法来使得操作数据的过程原子化。一旦线程进入了方法，必须确保其他线程可以进入该方法之前，这个方法以及被进入的线程完成了，而不会停留在该方法的某一处。
3. 同步化方法可能会引发“死锁(deadlock)问题”



#####  要点

1. `sleep()`需要在`try-catch`中执行，可能会抛出`InterruptedException`异常。

2. 多个线程时需要判断哪些指令是**不能够分割执行**的。

	> 例如查询余额和取钱等

3. 使用`synchronize`来防止两个线程同时进入**同一个对象**的**同一个方法**

4. 对象就算有多个同步化的方法，**也只有一个锁**。一旦某个线程进入该对象的同步化方法，其他线程就**无法进入**该对象的**任何**同步化线程。



**SimpleChatClient**

```java
package nju;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * @author Zyi
 */
public class SimpleChatClient {

    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public static void main(String[] args) {
        SimpleChatClient client = new SimpleChatClient();
        client.go();
    }

    public void go() {

        JFrame frame = new JFrame("Ludicrously Simple Chat Client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 500);
        frame.setVisible(true);
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class IncomingReader implements Runnable {
        @Override
        public void run() {
            String message;
            try {

                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

```

***



## 25 Java字节码 解析

**四种方法调用字节码指令**

1. invokestatic：静态方法
2. invokespecial：实例构造器\<init\>方法，私有方法和父类方法
3. invokevirtual：虚方法
4. invokeinterface：接口方法，会在运行时再确定一个实现次接口的对象
5. invokedynamic：先在运行时动态解析出调用点限定符所引用的方法，然后再执行该方法。把如何查找目标方法的决定权从虚拟机转嫁到具体用户代码之中，让用户有更高的自由度。



#### Class文件结构

1. 一组以8位字节为基础的二进制流
2. 魔数：0xCAFEBABE
3. 版本号
4. 常量池：字面量、符号引用
5. 访问标志
6. 类索引、父类索引与结构索引集合
7. 字段表集合
8. 方法表集合
9. 属性表集合（代码的实现作为一个属性值）

***



## 26 JVM命令的执行

**逻辑内存地址空间**

> 1. 栈是从低地址到高地址的，即栈顶在高地址
> 2. 堆是从高地址到低地址的，即堆顶在低地址

**JVM运行时数据区**

1. pc(Program Counter 程序计数器)
2. Java虚拟机栈
3. 堆（对象实际存放的区域）
4. 方法区
5. 运行时常量池
6. 本地方法栈

> 在多线程中：
>
> 1. 所有线程共享一个方法区(运行时常量池在其中)和堆。
> 2. 每个线程有自己的本地方法栈、PC和Java虚拟机栈。
