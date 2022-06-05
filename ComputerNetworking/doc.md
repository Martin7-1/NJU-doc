# 互联网计算

[toc]

## 1 计算机网络及其参考模型

### 1.1 Content

1. Overview of Computer Network
2. OSI Reference Model
3. TCP/IP Model
4. Network Topology
5. Network Devices



### 1.2 Networks

#### What is a network

A network is an intricately connected system of objects, devices, or people.

> 网络是由对象、设备或人员组成的错综复杂的连接系统

多个设备（也称为主机）的互联，它们使用多条路径连接，以发送/接受数据。计算机网络还可以包括有助于两个不同设备之间通信的多个设备/介质，这些被称为网络设备，包括路由器（routers）、集线器（hub）和网桥（bridges）之类的东西

![](https://s2.loli.net/2022/02/16/SdRUYrkjwpmLZIl.png)



#### Data Networks Classifications

1. LAN (Local Area Networks)
2. WAN (Wide Area Networks)

> 可以将网络分为 node 和 link。



**Local Area Networks**

* Operate locally (cover small areas)
* Multi-user access
* High speeds expected (up to Gbps / 10Gbps)
* Error rate is easily controlled

**Wide Area Networks**

* Operate over larger areas
* Access over serial links, optical links, etc.
* Traditionally, have Lower speeds
* Error rate can not be easily controlled



#### Internet with Multi-layer ISP structure



### 1.3 计算机网络中的一些概念

#### 1.3.1 Data

* Data is sent in bits, 1s and 0s

* Data is not the information itself

* Data is an encoded form of information which is a series of electrical impulses/optical signals into which information is transmitted for sending

	> 数据不是信息本身
	>
	> 数据是信息的编码形式，它是一系列电脉冲/光信号，信息被传输到其中以进行发送



#### 1.3.2 Data Packets

* For transmission, computer data is often broken into small, easily transmitted units
	* Using the OSI model, these units can be called packets, or frames or segments
* Why data packets?
	* Computers can take turns sending packets
	* If packet is lost, only small amount of data must be retransmitted.
	* Data can take different paths.

> * 对于传输，计算机数据通常被分解成易于传输的小单元
> 	* 使用 OSI 模型，这些单元可以称为数据包、帧或段
> * 为什么是数据包？
> 	* 电脑可以轮流发包
> 	* 如果数据包丢失，只需重新传输少量数据。
> 	* 数据可以采用不同的路径。



#### 1.3.3 Protocol（协议）

* It's possible for different types of computer systems to communicate
* All devices must speak the same "language" or use the same protocol（use same set of rules）

> * 不同类型的计算机系统可以进行通信
> * 所有设备必须使用相同的“语言”或使用相同的协议（使用相同的规则集）



#### 1.3.4 Source and Destination

* Source (源) address specifies the identity of the computer sending the packet
* Destination (终) address specifies the identity of the computer designated to receive the packet



#### 1.3.5 Media Types



#### 1.3.6 Digital Bandwidth (带宽)

Bandwidth is the measure of how much information can flow from one place to another in a given amount of time.

> 带宽是衡量在给定时间内可以从一个地方流向另一个地方的信息量。



#### 1.3.7 Throughput （通量）

> Throughput $\le$ bandwidth



### 1.4 OSI Reference Model

> OSI: Open System Interconnection

* Proposed by International Organization for Standardization (ISO)
* A network model that help network builders implement networks that could communicate and work together
* Describes how information or data moves from one computer through a network to another computer
* a layered communication process
	* Each layer performs a specific task

#### 1.4.1 七层模型

1. Application (应用层) -- User interface
2. Presentation (表示层) -- Data presentation and encryption
3. Session (会话层) -- Inter-host connection
4. Transport (传输层) -- End-to-end connections
5. Network (网络层) -- Addresses and best path
6. Data Link (数据链路层) -- Access to media
7. Physical (物理层) -- Binary transmission

![](https://s2.loli.net/2022/02/16/KiEbWeLkVw5ncxT.png)

##### 1.4.1.1 物理层（Physical Layer）

物理层是OSI模型中最底层的模型，它主要负责设备间真实的物理连接。物理层以`bits`的方式保存和传输数据。它负责将`bits`从一个节点传输到下一个节点。当接收到数据时，物理层会将信号转换成“01”串并且将其传输给`Data Link Layer`。`Data Link Layer`会将这些数据帧重新组合在一起。

![](https://s2.loli.net/2022/02/16/h34zmQ1rltjR8yG.png)

**物理层的作用**

* **Bit synchronization（位同步）**：物理层通过提供“时钟（clock）”来实现位同步。该时钟控制发送器和接收器，从而提供位级同步。
* **Bit rate control（位速率控制）**：物理层同时决定了数据的传输速率，即每秒传输的比特数
* **Physical topologies（物理拓扑）**：物理层指定不同的设备/节点在网络中的排列方式，即总线、星形或网状拓扑。
* **Transmission mode（传输方法）**：物理层还定义了数据在两个连接设备之间流动的方式。可能的各种传输模式是单工（simplex）、半双工（half-duplex）和全双工（full-duplex）。

> 集线器（Hub）、中继器（Repeater）、调制解调器（Modem）、电缆（Cables）是物理层设备。



##### 1.4.1.2 数据链路层（Data Link Layer）

数据链路层负责消息的节点到节点传递。该层的主要功能是确保通过物理层从一个节点到另一个节点的数据传输无差错。当数据包到达网络时，数据链路层 负责使用其 MAC（Media Address Control） 地址将其传输到主机。数据链路层可以划分成两个子层：

1. Logical Link Control
2. Media Access Control

![](https://s2.loli.net/2022/02/16/vj6Kw7uTQbe82rx.png)

**数据链路层的作用**

* **Frame（帧）**：成帧（Framing）是数据链路层的功能。它为发送者提供了一种方式来传输对接收者有意义的一组比特。这可以通过在帧的开头和结尾附加特殊的位模式来实现。
* **Physical addressing（物理寻址）**：在创建帧之后，数据链路层在每个帧的头中添加发送方和/或接收方的物理地址（MAC地址）
* **Error control（错误控制）**：数据链路层提供了一种控制错误的机制，它会发现并且重新传输损伤或者丢失的帧。
* **Flow control（流控制）**：双方的数据速率必须是恒定的，否则数据可能会被破坏，因此，流控制会协调在接收到确认之前可以发送的数据量。
* **Access control（权限控制）**：当一个单一的交流通道（a single communication channel）被多个设备共享时，数据链路层的子层 -- MAC会决定哪个设备在哪个时间能够拥有该通道的控制权。

> 数据链路层中的数据包称为**帧（Frame）**
>
> 交换机（Switch）和网桥（Bridge）是数据链路层设备



##### 1.4.1.3 网络层（Network Layer）

网络层用于将数据从一台主机传输到位于不同网络中的另一台主机。它还负责数据包路由，即从可用路由的数量中选择传输数据包的最短路径。发送方和接收方的IP地址由网络层放置在标头中。

![](https://s2.loli.net/2022/02/16/eOz3vB1DN6Jp4kW.png)

**网络层的作用**

* **Routing（路由）**：网络层协议决定了哪条路由适合从源到目的地。

	> 什么是**Routing**：**Routing** is the process of selecting a path for traffic in a [network](https://en.wikipedia.org/wiki/Network_theory) or between or across multiple networks.(wikipedia)

* **Logical Addressing（逻辑寻址）**：为了唯一地识别互联网上的每个设备，网络层定义了一个寻址方案。发送方和接收方的 IP 地址由网络层放置在标头中。这样的地址可以唯一且普遍地区分每个设备。

> 网络层中的数据段（Segment）称为包（Packet）
>
> 网络层由路由器等网络设备实现。



##### 1.4.1.4 传输层（Transport Layer）

> Keyword: Reliability, Flow control, Error correction

传输层向应用层提供服务，并从网络层获取服务。传输层中的数据称为分段（Segment）。它负责完整消息的端到端（End-to-End）交付。传输层还提供数据传输成功的确认，并在发现错误时重新传输数据。

* **在发送方看来：**

	传输层从上层接收格式化的数据，执行分段（Segmentation），并执行**流和错误控制**以确保正确的数据传输。它还在其标头中添加源和目标**端口号**，并将分段数据转发到网络层。

	> note：发送方需要知道与接收方应用程序关联的端口号。 通常，此目标端口号是默认配置或手动配置的。例如，当 Web 应用程序向 Web 服务器发出请求时，它通常使用端口号 80，因为这是分配给 Web 应用程序的默认端口。许多应用程序都分配了默认端口。

* **在接收方看来：**

	传输层从标头读取端口号，并将接收到的数据转发给相应的应用程序。它还执行分段数据的排序和重组。

**传输层的作用**

* **Segmentation and Reassembly（分段与重组）**：该层接受来自（会话）层的消息，将消息分成更小的单元。生成的每个段都有一个与之关联的标题。目标的传输层重新组装消息。
* **Service Point Addressing（服务点寻址）**：为了将消息传递给正确的进程，传输层标头（layer header）包含一种称为服务点地址或端口地址的地址。因此，通过指定此地址，传输层确保将消息传递到正确的位置。

> 传输层由操作系统操作。它是操作系统的一部分，通过系统调用与应用层通信。 传输层被称为 OSI 模型的核心。
>
> 数据在传输层中被称作**段（Segments）**



##### 1.4.1.5 会话层（Session Layer）

会话层负责连接的建立、会话的维护、身份验证，同时也保证了安全性。

**会话层的作用**

* **Session establishment, maintenance, and termination（会话建立、维护和终止）**：会话层允许两个进程建立，使用和终止连接
* **Synchronization（同步）**：该层允许进程将被视为同步点的检查点添加到数据中。这些同步点有助于识别错误，以便正确地重新同步数据，并且不会过早地切断消息的结尾并避免数据丢失。
* **Dialog Controller（对话控制器）**：会话层允许两个系统以半双工（half-duplex）或全双工（full-duplex）方式开始相互通信。



##### 1.4.1.6 表示层（Presentation）

表示层也称为**翻译层（Translation layer）**。来自应用层的数据在这里被提取出来，并按照所需的格式进行处理，以通过网络传输。

**表示层的作用**

* **Translation（翻译）**：比如，`ASCII`翻译成`EBCDIC`
* **Encryption/Decryption（加密/解密）**：数据加密，将数据转换成另一种形式或代码。加密后的数据称为密文，解密后的数据称为纯文本。密钥值用于加密和解密数据。
* **Compression（压缩）**：减少需要在网络上传输的比特数。



##### 1.4.1.7 应用层（Application Layer）

在 OSI  Model的最顶层，就是由网络应用程序实现的应用程序层。这些应用程序产生数据，这些数据必须通过网络传输。该层还用作应用服务访问网络和向用户显示接收到的信息的窗口。

![](https://s2.loli.net/2022/02/16/I6wUEWsukCKcBzS.png)

**应用层的作用**

* 网络虚拟终端
* FTAM-文件传输访问和管理
* 邮件服务
* 目录服务
* ......



#### 1.4.2 层级模型的好处

* 减少复杂性
* 标准化接口
* 促进模块化工程
* 确保可互操作的技术
* 加速进化
* 简化教学和学习

> 上三层一般被称为 **应用层**，因为它们处理用户界面、数据格式和应用程序访问。
>
> 下次层一般称为 **数据流层**，因为它们控制网络上消息的物理传递。



#### 1.4.3 总结

OSI 模型作为参考模型，由于其发明较晚，并未在 Internet 上实现。当前使用的模型是 TCP/IP 模型。



### 1.5 TCP/IP Model

OSI 模型只是一个参考/逻辑模型。它旨在通过将通信过程划分为更小和更简单的组件来描述通信系统的功能。但是当我们谈到 TCP/IP 模型时，它是由美国国防部 (DoD) 在 1960 年代设计和开发的，并且基于标准协议。它代表传输控制协议/互联网协议。TCP/IP模型仅仅包括四层：

1. Application Layer（应用层）
2. Transport Layer（传输层）
3. Internet Layer（网络层）
4. Network Access Layer（网络接入层）

![img](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/tcpAndOSI.png)

#### 1.5.1 TCP/IP Model 与 OSI Model 的差异

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



#### 1.5.2 Network Access Layer（网络接入层）

TCP/IP Model中的 Network Access Layer对应的是 OSI Model 中的 Physical Layer（物理层）和 Data Link Layer（数据链路层）。它寻找硬件地址，并且该层中存在的协议允许数据的物理传输。 我们刚才讲了ARP是Internet层的协议，但是将其声明为Internet Layer的协议还是Network Access Layer的协议是有冲突的。它被描述为位于第 3 层，由第 2 层协议封装。



#### 1.5.3 Internet Layer（网络层）

TCP/IP Model中的 Internet Layer与 OSI Model中的 Network Layer是对应的，它定义了负责在整个网络上进行数据逻辑传输的协议。驻留在这一层的主要协议是：

1. **Internet Protocol（IP）**：该协议负责通过查看数据包标头中的 IP 地址将数据包从源主机传递到目标主机。 IP有2个版本： IPv4 和 IPv6。 IPv4 是目前大多数网站都在使用的一种。但是 IPv6 正在增长，因为与用户数量相比，IPv4 地址的数量是有限的。
2. **Internet Control Message Protocol（ICMP）**：它封装在 IP 数据报中，负责向主机提供有关网络问题的信息。
3. **Address Resolution Protocol（ARP）**：它的工作是从已知的 IP 地址中找到主机的硬件地址。 ARP 有几种类型：反向 ARP（Reverse ARP, RARP）、代理 ARP（Proxy ARP）、免费 ARP（Gratuitous ARP） 和反向 ARP（Inverse ARP）。



#### 1.5.4 Transport Layer（传输层）

TCP/IP Model中的 Transport Layer与 OSI Model中的Transport Layer是对应的，主要负责端到端（end-to-end）的传输和流控制（Flow Control）以及错误纠正（Error Correction）。它使上层应用程序免受数据复杂性的影响。该层中存在的两个主要协议是：

1. **Transmission Control Protocol（TCP）**：该协议能够在两个终端之间提供无差错和可靠的传输，同时能够做到对数据的分段和分序（sequencing and segmentation）。同时TCP协议能够做到对数据的流控制（Flow Control），但其缺点是其功能过多导致其传输成本较大。
2. **User Datagram Protocol（UDP）**：该协议是比较新的传输层协议，其目的就是为了能够在传输成本比较小的情况下来进行传输。如果您的应用程序不需要可靠的传输，它是首选协议，因为它非常具有成本效益。与面向连接的协议 TCP 不同，UDP 是无连接的。



#### 1.5.5 Application Layer（应用层）

TCP/IP Model中的 Application Layer与 OSI Model中的上三层：Application Layer、Presentation Layer与Session Layer相对应。该层负责的是节点到节点（node-to-node）的传输以及控制用户界面规范。该层的协议主要有：HTTP, HTTPS, FTP, TFTP, Telnet, SSH, SMTP, SNMP, NTP, DNS, DHCP, NFS, X Window, LPD等。下面简要介绍几种：

1. **Hypertext Transfer Protocol（HTTP）**：超文本传输协议，万维网使用它来管理 Web 浏览器和服务器之间的通信。 HTTPS 代表 HTTP 安全。它是 HTTP 与 SSL（安全套接层）的组合。在浏览器需要填写表格、登录、验证和进行银行交易的情况下，它非常有效。
2. **Secure Shell（SSH）**：它是一个类似于 Telnet 的终端仿真软件。 SSH 更受欢迎的原因是它能够维护加密连接。它通过 TCP/IP 连接建立安全会话。
3. **Network Time Protocol（NTP）**：它用于将我们计算机上的时钟同步到一个标准时间源。它在银行交易等情况下非常有用。假设以下情况不存在 NTP。假设您执行一项事务，您的计算机在下午 2:30 读取时间，而服务器在下午 2:28 记录时间。如果服务器不同步，服务器可能会严重崩溃。



#### 1.5.6 总结

TCP/IP Model是目前主流使用的互联网模型，其能够替代OSI Model的原因一个是因为其的层数减少了，OSI过多的界限导致其无法很好的应用到实际中，另一个是因为 TCP 协议与 IP 协议的产生使得互联网得到了很好的发展。



### 1.6 Network Devices

#### 1.6.1 NIC - 网卡

网卡主要工作在第二层（数据链路层），主要的功能是控制终端（主机）和网络之间的数据传输，因为在计算机内部的数据时并行的，而在传输时数据需要变成串行的，因此就需要网卡来做从并行变成串行和从串行变成并行的转换。在一些中继设备比如交换机和路由器的端口上也是有网卡的。

#### 1.6.2 Repeaters - 中继器

中继器主要工作在第一层（物理层），主要的功能是延长网络传输的距离，因为数据会随着link的线的距离增长而丢失更多的信息，因此就需要有中继器来连接和扩展多条线路，同时重新组织和retime数据流。需要注意的是，中继器没有提供任何过滤的功能。中继器只连接两个端口。

#### 1.6.3 Hubs - 集线器

Hubs used to regenerate and retime network signals（集线器用于再生和重新定时网络信号），集线器可以看成是中继器的进化版，同样没有任何过滤的功能，也不能决定传输路径的最优选择，因为有着多个端口所以也叫多端口中继器（Multiport Repearters）。

与中继器的区别：中继器只有两个端口，集线器有多个端口，比较适合低速传输的网络，现在已经不是用的很多了。

#### 1.6.4 Bridges - 网桥

网桥是一个有着两个端口的第二层设备（数据链路层），它具有过滤的功能，其主要功能是通过MAC地址来形成MAC Table，然后通过MAC Table来决定是否要对数据进行转发。如果目的地址和原地址都在同一个网段中，则不进行转发，否则则会通过另一个端口进行转发。可以把一个局域网变成不同的冲突域。

#### 1.6.5 Switches - 交换机

交换机是结合了网桥和集线器的功能的第二层设备（数据链路层），交换机为每个端口都配置了MAC Table，然后根据这个MAC Table来决定是否进行数据的转发。会在跨冲突域的两个主机之间建立一个虚电路。逻辑上和物理上都是星型拓扑，支持大量主机同时接入，而不是网桥只有两个端口。

#### 1.6.6 Routers - 路由器

路由器是工作在第三层（网络层）的设备，能够做路径选择和报文的最佳路径转发，同时能够识别主机的IP地址



## 2 物理层原理与技术

### 2.1 Type of Networks

网络连接的方式有两种：多路复用和点对点

#### 2.1.1 多路复用方式

多个主机的数据可以经过同样的介质，即所有主机之间是通过相同的传输介质来连接的

#### 2.1.2 点对点方式

两个设备之间通过连接相连。



### 2.2 Local Area Network（LAN） Media

物理层负责把0/1的比特流变成物理信号（电信号、光信号或无线电波）。通过调幅、调频、调相的方式来实现信息的编码。不同的编码效果不一样。
