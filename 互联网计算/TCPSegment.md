# TCP Segment

## 0 Overview

TCP数据段主要有以下的几个特点：

1. TCP是进程间通信的协议：为了能够实现多个应用程序复用相同IP地址且在同一时刻进行网络上数据的传输，TCP协议为主机制定了“端口（port）”的概念，端口是一个**16bit**长的标识符，用来标识某个进程在主机占用的一个“位置”和标识进程在主机上发送和接收的信息的位置
2. TCP为数据的每一个字节制定了编号，对于编号没有要求，不一定要从0开始，只是作为一个标识符用于接收方对数据进行乱序重组和数据的确认，会在后面TCP数据段的结构处继续讨论。
3. TCP全双工：TCP是一种全双工的协议，这意味着它可以同时发送和接收数据
4. 面向连接服务：TCP是一种面向连接的服务，对于每一次传送数据，它都会有以下的三个步骤：
   1. 建立连接：通过三次握手来建立两个主机端口之间的连接
   2. 发送数据
   3. 释放连接：通过四次挥手来确认数据的传输完毕，释放两个端口之间的连接



## 1 TCP Segment Structure

![TCP Segment Structure](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220604184419.png)

TCP段主要包括以下几个结构：

1. 源端口（Source Port Address）：标识该数据段的发送方使用的端口，**16bit**大小
2. 目的端口（Destination Port Address）：标识该数据段的接收方接收的端口，**16bit*8大小
3. 序号（Sequence Number）：序号是一个**32bit**的字段，一般是该数据报的数据部分的第一个字节的编号，因为分组分时发送的缘故，某个报文切分后的TCP数据报可能在不同时间乱序到达接收方，因此需要序号来让接收方对TCP数据段进行重组。
4. 确认号（Acknowledgement Number）：确认号是一个**32bit**的字段，相当于是接收方再向发送方请求下一个想要获得的数据，比如对于两个主机A和B来说，A往B发送了一个TCP数据报，数据字段包括了该次报文的0-499编号的数据，那么，B在向A发送数据的时候，在确认号会标上500，以表示自己接受到了0-499的数据，想要请求500开始的数据。这是一种保证数据传输成功的确认手段，也能够让一些传输失败的数据进行重传
5. 数据偏移（也叫首部长度）：是一个**4bit**的数据，指出TCP报文段的数据起始处距离TCP报文段的起始处的长度，单位是32bit（4Byte）。首部有20Byte是固定的，即数据偏移字段最小都为5。
6. 保留：接下来的**6bit**是保留位，为今后使用做保留，目前置0（到目前都还未使用）。
7. 标志位：接下来有6个标识位，每个都是**1bit**
   1. URG：urgent位，当该位为1时，标识该报文段中有紧急数据，紧急指针字段（后面会介绍）有效，该位告诉系统此报文段中有紧急数据，应该尽快传送。
   2. ACK: ACK = 1时，确认号字段有效，反之无效。大部分情况下都为1，只有在初始建立连接关系的时候ACK可能会为0
   3. PSH：push位，接收方收到PSH = 1的报文段，就会尽快地将数据交付到接收数据的应用进程，而不再等到整个缓存都填满了后再向上交付。
   > TCP是面向字节的，会有个缓存区来保证可靠传输，并不是收到某个数据就直接向应用程序传输，而是要进行一定的判断 && 缓存区已满才会向应用进程传输。
   4. RST：reset位，RST=1时标识TCP连接中出现了严重的差错（比如主机崩溃或者其他的原因），必须要释放连接，然后再重新建立连接。
   5. SYN：Synchorized，表示这是一个连接请求或者连接接收的报文（连接建立时期）
   6. FIN：finish，用来释放一个连接，FIN=1标识此报文段的发送端的数据已经发送完毕，并要求释放运输连接（可能会应答对方发送的数据，但是不会再向接收方发送数据）。
8. 窗口：占据**8bit**，是接收方用来告知发送方下一次报文段的传输应该传输多少字节，窗口告知的是TCP报文段的数据部分的字节大小，比如说确认号是501，窗口是1000，那么表示下次传输接收方期待收到发送方的是501-1500（左开右闭）的数据。
    > 上面说到TCP会有个缓存区来保存数据，用窗口就是为了限制发送数据的数量以导致缓存区的溢出
9.  检验和：检验和占**2bit**，检验和字段检验的范围包括首部和数据这两部分（与packet不同，packet只包含首部的检验）。两个Byte为一个单位进行排列，然后进行逻辑加，然后将和取反码作为检验和。检验的时候只要以相同的流程计算出结果然后取反看是否是全0即可。
10. 紧急指针：占**16bit**，指出在本报文段中的紧急数据共有多少个字节（紧急数据放在本报文段数据的最前面）。
11. 选项：该部分是长度可变的字段，不包括在固定首部的20个Byte之内，一开始TCP只有一种选项，是<span style='color: red'>**最大报文长度 MSS（Maximum Segment Size）**</span>，该字段用于告诉接收方自己的缓存所能够接受的报文段的数据字段的最大长度是MSS个字节（注意这里只是数据字段）。
12. 填充：填充的作用是让TCP的报文段的首部长度刚好是4（字节）的倍数

**总结**

TCP数据报的首部是一个大于等于20个字节的首部（但必须是4字节的整数倍），数据段并不是必须的（建立连接时可能就没有数据）。TCP的数据段的大小和下层的IP Packet的大小是需要匹配的（IP Packet是在TCP Segment的基础上添加首部进行封装），但对于上层传输的数据可以是不一一对应的（传输时可能会进行分片）。同时对于TCP的每个Byte都有一个**32bit**的序号

## 2 Three-way Shakehands

下面介绍一下TCP建立连接的三次握手机制。这里以 **Server-Client** 的模式来进行一个例子的说明，Server服务器启动后等待Client的请求，Client和Server需要通过三次握手的机制来建立连接关系，具体的步骤如下所示：

![Three-way Shakehands](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220604202742.png)

下面简单介绍一下三次握手的流程：

1. Server持续监听能够被请求到的端口（比如HTTP服务时80端口），Client在需要请求的时候向Server发送一条TCP报文段，该报文段的 **SYN=1** ，表示这是一个请求建立连接的报文，**ACK=0** 表示确认号是无效的首部信息（因为这是第一条建立连接的报文，并没有需要确认的数据），同时首部的序号（sequence number）为 $x$。（第一次握手）
2. Server收到Client的请求建立连接的报文，如果请求的端口没有运行进程，那么就会将 **RST=1** 标识需要重新建立连接。如果想要建立连接的端口有进程正在运行，那么会判断是要接受还是拒绝这个请求，如果接受这个请求那么会发送给Client一个报文段，该报文段的 **SYN=1, ACK=1, seq=y, ack=x+1**，表示这是建立连接的报文，同时确认号是有效的为 **x+1**，同时发送的数据部分的第一个字节编号为 **y**。（第二次握手）
3. Client收到Server的第二次握手的应答之后，会返回Server一个报文段表示自己收到了确认建立连接的请求，此时的报文段会将 **SYN=0, ACK=1, seq=x+1, ack=y+1** 以表示连接建立的确认（第三次握手）

三次握手之后连接建立，就开始传输数据，注意对于 **Server-Client** 模式来说，请求都是从 Client 端发起的，所以在三次握手之后，虽然最后一次握手的发送方是 Client，但是接下来的数据传输的最开始也是需要由 Client 端来传输的，Client端会传输和最后一次握手的报文段一样的seq和ack（序号和确认号），同时带上data部分

> 对于 **SYN=1** 或者 **FIN=1** 的报文，默认数据部分的长度为1，所以三次握手的确认号总是在上一个序号的基础上加一即可，但是对于正常传输的数据来说，还需要计算数据部分的长度。


## 2 Data Transfer

TCP是一种可靠传输协议，因此会保证数据的传输可靠性，这就需要考虑何时需要重传数据和如何知道需要重传数据。在TCP中，采用了确认收到的机制（即上面对TCP数据段结构中ack和seq的介绍）来确保数据的正确传输，如果接收方迟迟没有回复确认收到数据的信息（有可能是接收方没收到或者回复的消息丢失或者回复超时），发送方就需要重传。

根据上面的介绍，我们知道发送方是需要重新传输数据的，这就说明发送方不能在第一次发送数据之后就把数据删除，而是需要保存一份副本在缓存中，在需要重传的时候再进行传输。同时，TCP协议规定了一个“超时计时器”，如果在一定的时间范围内发送方没有收到接收方确认收到的消息，那么发送方就直接重传数据，同时需要规定一个重传数据的次数防止无限次的传输数据。（一般计时时间会比往返时间稍长一点），这是一种 **stop-and-wait-protocol** 的简单协议，但是效率比较低下，往往会通过其他方式来优化。

**提高传输效率：多数据同时发送**

通过维护一个 **发送窗口** 来同时发送多个数据，收到某个确认就删除该报文段的数据备份，并且将窗口向前移动。（这里的窗口是通过报文段中的窗口字段来指定的，接收方可以指定能够接受的最大的窗口大小，以此来让发送方同时发送多少个报文段）。

## 3 TCP Data Connection Termination

上面说到TCP有释放连接的过程，释放连接主要有以下两种情况

1. 正确释放连接：这种情况是一般情况下的连接释放，连接的双方会通过 **四次握手** 来确认连接的释放。
2. 突然释放连接：这种情况发生在某一方出现了某种错误单方面关闭了连接或者TCP因为某种原因强行关闭了双方的连接

下面主要介绍一下正确释放连接的 **四次握手**，我们还是以 **Client-Server** 模型为例子来阐述四次握手的具体步骤，在前面的三次握手中，我们能看到主要在发挥传递信息作用的是 **SYN** 位，在四次握手中，主要发挥作用的是 **FIN** 位。

![Four-way ShakeHands](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220605131311.png)

1. 当 Client 想要关闭连接的时候，会向 Server 发送一个数据段，该数据段的 **FIN=1** ，表示数据已经传送完毕，想要关闭连接，同时在该数据段中的 序号和确认号仍然是有东西的。假设此时的 seq=u。
2. Sever 接收到 Client 的数据传输完成，想要释放连接的报文段之后，会返回一个确认收到的报文段，其中的 **ACK=1** 表示确认号有效，确认号ack=u+1，表示成功收到了 Client 发送的想要释放连接的请求，同时会带一个序号 seq=v，让 Client端来返回确认收到 “Server确认收到释放连接请求” 的报文段。Client 收到该报文段之后就不会再发送新的请求给 Server。<span style='color: red'>**但是由于之前Client发送的请求可能还未被Server处理完毕**</span>（注意：这里是为什么释放连接需要比建立连接多一次握手的原因所在），所以 Server 可能会在发送确认收到释放连接的请求的报文段之后，继续向 Client 返回之间请求的数据（即正常的数据传输过程，Client 也需要返回确认收到报文段），此时连接还没有真正被释放。
3. 当 Server 的所有数据全部都传输给 Client 之后，Server 再发送一个报文段给 Client，此报文段的 **FIN=1，ACK=1** 标识数据传输完毕，且确认号有效，此时的确认号仍旧是复用上面第二次握手的 **u+1** 用以让 Client 识别是哪一次的连接释放，同时会带上 seq=w 用以让 Client进行确认的回复
4. Client 收到后回复确认，**ACK=1**，seq=u+1，ack=w+1，用以确认收到 Server 端发送的数据传输完成的报文段，此时两者的连接完成释放。

> 需要注意的是，在第四次握手的报文段发送出去之后，Client 不能够马上关闭，而是要再等待一段时间（2MSL），这是防止第四次握手的报文段没有发送成功，如果 Server 重新发送释放连接请求，需要重新做出应答。MSL（Maximum Segment Lifetime），TCP允许不同的实现可以设置不同的MSL值。
> 1. 保证客户端发送的最后一个ACK报文能够到达服务器，因为这个ACK报文可能丢失，站在服务器的角度看来，我已经发送了FIN+ACK报文请求断开了，客户端还没有给我回应，应该是我发送的请求断开报文它没有收到，于是服务器又会重新发送一次，而客户端就能在这个2MSL时间段内收到这个重传的报文，接着给出回应报文，并且会重启2MSL计时器。
> 2. 第二，防止类似与“三次握手”中提到了的“已经失效的连接请求报文段”出现在本连接中。客户端发送完最后一个确认报文后，在这个2MSL时间中，就可以使本连接持续的时间内所产生的所有报文段都从网络中消失。这样新的连接中不会出现旧连接的请求报文。

下面两张图表示了 **四次握手** 期间 Server 和 Client 的状态

![Client TCP States](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220605133423.png)

![Server TCP States](https://img-bed-1309306776.cos.ap-shanghai.myqcloud.com/img/20220605133443.png)


## 4 TCP 计时器

TCP中有四种计时器，分别有着不同的作用

### 4.1 重传计时器

重传计时器（Retransmisssio Timer）的目的是为了控制丢失的报文段或者丢弃的报文段进行重传，对于该计时器可能有以下两种情况：

1. 如果在计时器到时之前已经收到了对报文段的确认，则撤销该计时器
2. 如果时间已经到了但是没有收到对此报文段的确认，则重传报文段，并且将该计时器复位重新开始计时。

### 4.2 坚持计时器

坚持计时器（Persistent Timer）的目的是解决 **零窗口大小** 可能导致的死锁问题。

**死锁问题的产生**

当接收端的窗口大小为0时，接收端向发送端发送一个零窗口报文段，发送端即停止向对端发送数据。此后，如果接收端缓存区有空间则会重新给发送端发送一个窗口大小，即窗口更新。但接收端发送的这个确认报文段有可能会丢失，而此时接收端不知道已经丢失并认为自己已经发送成功，则一直处于等待数据的状态；而发送端由于没有收到该确认报文段，就会一直等待对方发来新的窗口大小，这样一来，双方都处在等待对方的状态，这样就形成了一种死锁问题。

> 来自 [tcp中的四个计时器](https://blog.csdn.net/qq_33951180/article/details/60468267)

该计时器的原理就是在收到对方的零窗口大小的报文段的时候，开始计时，如果超过时间还没有收到对方的更改窗口大小的报文，那么会主动发送一个报文段来向对方确认窗口大小是否被改变，如果没有被改变则将计时器复位，如果改变则撤销该计时器。


### 4.3 保持计时器

保持计时器（Keeplive Timer）的目的是为了防止TCP连接出现长时间的空闲，因为TCP连接是一种消耗成本很大的连接，长时间的空闲会导致效率的低下，如果出现长时间的空闲可能是一方出现了故障导致的。该计时器是为了避免这种长时间的空闲。

当服务器收到客户端的数据时，会将该计时器进行重置，如果超出时间还没有收到客户端新的数据，那么服务端会发送一个报文段给客户端来探测客户端是否还“存活”，如果没有收到客户端的确认报文，那么就认为客户端出现了故障，就会释放已经建立的连接。

### 4.4 时间等待计时器

时间等待计时器（Time Wait Timer）是在释放连接时刻使用的（即上述的四次握手最后 Client的等待），这里不过多赘述。

