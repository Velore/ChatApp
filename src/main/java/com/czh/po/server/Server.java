package com.czh.po.server;

import com.czh.bo.LoginBo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 服务器端主启动类
 * 直接运行即可
 * @author chenzhuohong
 */
public class Server extends Thread{

    private ServerSocket serverSocket;

    private final ExecutorService threadPool;

    public static ArrayList<LoginBo> loginList;

    public static ArrayList<ServerCallable> threadList;

    public Server(int port){
        System.out.println("服务器启动中");
        loginList = new ArrayList<>();
        threadList = new ArrayList<>();
        threadPool = new ThreadPoolExecutor(
                5, 10, 1000,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ServerThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        try{
            this.serverSocket = new ServerSocket(port);
            System.out.println("等待客户端连接");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
//        建立监听socket
        Socket socket;
        try{
            while (true){
                //监听客户端的请求
                socket = this.serverSocket.accept();
                System.out.println(socket+"连接");
                //为连接的socket开启一个处理线程ServerCallable
                ServerCallable st = new ServerCallable(socket);
                threadList.add(st);
                //ServerThread开始运行
                threadPool.submit(new FutureTask<>(st));
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(this.serverSocket!=null){
                    this.serverSocket.close();
                }
                if(!threadPool.isShutdown()){
                    threadPool.shutdown();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(6666);
        server.start();
    }

}

