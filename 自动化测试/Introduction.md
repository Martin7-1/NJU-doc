# 自动化测试 Introductaion

## 测试的基本问题

1. 测试设计问题：假如你来提问，你会问什么问题
2. 测试预言问题：假如你来回答，你会如何来作答
3. 测试终止问题：假如你来判定，你会何时来终止

> 自动化：软件测试的一个梦想，通过自动化的工具来生成、判定、定义评估标准

## 自动化软件测试的系统性研究与实践

1. 自动化测试生成
2. 自动化测试执行
3. 自动化测试优化
4. 自动化测试修复
5. 自动化测试扩增
6. 自动化结果分析

## 什么叫 Bug

1. Software Fault: A static defect in the software（静态存在于软件中的缺陷，如字母拼写出错）
2. Software Error: An incorrect internal state that is the manifestation of some fault（存在的错误，如空指针）
3. Software Failure: External, incorrect behavior with respect to the requirements or other description of the expected behavior（已经在可以从外部观测到的失效的行为）

## PIE Model

PIE 模型：使用户或测试人员观测到failure的过程

1. Execution: 执行时必须通过错误
2. Infection: 项目的状态必须是错误的
3. Propagation: 错误的中间状态必须传播到最后输出，使得观测到的输出结果和预期结果不一致，即失效。

测试的难点在于：测试可能不会按照 PIE 模型的预期，有以下几种情况导致 Bug 无法被发现

1. 有些代码不会触发到fault（通过测试覆盖率来达到一定的解决）
2. 产生fault的程序，有可能不会产生error
3. 有fault和error也有可能不会导致failure（输入经过 bug 但是不一定会和预期结果产生差异）

## 测试的局限性

1. 输入空间庞大，无法穷举所有的输入
2. 实现逻辑复杂：无法想到所有场景
3. 测试预言未知：无法判定测试的预期输出

## 随机测试（大数定律）

只要测试执行次数够多、测试数据随机生成，则可能发生概率低的偶然现象

## 发现的 Bug 类型

1. 断言失败
2. 无效输入
3. 异常崩溃
4. 错误输出

## 常见的测试类型

### 变异测试

早期：变异测试旨在找出有效的测试用例，发现程序中真正的错误。（通过将原程序变错，看测试用例是否会发现该错误）。

模糊测试中的变异：变的是数据

### 蜕变测试

蜕变测试（Metamorphic Relation, MR）是指多次执行目标程序时，输入与输出之间期望遵循的关系。

eg：对 sin 函数的蜕变关系：

1. MR1: $\sin(x) = \sin(x+2 \pi)$
2. MR2: $\sin(x) = - \sin(x + \pi)$
3. MR3: $\sin(x) = - \sin(- x)$
4. MR4: $\sin(x) = \sin(2 \pi - x)$
5. MR5: $\sin(x) = - \sin(2 \pi - x)$
6. MR6: $\sin 2(x) + \sin 2(\frac{\pi}{2} - x) = 1$

### 差分测试

基本思想：通过将同一测试用例运行到一系列相似功能的应用中观察执行差异来检测 bug