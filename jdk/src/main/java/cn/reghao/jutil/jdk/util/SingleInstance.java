package cn.reghao.jutil.jdk.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.logging.Logger;

/**
 * 监听一个端口，保证一台机器只有一个应用实例
 *
 * @author reghao
 * @date 2021-12-24 10:41:03
 */
public class SingleInstance implements Runnable {
    private static final Logger log = Logger.getLogger(SingleInstance.class.getName());

    private final int port;

    private SingleInstance(int port) {
        this.port = port;
    }

    public static void onlyOne(int port) {
        SingleInstance singleInstance = new SingleInstance(port);
        Thread t = new Thread(singleInstance);
        t.start();
    }

    @Override
    public void run() {
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
        try {
            log.info(String.format("listening port %s to ensure only one instance running on os", port));
            ServerSocket server = new ServerSocket();
            server.bind(socketAddress);
            server.accept();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
