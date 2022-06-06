package com.nju.edu.screen;

import javax.swing.*;
import java.awt.*;

/**
 * @author Zyi
 */
public class GameScreen extends JFrame {
    private static final GameScreen GAME_SCREEN = new GameScreen("Calabash Game", Color.WHITE);

    public static GameScreen getInstance() {
        return GAME_SCREEN;
    }
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 680;

    private final String windowTitle;
    private final Color bgColor;

    public GameScreen(String windowTitle, Color bgColor) {
        this.windowTitle = windowTitle;
        this.bgColor = bgColor;

        createScreen();
    }

    private void createScreen() {
        setSize(WIDTH, HEIGHT);
        setTitle(this.windowTitle);
        setLocationRelativeTo(null);
        // setIconImage()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    public void exit() {
        System.exit(1);
    }

    public static int getWid() {
        return WIDTH;
    }

    public static int getHei() {
        return HEIGHT;
    }

}
