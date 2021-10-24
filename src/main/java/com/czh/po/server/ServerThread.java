package com.czh.po.server;

import com.czh.bo.LoginBo;
import com.czh.po.common.message.Message;
import com.czh.service.MsgService;
import lombok.Getter;
import lombok.Setter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 服务器端为每一个连接的客户端新建一个处理线程ServerThread
 * 功能包括接收服务器发送的Message和发送对应的结果Message
 * @author chenzhuohong
 */
@Getter
@Setter
public class ServerThread extends Thread{

    private final Socket socket;

    private LoginBo loginBo;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    public String getUid(){
        return this.loginBo.getLoginUid();
    }

    public ObjectOutputStream getOutput(){
        return this.output;
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
        try{
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            while(!this.socket.isClosed()){
                Message msgTemp = (Message) this.input.readObject();
                MsgService.handleMsg(this, msgTemp);
            }
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println("线程异常");
        }
    }
}
