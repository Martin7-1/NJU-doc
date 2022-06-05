package com.nju.edu.util;

import com.nju.edu.control.GameController;

import java.util.concurrent.TimeUnit;

/**
 * @author Zyi
 */
public class TimeControl implements Runnable {

    private boolean isExited = false;

    @Override
    public void run() {
        while (!isExited) {
            GameController.addTime();
            try {
                TimeUnit.MILLISECONDS.sleep(40);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
