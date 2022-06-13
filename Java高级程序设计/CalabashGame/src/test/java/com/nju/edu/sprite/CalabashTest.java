package com.nju.edu.sprite;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.skill.CDSkill;
import com.nju.edu.skill.MoveSkill;
import com.nju.edu.skill.RecoverSkill;
import com.nju.edu.skill.Skill;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CalabashTest {

    private static Calabash calabash;

    @BeforeClass
    public static void setUp() {
        calabash = Calabash.getInstance();
    }

    @After
    public void destroy() {
        calabash.clearSkillImpact();
    }

    @Test
    public void moveSkillTest() {
        Skill moveSkill = new MoveSkill();
        calabash.setSkill(moveSkill);
        moveSkill.start();

        assertEquals(15, calabash.getSpeed());
    }

    @Test
    public void CDSkillTest() {
        Skill CDSkill = new CDSkill();
        calabash.setSkill(CDSkill);
        CDSkill.start();

        assertEquals(80, calabash.getFireInterval());
    }

    @Test
    public void recoverTest() {
        Skill recoverSkill = new RecoverSkill();
        calabash.setSkill(recoverSkill);
        calabash.decreaseHP(20);
        recoverSkill.start();

        assertEquals(90, calabash.getHP());
    }

    @Test
    public void moveRightTest() {
        calabash.moveRight();

        assertEquals(110, calabash.getX());
    }

    @Test
    public void moveLeftTest() {
        calabash.moveLeft();

        assertEquals(100, calabash.getX());
    }

    @Test
    public void moveUpTest() {
        calabash.moveUp();

        assertEquals(310, calabash.getY());
    }

    @Test
    public void moveDownTest() {
        calabash.moveDown();

        assertEquals(320, calabash.getY());
    }

    @Test
    public void bulletTest() {
        CalabashBullet bullet = calabash.calabashFire();

        assertEquals(bullet.getX(), calabash.getX() + 100);
    }
}
