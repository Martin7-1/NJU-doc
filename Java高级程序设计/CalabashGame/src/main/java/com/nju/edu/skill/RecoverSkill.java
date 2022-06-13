package com.nju.edu.skill;

import com.nju.edu.screen.GameScreen;
import com.nju.edu.sprite.Calabash;

/**
 * 给葫芦娃恢复血量
 * @author Zyi
 */
public class RecoverSkill implements Skill {

    private boolean haveSkill = false;
    private final SkillName name = SkillName.RECOVER_SKILL;
    private Calabash calabash = Calabash.getInstance();

    @Override
    public boolean isHaveSkill() {
        return this.haveSkill;
    }

    @Override
    public void haveSkill() {
        this.haveSkill = true;
    }

    @Override
    public void start() {
        this.calabash.recover();
    }

    @Override
    public SkillName getName() {
        return this.name;
    }
}
