package com.czh.po.client;

import com.czh.bo.LoginBo;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.po.common.message.*;
import com.czh.service.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * 客户端输出给服务器端的线程类
 * @author chenzhuohong
 */
public class OutputCallable implements Callable<ReturnInfo> {

    private final Client client;

    public OutputCallable(Client client){
        this.client = client;
    }

    /**
     * 用户登录
     * @param ot 用户的输出线程
     * @param oos 输出流
     * @return 登录信息LoginBo
     */
    private LoginBo signIn(OutputCallable ot, ObjectOutputStream oos){
        try{
            // 不断输入帐号和密码，直至服务器端传过来了一个loginBo，代表登录成功
            do{
                ot.client.outputMsg = new UpdateMessage(UserService.userLogin());
                //发送登录信息
                oos.writeObject(ot.client.outputMsg);
                oos.flush();
                synchronized (this){
                    this.wait(2000);
                }
            }while(ot.client.inputMsg.getLoginBo()==null);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ot.client.loginBo;
    }

    /**
     * 客户端处理输入的命令
     * @param client 客户端
     * @param input 输入的命令
     * @return 处理结果
     */
    private Message handleInput(Client client, String input){
        //判断是否输入注销命令
        //！！！警告：注销命令存在问题
        if(!ClientCommand.SIGN_OUT.getCommand().equals(input)){
            //非注销命令交给命令分析器处理
            client.outputMsg = ClientInputHandler.inputAnalyse(
                    ClientInputHandler.inputSplit(input, ClientInputHandler.MSG_PATTERN));
            //输入分析器将分析结果返回至交互对象
            if(client.outputMsg == null){
                System.out.println("输入分析失败");
                return null;
            }else{
                //在交互对象中加入登录信息
                client.outputMsg.setLoginBo(client.loginBo);
                //如果是聊天信息, 就加入发送者uid
                if(MessageType.CHAT_TYPE.equals(client.outputMsg.getMsgType())){
                    ChatMessage message = (ChatMessage) client.outputMsg;
                    message.setSenderId(message.getLoginBo().getLoginUid());
                }
                //发送登录信息给服务器端进行登录验证
                return client.outputMsg;
            }
        }
        //客户端发送注销信息后离线
        return new StatusMessage(client.loginBo);
    }

    @Override
    public ReturnInfo call() {
        Scanner send = new Scanner(System.in);
        try {
            String input;
            ObjectOutputStream oos = new ObjectOutputStream(this.client.socket.getOutputStream());
            //获取接收到的登录信息
            this.client.outputMsg.setLoginBo(signIn(this, oos));
            while (!this.client.socket.isClosed()){
                //客户端输入
                do {
                    System.out.println("请输入想发送的信息");
                    input = send.nextLine();
                }while (input == null);
                //对输入的命令进行处理和打包
                Message message = handleInput(this.client, input);
                if(message!=null){
                    oos.writeObject(message);
                    oos.flush();
                    oos.reset();
                }
            }
            oos.close();
            return new ReturnInfo(StatusCode.SUCCESS_CODE, "输出线程运行结束");
        } catch (IOException e){
            e.printStackTrace();
        }
        return new ReturnInfo(StatusCode.ERROR_CODE, "输出线程异常结束");
    }
}
