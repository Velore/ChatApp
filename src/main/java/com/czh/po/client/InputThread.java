package com.czh.po.client;


import com.czh.po.common.message.InfoMessage;
import com.czh.po.common.message.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 客户端接收服务器端的输入线程类
 * @author chenzhuohong
 */
public class InputThread extends Thread{

    private final Client client;

    public InputThread(Client client){
        this.client = client;
    }

    private void handle(Client client){
        switch (client.inputMsg.getMsgType()){
            case 's':
                //接收到服务器发送的登录信息，传递给Client，Client再传递给输出线程
                client.loginBo = client.inputMsg.loginBo;
                System.out.println("登录信息" + client.inputMsg.loginBo);
                break;
            case 'c':
                //接收到聊天信息，输出在屏幕上，输出格式：[群组gid](发送时间)成员uid:{聊天内容}
                System.out.println(client.inputMsg);
                break;
            case 'i':
                //输出用户信息，群组信息，或群组的聊天信息，或者通知信息
                InfoMessage im = (InfoMessage) client.inputMsg;
                for(String s : im.getInfo()){
                    System.out.println(s+"\n");
                }
                break;
            default:
                System.out.println("msgType无效:"+client.inputMsg.getMsgType());
        }
    }

    @Override
    public void run() {
        try {
//            连接到的socket->字节流->对象输入流
            ObjectInputStream ois = new ObjectInputStream(this.client.socket.getInputStream());
            while(!this.client.socket.isClosed()) {
                if(this.client.socket.getInputStream()!=null){
//                    读取服务器端传递的交互对象
                    this.client.inputMsg = (Message) ois.readObject();
                    this.handle(this.client);
                }else{
                    ois.close();
                }
            }
            ois.close();
        } catch (EOFException e){
            System.out.println("EOF ending");
        } catch (IOException |ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}