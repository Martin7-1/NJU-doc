# Interface

## 接口

​	**接口与百分百abstract的类一样**：为了避免继承多个类

​	**Java中不允许“多继承”**。而应该改用接口

​	**接口**: 所有方法都是抽象的，只给出接口不给出实现，方法末尾应该有分号。

​	public interface Pet{}

​	public class Dog extends Caine implements Pet{}

​	**子类必须实现接口的所有方法**

​	**Extends one & Implements more**

​	public class Dog extends Animal implements Pet, Saveable, Paintable{}

​	**接口也可以继承**

​	**接口中定义的数据成员自动static和final(相当于常量)**

## class vs interface

​	1、是用具体的类

​		锁定某个具体的实现

​		丧失可扩展性

​		丧失灵活性

​	2、按接口编程

​		增加开发的可并行性



### Iterator()

​	**Iterator()**: 遍历集合的方法

​		**hasNext()**: 看集合是否有下一个元素

​		**next()**: 获得集合的下一个元素

​	**for(Iterator i = HashSet.iterator(); i.hasNext())**

​	按接口编程：以接口为参数传入方法，接口限定了怎么做，但是可以根据具体实现在执行时动态绑定真正指向的具体对象。

​	用父类和接口来描述做什么，用子类来描述怎么做。



### 水位监察

​	子类覆盖了父类的push方法，编译的时候只看能不能执行，检测到引用变量为父类且其中有push()和push_many()方法，则可以执行。但是执行时候根据实际指向的对象为子类，尽管子类没有覆盖父类的push_many()方法，但在执行的时候push_many()方法仍然会调用子类的push方法。



​	**谨慎使用继承(重关系)，使用组合+接口的方法来替代继承，**



## Invokevirtual vs invokeinterface

​	继承的方法和父类在同一号槽上(优先排父类的方法)

​	接口的方法不在同一号槽上(不固定)

​	invokeinterface 的时间比 invokevirtual 慢

## default method in Java8

​	允许接口加default，并在其中添加实现。

