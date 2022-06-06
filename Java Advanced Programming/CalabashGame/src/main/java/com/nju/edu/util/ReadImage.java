package com.nju.edu.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Zyi
 */
public class ReadImage {

    public static BufferedImage MonsterOne;
    public static BufferedImage MonsterTwo;
    public static BufferedImage MonsterThree;

    public static BufferedImage Calabash;
    public static BufferedImage GrandFather;

    public static BufferedImage MonsterBullet;
    public static BufferedImage CalabashBullet;

    public static BufferedImage startBackground;
    public static BufferedImage runningBackground;

    public static BufferedImage blast;

    private static final String ROOT = "src/main/resources/image/";

    static {
        try {
            MonsterOne = ImageIO.read(new FileInputStream(ROOT + "monster1.png"));
            MonsterTwo = ImageIO.read(new FileInputStream(ROOT + "monster2.png"));
            MonsterThree = ImageIO.read(new FileInputStream(ROOT + "monster3.png"));
            Calabash = ImageIO.read(new FileInputStream(ROOT + "calabash.png"));
            GrandFather = ImageIO.read(new FileInputStream(ROOT + "grandfather.png"));
            MonsterBullet = ImageIO.read(new FileInputStream(ROOT + "monsterBullet.png"));
            CalabashBullet = ImageIO.read(new FileInputStream(ROOT + "calabashBullet.png"));
            // TODO: find a background image
            startBackground = ImageIO.read(new FileInputStream(ROOT + "startBg.png"));
            runningBackground = ImageIO.read(new FileInputStream(ROOT + "runningBg.png"));
            blast = ImageIO.read(new FileInputStream(ROOT + "blast.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
