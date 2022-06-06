# Exception

**try-catch** 来捕获异常

**throw** 在方法处来声明可能抛出的异常

常见异常:

 	1. NullPointerException 空指针异常
 	2. ArrayIndexOutOfBoundException 数组越界异常



try-catch-finally

可抛出多个异常，先抛出较为具体的异常(子类异常)

遇到异常只有两种选择

1. 要么处理 try catch
2. 要么抛出 throw 交给调用这个方法的方法来处理



简单的测试可以调用 **assert** 方法

应该在异常的原因处就捕获异常，而不是等到错误出现才捕获异常

早抛出 晚捕获



Java 7 try-with-resources语句

``` java
try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
    
} catch (IOException e) {
    
}
```

好处：系统自动关闭而不用手动关闭
