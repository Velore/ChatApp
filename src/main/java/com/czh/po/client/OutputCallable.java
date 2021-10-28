package com.czh.po.client;

import com.czh.bo.LoginBo;
import com.czh.po.common.message.*;
import com.czh.service.UserService;
import com.czh.utils.ClientInputUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * 客户端输出给服务器端的线程类
 * @author chenzhuohong
 */
public class OutputThread extends Thread{

    private final Client client;

    public OutputThread(Client client){
        this.client = client;
    }

    /**
     * 用户登录
     * @param ot 用户的输出线程
     * @param oos 输出流
     * @return 登录信息LoginBo
     */
    private LoginBo signIn(OutputThread ot, ObjectOutputStream oos){
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

    private Message handle(Client client, ObjectOutputStream oos, String input){
        //判断是否输入注销命令
        //！！！警告：注销命令存在问题
        if(!"end".equals(input)){
            //非注销命令交给命令分析器处理
            client.outputMsg = ClientInputUtils.inputAnalyse(ClientInputUtils.inputSplit(input));
            //输入分析器将分析结果返回至交互对象
            if(client.outputMsg == null){
                System.out.println("输入分析失败");
                return null;
            }else{
                //在交互对象中加入登录信息
                client.outputMsg.setLoginBo(client.loginBo);
                //如果是聊天信息, 就加入发送者信息
                if(MessageType.CHAT_TYPE.equals(client.outputMsg.getMsgType())){
                    ChatMessage message = (ChatMessage) client.outputMsg;
                    message.setSenderId(message.getLoginBo().getLoginUid());
                }
                //发送登录信息给服务器端进行登录验证
                return client.outputMsg;
            }
        }
        //客户端发送注销信息后离线
        //但是这时输入线程还未停止，需要修改
        return new StatusMessage(client.loginBo);
    }

    @Override
    public void run() {
        Scanner send = new Scanner(System.in);
        String input;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(this.client.socket.getOutputStream());
            //获取接收到的登录信息
            this.client.outputMsg.setLoginBo(signIn(this, oos));
            while (!this.client.socket.isClosed()){
                //客户端输入
                do {
                    System.out.println("请输入想发送的信息");
                    input = send.nextLine();
                }while (input == null);
                oos.writeObject(handle(this.client, oos, input));
                oos.flush();
                oos.reset();
            }
            oos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
