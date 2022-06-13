package com.nju.edu.server;

import com.nju.edu.control.GameController;
import com.nju.edu.screen.GameScreen;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Zyi
 */
public class Server {

    private ServerSocketChannel channel;
    private Selector selector;

    // private Handler handler;

    private static final int PORT = 8080;
    private static final String ADDRESS = "localhost";
    private int playerNumber = 2;

    public Server() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bind() throws IOException {
        channel = ServerSocketChannel.open();
        // bind server socket channel to port
        channel.socket().bind(new InetSocketAddress(ADDRESS, PORT));
        channel.configureBlocking(false);
        // 连接selector
        channel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("server is ready, port = " + PORT);
    }

    public void startServer() throws IOException {
        this.bind();
        while (true) {
            // wait for events
            int nReady = selector.select();
            if (nReady == 0) {
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    this.accept(key);
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    // handler.channelQueueHashMap.put(sc,new ConcurrentLinkedDeque<>());
                    System.out.println("用户连接成功");
                }
                if (key.isReadable()) {
                    this.read(key);
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int count = sc.read(buffer);
                    buffer.flip();
                    // handler.handle(sc,buffer);
                    sc.register(selector,  SelectionKey.OP_WRITE);
                }
                if (key.isWritable()) {
                    this.write(key);
                    SocketChannel sc = (SocketChannel) key.channel();
                    // handler.write(sc);
                    sc.register(selector, SelectionKey.OP_READ);
                }
            }
            it.remove();
        }
    }

    /**
     * accept from client connection
     * @param key selector key
     * @throws IOException IO异常
     */
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        /*
         * Register channel with selector for further IO (record it for read/write
         * operations, here we have used read operation)
         */
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    /**
     * read from client
     * @param key selector key
     * @throws IOException IO异常
     */
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = channel.socket();
        ObjectInputStream objectInputStream;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                System.out.println(objectInputStream.readObject());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    /**
     * write to client
     * @param key selector key
     * @throws IOException IO异常
     */
    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket socket = channel.socket();


        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        while (true) {
            GameScreen gameScreen = new GameScreen("Calabash Game", Color.WHITE);
            GameController gameController = new GameController(30);
            gameScreen.add(gameController);
            gameController.startGame();
            gameScreen.setVisible(true);
        }
    }

    public static void main(String[] args) {
        try {
            new Server().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
