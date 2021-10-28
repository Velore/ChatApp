package com.czh.po.client;

import com.czh.bo.LoginBo;
import com.czh.po.common.message.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;

/**
 * 客户端主启动类
 * 直接运行即可
 * @author chenzhuohong
 */
public class Client {

    public Socket socket;
    public LoginBo loginBo;

    /**
     * 接收的Message
     */
    public Message inputMsg;

    /**
     * 发送的Message
     */
    public Message outputMsg;

    public Client(String host, int port){
        this.inputMsg = new Message();
        this.outputMsg = new Message();
        try{
            this.socket = new Socket(host, port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 启动客户端,连接相应端口,准备与服务器交互
     * @param address 客户端地址
     * @param port 客户端端口号
     */
    public static Client clientLunch(String address, int port){
        Client newClient = new Client(address, port);
        System.out.println("Client "+ newClient.socket.getLocalPort() +" online");
        System.out.println("正在登录");
//        客户端与服务器交互
        newClient.interact();
        return newClient;
    }

    /**
     * 客户端与服务器进行交互
     */
    public void interact() {
        System.out.println(this.socket);
        new Thread(new FutureTask<>(new OutputCallable(this))).start();
        new Thread(new FutureTask<>(new InputCallable(this))).start();
    }
    public static void main(String[] args) {
        clientLunch("127.0.0.1", 6666);
    }
}

