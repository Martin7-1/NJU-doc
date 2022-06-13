# Java Concurrency

> 先来看几个概念



## “线程”与“进程”的区别

1. **进程**：进程是系统进行资源分配和调度的一个独立单位，也是一个具有独立功能的程序；
2. **线程**：线程<span style = 'color: red'>**依托进程而存在**</span>，是CPU调度和分派的基本单位，它是比进程更小的能独立运行的基本单位。线程自己基本上不拥有系统资源，只拥有一点在运行中必不可少的资源（如`PC(Program Counter)`，一组寄存器和栈），但是它与同属一个进程的其他线程共享进程所拥有的的全部资源。

> 以Java虚拟机（JVM）来举例，在虚拟机中，以下东西是**线程共享**的：
>
> 1. **堆**（Heap）：存储对象实例（new）
> 2. **方法区**（Method Area）：存储被虚拟机加载的类信息、常量、静态变量等
>
> 以下东西是每个**线程独有**的：
>
> 1. **栈**（Stack）：保存线程执行方法时的信息
> 2. **本地方法栈**（Native Method Stack）：执行native方法，注意此时PC为空
> 3. **程序计数器**（Program Counter Register）



## “并发”与“并行“的区别

1. **并发**

	> 并发指的是**关于正确有效地控制对共享资源的访问**，即我们并没有将资源扩大或者翻倍，而是通过对不同线程的控制来实现对相同资源的访问，只不过我们通过巧妙的处理让不同线程之间的切换速度达到了一个阙值，从而使得在外部看起来这几个线程确实在同时运行。

2. **并行**

	> 并行指的是**使用额外的资源来更快地产生结果**。即我们增加了任务的处理速度，通过更多的资源来实现真正的同时处理任务。这在计算机内部表现为通过“多处理器”的方式来分配不同的任务，从而实现并行计算。

**Tips:** 当然，并发与并行在很多时候是存在重叠的，并发通常表示“不止一个任务正在执行”。而“并行”则表示“不止一个任务同时执行”。可以看出并发其实是一种特殊的并行。此外，为并行编写的程序其实也可以在单处理器上运行，而并发编写的程序也可以利用多处理器。



## 并发的新定义（On Java 8）

> 并发性是一系列专注于减少等待的性能技术

1. **何为减少等待**

	在我看来，减少等待的意思是在你拥有多个任务的时候，如果其中某个任务需要等待其他任务的完成，通过并发，你可以将该任务在等待的这段时间运用起来，从而提高效益。例：I/O中十分常见的堵塞就是一种等待的情况，如果有这种情况发生，我们可以通过并发来充分利用该时间去处理其他的任务。

> 值得强调的是，这个定义的有效性取决于“等待”这个词。如果没有什么可以等待，那就没有机会去加速。如果有什么东西在等待，那么就会有很多方法可以加快速度，这取决于多种因素，包括系统运行的配置，你要解决的问题类型以及其他许多问题。 -- 《On Java 8》



## Java并发的四条格言

> 1. 不要用它（避免使用并发）
> 2. 没有什么是真的，一切可能都有问题
> 3. 仅仅是它能运行，并不意味着它没有问题
> 4. 你必须理解它（逃不掉并发）



### 1.不要用它（避免使用并发）

> 切记不要自己去实现它

**Q：**为什么要使用并发？

**A：**唯一正当理由就是为了<span style = 'color: red'>**速度**</span>，只有在没有办法实现其他方法来优化它的时候，才应该去考虑有并发来提高程序的运行速度。 -- 我们应该首先用一个分析器来发现你是否可以执行其他一些优化

> 如果你被迫使用并发，请采取最简单，最安全的方法来解决问题。使用知名的库并尽可能少地自己编写代码。



### 2.没有什么是真的，一切可能都有问题

> 在并发世界中，我们并不能够确定那些事情是真的哪些事情是假的。我们必须对一切保持怀疑的态度，因为我们并不知道内部是如何分配任务给各个线程的，可能再一次运行就会出现不同的结果。

在并发编程中，不同的线程可能会修改共享的数据，这就会造成我们的程序出现错误，但很多时候我们并不能够轻易的知道错误究竟发生在何处，这时候我们就需要深刻了解一些重要的机制。

> 在非并发编程中你可以忽略的各种事情，在并发下突然变得很重要。例如，你必须了解处理器缓存以及保持本地缓存与主内存一致的问题，你必须理解对象构造的深层复杂性，这样你的构造函数就不会意外地暴露数据，以致于被其它线程更改。这样的例子不胜枚举。 -- 《On Java 8》



### 3.仅仅是它能运行，并不意味着它没有问题

> - 你不能验证出并发程序是正确的，你只能（有时）验证出它是不正确的。
> - 大多数情况下你甚至没办法验证：如果它出问题了，你可能无法检测到它。
> - 你通常无法编写有用的测试，因此你必须依靠代码检查和对并发的深入了解来发现错误。
> - 即使是有效的程序也只能在其设计参数下工作。当超出这些设计参数时，大多数并发程序会以某种方式失败。



### 4.你必须理解它

Java 是一种多线程语言，尽管我们在尽力的避免使用并发编程，但我们终究不能够避免它。Web 系统是常见的 Java 应用之一，本质上是多线程的 Web 服务器，通常包含多个处理器。



## 并行流（Parallel Streams）







## ExecutorService

> Java.util.concurrent包



### Runnable

`Runnable`只是定义了**所需要做的任务**，它是一种函数式接口（**Functional Interface**），创建线程并不能单单通过实现`Runnable`接口来创建，而是要通过`Thread`

```java
package com.nju.edu.concurrency;

public class LiftOff implements Runnable {

    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount++;

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id +"(" + (countDown > 0 ? countDown : "LiftOff!") + "), ";
    }

    @Override
    public void run() {
        // runnable接口仅仅布置任务
        while (countDown-- > 0) {
            System.out.println(status());
            Thread.yield();
        }
    }
}
```



### newCachedThreadPool

`Executors.newCachedThreadPool`创建的线程池会根据需要来创建线程。

1. 如果在提交任务的时候现有线程没有可用的，就创建一个新线程并添加到线程池中。
2. 如果在提交任务的时候有**被使用完但是还没有被销毁的线程**，`newCachedThreadPool`就会复用该线程。

``` java
package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nju.edu.concurrency.LiftOff;

public class CachedThreadPool {

    public static final int THREAD_AMOUNT = 3;

    public static void main(String[] args) {
        // 根据需要创建线程
        // 如果现有线程没有可用的，就创建一个新线程并添加到池中
        // 如果有被使用完但还没有被销毁的，就复用该线程
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < THREAD_AMOUNT; i++) {
            // 根据需要创建新线程的线程池
            exec.execute(new LiftOff(10));
        }
        exec.shutdown();

    }
    
}
```



### FixedThreadPool

``` java
package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nju.edu.concurrency.LiftOff;

public class FixedThreadPool {

    public static final int THREAD_AMOUNT = 5;

    public static void main(String[] args) {
        // 创建固定线程数的线程池
        // 任何时候都只有固定的线程数被创建
        // 如果所有线程都在活动状态时有其他任务提交，会在等待队列中直到有线程可用
        ExecutorService exec = Executors.newFixedThreadPool(THREAD_AMOUNT);

        for (int i = 0; i < THREAD_AMOUNT; i++) {
            exec.execute(new LiftOff(10));
        }
        exec.shutdown();
    }
    
}
```



### Callable and Future

`Callable`和`Runnable`是用处比较接近的两个接口，不同的是以下几点：

1. `Callable`接口能够返回异步执行的任务结果，而且可以抛出异常
2. `Runnable`接口没办法返回结果和抛出一个检查到的异常



`Future<V>`是一种用来保存异步计算结果的接口，其中的`get()`方法会阻塞直到`Callable`返回当前线程执行的结果。

#### 示例代码

> 定义一个属于自己的`Callable`类并覆盖其中的`Call()`方法

```java
package com.nju.edu.concurrency;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("做一些耗时的任务...");
        // sleep()方法参数为毫秒
        Thread.sleep(5000);

        return "OK";
    }
    
}
```

```java
package com.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.nju.edu.concurrency.MyCallable;

public class FutureSimpleDemo {
    
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 提交任务
        Future<String> future = executorService.submit(new MyCallable());

        System.out.println("do something...");
        try {
            // future.get()会堵塞直到Callable的任务做完并且返回结果
            System.out.println("得到异步任务返回结果: " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Completed!");
    }
}
```



> 上述代码的`future.get()`会一直阻塞直到当前线程的`Call()`方法返回结果。



### Thread.yield()

* `Thread.yield()`方法会临时暂停当前线程，让有同样优先级但是正在等待的线程有机会开始执行。

* 若没有正在等待的线程或者所有正在等待的线程的优先级都比较低，则当前执行`yield()`方法的线程可能会继续执行

* 执行`yield()`的线程何时会继续运行是由操作系统的线程调度器来决定的，我们无法去判断其什么时候会再次执行

* `yield()`方法不保证当前的线程会暂停或停止，但是可以保证当前线程在调用`yield()`方法时会放弃CPU

	> 即：在调用`yield()`方法的时候，虽然当前线程确实因为该方法暂停了，但是由于在所有线程中可能并没有其他线程的优先级是高于该线程的，所以该线程仍然可能继续被线程调度器选中从而继续执行，对于这种情况`yield()`并没有发挥我们设想中的作用。

<span style = 'color: red'>**注意：**</span> `yield()`方法**并不会释放**该对象的锁，即对加了锁的对象如果我们使用了`yield()`方法，即使该线程放弃了CPU，但因为该线程仍然持有该对象的锁，所以其他的线程仍然是访问不到该对象的，只有当线程调度器再次“唤醒”该线程的时候，该线程执行完某个加锁方法后才会将锁还给对象以供其他的线程来使用。



### Thread.sleep() -- old style

> `sleep()`方法与`yield()`方法大致相同，有以下几点不同：

* 当线程调用`sleep()`方法的时候，该线程将会被阻塞挂起**固定的时间**，在这期间线程调度器不会去调度该线程。
* 当线程调用`yield()`方法的时候，线程只是让出了自己剩余的时间片，但其状态仍然是**准备就绪**，即一直在等待线程调度器来调用它，也就会出现上面的刚`yield()`就马上又被调度的情况，从而导致`yield()`方法并没有发挥其应该有的作用。

#### 示例代码

```java
package com.nju.edu.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepingTask implements Runnable {

    private int countDown = 10;

    @Override
    public void run() {
        // TimeUnit.MILLISECONDS.sleep()方法可能会抛出异常
        // Thread.sleep()方法是过去使用的，现在都用上面这个方法
        try {
            while (countDown-- > 0) {
                // Old-style: Thread.sleep(100);
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(countDown);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
    }

    public static final int THREAD_AMOUNT = 5;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("before sleeping...");

        for (int i = 0; i < THREAD_AMOUNT; i++) {
            executorService.execute(new SleepingTask());
        }

        executorService.shutdown();
    }
    
}
```



#### new-style

现在经常使用`concurrent`包下面的`TimeUnit`来代替`Thread.sleep()`方法

> 原因：`TimeUnit`能够指定时间的单位，而不是跟`sleep()`一样一直保持毫秒，且没有在明面上体现毫秒

**例**：

1. `TimeUnit.SECOND.sleep(1) // sleep一秒`
2. `TimeUnit.MILLISECOND.sleep(100) // sleep一百毫秒`



## Priority 优先级

> 线程是有优先级的，但是我们尽量不要去更改各个线程的优先级，而是让线程调度器来分配究竟什么线程的优先级比较高

### 实例代码

``` java
package com.nju.edu.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplePriorities implements Runnable {

    private int countDown = 5;
    // Java中轻量级的锁
    private volatile double d;
    private int priority;

    public SimplePriorities(int priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(this.priority);

        while (true) {
            for (int i = 0; i < 100000; i++) {
                d += (Math.PI + Math.E) / (double) i;
                if (i % 1000 == 0) {
                    Thread.yield();
                }
            }
            System.out.println(this);
            if (--countDown == 0) {
                return;
            }
        }
    }

    @Override
    public String toString() {
        return Thread.currentThread() + ": " + this.countDown;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        executorService.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        executorService.shutdown();
    }
}
```



## Daemon线程





## Synchronized

`Synchronized`修饰某个方法意味着某个线程在调用该方法的时候必须要获得该**对象**的锁。



列表
