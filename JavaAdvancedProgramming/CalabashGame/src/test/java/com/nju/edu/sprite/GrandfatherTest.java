package com.nju.edu.sprite;

import com.nju.edu.skill.SkillName;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrandfatherTest {

    private static Calabash calabash = Calabash.getInstance();
    private static GrandFather grandFather = GrandFather.getInstance();

    @Test
    public void moveRightTest() {
        grandFather.moveRight();
        grandFather.moveRight();

        assertEquals(20, grandFather.getX());
    }

    @Test
    public void moveLeftTest() {
        grandFather.moveLeft();

        assertEquals(10, grandFather.getX());
    }

    @Test
    public void moveUpTest() {
        grandFather.moveUp();

        assertEquals(310, grandFather.getY());
    }

    @Test
    public void moveDownTest() {
        grandFather.moveDown();

        assertEquals(320, grandFather.getY());
    }

    @Test
    public void giveSkillTest() {
        // 爷爷给葫芦娃技能
        grandFather.giveSkill();
        assertEquals(calabash.getCurSkill().getName(), SkillName.CD_SKILL);
        grandFather.giveSkill();
        assertEquals(calabash.getCurSkill().getName(), SkillName.RECOVER_SKILL);
        grandFather.giveSkill();
        assertEquals(calabash.getCurSkill().getName(), SkillName.MOVE_SKILL);
    }
}
