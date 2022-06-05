package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonsterOneTest {

    private MonsterOne monsterOne = new MonsterOne(100, 100);
    private long time = 0;

    @Test
    public void moveDownTest() {
        monsterOne.move(time);

        assertEquals(94, monsterOne.getX());
    }

    @Test
    public void monsterBulletTest() {
        MonsterBullet monsterBullet = monsterOne.monsterFire();

        assertEquals(monsterOne.getX(), monsterBullet.getX());
    }

}
