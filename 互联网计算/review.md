# Review

## 1 Physical Layer

物理层主要是编码和解码，以及传输机制（比如复用）。以及一些物理介质的规范标准。该层没有协议

### 传输介质

1. Unshielded Twisted Pair，UTP（双绞线）：有效距离100m，速度和吞吐量大概在10-100Mbps，便宜，容易安装，且外经很小，不会迅速填满配线槽。但更加容易被电磁和噪声干扰，比同轴电缆和光纤的信号衰减距离都要短
2. Coaxial Cable（同轴电缆）：有效距离500m，速度和吞吐量大概在10-100Mbps，比双绞线贵但是比光线便宜
3. Fiber Optic Cable（光纤电缆）：有效距离大概在3000m以上（Single Mode），2000m以上（Multimode），速度和吞吐量大概在100+Mbps，最贵，但是**最不易受到电磁和噪声干扰**。电磁波通过光纤来引导
4. 无线传输：根据频率（frequency）不同来区分不同的电磁波
   1. Lasers（激光）：输出相干电磁场，其中所有波都处于相同的频率并排列在一个相位中
   2. Infrared（红外线）：可以反弹或者重定向，但是不能够穿过不透明的对象
   3. Radio（无线电）：可以携带信息穿过墙体，地面和卫星无线电技术

> 1. EMI：Electromagnetic Interference，电磁干扰
> 2. RFI：Radio Frequency Interference，射频干扰

#### Category of UTP

* 一类线：主要用于语音传输，不用于数据传输
* 二类线：传输频率1MHz，用于语音和最高4Mbps的数据传输，常见于令牌网
* 三类线：EIA/TIA568标准指定电缆，传输频率16MHz，用于语音传输及最高传输速率为10Mbps的数据传输，主要用于10BASE-T
* 四类线：传输频率为20MHz，用于语音传输和最高传输速率16Mbps的数据传输，主要用于令牌网和 10BASE-T/100BASE-T
* 五类线：增加了绕线密度，外套高质量绝缘材料，用于语音和数据传输（主要为100/1000BASE-T），是最常用的以太网电缆
* 超五类线：衰减小，串扰少，具有更高的衰减/串扰比和信噪比、更小的时延误差，主要用于1000BASE-T
* 六类线：传输频率为1MHz～250MHz，性能远高于超五类标准，适用于高于1Gbps的应用
* 七类线：带宽为600MHz，可能用于今后的10G比特以太网。

## 2 Data Link Layer -- 数据叫做Frame

以太网：以太网是一个广播网络，所有的终端都能够看见所有的帧，不管这个帧是不是以它为目的地址是通过MAC address来进行判断的，MAC Layer封装之后的帧的头部会带有Destination Address，以此来判断目的地址是否是自己，如果是的话就接收并且往上层传输，如果不是的话就会丢弃这个帧

### LLC

> Logical Link Control，逻辑链路控制，是数据链路层的子层中的上层部分

* 维护单一链路上设备间的通信
  * 使用SAP(Service Access Point，服务访问点)在逻辑上标识不同的(上层)协议种类并且封装它们
    * 添加两个寻址部分来标识每个端点(end)的上层协议
      * DSAP: Destination Service Access Point，目标服务访问点
      * SSAP: Source Service Access Point，源服务访问点
    * LLC帧类型依赖于上层协议期望什么标识符(identifier)

      |      | LLC头 | 包    |      |
      | ---- | ---- | ---- | ---- |
      | MAC头 | LLC头 | 包    | MAC尾 |

    * 提供了
      * 无确认的无连接服务，被使用在
        * 可靠链路(上层来保证数据正确性)
        * 实时任务
        * 大多数的局域网内
      * 有确认的无连接服务，被使用在
        * 不可靠链路，比如无线网
      * 有确认的有链接服务

### MAC

> Media Access Control，介质访问控制，是数据链路层的子层中的下层部分

- 定义了
    - 物理线路上传输帧的方式
    - 网络拓扑结构
    - 线路规则(line discipline)
  - 处理物理寻址
  - MAC地址
    - 48位
    - 高3字节为IEEE指定的，标识了生产商，包含了OUI(Organizational Unique Identifier, 组织唯一标识符)。生产商指定低三字节，包含接口序列号(interface serial number)。
    - 组播地址: FFFF.FFFF.FFFF

* 组帧
  * 组帧是第二层封装过程
  * 帧是第二层PDU(protocol data unit，协议数据单位)

    | 7        | 1   | 6                   | 6              | 2                        | 可变 | 4   |
    | -------- | --- | ------------------- | -------------- | ------------------------ | ---- | --- |
    | Preamble | SFD | Destination Address | Source Address | Length/Type(Ethernet II) | Data | FCS |

    1. Preamble: 前导码，0x55（1010101）。用来提示接受者一个帧正在到来。
    2. SFD Start of Frame Delimiter: 帧起始界定符（1）
    3. Destination Address: 目标地址。在单播、组播、广播时有不同的格式。
    4. Source Address: 源地址。始终是单播格式的地址。
    5. Length: 长度。指示数据的字节数。以太网中46~1500。
       1. 不小于46是为了帧总长度不小于64（除去前导码+帧前导界定符的剩下总共18位），以保证有足够的传输时间用于以太网卡精确的检测冲突，这一时间是根据网络的最大电缆长度和帧沿电缆传输所要的时间来决定的。
       2. 不大于1500是因为1500字节是以太网最大传输单元(Maximum Transmission Unit)
       3. 所以，802.3帧大小在64~1518字节之间。
    6. Data: 数据。
    7. FCS Frame Check Sequence: 帧校验序列。CRC值（循环冗余校验）。

### 2.1 局域网（Local Area Network, LAN）

只有MAC，没有LLC。没有复杂协议。

**CSMA/CD**

> Carrier Sense Multiple Access with Collision Detectioon，多点接入载波监听/冲突检测

流程：

* 使用CSMA机制来决定是否发送数据，信道空闲则发送数据
* 当发送数据时也同时侦听信道，如果发现冲突，所有传输被立即取消，并广播拥塞(jam)信号。然后根据推迟算法决定推迟多久再重新发送。同时将传输次数+1，如果传输次数过多的话就放弃传输（timeout）

![CSMA/CD](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220623225245.png)

**CSMA/CA**

> Carrier Sense Multiple Access with Collision Avoidance，多点接入载波监听/冲突避免

* 发送站点在发送数据前，以特殊的控制帧来刺激接受站点发送应答帧，使得接受站点周围的站点监听到该帧，从而在一定时间内避免数据发送
* 基本过程
  * A向B发送RTS(Request To Send，请求发送)帧，A周围的站点在一定时间内不发送数据，以保证CTS返回给A
  * B向A应答CTS(Clear To Send，清除发送)帧，B周围的站点在一点时间内不发送数据，以保证A发送完数据。
  * A开始发送
  * 若RTS与CTS发生冲突，采用二进制指数后退算法等待随机时间，再重新开始

**STP**

> Spanning-Tree Protocol，生成树协议

生成树协议的BID、根交换机、根端口、指定端口的选择

BID：BID是一个2byte的优先级（默认为32767）+6byte的MAC地址（路由器都有一个唯一的MAC地址），在STP中，越小的BID说明cost越低，能被指定为**根交换机**

根端口：根端口的选择是看其他路由器的端口哪一个到根交换机的cost最小，最小的就作为根端口。

指定端口：指定端口是根据端口所在交换机的BID来比较，小的就作为指定端口，另一个端口如果不是根端口则block掉

### 2.2 广域网

**PPP**

> Point-to-Point Protocol

LLC的一种进步

**CHAP**

> Challenge Handshake Authentication Protocol

## 3 Network Layer -- 数据叫packet/Datagram

**Difference between Routed and Routing protocol**

1. Routed protocol 用于路由器之间，用来保证路由器之间连通（完成转发），保证路由器有效连通
2. Routing protocol 用于路由器的**路由表生成**：路由器彼此交换信息
3. Routing protocol 决定 Routed protocol

### 数据报格式

注意IP数据报和MAC帧的区别：IP数据包中源IP地址在前，目标IP地址在后；MAC帧目标MAC地址在前，源MAC帧在后。

IP数据包包括首部+数据，其中首部的长度为20byte-60byte（首部长度4bit，1bit代表4个字节），固定首部包括以下内容：

1. 版本：4bit，IP协议的版本，目前为4（IPV4）或者6（IPV6）
2. 首部长度：4bit，可表示的最大数字是15（一个单位为4个字节），因此首部长度最多为60字节
3. 服务类型：8bit，没有被使用
4. 总长度：16bit，指首部和数据之和的长度，单位为字节，因此数据包最大传输长度为65535字节，但是不能超过最大传送单元MTU，因此一般在1500字节左右
5. 标识：16bit，计数器，产生数据包的标识（标识不同分片是同一个数据以便上层重新组装）
6. 标志：3bit，最高位为0，MF为0表示是最后一个分片
7. 片偏移：13bit，较长的分组在分片后某片在原分组的相对位置，片偏移以8个字节为偏移单位（即如果该数据在原来分组中为从第800字节开始，则片偏移为100）
8. 生存时间：8bit，TTL（Time To Live）数据报在网络中可以通过的路由器数的最大值
9. 协议：8bit，指出数据使用何种协议，以便目的主机的IP层将数据部分上交给哪个处理过程（ICMP、IGMP、OSPF、TCP、UDP）
10. 首部检验和：16bit，发送时对首部求和取反码作为检验和，接收端将所有求和取反看是否为0
11. 源地址：32bit，IP地址长度为32bit
12. 目的地址：32bit

接下来是可变部分和填充，填充的作用是让首部长度是4的倍数。然后就是数据部分

### IP地址

分为网络号+主机号

A类地址：网络号为前8位，第一位为0时为A类地址，即0-127
B类地址，网络号为前16位，第一位为1，第二位为0时为B类地址，即128-191
C类地址，网络号为前24位，第一位为1，第二位为1，第三位为0时为C类地址，即192-223
D类地址（Multicast）：224-239
E类地址（Research）：240-255

> 此时主机位全0地址用作网络号，主机位全1地址用作广播地址

只有相同网络号（即同一网段）的主机才能够直接的相互通信

**Private Address Space**

* 10.0.0.0 - 10.255.255.255
* 172.16.0.0 - 172.31.255.255
* 192.268.0.0 - 192.168.255.255

> IP地址耗尽的解决方法：NAT（Network Address Translation）、CIDR、IPv6

### subnet

subnet：划分广播域，最少借两位（借一位0代表网络号，1代表广播），最多借主机位数-2位（全0全1需要保留，所以只借一位和只剩一位都不行）

如何找出子网的网络号：将IP变成二进制和子网掩码的二进制表示做逻辑与，就能得到网络号

### 3.1 Routing（与路由表相关）

1. Static addressing：
   1. Configure each individual device with and IP address
   2. you should keep very meticulous records, because problems can occur if you use duplicate IP addresses
2. Dynamic addressing
   1. There are a few dirrerent methods can be used to assign IP addresses dynamically:
      1. RARP: Reverse Address Resolution Protocol，反向地址转换协议，ARP为IP到MAC的转换，而RARP为MAC到IP的转换，向RARP服务器请求分配IP
      2. BOOTP: Bootstrp Ptotocol，引导程序协议，向BOOTP服务器请求分配IP
      3. DHCP: Dynamic Host Configuration Protocol，动态主机配置协议，使网络环境中的主机动态的获得IP地址、Gateway地址、DNS服务器地址等信息

#### 3.1.1 IGP（Interior Gateway Protocol）

##### 3.1.1.1 Distance-Vector Protocols, DVP

1. 根据邻居节点的视角来获取整个拓扑信息
2. 传递路由表的拷贝给邻居节点
3. Add distance vectors from router to router（根据跳数）
4. 定时更新

**RIP**

> Router Informataion Protocol，路由信息协议

1. 内部网关协议 IGP 的一种
2. 距离向量协议 DVP 的一种
3. 唯一度量是**跳数**（即经过的路由器数量）
4. 最大跳数是15（超过认为目标不可达）
5. 每30秒更新一次
6. 不会每次都选择最快的路径（根据跳数来选择）

**IGRP**

> Interior Gateway Route Protocol 内部网关路由协议

与RIP不同点：

1. 度量是通过带宽、负载、延迟和可靠性
2. 最大跳数是255
3. 每90秒更新一次

**EIGRP**

> Enhanced IGRP

##### 3.1.1.2 Link State Protocols, LSP

1. 根据整个网络拓扑来获得信息
2. 计算到其他路由的最短路径
3. 事件驱动更新
4. 传递链路状态表更新信息给其他路由器（注意不是整个备份！而是更新传递）

**OSPF**

> Open Shortest Path First，开放最短路径优先

1. 度量是通过消耗、速度、可靠性和安全性等来度量
2. 事件驱动更新

Route ID的选举规则：

1. 手动配置OSPF路由器的Router ID（通常建议手动配置）
2. 如果没有手动配置Router ID，则路由器使用Loopback接口中最大的IP地址作为Router ID
3. 如果没有配置Loopback接口，则路由器使用物理接口中最大的IP地址作为Router ID

#### 3.1.2 EGP(Exterior Gateway Protocol)

### 3.2 Others

**IP**

> Internet Protocol，互联网协议

IP是无连接的，分组的不同报文会根据网路情况选择不同的路径，路由器来进行路径选择

**ICMP**

> Internet Control Message Protocol，因特网控制报文协议，为了提高IP数据报交付成功的机会

1. ICMP允许主机或者路由器报告差错情况和提供有关异常情况的报告
2. ICMP只是IP层的协议
3. ICMP报文作为IP层数据报的数据，加上数据报的首部，组成IP数据报发送出去

报文格式

1. 类型：8个bit
2. 代码：8个bit
3. 检验和：16个bit，这4个byte都是一样的
4. 接下来32bit：取决于ICMP报文类型（差错报告报文和查询报文）
5. ICMP数据部分，长度取决于类型

报文封装在IP报文的数据部分中。

1. 对 ICMP 差错报告报文不再发送 ICMP 差错报告报文
2. 对第一个分片的数据报片的所有后续数据报片都不发送 ICMP 差错报告报文
3. 对具有多播地址的数据报都不发送 ICMP 差错报告报文
4. 对具有特殊地址（如127.0.0.0或0.0.0.0）的数据报不发送 ICMP 差错报告报文

PING：Packet InterNet Groper，是用了 ICMP 的 “Echo request” 和 “Echo reply“ 消息来实现的，是应用层直接使用网络层ICMP的例子，没有通过运输层的 TCP 或者 UDP


![ICMP报文类型](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220624151706.png)

**ARP**

> Address Resolution Protocol，地址解析协议，根据IP查找MAC地址

ARP是一个根据IP地址来查找MAC地址的协议，即主机会维护一个ARP Table，发送消息时会根据IP地址在这个表中查找目标的MAC地址，如果找不到的话会有以下流程：
1. 向本网段发送一个APR Request，询问“该IP地址”的MAC地址是多少，ARP Request所携带的信息有源MAC地址（即发送方MAC地址），目的MAC地址（全1，广播），源IP地址，目的IP地址（目标是找到该IP地址的MAC地址），以及一条信息“What is your MAC Addr？”
2. 所有接收到该ARP Request的设备检查自己的IP地址，如果不符合则丢弃该ARP Request，如果符合的话则返回一个 ARP Reply，其实源MAC地址就附带上自己的MAC地址，目的地址是发送ARP Request的主机地址，目的IP是发送ARP Request的主机IP，源IP是自己的IP
3. 发送方收到ARP Reply之后就将该IP-MAC对应信息存入自己的ARP Table中，需要注意的是被请求的（即收到 ARP Request 的设备也会保存下发送方的 IP-MAC 对应关系）

> 如果两个主机不在一个网段上，要怎么通过ARP Request来获得MAC地址信息呢？
> 
> 1. 默认网关
> 2. 代理ARP

默认网关：连接源主机（即要发送ARP Request的主机）的路由器上的端口的IP地址，通过默认网关交给路由器，路由器来回复 ARP Reply（因为不同网段需要通过路由器来转发，所以主机会认为其他网段的MAC地址都是与本网段相连的路由器的MAC地址，然后再通过路由器处理进行转发）

**VLSM**

> Variable Length Subnet Masks，可变长子网掩码

Only unused subnets can be further subnetted!

**CIDR**

> Classless InterDomain Routing（CIDR），无类域间路由

## 4 Transport Layer -- 数据叫segment

### 4.1 TCP

> Transmission Control Protocol，传输控制协议

1. reliable and Connection-oriented
2. Software checking for segment
3. Re-send anything lost or error
4. Uses acknowledgements
5. Provide flow control

[TCP-Learning](https://zyinnju.com/2022/06/04/TCP-Segment/)

**ARQ**

> Automatic Repeat Request

重新传输的请求是自动传送的，接收方不需要请求传输者来重传错误的数据段

### 4.2 UDP

> User Datagram Protocol，用户数据报协议

[UDP-Learning](https://zyinnju.com/2022/06/05/UDP-Learning/)

## 5 Application Layer -- 数据叫data

只考概念和名词

1. HTTP：HyperText Transfer Protocol 超文本传输协议
2. FTP：File Transfer Protocol 文件传输协议
3. HTML：HyperText Markup Language 超文本标记语言

## 6 技术方案

### 6.1 NAT

> NAT, Network Address Translator，网络地址转换

NAT 允许私有IP地址来连入互联网（私有 IP 地址指的是 10.0.0.0 - 10.255.255.255 / 172.16.0.0 - 172.31.255.255 / 192.168.0.0 - 192.168.255.255）。NAT 是一种解决IP地址资源耗尽的方案，**因为在NAT中规定了内网主机统一访问的IP**，即内网的多台主机在因特网的其他主机看来都是相同的IP地址，只要访问这个IP地址就能够接触到其中的主机。

通过在路由器上配置 NAT，会生成一个 **NAT Table**，将 Private IP Address 映射为可以在因特网上传输的地址。网络地址转换 (NAT) 是一个过程，其中一个或多个本地 IP（或者说内网IP） 地址被转换为一个或多个全局 IP 地址，反之亦然，以便为本地主机提供 Internet 访问。此外，它还进行端口号的转换，即在将被路由到目的地的数据包中用另一个端口号屏蔽主机的端口号。然后在 NAT 表中生成对应的 IP 地址和端口号条目（这项技术通常被称为PAT, Port Address Translation，是一种NAT的进化版，能够通过运用同一个IP的不同端口来实现更加好的节约IP地址）。 NAT 通常在路由器或防火墙上运行。

NAT 运行的原理是通过在路由器上配置 NAT，实现由外网到内网的 IP 地址转换，当一个 IP数据报传送到该路由器上的时候，路由器根据NAT Table将映射的公网IP转为内网IP（即更改目的IP地址）或者将内网IP（即源IP地址）变为NAT Table映射的公网IP，这两种转换主要取决于是内网的主机访问外网的主机还是相反。

后面会介绍静态NAT和动态NAT，在动态NAT中，如果地址池中的IP地址被用完，那么内网主机就不可被访问，此时会向发送IP数据报的主机发送一个ICMP-主机不可达数据报。

![NAT](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220605194621.png)

### 3.1 Static NAT

静态 NAT，通过手动指定内部本地地址和内部全局地址映射关系来映射主机，是一种一对一的确定关系，缺点：每一台主机都需要一个内部全局的地址，这样是十分消耗IP地址的，对于一个公司来说可能没办法购买如此多的IP地址，同时，如果新增新的主机，那么就需要配置新的IP地址，其实也是十分不方便的。

### 3.2 Dynamic NAT

通过配置一个 **地址池** 来动态分配映射关系，在地址池中预先存放了一些能够被映射的内部全局IP地址，当某个主机需要传输IP数据报的时候，就在地址池中选择一个没有被使用的内部全局地址分配给该主机，此时该地址就无法再被其他的内部主机使用。因此，者是一种先来先服务的关系，如果地址池中的 IP 地址被用完那么就无法为后续的主机分配内部全局地址，此时传输的IP数据包只能够被丢弃。

### 6.2 VLAN

### 6.3 ACL

### 名词解释

1. AP：Access Point 接入点
2. ARP：Address Resolution Protocol 地址解析协议，把IP地址解析为MAC地址，解决了一个局域网上的主机或者路由器的IP地址和硬件地址的映射问题
3. ARQ：Automatic Repeat reQuest：自动重传请求，OSI
4. ACL：Access Control List：访问控制列表，是路由器和交换机接口的指令列表，用来控制端口进出的数据包
5. ACK：Acknowledgement：确认号
6. BDR：Back-up Desinated Router：备份指定路由器，对DR的备份
7. BPDU：Bridge Protocol Data Unit：桥接数据单元，用于在STP（Spanning Tree Protocol，生成树协议）中传递拓扑信息、选举等
8. BID：Bridge Identification：网桥标识，8byte，由优先级+交换机的MAC地址组成，用于选举跟桥接器、根端口等（SPF）
9. B Channel：64kbps
10. CSMA/CA：Carrier Sense Multiple Acess/Collision Avoidance：带有冲突检测的载波侦听多点访问，通过RTS（Request To Send）和CTS（Clear To Send）来避免一定时间内周围站点的数据发送防止冲突
11. CSMA/CD：Carrier Sense Multiple Access/Collision Detection：带有冲突检测等载波侦听多点访问，即发送数据时持续监听，如果发生冲突则放弃发送，根据推迟算法推迟一段时间再发送，且尝试次数加一，如果尝试次数过大则不再重发
12. CIDR：Classless Inter-Domain Routing：无类域间路由，在Internet上创建附加地址的方法，这些地址提供给服务提供商，再由ISP分配给客户
13. CHAP：Challenge Handshake Authentication Protocol：挑战握手协议，链路建立阶段后，认证者向对端点发送“challenge”消息；对端点用经过单向哈希函数计算出的值做应答，然后认证者看是否与自己计算的相同，如果相同则得到承认，否则连接终止；经过随机时间重新challenge
14. DHCP：Dynamic Host Configuration Protocol：动态主机配置协议，使用UDP协议工作，主要是给内部网络或者网络服务提供商自动分配IP地址
15. DVP：Distance Vector Protocol：距离矢量协议，通过计算目标路由器与源路由器之间的距离矢量和来选择最佳路径，包括RIP（Routing Information Protocol）、IGRP（Interior Gateway Routing Protocol）
16. DR：Designated Router
17. D Channel：16kbps
18. EGP（Exterior Gateway Protocol）：外部网关协议，在自治网络间交换路由协议
19. FTP（File Transfer Protocol）：文件传输协议，使用TCP，基于20和21端口
20. FDM（Frequency Division Multiplexing）：频分复用
21. HTTP（HyperText Transfer Protocol）：超文本传输协议
22. HTML（HyperText Markup Language）：超文本标记语言
23. ISP（Internet Service Provider）：互联网服务提供商，提供互联网接入业务、信息业务、增值业务的电信运营商
24. ICP（Internet Content Provider）：互联网信息业务和增值业务的电信运营商
25. IP（Internet Protocol）：网际互联协议
26. IGRP（Interior Gateway Routing Protocol）：内部网关协议，在自治网络内网关间交换路由的协议
27. ICMP（Internet Control Message Protocol）：因特网控制报文协议，允许主机或者路由器报告差错情况和提供有关异常情况报告的协议，运行在IP层（Ping的底层实现）
28. LAN（Local Area Network）：局域网
29. LLC（Logical Link Control）：逻辑链路控制，是数据链路层的上层部分。用户的数据链路服务通过LLC子层为网络层提供统一的接口。逻辑上标志不同协议类型且封装起来，处理差错通知（但是不修改差错），网络拓扑和流控制
30. LSP（Link State Protocol）：链路状态协议，每个路由器都了解整个网络的拓扑结构，事件驱动更新，通过链路状态来计算两个路由器之间的最短路径，每次都只向邻居路由器传递链路状态的更新信息，比如OSPF（Open Shortest Path First）
31. LSA（Link State Advertisement）：逻辑链路广播
32. MAC（Media Access Control）：介质访问控制，数据链路层的下半子层，MAC地址标识了所有网络设备的唯一编号
33. NAT（Network Address Translation）：网络地址转换，内部私有IP转换为公有IP地址以节省IP地址，有静态和动态两种方法
34. OSI（Open System Interconnection）：开放系统互连
35. OSPF（Open Shortest Path First）：开放式最短路径优先
36. PING（Packet Internet Groper）：因特网包探测器
37. PAT（Port Address Translation）：NAT的扩展，允许本地多个设备映射到一个单一的公网IP地址（NAT只能一对一）
38. POP3（Post Office Protocol 3）：邮局协议3，从服务器读取邮件
39. PPP（Point-to-Point Protocol）：是为在同等单元之间传输数据包这样的简单链路设计的数据链路层协议，是数据链路层使用最多的一种协议。这种链路提供全双工操作，并按照顺利传递数据包。特点为：简单；只检测差错而不纠正差错，不进行流量控制；支持多种网络层协议
40. RIP（Routing Information Protocol）：是一种DVP，固定时间间隔交换整张路由表
41. SMTP（Simple Matil Tranfer Protocol）：简单邮件传输协议
42. STP（Spanning Tree Protocol）：生成树协议，该协议可用于在网络中建立树形拓扑，消除网络环路（交换机、BID、根端口、指定端口、根交换机）
43. TCP（Transmission Control Protocol）：传输控制协议，面向连接、可靠的、基于字节流的传输层通信协议，不支持单播和组播
44. UDP（User Datagram Protocol）：用户数据报协议，无连接、不可靠的，没有确认，没有流控制的信息传送服务
45. URL（Uniform Resource Locator）：统一资源定位符
46. VLSM（Variable Length Subnet Mask）：可变长度子网掩码
47. VLAN（Virtual Local Area Network）：虚拟局域网，分割广播域
48. VTP（Vlan Trunk Protocol）：VLAN中继协议
49. WAN（Wide Area Network）