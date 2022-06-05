package com.nju.edu.client;

import com.nju.edu.control.GameController;
import com.nju.edu.screen.GameScreen;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author Zyi
 */
public class Client implements Runnable {

    private static final String HOST_NAME = "localhost";
    private static final int PORT = 8080;
    private GameScreen gameScreen;

    private void startClient() throws IOException, InterruptedException {
        InetSocketAddress hostAddress = new InetSocketAddress(HOST_NAME, PORT);
        SocketChannel clientChannel = SocketChannel.open(hostAddress);

        System.out.println("Client... started");

        String TheadName = Thread.currentThread().getName();
        System.out.println("thread: " + TheadName + " start!");

        // TODO:向client write信息
        // 需要将葫芦娃的状态写到服务端上
        // 同时需要保证只有一个地方在生成怪物
        // clientChannel.write();

        clientChannel.close();
    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }
}
