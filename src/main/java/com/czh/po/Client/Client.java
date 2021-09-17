package com.czh.po.Client;


import com.czh.bo.LoginBo;
import com.czh.po.Common.Message.Message;
import com.czh.po.Common.User;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端主启动类
 * 直接运行即可
 * @author chenzhuohong
 */
public class Client {

    public Socket socket;
    public User user;
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
     * 启动客户端,连接相应端口,与服务器交互
     * @param address 客户端地址
     * @param port 客户端端口号
     */
    public static Client clientOnline(String address, int port){
        Client newClient = new Client(address, port);
        System.out.println("Client "+ newClient.socket.getLocalPort() +" online");
        System.out.println("正在登录");
//        客户端与服务器交互
        newClient.interactions();
        return newClient;
    }

    //    客户端与服务器进行交互的方法
    public void interactions() {
        OutputThread ot = new OutputThread(this);
        System.out.println(this.socket);
        ot.start();
        InputThread it = new InputThread(this);
        it.start();
    }

    public static void main(String[] args) {
        Client client1 = clientOnline("127.0.0.1", 6666);
    }
}

