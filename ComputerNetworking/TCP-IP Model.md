# TCP/IP Model

## 概述

OSI 模型只是一个参考/逻辑模型。它旨在通过将通信过程划分为更小和更简单的组件来描述通信系统的功能。但是当我们谈到 TCP/IP 模型时，它是由美国国防部 (DoD) 在 1960 年代设计和开发的，并且基于标准协议。它代表传输控制协议/互联网协议。TCP/IP模型仅仅包括四层：

1. Application Layer（应用层）
2. Transport Layer（传输层）
3. Internet Layer（网络层）
4. Network Access Layer（网络接入层）

![img](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/tcpAndOSI.png)

## TCP/IP Model Introduction

### TCP/IP Model 与 OSI Model 的差异

|                            TCP/IP                            |                             OSI                              |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
|              TCP: Transmission Control Protocol              |              OSI: Open Systems Interconnection               |
|                      TCP/IP Model有四层                      |                       OSI Model有七层                        |
|                     TCP/IP Model更加可靠                     |                      OSI Model更不可靠                       |
|                TCP/IP Model没有非常严格的界限                |                  OSI Model有比较严格的界限                   |
| TCP/IP Model将Session Layer和Presentation Layer合并进了Application Layer | OSI Model 将Session Layer 和 Presentation Layer与Application Layer单独分离开 |
|         TCP/IP Model是开发协议然后根据协议来建立模型         |                OSI Model是根据模型来建立协议                 |
|     Transport Layer并没有提供数据包（packets）的可靠交付     |            Transport Layer提供了数据包的可靠交付             |
|        TCP/IP Model Internet Layer只提供无连接服务。         | OSI Model 中的 Network Layer 提供了无连接和面向连接的两种服务 |
|               TCP/IP中的协议并不能够被随意替代               | OSI Model中，协议被更好地覆盖，并且很容易随着技术的变化而被替换。 |



### Network Access Layer（网络接入层）

TCP/IP Model中的 Network Access Layer对应的是 OSI Model 中的 Physical Layer（物理层）和 Data Link Layer（数据链路层）。它寻找硬件地址，并且该层中存在的协议允许数据的物理传输。 我们刚才讲了ARP是Internet层的协议，但是将其声明为Internet Layer的协议还是Network Access Layer的协议是有冲突的。它被描述为位于第 3 层，由第 2 层协议封装。



### Internet Layer（网络层）

TCP/IP Model中的 Internet Layer与 OSI Model中的 Network Layer是对应的，它定义了负责在整个网络上进行数据逻辑传输的协议。驻留在这一层的主要协议是：

1. **Internet Protocol（IP）**：该协议负责通过查看数据包标头中的 IP 地址将数据包从源主机传递到目标主机。 IP有2个版本： IPv4 和 IPv6。 IPv4 是目前大多数网站都在使用的一种。但是 IPv6 正在增长，因为与用户数量相比，IPv4 地址的数量是有限的。
2. **Internet Control Message Protocol（ICMP）**：它封装在 IP 数据报中，负责向主机提供有关网络问题的信息。
3. **Address Resolution Protocol（ARP）**：它的工作是从已知的 IP 地址中找到主机的硬件地址。 ARP 有几种类型：反向 ARP（Reverse ARP, RARP）、代理 ARP（Proxy ARP）、免费 ARP（Gratuitous ARP） 和反向 ARP（Inverse ARP）。



### Transport Layer（传输层）

TCP/IP Model中的 Transport Layer与 OSI Model中的Transport Layer是对应的，主要负责端到端（end-to-end）的传输和流控制（Flow Control）以及错误纠正（Error Correction）。它使上层应用程序免受数据复杂性的影响。该层中存在的两个主要协议是：

1. **Transmission Control Protocol（TCP）**：该协议能够在两个终端之间提供无差错和可靠的传输，同时能够做到对数据的分段和分序（sequencing and segmentation）。同时TCP协议能够做到对数据的流控制（Flow Control），但其缺点是其功能过多导致其传输成本较大。
2. **User Datagram Protocol（UDP）**：该协议是比较新的传输层协议，其目的就是为了能够在传输成本比较小的情况下来进行传输。如果您的应用程序不需要可靠的传输，它是首选协议，因为它非常具有成本效益。与面向连接的协议 TCP 不同，UDP 是无连接的。



### Application Layer（应用层）

TCP/IP Model中的 Application Layer与 OSI Model中的上三层：Application Layer、Presentation Layer与Session Layer相对应。该层负责的是节点到节点（node-to-node）的传输以及控制用户界面规范。该层的协议主要有：HTTP, HTTPS, FTP, TFTP, Telnet, SSH, SMTP, SNMP, NTP, DNS, DHCP, NFS, X Window, LPD等。下面简要介绍几种：

1. **Hypertext Transfer Protocol（HTTP）**：超文本传输协议，万维网使用它来管理 Web 浏览器和服务器之间的通信。 HTTPS 代表 HTTP 安全。它是 HTTP 与 SSL（安全套接层）的组合。在浏览器需要填写表格、登录、验证和进行银行交易的情况下，它非常有效。
2. **Secure Shell（SSH）**：它是一个类似于 Telnet 的终端仿真软件。 SSH 更受欢迎的原因是它能够维护加密连接。它通过 TCP/IP 连接建立安全会话。
3. **Network Time Protocol（NTP）**：它用于将我们计算机上的时钟同步到一个标准时间源。它在银行交易等情况下非常有用。假设以下情况不存在 NTP。假设您执行一项事务，您的计算机在下午 2:30 读取时间，而服务器在下午 2:28 记录时间。如果服务器不同步，服务器可能会严重崩溃。



## 总结

TCP/IP Model是目前主流使用的互联网模型，其能够替代OSI Model的原因一个是因为其的层数减少了，OSI过多的界限导致其无法很好的应用到实际中，另一个是因为 TCP 协议与 IP 协议的产生使得互联网得到了很好的发展。



## Reference

1. 南京大学软件学院2022春季学期《互联网计算》课程
2. [TCP/IP Model](https://www.geeksforgeeks.org/tcp-ip-model/)