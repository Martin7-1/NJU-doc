package com.nju.edu.sprite;

import com.nju.edu.util.ReadImage;

import java.awt.image.BufferedImage;

/**
 * 爆炸特效类
 * @author Zyi
 */
public class Blast extends Sprite{

    public Blast(int x, int y) {
        super(x - 20, y - 20, 100, 100, ReadImage.blast);
    }
}
