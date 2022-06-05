# README

本游戏主要通过`Swing`和`awt`来实现一个“葫芦娃大战妖精”的2D横版射击游戏，借鉴游戏有：崩坏学园2、飞机大战等。https://github.com/Martin7-1/CalabashGame



## 代码结构

代码结构如下：

```
.
│  pom.xml	# Maven Config File
│  README.md	# This File
│
├─.idea
│
└─src
   ├─main
   │  └─java
   │      ├─com.nju.edu
   |	         |
   │             ├─bullet
   | 	         |    CalabashBullet.java   # 葫芦娃子弹类
   |             |    MonsterBullet.java    # 妖精子弹类
   |             |     
   |             ├─control
   |             |    GameController.java   # 游戏的控制类，进行线程的管理和Panel的绘制等
   |             |    
   |             ├─game
   |             |    Game.java             # 游戏的入口与主类，主要是初始化GameScreen
   |             |
   |             ├─skill
   |             |    skill.java	    # 一个技能的接口，声明了技能应该要有的几个方法
   |             |    MoveSkill.java        # 移动技能，能够加快葫芦娃的速度
   |             |    CDSkill.java	    # CD技能，能够缩短葫芦娃发射子弹时间的间隔
   |             |    RecoverSkill.java	    # 恢复技能，能够给葫芦娃回血
   |             |
   |             ├─screen
   |             |    GameScreen.java       # 继承了JFrame，游戏的主屏幕
   |             |    RenderThread.java     # 用于强行刷新屏幕的类，单独的线程管理
   |             |
   |             ├─sprite
   |             |    Calabash.java         # 葫芦娃类，继承自Sprite
   |             |    GameObject.java       # 游戏物体类，所有游戏内的物体都继承了该类
   |             |    Monster.java          # 妖精接口，声明了妖精的发射子弹方法
   |             |    MonsterOne.java       # 第一类妖精，继承自Sprite
   |             |    MonsterTwo.java       # 第二类妖精，继承自Sprite
   |             |    MonsterThree.java     # 第三类妖精，继承自Sprite
   |             |    Sprite.java           # 精灵类，继承自GameObject
   |             |
   |             ├─util
   |             |    GameState.java        # 游戏的一些状态，枚举类
   |             |    ReadImage.java        # 读取游戏图片的一个工具类
   |
   |
   └─test
       └─java
           └─com.nju.edu
              	  └─sprite
              	      CalabashTest.java
              	      GrandFatherTest.java
              	      MonsterOneTest.java
              	      MonsterTwoTest.java
              	      MonsterThreeTest.java
```



### 关于GameController

`GameController.java`是本游戏实现过程中最复杂的类了，其中集成了三个内部类`CalabashThread.java`和`MonsterThread.java`和`GrandfatherThread.java`，这三个内部类都实现了`Runnable`接口，本意其实是想要将这三个类单独出来的，但由于这两个类需要用到`GameController`中的许多数据，所以就当做内部类了。

同时，`GameController`自己也实现了`Runnable`接口，主要是做一些子弹碰撞的检测和防止两个妖精走到同一格的管理。



## 代码实现

类似于一个“飞机大战”的游戏，葫芦娃的目标就是击败妖精保护爷爷。



### 关于葫芦娃

葫芦娃类运用了**单例模式**，`Calabash.java`中主要声明了几个葫芦娃**特有**的属性：

* HP
* Skill
* fireInterval(开火间隔)
* ...

同时我实现了一个比较愚蠢的自动跟随（假），让爷爷跟着葫芦娃移动。



### 关于妖精

主要有三种妖精，他们出现的时间间隔是不同的，且移动的方式也是不同的。

#### 妖精一

移动速度较快，但是只会单向移动，发射子弹的速度也比较快

#### 妖精二

移动速度较慢，但是会上下移动，发射子弹速度较慢

#### 妖精三

会变换速度



随着游戏时间的增加，**出怪**的速度会越来越快。



### 关于技能

设定是这样的：每过一定时间，爷爷会给葫芦娃一个技能，目前只实现了三个技能，分别是：

* 加速技能，能够增加葫芦娃的移动速度
* CD技能，能够缩短葫芦娃发射子弹的CD
* 恢复技能，能够给葫芦娃回一次血（10HP）



### 关于不同包的一个解读

#### sprite

`sprite`包下存储的都是我们的**游戏物体**，包括妖精，葫芦娃都在该包下。

#### bullet

`bullet`包下存储的是葫芦娃和妖精的**子弹**定义。

#### control

`control`包下只有唯一的`GameController.java`类，也是我们游戏主要的控制类，其声明了`JPanel`接口，并进行线程的管理和一些游戏玩法的控制。后续可能还会分离出几个类。

#### screen

`screen`包下是关于游戏屏幕`JFrame`和一个屏幕刷新线程的定义（定义了FPS）。

#### skill

`skill`包下是技能的定义，通过`skill.java`定义了一个接口，然后不同的技能只要声明了该接口，然后各自实现具体即可。

#### util

`util`包是定义了一些工具，包括读取图片、游戏状态的枚举类和游戏时间的控制类。


## 关于未来的一些进展

* 目前暂时没有设置游戏取得胜利的条件
* 后面会加上更多的技能实现，包括增加其他的葫芦娃之类的玩法
* 以及游戏的boss设置还未实现。
* 会给不同的妖精赋予不同的血量，然后通过技能来增加子弹的伤害之类的玩法

## 关于读取文档

* 存储已经实现完成
* 通过序列化读取已经存在resources文件夹中的存储文件

## 关于网络对战
* 加入了爷爷，玩家二可以通过鼠标控制爷爷来躲避子弹，最终胜利条件是在保护好爷爷的情况下获得2000分
* 玩家一控制葫芦娃发射子弹击败各种怪物
* 爷爷只能够移动和给予葫芦娃技能，考验走位的时候到了！
