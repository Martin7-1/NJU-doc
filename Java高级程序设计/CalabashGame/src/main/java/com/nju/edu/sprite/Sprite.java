package com.nju.edu.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
public class Sprite extends GameObject {

    protected int speed = 1;

    public Sprite() {

    }

    public Sprite(int x, int y, int width, int height, BufferedImage image) {
        super(x, y, width, height, image);
    }

    @Override
    public void move(long time) {

    }

    @Override
    public void draw(Graphics g) {
        // 在(x, y)处绘制图片，width和height参数来进行缩放，注意比例
        g.drawImage(this.image, x, y,width, height, null);
    }
}
