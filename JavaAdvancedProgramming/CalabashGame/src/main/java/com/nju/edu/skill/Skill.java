package com.nju.edu.skill;

/**
 * @author Zyi
 */
public interface Skill {

    int SKILL_AMOUNT = 3;

    /**
     * 判断当前是否拥有该技能
     * @return true if the calabash have the skill, false otherwise
     */
    boolean isHaveSkill();

    /**
     * 获得该技能
     */
    void haveSkill();

    /**
     * 使用技能
     */
    void start();

    /**
     * 获得当前的技能
     * @return 技能名字
     */
    SkillName getName();
}
