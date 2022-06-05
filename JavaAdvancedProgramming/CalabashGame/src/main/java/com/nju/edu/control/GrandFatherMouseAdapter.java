package com.nju.edu.control;

import com.nju.edu.sprite.GrandFather;
import com.nju.edu.util.GameState;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 爷爷的鼠标控制类
 * @author Zyi
 */
public class GrandFatherMouseAdapter extends MouseAdapter {

    private GrandFather grandFather = GrandFather.getInstance();

    @Override
    public void mouseClicked(MouseEvent e) {
        // nothing to do
        // mouse click event
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mx = grandFather.getX();
        int my = grandFather.getY();

        if (GameController.STATE == GameState.RUNNING) {
            // 如果正在游戏中
            // 就将爷爷移动到对应的地方
            grandFather.setX(mx);
            grandFather.setY(my);
        }
    }
}
