package com.nju.edu.screen;

import com.nju.edu.control.GameController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Zyi
 */
public class RenderThread implements Runnable {

    private Thread thread = Thread.currentThread();
    /**
     * 游戏是否被退出
     */
    private boolean isExited = false;
    /**
     * 两次绘制之间相隔的时间
     */
    private final int interval;
    public GameController gameController;

    public RenderThread(GameController gameController) {
        this.gameController = gameController;
        // 计算出隔多久要刷新一次屏幕(单位为毫秒)
        interval = 1000 / this.gameController.getFps();

        // test
        System.out.println("[Render]Created");
        System.out.println("[Render]Render interval: " + this.interval + " ms");
    }

    @Override
    public void run() {
        System.out.println(this.thread.getName() + " start rendering");

        while (!isExited) {
            // 强制刷新
            this.gameController.repaint();
            try {
                // 每隔一定时间渲染一次，以实现稳定的fps
                TimeUnit.MILLISECONDS.sleep(interval);
            } catch (InterruptedException e) {
                System.out.println(this.thread.getName() + " error: " + e.toString());
                break;
            }
        }
    }
}
