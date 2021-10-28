package com.czh.po.client;

import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.po.common.message.Message;
import com.czh.service.MsgService;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;

/**
 * 客户端接收服务器端的输入线程类
 * @author chenzhuohong
 */
public class InputCallable implements Callable<ReturnInfo> {

    private final Client client;

    public InputCallable(Client client){
        this.client = client;
    }

    @Override
    public ReturnInfo call(){
        ObjectInputStream ois = null;
        try {
//            连接到的socket->字节流->对象输入流
            ois = new ObjectInputStream(this.client.socket.getInputStream());
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
            return new ReturnInfo(StatusCode.SUCCESS_CODE, "客户端运行结束");
        } catch (EOFException e){
            System.out.println("EOF ending");
            try{
                if(ois!=null){
                    ois.close();
                }
                System.exit(0);
            }catch (Exception e1){
                e.printStackTrace();
            }
            return new ReturnInfo(StatusCode.SUCCESS_CODE, "客户端运行结束");
        } catch (IOException |ClassNotFoundException e){
            e.printStackTrace();
        }
        return new ReturnInfo(StatusCode.ERROR_CODE, "客户端异常结束");
    }
}
