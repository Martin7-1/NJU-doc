package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.util.ReadImage;

import java.io.Serializable;

/**
 * @author Zyi
 */
public class MonsterThree extends Sprite implements Monster, Serializable {

    private static final long serialVersionUID = 3424717620188168556L;

    public MonsterThree() {

    }

    public MonsterThree(int x, int y) {
        super(x, y, 150, 150, ReadImage.MonsterThree);
        this.speed = 2;
    }

    public MonsterThree(int x, int y, int speed) {
        super(x, y, 50, 50, ReadImage.MonsterThree);
        this.speed = speed;
    }

    @Override
    public void move(long time) {
        // 游戏的怪物都是从左边向右边走的
        // monsterThree的设定就是越走越快
        this.x -= speed;
        if (time % 6000 == 0) {
            speed++;
        }
    }

    @Override
    public MonsterBullet monsterFire() {
        MonsterBullet bullet = new MonsterBullet(this.x, this.y + height / 2);

        return bullet;
    }
}
