package com.czh.po.client;

import com.czh.bo.LoginBo;
import com.czh.po.common.message.StatusMessage;
import com.czh.po.common.message.UpdateMessage;
import com.czh.utils.MsgUtils;
import com.czh.utils.UserUtils;

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
                ot.client.user = UserUtils.userLogin();
                ot.client.outputMsg = new UpdateMessage(ot.client.user);
                //发送登录信息
                oos.writeObject(ot.client.outputMsg);
                oos.flush();
                synchronized (this){
                    ot.wait(100);
                }
            }while(ot.client.inputMsg.loginBo==null);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ot.client.loginBo;
    }

    private void handle(Client client, ObjectOutputStream oos, String input){
        try{
            //判断是否输入注销命令
            //！！！警告：注销命令存在问题
            if(!"end".equals(input)){
                //非注销命令交给命令分析器处理
                client.outputMsg = MsgUtils.inputAnalyse(MsgUtils.inputSplit(input), client.user);
                //输入分析器将分析结果返回至交互对象
                if(client.outputMsg == null){
                    System.out.println("输入分析失败");
                }else{
                    //在交互对象中加入登录信息
                    client.outputMsg.loginBo = client.loginBo;
                    //发送登录信息给服务器端进行登录验证
                    oos.writeObject(client.outputMsg);
                    oos.flush();
                    oos.reset();
                }
            }else {
                //客户端发送注销信息后离线
                //但是这是输入线程还未停止，需要修改
                oos.writeObject(new StatusMessage(client.loginBo));
                oos.flush();
                oos.reset();
                client.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner send = new Scanner(System.in);
        String input;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(this.client.socket.getOutputStream());
            //获取接收到的登录信息
            this.client.outputMsg.loginBo = signIn(this, oos);
            while (!this.client.socket.isClosed()){
                //客户端输入
                do {
                    System.out.println("请输入想发送的信息");
                    input = send.nextLine();
                }while (input == null);
                handle(this.client, oos, input);
            }
            oos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
