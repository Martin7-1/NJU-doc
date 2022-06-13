package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonsterTwoTest {

    private MonsterTwo monsterTwo = new MonsterTwo(100, 100);
    private long time = 0;

    @Test
    public void moveTest() {
        monsterTwo.move(time);

        assertEquals(99, monsterTwo.getX());
    }

    @Test
    public void bulletTest() {
        MonsterBullet bullet = monsterTwo.monsterFire();

        assertEquals(monsterTwo.getX(), bullet.getX());
    }
}
