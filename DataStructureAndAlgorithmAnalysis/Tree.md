# Tree

## 霍夫曼树

### 增长树

* 对原本二叉树中度数为1的节点，增加一个空树叶

* 对原本二叉树中的树叶，增加两个空树叶

> 即增长树中原本二叉树的节点的度数都是2



**外通路长度（外路径）**

根到每个外节点（增长树的叶子）的路径长度的总和（边数）

**内通路长度（内路径）**

根到每个内节点（非叶子）的路径长度的总和（边数）

**节点的带权路径长度**

一个节点的权值与节点的路径长度的乘积

**带权的外路径长度**

各个叶节点的带权路径长度之和

**带权的内路径长度**

各个非叶节点的带权路径长度之和



### Definition(霍夫曼树)

给出 $m$ 个 实数 $W_1, W_2, \dots, W_m (m \ge 2)$ 作为 $m$ 个**外节点**的权构造出一棵增长树，使得这个增长树的带权外路径长度
$$
\sum_{i = 1}^m W_il_i
$$
最小，其中 $l_i$ 为从根节点出发到具有权威 $w_i$ 的外节点的通路长（边数）。



### Huffman算法

**思想：**权大的外节点靠近根，权小的远离根

**算法**：从 $m$ 个权值中找出两个最小值 $W_1, W_2$ 构成一个子树，然后把这个子树的权重（即 $W_1 + W_2$）当成整体再一次加入所有权重值中排序。

![image-20211119162540814](C:\Users\Zyi\AppData\Roaming\Typora\typora-user-images\image-20211119162540814.png)

重复上述过程，直到将所有的权重节点都加入了树中，

<span style = 'color: red'>**注意：**</span>当**内节点**的权值与**外节点**的权值相等的情况下，内节点应该排在外节点之后，除了保证 $\sum W_i l_i$ 最小外，还应该保证 $max I_j ,\sum I_j$ 也应该有最小值



### Huffman编码

1. 统计字符出现的频率
2. 将该频率作为权重来将这些字符构造成Huffman树
3. 将这课数的左子节点标成0，右子节点标成1，然后将从根到每个叶子的路径上的号码连接起来，就是外节点的字符编码。

**解码**

沿着Huffman树走即可