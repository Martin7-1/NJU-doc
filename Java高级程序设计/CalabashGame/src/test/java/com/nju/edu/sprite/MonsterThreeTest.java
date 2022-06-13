package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonsterThreeTest {

    private MonsterThree monsterThree = new MonsterThree(100, 100);
    private long time = 0;

    @Test
    public void moveTest() {
        monsterThree.move(time);

        assertEquals(98, monsterThree.getX());
    }

    @Test
    public void bulletTest() {
        MonsterBullet bullet = monsterThree.monsterFire();

        assertEquals(monsterThree.getX(), bullet.getX());
    }
}
