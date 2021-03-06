# OOC-Review

## 1 OO基础

### 1.1 从过程化到面向对象

#### 1.1.1 外部质量因素

1. Correctness 正确性：依据规约 完成任务
2. Robustness 鲁棒性：异常情况 合理反映
3. Integrity 完整性：非法访问或修改 合理反映
4. Extendilibity 易扩展性：软件产品应 规约改变 而 改变
5. Resusability 易复用性：软件模块 用于构建多种不同应用
6. Compatibility 兼容性：软件模块相互组合的难易
7. Efficiency 高效性：尽量少地使用硬件资源、处理器时间、内存、外存、网络带宽等
8. Portability 易移植性：转换到不同的软硬件平台上
9. Ease of use 易用性：不同背景的用户学习使用软件产品解决问题的难易

初次之外，外部质量因素主要考虑下面三个方面：

* Functionality：功能
* Timeliness：按时交付
* Verifiability，Reparability，Economy：可验证性、可修复型、经济性

#### 1.1.2 应对复杂性

> 复杂性是软件开发过程中所固有的特质

应对复杂性的基本途径：

1. 分解Decomposition：分而治之
2. 抽象Abstraction：抓本质、抓重点
3. 层次话Hierarchy：应对大系统，纲举目张

**结构化开发方法**

按照一定的原则与原理，组织和编写正确且易读的程序的软件技术，主要思想是自顶向下、逐步求精。

#### 1.1.3 模块化

模块化是软件构造为一组“模块”之有序组合，从而易于装配、易于修补替换模块。模块化是扩展性和易复用性的要求。模块化的方法有以下的五个目标和五个规则：

**五个目标**

1. Decomposability 易分解性：复杂的问题分解成多个子问题
2. Composability 易组合性：软件单元可以自由组合进而生成新的软件
3. Understandability 易理解性：模块应该可以被单独理解
4. Continuity 连续性：规约中的小变化也只会引起结构的小变化
5. Protection 保护性：异常不扩散或者扩散的范围是有限的

**五个规则**

1. Direct Mapping 直接映射
2. Few Interfaces 接口要少
3. Small Interfaces 接口要小：接口应该只获得需要的信息
4. Explicit Interfaces 接口要明确
5. Information Hiding 信息隐蔽：通过 `public` 接口和外界交互，其他都属于秘密，外界无法获得

模块的根本特征是“相对独立，功能单一”。换言之，一个好的模块必须具有高度的独立性和相对较强的功能。

**耦合度和内聚度**

* 耦合度，是指模块之间相互依赖性大小的度量，耦合度越小，模块的相对独立性越大
* 内聚度，是指模块内各成分之间相互依赖性大小的度量，内聚度越大，模块各成分之间联系越紧密，其功能越强。

在模块划分当中应该做到“耦合度尽量小，内聚度尽量大”（高内聚、低耦合）

#### 1.1.4 面向对象开发方法

**面向对象的基本思想**

1. 任何事物都是对象，对象有属性和方法。复杂对象可以由相对简单的对象以某种方式构成
2. 通过类比发现对象间的相似性，即对象间的共同属性，是构成对象类的依据
3. 对象间的互相联系是通过传递“消息”来完成的。通过对象之间的消息通信驱动对象执行一系列的操作从而完成某一任务

**基本概念**：对象、类、封装性、继承性、多态性、动态绑定、消息传递

### 1.2 抽象数据类型

#### 1.2.1 Overview

* 基于过程的抽象：指任何一个明确定义功能额度操作都可以被使用者看作单个的实体，尽管这个操作实际可能由一系列更低级的操作完成
* 基于数据的抽象：定义了数据类型和施加于该类型对象上的操作，并限定了对象的值只能通过这些操作修改和观察。包含了2个概念：
  * 模块封装
  * 信息隐藏

数据抽象提供了面向对象计算的起点：系统应该被分解为概念上的实体，实体的内部细节应该被隐藏

#### 1.2.2 Abstract Data Type

抽象数据类型（ADT）：用<span style='color: red'>**数学方法**</span>定义对象集合和运算集合，仅通过运算的性质刻画数据对象，而独立于计算机中可能的表示方法

ADT规约方法：**代数方法**

1. 语法部分
   * ADT名
   * 运算（函数）的定义域和值域
2. 公理部分
   * 给出一组刻画各运算之间相互关系的方程来定义各运算的含义
   * **语义正确性**：相应代数满足规约中公理部分的所有公理

![ADT-Stack](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/ADT-Stack.png)

![ADT-Function](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/ADT-Function.png)

**ADT函数分类**

一个ADT T中可以有三种函数（算子）：

1. Creators（构造算子）： $OTHER \rightarrow T$  e.g.new
2. Queries（观察算子）：$T \times ... \rightarrow \ OTHER$ e.g.item, empty
3. Commands（运算算子）：$T \times ... \rightarrow \ T$ e.g.put, remove

> 偏函数（Partial Functions），在ADT中用箭头加一个斜杠表示（即remove和item）。
>
> 偏函数是一种可能没有定义所有可能的参数的函数（即可能会有默认的参数），这样做是为了减少重复的参数传递，提高函数的适用性以及固定执行环境的上下文
> 
> 1. 对于 `item` 来说，其语义是“获得栈最顶层的元素”，这里就省略了给 `item` 传递元素位置 -- 最顶层 这个参数，因为这个参数是默认的，而且是符合栈的结构的，如果定义为可以让使用者传递参数，那么使用者随意传参来获取某个位置的元素就会导致 Stack 失去原有的信息隐藏性。
> 2. 对于 `remove` 来说，其语义是“删除栈最顶层的元素”，和 `item` 同理，也是省略了传递参数位置 -- 最顶层 这个参数，也是为了遵守 Stack 这个 ADT 的规约和信息隐藏，只能够对最顶层的元素进行操作（这里的 `remove` 其实就是 `pop`）

![ADT-Stack-Cond](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/ADT-Stack-Cond.png)

![ADT-Stack-complete](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220611214633.png)

**总结**

使用代数方法定义一个 ADT 的时候，需要有以下几个内容以及注意的点：

1. 首先是 ADT 的名字（TYPES，如果其是像 Stack 这种存储元素的列表，对于未定义的元素类型可以用类似泛型的参数定义）
2. 其次是 ADT 的函数部分，包含了该 ADT 能够支持的操作
3. ADT 的公理部分，需要定义一些在 ADT 中**最为基础**的行为，从而能够根据公理推出其他的任何代数表达式
4. 最后是 ADT 的前置条件，这是针对一些函数所需要的，比如要求非空等。

**注意点**

1. 注意某些函数定义时是使用偏函数还是正常的函数
2. 注意前置条件的考虑
3. 注意代数运算时需要满足的公理的考虑（即需要考虑如何定义最基本的运算含义）
4. ADT一致性：当且仅当对于任何格式良好的查询表达式 e，公理使得最多可以推断出 e 的一个值时，ADT 规范才是一致的。

#### 1.2.3 ADT and software architecture

* 找到所有的抽象数据类型的模块：使用抽象数据类型的实现来标识每个模块，即具有公共接口的一组对象的描述。
* 定义抽象数据类型中的操作以及定理：接口由一组操作（实现 ADT 的功能）定义，这些操作受抽象属性（公理和前提条件）的约束。
* 具体实现：该模块由抽象数据类型的表示形式和每个操作的实现组成。辅助操作也可能包括在内。

#### 1.2.4 信息隐蔽原则

使用 ADT 的程序应该只依赖于它的规约保证的性质，而不依赖于它的任何特定实现。即对于某一个 ADT 来说，使用者只需要遵守它的规约保证的性质，而不需要知道该 ADT 在内部是如何实现的。

### 1.3 契约式设计

> Design by Contract(Dbc) 契约式设计，与面向对象技术中的其它技术同等重要

契约式设计是一种保证软件质量（可靠性）的手段，程序中的每个元素都有它的目标（权利）和义务，两者组合在一起称为该元素的契约。

#### 1.3.1 契约式设计的基本原则

**Properties of contracts**

* 绑定双方或者多方
* 显式指出的
* 规定相互的义务和权利
* 一方的义务对应另一方的权利，反之亦然
* 没有隐式条约（即第二条，契约都应该显式的指出）
* 通常，依赖适用所有契约的一般规则

**契约式设计和assert的区别**

* `assert` 并没有显式的指定契约
* 使用者并不能够从接口中看到 `assert`（因为 `assert` 是在方法内部的，并没有体现在方法的接口上）
* `assert` 和语义没有关联
* `assert` 不是显式的，无论其作为前置条件、后置条件还是不变式
* `assert` 不支持继承

**契约就是“规范和检查”**

1. Precondition：针对某个方法，前置条件规定了在调用该方法之前必须为真的条件（即调用该方法之前必须要满足某些条件才能够调用）
2. Postcondition：针对某个方法，后置条件规定了方法顺利执行完毕之后必须为真的条件（即调用该方法之后会发生的情况，相当于告诉用户该方法能够保证改变的东西）
3. Invariant：针对**整个类**，不变量规定了该类任何实例调用任何方法都**必须为真的条件**

#### 1.3.2 Hoare Tripbles

契约式设计可以用一个三元组来表示：$\{P\} A \{Q\}$，其中：

1. P、Q：代表前置条件和后置条件，可能是某些计算后的状态
2. A：指令序列

该元组表示了某个变量满足 $P$ 的时候，通过 $A$ 的一系列运算，能够满足 $Q$ 中的条件，比如： $\{ n > 5\} n := n+9 \{ n > 13\}$

* Strongest postcondition(from given precondition)：$n > 14$
* Weakest precondition(from given postcondition)：$n > 4$
* $P$ is stronger than or equal to $Q$ means: $P$ implies $Q$

#### 1.3.3 契约式设计与继承

**Q：子类中的契约和父类中的契约是什么关系**

* 子类的前置条件要和父类一样或者比父类强
* 子类的后置条件要和父类一样活着比父类弱

子类可以使用 `require else` 削弱父类的先验条件（前置断言），即子类的前置断言要弱于父类的前置断言；子类可以使用 `ensure then` 加强父类的后验条件（后置断言），即子类的后置断言要强于父类的后置断言；子类可以用 `and` 把不变式子句和子类所继承的父类的不变式子句结合起来，加强不变式断言，即子类的不变式要强于父类的不变式。

#### 1.3.4 契约式设计与文档

契约能使文档更出色

* 更清晰的文档：契约乃是类特性的公开视图中的固有成分
* 更可靠的文档：运行时要检查断言，以便保证制定的契约与程序的实际运行情况一致
* 明确的测试指导：断言定义了测试的预期结果，并且由代码进行维护
* 更精确的规范既能够获得精确规范得到的益处，同时还使得程序员继续以他们所熟悉的方式工作

#### 1.3.4 契约式设计和防御式编程的异同

防御性编程的含义：防御性编程是一种细致、谨慎的编程方法。为了开发可靠的软件，我们要设计系统中的每个组件，以使其尽可能地“保护”自己。我们通过明确地在代码中对设想进行检查，击碎了未记录下来的设想。这是一种努力，防止（或至少是观察）我们的代码以将会展现错误行为的方式被调用。

相同点：都可以提高软件的可靠性。

不同点：
1. DbC中先验条件是程序文档的组成部分，而产生异常的语句是程序体本身的组成部分。
2. 采用注释来描述例程对参数的限制时，很难保证这个注释正确地描述了该限制。但可以相信具有显式先验条件检查的文档，因为断言在测试时得到了验证

### 1.4 异常

#### 1.4.1 异常分类

**定义**

1. Checked Exception：程序与外界交互所产生的异常，通常是由于外界环境出现未被考虑的情况而引起的。如找不到文件（`FileNotFoundException`）、错误的输入类型（`TypeMismatchException`）等
2. Unchecked Exception：程序本身存在的问题，如数组越界（`ArrayIndexOutOfBoundException`），除以零（`ArithmeticException`），调用空对象（`NullPointerException`）等
3. Error：在控制外围之外的低层系统错误，并不是程序本身的错误，但程序会因此受到影响而终止，如内存不足等等。

**使用区别**

1. Checked Exception：在**编译时被强制检查**的异常，必须为所有的Checked Exception 提供异常处理机制，否则编译不会通过
2. Unchecked Exception：在编程过程中可人为避免，编译器不会强制检查的异常，它的产生往往意味着处理逻辑的错误，我们需要对程序进行修改
3. Error：在控制范围之外，恢复比较困难，由于Error的产生往往导致程序本身运行所依赖的低层系统崩溃，程序本身没有办法对此进行处理，并且处理问题的责任往往不在程序本身，因为我们对于Error通常不需要处理，就让程序中止即可。

#### 1.4.2 Java中的异常

![Exception in Java](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220612170024.png)

**如何正确使用异常**

1. 捕获了某个异常就应该对它进行适当的处理，不要捕获异常之后又把它丢弃，不予理睬
2. 在 `catch` 语句中尽可能制定具体的异常类型，必要时使用多个 `catch`。不要试图处理所有可能出现的异常
3. 对一些打开的资源应该要记得释放，不能因为异常的抛出而不释放这些资源（使用 `finally` 关键字）
4. 在异常处理模块中提供适量的错误原因信息，例如当前正在执行的类、方法和其它状态信息，包括以一种更适合阅读的方式整理和组织 `printStackTrace()` 提供的信息使异常更加易于理解和阅读
5. 分离各个可能出现异常的段落并分别捕获其异常，尽量减小 `try` 块的体积
6. 全面考虑可能出现的异常以及这些异常对执行流程的影响

**Exception Principles in Effective Java**

> 《Effective Java》中关于异常机制的九大原则

1. Use Exceptions only for exceptional conditions（只针对不正常的条件才使用异常）
2. Use checked exceptions for recoverable conditions and runtime exceptions for programming errors（对于可恢复的条件使用被检查的异常，对于程序错误使用运行时异常）
   1. Java编译器会对“Checked Exception”进行检查，而对“Runtime Exception”不会检查
   2. 即对于“Checked Exception”，要么通过 `throws` 进行声明抛出，要么通过 `try-catch` 进行捕获处理，否则不能通过编译（通过程序处理恢复运行）
   3. 而对于“Runtime Exception”，倘若没有通过 `throws` 声明抛出，也没有用 `try-catch` 捕获，还是能够编译通过
3. Avoid unnecessary use of checked exceptions（避免不必要地使用被检查的异常）
   1. 过分使用“checked Exception”会使API用起来非常不方便
   2. 如果一个方法抛出一个或者多个“Checked Exception”，那么调用该方法的代码则必须在一个或者多个 `catch` 语句块中处理这些异常，或者必须通过 `throws` 声明抛出这些异常。无论是通过 `catch` 处理，还是通过 `throws` 声明抛出，都给程序员添加了不可忽略的负担
   3. 适用于“Checked Exception”必须同时满足两个条件：
      1. 第一：即使正确使用API并不能阻止异常条件的发生
      2. 第二：一旦产生了异常，使用API的程序员可以采取有用的动作对程序进行处理
4. Favor the use of standard exceptions（尽量使用标准的异常）
5. Throw exceptions appropriate to the abstraction（抛出的异常要适合于相应的抽象）
   1. 如果一个方法抛出的异常与它执行的任务没有明显的关联关系，这种情形会让人不知所措
   2. 为了避免这个问题，高层实现应该捕获低层的异常，同时抛出一个可以按照高层抽象进行介绍的异常。这种做法被称为**异常转译（exception translation）**
6. Document all exceptions thrown by each method（每个方法抛出的异常都要有文档）
7. Include failure-capture information in detail messages（在细节消息中包包含失败-捕获信息）
   1. 即当我们自定义异常或者抛出异常的时候，应该包含失败有关的信息
   2. 当一个程序由于一个未被捕获的异常而失败的时候，系统会自动打印出该异常的栈轨迹。在栈轨迹中包含该异常的字符串表示。典型情况下它包含改异常类的类名，以及紧随其后的细节信息。
8. Strive for failure atomicity（努力使失败保持原子性）
   1. 当一个对象抛出某个异常时，我们总期望这个对象仍然保持在一种定义良好的可用状态之中（而不是因为异常的发生而变的混乱或者不可用）
   2. 设计一个非可变对象
   3. 对于在可变对象上执行操作的方法，获得“失败原子性”的最常见方法，是在执行操作之前检查参数的有效性（防止非法参数的操作影响到对象的数据）
9. Don't ignore exceeptions（不要忽略异常）