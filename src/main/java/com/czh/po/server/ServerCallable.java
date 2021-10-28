package com.czh.po.server;

import com.czh.bo.LoginBo;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.po.common.message.Message;
import com.czh.service.MsgService;
import lombok.Getter;
import lombok.Setter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * 服务器端为每一个连接的客户端新建一个处理对象ServerCallable,并开启线程进行处理
 * 功能包括接收服务器发送的Message和发送对应的处理结果Message
 * @author chenzhuohong
 */
@Getter
@Setter
public class ServerCallable implements Callable<ReturnInfo>{

    private final Socket socket;

    private LoginBo loginBo;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    public String getUid(){
        return this.loginBo.getLoginUid();
    }

    public ServerCallable(Socket socket) {
        this.socket = socket;
        try{
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ReturnInfo call() {
        try{
            while(!this.socket.isClosed()){
                Message msgTemp = (Message) this.input.readObject();
                MsgService.handleMsg(this, msgTemp);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ReturnInfo(StatusCode.ERROR_CODE, Arrays.toString(e.getStackTrace()));
        }
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "线程运行结束");
    }
}
