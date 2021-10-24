package com.czh.po.client;


import com.czh.po.common.message.Message;
import com.czh.service.MsgService;

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

    @Override
    public void run() {
        try {
//            连接到的socket->字节流->对象输入流
            ObjectInputStream ois = new ObjectInputStream(this.client.socket.getInputStream());
            while(!this.client.socket.isClosed()) {
                if(this.client.socket.getInputStream()!=null){
//                    读取服务器端传递的交互对象
                    this.client.inputMsg = (Message) ois.readObject();
                    MsgService.handleMsg(this.client);
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
