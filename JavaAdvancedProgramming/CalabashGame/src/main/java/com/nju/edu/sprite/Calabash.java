package com.nju.edu.sprite;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.skill.Skill;
import com.nju.edu.skill.SkillName;
import com.nju.edu.util.ReadImage;

import java.io.Serializable;

/**
 * @author Zyi
 */
public class Calabash extends Sprite implements Serializable {

    private static final Calabash CALABASH = new Calabash(100, 320);
    private static final long serialVersionUID = -4970820453164850503L;

    public static Calabash getInstance() {
        return CALABASH;
    }

    public transient Skill skill;
    private boolean isFirstUse = true;
    /**
     * 葫芦娃的血量
     */
    public int HP = 100;
    private int fireInterval = 120;

    private Calabash(int x, int y) {
        super(x, y, 100, 100, ReadImage.Calabash);
        this.speed = 10;
    }

    private Calabash(int x, int y, int speed) {
        super(x, y, 100, 100, ReadImage.Calabash);
        this.speed = speed;
    }

    public Calabash() {
        // serializable
        this.speed = 10;
    }

    public void moveUp() {
        if (this.y - speed >= 0) {
            this.y -= speed;
        }
    }

    public void moveDown() {
        if (this.y + speed <= GameScreen.getHei() - 150) {
            this.y += speed;
        }
    }

    public void moveLeft() {
        if (this.x - speed >= 0) {
            this.x -= speed;
        }
    }

    public void moveRight() {
        if (this.x + speed <= GameScreen.getWid() - 150) {
            this.x += speed;
        }
    }

    public CalabashBullet calabashFire() {
        CalabashBullet bullet = new CalabashBullet(this.x + width, this.y + height / 2);
        return bullet;
    }

    /**
     * 获得当前Calabash的血量
     * @return 血量
     */
    public int getHP() {
        return this.HP;
    }

    /**
     * 受到伤害减少血量
     * @param damage 伤害
     */
    public void decreaseHP(int damage) {
        this.HP -= damage;
    }

    public void recover() {
        if (this.HP + 10 > 100) {
            this.HP = 100;
        } else {
            this.HP += 10;
        }
    }

    public void resetHP() {
        this.HP = 100;
    }

    public boolean haveSkill() {
        return this.skill != null;
    }

    public void useSkill() {
        System.out.println("Use skill: " + this.skill.getName());
        this.skill.start();
    }

    public boolean isFirstUse() {
        return this.isFirstUse;
    }

    public void setFirstUse() {
        isFirstUse = !isFirstUse;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Skill getCurSkill() {
        // 获得当前拥有的技能
        return this.skill;
    }

    public void speedUp(boolean haveSkill) {
        if (haveSkill) {
            // 加速
            this.speed += 5;
        }
    }

    private void speedDown() {
        this.speed -= 5;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void clearSkillImpact() {
        // 根据当前技能来清空技能效果
        if (this.skill != null) {
            if (this.skill.getName() == SkillName.MOVE_SKILL && this.speed == 15) {
                speedDown();
            } else if (this.skill.getName() == SkillName.CD_SKILL && this.fireInterval == 80) {
                this.fireInterval = 120;
            } else if (this.skill.getName() == SkillName.RECOVER_SKILL) {
                // nothing to do, recover do not need to reset
            }
        }
    }

    public int getFireInterval() {
        return this.fireInterval;
    }

    public void setFireInterval(int fireInterval) {
        this.fireInterval = fireInterval;
    }
}
