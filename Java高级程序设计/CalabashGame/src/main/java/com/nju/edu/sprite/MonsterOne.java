package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.util.ReadImage;

import java.io.Serializable;

/**
 * @author Zyi
 */
public class MonsterOne extends Sprite implements Monster, Serializable {

    private static final long serialVersionUID = 4531157429822834022L;

    public MonsterOne() {

    }

    public MonsterOne(int x, int y) {
        super(x, y, 100, 100, ReadImage.MonsterOne);
        this.speed = 6;
    }

    public MonsterOne(int x, int y, int speed) {
        super(x, y, 40, 40, ReadImage.MonsterOne);
        this.speed = speed;
    }

    @Override
    public void move(long time) {
        // 游戏的怪物都是从左边向右边走的
        // monsterOne的设定就是很慢的朝左走
        this.x -= speed;
    }

    @Override
    public MonsterBullet monsterFire() {
        MonsterBullet bullet = new MonsterBullet(this.x, this.y + height / 2);

        return bullet;
    }
}
