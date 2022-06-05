package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;

/**
 * @author Zyi
 */
public interface Monster {

    /**
     * 怪物发射子弹
     * @return 子弹
     */
    MonsterBullet monsterFire();
}
