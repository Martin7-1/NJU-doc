package com.nju.edu.control;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.screen.RenderThread;
import com.nju.edu.skill.SkillName;
import com.nju.edu.sprite.*;
import com.nju.edu.util.GameState;
import com.nju.edu.util.ReadImage;
import com.nju.edu.util.TimeControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zyi
 */
public class GameController extends JPanel implements Runnable {

    /**
     * 游戏的时间
     */
    private static long TIME = 0;
    /**
     * 游戏的分数
     */
    private int score = 0;
    /**
     * 游戏的状态
     */
    public static GameState STATE = GameState.START;
    /**
     * 用一个线程池来管理妖精的出现
     */
    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 用一个单独线程池来管理fps
     */
    private ExecutorService render = Executors.newSingleThreadExecutor();
    private int fps;

    private JLabel scoreLabel;
    private JLabel HPLabel;
    /**
     * 用于存放当前的技能
     */
    private JLabel skillLabel;

    private Calabash calabash;
    private GrandFather grandFather;
    private List<MonsterOne> monsterOneList;
    private List<MonsterTwo> monsterTwoList;
    private List<MonsterThree> monsterThreeList;
    private List<MonsterBullet> monsterBulletList;
    private List<CalabashBullet> calabashBulletList;
    private List<Blast> blastList;

    private boolean isExited = false;
    private CalabashThread calabashThread;
    private GrandfatherThread grandfatherThread;
    private MonsterThread monsterThread;
    private TimeControl timeControl;

    GrandFatherMouseAdapter adapter;

    /**
     * 直接开始游戏
     */
    public void startGame() {
        this.addKeyListener(calabashThread);
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
        this.requestFocus();

        resetBoard();

        this.render.execute(new RenderThread(this));
        executor.execute(calabashThread);
        executor.execute(grandfatherThread);
        executor.execute(monsterThread);
        executor.execute(timeControl);
        executor.execute(this);

        executor.shutdown();
        render.shutdown();
    }

    /**
     * 加载存储好的游戏
     */
    public void loadGame() {
        // TODO
    }

    public GameController(int fps) {
        this.fps = fps;
        // 并发容器的使用
        // 线程安全的arrayList
        this.monsterOneList = new CopyOnWriteArrayList<>();
        this.monsterTwoList = new CopyOnWriteArrayList<>();
        this.monsterThreeList = new CopyOnWriteArrayList<>();
        this.monsterBulletList = new CopyOnWriteArrayList<>();
        this.calabashBulletList = new CopyOnWriteArrayList<>();
        this.blastList = new CopyOnWriteArrayList<>();
        calabashThread = new CalabashThread();
        adapter = new GrandFatherMouseAdapter();
        grandfatherThread = new GrandfatherThread();
        monsterThread = new MonsterThread();
        timeControl = new TimeControl();
    }

    public int getFps() {
        return this.fps;
    }

    @Override
    public void run() {
        while (!isExited) {
            monsterCollision();
            calabashCollision();

            try {
                TimeUnit.MILLISECONDS.sleep(40);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint();
        }
    }

    private synchronized void monsterCollision() {
        // 检查妖精1是否被葫芦娃打到
        // 敌方没有血量，直接死亡
        // TODO: 这里三个循环可以抽象一个方法
        int calabashBulletLength = calabashBulletList.size();
        int monsterOneLength = monsterOneList.size();

        for (int i = 0; i < monsterOneLength; i++) {
            MonsterOne monsterOne = monsterOneList.get(i);
            for (int j = 0; j < calabashBulletLength; j++) {
                CalabashBullet bullet = calabashBulletList.get(j);
                if (GameObject.isCollide(monsterOne, bullet)) {
                    Blast blast = new Blast(bullet.getX(), bullet.getY());
                    blastList.add(blast);
                    calabashBulletList.remove(bullet);
                    monsterOneList.remove(monsterOne);

                    // 第一类妖精的分数设置为10分
                    score += 10;
                    scoreLabel.setText("Score: " + score);
                    monsterOneLength--;
                    calabashBulletLength--;
                    // 返回妖精的上一个位置
                    i--;
                    // 碰撞了就可以跳出循环了
                    break;
                }
            }
        }

        int monsterTwoLength = monsterTwoList.size();

        for (int i = 0; i < monsterTwoLength; i++) {
            MonsterTwo monsterTwo = monsterTwoList.get(i);
            for (int j = 0; j < calabashBulletLength; j++) {
                CalabashBullet bullet = calabashBulletList.get(j);
                if (GameObject.isCollide(monsterTwo, bullet)) {
                    Blast blast = new Blast(bullet.getX(), bullet.getY());
                    blastList.add(blast);
                    calabashBulletList.remove(bullet);
                    monsterTwoList.remove(monsterTwo);

                    // 第二类妖精的分数设置为20分
                    score += 20;
                    scoreLabel.setText("Score: " + score);
                    monsterTwoLength--;
                    calabashBulletLength--;
                    i--;
                    break;
                }
            }
        }

        int monsterThreeLength = monsterThreeList.size();

        for (int i = 0; i < monsterThreeLength; i++) {
            MonsterThree monsterThree = monsterThreeList.get(i);
            for (int j = 0; j < calabashBulletLength; j++) {
                CalabashBullet bullet = calabashBulletList.get(j);
                if (GameObject.isCollide(monsterThree, bullet)) {
                    Blast blast = new Blast(bullet.getX(), bullet.getY());
                    blastList.add(blast);
                    calabashBulletList.remove(bullet);
                    monsterThreeList.remove(monsterThree);

                    // 第三类妖精的分数设置为30分
                    score += 30;
                    scoreLabel.setText("Score: " + score);
                    monsterThreeLength--;
                    calabashBulletLength--;
                    i--;
                    break;
                }
            }
        }
    }

    private synchronized void calabashCollision() {
        // 妖精子弹和葫芦娃的碰撞
        int monsterBulletLength = monsterBulletList.size();
        for (int i = 0; i < monsterBulletLength; i++) {
            MonsterBullet bullet = monsterBulletList.get(i);
            if (GameObject.isCollide(bullet, calabash)) {
                monsterBulletList.remove(bullet);
                monsterBulletLength--;
                // 一次造成的伤害为5点
                // TODO:改成不同的妖精有不同的伤害
                calabash.decreaseHP(5);
                if (calabash.getHP() >= 0) {
                    HPLabel.setText("HP: " + calabash.getHP());
                } else {
                    HPLabel.setText("HP: " + 0);
                }
                if (calabash.getHP() <= 0) {
                    // 死亡，结束
                    STATE = GameState.GAME_OVER;
                }
            }
        }
    }

    private class CalabashThread implements Runnable, KeyListener {
        private Thread thread = Thread.currentThread();

        /**
         * 用一个HashMap来存储某个按键是否被按下
         */
        private Map<Integer, Boolean> keys;
        /**
         * 记录存放的按键数量
         */
        private final static int KEY_COUNTS = 1000;


        public CalabashThread() {
            System.out.println("[CalabashThead]created");
            init();
        }

        @Override
        public void run() {
            // 葫芦娃的移动，攻击，监听键盘事件
            while (!isExited) {
                moving();
                calabashBulletMove(TIME);

                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                repaint();
            }
        }

        public void moving() {
            // 监听一下键盘的事件
            if (getKeyDown(KeyEvent.VK_W) || getKeyDown(KeyEvent.VK_UP)) {
                // 向上走y值减小
                // 判断会不会走出边界
                calabash.moveUp();
            } else if (getKeyDown(KeyEvent.VK_S) || getKeyDown(KeyEvent.VK_DOWN)) {
                // 向下走y值增大
                // 判断会不会走出边界
                calabash.moveDown();
            } else if (getKeyDown(KeyEvent.VK_A) || getKeyDown(KeyEvent.VK_LEFT)) {
                // 向左走x值减小
                // 判断会不会走出边界
                calabash.moveLeft();
            } else if (getKeyDown(KeyEvent.VK_D) || getKeyDown(KeyEvent.VK_RIGHT)) {
                // 向右走x值增大
                // 判断会不会走出边界
                calabash.moveRight();
            } else if (getKeyDown(KeyEvent.VK_J)) {
                // 按j发射子弹
                CalabashBullet bullet = calabash.calabashFire();
                if (TIME % calabash.getFireInterval() == 0) {
                    calabashBulletList.add(bullet);
                }
            } else if (getKeyDown(KeyEvent.VK_ENTER)) {
                if (GameController.STATE == GameState.START) {
                    STATE = GameState.RUNNING;
                } else if (GameController.STATE == GameState.GAME_OVER) {
                    STATE = GameState.RUNNING;
                    restart();
                }
            } else if (getKeyDown(KeyEvent.VK_X)) {
                // 按x使用技能
                if (calabash.haveSkill() && calabash.isFirstUse()) {
                    calabash.useSkill();
                    // 只能够使用一次技能
                    calabash.setFirstUse();
                    if (calabash.getCurSkill().getName() == SkillName.RECOVER_SKILL) {
                        // 更改血量的显示
                        HPLabel.setText("HP: " + calabash.getHP());
                    }
                }
            } else if (getKeyDown(KeyEvent.VK_L)) {
                // 按L来加载上次存储的数据
                try {
                    loadData();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (GameController.STATE == GameState.START) {
                    STATE = GameState.RUNNING;
                } else if (GameController.STATE == GameState.GAME_OVER) {
                    STATE = GameState.RUNNING;
                }
            }
        }

        public void calabashBulletMove(long time) {
            for (CalabashBullet bullet : calabashBulletList) {
                bullet.move(time);
            }
        }

        public void init() {
            keys = new HashMap<>(KEY_COUNTS);
            for (int i = 0; i < KEY_COUNTS; i++) {
                keys.put(i, false);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // nothing to do
        }

        @Override
        public void keyPressed(KeyEvent key) {
            // 当有某个按键被按下时
            keys.put(key.getKeyCode(), true);
        }

        @Override
        public void keyReleased(KeyEvent key) {
            // 当有某个按键松开时
            keys.put(key.getKeyCode(), false);
        }

        public boolean getKeyDown(int keyCode) {
            return keys.get(keyCode);
        }
    }

    private class MonsterThread implements Runnable {
        private Thread thread = Thread.currentThread();

        public MonsterThread() {
            // test
            System.out.println("[MonsterThread]created");
        }

        @Override
        public void run() {
            while (!isExited) {
                monsterMove(TIME);
                monsterBulletMove(TIME);
                monsterAppear(TIME);
                monsterFire(TIME);

                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                repaint();
            }
        }

        private void monsterMove(long time) {
            for (MonsterOne monsterOne : monsterOneList) {
                monsterOne.move(time);
            }
            for (MonsterTwo monsterTwo : monsterTwoList) {
                monsterTwo.move(time);
            }
            for (MonsterThree monsterThree : monsterThreeList) {
                monsterThree.move(time);
            }
        }

        /**
         * 妖精子弹移动
         * @param time 游戏时间
         */
        private void monsterBulletMove(long time) {
            for (MonsterBullet monsterBullet : monsterBulletList) {
                monsterBullet.move(time);
            }
        }

        private static final int FIRE_INTERVAL_ONE = 1000;
        private static final int FIRE_INTERVAL_TWO = 2000;
        private static final int FIRE_INTERVAL_THREE = 3000;
        private static final int MONSTER_ONE_APPEAR = 2000;
        private static final int MONSTER_TWO_APPEAR = 4000;
        private static final int MONSTER_THREE_APPEAR = 4000;

        /**
         * 妖精发射子弹的时间
         * @param time 游戏时间
         */
        private void monsterFire(long time) {
            if (time % FIRE_INTERVAL_ONE == 0) {
                for (MonsterOne monsterOne : monsterOneList) {
                    if (isInGameScreen(monsterOne)) {
                        MonsterBullet monsterBullet = monsterOne.monsterFire();
                        monsterBulletList.add(monsterBullet);
                    }
                }
            }
            if (time % FIRE_INTERVAL_TWO == 0) {
                for (MonsterTwo monsterTwo : monsterTwoList) {
                    if (isInGameScreen(monsterTwo)) {
                        MonsterBullet monsterBullet = monsterTwo.monsterFire();
                        monsterBulletList.add(monsterBullet);
                    }
                }
            }
            if (time % FIRE_INTERVAL_THREE == 0) {
                for (MonsterThree monsterThree : monsterThreeList) {
                    if (isInGameScreen(monsterThree)) {
                        MonsterBullet monsterBullet = monsterThree.monsterFire();
                        monsterBulletList.add(monsterBullet);
                    }
                }
            }
        }

        /**
         * 妖精出现的时间
         * @param time 游戏时间
         */
        private void monsterAppear(long time) {
            Random random = new Random();
            // 妖精一出现的时间
            if (time % MONSTER_ONE_APPEAR == 0) {
                MonsterOne monsterOne = new MonsterOne(GameScreen.getWid(), random.nextInt(GameScreen.getHei() - 200));
                monsterOneList.add(monsterOne);
            }
            // 妖精二出现的时间
            if (time % MONSTER_TWO_APPEAR == 0) {
                MonsterTwo monsterTwo = new MonsterTwo(GameScreen.getWid(), random.nextInt(GameScreen.getHei() - 200));
                monsterTwoList.add(monsterTwo);
            }
            if (time % MONSTER_THREE_APPEAR == 0) {
                MonsterThree monsterThree = new MonsterThree(GameScreen.getWid(), random.nextInt(GameScreen.getHei() - 200));
                monsterThreeList.add(monsterThree);
            }
        }
    }

    private class GrandfatherThread implements Runnable {
        private Thread thread = Thread.currentThread();

        private static final int GIVE_SKILL_INTERVAL = 4000;

        public GrandfatherThread() {
            // test
            System.out.println("[GrandfatherThread]created");
        }

        @Override
        public void run() {
            while (!isExited) {
                moving();

                // 根据时间的间隔给予葫芦娃技能
                if (TIME % GIVE_SKILL_INTERVAL == 0) {
                    // 清空moveSkill和cdSkill的效果
                    calabash.clearSkillImpact();
                    grandFather.clearSkillImpact();
                    grandFather.giveSkill();
                    skillLabel.setText("curSkill: " + calabash.getCurSkill().getName());
                    calabash.setFirstUse();
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * 爷爷通过鼠标移动来实现移动
         */
        public void moving() {

        }
    }

    private boolean isInGameScreen(GameObject gameObject) {
        return gameObject.getX() + 150 <= GameScreen.getWid() && gameObject.getY() + 150 <= GameScreen.getHei();
    }

    public void restart() {
        monsterOneList.clear();
        monsterTwoList.clear();
        monsterThreeList.clear();
        monsterBulletList.clear();
        calabashBulletList.clear();
        STATE = GameState.START;
        score = 0;
        calabash.resetHP();
        resetBoard();
    }

    private void resetBoard() {
        // 清空JPanel里的所有内容
        this.removeAll();
        this.addKeyListener(calabashThread);

        // 葫芦娃的初始位置
        calabash = Calabash.getInstance();
        grandFather = GrandFather.getInstance();

        // 初始化一些Label
        scoreLabel = new JLabel("Score: " + this.score);
        scoreLabel.setForeground(Color.RED);
        HPLabel = new JLabel("HP: " + calabash.getHP());
        HPLabel.setForeground(Color.RED);
        skillLabel = new JLabel("curSkill: null");
        skillLabel.setForeground(Color.RED);

        // 游戏继续按钮
        final JButton goOnButton = new JButton("继续");
        goOnButton.setForeground(Color.RED);
        // 设置按钮为透明
        goOnButton.setContentAreaFilled(false);
        // 游戏暂停按钮
        JButton stopButton = new JButton("暂停");
        stopButton.setForeground(Color.RED);
        stopButton.setContentAreaFilled(false);

        // 游戏存储按钮
        JButton storeButton = new JButton("store");
        storeButton.setForeground(Color.RED);
        storeButton.setContentAreaFilled(false);

        // 继续按钮添加监听
        goOnButton.addActionListener(e -> {
            if (STATE == GameState.PAUSE) {
                STATE = GameState.RUNNING;
                /*
                 * 重新使JPanel成为焦点！一定需要从新请求焦点。不然暂停一次键盘就直接失灵了
                 */
                GameController.this.requestFocusInWindow();
            }
        });
        // 暂停按钮添加监听
        stopButton.addActionListener(e -> {
            if (STATE == GameState.RUNNING) {
                STATE = GameState.PAUSE;
            }
        });

        // 存储按钮添加监听
        storeButton.addActionListener(e -> {
            try {
                storeData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // 新建一个面板，把按钮和标签都加入到面板中间
        JPanel labelPanel = new JPanel();
        // 设置背景颜色为黑色
        labelPanel.setBackground(Color.BLACK);
        // 使用浮动布局管理器
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 10));
        labelPanel.add(scoreLabel);
        labelPanel.add(HPLabel);
        labelPanel.add(skillLabel);
        labelPanel.add(goOnButton);
        labelPanel.add(stopButton);
        labelPanel.add(storeButton);

        // JPanel 面板添加这个面板
        this.add(labelPanel);
    }

    public static void addTime() {
        TIME += 20;
    }

    /**
     * 绘制游戏时的背景
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage bgImage = ReadImage.runningBackground;
        g.drawImage(bgImage, 0, 0, 1080, 680, null);
    }

    @Override
    public void paint(Graphics g) {
        // TODO: 绘制所有物体
        // TODO: 双缓冲
        super.paint(g);

        // 如果为初始状态
        if (STATE == GameState.START) {
            paintStart(g);
            // 如果为游戏运行状态
        } else if (STATE == GameState.RUNNING) {
            // 绘制葫芦娃
            paintCalabash(g);
            // 绘制妖精
            paintMonster(g);
            // 绘制爷爷
            paintGrandfather(g);
            // 绘制一组妖精子弹
            paintMonsterBullets(g);
            // 绘制葫芦娃的子弹
            paintCalabashBullets(g);
            // 绘制爆炸特效和移除爆炸特效
            paintBlast(g);
        } else if (STATE == GameState.PAUSE) {
            g.setFont(new Font("黑体", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("游戏暂停", 450, 320);
        } else if (STATE == GameState.GAME_OVER) {
            // paintOver(g);
        }
    }

    public void paintStart(Graphics g) {
        g.drawImage(ReadImage.startBackground, 0, 0, 1080, 680, null);
        Font font = new Font("黑体", Font.PLAIN, 20);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("按ENTER键开始游戏", 50, 400);
        g.drawString("J:发射子弹", 50, 450);
        g.drawString("X:使用技能", 50, 500);
        g.drawString("L:加载存档", 50, 550);
        g.drawString("方向键:↑,↓,←,→", 50, 600);
        g.drawString("作者:Martin", 50, 650);
    }

    public void paintOver(Graphics g) {
        // 绘制结束界面
        // TODO
    }

    private void paintBlast(Graphics g) {
        int length = this.blastList.size();
        for (int i = 0; i < length; i++) {
            Blast blast = blastList.get(i);
            blast.draw(g);
            // 绘制后立即移除
            blastList.remove(blast);
            break;
        }
    }

    private void paintCalabash(Graphics g) {
        g.drawImage(ReadImage.Calabash, calabash.getX(), calabash.getY(), 100, 100, null);
    }

    private void paintGrandfather(Graphics g) {
        g.drawImage(ReadImage.GrandFather, grandFather.getX(), grandFather.getY(), 100, 100, null);
    }

    private void paintMonster(Graphics g) {
        for (MonsterOne monsterOne : this.monsterOneList) {
            monsterOne.draw(g);
        }

        for (MonsterTwo monsterTwo : this.monsterTwoList) {
            monsterTwo.draw(g);
        }

        for (MonsterThree monsterThree : this.monsterThreeList) {
            monsterThree.draw(g);
        }
    }

    private void paintCalabashBullets(Graphics g) {
        for (CalabashBullet bullet : this.calabashBulletList) {
            bullet.draw(g);
        }
    }

    private void paintMonsterBullets(Graphics g) {
        for (MonsterBullet bullet : this.monsterBulletList) {
            bullet.draw(g);
        }
    }

    /**
     * 保存当前游戏的数据
     * @throws IOException IO异常
     */
    public void storeData() throws IOException {
        // 保存当前有的葫芦娃、爷爷和妖精的属性即可
        // 序列化保存妖精一
        final String root = "src/main/resources/data/";
        FileOutputStream fileMonsterOne = new FileOutputStream(root + "monster_one.ser");
        ObjectOutputStream outMonsterOne = new ObjectOutputStream(fileMonsterOne);
        for (MonsterOne monsterOne : monsterOneList) {
            outMonsterOne.writeObject(monsterOne);
        }

        // 序列化保存妖精二
        FileOutputStream fileMonsterTwo = new FileOutputStream(root + "monster_two.ser");
        ObjectOutputStream outMonsterTwo = new ObjectOutputStream(fileMonsterTwo);

        for (MonsterTwo monsterTwo : monsterTwoList) {
            outMonsterTwo.writeObject(monsterTwo);
        }

        // 序列化保存妖精三
        FileOutputStream fileMonsterThree = new FileOutputStream(root + "monster_three.ser");
        ObjectOutputStream outMonsterThree = new ObjectOutputStream(fileMonsterThree);

        for (MonsterThree monsterThree : monsterThreeList) {
            outMonsterThree.writeObject(monsterThree);
        }

        // 序列化保存葫芦娃
        FileOutputStream fileCalabash = new FileOutputStream(root + "calabash.ser");
        ObjectOutputStream outCalabash = new ObjectOutputStream(fileCalabash);
        outCalabash.writeObject(calabash);

        // 序列化保存爷爷
        FileOutputStream fileGrandfather = new FileOutputStream(root + "grandfather.ser");
        ObjectOutputStream outGrandFather = new ObjectOutputStream(fileGrandfather);
        outGrandFather.writeObject(grandFather);

        // 序列化保存葫芦娃子弹
        FileOutputStream fileCalabashBullet = new FileOutputStream(root + "calabash_bullet.ser");
        ObjectOutputStream outCalabashBullet = new ObjectOutputStream(fileCalabashBullet);

        for (CalabashBullet bullet : calabashBulletList) {
            outCalabashBullet.writeObject(bullet);
        }

        // 序列化保存妖精子弹
        FileOutputStream fileMonsterBullet = new FileOutputStream(root + "monster_bullet.ser");
        ObjectOutputStream outMonsterBullet = new ObjectOutputStream(fileMonsterBullet);

        for (MonsterBullet bullet : monsterBulletList) {
            outMonsterBullet.writeObject(bullet);
        }

        System.out.println("存储成功");
        fileMonsterOne.close();
        outMonsterOne.close();
        fileMonsterTwo.close();
        outMonsterTwo.close();
        fileMonsterThree.close();
        outMonsterThree.close();

        fileCalabash.close();
        outCalabash.close();
        fileGrandfather.close();
        outGrandFather.close();
        fileCalabashBullet.close();
        outCalabashBullet.close();
        fileMonsterBullet.close();
        outMonsterBullet.close();
    }

    /**
     * 加载数据
     * @throws IOException IO异常
     * @throws ClassNotFoundException 找不到该类型异常
     */
    public void loadData() throws IOException, ClassNotFoundException {
        // 读取文件
        final String root = "src/main/resources/data/";
        FileInputStream fileMonsterOne = new FileInputStream(root + "monster_one.ser");
        ObjectInputStream inMonsterOne = new ObjectInputStream(fileMonsterOne);

        MonsterOne monsterOne;
        while (inMonsterOne.available() > 0) {
            monsterOne = (MonsterOne) inMonsterOne.readObject();
            this.monsterOneList.add(monsterOne);
        }

        FileInputStream fileMonsterTwo = new FileInputStream(root + "monster_two.ser");
        ObjectInputStream inMonsterTwo = new ObjectInputStream(fileMonsterTwo);

        MonsterTwo monsterTwo;
        while (inMonsterTwo.available() > 0) {
            monsterTwo = (MonsterTwo) inMonsterTwo.readObject();
            this.monsterTwoList.add(monsterTwo);
        }

        FileInputStream fileMonsterThree = new FileInputStream(root + "monster_three.ser");
        ObjectInputStream inMonsterThree = new ObjectInputStream(fileMonsterThree);

        MonsterThree monsterThree;
        while (inMonsterThree.available() > 0) {
            monsterThree = (MonsterThree) inMonsterThree.readObject();
            this.monsterThreeList.add(monsterThree);
        }

        FileInputStream fileCalabash = new FileInputStream(root + "calabash.ser");
        ObjectInputStream inCalabash = new ObjectInputStream(fileCalabash);
        this.calabash = (Calabash) inCalabash.readObject();

        FileInputStream fileGrandfather = new FileInputStream(root + "grandfather.ser");
        ObjectInputStream inGrandfather = new ObjectInputStream(fileGrandfather);
        this.grandFather = (GrandFather) inGrandfather.readObject();

        // 读取子弹
        FileInputStream fileCalabashBullet = new FileInputStream(root + "calabash_bullet.ser");
        ObjectInputStream inCalabashBullet = new ObjectInputStream(fileCalabashBullet);

        CalabashBullet calabashBullet;
        while (inCalabashBullet.available() > 0) {
            calabashBullet = (CalabashBullet) inCalabashBullet.readObject();
            this.calabashBulletList.add(calabashBullet);
        }

        FileInputStream fileMonsterBullet = new FileInputStream(root + "monster_bullet.ser");
        ObjectInputStream inMonsterBullet = new ObjectInputStream(fileMonsterBullet);

        MonsterBullet monsterBullet;
        while (inMonsterBullet.available() > 0) {
            monsterBullet = (MonsterBullet) inMonsterBullet.readObject();
            this.monsterBulletList.add(monsterBullet);
        }

        System.out.println("load succeed!");
        fileMonsterOne.close();
        inMonsterOne.close();
        fileMonsterTwo.close();
        inMonsterTwo.close();
        fileMonsterThree.close();
        inMonsterThree.close();
        fileCalabash.close();
        inCalabash.close();
        fileGrandfather.close();
        inGrandfather.close();
        fileCalabashBullet.close();
        inCalabashBullet.close();
        fileMonsterBullet.close();
        inMonsterBullet.close();
    }

    public Calabash getCalabash() {
        return this.calabash;
    }
}
