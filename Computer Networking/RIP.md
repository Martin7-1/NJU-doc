# RIP

## 0 Overview

RIP, Router Information Protocol, 是一种根据跳数（hop count，既需要经过多少个中间设备才能够到达目标设备）来进行路由度量（routing metrix）从而在发送方和接收方之间决定最佳路径（best path）的动态路由协议。

**What is Hop Count**

跳数指的是在发送方和接收方之间报文经过的路由器数量，经过最少路由器的路径通常被认为是最佳路径，且会作为当前的一个路径选择被保留在路由表之中（某个报文到达某个路由器的时候会通过查询路由表来获得接收方地址应该从哪个端口转发）。RIP通过限制最远跳数（大于等于16跳的接收地址被认为是不可达）来防止 Routing Loops。

## 1 Features of RIP

1. 路由器之间的路由表信息交换是定期的，即相邻的路由器会定期学习对方的路由表以确定如果需要到达某个目标主机需要从哪个端口转发。
2. 路由信息的更新一般都是通过广播的形式。
3. 全部的路由表信息在更新时被发送
4. 路由器会相信从相邻路由器而来的路由表，这可能会导致一定的问题（比如有人在传输时修改了该路由表）

RIP主要分为version1和version2以及RIPng，下面简要介绍一下前两种的区别

|   RIP Version1         |   RIP Version2         |
|   ------------         |   -------------        |
|   只支持有类路由协议      |   支持无类路由协议        |
|   更新路由时不会有子网信息 |   更新路由时会携带子网信息 |
|   不支持VLSM，同一网段的所有设备必须有相同的子网掩码| 支持VLSM，可以划分不同的子网|
|通过255.255.255.255来进行广播|通过244.0.0.9来进行多播|

**VLSM**：Variable Length Subnet Mask，变长的子网掩码，允许划分不同大小的子网，拥有不同的子网掩码，以此来减少IP地址的浪费。

**RIP Version 1**

RIP Version1 是一种距离向量路由协议（distance vector protocol），会根据预先设置好的时间定期与相邻的路由器交换路由表，在上面的对比中提到它是一种只支持有类路由协议，且更新路由表时发送的信息没有子网信息，且不支持VLSM，即同一网段的所有设备必须有相同的子网掩码，同时，它也不支持更新消息的身份验证（authentication of updated messages），RIP Version1 通过 255.255.255.255 来进行广播。

**RIP Version 2**

RIP Version2 在 Version1 的基础上进行了改进，此时的 RIP 支持无类路由协议而且在更新路由发送更新消息时会携带子网信息，同时它也是支持VLSM的，它支持更新消息的身份验证，通过 224.0.0.9 来进行多播

## 2 configure RIP

可以使用 `debug ip rip` 来获得路由器之间通过rip交换的路由表的具体内容，使用 `show ip route`来看该路由器的所有路由配置，使用 `show ip protocols` 来看路由器上的所有配置的协议以及具体的内容

![configure RIP](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220606194509.png)

Configure RIP for R1:

```
R1(config)# router rip
R1(config-router)# network 192.168.20.0
R1(config-router)# network 172.16.10.4
R1(config-router)# version 2
R1(config-router)# no auto-summary
```

> `no auto-summary` 命令禁用自动汇总。如果我们不选择任何自动汇总，则子网掩码将在版本 1 中被视为有类。

`network` 后面跟着的是要配置的网段，在这里有两个网段，一个是192.186.20。0，一个是172.16。10.4（注意子网掩码长度为30，因此网络号应该到第30位）。

Configure RIP for R2

```
R2(config)# router rip
R2(config-router)# network 192.168.10.0
R2(config-router)# network 172.16.10.0
R2(config-router)# version 2
R2(config-router)# no auto-summary
```

Configure RIP for R3

```
R3(config)# router rip
R3(config-router)# network 10.10.10.0
R3(config-router)# network 172.16.10.4
R3(config-router)# network 172.16.10.0
R3(config-router)# version 2
R3(config-router)# no auto-summary
```