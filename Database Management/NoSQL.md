# NoSQL



## 1 NoSQL的由来

**阻抗失谐**

* 基于关系代数(relational algebra)，关系模型把数据组织成“关
	系”(relation)和“元组”(tuple)。
	* 元组是由“键值对”(name-value pair)构成的集合。
	* 而关系则是元组的集合。
	* SQL操作所使用及返回的数据都是“关系”
	* 元组不能包含“嵌套记录”(nested record)或“列表”(list) 等任何结构
* 而内存中的数据结构则无此限制，它可以使用的数据组织形比“关系”更丰富。
* 关系模型和内存中的数据结构之间存在差异。这种现象通常称为“阻抗失谐”。
	* 如果在内存中使用了较为丰富的数据结构，那么要把它保存到磁盘之前，必须先将其转换成“关系形式。于是就发生了“阻抗失谐”：需要在两种不同的表示形式之间转译



**解决方法**：

1. 面向对象数据库
2. “对象-关系映射框架”（Object-Relational-Mapping Framework），通过映射模式表达转换。

**问题**：

1. 查询性能问题
2. 集成问题



## 2 聚合

把一组相互关联的对象视为一个整体单元来操作，而这个单元就叫聚合（aggregate）

* 通过原子操作（atomic operation）更新聚合的值（含一致性管理）
* 以聚合为单位与数据存储通信
* 在集群中操作数据库时，用聚合为单位来复制和分片
* 由于程序员经常通过聚合结构来操作数据，故而采用聚合也能让其工作更为轻松。

面向聚合操作数据时所用的单元，其结构比元组集合复杂得多

> "键值数据库"、"文档数据库"、"列族数据库"

### 2.1 聚合无知

关系型数据库的数据模型中，没有“聚合”这一概念，因此我们称之为“聚合无知”（aggregate-ignorant）

> “图数据库”也是聚合无知的

* 聚合反应数据操作的边界，很难在共享数据的多个场景中“正确”划分，对某些数据交互有用的聚合结构，可能会阻碍另一些数据交互
	* 在客户下单并核查订单，以及零售商处理订单时，将订单视为一个聚合结构就比较合适。
	* 如零售商要分析过去几个月的产品销售情况，那么把订单做成一个聚合结构反而麻烦了。要取得商品销售记录，就必须深挖数据库中的每一个聚合。
* 若是采用“聚合无知模型”，那么很容易就能以不同方式来查看数据
	* 在操作数据时，如果没有一种占主导地位的结构，那么选用此模型效果会更好。



## 3 主要的NoSQL数据模型

### 3.1 键值数据模型与文档数据模型

这两类数据库都包含大量聚合，每个聚合中都有一个获取数据所用的键或ID。

两种模型的**区别**是:

* 键值数据库的聚合不透明，只包含一些没有太多意义的大块信息
	* 聚合中可以存储任意数据。数据库可能会限制聚合的总大小，但除此之外，其他方面都很随意
	* 在键值数据库中，要访问聚合内容，只能通过键来查找

* 在文档数据库的聚合中，可以看到其结构。
	* 限制其中存放的内容，它定义了其允许的结构与数据类型
	* 能够更加灵活地访问数据。通过用聚合中的字段查询，可以只获取一部分聚合，而不用获取全部内容
	* 可以按照聚合内容创建索引



### 3.2 列族存储

大部分数据库都以行为单元存储数据。然而，有些情况下写入操作执行得很少，但是经常需要一次读取若干行中的很多列。此时，列存储数据库将所有行的某一组列作为基本数据存储单元。

列族数据库将列组织为列族。每一列都必须是某个列族的一部分，而且访问数据的单元也得是列。

> 某个列族中的数据经常需要一起访问。

列族模型将其视为**两级聚合结构**（two-level aggregate structure）。

* 与“键值存储”相同，第一个键通常代表行标识符，可以用它来获取想要的聚合。
* 列族结构与“键值存储”的区别在于，其“行聚合”(row aggregate)本身又是一个映射，其中包含一些更为详细的值。这些“二级值" (second-level value)就叫做“列”。与整体访问某行数据一样，我们也可以操作特定的列。

**两种数据组织方式**

* 面向行（row-oriented）：每一行都是一个聚合（例如ID为1234的顾客就是一个聚合），该聚合内部存有一些包含有用数据块（客户信息、订单记录）的列族
* 面向列（column-oriented）：每个列族都定义了一种记录类型（例如客户信息），其中每行都表示一条记录。数据库中的大“行”理解为列族中每一个短行记录的串接。

### 3.3 面向聚合的数据模型
